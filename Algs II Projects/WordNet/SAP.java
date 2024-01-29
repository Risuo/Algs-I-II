/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class SAP {

    private final Digraph digraph;
    private int length, ancestor;
    private DeluxBFS deluxBFS;
    // private int ancestor;
    // private Iterable<Integer> pathToV, pathToW;


    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("Digraph cannot be null");
        }
        this.digraph = new Digraph(G);


    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v != w) {

            try {
                length = deluxBFS.getSapLength();
            }
            catch (NullPointerException e) {
                // DeluxBFS deluxBFS = new DeluxBFS(digraph, v, w);

                length = deluxBFS.getSapLength();
            }
            if (length < Integer.MAX_VALUE) {
                // length = deluxBFS.getSapLength();
                // ancestor = deluxBFS.getAncestor();
                return length;
            }
            else {
                return -1;
            }
        }
        else {
            return 0;
        }
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v != w) {
            try {
                ancestor = deluxBFS.getAncestor();
            }
            catch (NullPointerException e) {
                DeluxBFS deluxBFS = new DeluxBFS(digraph, v, w);
                ancestor = deluxBFS.getAncestor();
            }
            return ancestor;
        }
        else {
            length = 0;
            ancestor = v;
            return ancestor;
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {

        try {
            length = deluxBFS.getSapLength();
        }
        catch (NullPointerException e) {
            DeluxBFS deluxBFS = new DeluxBFS(digraph, v, w);
            length = deluxBFS.getSapLength();
        }
        if (length < Integer.MAX_VALUE) {
            return length;
        }
        else {
            return -1;
        }
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        try {
            ancestor = deluxBFS.getAncestor();
        }
        catch (NullPointerException e) {
            DeluxBFS deluxBFS = new DeluxBFS(digraph, v, w);
            ancestor = deluxBFS.getAncestor();
        }
        return ancestor;
    }


    // do unit testing of this class?
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        // while (!StdIn.isEmpty()) {
        //     int v = StdIn.readInt();
        //     int w = StdIn.readInt();
        //     int length = sap.length(v, w);
        //     int ancestor = sap.ancestor(v, w);
        //     StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        // }
        Integer[] a = { 8, 3, 9, 6 };
        Integer[] b = { 1, 2, 4, 5, 10 };
        Iterable<Integer> aSet = (a != null) ? Arrays.asList(a) : null;
        Iterable<Integer> bSet = (b != null) ? Arrays.asList(b) : null;

        int length = sap.length(aSet, bSet);
        int ancestor = sap.ancestor(aSet, bSet);


        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);

    }

}

