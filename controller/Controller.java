package controller;
import model.*;
import view.GameView;

import java.util.*;
import java.util.function.Function;

public class Controller {
    private List<Player> players;
    private Board board;
    private Deck deck;
    private int currentPlayerIndex;
    private Random random;
    private GameView gameView;

    public Controller() {
        this.players = new ArrayList<>();
        this.board = new Board();
        this.deck = new Deck();
        this.random = new Random();

    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }
    
    
    public void addPlayer(String name) {
        Player newPlayer = new Player(name, players.size());
        newPlayer.initializePawns();
        players.add(newPlayer);
    }
    
    public void initializeGame() {
        if (players.isEmpty()) {
            addPlayer("Player 1");
            addPlayer("Player 2");
        }
        
        updateGUI();
        // Set player indices
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setPlayerIndex(i);
        };
        // Randomly choose first player
        currentPlayerIndex = random.nextInt(players.size());
        
        // Place pawns at the start positions of different paths
        List<Path> paths = board.getPaths();
        if (paths.isEmpty()) {
            System.out.println("Error: No paths available on the board.");
            return;
        }
    
        int pathIndex = 0;
        for (Player player : players) {
            for (Pawn pawn : player.getPawns()) {
                Path path = paths.get(pathIndex % paths.size());
                Position startPosition = path.getPositionAt(0);
                pawn.setPosition(startPosition);
                startPosition.setPawn(pawn);
                pathIndex++;
            }
        }
    
        // Initialize and shuffle the deck
        deck.initializeDeck();
        deck.shuffle();
    
        // Deal initial cards to players
        dealInitialCards();

