package model;
       
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import model.Card.CardColor;
        
    public class Board {
        private final List<Path> paths;
        private final List<Finding> findings;
        private static final int NUM_PATHS = 4;
        private static final int STEPS_PER_PATH = 10;
        private static final int[] FINDING_POSITIONS = {2, 4, 6, 8};
        private static final int NUM_STATUETTES = 10;
        private static final int NUM_MURALS = 6;
        private static final int CHECKPOINT_STEP = 7; 
        private static final int PAWNS_PAST_CHECKPOINT_FOR_GAME_OVER = 4;
        /**
         * Constructs a new Board and initializes paths and findings.
        */
        public Board() {
            this.paths = new ArrayList<>();
            this.findings = new ArrayList<>();
            initializeBoard();
        }
    
        private void initializeBoard() {
                initializePaths();
                initializeRareFindings();
                initializeStatuettes();
                initializeMurals();
                placeFindings();
                placeRareFindings();
        }
        
        public Position getStartPosition() {
            // Assuming the start position is the first position of the first path
            if (!paths.isEmpty()) {
                Path firstPath = paths.get(0);
                return firstPath.getPositionAt(0);
            }
            return null;
        }
        
        public void initializePaths() {
            System.out.println("Initializing " + NUM_PATHS + " paths with " + STEPS_PER_PATH + " steps each.");
            paths.clear(); 
            CardColor[] Colors = {CardColor.RED, CardColor.BLUE, CardColor.GREEN, CardColor.YELLOW};
            for (int i = 0; i < NUM_PATHS; i++) {
                Path path = new Path(i, "Palace " + (i + 1), Colors[i]);
                for (int j = 0; j < STEPS_PER_PATH; j++) {
                    Position position = new Position(j, path);
                    path.addPosition(position);
                }
                paths.add(path);
                System.out.println("Path " + i + " initialized with " + STEPS_PER_PATH + " positions.");
            }
            System.out.println("Total paths initialized: " + paths.size());
        }

    private void initializeRareFindings() {
            findings.add(new Finding("Disc of Phaistos", 35, 0));
            findings.add(new Finding("Ring of Minos", 25, 1));
            findings.add(new Finding("Jewel of Malia", 25, 2));
            findings.add(new Finding("Ryto of Zakros", 25, 3));
    }
    
    private void initializeStatuettes() {
            for (int i = 0; i < NUM_STATUETTES; i++) {
                findings.add(new Finding("Statuette", 10, -1));
            }
    }
    
    private void initializeMurals() {
            int[] muralValues = {20, 20, 15, 15, 15, 20};
            for (int i = 0; i < NUM_MURALS; i++) {
                findings.add(new Finding("Mural"+i, muralValues[i], -1));
            }
    }
    private void placeFindings() {
            System.out.println("Number of paths: " + paths.size());
            System.out.println("Number of findings: " + findings.size());

            Random random = new Random();
            List<Finding> unplacedFindings = new ArrayList<>(findings);
            unplacedFindings.removeIf(f -> f.getPalaceIndex() != -1); // Remove rare findings
    
            for (Path path : paths) {
                List<Integer> availablePositions = new ArrayList<>(Arrays.asList(Arrays.stream(FINDING_POSITIONS).boxed().toArray(Integer[]::new)));
    
                while (!availablePositions.isEmpty() && !unplacedFindings.isEmpty()) {
                    int positionIndex = availablePositions.remove(random.nextInt(availablePositions.size()));
                    Position position = path.getPositionAt(positionIndex);
    
                    if (position != null && !position.hasFinding()) {
                        Finding finding = unplacedFindings.remove(random.nextInt(unplacedFindings.size()));
                        position.setFinding(finding);
                        System.out.println("Placed finding " + finding.getName() + " at path " + path.getPathIndex() + ", position " + positionIndex);
                    }
                }
            }
            if (!unplacedFindings.isEmpty()) {
                System.out.println("Warning: " + unplacedFindings.size() + " findings could not be placed.");
            }
    
            System.out.println("Finished placing findings.");
    }
    
   

    private void placeRareFindings() {
            for (Finding finding : findings) {
                if (finding.getPalaceIndex() != -1) {
                    Path path = paths.get(finding.getPalaceIndex());
                    path.getPositionAt(STEPS_PER_PATH - 1).setFinding(finding);
                }
            }
        
        }
        
    /**
     * Gets the path at the specified index.
     *
     * @param index the index of the path
     * @return the Path at the specified index
     * @throws IllegalArgumentException if the index is invalid
     */
    public List<Path> getPaths() {
        return new ArrayList<>(paths);
    }

    public Path getPath(int index) {
        if (index < 0 || index >= NUM_PATHS) {
            throw new IllegalArgumentException("Invalid path index: " + index);
        }
        return paths.get(index);
    }

    /**
     * Checks if a position is a palace (last step of a path).
     *
     * @param position the position to check
     * @return true if the position is a palace, false otherwise
     */
    public boolean isPalace(Position position) {
        return position.getStep() == STEPS_PER_PATH - 1;
    }

     /**
    * Gets the next position from the current position, moving the specified number of steps.
    *
    * @param currentPosition the current position
    * @param steps the number of steps to move (can be positive or negative)
     * @return the next position, or null if the move is invalid
    */
   
    public Position getNextPosition(Position currentPosition, int steps) {
        if (currentPosition == null) {
            return null;
        }
    
        Path currentPath = currentPosition.getPath();
        if (currentPath == null) {
            return null;
        }
    
        int currentStep = currentPosition.getStep();
        int newStep = currentStep + steps;
    
        if (newStep >= 0 && newStep < currentPath.getLength()) {
            return currentPath.getPositionAt(newStep);
        } else if (newStep < 0) {
            return currentPath.getPositionAt(0);
        } else {
            return currentPath.getPalacePosition();
        }
    }
    

    /**
     * Calculates the score for a position on a path.
     *
     * @param position the position to calculate the score for
     * @return the score for the position
     */
    public static boolean isValidMove(Position from, Position to) {
        if (from == null || to == null) {
            return false;
        }
    
        // Check if the positions are on the same path
        if (from.getPath() != to.getPath()) {
            return false;
        }
    
        // Check if the move is in the correct direction (forward)
        int steps = to.getStep() - from.getStep();
        return steps > 0 && steps <= 8; // Ensure the move is forward and within the path length
    }
    
    /**
     * Resets the board by clearing all findings and pawns, then reinitializing findings.
     */
    public void resetBoard() {
        for (Path path : paths) {
            for (Position position : path.getPositions()) {
                position.setFinding(null);
                position.setPawn(null);
            }
        }
    }

    /**
     * Places a pawn on a specific position.
     *
     * @param pawn the pawn to place
     * @param position the position to place the pawn on
     */
    public void placePawn(Pawn pawn, Position position) {
        position.setPawn(pawn);
        pawn.setPosition(position);
    }

    /**
     * Removes a pawn from a position.
     *
     * @param position the position to remove the pawn from
     */
    public void removePawn(Position position) {
        Pawn pawn = position.getPawn();
        if (pawn != null) {
            position.setPawn(null);
            pawn.setPosition(null);
        }
    }

    /**
     * Gets all pawns currently on the board.
     *
     * @return a list of all pawns on the board
     */
    public List<Pawn> getAllPawns() {
        List<Pawn> pawns = new ArrayList<>();
        for (Path path : paths) {
            for (Position position : path.getPositions()) {
                if (position.hasPawn()) {
                    pawns.add(position.getPawn());
                }
            }
        }
        return pawns;
    }

    /**
     * Gets all available moves for a pawn given a number of steps.
     *
     * @param pawn the pawn to move
     * @param steps the number of steps to move
     * @return a list of available positions to move to
     */
    public List<Position> getAvailableMoves(Pawn pawn, int steps) {
        List<Position> availableMoves = new ArrayList<>();
        Position currentPosition = pawn.getCurrentPosition();
        Path currentPath = paths.get(currentPosition.getPathIndex());

        int newStep = currentPosition.getStep() + steps;
        if (newStep >= 0 && newStep < STEPS_PER_PATH) {
            Position newPosition = currentPath.getPositionAt(newStep);
            availableMoves.add(newPosition);
        }

        return availableMoves;
    }
    
        public void printBoardState() {
            for (Path path : paths) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 9; i++) {  
                    Position position = path.getPositionAt(i);
                    sb.append(position.toString()).append(" ");
                }
                System.out.println(sb.toString().trim());
            }
        }
    /**
     * Switches a pawn to a new path at the same step.
     *
     * @param pawn the pawn to switch
     * @param newPathIndex the index of the new path
     * @return the new position if the switch was successful, or the current position if not
     */

    /**
     * Gets a path by its palace name.
     *
     * @param palaceName the name of the palace
     * @return the Path with the specified palace name, or null if not found
     */
    public Path getPathByPalaceName(String palaceName) {
        for (Path path : paths) {
            if (path.getPalaceName().equalsIgnoreCase(palaceName)) {
                return path;
            }
        }
        return null;
    }

    public void updatePawnVisibility(int currentPlayerIndex) {
        for (Path path : paths) {
            for (Position position : path.getPositions()) {
                Pawn pawn = position.getPawn();
                if (pawn != null) {
                    Player owner = pawn.getOwner();
                    boolean isVisible = (owner.getPlayerIndex() == currentPlayerIndex);
                    pawn.setVisible(isVisible);
                }
            }
        }
    }
    
    public boolean isGameOver() {
        int pawnsPastCheckpoint = 0;
        for (Path path : paths) {
            for (int i = CHECKPOINT_STEP; i < path.getLength(); i++) {
                Position position = path.getPositionAt(i);
                if (position.hasPawn()) {
                    pawnsPastCheckpoint++;
                    if (pawnsPastCheckpoint >= PAWNS_PAST_CHECKPOINT_FOR_GAME_OVER) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
       
}