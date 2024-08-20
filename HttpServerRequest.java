public class HttpServerRequest {
    private String file = null;
    private String host = null;
    private boolean done = false;
    private int line = 0;

    public boolean isDone() { return done; }
    public String getFile() { return file; }
    public String getHost() { return host; }

    /*
	 * process the line, setting 'done' when HttpServerSession should
	 * examine the contents of the request using getFile and getHost
	 */
    public void process(String in){
        String parts[] = in.split(" ");

        //First part
        
    }
}
