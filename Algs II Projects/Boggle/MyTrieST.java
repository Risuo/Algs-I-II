/******************************************************************************
 *  Compilation:  javac TrieST.java
 *  Execution:    java TrieST < words.txt
 *  Dependencies: StdIn.java
 *  Data files:   https://algs4.cs.princeton.edu/52trie/shellsST.txt
 *
 *  A string symbol table for extended ASCII strings, implemented
 *  using a 256-way trie.
 *
 *  % java TrieST < shellsST.txt
 *  by 4
 *  sea 6
 *  sells 1
 *  she 0
 *  shells 3
 *  shore 7
 *  the 5
 *
 ******************************************************************************/


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Map;

public class MyTrieST<Value> {
    private static final int R = 26;        // extended ASCII
    private static final Map<Integer, Integer> Points_Map = new HashMap<>();

    private Node root;      // root of trie
    private int n;          // number of keys in trie

    // R-way trie node
    private static class Node {
        private Object val;
        private Object score;
        private Boolean isNewWord;
        private Node[] next = new Node[R];
    }

    /**
     * Initializes an empty string symbol table.
     */
    public MyTrieST() {
        Points_Map.put(3, 1);
        Points_Map.put(4, 1);
        Points_Map.put(5, 2);
        Points_Map.put(6, 3);
        Points_Map.put(7, 5);
        Points_Map.put(8, 11);
    }

    public Boolean isNewWord(String key) {
        if (key == null) throw new IllegalArgumentException("argument to isNewWord() is null");
        Node x = isNewWord(root, key, 0);
        if (x == null) return null;
        return x.isNewWord;
    }

    public Value getScore(String key) {
        if (key == null) throw new IllegalArgumentException("argument to getScore() is null");
        Node x = getScore(root, key, 0);
        if (x == null) return null;
        return (Value) x.score;
    }

    public void oldWord(String key) {
        if (key == null) throw new IllegalArgumentException("argument to oldWord() is null");
        Node x = get(root, key, 0);
        x.isNewWord = false;
    }

    public void wordReset(String key) {
        if (key == null) throw new IllegalArgumentException("argument to wordReset() is null");
        Node x = get(root, key, 0);
        x.isNewWord = true;
    }


    /**
     * Returns the value associated with the given key.
     *
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     * and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(String key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        Node x = get(root, key, 0);
        if (x == null) return null;
        return (Value) x.val;
    }

    /**
     * Does this symbol table contain the given key?
     *
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     * {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    private Node isNewWord(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        int index = c - 'A';
        return isNewWord(x.next[index], key, d + 1);
    }

    private Node getScore(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        int index = c - 'A';
        return getScore(x.next[index], key, d + 1);
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        int index = c - 'A';
        return get(x.next[index], key, d + 1);
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     *
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(String key, Value val) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        if (val == null) delete(key);
        else root = put(root, key, val, 0);
    }

    // public void switchWord(String key, Value val) {
    //     if (key == null) throw new IllegalArgumentException("first argument to switchWord is null");
    //     if (val == null) delete(key);
    //     else root = switchWord(root, key, val, 0);
    // }


    public static Integer scoreWord(int length) {
        if (length < 3) return 0;
        if (length < 8) return Points_Map.get(length);
        else return Points_Map.get(8);
    }


    // private Node switchWord(Node x, String key, Value val, int d) {
    //     if (x == null) x = new Node();
    //     if (d == key.length()) {
    //         if (x.val == null) n++;
    //         StdOut.println("within switchWord" + x.val + " " + x.isNewWord);
    //         x.isNewWord = false;
    //         StdOut.println("post change: " + x.isNewWord);
    //         return x;
    //     }
    //     char c = key.charAt(d);
    //     int index = c - 'A';
    //     x.next[index] = switchWord(x.next[index], key, val, d + 1);
    //     return x;
    // }


    private Node put(Node x, String key, Value val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (x.val == null) n++;
            x.val = val;
            x.score = scoreWord(key.length());
            x.isNewWord = true;
            return x;
        }
        char c = key.charAt(d);
        int index = c - 'A';
        x.next[index] = put(x.next[index], key, val, d + 1);
        return x;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     *
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * Is this symbol table empty?
     *
     * @return {@code true} if this symbol table is empty and {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     *
     * @return all keys in the symbol table as an {@code Iterable}
     */
    public Iterable<String> keys() {
        return keysWithPrefix("");
    }

