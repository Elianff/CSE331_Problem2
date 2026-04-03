package ub.cse.algo;

import java.util.ArrayList;
import java.util.HashMap;


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

        HashMap<Integer, ArrayList<Integer>> path = Traversals.bfsPaths(graph, clients);


        //go down in order of priority
        //if Clients is [client2 = $1,client0 = $30,client1 = $100]
        //and sortedClients is client1=$100, client0 = $30, client2=$1
        //then priority would be {index 0: client2, index 1 : client0, index 2 : client1} because simulator has descending order

        for (Client client : clients) {
            int shortestDelay = info.shortestDelays.get(client.id);
            if (shortestDelay <= client.alpha) {
                sol.paths.put(client.id, path.get(client.id));
                int priority = (int) (client.payment / client.alpha);
                sol.priorities.put(client.id, priority);
            }
        }



        return sol;

    }
}
