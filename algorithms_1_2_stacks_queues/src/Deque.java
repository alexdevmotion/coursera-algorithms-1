import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    public Deque() { // construct an empty deque
    }

    public boolean isEmpty() { // is the deque empty?
        return size == 0;
    }

    public int size() { // return the number of items on the deque
        return size;
    }

    public void addFirst(Item item) { // add the item to the front
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        Node newFirst = new Node(item, null, first);
        if (first != null) {
            first.previous = newFirst;
        }
        first = newFirst;
        if (last == null) {
            last = first;
        } else if (last.previous == null) {
            last.previous = first;
        }
        size++;
    }

    public void addLast(Item item) { // add the item to the end
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        Node newLast = new Node(item, last, null);
        if (last != null) {
            last.next = newLast;
        }
        last = newLast;
        if (first == null) {
            first = last;
        } else if (first.next == null) {
            first.next = last;
        }
        size++;
    }

    public Item removeFirst() { // remove and return the item from the front
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item result = first.item;
        Node newFirst = first.next;
        if (newFirst != null) {
            newFirst.previous = null;
        }
        if (first == last) {
            last = null;
        }
        first = newFirst;
        size--;

        return result;
    }

    public Item removeLast() { // remove and return the item from the end
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item result = last.item;
        Node newLast = last.previous;
        if (newLast != null) {
            newLast.next = null;
        }
        if (first == last) {
            first = null;
        }
        last = newLast;
        size--;

        return result;
    }

    public Iterator<Item> iterator() { // return an iterator over items in order from front to end
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class Node {
        public Item item;
        public Node previous;
        public Node next;

        public Node(Item item, Node previous, Node next) {
            this.item = item;
            this.previous = previous;
            this.next = next;
        }
    }

    public static void main(String[] args) { // unit testing (optional)
        Deque<Integer> deque = new Deque<Integer>();
        deque.addLast(1);
        System.out.println(deque.removeLast());

        for (Integer i : deque) {
            System.out.println(i);
        }
    }
}
