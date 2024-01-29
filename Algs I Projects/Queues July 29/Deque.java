package Queues;/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node head;
    private Node tail;
    private int length = 0;

    private class Node {
        Item item;
        Node next;
        Node prev;

        Node(Item item) {
            this.item = item;
            this.prev = null;
            this.next = null;
        }
    }

    // construct an empty deque
    public Deque() {
        head = null;
        tail = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return length == 0;
    }

    // return the number of items on the deque
    public int size() {
        return length;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node newNode = new Node(item);
        if (head == null) {
            head = newNode;
            tail = newNode;
        }
        else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        length++;
        // StdOut.println("Printing after addFirst: " + item + ".");
        // printList(item);
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node newNode = new Node(item);
        if (head == null) {
            head = newNode;
            tail = newNode;
        }
        else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        length++;
        // StdOut.println("Printing after addLast: " + item + ".");
        // printList(item);
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (length != 0) {
            Item item = head.item;
            // StdOut.println("Printing after removeFirst: " + item + ".");
            if (head.next != null) {
                head = head.next;
                head.prev = null;
                --length;
            }
            else {
                head = null;
                tail = null;
                --length;
            }
            // printList(item);
            return item;
        }
        throw new NoSuchElementException();
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (tail != null) {
            Item item = tail.item;
            // StdOut.println("Printing after removeLast: " + item + ".");
            if (tail.prev != null) {
                tail = tail.prev;
                tail.next = null;
                --length;
            }
            else {
                head = null;
                tail = null;
                --length;
            }
            // printList(item);
            return item;
        }
        throw new NoSuchElementException();
    }

    private void printList() {
        Iterator<Item> iterator = iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            // StdOut.print(item + ",");
        }
        // StdOut.println();
    }

    private class ListIterator implements Iterator<Item> {

        private Node current = head;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            else {
                Item item = current.item;
                current = current.next;
                return item;
            }
        }

    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> test = new Deque<>();

        // StdOut.println(test.isEmpty());

        for (int a = 0; a < 5; a++) {
            test.addFirst(a);
        }
        for (int a = 5; a < 10; a++) {
            test.addLast(a);
        }
        for (int a = 0; a < 2; a++) {
            test.removeFirst();
            // StdOut.println("length: " + test.size());
            test.removeLast();
            // StdOut.println("length: " + test.size());

        }

        // StdOut.println("Testing:" + test.isEmpty() + test.size());
        test.printList();

        for (int a = 0; a < 6; a++) {
            test.removeFirst();
        }
        test.printList();
        // StdOut.println(test.isEmpty());
    }

}
