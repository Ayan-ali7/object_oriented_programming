package view;

import controller.Controller;
import model.*;
import model.Card.CardColor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class GameView extends JFrame {
    private Controller controller;
    private JPanel backgroundPanel;
    private JPanel player1Panel;
    private JPanel player2Panel;
    private JPanel boardPanel;
    private JPanel controlPanel;
    private JLabel turnLabel;
    private JLabel scoreLabel;
    private JLabel cardDeckLabel;
    private JLabel[][] playerCardSlots;
    private Card selectedCard;
    private Clip player1Audio;
    private Clip player2Audio;
    private JTextArea player1FindingsArea;
    private JTextArea player2FindingsArea;

    public GameView(Controller controller) {
        super("Searching for the Ancient Minoan Palace");
        this.controller = controller;
        controller.setGameView(this);

        playerCardSlots = new JLabel[2][8];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                playerCardSlots[i][j] = new JLabel("Empty");
                playerCardSlots[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
        }

        loadAudioFiles();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLayout(new BorderLayout());
        initializeComponents();
        layoutComponents();
        
        controller.initializeGame();
        updateGUI();

        addCardSelectionListeners();

        setVisible(true);
    }

    private void initializeComponents() {
        initializeBackground();
        initializePlayerPanels();
        initializeBoardPanel();
        initializeControlPanel();
    }

    private void initializeControlPanel() {
        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(6, 2, 10, 10));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Controls"));

        JButton playTurnButton = new JButton("Play Turn");
        JButton discardCardButton = new JButton("Discard Card");
        JButton exitButton = new JButton("Exit");
        

        playTurnButton.addActionListener(e -> playTurn());
        discardCardButton.addActionListener(e -> discardCard());
        exitButton.addActionListener(e -> System.exit(0));
        

        scoreLabel = new JLabel("Score: Player 1 - 0 | Player 2 - 0");
        turnLabel = new JLabel("Turn: Player 1");
        cardDeckLabel = new JLabel("Available Cards: 100");

    player1FindingsArea = new JTextArea(3, 20);
    player1FindingsArea.setEditable(false);
    player1FindingsArea.setBorder(BorderFactory.createTitledBorder("Player 1 Findings"));

    player2FindingsArea = new JTextArea(3, 20);
    player2FindingsArea.setEditable(false);
    player2FindingsArea.setBorder(BorderFactory.createTitledBorder("Player 2 Findings"));

    controlPanel.add(playTurnButton);
    controlPanel.add(discardCardButton);
    controlPanel.add(exitButton);
    controlPanel.add(scoreLabel);
    controlPanel.add(turnLabel);
    controlPanel.add(cardDeckLabel);
    controlPanel.add(player1FindingsArea);
    controlPanel.add(player2FindingsArea);
    }


    public void closeAudioResources() {
        if (player1Audio != null) {
            player1Audio.close();
        }
        if (player2Audio != null) {
            player2Audio.close();
        }
    }

    private void layoutComponents() {
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(player1Panel, BorderLayout.NORTH);
        backgroundPanel.add(boardPanel, BorderLayout.CENTER);
        backgroundPanel.add(player2Panel, BorderLayout.SOUTH);
        backgroundPanel.add(controlPanel, BorderLayout.EAST);
        
        setContentPane(backgroundPanel);
    }


    private void initializeBackground() {
        backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
        };
        };
    }

    private void initializePlayerPanels() {
        player1Panel = createPlayerPanel("Player 1", 0);
        player2Panel = createPlayerPanel("Player 2", 1);
    }
    
    private JPanel createPlayerPanel(String playerName, int playerIndex) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(playerName));
    
        // Create a panel for cards and pawns
        JPanel contentPanel = new JPanel(new BorderLayout());
    
        // Card panel (squeezed)
        JPanel cardPanel = new JPanel(new GridLayout(1, 8, 2, 0)); // Reduced horizontal gap
        for (int i = 0; i < 8; i++) {
            JLabel cardSlot = new JLabel();
            cardSlot.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            cardSlot.setPreferredSize(new Dimension(60, 90)); // Reduced size
            playerCardSlots[playerIndex][i] = cardSlot;
            cardPanel.add(cardSlot);
        }
    
        contentPanel.add(cardPanel, BorderLayout.CENTER);
    
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        JLabel cardInfoLabel = new JLabel("Cards: 0");
        JLabel pawnInfoLabel = new JLabel("Pawns: 0 Archaeologist, 0 Theseus");
        infoPanel.add(cardInfoLabel);
        infoPanel.add(pawnInfoLabel);
    
        panel.add(contentPanel, BorderLayout.CENTER);
        panel.add(infoPanel, BorderLayout.SOUTH);
    
        return panel;
    }
    private void initializeBoardPanel() {
        boardPanel = new JPanel(new GridLayout(4, 1, 0, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon boardIcon = new ImageIcon("project_assets/images/background.jpg");
                g.drawImage(boardIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        boardPanel.setBorder(BorderFactory.createTitledBorder("Game Board"));
    
        String[] pathNames = {"Knossos", "Malia", "Phaistos", "Zakros"};
       CardColor[] pathColors = {CardColor.RED, CardColor.YELLOW, CardColor.GREEN, CardColor.BLUE};

        for (int i = 0; i < 4; i++) {
            addPath(boardPanel, pathColors[i], pathNames[i]);
        }
    }
    
    private void addPath(JPanel boardPanel, CardColor color, String pathName) {
        JPanel pathPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        pathPanel.setOpaque(false);
        pathPanel.setBorder(BorderFactory.createTitledBorder(pathName));

        for (int i = 0; i < 10; i++) {
            JPanel stepPanel = new JPanel();
            stepPanel.setPreferredSize(new Dimension(60, 60));
            stepPanel.setBackground(convertCardColorToAwtColor(color));
            stepPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            pathPanel.add(stepPanel);
        }
    
        boardPanel.add(pathPanel);
    }
    
    private Color convertCardColorToAwtColor(CardColor cardColor) {
        switch (cardColor) {
            case RED:
                return Color.RED;
            case YELLOW:
                return Color.YELLOW;
            case GREEN:
                return Color.GREEN;
            case BLUE:
                return Color.BLUE;
            default:
                return Color.GRAY; // Default color if somehow an undefined color is used
        }
    }

    private ImageIcon getCardIcon(Card card) {
    int width = 80;
    int height = 120;
    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = bufferedImage.createGraphics();

    // Set card background color
    g2d.setColor(card.getCardColorAsColor());
    g2d.fillRect(0, 0, width, height);

    // Draw border
    g2d.setColor(Color.BLACK);
    g2d.drawRect(0, 0, width - 1, height - 1);

    // Add card text
    g2d.setColor(Color.WHITE);
    g2d.setFont(new Font("Arial", Font.BOLD, 30));
    String text;
    if (card instanceof NumberCard) {
        text = String.valueOf(((NumberCard) card).getNumber());
    } else if (card instanceof AriadneCard) {
        text = "A";
    } else if (card instanceof MinotaurCard) {
        text = "M";
    } else {
        text = "?";
    }

    FontMetrics fm = g2d.getFontMetrics();
    int textWidth = fm.stringWidth(text);
    int textHeight = fm.getHeight();
    g2d.drawString(text, (width - textWidth) / 2, (height + textHeight) / 2);

    g2d.dispose();
    return new ImageIcon(bufferedImage);
}

private void addCardSelectionListeners() {
    for (int playerIndex = 0; playerIndex < 2; playerIndex++) {
        for (int cardIndex = 0; cardIndex < 8; cardIndex++) {
            final int finalPlayerIndex = playerIndex;
            final int finalCardIndex = cardIndex;
            playerCardSlots[playerIndex][cardIndex].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (finalPlayerIndex == controller.getCurrentPlayerIndex()) {
                        List<Card> playerCards = controller.getCurrentPlayer().getCards();
                        if (finalCardIndex < playerCards.size()) {
                            selectedCard = playerCards.get(finalCardIndex);
                            highlightSelectedCard(finalPlayerIndex, finalCardIndex);
                        }
                    }
                }
            });
        }
    }
}

    private void highlightSelectedCard(int playerIndex, int cardIndex) {
        for (int i = 0; i < 8; i++) {
            playerCardSlots[playerIndex][i].setBorder(
                i == cardIndex ? 
                BorderFactory.createLineBorder(Color.RED, 2) : 
                BorderFactory.createLineBorder(Color.BLACK)
            );
        }
    }

    public void showGameOverScreen(List<Player> players) {
        StringBuilder message = new StringBuilder("Game Over!\n");
        Player winner = null;
    
        winner = controller.determineWinner();
    
        if (winner != null) {
            message.append("\nWinner: ").append(winner.getName());
        } else {
            message.append("\nIt's a tie!");
        }
    
        JOptionPane.showMessageDialog(this,
            message.toString(),
            "Game Over",
            JOptionPane.INFORMATION_MESSAGE);
    }

    
    private void playTurn() {
        if (selectedCard == null) {
            JOptionPane.showMessageDialog(this, "Please select a card first.");
            return;
        }
    
        boolean turnPlayed = controller.playTurn(selectedCard);
        if (turnPlayed) {
            selectedCard = null;
            updateGUI();
        } else {
            String errorMessage = "Unable to play the selected card. ";
            if (selectedCard instanceof MinotaurCard) {
                errorMessage += "not a valid move according to the Minotaur card.";
            } else {
                errorMessage += "Make sure you have a pawn on a path matching the card color and it can move to a valid position.";
            }
            JOptionPane.showMessageDialog(this, errorMessage);
        }
    }
    
    
    
    public void updatePlayerCards(int playerIndex, List<Card> cards) {
        for (int i = 0; i < 8; i++) {
            final int cardIndex = i;
            if (i < cards.size()) {
                Card card = cards.get(i);
                ImageIcon icon = getCardIcon(card);
                playerCardSlots[playerIndex][i].setIcon(icon);
                playerCardSlots[playerIndex][i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (playerIndex == controller.getCurrentPlayerIndex()) {
                            selectCard(playerIndex, cardIndex);
                        }
                    }
                });
            } else {
                playerCardSlots[playerIndex][i].setIcon(null);
                playerCardSlots[playerIndex][i].setText("Empty");
            }
        }
    }
    
    private void selectCard(int playerIndex, int cardIndex) {
        // Deselect all cards
        for (int i = 0; i < 8; i++) {
            playerCardSlots[playerIndex][i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
        // Highlight the selected card
        playerCardSlots[playerIndex][cardIndex].setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        selectedCard = controller.getPlayers().get(playerIndex).getCards().get(cardIndex);
    }

    
    public void updatePlayerInfo(int playerIndex, Player player) {
    JPanel playerPanel = (playerIndex == 0) ? player1Panel : player2Panel;
    JPanel infoPanel = (JPanel) playerPanel.getComponent(1); 
    JLabel cardInfoLabel = (JLabel) infoPanel.getComponent(0);
    JLabel pawnInfoLabel = (JLabel) infoPanel.getComponent(1);

    int archaeologistCount = 0;
    int theseusCount = 0;
    for (Pawn pawn : player.getPawns()) {
        if (pawn instanceof Archaeologist) {
            archaeologistCount++;
        } else if (pawn instanceof Theseus) {
            theseusCount++;
        }
    }

    cardInfoLabel.setText("Cards: " + player.getCards().size());
    pawnInfoLabel.setText("Pawns: " + archaeologistCount + " Archaeologist, " + theseusCount + " Theseus");
}

    

    private void discardCard() {
        if (selectedCard == null) {
            JOptionPane.showMessageDialog(this, "Please select a card to discard first.");
            return;
        }
    
        boolean discarded = controller.discardCard(selectedCard);
        if (discarded) {
            JOptionPane.showMessageDialog(this, "Card discarded successfully. Turn ended.");
            selectedCard = null;
            updateGUI();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to discard the card. Please try again.");
        }
    }


    private void updateBoardPanel() {
        List<Path> paths = controller.getBoard().getPaths();
        int currentPlayerIndex = controller.getCurrentPlayerIndex();
    
        for (int i = 0; i < paths.size(); i++) {
            Path path = paths.get(i);
            JPanel pathPanel = (JPanel) boardPanel.getComponent(i);
            
            for (int j = 0; j < path.getLength(); j++) {
                Position position = path.getPositionAt(j);
                JPanel stepPanel = (JPanel) pathPanel.getComponent(j);
                stepPanel.removeAll();
                stepPanel.setLayout(new BorderLayout());
    
                // Set background color based on position
                if (j == 0) {
                    if ( i== 0) {
                        stepPanel.setBackground(Color.red);
                    }
                    else if (i==1) {
                        stepPanel.setBackground(Color.blue);   
                        } 
                        else if (i==2) { stepPanel.setBackground(Color.green); 
                        } 
                        else if (i==3) {
                        stepPanel.setBackground(Color.yellow);
                        }
                    else {  stepPanel.setBackground(Color.GRAY);  // Starting position  
                    }

                } else if (j == path.getLength() - 1) {
                    stepPanel.setBackground(Color.white); // Palace position
                } 
                 else {
                    stepPanel.setBackground(Color.WHITE); // Regular position
                }
    
                // Add position number
                if (position.hasPawn()) {
                    Pawn pawn = position.getPawn();
                    boolean isVisible = pawn.getOwner().getPlayerIndex() == currentPlayerIndex || 
                                        isPawnVisible(pawn, j) || 
                                        pawn.isVisibleToBoth();
                    
                    if (isVisible) {
                        String pawnLabel = (pawn instanceof Archaeologist ? "A" : "T") + (pawn.getOwner().getPlayerIndex() + 1);
                        JLabel pawnLabelComponent = new JLabel(pawnLabel);
                        pawnLabelComponent.setHorizontalAlignment(SwingConstants.CENTER);
                        pawnLabelComponent.setForeground(pawn.getOwner().getPlayerIndex() == 0 ? Color.black : Color.darkGray);
                        stepPanel.add(pawnLabelComponent, BorderLayout.CENTER);
                    }
                }
    
                // Add finding if present
                if (position.hasFinding()) {
                    String findingLabel = "F" ;
                    JLabel findingLabelComponent = new JLabel(findingLabel);
                    findingLabelComponent.setHorizontalAlignment(SwingConstants.CENTER);
                    findingLabelComponent.setForeground(Color.GREEN);
                    stepPanel.add(findingLabelComponent, BorderLayout.SOUTH);
                }
    
                stepPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                stepPanel.revalidate();
                stepPanel.repaint();
            }
        }
    }

    private void updatePlayerPanels() {
        for (int i = 0; i < controller.getPlayers().size(); i++) {
            Player player = controller.getPlayers().get(i);
            JPanel playerPanel = i == 0 ? player1Panel : player2Panel;
            
            // Update card display
            List<Card> playerCards = player.getCards();
            for (int j = 0; j < 8; j++) {
                if (j < playerCards.size()) {
                    playerCardSlots[i][j].setIcon(getCardIcon(playerCards.get(j)));
                } else {
                    playerCardSlots[i][j].setIcon(null);
                }
            }
            
            // Update player info
            JPanel infoPanel = (JPanel) playerPanel.getComponent(1);
            JLabel cardInfoLabel = (JLabel) infoPanel.getComponent(0);
            JLabel pawnInfoLabel = (JLabel) infoPanel.getComponent(1);
            
            cardInfoLabel.setText("Cards: " + player.getCards().size());
            int archaeologistCount = (int) player.getPawns().stream().filter(p -> p instanceof Archaeologist).count();
            int theseusCount = (int) player.getPawns().stream().filter(p -> p instanceof Theseus).count();
            pawnInfoLabel.setText("Pawns: " + archaeologistCount + " Archaeologist, " + theseusCount + " Theseus");
        }
    }
    
    private void updateControlPanel() {
        turnLabel.setText("Turn: Player " + (controller.getCurrentPlayerIndex() + 1));
        scoreLabel.setText("Score:  Player 1 : " + controller.getPlayerScore(0) + "      Player 2 : " + controller.getPlayerScore(1));
        cardDeckLabel.setText("Available Cards: " + controller.getDeck().size());
    }

    private void updateFindingsDisplay() {
        List<Player> players = controller.getPlayers();
        if (players.size() >= 2) {
            updatePlayerFindingsCounts(players.get(0), player1FindingsArea);
            updatePlayerFindingsCounts(players.get(1), player2FindingsArea);
        }
    }
    
    private void updatePlayerFindingsCounts(Player player, JTextArea findingsArea) {
        int muralCount = 0;
        int statuetteCount = 0;
        int rareFindingCount = 0;
    
        for (Finding finding : player.getFindings()) {
            if (finding.getName().startsWith("Mural")) {
                muralCount++;
            } else if (finding.getName().equals("Statuette")) {
                statuetteCount++;
            } else if (controller.isRareFinding(finding)) {
                rareFindingCount++;
            }
        }
    
        StringBuilder sb = new StringBuilder();
        sb.append("Rare Findings: ").append(rareFindingCount).append("\n");
        sb.append("Murals: ").append(muralCount).append("\n");
        sb.append("Statuettes: ").append(statuetteCount).append("\n");
        
    
        findingsArea.setText(sb.toString());
    }
     
    private void loadAudioFiles() {
        try {
            AudioInputStream audioInputStream1 = AudioSystem.getAudioInputStream(new File("project_assets/music/player1.wav"));
            player1Audio = AudioSystem.getClip();
            player1Audio.open(audioInputStream1);
    
            AudioInputStream audioInputStream2 = AudioSystem.getAudioInputStream(new File("project_assets/music/player2.wav"));
            player2Audio = AudioSystem.getClip();
            player2Audio.open(audioInputStream2);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playTurnAudio() {
        int currentPlayerIndex = controller.getCurrentPlayerIndex();
        // Stop both audio clips
        if (player1Audio.isRunning()) {
            player1Audio.stop();
        }
        if (player2Audio.isRunning()) {
            player2Audio.stop();
        }
        
        // Play the appropriate audio for the current player
        if (currentPlayerIndex == 0) {
            player1Audio.setFramePosition(0);
            player1Audio.start();
        } else {
            player2Audio.setFramePosition(0);
            player2Audio.start();
        }
    }

    public void updateGUI() {
        updateBoardPanel();
        updatePlayerPanels();
        updateControlPanel();
        updateFindingsDisplay();
        playTurnAudio();
        revalidate();
        repaint();
    }
    
    private boolean isPawnVisible(Pawn pawn, int positionIndex) {
        return positionIndex == 0 || positionIndex == 8 || positionIndex == 6;
    }
    
    //Theseus options when a theseus pawn reached a finding
    public boolean showTheseusOptions(String playerName) {
        String[] options = {"Destroy Finding", "Keep Finding"};
        int choice = JOptionPane.showOptionDialog(
            this,
            playerName + ", Theseus has encountered a finding. Do you want to destroy it?",
            "Theseus Action",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        return choice == JOptionPane.YES_OPTION;
    }
    

    //Archaeologist options when a archaeologist pawn reached a finding
    public boolean showDigOption(String playerName) {
        int choice = JOptionPane.showConfirmDialog(
            this,
            playerName + ", do you want to dig for the finding?",
            "Dig Option",
            JOptionPane.YES_NO_OPTION
        );
        return choice == JOptionPane.YES_OPTION;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public void showGameOverMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }
}