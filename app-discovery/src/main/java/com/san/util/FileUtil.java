package com.san.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {

	static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	public static void makeZip(String sourceFolder, String zipPath) {
		File zipFile = new File(zipPath);
		zipFile.getParentFile().mkdirs();
		zipFile.delete();
		List<String> fileList = new ArrayList<String>();
		generateFileList(fileList, new File(sourceFolder), sourceFolder);
		zipIt(fileList, sourceFolder, zipPath, zipFile.getName());
	}

	public static byte[] fetchFileBytes(String filePath) throws IOException {
		FileInputStream fis = null;
		byte[] data = null;
		try {
			fis = new FileInputStream(new File(filePath));
			data = new byte[fis.available()];
			fis.read(data);
		} catch (IOException e) {
			logger.error("Exception occured while raeding file : " + filePath, e);
			throw e;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					logger.error("Exception occured while closing file : " + filePath, e);
				}
			}
		}
		return data;
	}

	private static void zipIt(List<String> fileList, String sourceFolder, String zipFile, String zipName) {
		byte[] buffer = new byte[1024];
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(fos);

			FileInputStream in = null;

			for (String file : fileList) {
				ZipEntry ze = new ZipEntry(zipName + File.separator + file);
				zos.putNextEntry(ze);
				try {
					in = new FileInputStream(sourceFolder + File.separator + file);
					int len;
					while ((len = in.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
				} finally {
					in.close();
				}
			}

			zos.closeEntry();

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void generateFileList(List<String> fileList, File node, String sourceFolder) {
		if (node.isFile()) {
			fileList.add(generateZipEntry(node.toString(), sourceFolder));
		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(fileList, new File(node, filename), sourceFolder);
			}
		}
	}

	private static String generateZipEntry(String file, String sourceFolder) {
		return file.substring(sourceFolder.length() + 1, file.length());
	}

	public static String readLine(FileChannel fc) throws IOException {
		ByteBuffer buffers = ByteBuffer.allocate(128);
		long lastPos = fc.position();
		if (fc.read(buffers) > 0) {
			byte[] data = buffers.array();
			boolean foundTmpTerminator = false;
			boolean foundTerminator = false;
			long endPosition = 0;
			for (byte nextByte : data) {
				endPosition++;
				switch (nextByte) {
				case -1:
					foundTerminator = true;
					break;
				case (byte) '\r':
					foundTmpTerminator = true;
					break;
				case (byte) '\n':
					foundTmpTerminator = true;
					break;
				default:
					if (foundTmpTerminator) {
						endPosition--;
						foundTerminator = true;
					}
				}
				if (foundTerminator) {
					break;
				}
			}
			fc.position(lastPos + endPosition);
			String line = new String(data, 0, (int) endPosition);
			if (foundTerminator || line == null || line.length() < 1) {
				return line;
			} else {
				return line + readLine(fc);
			}
		}
		return null;
	}

	public static String readPreviousLine(FileChannel fc) throws IOException {
		ByteBuffer buffers = ByteBuffer.allocate(128);
		long lastPos = fc.position();
		if (lastPos < 1) {
			return null;
		}
		boolean specialCase = false;
		if ((lastPos - 128) > 0) {
			fc.position(lastPos - 128);
		} else {
			fc.position(0);
			specialCase = true;
		}
		int readBytes = fc.read(buffers);
		if (readBytes > 0) {
			byte[] data = buffers.array();
			boolean foundTmpTerminator = false;
			boolean foundTerminator = false;
			long startPosition = readBytes - 1;
			int counter = readBytes - 1;
			if (specialCase) {
				counter = (int) lastPos - 1;
			}
			for (; counter >= 0; counter--) {
				byte nextByte = data[counter];
				switch (nextByte) {
				case (byte) '\r':
					if (foundTmpTerminator) {
						foundTerminator = true;
					}
					break;
				case (byte) '\n':
					if (foundTmpTerminator) {
						foundTerminator = true;
					}
					break;
				default:
					foundTmpTerminator = true;
				}
				startPosition = counter;
				if (foundTerminator) {
					break;
				}
			}
			String line = null;
			if (specialCase) {
				fc.position(lastPos - (lastPos - startPosition));
				line = new String(data, (int) startPosition + 1, ((int) lastPos - (int) startPosition - 1));
			} else {
				fc.position(lastPos - (readBytes - startPosition));
				line = new String(data, (int) startPosition + 1, (readBytes - (int) startPosition - 1));
			}
			if (foundTerminator || fc.position() < 1 || line == null || line.length() < 1) {
				return line;
			} else {
				return readPreviousLine(fc) + line;
			}
		}
		return null;
	}

}
