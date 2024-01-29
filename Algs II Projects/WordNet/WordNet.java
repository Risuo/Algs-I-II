/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TopologicalX;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordNet {


    private final Map<Integer, String[]> wordNet;
    // private final ArrayList<Integer> hypernyms;
    private Set<String> allNouns;
    private int vertexCount;
    private Digraph digraph;
    private Set<Integer> keySetA;
    private Set<Integer> keySetB;
    private int length;
    private SAP sap;
    private final Map<Integer, Set<String>> synsetIdToNouns = new HashMap<>();
    private final Map<String, Set<Integer>> nounToSynsetIds = new HashMap<>();


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        wordNet = wordNet(synsets);

        digraph = new Digraph(vertexCount);

        digraph = hypernyms(hypernyms);

        sap = new SAP(digraph);

        nouns();


    }

    private Digraph hypernyms(String hypernyms) {
        In in = new In(hypernyms);
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] parts = line.split(",");
            for (int i = 1; i < parts.length; i++) {
                digraph.addEdge(Integer.parseInt(parts[0]), Integer.parseInt(parts[i]));
            }
        }
        TopologicalX topological1 = new TopologicalX(digraph);
        if (!topological1.hasOrder()) {
            throw new IllegalArgumentException("digraph is not a DAG");
        }
        return digraph;
    }

    private Digraph getDigraph() {
        return this.digraph;
    }


    private Map<Integer, String[]> wordNet(String synset) {
        Map<Integer, String[]> wordNet = new HashMap<>();
        In in = new In(synset);
        int count = 0;
        while (!in.isEmpty()) {
            String line = in.readLine();
            count++;
            String[] parts = line.split(",", 3);

            int key = Integer.parseInt(parts[0]);
            String[] nouns = parts[1].split(" ");

            // for (int i = 0; i < nouns.length; i++) {
            //     // StdOut.println("key: " + key + " nouns: " + nouns[i]);
            //     nouns[i] = nouns[i].replace("_", " ");
            // }
            wordNet.put(key, nouns);
            for (String noun : nouns) {
                addNounToSynset(count - 1, noun);
            }
        }
        vertexCount = count;
        return wordNet;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {

        allNouns = new HashSet<>();

        for (String[] nouns : wordNet.values()) {
            for (String noun : nouns) {
                allNouns.add(noun);
            }
        }
        // StdOut.println("length of allNouns: " + (long) allNouns.size());
        return allNouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word != null) {
            return (allNouns.contains(word));
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        // SAP sap = new SAP(digraph);
        // Iterable<String> nouns = nouns();
        // StdOut.println(nounA + " " + nounB);
        if (isNoun(nounA) && isNoun(nounB)) {

            keySetA = getSynsetIdsContainingNoun(nounA);
            keySetB = getSynsetIdsContainingNoun(nounB);
        }
        length = sap.length(keySetA, keySetB);
        return length;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        // SAP sap = new SAP(digraph);
        // Iterable<String> nouns = nouns();
        if (isNoun(nounA) && isNoun(nounB)) {

            keySetA = getSynsetIdsContainingNoun(nounA);
            keySetB = getSynsetIdsContainingNoun(nounB);
        }
        int ancestor = sap.ancestor(keySetA, keySetB);
        // length = sap.length(keySetA, keySetB);
        // Set<String> output = Set.(getNounsInSynset(ancestor));
        String output = removeBrackets(Arrays.toString(wordNet.get(ancestor)));
        output = output.replace(",", "");
        return output;
    }

    private static String removeBrackets(String input) {
        return input.substring(1, input.length() - 1);
    }

    private void addNounToSynset(int synsetId, String noun) {
        synsetIdToNouns.computeIfAbsent(synsetId, k -> new HashSet<>()).add(noun);

        nounToSynsetIds.computeIfAbsent(noun, k -> new HashSet<>()).add(synsetId);
    }

    private Set<Integer> getSynsetIdsContainingNoun(String noun) {
        return nounToSynsetIds.getOrDefault(noun, Collections.emptySet());
    }

    private Set<String> getNounsInSynset(int synsetId) {
        return synsetIdToNouns.getOrDefault(synsetId, Collections.emptySet());
    }


    // private Set<Integer> synsetsContains(String noun) {
    //
    //     Set<Integer> synsetId = getSynsetIdsContainingNoun(noun);
    //     // ArrayList<Integer> keySet1 = new ArrayList<Integer>(synsetId);
    //
    //     // ArrayList<Integer> keySet2 = new ArrayList<Integer>();
    //     // for (int key : wordNet.keySet()) {
    //     //     if (Arrays.asList(wordNet.get(key)).contains(noun)) {
    //     //         keySet2.add(key);
    //     //         // StdOut.println(key + Arrays.toString(wordNet.get(key)));
    //     //     }
    //     // }
    //     // for (int a : keySet1) {
    //     //     StdOut.println(noun + " A:" + a + " " + getNounsInSynset(a));
    //     // }
    //     // for (int b : keySet2) {
    //     //     StdOut.println(noun + " B:" + b + " " + getNounsInSynset(b));
    //     // }
    //
    //     return synsetId;
    // }

    // do unit testing of this class
    public static void main(String[] args) {
        String inA = args[0];
        String inB = args[1];
        WordNet wordNet = new WordNet(inA, inB);
        // Iterable<String> allNouns = wordNet.nouns();
        // StdOut.println(wordNet.isNoun("anamorphosis"));
        // StdOut.println(wordNet.hashCode());
        // StdOut.println(wordNet.synsetsContains("bird"));
        // StdOut.println("next check");

        // while (!StdIn.isEmpty()) {
        //     int v = StdIn.readInt();
        //     int w = StdIn.readInt();
        //     int length = sap.length(v, w);
        //     int ancestor = sap.ancestor(v, w);
        //     StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        // }

        while (!StdIn.isEmpty()) {
            String nounA = StdIn.readString();
            String nounB = StdIn.readString();
            Set<Integer> synsetIdA = wordNet.getSynsetIdsContainingNoun(nounA);
            Set<Integer> synsetIdB = wordNet.getSynsetIdsContainingNoun(nounB);
            for (int id : synsetIdA) {
                StdOut.println(wordNet.getNounsInSynset(id));
            }
            StdOut.println("\n");
            for (int id : synsetIdB) {
                StdOut.println(wordNet.getNounsInSynset(id));
            }

            String ancestor = wordNet.sap(nounA, nounB);
            int length = wordNet.distance(nounA, nounB);
            StdOut.println("sap: " + ancestor);
            StdOut.println("length: " + length);

            StdOut.println(wordNet.getNounsInSynset(81683));
            // StdOut.printf("is %s within the synset: %b\n", check, contains);
        }

        // String nounA = args[2];
        // String nounB = args[3];
        //
        //
        // String ancestor = wordNet.sap(nounA, nounB);
        //
        // StdOut.println("sap: " + ancestor);
        // StdOut.println("length: " + wordNet.distance(nounA, nounB));


        // DeluxBFS deluxBFS = new DeluxBFS(wordNet.getDigraph(), nounA, nounB);

        // while (!StdIn.isEmpty()) {
        //     String check = StdIn.readString();
        //     StdOut.println("This is the string: " + check);
        //     boolean contains = wordNet.isNoun(check);
        //     StdOut.printf("is %s within the synset: %b\n", check, contains);
        // }

        // for (int key : wordNet.wordNet.keySet()) {
        //     StdOut.println(key + ": " + Arrays.toString(wordNet.wordNet.get(key)));
        // }


        // for (String word : allNouns) {
        //     StdOut.println("word: " + word + (Objects.equals(word, "f")));
        // }
        // StdOut.println("length of allNouns: " + allNouns);


        // Map<Integer, String[]> map = new HashMap<>();
        // while (!in.isEmpty()) {
        //     String line = in.readLine();
        //     String[] parts = line.split(",", 3);
        //     // for (String a : parts) {
        //     //     StdOut.println(a + " a ");
        //     // }
        //
        //     int key = Integer.parseInt(parts[0]);
        //     String[] nouns = parts[1].split(" ");
        //
        //     for (int i = 0; i < nouns.length; i++) {
        //         nouns[i] = nouns[i].replace("_", " ");
        //     }
        //
        //     map.put(key, nouns);
        //
        // }
        //
        // for (int key : map.keySet()) {
        //     StdOut.println(key + ": " + Arrays.toString(map.get(key)));
        // }


    }
}
