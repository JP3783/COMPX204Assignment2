import java.net.*;
import java.io.*;
import java.util.*;

class HttpServer{
    public static void main(String[] args) {
        System.out.println("Web Server Starting");
        
        try {
            ServerSocket serverSocket = new ServerSocket(0);

            if(serverSocket.getLocalPort() > 65535 || serverSocket.getLocalPort() < 50000){
                System.out.println("port number invalid");
                serverSocket.close();
                return;
            }

            System.out.println("port number: " + serverSocket.getLocalPort());
            serverSocket.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}