//Name: Justin Poutoa
//ID: 1620107

import java.net.*;
import java.io.*;

class HttpServer{
    /**
     * This is the main method - entry point.
     */
    public static void main(String[] args) {
        //Print a line to the output to indicate the programing is starting
        System.out.println("Web Server Starting...");
        try {
            try (//Initialise the serverSocket with a hard-coded port number
            ServerSocket serverSocket = new ServerSocket(55535)) {
                //Loop around, accepting new connections as they arrive
                while(true){
                    //Wait for new connection request and accept it
                    Socket socket = serverSocket.accept();
                    //Indicate connection has been made and its IP address
                    System.out.println("");
                    System.out.println("Connected (:");
                    System.out.println("Connection received from " + socket.getInetAddress());
                    //Spawn a thread to then process that connection
                    HttpServerSession sessionThread = new HttpServerSession(socket);
                    sessionThread.start();
                }
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

    /**
     * This is the constructor of the class.
     * @param socket endpoint for communication between client and server
     */
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
            //While isDone is false
            while(!request.isDone()){
                line = reader.readLine();
                // System.out.println(line); //Output for debugging
                request.process(line);     //Call the process method using the HttpServerRequest
            }
            //Get the host and the file
            String host = request.getHost();
            String file = request.getFile();
            //If host is null, set it to "localhost"
            if(host == null){
                host = "localhost";
            }
            //Make a filepath
            String filePath = host + "/" + file;
            //Declare the contentType variable
            String contentType = getContentType(file);
            try(FileInputStream fileInputStream = new FileInputStream(filePath)){
                //Log the file found
                System.out.println("File found: " + filePath);
                //Send 200 OK response
                sendResponse("HTTP/1.1 200 OK");
                //Send contentType header
                sendResponse("Content-Type: " + contentType);
                //Add an empty line
                sendResponse("");
                //Declare a byteArray with a fixed size
                byte[] byteArray = new byte[8192]; //this is 8KB
                int bytesRead;
                //While the fileInputStream is reading to the end of the file
                while((bytesRead = fileInputStream.read(byteArray)) != -1){
                    //Print it out
                    out.write(byteArray, 0, bytesRead);
                    //Add method to make it go slow when sending a file back
                    sleep(1000);
                }
                //Forces everything to be written to the output stream
                out.flush();
            } catch(FileNotFoundException e){
                //Log the file not found
                System.out.println("404 File Not Found: " + filePath);
                String notFoundMessage = file + " not found";
                //Handle file not found (send 404 response)
                sendResponse("HTTP/1.1 404 Not Found");
                sendResponse("Content-Type: text/plain; charset=UTF-8");
                sendResponse("Content-Length: " + (notFoundMessage.length() + 26));
                sendResponse(""); //End of headers
                //Send the HTML content
                sendResponse("404 Not Found");
            } catch(IOException exception){
                System.err.println("Error reading file: " + exception.getMessage());
            }
        } catch(Exception e){
            System.err.println(e.getMessage());
        } finally { //This is to make sure that everything is closed after beind used
            try {
                if (privateSocket != null && !privateSocket.isClosed()) {
                    privateSocket.close();
                }
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

    /**
     * This method determines the content type based on the file extension
     * @param file the name of the file
     * @return the content type string
     */
    private String getContentType(String file) {
        if (file.endsWith(".html")) {
            return "text/html; charset=UTF-8";
        } else if (file.endsWith(".png")) {
            return "image/png";
        } else if (file.endsWith(".jpg") || file.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (file.endsWith(".gif")) {
            return "image/gif";
        } else if (file.endsWith(".css")) {
            return "text/css";
        } else if (file.endsWith(".js")) {
            return "application/javascript";
        } else {
            return "application/octet-stream"; //Default binary type
        }
    }
}
