package utilities;

/**
 * Custom implementation of Array data structure.
 * This is a fixed-size array.
 *
 * @param <E> the type of elements held in this collection
 */
public class Array<E> {
    private Object[] elements;

    public Array(int size) {
        elements = new Object[size];
    }

    public void set(int index, E element) {
        elements[index] = element;
    }

    public E get(int index) {
        return (E) elements[index];
    }

    public int size() {
        return elements.length;
    }
}
