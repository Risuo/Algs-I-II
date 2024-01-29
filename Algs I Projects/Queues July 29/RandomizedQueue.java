package Queues;
/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] bag;
    private int count = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        bag = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return (size() == 0);
    }

    // return the number of items on the randomized queue
    public int size() {
        return count;
    }

    // add the item
    public void enqueue(Item item) {
        // StdOut.println("bag.length:" + bag.length + " count: " + count);
        if (item != null) {
            if (count == bag.length) {
                resize((2 * count));
            }
            bag[count++] = item;
        }
        else {
            throw new IllegalArgumentException();
        }

    }

    // remove and return a random item
    public Item dequeue() {
        // StdOut.println("bag.length within dequeue: " + bag.length + "  count: " + count);
        if (count > 0) {
            if (count == bag.length / 4) {
                resize((bag.length / 2));
            }

            int rand;
            Item item;

            // StdOut.println("count: " + count);
            try {
                rand = StdRandom.uniformInt(count);
            }
            catch (IllegalArgumentException e) {
                rand = 0;
            }
            // StdOut.println("rand: " + rand);
            // StdOut.println("count: " + count);

            item = bag[rand];
            // StdOut.println("bag.length: " + bag.length);


            bag[rand] = bag[count - 1];
            bag[count - 1] = null;

            --count;

            return item;
        }
        else {
            throw new NoSuchElementException();
        }
    }

    private void resize(int capacity) {
        // StdOut.println("Resize triggered. ");
        Item[] copy = (Item[]) new Object[capacity];
        int j = 0;
        for (int i = 0; i < count; i++) {
            // if (bag[i] != null) {
            copy[j] = bag[i];
            j++;
        }
        bag = copy;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (count > 0) {
            int rand;
            Item item;
            rand = StdRandom.uniformInt(count);
            item = bag[rand];

            return item;
        }
        else throw new NoSuchElementException();

    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        // return new ReverseArrayIterator();
        return new RandomizedIterator();
    }


    private class RandomizedIterator implements Iterator<Item> {

        private int i = 0;
        private boolean randomized = false;

        Item[] randomCopyArr = (Item[]) new Object[0];

        private void randomCopy() {
            randomCopyArr = (Item[]) new Object[count];
            int j = 0;
            // StdOut.println("RandomCopyArr: " + randomCopyArr.length);
            // StdOut.println("bag.length: " + bag.length + " j: " + j);
            for (int k = 0; k < count; k++) {
                randomCopyArr[k] = bag[k];

            }
            StdRandom.shuffle(randomCopyArr);
        }

        public boolean hasNext() {
            if (!randomized) {
                randomCopy();
                randomized = true;
            }

            // StdOut.println("i within hasNext: " + i);
            // StdOut.println("Count within hasNext: " + count);
            return i < count;
        }

        public void remove() {
            throw new UnsupportedOperationException();
            /* not supported */
        }

        public Item next() {

            if (i == count) {
                throw new NoSuchElementException();
            }
            else {
                if (!randomized) {
                    randomCopy();
                    randomized = true;
                }
                return randomCopyArr[i++];

                // return bag[i++];
                // randomCopy(count);
                // return randomCopyArr[i++];
            }
        }

    }


    private void printList() {
        if (count > 0) {
            Iterator<Item> iterator = iterator();
            while (iterator.hasNext()) {
                Item item = iterator.next();
                StdOut.print(item + ",");
            }
            StdOut.println();
        }
        else {
            StdOut.println("Empty Array.");
        }
        // for (int a = 0; a < bag.length; a++) {
        //     StdOut.print(bag[a] + ",");
        // }
    }


    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> test = new RandomizedQueue<>();

        StdOut.println(test.isEmpty());

        StdOut.println("Start of enqueue");
        // for (int a = 10; a < 25; a++) {
        //     test.enqueue(a);
        // }
        for (int a = 0; a < 10; a++) {
            test.enqueue(0);
            test.enqueue(1);
            test.enqueue(2);
            test.printList();
            test.dequeue();
            test.printList();
            test.dequeue();
            test.printList();
            test.dequeue();
            StdOut.println("");
        }
        test.enqueue(2);
        test.enqueue(3);
        test.printList();
        test.printList();
        test.enqueue(3);
        test.printList();
        test.printList();
        test.printList();


        // for (int a = 0; a < 5; a++) {
        //     StdOut.println(test.dequeue());
        //     test.printList();
        // }

        // // StdOut.println("End of enqueue");

        // StdOut.println(test.isEmpty());
        // // test.printList();
        //
        // // StdOut.println(test.count);
        // for (int a = 10; a < 60; a++) {
        //     test.enqueue(a);
        // }
        // for (int a = 0; a < 130; a++) {
        //     // test.printList();
        //     test.dequeue();
        // }
        // for (int a = 0; a < 50; a++) {
        //     // test.printList();
        //     test.enqueue(a);
        // }
        // for (int a = 0; a < 5; a++) {
        //     // test.printList();
        //     test.dequeue();
        // }
        // StdOut.println("testing Sample:");
        // for (int a = 0; a < 10; a++) {
        //     StdOut.print(test.sample() + " , ");
        // }
        // StdOut.println("testing Sample:");
        // for (int a = 0; a < 10; a++) {
        //     StdOut.print(test.sample() + " , ");
        // }
        // StdOut.println("testing Sample:");
        // for (int a = 0; a < 10; a++) {
        //     StdOut.print(test.sample() + " , ");
        // }
        // StdOut.println(test.count + ", " + test.bag.length);
        // test.printList();
        // StdOut.println(test.count);
        // StdOut.println(test.dequeue());


        // StdOut.println(test.count);

        // for (int a = 0; a < 35; a++) {
        //     int random = test.dequeue();
        //     StdOut.println("Removing : " + random);
        //     StdOut.println("Full list of count: " + test.count);
        //     // StdOut.println("Bag length: " + test.bag.length);
        //     // test.printList();
        // }
    }

}
