import java.io.IOException;

public class BootStrap {
    public static void main(String[] args) {
        HttpConnector httpConnector = new HttpConnector();
        try {
            httpConnector.initialize();
            httpConnector.start();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
        try {
            System.in.read();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
