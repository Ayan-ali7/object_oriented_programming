package model;

public class MinotaurCard extends Card {
    private static final int MINOTAUR_VALUE = -2;
    public MinotaurCard(CardColor color) {
            super(CardType.MINOTAUR, color);
    }

    @Override
    public int getValue() {
        return MINOTAUR_VALUE;  
    }

    @Override
    public int applyEffect(Pawn pawn) {
        return (pawn instanceof Archaeologist) ? MINOTAUR_VALUE : 0;
    }

    @Override
    public String getEffectDescription() {
        return "Move an opponent's Archaeologist pawn backward 2 steps on the " + getColor() + " path";
    }
    
    @Override
    public String toString() {
        return "MinotaurCard{" +
               "color=" + getColor() +
               '}';
    }
}
