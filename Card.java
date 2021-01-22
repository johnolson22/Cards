import java.awt.*;
import javax.swing.*;

public class Card extends JLabel
{
   public Card parentCard = null;
   public boolean inPlay = false;
   private String suit, color;
   private int value, side;
   public int lastX = 0, lastY = 0;
   private ImageIcon front, back = new ImageIcon("textures/back.png");
   
   
   public void setParentCard(Card cd){
      parentCard = cd;
   }
   
   public Card getParentCard()
   {
      return parentCard;
   }
   
   public Card(String suit, int value)
   {
      this.suit = suit;
      if(suit.equals("hearts") || suit.equals("diamonds"))
         color = "red";
      else color = "black";
      this.value = value;
      front = new ImageIcon("textures/" + value + suit + ".gif");
      side = -1;
      setIcon(back);
   }
   
   
   public boolean goesUnder(Card other) {
      return getValue() == other.getValue() - 1 && ! getColor().equals(other.getColor()) && inPlay && other.inPlay;
   }
   
   public boolean isNear(Card other) {
      return Math.abs(getX() - other.getX()) < 35 && Math.abs(getY() - other.getY()) < 75;
   }
   
   public void moveBack(){
      moveTo(lastX, lastY);
   }
   
   public void setLast(int x, int y){
      lastX = x;
      lastY = y;
   }
   
   public String getSuit(){
      return suit;
   }
   
   public int getValue(){
      return value;
   }
   
   public String getColor() {
      return color;
   }
   
   public int getSide()
   {
      return side;
   }
   
   public void moveTo(Card cd)
   {
      setBounds(cd.getX(), cd.getY() + 15, 75, 100);
   }
   public void moveTo(Point p)
   {
      setBounds(p.x - 45, p.y - 70, 75, 100);
   }
   
   public void moveTo(int x, int y)
   {
      setBounds(x, y, 75, 95);
   }

   public void moveBehind(Card cd)
   {
      setBounds(cd.getX(), cd.getY() -15, 75, 100);
   }

   public void moveNext(Card cd)
   {
      setBounds(cd.getX() + 40, cd.getY(), 75, 100);
   }

   public void flipUp(){
      setIcon(front);
      inPlay = true;
      side = 1;
   }
   
   public void flipDown(){      
      setIcon(back);
      inPlay = false;
      side = -1;
   }

   
   public String toString(){
      String result = "";
      switch(value){
         case 1: result = "Ace";
            break;
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10: result = Integer.toString(value);
            break;
         case 11: result = "Jack";
            break;
         case 12: result = "Queen";
            break;
         case 13: result = "King";
            break;
         default: result = "";
            break;
      }
      
      result += " of "+ suit;
      return result;
   }
}