package model;

/**
 * Interface for entities that can have a score.
 * 
 * Preconditions:
 * - The entity must have a mechanism to calculate or store scores.
 * 
 * Postconditions:
 * - Allows retrieval and modification of scores.
 * - Provides methods for score-related operations.
 */
public interface Scorable {

    /**
     * Retrieves the current score of the entity.
     * 
     * @return the current score
     */
    int getScore();

    /**
     * Sets the score of the entity.
     * 
     * @param score the new score to set
     * @throws IllegalArgumentException if the score is negative
     */
    void setScore(int score);

    /**
     * Adds points to the current score.
     * 
     * @param points the number of points to add
     * @throws IllegalArgumentException if points is negative
     */
    void addPoints(int points);

    /**
     * Subtracts points from the current score.
     * 
     * @param points the number of points to subtract
     * @throws IllegalArgumentException if points is negative
     */
    void subtractPoints(int points);

    /**
     * Resets the score to a default value (usually zero).
     */
    void resetScore();

    /**
     * Calculates and updates the score based on the entity's current state.
     * This method should be called when the game state changes in a way that
     * might affect the score.
     */
    void updateScore();

    /**
     * Compares this Scorable entity's score with another.
     * 
     * @param other the other Scorable entity to compare with
     * @return a negative integer, zero, or a positive integer as this entity's
     *         score is less than, equal to, or greater than the specified entity's score.
     * @throws NullPointerException if other is null
     */
    default int compareScore(Scorable other) {
        if (other == null) {
            throw new NullPointerException("Cannot compare with null Scorable");
        }
        return Integer.compare(this.getScore(), other.getScore());
    }
}