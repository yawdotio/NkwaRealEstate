package graph;

import java.util.*;

/**
 * Models internal bank account relationships as a directed graph.
 */
public class AccountGraph {

    private final Map<String, List<String>> adjacencyList;

    public AccountGraph() {
        adjacencyList = new HashMap<>();
    }

    /**
     * Adds a node if it doesn't exist.
     */
    public void addAccount(String accountId) {
        adjacencyList.putIfAbsent(accountId, new ArrayList<>());
    }

    /**
     * Adds a directed edge: from -> to (money transfer)
     */
    public void addTransfer(String fromAccount, String toAccount) {
        addAccount(fromAccount);
        addAccount(toAccount);
        adjacencyList.get(fromAccount).add(toAccount);
    }

    /**
     * Returns a list of directly connected accounts from given account.
     */
    public List<String> getConnections(String accountId) {
        return adjacencyList.getOrDefault(accountId, new ArrayList<>());
    }

    /**
     * Performs BFS to find all reachable accounts from a given start node.
     */
    public Set<String> getReachableAccounts(String startAccount) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(startAccount);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (!visited.contains(current)) {
                visited.add(current);
                List<String> neighbors = getConnections(current);
                queue.addAll(neighbors);
            }
        }

        visited.remove(startAccount); // remove self
        return visited;
    }

    /**
     * Displays all account connections (edges).
     */
    public void displayGraph() {
        if (adjacencyList.isEmpty()) {
            System.out.println("❌ No account transfers recorded.");
            return;
        }

        System.out.println("🌐 Internal Account Transfers:");
        for (String acc : adjacencyList.keySet()) {
            List<String> neighbors = adjacencyList.get(acc);
            if (!neighbors.isEmpty()) {
                for (String neighbor : neighbors) {
                    System.out.println("- " + acc + " ➝ " + neighbor);
                }
            }
        }
    }
}
