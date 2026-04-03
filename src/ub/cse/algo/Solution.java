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
        sol.paths = Traversals.bfsPaths(graph, clients);
        sol.bandwidths = new ArrayList<>(bandwidths);

        //need sol.priorities to organize priorities.
        //determine order based on payment
        ArrayList<Client> sortedClients = new ArrayList<>(clients); //based on payments
        //sort sortedClients
        //either use sortedClients.sort() if allowed or manually do it. Use client.payment?
        for (int i = 0; i < sortedClients.size(); i++) {
            for (int j = i + 1; j < sortedClients.size(); j++) {
                if (sortedClients.get(i).payment < sortedClients.get(j).payment) {
                    Client temp = sortedClients.get(i);
                    sortedClients.set(i, sortedClients.get(j));
                    sortedClients.set(j, temp);
                }
            }
        }

        //go down in order of priority
        //if Clients is [client2 = $1,client0 = $30,client1 = $100]
        //and sortedClients is client1=$100, client0 = $30, client2=$1
        //then priority would be {index 0: client2, index 1 : client0, index 2 : client1} because simulator has descending order
        int priority = sortedClients.size();
        for (Client client : sortedClients) {
            sol.priorities.put(client.id, priority--);
        }

        return sol;
    }
}
