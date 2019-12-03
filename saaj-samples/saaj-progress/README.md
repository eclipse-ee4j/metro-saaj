Progress Server
---------------
Whenever it receives a message from client, it sends n number of progress
messages to the client

Progress Client
---------------
Sends a message and listens for 5 progress messages from server.

Steps to run the sample :
------------------------------------------

1. Run server:

```shell script
    mvn exec:java@server -Pstaging
```

2. While server is up run the client:

```shell script
    mvn exec:java@client -Pstaging
```
