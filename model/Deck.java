package model;

import java.util.*;

import model.Card.CardColor;

/**
 * Represents a deck of cards in the game.
 * 
 * Preconditions:
 * - The deck must be initialized with the correct number and types of cards.
 * 
 * Postconditions:
 * - The deck contains all required cards.
 * - The deck can be shuffled, drawn from, and reset.
 * 
 * Invariants:
 * - The deck always contains valid Card objects.
 * - The total number of cards is consistent with the game rules.
 */
public class Deck {
    private List<Card> cards;
    private static final int NUMBER_CARDS_PER_COLOR = 10;
    private static final int ARIADNE_CARDS_PER_COLOR = 3;
    private static final int MINOTAUR_CARDS_PER_COLOR = 2;
    private static final CardColor[] COLORS = {CardColor.RED, CardColor.BLUE, CardColor.GREEN, CardColor.YELLOW};

    /**
     * Constructs a new deck with all the required cards.
     */
    public Deck() {
        this.cards = new ArrayList<>();
    }


    /**
     * Initializes the deck with all the required cards.
     */
    public void initializeDeck() {
        for (CardColor color : COLORS) {
            // Add number cards
            for (int i = 1; i <= NUMBER_CARDS_PER_COLOR; i++) {
                cards.add(new NumberCard(i, color));
                cards.add(new NumberCard(i, color));
            }
            // Add Ariadne cards
            for (int i = 0; i < ARIADNE_CARDS_PER_COLOR; i++) {
                cards.add(new AriadneCard(color));
            }
            // Add Minotaur cards
            for (int i = 0; i < MINOTAUR_CARDS_PER_COLOR; i++) {
                cards.add(new MinotaurCard(color));
            }
        }
    }
    
     
    /**
     * Shuffles the deck.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Draws a card from the top of the deck.
     * 
     * @return the drawn card, or null if the deck is empty
     */
    public Card drawCard() {
        if (isEmpty()) {
            System.out.println("Attempting to draw from an empty deck!");
            return null;
        }
        return cards.remove(0);
    }

    /**
     * Adds a card to the bottom of the deck.
     * 
     * @param card the card to add
     * @throws IllegalArgumentException if the card is null
     */
    public void addCardToBottom(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Cannot add null card to the deck");
        }
        cards.add(card);
    }

    public void addCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Cannot add null card to the deck");
        }
        cards.add(card);
    }
    /**
     * Gets the number of cards remaining in the deck.
     * 
     * @return the number of cards in the deck
     */
    public int getCardCount() {
        return cards.size();
    }

    /**
     * Checks if the deck is empty.
     * 
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Resets the deck to its initial state.
     */
    public void reset() {
        cards.clear();
        initializeDeck();
        shuffle();
    }

    /**
     * Validates the deck to ensure it contains the correct number and types of cards.
     * 
     * @return true if the deck is valid, false otherwise
     */
    public boolean isValid() {
        int expectedTotalCards = COLORS.length * (NUMBER_CARDS_PER_COLOR + ARIADNE_CARDS_PER_COLOR + MINOTAUR_CARDS_PER_COLOR);
        return cards.size() == expectedTotalCards && cards.stream().allMatch(Objects::nonNull);
    }

    @Override
    public String toString() {
        return "Deck{" +
               "cardCount=" + getCardCount() +
               "}";
    }

    public String size() {
        return String.valueOf(getCardCount());
    }
}