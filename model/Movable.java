package model;

/**
 * Interface for entities that can move on the game board.
 * 
 * Preconditions:
 * - The entity must have a defined starting position.
 * - Valid movement depends on game rules and the specific entity type.
 * 
 * Postconditions:
 * - Updates the position of the entity after movement.
 * - Returns whether the movement was successful.
 */
public interface Movable {

    /**
     * Moves the entity based on the provided card.
     * 
     * @param card the card determining the movement
     * @return true if the move was successful, false otherwise
     * @throws IllegalArgumentException if the card is null
     */
    boolean move(Card card);

    /**
     * Gets the current position of the movable entity.
     * 
     * @return the current position of the entity, or null if not positioned
     */
    Position getCurrentPosition();

    /**
     * Sets the current position of the movable entity.
     * 
     * @param position the new position to set
     * @throws IllegalArgumentException if the position is null or invalid
     */
    void setPosition(Position position);

    /**
     * Checks if the entity can move to a specific position.
     * 
     * @param position the position to check
     * @return true if the move is valid, false otherwise
     * @throws IllegalArgumentException if the position is null
     */
    boolean canMoveTo(Position position);

    /**
     * Performs any necessary actions after a move is completed.
     * This method can be used for collecting findings, triggering events, etc.
     */
    default void afterMove() {
        // Default implementation does nothing
    }
}