public class HttpServerRequest {
    private String file = null;
    private String host = null;
    private boolean done = false;
    private int lineNumber = 0;
    private String filename = null;

    public boolean isDone() { return done; }
    public String getFile() { return file; }
    public String getHost() { return host; }

    /*
	 * process the line, setting 'done' when HttpServerSession should
	 * examine the contents of the request using getFile and getHost
	 */
    public void process(String line){
        //If it is the first line
        if(lineNumber == 0){
            //Split the first line into parts
            String parts[] = line.split(" ");        

            //Check if it contains 3 parts and if it contains the "GET" method
            if(parts.length == 3 && parts[0].compareTo("GET") == 0){
                //substring() method: returns everything after the 1st character in the string
                //Basically removes the leading "/"
                filename = parts[1].substring(1);
                //If filename is empty, set filename to index.html
                if(filename.isEmpty()){
                    filename = "index.html";
                }
            }
        } else if(line.startsWith("Host: ")){
            //Set host
            host = line.substring(6);
        }
        //Increment the line number
        lineNumber++;
        //Set done to true
        if(line.isEmpty()){
            done = true;
        }
    }
}
