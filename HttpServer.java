import java.net.*;
import java.io.*;
import java.util.*;

class HttpServer{
    public static void main(String[] args) {
        //Print a line to the output to indicate the programing is starting
        System.out.println("Web Server Starting...");
        
        try {
            //Initialise the serverSocket with a hard-coded port number
            ServerSocket serverSocket = new ServerSocket(55535);

            //Loop around, accepting new connections as they arrive
            while(true){
                //Wait for new connection request and accept it
                Socket socket = serverSocket.accept();
                //Indicate connection has been made and its IP address
                System.out.println("Connected (:");
                System.out.println("IP address is " + socket.getInetAddress());

                //Spawn a thread to then process that connection
                HttpServerSession sessionThread = new HttpServerSession(socket);
                sessionThread.start();

            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

class HttpServerSession extends Thread{
    //Declare private variables
    private Socket privateSocket;
    private BufferedReader reader;
    private BufferedOutputStream out;

    public HttpServerSession(Socket socket){
        this.privateSocket = socket;
    }

    public void run(){
        try{
            //Declare a BufferedReader that is connected to the socket's InputStream
            reader = new BufferedReader(new InputStreamReader(privateSocket.getInputStream()));
            //Declare a BufferedOutputStream and parse in the outputStream from the private socket
            out = new BufferedOutputStream(privateSocket.getOutputStream());
            //Create a string variable to store each line
            String line = reader.readLine();
            //Add an empty line
            System.out.println("");

            while(line != null && line != "" && !line.isEmpty()){
                //Prints out the request to the console
                System.out.println(line);
                line = reader.readLine();
            }
            // sleep idle for 2s
			Thread.sleep(2000);
            println(out, "HTTP/1.1 200 OK");
            println(out, "");
            println(out, "Hello World");
            
            

            privateSocket.close();
        } catch(Exception e){
            System.err.println(e.getMessage());
        }
    }

    private boolean println(BufferedOutputStream bos, String s){
        String news = s + "\r\n";
        byte[] array = news.getBytes();
        try {
            bos.write(array, 0, array.length);
        } catch(IOException e) {
            return false;
        }
        return true;
    }
}