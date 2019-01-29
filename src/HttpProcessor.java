import java.io.*;
import java.net.Socket;
import java.util.Date;

public class HttpProcessor implements Runnable, Lifecycle {
    private Connector connector;
    private Request request;
    private Response response;
    private Socket socket;
    private boolean aviliable = false;
    private boolean shutdown = false;
    private boolean keepalive = true;

    protected String ack = "";

    public HttpProcessor(Connector connector) {
        this.connector = connector;
        this.request = connector.createRequest();
        this.response = connector.createResponse();
    }

    @Override
    public void addLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public LifecycleListener[] findLifecycleListeners() {
        return new LifecycleListener[0];
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public void start() throws LifecycleException {
        Thread thread = new Thread(this, "HttpProcessor");
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void stop() throws LifecycleException {

    }

    @Override
    public void run() {
        boolean ok = true;
        while (!shutdown && keepalive) {
            BufferedReader bufferedReader = null;
            OutputStream outputStream = null;
            PrintWriter printWriter = null;
            Socket mSocket = null;
            try {
                mSocket = await();
                bufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "utf-8"));
                printWriter = new PrintWriter(new OutputStreamWriter(mSocket.getOutputStream(), "utf-8"), true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request.setBfr(bufferedReader);
            response.setOutputStream(printWriter);
//            if (!parseRequestLine())
//                break;
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Date:" + new Date());
            printWriter.println("Content-Type: text/html; charset=UTF-8");
            //printWriter.println("Transfer-Encoding: chunked");
            printWriter.println("Connection: keep-alive");
            printWriter.println("");
            printWriter.println("25\r\n");
            printWriter.println("This is the data in the first chunk");
//            printWriter.close();
            BufferedReader bufferedReader1 = null;
            try {
                bufferedReader1 = new BufferedReader(new FileReader("C:\\Users\\Y.Y-PC.000\\Desktop\\Google.html"), 1024 * 10);
                String s = "";
                while ((s = bufferedReader1.readLine()) != null) {
                    if (s.length() == 0)
                        break;
                    System.out.println(s.length());
                    //response.printWriter.println(Integer.toHexString(s.length() + 2) + "\r\n");
                    response.printWriter.println(s + "\r\n");
                }
                bufferedReader1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            printWriter.println("0\r\n");


            // response.printWriter.flush();

//            keepalive = false;
//            try {
//                mSocket.close();
//            }catch (IOException e){
//                e.printStackTrace();
//            }
            try {
                socket.shutdownOutput();

            }catch (IOException e){
                e.printStackTrace();
            }
            System.out.println("request over");
            ((HttpConnector) connector).recycle(this);
        }


    }

    private boolean parseRequestLine() {
        BufferedReader bfr = request.bfr;
        String line = "";
        do {
            try {
                line = bfr.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (line.trim().length() == 0);
        line = line.trim();
        String[] s = line.split(" ");
        if (s.length < 3) {
            response.printWriter.println("error request \r\n");
            return false;
        }
        request.requestLine = s;
        return true;

    }

    public synchronized void assign(Socket socket) {
        while (aviliable) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.socket = socket;
        aviliable = true;
        notifyAll();


    }

    private synchronized Socket await() {
        while (!aviliable) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Socket s = this.socket;


        notifyAll();
        aviliable = false;
        return s;

    }

}
