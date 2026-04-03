package ub.cse.algo;

import java.util.*;


public class Solution {

    private Info info;
    private Graph graph;
    private ArrayList<Client> clients;
    private ArrayList<Integer> bandwidths;

    /**
     * Basic Constructor
     *
     * @param info: data parsed from input file
     */
    public Solution(Info info) {
        this.info = info;
        this.graph = info.graph;
        this.clients = info.clients;
        this.bandwidths = info.bandwidths;
    }

    /**
     * Method that returns the calculated 
     * SolutionObject as found by your algorithm
     *
     * @return SolutionObject containing the paths, priorities and bandwidths
     */
    public SolutionObject outputPaths() {
        SolutionObject sol = new SolutionObject();
        sol.bandwidths = new ArrayList<>(bandwidths);

        int[] count = new int[bandwidths.size()];

        //need sol.priorities to organize priorities.
        //determine order based on payment
        ArrayList<Client> sortedClients = new ArrayList<>(clients); //based on payments
        //sort sortedClients
        //either use sortedClients.sort() if allowed or manually do it. Use client.payment?
        sortedClients.sort((a,b) -> {
            if (b.payment != a.payment)
                return b.payment - a.payment;
            else
                return Float.compare(a.alpha, b.alpha);
        });

        for (Client c : sortedClients) {
            PriorityQueue<int[]> todo = new PriorityQueue<>((a, b) -> a[1] - b[1]);
            int[] dist = new int[graph.size()];
            Arrays.fill(dist, Integer.MAX_VALUE);
            int[] priors = new int[graph.size()];
            Arrays.fill(priors, -1);

            int startNode = graph.contentProvider; //Need to locate starting node
            dist[startNode] = 0;
            todo.add(new int[]{startNode, 0});

            while (!todo.isEmpty()) {
                int[] current = todo.poll();
                int node = current[0];
                int cost = current[1];

                if (node == c.id)
                    break;

                if (cost > dist[node])
                    continue;

                for (Integer n : graph.get(node)) {
                    int extra = (count[n] + 1)/ bandwidths.get(n);
                    int newCost = dist[node] + 1 + extra;

                    if (newCost < dist[n]) {
                        dist[n] = newCost;
                        priors[n] = node;
                        todo.add(new int[]{n, newCost});
                    }
                }
            }

            if (priors[c.id] != -1) {
                ArrayList<Integer> path = new ArrayList<>();
                int currentClient = c.id; //start from client
                //backtrack from client to ISP
                while (currentClient != startNode) {
                    path.add(0, currentClient);
                    currentClient = priors[currentClient];
                }

                path.add(0, startNode); //add ISP at front
                sol.paths.put(c.id, path);

                for (int i = 0; i < path.size()-1 ; i++) {
                    count[path.get(i)]++;
                }
            }
        }

        return sol;
    }
}
