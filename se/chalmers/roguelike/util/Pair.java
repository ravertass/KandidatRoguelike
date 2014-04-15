package se.chalmers.roguelike.util;
/**
 * A class for pairing two generic types
 * @author twister
 *
 * @param <F>
 * @param <S>
 */
public class Pair<F, S> {
    private F first; 
    private S second; 

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }
    /**
     * Sets the first one in the pair.
     * @param first
     */
    public void setFirst(F first) {
        this.first = first;
    }
    /**
     * Sets the second one in the pair.
     * @param second
     */
    public void setSecond(S second) {
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }
}