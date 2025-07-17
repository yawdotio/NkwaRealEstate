package utilities;

import java.util.HashMap;

/**
 * Custom implementation of HashSet data structure.
 * This is a set backed by a HashMap.
 *
 * @param <E> the type of elements held in this collection
 */
public class HashSet<E> {
    private HashMap<E, Object> map;
    private static final Object PRESENT = new Object();

    public HashSet() {
        map = new HashMap<>();
    }

    public boolean add(E element) {
        return map.put(element, PRESENT) == null;
    }

    public boolean remove(E element) {
        return map.remove(element) != null;
    }

    public boolean contains(E element) {
        return map.containsKey(element);
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.size() == 0;
    }
}
