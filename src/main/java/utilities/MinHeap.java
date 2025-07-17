package utilities;

import java.util.Comparator;

/**
 * Custom implementation of MinHeap data structure.
 * This is a binary heap that maintains the smallest element at the root.
 *
 * @param <E> the type of elements held in this collection
 */
public class MinHeap<E> {
    private Object[] heap;
    private int size;
    private Comparator<? super E> comparator;

    private static final int DEFAULT_CAPACITY = 10;

    public MinHeap(Comparator<? super E> comparator) {
        this.heap = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }

    public void add(E element) {
        ensureCapacity();
        heap[size] = element;
        siftUp(size);
        size++;
    }

    public E poll() {
        if (size == 0) return null;
        @SuppressWarnings("unchecked")
        E result = (E) heap[0];
        heap[0] = heap[--size];
        siftDown(0);
        return result;
    }

    public E peek() {
        if (size == 0) return null;
        @SuppressWarnings("unchecked")
        return (E) heap[0];
    }

    private void ensureCapacity() {
        if (size == heap.length) {
            Object[] newHeap = new Object[heap.length * 2];
            System.arraycopy(heap, 0, newHeap, 0, heap.length);
            heap = newHeap;
        }
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            @SuppressWarnings("unchecked")
            E current = (E) heap[index];
            @SuppressWarnings("unchecked")
            E parentElement = (E) heap[parent];
            if (comparator.compare(current, parentElement) >= 0) break;
            swap(index, parent);
            index = parent;
        }
    }

    private void siftDown(int index) {
        while (index * 2 + 1 < size) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            int smallest = left;
            @SuppressWarnings("unchecked")
            E leftElement = (E) heap[left];
            @SuppressWarnings("unchecked")
            E rightElement = right < size ? (E) heap[right] : null;
            if (rightElement != null && comparator.compare(rightElement, leftElement) < 0) {
                smallest = right;
            }
            @SuppressWarnings("unchecked")
            E current = (E) heap[index];
            @SuppressWarnings("unchecked")
            E smallestElement = (E) heap[smallest];
            if (comparator.compare(current, smallestElement) <= 0) break;
            swap(index, smallest);
            index = smallest;
        }
    }

    private void swap(int i, int j) {
        Object temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
}
