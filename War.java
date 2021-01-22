import javax.swing.*;
import java.net.*;
import java.lang.Thread;
import java.awt.*;
import java.io.*;
import java.util.*;



public class War extends JFrame {
    JLayeredPane p = new JLayeredPane();
    private final Point PLAYER_SPOT = new Point(100,100);
    private final Point OPPONENT_SPOT = new Point(1,1);
    Card CardSpot = new Card("spades", 1);

    public War(){
        super("War");

        setSize(400, 400);
        p.setOpaque(true);
        p.setBackground(Color.RED);
        p.setSize(400,400);
        add(p);
        CardSpot.moveTo(PLAYER_SPOT);
        p.add(CardSpot);
        setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        try{
          Socket socket=new Socket("127.0.0.1",8888);
          DataInputStream inStream=new DataInputStream(socket.getInputStream());
          DataOutputStream outStream=new DataOutputStream(socket.getOutputStream());
          BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
          String clientMessage="",serverMessage="";
          while(!clientMessage.equals("bye")){
            System.out.println("Enter number :");
            clientMessage=br.readLine();
            outStream.writeUTF(clientMessage);
            outStream.flush();
            serverMessage=inStream.readUTF();
            System.out.println(serverMessage);
          }
          outStream.close();
          outStream.close();
          socket.close();
        }catch(Exception e){
          System.out.println(e);
        }
        }
}
