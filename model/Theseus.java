package model;

/**
 * Represents a Theseus pawn in the game.
 * 
 * Preconditions:
 * - Theseus must follow movement rules.
 * - Theseus must be associated with a player.
 * 
 * Postconditions:
 * - Can move along specific paths on the board and may have special abilities.
 * - Can collect findings when moving to a position with a finding.
 */
public class Theseus extends Pawn {

    /**
     * Constructs a Theseus pawn.
     * 
     * @param owner The player who controls Theseus
     */
    public Theseus(Player owner) {
        super("Theseus", owner);
    }


    /**
     * Moves Theseus based on the provided card and board state.
     * 
     * @param card the card determining the movement
     * @param board the game board
     * @return true if the move was successful, false otherwise
     */
    @Override
    public boolean move(Card card, Board board) {
        if (card == null || getCurrentPosition() == null ) {
            return false;
        }

        int steps;
    if (card instanceof NumberCard) {
        steps = 1;  // Always move 1 step for number cards
    } else if (card instanceof AriadneCard) {
        steps = 2;
    } else if (card instanceof MinotaurCard) {
        steps = 2;  // Theseus moves forward with Minotaur card
    } else {
        return false;  // Invalid card type
    }
        Position newPosition = board.getNextPosition(getCurrentPosition(), steps);
        if (newPosition != null ) {
            setPosition(newPosition);

            // Check for and collect findings
            if (newPosition.hasFinding()) {
                Finding finding = collectFinding();
                if (finding != null) {
                    getOwner().addFinding(finding, type);
                }
            }

            return true;
        }

        return false;
    }

    /**
     * Special ability: Theseus can move to any position on any path.
     * 
     * @param position the position to move to
     * @param board the game board
     * @return true if the move was successful, false otherwise
     */
    public boolean specialMove(Position position, Board board) {
        if (position == null || getCurrentPosition() == null || board == null) {
            return false;
        }

        if (canMoveTo(position, board)) {
            setPosition(position);

            // Check for and collect findings
            if (position.hasFinding()) {
                Finding finding = collectFinding();
                if (finding != null) {
                    getOwner().addFinding(finding, type);
                }
            }

            return true;
        }

        return false;
    }

    /**
     * Checks if Theseus can move to a specific position.
     * 
     * @param position the position to check
     * @param board the game board
     * @return true if the move is valid, false otherwise
     */
    @Override
    public boolean canMoveTo(Position position, Board board) {
        if (position == null || getCurrentPosition() == null || board == null) {
            return false;
        }

        // Theseus can move to any position on any path, as long as it's not occupied
        return Board.isValidMove(getCurrentPosition(), position);
    }

    public boolean destroyFinding(Board board) {
        Position currentPosition = getCurrentPosition();
        if (currentPosition != null && currentPosition.hasFinding()) {
            currentPosition.removeFinding();
            return true;
        }
        return false;
    }

    public boolean ignoreFinding() {
        Position currentPosition = getCurrentPosition();
        return currentPosition != null && currentPosition.hasFinding();
    }


    @Override
    public String getSymbol() {
        return "T";
    }

    @Override
    public String toString() {
        return "T" + getOwner().getPlayerIndex();
    }
}