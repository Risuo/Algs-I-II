/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Outcast {

    private final WordNet wordnet;
    private String outcast;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
        Iterable<String> allNouns = wordnet.nouns();
    }


    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {


        Map<String[], Integer> table = new HashMap<>();


        int maxDist = -1;
        // int thisDist = 0;
        int testDist = 0;
        int tableLength = 0;
        for (int i = 0; i < nouns.length; i++) {
            tableLength += i;
        }
        // StdOut.println(tableLength);
        int index = 0;
        for (int i = 0; i < tableLength - 1; i++) {
            for (int j = nouns.length - 1; j > i; j--) {
                // StdOut.println(nouns[i] + " : " + nouns[j]);
                table.put(new String[] { nouns[i], nouns[j] },
                          wordnet.distance(nouns[i], nouns[j]));
                index++;
            }
        }
        for (String noun : nouns) {
            int thisDist = 0;
            for (String[] keys : table.keySet()) {
                List<String> keyList = Arrays.asList(keys);
                if (keyList.contains(noun)) {
                    testDist = table.get(keys);
                    thisDist += testDist;
                }
            }
            if (thisDist > maxDist) {
                maxDist = thisDist;
                outcast = noun;
            }
        }


        // for (String keys : table.keySet()) {
        //     if (keys.contains("zebra")) {
        //         StdOut.println(keys);
        //
        //     }
        // }
        // for (int values : table.values()) {
        //     StdOut.println(values);
        // }
        //
        //
        // for (int i = 0; i < nouns.length; i++) {
        //     thisDist = 0;
        //     for (int j = 0; j < nouns.length; j++) {
        //         if (j != i) {
        //
        //             testDist = wordnet.distance(nouns[i], nouns[j]);
        //             // StdOut.printf("Testing %s against %s: %s\n", nouns[i], nouns[j],
        //             //               String.valueOf(testDist));
        //             thisDist += testDist;
        //         }
        //     }
        //
        //     // StdOut.println("thisDist: " + thisDist + " maxDist: " + maxDist);
        //     if (thisDist > maxDist) {
        //         maxDist = thisDist;
        //         outcast = nouns[i];
        //     }
        // }

        return outcast;
    }


    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }


}
