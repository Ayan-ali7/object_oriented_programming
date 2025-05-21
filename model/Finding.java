package model;

/**
 * Represents a finding in the game.
 * 
 * Preconditions:
 * - Name, value, and palace index must be provided when creating a Finding.
 * 
 * Postconditions:
 * - Stores the name, value, and palace index of the finding.
 * - Provides methods to access these properties.
 */
public class Finding {
    private String name;
    private int value;
    private int palaceIndex;

    /**
     * Constructs a new Finding.
     * 
     * @param name The name of the finding
     * @param value The score value of the finding
     * @param palaceIndex The index of the palace associated with this finding
     */
    public Finding(String name, int value, int palaceIndex) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Finding name cannot be null or empty");
        }
        if (value < 0) {
            throw new IllegalArgumentException("Finding value cannot be negative");
        }
        if (palaceIndex < -1 || palaceIndex > 3) {
            throw new IllegalArgumentException("Invalid palace index. Must be between -1 and 3");
        }
        
        this.name = name;
        this.value = value;
        this.palaceIndex = palaceIndex;
    }

    /**
     * Gets the name of the finding.
     * 
     * @return The name of the finding
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the palace index associated with this finding.
     * 
     * @return The palace index
     */
    public int getPalaceIndex() {
        return palaceIndex;
    }

    /**
     * Gets the score value of the finding.
     * This method is equivalent to getValue() and is provided for clarity.
     * 
     * @return The score value
     */
    public int getScoreValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Finding{" +
               "name='" + name + '\'' +
               ", value=" + value +
               ", palaceIndex=" + palaceIndex +
               '}';
    }
}