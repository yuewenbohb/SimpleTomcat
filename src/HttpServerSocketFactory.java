import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class HttpServerSocketFactory extends ServerSocketFactory {
    protected HttpServerSocketFactory() {
        super();
    }

    @Override
    public ServerSocket createServerSocket() throws IOException {
        return createServerSocket(8080);
    }

    @Override
    public ServerSocket createServerSocket(int i) throws IOException {
        return createServerSocket(i, 5);
    }

    @Override
    public ServerSocket createServerSocket(int i, int i1) throws IOException {
        return createServerSocket(i, i1, InetAddress.getByName("127.0.0.1"));
    }

    @Override
    public ServerSocket createServerSocket(int i, int i1, InetAddress inetAddress) throws IOException {
        return new ServerSocket(i, i1, inetAddress);
    }
}
