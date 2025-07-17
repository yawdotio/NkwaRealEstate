package utilities;

import java.util.Comparator;

/**
 * Custom implementation of PriorityQueue data structure.
 * This is a binary heap that maintains elements based on their priority.
 *
 * @param <E> the type of elements held in this collection
 */
public class PriorityQueue<E> {
    private MinHeap<E> heap;

    public PriorityQueue(Comparator<? super E> comparator) {
        heap = new MinHeap<>(comparator);
    }

    public void add(E element) {
        heap.add(element);
    }

    public E poll() {
        return heap.poll();
    }

    public E peek() {
        return heap.peek();
    }

    public int size() {
        return heap.size();
    }

    public boolean isEmpty() {
        return heap.size() == 0;
    }
}
