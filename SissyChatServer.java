import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class SissyChatServer {
    private static int portnum;

    private Vector<Socket> sockets = new Vector<>();
    public static void main(String [] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the port of the server: ");
        portnum = scan.nextInt();
        scan.close();
        new SissyChatServer();
    }

    public SissyChatServer() {
        ServerSocket ss = null;
        Socket s = null;
        try {
            ss = new ServerSocket(portnum);
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
