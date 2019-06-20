@REM docker load -i ./resources/app-config.tar
@REM docker load -i ./resources/app-discovery.tar
@REM docker load -i ./resources/app-gateway.tar
@REM docker load -i ./resources/app-service1.tar
@REM docker load -i ./resources/app-service2.tar

kubectl --namespace app-namespace delete service app-config-service
kubectl --namespace app-namespace delete deployment app-config-deployment

kubectl --namespace app-namespace delete service app-discovery-service
kubectl --namespace app-namespace delete deployment app-discovery-deployment

kubectl --namespace app-namespace delete service app-gateway-service
kubectl --namespace app-namespace delete deployment app-gateway-deployment

kubectl --namespace app-namespace delete service app-service1-service
kubectl --namespace app-namespace delete deployment app-service1-deployment

kubectl --namespace app-namespace delete service app-service2-service
kubectl --namespace app-namespace delete deployment app-service2-deployment

kubectl apply -f D:\workspace\spring-cloud\dockerization-scripts\kubernetes-configuration.json

@REM kubectl --namespace app-namespace get pods
@REM kubectl --namespace app-namespace get deployments
@REM kubectl --namespace app-namespace get services

@REM minikube --namespace app-namespace service --url=true app-config-service
@REM minikube --namespace app-namespace service --url=true app-discovery-service
@REM minikube --namespace app-namespace service --url=true app-gateway-service
@REM minikube --namespace app-namespace service --url=true app-service1-service
@REM minikube --namespace app-namespace service --url=true app-service2-service

@REM kubectl --namespace app-namespace -it exec app-config-deployment* -- /bin/sh
@REM kubectl --namespace app-namespace logs app-config-deployment* -c app-config-init
@REM kubectl --namespace app-namespace describe pod app-config-deployment*
@REM kubectl --namespace app-namespace scale deployment app-gateway-deployment --replicas=0