    /**
     * Returns all of the keys in the set that start with {@code prefix}.
     *
     * @param prefix the prefix
     * @return all of the keys in the set that start with {@code prefix},
     * as an iterable
     */
    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> results = new Queue<String>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        return results;
    }

    public boolean hasKeysWithPrefix(String prefix) {
        Node x = get(root, prefix, 0);
        return (x != null);
    }

    private void collect(Node x, StringBuilder prefix, Queue<String> results) {
        if (x == null) return;
        if (x.val != null) results.enqueue(prefix.toString());
        for (char c = 'A'; c <= 'Z'; c++) {
            int index = c - 'A';
            prefix.append(c);
            collect(x.next[index], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /**
     * Returns all of the keys in the symbol table that match {@code pattern},
     * where the character '.' is interpreted as a wildcard character.
     *
     * @param pattern the pattern
     * @return all of the keys in the symbol table that match {@code pattern},
     * as an iterable, where . is treated as a wildcard character.
     */
    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> results = new Queue<String>();
        collect(root, new StringBuilder(), pattern, results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, String pattern, Queue<String> results) {
        if (x == null) return;
        int d = prefix.length();
        if (d == pattern.length() && x.val != null)
            results.enqueue(prefix.toString());
        if (d == pattern.length())
            return;
        char c = pattern.charAt(d);
        if (c == '.') {
            for (char ch = 0; ch < R; ch++) {
                prefix.append(ch);
                collect(x.next[ch], prefix, pattern, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        else {
            prefix.append(c);
            collect(x.next[c], prefix, pattern, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /**
     * Returns the string in the symbol table that is the longest prefix of {@code query},
     * or {@code null}, if no such string.
     *
     * @param query the query string
     * @return the string in the symbol table that is the longest prefix of {@code query},
     * or {@code null} if no such string
     * @throws IllegalArgumentException if {@code query} is {@code null}
     */
    public String longestPrefixOf(String query) {
        if (query == null)
            throw new IllegalArgumentException("argument to longestPrefixOf() is null");
        int length = longestPrefixOf(root, query, 0, -1);
        if (length == -1) return null;
        else return query.substring(0, length);
    }

    // returns the length of the longest string key in the subtrie
    // rooted at x that is a prefix of the query string,
    // assuming the first d character match and we have already
    // found a prefix match of given length (-1 if no such match)
    private int longestPrefixOf(Node x, String query, int d, int length) {
        if (x == null) return length;
        if (x.val != null) length = d;
        if (d == query.length()) return length;
        char c = query.charAt(d);
        int index = c - 'A';
        return longestPrefixOf(x.next[index], query, d + 1, length);
    }

    /**
     * Removes the key from the set if the key is present.
     *
     * @param key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void delete(String key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        root = delete(root, key, 0);
    }

    private Node delete(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) {
            if (x.val != null) n--;
            x.val = null;
        }
        else {
            char c = key.charAt(d);
            int index = c - 'A';
            x.next[index] = delete(x.next[index], key, d + 1);
        }

        // remove subtrie rooted at x if it is completely empty
        if (x.val != null) return x;
        for (int c = 0; c < R; c++)
            if (x.next[c] != null)
                return x;
        return null;
    }

    /**
     * Unit tests the {@code TrieST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        // MyTrieST<Integer> trie = new MyTrieST<>();
        // trie.put("APPLE", 1);
        // trie.put("BANANA", 2);
        // trie.put("ORANGE", 3);
        //
        // System.out.println("Value for 'APPLE': " + trie.get("APPLE"));
        //
        // System.out.println("Keys in the trie: " + trie.keys());

        // build symbol table from standard input
        MyTrieST<Integer> st = new MyTrieST<Integer>();

        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();


        for (int i = 0; i < dictionary.length; i++) {
            String key = dictionary[i];
            st.put(key, i);
        }

        // print results
        if (st.size() < 100) {
            StdOut.println("keys(\"\"):");
            for (String key : st.keys()) {
                StdOut.println(key + " " + st.get(key));
            }
            StdOut.println();
        }

        StdOut.println("longestPrefixOf(\"shellsort\"):");
        StdOut.println(st.longestPrefixOf("SHELLSORT"));
        StdOut.println();

        StdOut.println("longestPrefixOf(\"quicksort\"):");
        StdOut.println(st.longestPrefixOf("QUICKSORT"));
        StdOut.println();

        StdOut.println("keysWithPrefix(\"shor\"):");
        for (String s : st.keysWithPrefix("ABI"))
            StdOut.println(s);
        StdOut.println();

        // StdOut.println("keysThatMatch(\".he.l.\"):");
        // for (String s : st.keysThatMatch(".he.l."))
        //     StdOut.println(s);
    }
}

/******************************************************************************
 *  Copyright 2002-2022, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
