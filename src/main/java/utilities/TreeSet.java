package utilities;

import java.util.Comparator;

/**
 * Custom implementation of TreeSet data structure.
 * This is a set backed by a binary search tree.
 *
 * @param <E> the type of elements held in this collection
 */
public class TreeSet<E> {
    private TreeMap<E, Object> map;
    private static final Object PRESENT = new Object();

    public TreeSet(Comparator<? super E> comparator) {
        map = new TreeMap<>(comparator);
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
