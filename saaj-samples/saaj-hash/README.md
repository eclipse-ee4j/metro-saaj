
Hash service
-----------------

This is a sample application using the SAAJ API. 

The Hashing service creates hash in every digest type available. 

The hashes can be returned either as attachments to a SAAJ message or 
within the SOAPBody depending on which option is chosen 
in the radio button from the application's index.html.

Running the Sample
-------------------

1. 
```shell script
mvn jetty:run -Pstaging
```

2. Open in browser http://localhost:8080