# Integration samples
## contents:
- **README.md** _this file_
- **asyncapi**
    - **samples**
      - **java**
          - **MqttJavaClient** _java consumer example_
      - **node**
          - **mqtt-node-client** _node consumer example_
    - **yaml**
      - **asyncapi-public.html**  _AsyncApi web site_ 
      - **asyncapi-public.yaml**  _AsyncApi yaml file_

  
    
# Build and run java example
Change the following lines in _application.properties_
```
mqttHost=app.{TODO: tenant code here}.milkmantechnologies.com
mqttTopic=mds/tenants/{TODO: tenant code here}/notifications

```
with your tenant code.

Change the line in _application.properties_
```
mqttUsername=TODO: Insert here a new valid IdToken

```
with a new valid token. Please refer to the documentation on how to obtain a valid token.

Then, from a terminal, type the following commands:
```
cd asyncapi/samples/java/MqttJavaClient

./mvnw install  

./mvnw spring-boot:run
```

# Build and run node example

Change the following lines in _index.js_
```
const host = "app.{TODO: tenant code here}.milkmantechnologies.com";
const topic = "mds/tenants/{TODO: tenant code here}/notifications";
```
with your tenant code.

change the line in _index.js_
```
const token = "TODO: Insert here a new valid IdToken";
```
with a new valid token.  Please refer to the documentation on how to obtain a valid token.

Then, from a terminal, type the following commands:

```
cd asyncapi/samples/node/mqtt-node-client

npm update

node index.js
```