        // Update the game view
        if (gameView != null) {
            gameView.updateGUI();}
        }

    private void dealInitialCards() {
        for (Player player : players) {
            for (int i = 0; i < 8; i++) {
                player.addCard(deck.drawCard());
            }
        }
    }

    public boolean movePawn(Pawn selectedPawn, Card selectedCard) {
        if (selectedPawn == null || selectedCard == null) {
            return false;
        }
    
        Player currentPlayer = getCurrentPlayer();
        if (!currentPlayer.getPawns().contains(selectedPawn)) {
            return false;
        }
    
        Position currentPosition = selectedPawn.getCurrentPosition();
        if (currentPosition == null) {
            return false;
        }
    
        // Check if the card color matches the path color
        if (selectedCard.getColor() != currentPosition.getPath().getColor()) {
            return false;
        }
        // Check if the card color matches the path color or if it's a Minotaur card
        if (!(selectedCard instanceof MinotaurCard) && selectedCard.getColor() != currentPosition.getPath().getColor()) {
        return false;
        }
    
        int steps;
        if (selectedCard instanceof NumberCard) {
            steps = 1;
        } else if (selectedCard instanceof AriadneCard) {
            steps = 2;
        } else if (selectedCard instanceof MinotaurCard) {
            steps = selectedPawn instanceof Theseus ? 2 : -2;
        } else {
            return false;
        }
    
        Position newPosition = board.getNextPosition(currentPosition, steps);
   
       if (newPosition == null ) {
            return false;
        }
    
        // Move the pawn
        currentPosition.removePawn();
        newPosition.setPawn(selectedPawn);
        selectedPawn.setPosition(newPosition);
    
        // Handle finding collection
        Finding finding = newPosition.getFinding();
        if (finding != null) {
            currentPlayer.addFinding(finding, newPosition.getPath().getPalaceName());
            newPosition.removeFinding();
        }
    
        // Remove the used card and draw a new one
        currentPlayer.getCards().remove(selectedCard);
        Card newCard = deck.drawCard();
        if (newCard != null) {
            currentPlayer.addCard(newCard);
        }
    
        return true;
    }

    public void endTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        board.updatePawnVisibility(currentPlayerIndex);
        if (gameView != null) {
            gameView.playTurnAudio();
            gameView.updateGUI();
        }
    }
    
    public boolean playTurn(Card selectedCard) {
        Player currentPlayer = getCurrentPlayer();
        List<Pawn> validPawns = new ArrayList<>();
        
        if (selectedCard == null || !currentPlayer.getCards().contains(selectedCard)) {
            return false;
        }
    
        if (selectedCard instanceof MinotaurCard) {
            // For Minotaur card, affect opponent's Archaeologist pawn on the path matching the card color
            Player opponentPlayer = getOpponentPlayer();
            for (Pawn pawn : opponentPlayer.getPawns()) {
                if (pawn instanceof Archaeologist && pawn.getCurrentPosition() != null &&
                    pawn.getCurrentPosition().getPath().getColor() == selectedCard.getColor()) {
                    validPawns.add(pawn);
                    break;
                }
            }
        } else {
            // For other cards, current player's pawns on matching color paths are valid
            for (Pawn pawn : currentPlayer.getPawns()) {
                if (pawn.getCurrentPosition() != null && 
                    pawn.getCurrentPosition().getPath().getColor() == selectedCard.getColor()) {
                    validPawns.add(pawn);
                }
            }
        }
    
        if (validPawns.isEmpty()) {
            return false;
        }
    
        Pawn selectedPawn = validPawns.get(0);
        Position currentPosition = selectedPawn.getCurrentPosition();
    
        int steps;
        if (selectedCard instanceof NumberCard) {
            steps = 1;
        } else if (selectedCard instanceof AriadneCard) {
            steps = 2;
        } else if (selectedCard instanceof MinotaurCard) {
            steps = selectedPawn instanceof Theseus ? 2 : -2;
        } else {
            return false;
        }
    
        Position newPosition = board.getNextPosition(currentPosition, steps);
        if (newPosition == null ) {
            return false;
        }
    
        // Move the pawn
        currentPosition.removePawn();
        newPosition.setPawn(selectedPawn);
        selectedPawn.setPosition(newPosition);
    
        if (!(selectedCard instanceof MinotaurCard) || selectedPawn.getOwner() == currentPlayer) {
            Finding finding = newPosition.getFinding();
            if (finding != null) {
                if (selectedPawn instanceof Theseus) {
                    boolean destroyFinding = gameView.showTheseusOptions(currentPlayer.getName());
                    if (destroyFinding) {
                        newPosition.removeFinding();
                        gameView.showMessage(currentPlayer.getName() + " destroyed a " + finding.getName() + "!");
                    } else {
                        gameView.showMessage(currentPlayer.getName() + " ignored a " + finding.getName() + ".");
                    }
                } else if (selectedPawn instanceof Archaeologist) {
                    boolean dig = gameView.showDigOption(currentPlayer.getName());
                    if (dig) {
                        currentPlayer.addFinding(finding, newPosition.getPath().getPalaceName());
                        newPosition.removeFinding();
                        selectedPawn.setVisibleToBoth(true); // Set the pawn visible to both players
                        String message = currentPlayer.getName() + " found a " + finding.getName() + "!";
                        gameView.showMessage(message);
                    }
                }
            }
        }
    
        // Remove the used card from the player's hand
        currentPlayer.removeCard(selectedCard);
    
        // Draw a new card and add it to the player's hand
        Card newCard = deck.drawCard();
        if (newCard != null) {
            currentPlayer.addCard(newCard);
        }
    
        endTurn();

        if (isGameOver()) {
            System.out.println("Game is over!");
            Player winner = determineWinner();
            if (winner != null) {
                System.out.println("The winner is: " + winner.getName());
            } else {
                System.out.println("The game ended in a tie!");
            }
            if (gameView != null) {
                gameView.showGameOverScreen(players);
            }
        }
        return true;
    }

    
    public Player determineWinner() {
        List<Player> highestScorers = new ArrayList<>();
        int highestScore = Integer.MIN_VALUE;
    
        for (Player player : players) {
            int score = calculatePlayerScore(player);
            if (score > highestScore) {
                highestScorers.clear();
                highestScorers.add(player);
                highestScore = score;
            } else if (score == highestScore) {
                highestScorers.add(player);
            }
        }
    
        if (highestScorers.size() == 1) {
            return highestScorers.get(0);
        } else {
            return tieBreaker(highestScorers);
        }
    }
    
    private Player tieBreaker(List<Player> tiedPlayers) {
        // a) Most rare findings
        Player winner = getMostRareFindings(tiedPlayers);
        if (winner != null) return winner;
    
        // b) Most murals
        winner = getMostMurals(tiedPlayers);
        if (winner != null) return winner;
    
        // c) Most statuettes
        winner = getMostStatuettes(tiedPlayers);
        if (winner != null) return winner;
    
        // d) If still tied, return null (it's a tie)
        return null;
    }

    private Player getOpponentPlayer() {
        return players.get((currentPlayerIndex + 1) % players.size());
    }

    public void destroyFinding(Position position) {
        if (position.hasFinding()) {
            position.removeFinding();}
        }
    

    public boolean isGameOver() {
        return board.isGameOver() || deck.isEmpty();
    }

    public int getPlayerScore(int playerIndex) {
        return calculatePlayerScore(players.get(playerIndex));
    }
    
    public int calculatePlayerScore(Player player) {
        int score = 0;
    
        // 1. Score from murals
        score += player.getFindings().stream()
                .filter(f -> f.getName().startsWith("Mural"))
                .mapToInt(Finding::getScoreValue)
                .sum();
    
        // 2. Score from rare finds
        score += player.getFindings().stream()
                .filter(f -> isRareFinding(f))
                .mapToInt(Finding::getScoreValue)
                .sum();
    
        // 3. Score from statuettes
        int statuetteCount = (int) player.getFindings().stream()
                .filter(f -> f.getName().equals("Statuette"))
                .count();
        score += getStatuetteScore(statuetteCount);
    
        // 4 & 5. Score from pawn positions
        for (Pawn pawn : player.getPawns()) {
            Position position = pawn.getCurrentPosition();
            if (position != null) {
                int positionScore = getPositionScore(position.getStep());
                score += (pawn instanceof Theseus) ? positionScore * 2 : positionScore;
            }
        }
    
        return score;
    }
    
    public boolean isRareFinding(Finding finding) {
        return finding.getName().equals("Disc of Phaistos") ||
               finding.getName().equals("Ring of Minos") ||
               finding.getName().equals("Jewel of Malia") ||
               finding.getName().equals("Ryto of Zakros");
    }
    
    private int getStatuetteScore(int count) {
        return switch (count) {
            case 0 -> 0;
            case 1 -> -20;
            case 2 -> -15;
            case 3 -> 10;
            case 4 -> 15;
            case 5 -> 30;
            case 6 -> 50;
            default -> 0;
        };
    }
    
    private int getPositionScore(int step) {
        return switch (step) {
            case 0 -> 0;
            case 1 -> -20;
            case 2 -> -15;
            case 3 -> -10;
            case 4 -> 5;
            case 5 -> 10;
            case 6 -> 15;
            case 7 -> 30;
            case 8 -> 35;
            case 9 -> 50;
            default -> 0;
        };
    }
    
    private Player getMostRareFindings(List<Player> players) {
        return getPlayerWithMost(players, p -> (int) p.getFindings().stream()
                .filter(this::isRareFinding)
                .count());
    }
    
    private Player getMostMurals(List<Player> players) {
        return getPlayerWithMost(players, p -> (int) p.getFindings().stream()
                .filter(f -> f.getName().startsWith("Mural"))
                .count());
    }
    
    private Player getMostStatuettes(List<Player> players) {
        return getPlayerWithMost(players, p -> (int) p.getFindings().stream()
                .filter(f -> f.getName().equals("Statuette"))
                .count());
    }
    
    private Player getPlayerWithMost(List<Player> players, Function<Player, Integer> countFunction) {
        Player winner = null;
        int maxCount = -1;
        boolean tie = false;
    
        for (Player player : players) {
            int count = countFunction.apply(player);
            if (count > maxCount) {
                winner = player;
                maxCount = count;
                tie = false;
            } else if (count == maxCount) {
                tie = true;
            }
        }
    
        return tie ? null : winner;
    }

    public void resetGame() {
        board.resetBoard();
        deck.initializeDeck();
        deck.shuffle();

        for (Player player : players) {
            player.resetPlayer();
        }

        initializeGame();
    }

    public boolean discardCard(Card card) {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer.hasCard(card)) {
            // Remove the card from the player's hand
            currentPlayer.removeCard(card);
            
            // Add the discarded card to the bottom of the deck
            deck.addCard(card);
            
            // Draw a new card and add it to the player's hand
            Card newCard = deck.drawCard();
            if (newCard != null) {
                currentPlayer.addCard(newCard);
            }
            
            // Update the GUI to reflect the changes
            updateGUI();
            
            // End the current player's turn
            endTurn();
            
            return true;
        }
        return false;
    }

    public void updateGUI() {
        if (gameView != null) {
            gameView.updateGUI();
        }
    }

    public Deck getDeck() {
        return deck;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
    public List<Player> getPlayers() {
        return players;
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
}

