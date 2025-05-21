package model;

public class NumberCard extends Card {
    private final int value;

    public NumberCard(int value, CardColor color) {
        super(CardType.NUMBER, color);
        if (value < 1 || value > 10) {
            throw new IllegalArgumentException("Number card value must be between 1 and 10.");
        }
        this.value = value;
    }

    public int getNumber() {
        return value;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public int applyEffect(Pawn pawn) {
        return value;
    }

    @Override
    public String getEffectDescription() {
        return "Move forward " + value + " steps";
    }

    @Override
    public String toString() {
        return "NumberCard{" +
               "value=" + value +
               ", color=" + getColor() +
               '}';
    }
}

