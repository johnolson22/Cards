import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Solitaire extends JFrame implements MouseListener, MouseMotionListener
{
   JLayeredPane p = new JLayeredPane();
   private int heartVal = 0, diamondVal = 0, clubVal = 0, spadeVal = 0, nextHand = 0;
   Card c;
   Deck cards = new Deck(1, 13), placeHolders = new Deck(0, 0);
   private java.util.List<Card> tableau, hand;
   private final Point HEART_SPOT = new Point(845, 120);   
   private final Point DIAMOND_SPOT = new Point(845, 220);   
   private final Point CLUB_SPOT = new Point(845, 320);   
   private final Point SPADE_SPOT = new Point(845, 420);
   private final Point WASTE_SPOT = new Point(650, 600);
   private final int[] columns = {65, 145, 225, 305, 385, 465, 545};   
   
   
   public Solitaire()
   {
      for(Card ph : placeHolders){
         p.add(ph, JLayeredPane.DEFAULT_LAYER, -1);
         ph.flipUp();
         ph.inPlay = false;
      }
      placeHolders.get(0).moveTo(CLUB_SPOT); placeHolders.get(1).moveTo(DIAMOND_SPOT); placeHolders.get(2).moveTo(HEART_SPOT); placeHolders.get(3).moveTo(SPADE_SPOT);
      
      
      getContentPane().setBackground(Color.BLUE);
      setTitle("Solitaire");
      setIconImage(new ImageIcon("JO.GIF").getImage());
      Collections.shuffle(cards); 
      tableau = cards.subList(0, 28);
      hand = cards.subList(28, cards.size());         
      p.addMouseListener(this);
      p.addMouseMotionListener(this);
      int n = 0;
      for(int r = 7; r > 0; r--){
         for(int c = r; c > 0; c--){
            p.add(cards.get(n), JLayeredPane.PALETTE_LAYER);
            cards.get(n).moveTo(-15 + 80 * r, 5 + 20 * c);
            n++;  
         }
      }
      while(n < 52){
         p.add(cards.get(n), JLayeredPane.PALETTE_LAYER);
         cards.get(n).moveTo(705 + 2 * n, 530);
         p.add(new JLabel());
         n++;
      }
      add(p);
      for(int i = 0; i < 52; i++){
         cards.get(i).setLast(cards.get(i).getX(), cards.get(i).getY());
      }
      update();
   }
   
   public void win() {
      for(Card cd : cards)
         p.remove(cd);
      for(Card cd : placeHolders)
         p.remove(cd);
      getContentPane().setBackground(Color.BLACK);
   
      JLabel winLabel = new JLabel("You win!!!");
      winLabel.setFont(new Font("Helvetica", Font.ITALIC, 100));
      JLabel crownPicture = new JLabel(new ImageIcon("win.png"));
      winLabel.setBounds(250, 100, 900, 700);
      crownPicture.setBounds(0, 0, 900, 700);
      winLabel.setForeground(Color.RED);
      p.add(winLabel, JLayeredPane.DRAG_LAYER);
      p.add(crownPicture, JLayeredPane.DRAG_LAYER);
   }
   
   public void resetHand() {
      while (nextHand > 0){
         nextHand--;
         hand.get(nextHand).moveTo(705 + 2 * (nextHand + tableau.size()), 530);
         hand.get(nextHand).setLast(705 + 2 * (nextHand + tableau.size()), 530);
         hand.get(nextHand).flipDown();
         p.moveToFront(hand.get(nextHand));
      }
   }
   
   public void moveHand() {
      if(! hand.isEmpty())
      {
         if(nextHand >= hand.size())
            resetHand();
         else {
            hand.get(nextHand).moveTo(WASTE_SPOT);
            hand.get(nextHand).flipUp();
            p.moveToFront(hand.get(nextHand));
            hand.get(nextHand).setLast(hand.get(nextHand).getX(), hand.get(nextHand).getY());
            p.moveToFront(hand.get(nextHand));
            if(nextHand > 0){
               hand.get(nextHand - 1).inPlay = false;
            }
            nextHand++;
         }
      }
   }
   
   public void update()
   {
      if(heartVal == 13 && diamondVal == 13 && clubVal == 13 && spadeVal == 13)
         win();
      for(int n = 0; n < 52; n++){
         
         cards.get(n).moveBack();
         if(cards.get(n).getParentCard() != null)
            cards.get(n).moveTo(cards.get(n).getParentCard());
         moveChildCards(cards.get(n));
         
      }
      
      for(int n = 0; n < tableau.size(); n++){
         if(cards.get(n).getX() < 675){
            if(isLowest(cards.get(n)))
               cards.get(n).flipUp();
            if(cards.get(n).getSide() > 0)
               cards.get(n).inPlay = true;
         }
      }
   
                     
   }
   
   public void removeFromHand(Card cd){
      Collections.swap(hand, hand.indexOf(cd), 0);
      hand = cards.subList(53 - hand.size(), cards.size());
      tableau = cards.subList(0, tableau.size() + 1);
      if(nextHand >= 3){
         nextHand-=2;
         hand.get(nextHand - 1).inPlay = true;
         c = hand.get(nextHand - 1);
      }
      else{
         nextHand = 0;
         hand.get(nextHand).inPlay = true;
      }
     
   }
   
   private boolean isLowest(Card cd){
      for(int n = 0; n < 28; n++)
         if(cards.get(n) != cd && cd.getX() == cards.get(n).getX() && cd.getY() <= cards.get(n).getY() && cards.get(n).getParentCard() != cd)
            return false;
      return true;
   
   }
   
   public Card getChildCard(Card cd)
   {
      for(int n = 0; n < cards.size(); n++)
         if(cards.get(n).getParentCard() == cd)
            return cards.get(n);
      return null;
   }
   
   public boolean isEmptyColumn(int x){
      for(Card cd : cards)
         if(cd.getX() == x)
            return false;
      return true;
   }
   
   public void moveToTableau(Card cd){
      for(int n = 0; n < tableau.size() ; n++)
         if (c != null && cd.goesUnder(cards.get(n)) && cards.get(n).inPlay && getChildCard(cards.get(n)) == null){
            cd.moveTo(cards.get(n));
            cd.setParentCard(cards.get(n));
            p.moveToFront(cd);
            cd.lastX = cd.getX();
            cd.lastY = cd.getY();
         
            if(hand.contains(cd))
               removeFromHand(cd);
            c = null;
            return;
         }
   }
   
   public void moveToClosest(Card cd) {
      for(int n = 0; n < cards.size(); n++){
         if(cards.get(n) != cd && cd.isNear(cards.get(n)) && cd.goesUnder(cards.get(n)) && cd.inPlay && cd.getParent() != cards.get(n) && getChildCard(cards.get(n)) == null){
            cd.moveTo(cards.get(n));
            cd.setParentCard(cards.get(n));
            cd.lastX = cd.getX();
            cd.lastY = cd.getY();
         
            if(hand.contains(cd))
               removeFromHand(cd);
         
            c = null;
            return;
         }
      }
      moveKing(cd);
   }

   public void moveKing(Card cd){
      if(cd.getValue() == 13 && cd.inPlay){
         for(int x : columns)
            if(isEmptyColumn(x)){
               cd.moveTo(x, 25);
               cd.lastX = cd.getX();
               cd.lastY = cd.getY();
               c = null;
               if(hand.contains(cd))
                  removeFromHand(cd);                  
               return;
            }
      }
   }
   
   public void moveToFoundation(Card cd) {
      if(cd == null || getChildCard(cd) != null) 
         return;
      switch(c.getSuit()){
         case "hearts" : 
            if(cd.getValue() == heartVal + 1){
               cd.moveTo(HEART_SPOT);
               heartVal++;
               endCard(cd);
            }
            break;
         case "diamonds" : 
            if(cd.getValue() == diamondVal + 1){
               cd.moveTo(DIAMOND_SPOT);
               diamondVal++;
               endCard(cd);
            }
            break;
         case "clubs" : 
            if(cd.getValue() == clubVal + 1){
               cd.moveTo(CLUB_SPOT);
               clubVal++;
               endCard(cd);
            }
            break;
         case "spades" : 
            if(cd.getValue() == spadeVal + 1){
               cd.moveTo(SPADE_SPOT);
               spadeVal++;
               endCard(cd);
            }
            break;
         default :
            return;
         
      }
      
   }

   
   public void endCard(Card cd){
      cd.setLast(cd.getX(), cd.getY());
      cd.inPlay = false;
      cd.setParentCard(null);
      p.moveToFront(cd);
      if(hand.contains(cd))
         removeFromHand(cd);
   }
   
   public void moveChildCards(Card cd){
      if(getChildCard(cd) != null){
         getChildCard(cd).moveTo(cd);
         p.moveToFront(getChildCard(cd));
         moveChildCards(getChildCard(cd));
      }
   }
   
   public void dragTo(Point p, Card cd) {
   
      if(getChildCard(cd) != null && cd.getParentCard() != null)
         c = cd.getParentCard();
      
      else if(p.y < cd.getY() - 20 && cd.getParentCard() != null)
         c = cd.getParentCard();
      
      else if(cd.inPlay){
      
         cd.moveTo(p);
         this.p.moveToFront(cd);
         moveChildCards(cd);
         
      }
      
      
   
   }
   
   

   @Override
   public void mouseClicked(MouseEvent e) {
      if(e.getX() > 675 && e.getY() > 450)
         moveHand();
      else if(findComponentAt(e.getX(), e.getY()) instanceof Card){
         c = (Card) findComponentAt(e.getX(), e.getY());
         
         if(c.inPlay){
            moveToFoundation(c);
            moveToTableau(c);
         
         }            
      }
   
      update();
   }
   
   @Override
   public void mouseEntered(MouseEvent e) {
   }

   @Override
   public void mouseExited(MouseEvent e) {
   }

   @Override
   public void mousePressed(MouseEvent e) {
      if(findComponentAt(e.getX(), e.getY()) instanceof Card && isLowest((Card) findComponentAt(e.getX(), e.getY()))){
         c = (Card) findComponentAt(e.getX(), e.getY());
      }
   }
   
   @Override
   public void mouseReleased(MouseEvent e) {
      if(c instanceof Card && c.inPlay && c.getX() > 675 && c.getY() < 450)
         moveToFoundation(c);
      else if(c instanceof Card){
         moveToClosest(c);
      }
      update();
   }

   
   @Override
   public void mouseDragged(MouseEvent e) {
      if(c instanceof Card && c.inPlay) {
         dragTo(e.getPoint(), c);
               
      }        
   }

   @Override
   public void mouseMoved(MouseEvent e) {
   }
}