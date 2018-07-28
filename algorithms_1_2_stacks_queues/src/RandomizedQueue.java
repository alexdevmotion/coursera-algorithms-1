import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int n = 0;
    private Item[] q = (Item[]) new Object[0];

    public RandomizedQueue() { // construct an empty randomized queue
    }

    public boolean isEmpty() { // is the randomized queue empty?
        return size() == 0;
    }

    public int size() { // return the number of items on the randomized queue
        return n;
    }

    private Item[] copy(int size) {
        Item[] copy = (Item[]) new Object[size];
        for (int i = 0; i < n; i++)
            copy[i] = q[i];
        return copy;
    }

    private void resize(int capacity) {
        q = copy(capacity);
    }

    public void enqueue(Item item) { // add the item
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (n == q.length) {
            int newSize = (n == 0) ? 1 : 2*n;
            resize(newSize);
        }
        q[n++] = item;
    }

    public Item dequeue() { // remove and return a random item
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int uniformRandom = n > 0 ? StdRandom.uniform(n) : 0;
        Item i = q[uniformRandom];
        if (uniformRandom != (n-1)) {
            q[uniformRandom] = q[n-1];
        }
        q[n-1] = null;
        n--;
        if (n > 0 && n == q.length/4) resize(q.length/2);
        return i;
    }

    public Item sample() { // return a random item (but do not remove it)
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int uniformRandom = StdRandom.uniform(n);
        return q[uniformRandom];
    }

    private void shuffle() {
        for (int i = 1; i < n; i++) {
            int uniformRandom = StdRandom.uniform(i+1);
            Item aux = q[i];
            q[i] = q[uniformRandom];
            q[uniformRandom] = aux;
        }
    }

    public Iterator<Item> iterator() { // return an independent iterator over items in random order
        shuffle();
        return new RandomizedQueueIterator(copy(n));
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i = 0;
        private final Item[] randq;

        public RandomizedQueueIterator(Item[] q) {
            this.randq = q;
        }

        @Override
        public boolean hasNext() {
            return i < n;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return randq[i++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) { // unit testing (optional)
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.enqueue(325);
        rq.enqueue(113);
        rq.enqueue(222);
        rq.enqueue(5);
        rq.enqueue(87);

        for (Integer i : rq) {
            System.out.println(i);
        }
    }
}