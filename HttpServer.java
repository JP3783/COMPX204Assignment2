import java.net.*;
import java.io.*;
import java.util.*;

class HttpServer{
    public static void main(String[] args) {
        //Print a line to the output to indicate the programing is starting
        System.out.println("Web Server Starting");
        
        try {
            //Initialise the serverSocket
            ServerSocket serverSocket = new ServerSocket(0);

            //This is just here to make sure we get a port number between 50,000 and 65535
            if(serverSocket.getLocalPort() > 65535 || serverSocket.getLocalPort() < 50000){
                System.out.println("port number invalid");
                serverSocket.close();
                return;
            }

            while(true){
                //Wait for new connection request and accept it
                Socket socket = serverSocket.accept();
                //Spawn a thread to then process that connection
                HttpServerSession sessionThread = new HttpServerSession(socket);

                serverSocket.close();
            }

            // //Prints to the output so I can see what port number it is getting
            // System.out.println("port number: " + serverSocket.getLocalPort());
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

class HttpServerSession extends Thread{
    private Socket clientSocket;
    private BufferedReader reader;

    public  HttpServerSession(Socket s){
        this.clientSocket = s;
    }

    public void run(){
        try{
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while(reader.readLine() != null){
                
            }
        } catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
}