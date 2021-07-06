import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class TinyChatServer {

    private Vector<Socket> sockets = new Vector<>();
    public static void main(String [] args) {
        new TinyChatServer();
    }

    public TinyChatServer() {
        ServerSocket ss = null;
        Socket s = null;
        try {
            ss = new ServerSocket(12410);
            while(true) {
                s = ss.accept();
                System.out.println("Connected");
                sockets.add(s);
                MyTH th = new MyTH(s);
                th.start();
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    class MyTH extends Thread {

        private Socket s;

        public MyTH(Socket s) {
            this.s = s;
        }

        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
                pw.println("Connected");
                pw.flush();
                while(true) {
                    String line = br.readLine();
                    if(line != null) {
                        for(Socket s : sockets) {
                            PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
                            pw1.println(line);
                            pw1.flush();
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
