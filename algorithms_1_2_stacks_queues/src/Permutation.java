import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.NoSuchElementException;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<String>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            try {
                String token = StdIn.readString();
                if (i >= k) {
                    int uniformRandom = i > 0 ? StdRandom.uniform(i+1) : 0;
                    if (uniformRandom < k) {
                        randomizedQueue.dequeue();
                        randomizedQueue.enqueue(token);
                    }
                } else {
                    randomizedQueue.enqueue(token);
                }
            } catch (NoSuchElementException e) {
                break;
            }
        }

        for (int i = 0; i < k; i++) {
            StdOut.println(randomizedQueue.dequeue());
        }
    }
}