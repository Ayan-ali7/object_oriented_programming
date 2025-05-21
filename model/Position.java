package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a position on a path in the game board.
 * 
 * Preconditions:
 * - A position must be initialized with a valid step number (0 to 8).
 * - A position must be associated with a path.
 * 
 * Postconditions:
 * - The position correctly represents a location on a path.
 * - The position can hold a finding, a pawn, and a card, and track if it's a palace or intersection.
 */
public class Position {
    private final int step;       // The step number in the path (0 to 8)
    private final Path path;      // The path this position belongs to
    private Finding finding;      // The finding at this position, if any
    private Pawn pawn;            // The pawn at this position, if any
    private Card card;            // The card at this position, if any
    private final boolean isPalace;// Whether this position represents a palace
    private List<Pawn> pawns; 
    private Pawn overlappingPawn;
    /**
     * Constructs a new Position.
     * 
     * @param step The step number on the path (0 to 9)
     * @param path The path this position belongs to
     * @throws IllegalArgumentException if step is not between 0 and 8 or path is null
     */
    public Position(int step, Path path) {
        if (step < 0 || step > 9) {
            throw new IllegalArgumentException("Step must be between 0 and 8");
        }
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        this.step = step;
        this.path = path;
        this.pawns = new ArrayList<>();
        this.isPalace = (step == 9); // The last position (9) is always a palace
    }

    /**
     * Gets the step number of this position.
     * 
     * @return The step number
     */
    public int getStep() {
        return this.step;
    }

    /**
     * Gets the path this position belongs to.
     * 
     * @return The path
     */
    public Path getPath() {
        return this.path;
    }

    /**
     * Gets the path index of this position.
     * 
     * @return The path index
     */
    public int getPathIndex() {
        return path.getPathIndex();
    }

    /**
     * Checks if this position has a finding.
     * 
     * @return true if there's a finding at this position, false otherwise
     */
    public boolean hasFinding() {
        return finding != null;
    }

    /**
     * Gets the finding at this position.
     * 
     * @return The finding, or null if there isn't one
     */
    public Finding getFinding() {
        return finding;
    }

    /**
     * Sets a finding at this position.
     * 
     * @param finding The finding to place at this position
     */
    public void setFinding(Finding finding) {
        this.finding = finding;
    }

    /**
     * Removes and returns the finding from this position.
     * 
     * @return The removed finding, or null if there wasn't one
     */
    public Finding removeFinding() {
        Finding removedFinding = this.finding;
        this.finding = null;
        return removedFinding;
    }

    /**
     * Checks if this position has a pawn.
     * 
     * @return true if there's a pawn at this position, false otherwise
     */
    public boolean hasPawn() {
        return !pawns.isEmpty() || pawn != null;
    }

    /**
     * Gets the pawn at this position.
     * 
     * @return The pawn, or null if there isn't one
     */
    public Pawn getPawn() {
        return pawn;
    }

    /**
     * Sets a pawn at this position.
     * 
     * @param pawn The pawn to place at this position
     */
    public void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }

    public void addPawn(Pawn pawn) {
        if (pawn != null) {
            pawns.add(pawn);
        }
    }

    public void setOverlappingPawn(Pawn pawn) {
        this.overlappingPawn = pawn;
    }

    public Pawn getOverlappingPawn() {
        return overlappingPawn;
    }

    public void removePawn(Pawn pawn) {
        pawns.remove(pawn);
    }

    public List<Pawn> getPawns() {
        return new ArrayList<>(pawns);
    }

    /**
     * Removes and returns the pawn from this position.
     * 
     * @return The removed pawn, or null if there wasn't one
     */
    public Pawn removePawn() {
        Pawn removedPawn = this.pawn;
        this.pawn = null;
        return removedPawn;
    }

    /**
     * Checks if this position represents a palace.
     * 
     * @return true if this position is a palace, false otherwise
     */
    public boolean isPalace() {
        return isPalace;
    }

    /**
     * Sets the card at this position.
     * 
     * @param card the card to set
     */
    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * Retrieves the card at this position.
     * 
     * @return the card at this position, or null if no card is present
     */
    public Card getCard() {
        return card;
    }

    /**
     * Removes and returns the card from this position.
     * 
     * @return The removed card, or null if there wasn't one
     */
    public Card removeCard() {
        Card removedCard = this.card;
        this.card = null;
        return removedCard;
    }

    /**
     * Checks if this position has a card.
     * 
     * @return true if there's a card at this position, false otherwise
     */
    public boolean hasCard() {
        return card != null;
    }

    /**
     * Checks if this position is an intersection.
     * 
     * @return true if this position is an intersection, false otherwise
     */
    public boolean isIntersection() {
        return step == 2 || step == 5 || step == 7;
    }

    /**
     * Checks if this position is adjacent to another position.
     * 
     * @param other The other position to check
     * @return true if the positions are adjacent, false otherwise
     */
    public boolean isAdjacentTo(Position other) {
        return this.path.equals(other.path) && Math.abs(this.step - other.step) == 1;
    }

    /**
     * Gets the name of the palace this position belongs to.
     * 
     * @return The name of the palace
     */
    public String getPalaceName() {
        return path.getPalaceName();
    }

    /**
     * Checks if this position is empty (no pawn and no finding).
     * 
     * @return true if the position is empty, false otherwise
     */
    public boolean isEmpty() {
        return !hasPawn() && !hasFinding();
    }

    /**
     * Calculates the score of this position based on its step.
     * 
     * @return The score of the position
     */
    public int getScore() {
        if (step < 3) {
            return -5 + (step * 5); // -5, 0, 5 for steps 0, 1, 2
        } else {
            return (step - 2) * 5; // 5, 10, 15, 20, 25, 30 for steps 3-8
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return step == position.step && Objects.equals(path, position.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(step, path);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Pawn pawn : pawns) {
            String pawnType = pawn.getClass().getSimpleName().substring(0, 1);
            int playerNumber = pawn.getOwner().getPlayerIndex();
            sb.append(pawnType).append(playerNumber);
        }
        if (finding != null) {
            sb.append(finding.getName().substring(0, 1));
        }
        sb.append("]");
        return sb.toString();
    }
}