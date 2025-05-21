package model;

/**
 * Represents a generic pawn in the game.
 * 
 * Preconditions:
 * - A valid position must be assigned before any movement.
 * 
 * Postconditions:
 * - The pawn's position is updated upon movement.
 * 
 * Invariants:
 * - A pawn must always belong to a valid path or position.
 */ 
public abstract class Pawn {
    protected final String type;  // "Archaeologist" or "Theseus"
    protected Position currentPosition; 
    protected final Player owner; 
    protected int playerNumber;
    private boolean revealed = false;
    private boolean isVisible;
    private boolean visibleToBoth;

    /**
     * Constructs a new Pawn.
     * 
     * @param type The type of the pawn ("Archaeologist" or "Theseus")
     * @param owner The player who owns this pawn
     */
    public Pawn(String type, Player owner) {
        this.type = type;
        this.owner = owner;
        this.playerNumber = owner.getPlayerIndex() + 1;
    }

    /**
     * Moves the pawn based on the given card.
     * 
     * @param card the card to determine movement
     * @param board the game board
     * @return true if the move was successful, false otherwise
     */
    public abstract boolean move(Card card, Board board);

    /**
     * Gets the current position of the pawn.
     * 
     * @return the current position
     */
    public Position getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Sets the current position of the pawn.
     * 
     * @param position the new position
     */
    public void setPosition(Position position) {
        if (this.currentPosition != null) {
            this.currentPosition.removePawn(this);
        }
        this.currentPosition = position;
        if (position != null) {
            position.setPawn(this);
        }
    }

    /**
     * Retrieves the type of the pawn.
     * 
     * @return the type of the pawn
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the owner of this pawn.
     * 
     * @return the player who owns this pawn
     */

    public Player getOwner() {
        return owner;
    }

    public String getSymbol() {
        // Return the first letter of the pawn's class name
        return this.getClass().getSimpleName().substring(0, 1);
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * Checks if the pawn has reached a palace.
     * 
     * @return true if the pawn is at a palace position, false otherwise
     */
    public boolean isAtPalace() {
        return currentPosition != null && currentPosition.isPalace();
    }

    /**
     * Attempts to collect a finding at the current position.
     * 
     * @return the collected Finding, or null if no finding was present
     */
    public Finding collectFinding() {
        if (currentPosition != null && currentPosition.hasFinding()) {
            Finding finding = currentPosition.removeFinding();
            owner.addFinding(finding, type.toString());
            return finding;
        }
        return null;
    }

    public boolean isAffectedByMinotaur() {
        if (currentPosition != null) {
            Card card = currentPosition.getCard();
            return card != null && card.getType() == Card.CardType.MINOTAUR;
        }
        return false;
    }

    

    public void setRevealed(boolean revealed) {
    this.revealed = revealed;
    }

    public boolean isRevealed() {
    return revealed;
    }

    

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisibleToBoth(boolean visible) {
        this.visibleToBoth = visible;
    }

    public boolean isVisibleToBoth() {
        return visibleToBoth;
    }

    /**
     * Checks if the pawn can move to a specific position.
     * 
     * @param position the position to check
     * @param board the game board
     * @return true if the move is valid, false otherwise
     */
    public abstract boolean canMoveTo(Position position, Board board);
    

    @Override
    public String toString() {
        return "Pawn{" +
               "type='" + type + '\'' +
               ", position=" + (currentPosition != null ? currentPosition.getStep() : "null") +
               ", path=" + (currentPosition != null ? currentPosition.getPath().getPalaceName() : "null") +
               ", owner=" + owner.getName() +
               '}';
    }
}