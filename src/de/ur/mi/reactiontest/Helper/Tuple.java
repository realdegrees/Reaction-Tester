package de.ur.mi.reactiontest.Helper;

public class Tuple<E, T> {
    private E key;
    private T value;

    public Tuple (E key, T value){
        this.key = key;
        this.value = value;
    }

    public E getKey() {
        return key;
    }

    public T getValue() {
        return value;
    }
}
