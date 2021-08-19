package sh.sagan.phobos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class Util {

    private Util() {}

    public static <T> List<List<T>> split(List<T> toSplit, int elementsPerPart) {
        if (toSplit.isEmpty() || elementsPerPart < 1) {
            return new ArrayList<>();
        }

        List<T> mutable = new ArrayList<>(toSplit);
        List<List<T>> returnable = new ArrayList<>();

        while (!mutable.isEmpty()) {
            List<T> inner = new ArrayList<>();
            for (int i = 0; i < elementsPerPart; i++) {
                if (mutable.isEmpty()) {
                    break;
                }
                inner.add(mutable.remove(0));
            }
            returnable.add(inner);
        }

        return returnable;
    }

    public static <T> List<List<T>> splitOnNewValue(List<T> sortedToSplit, Function<T, Integer> splitOn) {
        if (sortedToSplit.isEmpty()) {
            return new ArrayList<>();
        } else if (sortedToSplit.size() <= 1) {
            return Collections.singletonList(sortedToSplit);
        }

        List<T> mutable = new ArrayList<>(sortedToSplit);
        List<List<T>> returnable = new ArrayList<>();

        List<T> set = new ArrayList<>();
        T currentE = mutable.remove(0);
        set.add(currentE);
        T nextE = mutable.remove(0);

        while (true) {
            int current = splitOn.apply(currentE);
            int next = splitOn.apply(nextE);
            if (next != current) {
                returnable.add(set);
                set = new ArrayList<>();
                currentE = nextE;
            }
            set.add(nextE);
            if (mutable.isEmpty()) {
                returnable.add(set);
                break;
            }
            nextE = mutable.remove(0);
        }
        return returnable;
    }
}
