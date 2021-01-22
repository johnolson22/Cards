import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Blackjack extends JFrame implements ActionListener{

    private final Point PLAYER_ZONE = new Point(200, 400);
    private final Point DEALER_ZONE = new Point(800, 400);

    private Color bg = Color.GREEN.darker();
    
    private int balance = 1000;
    private int bet;
    private ArrayList<Card> myCards;
    private ArrayList<Card> dHand;
    private Deck cards = new Deck();
    private Deck dCards = new Deck();
    
    private JLabel pLabel = new JLabel();
    private JLabel dLabel = new JLabel();
    private JButton hit = new JButton("hit");
    private JButton stand = new JButton("stand");
    private JButton deal = new JButton("deal");
    private JTextField betField = new JTextField();
    private JLabel betLabel = new JLabel("Please enter your bet");
    private JLabel text = new JLabel("Welcome to Blackjack!", SwingConstants.CENTER);
    private JLayeredPane cardTable = new JLayeredPane();
    private JPanel nestedPanel = new JPanel();
    private JLabel balanceDisplay = new JLabel("Current balance: $" + Integer.toString(balance));
    private JPanel buttonPanel = new JPanel();
    
    private JDialog winner = new JDialog(this);
    private JLabel winLabel = new JLabel();
    private JButton accept = new JButton("accept");
    private JPanel dPanel = new JPanel();

    public Blackjack()
    {
        super("Blackjack");
    }
    
    public void initialize(){
        
        text.setFont(new Font("Sans", Font.ITALIC, 50));
        betField.setFont(new Font("Courier", Font.BOLD, 25));
        
        hit.setBackground(Color.DARK_GRAY);
        hit.setForeground(Color.WHITE);
        stand.setBackground(Color.LIGHT_GRAY);
    
        setSize(1000, 800);
        setVisible(true);
        setResizable(false);
        
        accept.addActionListener(this);
        accept.setSize(new Dimension(150, 50));
        accept.setAlignmentX(Component.CENTER_ALIGNMENT);
        winLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        winner.setOpacity(1);
        dPanel.setBackground(bg);
        winner.setSize(new Dimension(200, 200));
        dPanel.setLayout(new BoxLayout(dPanel, BoxLayout.PAGE_AXIS));
        dPanel.add(accept);
        dPanel.add(winLabel);
        
        winner.add(dPanel);

        betField.setPreferredSize(new Dimension(100, 50));
        cardTable.setOpaque(true);
        cardTable.setBackground(bg);
        cardTable.setPreferredSize(new Dimension(1000, 650));
        pLabel.setBounds(170, 250, 100, 100);
        dLabel.setBounds(770, 250, 100, 100);
        add(cardTable, BorderLayout.PAGE_END);
        

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.add(hit, Component.CENTER_ALIGNMENT);
        buttonPanel.add(stand, Component.CENTER_ALIGNMENT);

        nestedPanel.add(balanceDisplay);
        nestedPanel.add(betField);
        nestedPanel.add(betLabel, Component.RIGHT_ALIGNMENT);


        text.setPreferredSize(new Dimension(1000, 50));
        add(nestedPanel, BorderLayout.CENTER);
        add(text, BorderLayout.PAGE_START);
        add(buttonPanel, BorderLayout.LINE_START);
        add(deal, BorderLayout.LINE_END);

        deal.addActionListener(this);
        hit.addActionListener(this);
        stand.addActionListener(this);
        resetCards();
    }

    public void resetCards(){
        winLabel.setForeground(Color.BLACK);
        dLabel.setVisible(false);

        myCards = new ArrayList<Card>();
        dHand = new ArrayList<Card>();

        cards.clear();
        cards = new Deck();

        dCards.clear();
        dCards = new Deck();

        deal.setEnabled(true);
        hit.setEnabled(false);
        stand.setEnabled(false);

        
        cardTable.removeAll();
        pLabel.setText("");
        cardTable.add(pLabel, JLayeredPane.MODAL_LAYER);
        cardTable.add(dLabel, JLayeredPane.MODAL_LAYER);
        
        cards.shuffleCards();
        dCards.shuffleCards();
        cards.get(0).moveTo(450, 425);
        cardTable.add(cards.get(0), JLayeredPane.PALETTE_LAYER);
        dCards.get(0).moveTo(450, 150);
        cardTable.add(dCards.get(0), JLayeredPane.PALETTE_LAYER, 0);
        for(int n = 1; n < cards.size(); n++){
            cardTable.add(cards.get(n), JLayeredPane.PALETTE_LAYER);
            cards.get(n).moveTo(cards.get(n-1));
            cardTable.add(dCards.get(n), JLayeredPane.PALETTE_LAYER, n);
            dCards.get(n).moveBehind(dCards.get(n-1));
        }
    }
    
    public void hit(){
        Card next = cards.get(myCards.size());
        next.flipUp();
        if(myCards.isEmpty())
            next.moveTo(PLAYER_ZONE);    
        else
            next.moveNext(myCards.get(myCards.size()-1));
        myCards.add(next);
        pLabel.setText("Value: " + totalValue(myCards));
        if(totalValue(myCards) >= 21)
            stand();
    }

    public int totalValue(ArrayList<Card> list){
        int sum = 0;
        for(Card c : list)
            sum+=c.getValue();
        return sum;
    }

    public void chooseCards(){
        while(totalValue(dHand) < 17)
            dHand.add(dCards.get(dHand.size()));
    }
    
    public void stand(){
        chooseCards();
        int pVal = totalValue(myCards);
        int dVal = totalValue(dHand);

        if((pVal > dVal || dVal > 21) && pVal <= 21){
            balance += bet;
            winLabel.setForeground(Color.GREEN);
            winLabel.setText("You win! You won $" + bet);
        }
        else if (pVal != dVal && dVal <= 21){
            balance -= bet;
            winLabel.setForeground(Color.RED);
            winLabel.setText("Dealer wins! You lost $" + bet);
        }
        else
            winLabel.setText("Push");
        dLabel.setText("Dealer value: " + Integer.toString(totalValue(dHand)));
        dLabel.setVisible(true);
        winner.setVisible(true);
    }

    public void moveDCards(){
        dHand.add(dCards.get(0));
        dHand.get(0).moveTo(DEALER_ZONE);
        dHand.get(0).flipUp();

        dHand.add(dCards.get(1));
        dHand.get(1).moveNext(dHand.get(0));
    }

    public void updateBet(){
        try{
            bet = Integer.parseInt(betField.getText());
            if(bet <= balance){
                betLabel.setText("Your bet is : $" + bet);
                deal.setEnabled(false);
                hit.setEnabled(true);
                stand.setEnabled(true);
                
                moveDCards();

                hit();
            }
            else 
                betLabel.setText("Please enter a valid bet");
        }
        catch(Exception e){
            betLabel.setText("Please enter a valid bet");
        }

    }

    @Override
    public void actionPerformed(ActionEvent e){  
        if(e.getSource() == deal)
            updateBet();
        else if(e.getSource() == hit){  
            hit();
        }
        else if (e.getSource() == stand)
            stand();
   
        else if (e.getSource() == accept){
            balanceDisplay.setText("Current balance: $" + Integer.toString(balance));
            winner.setVisible(false);
            resetCards();
        }
    }

    public static void main(String[] args){
        Blackjack bj = new Blackjack();
        bj.setDefaultCloseOperation(EXIT_ON_CLOSE);
        bj.initialize();
    }

}