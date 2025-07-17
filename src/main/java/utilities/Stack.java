package utilities;

/**
 * Custom implementation of Stack data structure.
 * This is a LIFO (Last In First Out) stack.
 *
 * @param <E> the type of elements held in this collection
 */
public class Stack<E> {
    private LinkedList<E> list = new LinkedList<>();

    public void push(E element) {
        list.addLast(element);
    }

    public E pop() {
        return list.removeLast();
    }

    public E peek() {
        return list.size() > 0 ? list.removeLast() : null;
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.size() == 0;
    }
}
