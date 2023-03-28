package utility;

import java.io.Serializable;

public class Pair<T, U> implements Serializable {
    public final T first;
    public final U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }
}