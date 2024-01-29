package Queues;/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> test = new RandomizedQueue<>();

        // Iterator<String> iterator = test.iterator();

        int p = 0;
        int rand;

        while (!StdIn.isEmpty()) {
            String in = StdIn.readString();
            if (k + p == 0) {
                rand = 1;
            }
            else {
                rand = StdRandom.uniformInt(k + p);
            }
            if (test.size() < k) {
                test.enqueue(in);
                p++;
            }
            else if (rand < k) {
                test.dequeue();
                test.enqueue(in);
                p++;
            }
            else {
                p++;
            }
        }

        while (!test.isEmpty()) {
            StdOut.println(test.dequeue());
        }
        // for (int a = 0; a < k; a++) {
        //     StdOut.println(iterator.next());
        // }
        //
        //     try {
        //         for (int a = 0; a < k + p; a++) {
        //             rand = StdRandom.uniformInt(k + p);
        //             String in = StdIn.readString();
        //             // StdOut.println("rand:" + rand + " k: " + k + " p:" + p);
        //             if (test.size() < k) {
        //                 test.enqueue(in);
        //                 p++;
        //             }
        //             else if (rand < k) {
        //                 test.dequeue();
        //                 test.enqueue(in);
        //                 p++;
        //             }
        //             else {
        //                 // StdOut.println("trash: " + in);
        //                 p++;
        //             }
        //         }
        //     }
        //     catch (NoSuchElementException e) {
        //         //
        //     }
        //     finally {
        //         // StdOut.println("Testing here");
        //         // while (iterator.hasNext()) {
        //         // for (int a = 0; a < k; a++) {
        //         //     StdOut.println(iterator.next());
        //     }
        //  }
    }
}


