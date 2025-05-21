package model;

import java.awt.Color;

/**
 * Represents a card in the game.
 * 
 * Preconditions:
 * - A card must have a valid type (Number, Ariadne, or Minotaur) and a color (path).
 * - Number cards must have a value between 1 and 10.
 * 
 * Postconditions:
 * - A card is created with the specified type, value (if applicable), and color.
 * 
 * Invariants:
 * - The type, value, and color of the card cannot be changed after creation.
 */
public abstract class Card {
    public enum CardType {
        NUMBER, ARIADNE, MINOTAUR
    }

    public enum CardColor {
        RED, BLUE, GREEN, YELLOW
    }

    private final CardType type;
    private final CardColor color;  // Card color (path indicator)

    /**
     * Constructs a card with the given type and color.
     * 
     * @param type the type of the card
     * @param color the color/path of the card
     */
    public Card(CardType type, CardColor color) {
        this.type = type;
        this.color = color;
    }

    /**
     * Retrieves the card's type.
     * 
     * @return the type of the card
     */
    public CardType getType() {
        return type;
    }

    /**
     * Retrieves the card's color.
     * 
     * @return the color of the card
     */
    public CardColor getColor() {
        return color;
    }

    /**
     * Retrieves the card's value. This method should be overridden by subclasses if applicable.
     * 
     * @return the value of the card, or 0 if not applicable
     */
    public abstract int getValue();

    /**
     * Applies the card's effect to a pawn's movement.
     * 
     * @param pawn the pawn to move
     * @return the number of steps to move (positive for forward, negative for backward)
     */
    public abstract int applyEffect(Pawn pawn);

    /**
     * Checks if this card can be played on the given path.
     * 
     * @param path the path to check
     * @return true if the card can be played on this path, false otherwise
     */
    public Color getCardColorAsColor() {
        switch (this.color) {
            case RED:
                return Color.RED;
            case BLUE:
                return Color.BLUE;
            case GREEN:
                return Color.GREEN;
            case YELLOW:
                return Color.YELLOW;
            default:
                return null;
        }
    }
     

    /**
     * Gets a description of the card's effect.
     * 
     * @return a string describing the card's effect
     */
    public abstract String getEffectDescription();

    /**
     * Factory method for creating cards.
     * 
     * @param type the type of card to create
     * @param color the color of the card
     * @param value the value of the card (for number cards)
     * @return the created card
     */
    public static Card createCard(CardType type, CardColor color, int value) {
        return switch (type) {
            case NUMBER -> new NumberCard(value, color);
            case ARIADNE -> new AriadneCard(color);
            case MINOTAUR -> new MinotaurCard(color);
        };
    }

    @Override
    public String toString() {
        return "Card{" +
               "type=" + type +
               ", color=" + color +
               '}';
    }
}