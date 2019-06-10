package com.san.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ExtendedRandomAccessFile extends RandomAccessFile {

	private long _reverseFilePointer = -1;
	private final static int _reverseLineMaxBufferSize = 256;

	public ExtendedRandomAccessFile(String name, String mode) throws FileNotFoundException {
		super(name, mode);
	}

	public ExtendedRandomAccessFile(File file, String mode) throws FileNotFoundException {
		super(file, mode);
	}

	public void setReverseFilePointer(long pointer) {
		if (pointer > 0) {
			this._reverseFilePointer = pointer;
		}
	}

	public boolean hasMorePreviousLines() {
		return _reverseFilePointer > -2;
	}

	// Note: Should synchronized if needed.
	// Implemented to traverse a file in reverse order in a single thread.
	// Use only one of readLine/readPreviousLine at one time while reading any file
	public final String readPreviousLine(boolean restart) throws IOException {
		if (_reverseFilePointer == -1 || restart) {
			_reverseFilePointer = length() - 1;
		}
		if (_reverseFilePointer < 0) {
			return null;
		}
		String line = null;
		long startPointer = _reverseFilePointer - _reverseLineMaxBufferSize + 1;
		startPointer = startPointer > 0 ? startPointer : 0;
		int reverseLineBufferSize = (int) (_reverseFilePointer - startPointer) + 1;
		byte[] buffer = new byte[reverseLineBufferSize];
		seek(startPointer);
		int readSize = read(buffer, 0, reverseLineBufferSize);
		int byteCounter = readSize;
		int c = -1;
		boolean endOfLine = false;

		while (!endOfLine && byteCounter > 0) {
			c = buffer[--byteCounter];
			switch (c) {
			case -1:
			case '\n':
				endOfLine = true;
				break;
			case '\r':
				endOfLine = true;
				break;
			}
		}
		// In case line is longer than given length variable endOfLine will be false, lets break the line
		endOfLine = true;
		if (endOfLine) {
			line = new String(buffer, byteCounter + 1, readSize - byteCounter - 1);
			_reverseFilePointer = startPointer + byteCounter;

			while (endOfLine && _reverseFilePointer > 0) {
				seek(--_reverseFilePointer);
				c = read();
				--_reverseFilePointer;
				switch (c) {
				case -1:
				case '\n':
				case '\r':
					break;
				default:
					endOfLine = false;
					_reverseFilePointer = getFilePointer();
				}

			}
			if (endOfLine) {
				_reverseFilePointer = -2;
			}
		}
		return line;
	}

}
