package model;

public class Archaeologist extends Pawn {

    public Archaeologist(Player owner) {
        super("Archaeologist", owner);
    }

    @Override
public boolean move(Card card, Board board) {
    if (card == null || getCurrentPosition() == null || board == null) {
        return false;
    }

    int steps = 1;
    if (card instanceof NumberCard) {
        steps = 1;
    }
    else if (card instanceof AriadneCard) {
        steps = 2;
    } else if (card instanceof MinotaurCard) {
        steps = -2;
    }

    Path currentPath = getCurrentPosition().getPath();
    int currentStep = getCurrentPosition().getStep();
    int newStep = currentStep + steps;

    // Ensure the new step is within the path bounds
    if (newStep < 0) {
        newStep = 0;
    } else if (newStep >= currentPath.getLength()) {
        newStep = currentPath.getLength() - 1;
    }

    Position newPosition = currentPath.getPositionAt(newStep);
    if (newPosition != null ) {
        setPosition(newPosition);

        // Check for and collect findings
        if (newPosition.hasFinding()) {
            Finding finding = collectFinding();
            if (finding != null) {
                getOwner().addFinding(finding, currentPath.getPalaceName());
            }
        }

        return true;
    }

    return false;
}

@Override
public String toString() {
    return "A" + getOwner().getPlayerIndex();
}
    
public boolean isValidMove(Pawn pawn, Position from, Position to) {
        // Check if the move is within the same path
        if (!from.getPath().equals(to.getPath())) {
            return false;
        }

        // Check if the destination is occupied
        if (to.hasPawn()) {
            return false;
        }

        // Check if the move is forward (for Archaeologist)
        if (pawn instanceof Archaeologist) {
            return to.getStep() > from.getStep();
        }

        // For Theseus, allow movement to any unoccupied position
        return true;
    }
        @Override
        public boolean canMoveTo(Position position, Board board) {
            if (position == null || getCurrentPosition() == null || board == null) {
                return false;
            }
        
            // Archaeologists can only move within the same path
            if (!position.getPath().equals(getCurrentPosition().getPath())) {
                return false;
            }
        
            // Check if the position is occupied
            if (position.hasPawn()) {
                return false;
            }
        
            int currentStep = getCurrentPosition().getStep();
            int newStep = position.getStep();
        
            // For Minotaur card (moving backwards)
            if (newStep < currentStep) {
                return true;
            }
        
            // For normal forward movement
            return newStep > currentStep && Board.isValidMove(getCurrentPosition(), position);
        }
    @Override
    public String getSymbol() {
        return "A";
    }
    }