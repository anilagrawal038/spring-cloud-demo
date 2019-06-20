CD .\..\dockerization-scripts

MKDIR resources

call mvn -f .\..\app-config package
COPY /Y .\..\app-config\target\app-config-1.0.jar .\..\dockerization-scripts\resources
Xcopy /Y /E /I .\..\app-config-repo .\..\dockerization-scripts\resources\app-config-repo

call mvn -f .\..\app-discovery package
COPY /Y .\..\app-discovery\target\app-discovery-1.0.jar .\..\dockerization-scripts\resources

call mvn -f .\..\app-gateway package
COPY /Y .\..\app-gateway\target\app-gateway-1.0.jar .\..\dockerization-scripts\resources

call mvn -f .\..\app-service1 package
COPY /Y .\..\app-service1\target\app-service1-1.0.jar .\..\dockerization-scripts\resources

call mvn -f .\..\app-service2 package
COPY /Y .\..\app-service2\target\app-service2-1.0.jar .\..\dockerization-scripts\resources


docker image rm -f app-config
docker build ./ --build-arg APP_JAR_FILE=./resources/app-config-1.0.jar --build-arg APP_CONFIG_REPO=./resources/app-config-repo -f Dockerfile-config -t app-config --compress
@REM docker image save app-config -o ./resources/app-config.tar

docker image rm -f app-discovery
docker build ./ --build-arg APP_JAR_FILE=./resources/app-discovery-1.0.jar -f Dockerfile-app -t app-discovery --compress
@REM docker image save app-discovery -o ./resources/app-discovery.tar

docker image rm -f app-gateway
docker build ./ --build-arg APP_JAR_FILE=./resources/app-gateway-1.0.jar -f Dockerfile-app -t app-gateway --compress
@REM docker image save app-gateway -o ./resources/app-gateway.tar

docker image rm -f app-service1
docker build ./ --build-arg APP_JAR_FILE=./resources/app-service1-1.0.jar -f Dockerfile-app -t app-service1 --compress
@REM docker image save app-service1 -o ./resources/app-service1.tar

docker image rm -f app-service2
docker build ./ --build-arg APP_JAR_FILE=./resources/app-service2-1.0.jar -f Dockerfile-app -t app-service2 --compress
@REM docker image save app-service2 -o ./resources/app-service2.tar