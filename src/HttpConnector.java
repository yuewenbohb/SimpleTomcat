import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class HttpConnector implements Connector, Lifecycle, Runnable {
    /**
     * basic param
     */
    protected int maxThread = 10;
    protected int minThread = 2;
    protected int activateThread = 0;
    protected int created = 0;

    protected String address = "127.0.0.1";
    protected int port = 8080;
    protected boolean enableLookups = false;
    protected int redirectPort = 8443;
    protected int connectorTimeout = 3000;

    protected boolean compression = false;
    protected String scheme = "http";

    protected Container container;

    protected Stack stack = new Stack();

    protected ServerSocketFactory factory;

    protected Set<LifecycleListener> listeners = new HashSet<>();

    /**
     * status
     */
    protected boolean initialized = false;
    protected boolean shutdown = false;

    @Override
    public Container getContainer() {
        return this.container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public boolean getEnableLookups() {
        return this.enableLookups;
    }

    @Override
    public void setEnableLookups(boolean enableLookups) {
        this.enableLookups = enableLookups;
    }

    @Override
    public ServerSocketFactory getFactory() {
        return this.factory;
    }

    @Override
    public void setFactory(ServerSocketFactory factory) {
        this.factory = factory;
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public int getRedirectPort() {
        return this.redirectPort;
    }

    @Override
    public void setRedirectPort(int redirectPort) {
        this.redirectPort = redirectPort;
    }

    @Override
    public String getScheme() {
        return this.scheme;
    }

    @Override
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public boolean getSecure() {
        return false;
    }

    @Override
    public void setSecure(boolean secure) {

    }

    @Override
    public Request createRequest() {

        return new Request(this);
    }

    @Override
    public Response createResponse() {
        return new Response(this);
    }

    @Override
    public void initialize() throws LifecycleException {
        if (initialized)
            throw new LifecycleException("connector already initialized");
        initialized = true;
        factory = ServerSocketFactory.getDefault();
        try {
            for (int i = 0; i < minThread; i++) {
                HttpProcessor processor = creatHttpProcessor();
                if (processor != null)
                    stack.push(processor);
            }
        } catch (LifecycleException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        listeners.add(listener);
    }

    @Override
    public LifecycleListener[] findLifecycleListeners() {
        return listeners.toArray(new LifecycleListener[]{});
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void start() throws LifecycleException {
        Thread thread = new Thread(this, "HttpConnector");
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void stop() throws LifecycleException {

    }

    public void recycle(HttpProcessor httpProcessor) {
        stack.push(httpProcessor);
        System.out.println("HttpProcessor recycle !");
        activateThread--;
    }

    private HttpProcessor newHttpProcessor() throws LifecycleException {

        if (activateThread < created) {
            activateThread++;
            return (HttpProcessor) stack.pop();
        }
        if (maxThread < 0) {
            activateThread++;
            return creatHttpProcessor();
        }
        System.out.println("HttpProcessor out of store!");
        return null;
    }

    private HttpProcessor creatHttpProcessor() throws LifecycleException {
        if (maxThread < 0 || created < maxThread) {
            HttpProcessor httpProcessor = new HttpProcessor(this);
            httpProcessor.start();
            created++;
            return httpProcessor;
        }

        return null;

    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;


        try {
            serverSocket = factory.createServerSocket(port, 2, InetAddress.getByName(address));
            // serverSocket.setSoTimeout(this.connectorTimeout);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!shutdown) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                System.out.println("request received !");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(socket.getInetAddress() + ":" + socket.getPort());
//            try {
//                socket.close();
//
//            }catch (IOException e){
//                e.printStackTrace();
//            }

            HttpProcessor httpProcessor = null;

            try {
                httpProcessor = newHttpProcessor();
                if (httpProcessor == null) {
                    try {
                        socket.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    continue;

                }
                System.out.println(socket.getInetAddress() + ":" + socket.getPort());
                httpProcessor.assign(socket);

            } catch (LifecycleException e) {
                e.printStackTrace();
            }

        }


    }
}
