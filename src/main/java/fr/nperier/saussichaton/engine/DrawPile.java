package fr.nperier.saussichaton.engine;

import fr.nperier.saussichaton.rules.data.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class DrawPile extends Stack<Card> {

    public DrawPile() {
        super();
    }

    public void shuffle() {
        Collections.shuffle(this);
    }

    public Card draw() {
        return this.pop();
    }

    public List<Card> draw(int n) {
        List<Card> res = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            res.add(this.pop());
        }
        return res;
    }

    public List<Card> peek(int n) {
        Stack<Card> tempStack = new Stack<>();
        for(int i = 0; i < n; i++) {
            tempStack.push(this.pop());
        }
        List<Card> res = new ArrayList<>(tempStack);
        while(!tempStack.isEmpty()) {
            this.push(tempStack.pop());
        }
        return res;
    }

    public void push(Card card, int n) {
        for(int i = 0; i < n; i++) {
            this.push(card);
        }
    }

}
