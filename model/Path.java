package model;

import java.util.List;
import java.util.ArrayList;
import model.Card.CardColor;

/**
 * Represents a path that pawns follow to reach a palace.
 * 
 * Preconditions:
 * - A path must be initialized with 9 positions.
 * 
 * Postconditions:
 * - The path contains 9 positions for pawns to move along.
 * - The last position represents the palace.
 */
public class Path {
    private List<Position> positions;
    private static final int PATH_LENGTH = 10;
    private int pathIndex;
    private String palaceName;
    private CardColor color;
    /**
     * Initializes a path with 9 positions.
     * 
     * @param pathIndex the index of this path on the board
     * @param palaceName the name of the palace at the end of this path
     */
    public Path(int pathIndex, String palaceName, CardColor color) {
        this.pathIndex = pathIndex;
        this.palaceName = palaceName;
        this.color = color;
        this.positions = new ArrayList<>(PATH_LENGTH);
        initializePositions();
    }

    
    private void initializePositions() {
        for (int i = 0; i < PATH_LENGTH; i++) {
            positions.add(new Position(i, this));
        }
    }

    public void addPosition(Position position) {
        positions.add(position);
    }

        /**
     * Sets a pawn on a specific position in this path.
     * 
     * @param pawn the pawn to set
     * @param index the index of the position
     * @return true if the pawn was successfully set, false otherwise
     */
    public boolean setPawnOnPosition(Pawn pawn, int index) {
        if (index >= 0 && index < PATH_LENGTH) {
            Position position = positions.get(index);
            if (position!=null)  {
                position.setPawn(pawn);
                return true;
            }
        }
        return false;
    }

        /**
     * Removes a pawn from a specific position in this path.
     * 
     * @param index the index of the position
     * @return the removed pawn, or null if no pawn was present
     */
    public Pawn removePawnFromPosition(int index) {
        if (index >= 0 && index < PATH_LENGTH) {
            Position position = positions.get(index);
            Pawn pawn = position.getPawn();
            position.setPawn(null);
            return pawn;
        }
        return null;
    }
    
    /**
     * Retrieves the list of positions in this path.
     * 
     * @return the list of positions
     */
    public List<Position> getPositions() {
        return new ArrayList<>(positions); // Return a copy to preserve encapsulation
    }

    /**
     * Retrieves the position at a specific index on the path.
     * 
     * @param index the index of the position on the path
     * @return the position at the given index, or null if out of range
     */
    public Position getPositionAt(int index) {
        if (index >= 0 && index < positions.size()) {
            return positions.get(index);
        }
        return null;
    }

    /**
     * Checks if a position is the palace (end of the path).
     * 
     * @param position the position to check
     * @return true if the position is the palace, false otherwise
     */
    public boolean isPalace(Position position) {
        return position.getStep() == PATH_LENGTH - 1;
    }

    /**
     * Gets the palace position of this path.
     * 
     * @return the palace position
     */
    public Position getPalacePosition() {
        return positions.get(PATH_LENGTH - 1);
    }

    /**
     * Gets the index of this path on the board.
     * 
     * @return the path index
     */
    public int getPathIndex() {
        return pathIndex;
    }

    /**
     * Gets the name of the palace at the end of this path.
     * 
     * @return the palace name
     */
    public String getPalaceName() {
        return palaceName;
    }

    /**
     * Gets the length of the path.
     * 
     * @return the path length
     */
    public int getLength() {
        return PATH_LENGTH;
    }

    /**
     * Checks if a specific position on the path has a finding.
     * 
     * @param index the index of the position to check
     * @return true if the position has a finding, false otherwise
     */
    public boolean hasFindingAtPosition(int index) {
        Position position = getPositionAt(index);
        return position != null && position.hasFinding();
    }

    /**
     * Gets the next position given a current position and number of steps.
     * 
     * @param currentPosition the current position
     * @param steps the number of steps to move
     * @return the next position, or null if out of bounds
     */
    public Position getNextPosition(Position currentPosition, int steps) {
        int currentIndex = positions.indexOf(currentPosition);
        int nextIndex = currentIndex + steps;
        if (nextIndex >= 0 && nextIndex < PATH_LENGTH) {
            return positions.get(nextIndex);
        }
        return null;
    }

    public void setcolor(CardColor color) {
        this.color=color;}
    /**
     * Gets the color associated with this path.
     * 
     * @return the color of the path
     */
    public CardColor getColor() {
        return color;
    }
       @Override
    public String toString() {
        return "Path{" +
               "pathIndex=" + pathIndex +
               ", palaceName='" + palaceName + '\'' +
               '}';
    }
}