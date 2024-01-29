/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BaseballElimination {

    private String[] teamsList;
    private int[] w, l, r;
    private int[][] g;
    private Iterable<String> teams;
    private static final double INFINITY = Double.POSITIVE_INFINITY;
    private String[][] certificateArraysTable;
    private int totalNetworkVertices;
    private long gamesVertices;
    private int teamVertices;
    private int networkVertices, teamsLength;
    private boolean isTrivial;


    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        createData(filename);
        // StdOut.println("teamsLength: " + teamsLength);
        teamVertices = (teamsLength - 1);
        gamesVertices = binomialCoefficient(teamsLength - 1, 2);
        // StdOut.println("gamesVertices: " + gamesVertices);
        totalNetworkVertices = (int) ((teamsLength - 1) + (gamesVertices) + 2);
        certificateArraysTable = new String[teamsLength][1];
        // StdOut.println(totalNetworkVertices);

        FlowNetwork[] networkList = new FlowNetwork[teamsList.length];
        FordFulkerson[] fordFulkersonList = new FordFulkerson[teamsList.length];
        for (int i = 0; i < networkList.length; i++) {
            networkList[i] = createFlowNetwork(teamsList[i]);
            fordFulkersonList[i] = new FordFulkerson(networkList[i], 0, totalNetworkVertices - 1);
        }
        for (int j = 0; j < teamsList.length; j++) {
            if (certificateArraysTable[j][0] == null) {

                // StdOut.println("Before " + teamsList[j] + " " + certificateArraysTable[j][0]);
                createCertificateArray(teamsList[j], fordFulkersonList[j]);
                // StdOut.println("After " + teamsList[j] + " " + certificateArraysTable[j][0]);
                // StdOut.println(teamsList[j]);
                // for (int i = 0; i < totalNetworkVertices; i++) {
                //     StdOut.println("vertex: " + i + " is within the source-side of the mincut: " +
                //                            fordFulkersonList[j].inCut(i));
                // }
            }
        }
        // print2DArray(certificateArraysTable);
    }


    // teams = Arrays.asList(teamsList);

    private String[] createCertificateArray(String eliminatingTeam) {
        String[] certificateAlpha = new String[4];
        certificateAlpha[0] = eliminatingTeam;
        return certificateAlpha;
    }

    private void createCertificateArray(String currentTeam,
                                        FordFulkerson fordFulkerson) {
        int index = getIndex(teams, currentTeam);
        String[] examinedTeamsList = new String[teamsLength - 1];
        int k = 0;
        for (int j = 0; j < teamsLength; j++) {
            if (j != index) {
                examinedTeamsList[k] = teamsList[j];
                k++;
            }
        }
        // StdOut.println("Non-Trivial ExaminedTeamsList here: ");
        // print1DArray(examinedTeamsList);

        int count = 0;
        int tableIndex = 0;
        String[] teamsListTable = new String[teamsLength - 1];
        for (int i = (int) gamesVertices + 1; i < totalNetworkVertices - 1; i++) {
            if (fordFulkerson.inCut(i)) {
                // StdOut.println("fordFulkerson.inCut(" + i + ") : " + fordFulkerson.inCut(i));
                teamsListTable[tableIndex] = examinedTeamsList[count];
                tableIndex++;
            }
            count++;
            // StdOut.print("teamsListTable: ");
            // print1DArray(teamsListTable);
            certificateArraysTable[index] = teamsListTable;
        }
    }


    private FlowNetwork createFlowNetwork(String team) {
        networkVertices = (int) ((teamsLength - 1) + gamesVertices + 2);
        teamVertices = (teamsLength - 1);
        // StdOut.println("networkVertices: " + networkVertices);
        FlowNetwork network = new FlowNetwork(networkVertices);
        // StdOut.println(network.toString());
        FlowEdge[] first = fromStoGames(team);

        addToFlowNetwork(network, first);
        FlowEdge[] second = fromGamesToTeams();
        // StdOut.println(team);
        addToFlowNetwork(network, second);
        // StdOut.println(network.toString());
        FlowEdge[] third = fromTeamsToT(team);
        // for (FlowEdge edge : third) {
        //     StdOut.println(edge.toString());
        // }
        addToFlowNetwork(network, third);
        // StdOut.println(team);
        // StdOut.println(network.toString());
        // loopTest((int) gamesVertices, teamVertices);
        return network;
    }

    private FlowEdge[] fromTeamsToT(String examinedTeam) {
        FlowEdge[] output = new FlowEdge[teamVertices];
        int firstTeamVertex = (int) gamesVertices + 1;
        int t = (int) gamesVertices + teamVertices + 1;
        int index = 0;
        // StdOut.println(firstTeamVertex + " " + t);
        for (int j = 0; j < teamsLength; j++) {
            if (!teamsList[j].equals(examinedTeam)) {
                int capacity = wins(examinedTeam) + remaining(examinedTeam) - wins(
                        teamsList[j]);
                if (capacity < 0) {
                    capacity = 0;
                    isTrivial = true;
                    // StdOut.println("This team: " + examinedTeam + " was trivially removed by: "
                    //                        + teamsList[j]);

                    String[] test = createCertificateArray(teamsList[j]);
                    // print1DArray(test);
                    certificateArraysTable[getIndex(teams, examinedTeam)] = test;
                }

                // StdOut.println(examinedTeam +
                //                        " Wins: " + wins(examinedTeam) + " Remaining: "
                //                        + remaining(
                //         examinedTeam) + " " +
                //                        teamsList[j] + " wins: " + wins(teamsList[j]) + " t: "
                //                        + t
                //                        + " firstTeamVertex: " + firstTeamVertex);
                output[index] =
                        new FlowEdge(firstTeamVertex, t,
                                     (capacity));
                index++;
                firstTeamVertex++;

            }
        }
        return output;
    }

    private FlowEdge[] fromStoGames(String team) {
        FlowEdge[] output = new FlowEdge[(int) gamesVertices];
        // for (int i = 0; i < gamesVertices; i++) {
        int index = 0;
        int vertex = 1;
        for (int j = 0; j < teamsLength; j++) {
            if (!teamsList[j].equals(team)) {
                for (int k = j; k < teamsLength; k++) {
                    if (!teamsList[k].equals(team)) {
                        if (j != k) {
                            // StdOut.println(gamesVertices);
                            // StdOut.println(teamsList[j] + " " + teamsList[k] +
                            //                        " 0 " + "vertex: " + vertex + " against: "
                            //                        + against(teamsList[j],
                            //                                  teamsList[k]));
                            output[index] =
                                    new FlowEdge(0, vertex, against(teamsList[j], teamsList[k]));
                            index++;
                            vertex++;
                        }
                    }
                }
            }
        }
        return output;
    }

    private FlowEdge[] fromGamesToTeams() {
        FlowEdge[] output = new FlowEdge[(int) gamesVertices * 2];
        int firstTeamVertex = (int) gamesVertices + 1;
        int secondTeamVertex = firstTeamVertex + 1;
        int outputIndex = 0;
        int index = 1;

        while (firstTeamVertex <= gamesVertices + teamVertices) {
            while (secondTeamVertex <= gamesVertices + teamVertices) {
                // StdOut.println(index + ":" + firstTeamVertex + ":" + secondTeamVertex);
                output[outputIndex] = new FlowEdge(index, firstTeamVertex, INFINITY);
                outputIndex++;
                output[outputIndex] = new FlowEdge(index, secondTeamVertex, INFINITY);
                outputIndex++;
                index++;
                secondTeamVertex++;
            }
            firstTeamVertex++;
            secondTeamVertex = firstTeamVertex + 1;
        }
        return output;
    }

    private void addToFlowNetwork(FlowNetwork network, FlowEdge[] list) {
        for (FlowEdge edge : list) {
            network.addEdge(edge);
        }
    }

    private void createData(String filename) {
        In in;
        in = new In(filename);
        int i = in.readInt();
        teamsList = new String[i];
        teamsLength = teamsList.length;
        w = new int[i];
        l = new int[i];
        r = new int[i];
        g = new int[i][i];
        i = 0;
        while (!in.isEmpty()) {
            teamsList[i] = in.readString();
            w[i] = Integer.parseInt(in.readString());
            l[i] = Integer.parseInt(in.readString());
            r[i] = Integer.parseInt(in.readString());
            for (int j = 0; j < teamsLength; j++) {
                g[i][j] = Integer.parseInt(in.readString());
            }
            i++;
        }
        teams = Arrays.asList(teamsList);
    }

    // number of teams
    public int numberOfTeams() {
        return teamsLength;
    }

    // all teams
    public Iterable<String> teams() {
        return teams;
    }

    // number of wins for given team
    public int wins(String team) {
        int index = getIndex(teams, team);
        if (index < 0) {
            throw new IllegalArgumentException(team + " is not a valid team");
        }
        else {
            return w[index];
        }
    }

    // number of losses for given team
    public int losses(String team) {
        int index = getIndex(teams, team);
        if (index < 0) {
            throw new IllegalArgumentException(team + " is not a valid team");
        }
        else {
            return l[index];
        }
    }

    // number of remaining games for given team
    public int remaining(String team) {
        int index = getIndex(teams, team);
        if (index < 0) {
            throw new IllegalArgumentException(team + " is not a valid team");
        }
        else {
            return r[index];
        }
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        int indexOne = getIndex(teams, team1);
        int indexTwo = getIndex(teams, team2);

        if (indexOne < 0) {
            throw new IllegalArgumentException(team1 + " is not a valid team");
        }
        if (indexTwo < 0) {
            throw new IllegalArgumentException(team2 + " is not a valid team");
        }
        else {
            return g[indexOne][indexTwo];
        }
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        int index = getIndex(teams, team);
        if (index < 0) {
            throw new IllegalArgumentException(team + " is not a valid team");
        }
        else {
            String[] teamResults = certificateArraysTable[index];
            boolean results = teamResults[0] != null;
            return results;
        }
    }


    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {

        int index = getIndex(teams, team);
        if (index < 0) {
            throw new IllegalArgumentException(team + " is not a valid team");
        }
        else {
            String[] teamResults = certificateArraysTable[index];
            int nonNullCount = countNonNull(teamResults);
            String[] output = new String[nonNullCount];
            if (nonNullCount == 0) {
                return null;
            }
            else {
                for (int i = 0; i < nonNullCount; i++) {
                    output[i] = teamResults[i];
                }
            }

            Iterable<String> results = Arrays.asList(output);
            return results;
        }
    }

    private int countNonNull(String[] teamResults) {
        int count = 0;
        for (String team : teamResults) {
            if (team != null) {
                count++;
            }
            else {
                return count;
            }
        }
        return count;
    }

    private int getIndex(Iterable<String> teams, String team) {
        int index = 0;
        for (String item : teams) {
            if (item.equals(team)) {
                return index;
            }
            index++;
        }
        return -1;
    }


    private static void print1DArray(String[] array) {
        for (String entry : array) {
            StdOut.printf("%9s ", entry);
        }
        StdOut.println();
    }

    private void printTeamRow(int team) {
        StdOut.print(teamsList[team] + " " + w[team] + " " + l[team] + " " + r[team]);
        for (int x : g[team]) {
            StdOut.printf("%2d ", x);
        }
        StdOut.println();
    }

    private static void print2DArray(int[][] array) {
        // StdOut.println("width: " + array[0].length + " height: " + array.length);
        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array[0].length; col++) {
                StdOut.printf("%9d ", array[row][col]);
            }
            StdOut.println();
        }
    }

    private static void print2DArray(String[][] array) {
        // StdOut.println("width: " + array[0].length + " height: " + array.length);
        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array[0].length; col++) {
                StdOut.printf("%3s ", array[row][col]);
            }
            StdOut.println();
        }
    }

    private static long binomialCoefficient(int n, int k) {
        if (k < 0 || k > n) {
            return 0;
        }
        if (k == 0 || k == n) {
            return 1;
        }
        // Calculate C(n, k) = n! / (k! * (n - k)!) without unnecessary intermediate steps
        long result = 1;
        for (int i = 1; i <= k; i++) {
            result *= (n - i + 1);
            result /= i;
        }
        return result;
    }

    // private static long factorial(int n) {
    //     if (n == 0 || n == 1) {
    //         return 1;
    //     }
    //     long result = 1;
    //     for (int i = 2; i <= n; i++) {
    //         result *= i;
    //     }
    //     return result;
    // }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
