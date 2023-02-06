# networks
this repository stores the 2 practicals implemented on networks labs
### p0 - socket tutorial
the first practical aims to some basic concepts, specifically, we are asked to implement the following:
- implement an application on `es.udc.redes.tutorial.copy.Copy` for copying a file (with either text or binary content) in the local disk using data streams

    the application will recieve two input parameters:
    `java es.udc.redes.tutorial.copy.Copy <source file> <destination file>`
- implement an application for obtaining the main properties of a file:
    - size
    - last modification date
    - name
    - extension
    - file type
    - absolute path
    
    the application will recieve one input parameter:
    `java es.udc.redes.tutorial.info.Info <source file>`
- implement a udp echo server able to respond the request sent by the client already implemented, modifying `es.udc.redes.tutorial.udp.server.UdpServer.java`
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

- tcp echo server
### p1 - web server
we are asked to develop a basic java web server capable of interacting with a web browser to navigate through a web site

