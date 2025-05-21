package model;

public class AriadneCard extends Card {
    private static final int ARIADNE_VALUE = 2;
    public AriadneCard(CardColor color) {
        super(CardType.ARIADNE, color);
    }

    /**
     * Retrieves the value of the card, which is 2 for Ariadne cards.
     * 
     * @return the value of the card
     */
	@Override
    public int getValue() {
        return ARIADNE_VALUE;  // Ariadne cards always move 2 steps forward
    }

    @Override
    public int applyEffect(Pawn pawn) {
        return getValue();
    }

    @Override
    public String getEffectDescription() {
        return "Move forward 2 steps";
    }

    @Override
    public String toString() {
        return "AriadneCard{" +
               "color=" + getColor() +
               '}';
    }
}

