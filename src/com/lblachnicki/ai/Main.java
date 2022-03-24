package com.lblachnicki.ai;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {
    public static List<Node> Traverse(Node parentNode) {
        LinkedHashMap<Node, Node> bestParents = new LinkedHashMap<>();

        PriorityQueue<Node> open = new PriorityQueue((Comparator<Node>) (a, b) -> {
            // nodes with biggest F are at the top
            if (a.getF() < b.getF()) {
                return -1;
            } else {
                return 1;
            }
        });

        parentNode.G = parentNode.getWeight();
        open.add(parentNode);

        Node node = null;

        while (!open.isEmpty()) {
            // Find the Node with biggest F
            node = open.remove();

            // todo escape case? no children == goal reached :)))

            for (var child : node.getEdges()) {
                var currentTime = node.G + node.getWeight();
                // If the time to get to child is smaller than current, continue checking, this is not the one
                if (currentTime < child.G) continue;
                bestParents.put(child, node);
                child.G = currentTime;
                if (open.contains(child)) continue;
                open.add(child);
            }
        }

        // Recreate the best path, going from the last node and it's parents
        List<Node> path = new ArrayList<>();
        while (bestParents.get(node) != null) {
            path.add(node);
            node = bestParents.get(node);
        }
        Collections.reverse(path);

        return path;
    }

    public static void main(String[] args) {
        List<Map.Entry<String, Double>> graphsWithResults = new ArrayList<>();
        graphsWithResults.add(new AbstractMap.SimpleEntry<>("/Users/lblachnicki/Documents/Repositories/university-ai-labs/data/test_from_pdf.dag", 1.97));
        graphsWithResults.add(new AbstractMap.SimpleEntry<>("/Users/lblachnicki/Documents/Repositories/university-ai-labs/data/test_small.dag", 1.61765274405479));
        graphsWithResults.add(new AbstractMap.SimpleEntry<>("/Users/lblachnicki/Documents/Repositories/university-ai-labs/data/test_small_sparse.dag", 1.53830736875534));
        graphsWithResults.add(new AbstractMap.SimpleEntry<>("/Users/lblachnicki/Documents/Repositories/university-ai-labs/data/test_medium.dag", 18.2182804942131));
        graphsWithResults.add(new AbstractMap.SimpleEntry<>("/Users/lblachnicki/Documents/Repositories/university-ai-labs/data/test_medium_sparse.dag", 15.6808122843504));
        graphsWithResults.add(new AbstractMap.SimpleEntry<>("/Users/lblachnicki/Documents/Repositories/university-ai-labs/data/test_large.dag", 29.5272732377052));
        graphsWithResults.add(new AbstractMap.SimpleEntry<>("/Users/lblachnicki/Documents/Repositories/university-ai-labs/data/test_large_sparse.dag", 21.253554970026));
        graphsWithResults.add(new AbstractMap.SimpleEntry<>("/Users/lblachnicki/Documents/Repositories/university-ai-labs/data/test_xlarge.dag", 39.9794295579195));
        graphsWithResults.add(new AbstractMap.SimpleEntry<>("/Users/lblachnicki/Documents/Repositories/university-ai-labs/data/test_xlarge_sparse.dag", 29.2775901407003));

        List<String[]> table = new ArrayList<>();
        table.add(new String[]{"file", "h(0) took ns", "traverse h(0) took ns"});
        table.add(new String[]{"---------", "---------", "---------"});


        for (var testCase :
                graphsWithResults) {
            var filePath = testCase.getKey();
            List<Node> nodes = GraphLoader.loadFromFile(filePath);


            var artificialParent = nodes.get(nodes.size() - 2);
            var artificialChild = nodes.get(nodes.size() - 1);

            long startTime = System.nanoTime();
            artificialChild.BruteForceHeuristic();
            long bruteForceHeuristicTime = System.nanoTime() - startTime;

            startTime = System.nanoTime();
            var path = Traverse(artificialParent);
            long bruteForceTraverseTime = System.nanoTime() - startTime;

            var totalCost = path.stream().mapToDouble(n -> n.getWeight()).sum();
//            System.out.println(path);
//            System.out.println(totalCost);

            table.add(new String[]{
                    filePath.substring(filePath.lastIndexOf("/") + 1),
                    String.valueOf(bruteForceHeuristicTime),
                    String.valueOf(bruteForceTraverseTime)
            });

            assert (testCase.getValue() - totalCost < 0.0000003);
        }

        for (var row : table) {
            System.out.format("%-25s%-15s%-15s\n", row);
        }
    }
}
