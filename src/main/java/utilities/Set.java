package utilities;

/**
 * Custom implementation of Set data structure.
 * This is a collection that does not allow duplicate elements.
 *
 * @param <E> the type of elements held in this collection
 */
public class Set<E> {
    private HashSet<E> hashSet;

    public Set() {
        hashSet = new HashSet<>();
    }

    public boolean add(E element) {
        return hashSet.add(element);
    }

    public boolean remove(E element) {
        return hashSet.remove(element);
    }

    public boolean contains(E element) {
        return hashSet.contains(element);
    }

    public int size() {
        return hashSet.size();
    }

    public boolean isEmpty() {
        return hashSet.size() == 0;
    }
}
