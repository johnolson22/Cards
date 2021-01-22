import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class CardGame extends JFrame implements ActionListener{
   JPanel p = new JPanel();
   JButton solitaireButton = new JButton("Solitaire");  
   JButton warButton = new JButton("War");
   JButton blackjackButton = new JButton("Blackjack");
 
   public CardGame()
   {      
      super("Cards");
   }

   public void initialize(){
      solitaireButton.addActionListener(this);
      warButton.addActionListener(this);
      blackjackButton.addActionListener(this);
      add(p);
      p.setBackground(Color.RED);
      p.add(solitaireButton);
      p.add(warButton);
      p.add(blackjackButton);
      setSize(300, 200);
      setVisible(true);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
   }
   
   public void playSolitaire()
   {
      Solitaire frame = new Solitaire();
      frame.setVisible(true);
      frame.setSize(1000, 800);
      frame.setLocationRelativeTo(null);
   }
      

   public void playBlackjack()
   {
      Blackjack game = new Blackjack();
      game.setVisible(true);
      game.initialize();
      game.setLocationRelativeTo(null);     
   }
  
   @Override
   public void actionPerformed(ActionEvent e){  
      if(e.getSource() == solitaireButton)
         playSolitaire();
      else
         playBlackjack();
   }
   
   public static void main(String[] args)
   {
      CardGame game = new CardGame();
      game.initialize();
   }
      
}