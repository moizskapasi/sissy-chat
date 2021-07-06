import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class SissyChatClientGUI implements ActionListener{
    private JFrame frame;
    private JPanel msgarea;
    private JPanel msgbut;
    private JTextArea chat;
    private JTextField msgin;
    private JButton send;
    private JButton save;
    private JButton exit;
    private Socket s;
    private String name;
    private PrintWriter pw;
    private BufferedReader br;
    private static int portnum;
    private static String ip;

    public SissyChatClientGUI(String name) {
        this.name = "<"+name+">: ";
        frame = new JFrame(name + "'s Chat");
        frame.setSize(750, 685);
        msgarea = new JPanel();
        msgarea.setBackground( new Color(51, 85, 255));
        msgbut = new JPanel(new FlowLayout());
        msgbut.setBackground( new Color(51, 85, 255));
        chat = new JTextArea(30,50);
        chat.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        chat.setBackground(new Color(255, 166, 77));
        chat.setForeground(new Color(102, 0, 0));
        chat.setEditable(false);
        msgin = new JTextField(20);
        send = new JButton("Send");
        send.addActionListener(this);
        save = new JButton("Save Chat");
        save.addActionListener(this);
        exit = new JButton("Exit");
        exit.addActionListener(this);
        msgarea.add(chat);
        msgbut.add(msgin);
        msgbut.add(send);
        msgbut.add(save);
        msgbut.add(exit);
        frame.add(msgarea, BorderLayout.NORTH);
        frame.add(msgbut, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        try {
            this.s = new Socket(ip, portnum);
            this.pw = new PrintWriter(new OutputStreamWriter(this.s.getOutputStream()));
            this.br = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
            pw.println(this.name + "has entered the chat (Server)");
            pw.flush();
            while(true) {
                String msg = br.readLine();
                if(msg != null) {
                    System.out.println(msg);
                    chat.append(msg + "\n");
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == exit) {
            try {
                pw.println(this.name + "has left the chat (Server)");
                pw.flush();    
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.exit(0);
        } else if(ae.getSource() == send) {
            try {
                pw.println(this.name + msgin.getText());
                pw.flush();
            } catch(Exception e) {
                e.printStackTrace();
            }
            msgin.setText("");
        } else if(ae.getSource() == save) {
            try {
                BufferedWriter bw = new BufferedWriter( new FileWriter("chat.txt"));
                bw.write(chat.getText());
                bw.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        } 
    }

    public static void main(String [] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = scan.nextLine();
        System.out.println("Enter the port of the server: ");
        portnum = scan.nextInt();
        System.out.println("Enter the IP of the server: ");
        ip = scan.next();
        scan.close();
        new SissyChatClientGUI(name);

    }
}
