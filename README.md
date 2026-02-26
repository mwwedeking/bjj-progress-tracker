# bjj-progress-tracker

# Build & run
First clone the repo and install any required dependencies 

Then, create .env file and add corresponding environment variables 

Then, cd to the 'demo' directory (project root) 

Then, run the following command(s): 
mvn clean package && mvn spring-boot:run 

Note: 
API endpoints may be access through LocalHost(Port 8080) or using CURL operations in another terminal window instance. 

LocalHost browser example of getting all techniques: 
Paste in web browser: 
http://localhost:8080/api/techniques 

Curl example of creating a new technique: 
Paste in a separate terminal from where spring boot application is running: 
curl -X POST http://localhost:8080/api/techniques \
  -H "Content-Type: application/json" \
  -d '{
    "name":"example-name",
    "position":"example-postion",
    "numFinishes":example-num-finishes(typeInt),
    "numTaps":example-num-taps(typeInt)
  }'