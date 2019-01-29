package homework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class test {
    public static void main(String[] args) {
        HttpConntor httpConntor = new HttpConntor();
        httpConntor.initial();
        httpConntor.start();
        try {
            System.in.read();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class HttpProcessor implements Runnable {

        @Override
        public void run() {

        }
    }

    static class HttpConntor implements Runnable {
        static int count = 1;
        ServerSocket serverSockets = null;
        int port = 8080;
        InetAddress addr;
        boolean shutdown = false;

        public HttpConntor() {

        }

        void start() {
            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        }

        void initial() {
            try {
                addr = InetAddress.getByName("127.0.0.1");
                serverSockets = new ServerSocket(port, 2, addr);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (!shutdown) {
                Socket s = null;
                try {
                    s = serverSockets.accept();
                    // s.setSoTimeout(3000);
                    System.out.println("custom:" + count++);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    process(s);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                System.out.println("finish !");


//                try {
//                    System.out.println(s + "  closed");
//                    s.close();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

            }

        }
    }

    private static void process(Socket s) throws UnsupportedEncodingException {
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String res = "i am first sentences!";
//               printWriter.println("HTTP/1.1 200 OK");
//               printWriter.println("Content-Type: text/html");
//               printWriter.println("Content-length:"+res.length());
//               printWriter.println("");
//               printWriter.println(res);

        String res2 = "我是中文测试句子";
        String res3="我是很长的中文句子\r\n一条大河波浪宽，风吹稻花香两岸，我家就在岸上住！";
        printWriter.println("HTTP/1.1 200 OK");
        printWriter.println("Content-Type: text/html; charset=UTF-8");
        printWriter.println("Transfer-Encoding:Chunked");
        printWriter.println("");
        printWriter.println("");
        printWriter.println(Integer.toHexString(res.getBytes("utf-8").length));
        printWriter.println(res);
        printWriter.println(Integer.toHexString(res2.getBytes("utf-8").length));
        printWriter.println(res2);
        printWriter.println(Integer.toHexString(res3.getBytes("utf-8").length));
        printWriter.println(res3);
        printWriter.println("0\r\n");
    }

}
