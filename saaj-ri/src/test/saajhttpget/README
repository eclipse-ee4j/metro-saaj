1. Goto <saajhttpget>

2. Edit build.properties and do the following changes. 
   
   #Only one should be active at a time. If you want to run the tests
   #using appserver then sjsas.home should be active. Other two property
   #should be disabled.

   sjsas.home=<where appserver is installed>
   #sjsws.home=<where webserver is installed>
   #tomcat.home=<where tomcat is installed>

   if u are using appserver container.

   Also Change the followings  
 
   javahome=/javahome

   endpoint.host=localhost
   endpoint.port=8080

   VS.DIR=<where webserver is installed>

   user=admin
   password=admin123

3. Start Appserver / Tomcat / Webserver  

4. From <saajhttpget> Execute : ant run-client to execute the sample.
   For Appserver8 use <appserver>/bin/asant instead of ant. 
