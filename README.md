# networks
this repository stores the 2 practicals implemented on networks labs
### p0 - socket tutorial
the first practical aims to some basic concepts, specifically, we are asked to implement the following:

#### copy
implement an application on `es.udc.redes.tutorial.copy.Copy` for copying a file (with either text or binary content) in the local disk using data streams

the application will recieve two input parameters:
`java es.udc.redes.tutorial.copy.Copy <source file> <destination file>`

#### properties
implement an application for obtaining the main properties of a file:
- size
- last modification date
- name
- extension
- file type
- absolute path

the application will recieve one input parameter:
`java es.udc.redes.tutorial.info.Info <source file>`

#### udp echo server
implement a udp echo server able to respond the request sent by the client already implemented, modifying `es.udc.redes.tutorial.udp.server.UdpServer.java`
the server will behave as follows:
- create a `DatagramSocket` associated to a specific port number
- set a maximum timeout for the socket
- create an infinite loop executing the following steps:
    - prepare a datagram to receive the message from the client (create a new `DatagramPacket` objet for each message
    - receive a message
    - prepare the datagram to answer the client (the received datagram contains ip address & port number of the client)
    - send response message
 
once the udp echo server is implemented, it can be checked as follows:
`java es.udc.redes.tutorial.udp.server.UdpServer 5000`
the server will wait for incoming request for as long as specified on timeout
`java es.udc.redes.tutorial.udp.client.UdpClient localhost 5000 "testing my udp server"`
the following messages should appear:
- on server terminal:
    ```
    SERVER: received testing my udp server from /127.0.0.1:ABCDEF
    SERVER: sending testing my udp server to /127.0.0.1:ABCDEF
    ```
- on client terminal
    ```
    CLIENT: sending testing my udp server to localhost/127.0.0.1:5000
    CLIENT: recieved testing my udp server from /127.0.0.1:5000
    ```

#### tcp echo server
implement a multithreaded tcp echo server on `es.udc.redes.tutorial.tcp.serverr.TcpServer.java`
to make this exercise easier, first a monothread version of the server will be implemented:
- create a `serverSocket`
- set a maximum timeout
- create an infinite loop executing the following steps:
    - invoke the `accept()` method of the server socket
    - set input & output streams for the new socket
    - revieve the message from client
    - send the message to client
    . close the streams & connection associated to the socket created in the `accept()` method
once this version is implemented, it can be checked as follows:
`java es.udc.redes.tutorial.tcp.server.MonoThreadTcpServer 5000`
the server will wait for incoming requests so from another terminal:
`java es.udc.redes.tutorial.tcp.client.TcpClient localhost 5000 "testing my tcp server"`
the following messages should appear:
- on server terminal:
    ```
    SERVER: received testing my tcp server from /127.0.0.1:ABCDEF
    SERVER: sending testing my tcp server to /127.0.0.1:ABCDEF
    ```
- on client terminal
    ```
    CLIENT: sending testing my tcp server to localhost/127.0.0.1:5000
    CLIENT: recieved testing my tcp server from /127.0.0.1:5000
    ```
to implement the multithread server, implement a new class `ServerThread` tht extends `Thread`, this class will be in charge of processing each connection request like:
- create a ServerSocket associated to a port number
- set timeout
- infinite loop that executes:
    - invoke the `accept()` method that will create the communication socket
    - create a new `ServerThread` passing the new socket as a parameter
    - start execution of the thread using `start()`
once the multithread server is implemented, it can be checked by executing:
`java es.udc.redes.tutorial.tcp.server.MonoThreadTcpServer 5000`
in another terminal run:
`nc loclahost 5000`
and then, on another one:
`java es.udc.redes.tutorial.tcp.client.TcpClient localhost 5000 "testing my tcp server"`
the following should happen:
- the server receives the message from the client
- it responds to the client with the same message
- the client receives the serverr response, just like the monothread server

### p1 - web server
we are asked to develop a basic java web server capable of interacting with a web browser to navigate through a web site

the requirements for the web server are as follows:
- use HTTP 1.0 protocol, which implies that independent HTTP request will be ginerated for each web object
- the server must be multithread to be able to process multipple requests in parallel
- process GET & HEAD methods
- process if-modified-since in a request

the web server will receive and process an HTTP request and prepare & send an HTTP response
regarding the status line of the HTTP response, the web server must implement the following codes:
- 200 OK
- 304 Not Modified: as reply to an if-modified-since
- 400 Bad Request: ill-formatted HTTP request
- 404 Not Found: the resource requested does not exist in the server

regarding the header lines of the HTTP response, the web server must, at least, implement the following:
- date: date & time when the response was created & sent
- server: specifies web server name that attended the request
- content-length: number of bytes of the resource sent
- content-type: type of resource contained in the entity body
    - text/html
    - text/plain
    - image/gif
    - image/png
    - application/octet-stream
- last modifiied: indicates the date & time when the resource was modiified for the last time

in order to correctly process a GET request with the if-modified-since header option, if the resource requested was last modified afteer the date & time specified by the option, then the resource must be sent normally, otherwise, the server must reply a status code 304 Not Modified in the HTTP header & no resource is sent

the project includes the `p1-files` folder that contains utility files for the development of the web server

a java application test `p1-files/httptester.jar` is provided to verify proper behaviour of the webb server
it is executed as follows:
``` java -jar httptester.jar <host> <port> [<0-9>] ```
