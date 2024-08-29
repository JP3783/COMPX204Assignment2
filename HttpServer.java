import java.net.*;
import java.io.*;

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

    /**
     * This is the run method which executes the communication with the server. Includes reading the response and outputing it to the console.
     */
    public void run(){
        //Declare a HttpServerRequest instance
        HttpServerRequest request = new HttpServerRequest();
        try{
            //Declare a BufferedReader that is connected to the socket's InputStream
            reader = new BufferedReader(new InputStreamReader(privateSocket.getInputStream()));
            //Declare a BufferedOutputStream and parse in the outputStream from the private socket
            out = new BufferedOutputStream(privateSocket.getOutputStream());
            //Create a string variable to store each line
            String line;
            //Add an empty line
            System.out.println("");
            // //Loop through response to print it to the output
            // while((line = reader.readLine()) != null && !line.isEmpty()){
            //     System.out.println(line);
            //     //Call the process method using the HttpServerRequest
            //     request.process(line);
            // }

            while(request.isDone() == false){
                line = reader.readLine();
                System.out.println(line); //Just so I can see it outputs
                request.process(line);
            }

            //Send the 200 message
            sendResponse("HTTP/1.1 200 OK");
            sendResponse("Content-Type: text/plain; charset=UTF-8");
            sendResponse("Content-Length: 11"); // Length of "Hello World"
            sendResponse(""); // Empty line to end the header
            //Send the "Hello World" text
            sendResponse("Hello World");

            //Close the resources
            out.flush();
            privateSocket.close();
        } catch(Exception e){
            System.err.println(e.getMessage());
        } finally { //This is to make sure that everything is closed after beind used
            try {
                if (reader != null) reader.close();
                if (out != null) out.close();
                if (privateSocket != null && !privateSocket.isClosed()) privateSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    /**
     * A method that mimics the println method but follows the HTTP requirements
     * @param response message to send 
     * @throws IOException any IOExceptions which might occur
     */
    private void sendResponse(String response) throws IOException{
        String formattedResponse = response + "\r\n";
        byte[] responseBytes = formattedResponse.getBytes("UTF-8");
        out.write(responseBytes);
    }
}
