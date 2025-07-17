package utilities;

/**
 * Custom implementation of Queue data structure.
 * This is a FIFO (First In First Out) queue.
 *
 * @param <E> the type of elements held in this collection
 */
public class Queue<E> {
    private LinkedList<E> list = new LinkedList<>();

    public void offer(E element) {
        list.addLast(element);
    }

    public E poll() {
        return list.removeFirst();
    }

    public E peek() {
        return list.size() > 0 ? list.removeFirst() : null;
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.size() == 0;
    }
}
