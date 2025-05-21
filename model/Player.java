package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;

public class Player {
    private final String name;
    private final List<Card> cards;
    private final List<Pawn> pawns;
    private final List<Finding> findings;
    private final Map<String, List<Finding>> findingsByPalace;
    private int points;
    private int playerIndex;
    
        public Player(String name, int playerIndex) {
            this.name = name;
            this.playerIndex = playerIndex;
            this.cards = new ArrayList<>();
            this.pawns = new ArrayList<>();
            this.findings = new ArrayList<>();
            this.findingsByPalace = new HashMap<>();
            this.points = 0;
            initializePawns();
        }
    
        public void initializePawns() {
            pawns.clear();
            for (int i = 0; i < 3 ; i++) {
                 pawns.add(new Archaeologist(this));
            }
            pawns.add(new Theseus(this));
        }
    
        public void addCard(Card card) {
            cards.add(card);
        }
    
        public Card playCard(int index) {
            if (index >= 0 && index < cards.size()) {
                return cards.remove(index);
            }
            return null;
        }
    
        public void addPawn(Pawn pawn) {
            pawns.add(pawn);
        }
    
        public void addFinding(Finding finding, String palaceName) {
            if (finding != null) {
                findings.add(finding);
                System.out.println(getName() + " added finding: " + finding.getName());
            }
        }
    
        public List<Card> getCards() {
            return new ArrayList<>(cards);
        }
    
        public List<Pawn> getPawns() {
            return new ArrayList<>(pawns);
        }
        
        public List<Finding> getFindings() {
            return new ArrayList<>(findings);
        }
    
        public Map<String, List<Finding>> getFindingsByPalace() {
            return new HashMap<>(findingsByPalace);
        }
    
        public int getPoints() {
            return points;
        }
        
        public String getName() {
            return name;
        }
        public void setPlayerIndex(int index) {
            this.playerIndex = index;
    }
    public int getPlayerIndex() {
        return playerIndex;
    }

    public boolean hasCard(Card card) {
        return cards.contains(card);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public int hashCode() {
        return Objects.hash(name, playerIndex, points);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return playerIndex == player.playerIndex && name.equals(player.name) && points == player.points;
    }

    public boolean hasTheseusSpecialAbility() {
        return pawns.stream().anyMatch(pawn -> pawn instanceof Theseus);
    }

    public void resetPlayer() {
        cards.clear();
        pawns.clear();
        findings.clear();
        findingsByPalace.clear();
        points = 0;
    }

    @Override
    public String toString() {
        return "Player{" +
               "name='" + name + '\'' +
               ", playerIndex=" + playerIndex +
               ", cards=" + cards.size() +
               ", pawns=" + pawns.size() +
               ", findings=" + findings.size() +
               ", findingsByPalace=" + findingsByPalace +
               ", points=" + points +
               '}';
    }
}