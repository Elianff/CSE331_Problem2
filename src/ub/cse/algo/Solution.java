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

    //sources: Day 19 Slides, https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html,https://docs.oracle.com/javase/8/docs/api/java/util/PriorityQueue.html
    public SolutionObject outputPaths() {
        SolutionObject sol = new SolutionObject();
        sol.bandwidths = new ArrayList<>(bandwidths);

        int[] count = new int[bandwidths.size()];   //create integer array to store bandwith at each id/index. We don't plan to add more than bandwidths.size

        //need sol.priorities to organize priorities.
        //determine order based on payment
        ArrayList<Client> sortedClients = new ArrayList<>(clients); //based on payments
        //sort sortedClients
        //either use sortedClients.sort() if allowed or manually do it. Use client.payment?
        //https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html:
        //to sort the clients. If negative, client a is first, and if positive, b

        sortedClients.sort((a,b) -> {
            if (b.payment != a.payment) {
                //effectively: if b.payment>a.payment, return 1, b goes first
                //if a.payment>b.payment, return -1, a goes first
                //simplified b.payment-a.payment
                //example: b=100 and a=500 then 100-500=-400, a goes first
                return b.payment - a.payment;
            }
            else
                //if it is equal, need tiebreaker to determine which one is first.
                return Float.compare(a.alpha, b.alpha);
        });

        for (Client client : sortedClients) {
            //https://docs.oracle.com/javase/8/docs/api/java/util/PriorityQueue.html
            PriorityQueue<int[]> todo = new PriorityQueue<>((a, b) -> a[1] - b[1]);
            int[] distance = new int[graph.size()];
            Arrays.fill(distance, Integer.MAX_VALUE);      //default fill with really big numbers
            int[] priority = new int[graph.size()];
            Arrays.fill(priority, -1);                 //default fill with smallest numbers, update when adding

            int startNode = graph.contentProvider; //Need to locate starting node
            distance[startNode] = 0;     //distance from startNode to..startNode is 0
            int[] toQueue = {startNode, 0};    //increased steps to make it clearer, store the node and the cost, least cost prioritized
            todo.add(toQueue);

            //Queue
            while (!todo.isEmpty()) {
                int[] current = todo.poll();   // take first from queue
                int node = current[0];         //current looks like {int, int} or {startNode, cost}
                int cost = current[1];

                if (node == client.id)// removing client from the queue
                    break;

                if (cost > distance[node]) // comparing node distance with cost
                    continue;

                for (Integer n : graph.get(node)) {//going through all non-client nodes
                    int extra = (count[n] + 1)/ bandwidths.get(n);
                    int newCost = distance[node] + 1 + extra;

                    if (newCost < distance[n]) {//if distance of node to starting node is >than new cost
                        distance[n] = newCost;
                        priority[n] = node;
                        todo.add(new int[]{n, newCost});//adding new cost to queue
                    }
                }
            }

            if (priority[client.id] != -1) {
                ArrayList<Integer> path = new ArrayList<>();
                int currentClient = client.id; //start from client
                //backtrack from client to ISP
                while (currentClient != startNode) {//condition for looping
                    path.add(0, currentClient);//constructing path
                    currentClient = priority[currentClient];
                }

                path.add(0, startNode); //add ISP at front
                sol.paths.put(client.id, path);

                for (int i = 0; i < path.size()-1 ; i++) {//counting bandwidth to calculate new cost
                    count[path.get(i)]++;
                }
            }
        }

        return sol;
    }
}
