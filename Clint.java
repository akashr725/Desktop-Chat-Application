import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Clint extends JFrame {

    Socket socket;

    BufferedReader br;
    PrintWriter out;

    // Declare Components

    private JLabel heading = new JLabel("Clint Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Robot", Font.PLAIN, 20);

    public Clint() {

        try {
            System.out.println("Sending request to server");

            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connection done...");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvent();

            startReading();
            // startWritting();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handleEvent() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

                // System.out.println(e.getKeyCode());

                if (e.getKeyCode() == 10) {
                    // System.out.println("you have pressed enter button");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me: " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }

            }

        });
    }

    private void createGUI() {

        this.setTitle("Clint Messager[END]");
        this.setSize(600, 800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("Icon.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        // layout of frame

        this.setLayout(new BorderLayout());

        // adding the components to frame

        this.add(heading, BorderLayout.NORTH);
        JScrollPane jscroll = new JScrollPane(messageArea);
        this.add(jscroll, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);

    }

    public void startReading() {

        // thread read data

        Runnable r1 = () -> {

            System.out.println("Reader Start....");

            try {

                while (true) {

                    String msg = br.readLine();

                    if (msg.equals("exit")) {
                        System.out.println("Server Terminated the Chat...");
                        JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }

                    // System.out.println("Server: " + msg);
                    messageArea.append("Server: " + msg + "\n");
                }
            } catch (Exception e) {

                System.out.println("Connection is closed...");

            }

        };

        new Thread(r1).start();

    }

    public void startWritting() {

        // thread write data

        Runnable r2 = () -> {

            System.out.println("Writer Started....");

            try {
                while (!socket.isClosed()) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content;

                    content = br1.readLine();

                    out.println(content);
                    out.flush();

                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }

                }
            } catch (Exception e) {

                System.out.println("Connection is closed...");
            }

        };

        new Thread(r2).start();

    }

    public static void main(String[] args) {

        new Clint();

    }
}
