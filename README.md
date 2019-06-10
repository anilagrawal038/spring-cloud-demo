# spring-cloud-demo


## app-config-repo 
   It could be local foder or a git repo that contains configuration files for all projects other thar app-config and being served by app-config service.<br/><br/>
    
    
## app-config
   Its a  Spring Cloud Configuration Server, that serves configuration files to different micro-services. And help us to manage configurations at single place.<br/><br/>
    
    
## app-discovery
   Its a Spring Cloud Eureka (Discovery) Server. It provides a way for all servers to be able to find each other. This server should be known to all other servuces. All services will register it self to discovery server and can find other services using discovery server.<br/><br/>
    
    
## app-gateway
   Its a Gateway server. This will act as a reverse proxy shuttling requests from clients to our back end servers. In this demo, we will route requests to app-service1 and app-service2 through gateway server.<br/><br/>
    
    
## app-service1
   Its a spring boot micro-service that exposes rest API /service1/status1. Its a client application for discovery server and a node for gateway server.<br/><br/>
    
    
## app-service2
   Its a spring boot micro-service that exposes rest API /service2/status2. Its a client application for discovery server and a node for gateway server.<br/><br/>


## Note
  * We must have to start app-config service at very first, as it will provide configurations to other services.
  * app-discovery service will be next service that should be started after app-config. As It will help other services to find Configuration Server. Also we have to wait until Configuration Server will register it self to Discovery Server before starting other services.
  * After starting app-config and app-discovery, we can start other services in any order.
  * app-gateway service will find the services by the help of Discovery Server and act as a reverse proxy for them and provide access to underneath services by configured mapping.
  * we can scale app-service1 and app-service2 to any no of instances. app-gateway service will balance the load for these instances automatically.
  * We can access the rest APIs for app-service1 through app-gateway as below<br/>
        http://gateway-server-ip:8080/gateway/s1/service1/status1
  * We can access the rest APIs for app-service2 through app-gateway as below<br/>
        http://gateway-server-ip:8080/gateway/s2/service2/status2
  * We can change the prefix and path for services for app-gateway using file app-config-repo/app-gateway.properties
