package com.lblachnicki.ai;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {
    public static List<Node> Traverse(Node startNode, Node goalNode) {
        LinkedHashMap<Node, Node> bestParents = new LinkedHashMap<>();

        ArrayList<Node> open = new ArrayList<>();

        startNode.G = startNode.getWeight();
        open.add(startNode);

        while (!open.isEmpty()) {
            Node node = open.get(0);

            for (Node n : open){
                if (n.getF() < node.getF())
                    continue;
                node = n;
            }

            if(node == goalNode) {
                // recreate the best path
                List<Node> path = new ArrayList<>();

                Node current = goalNode;
                while (bestParents.get(current) != null) {
                    path.add(current);
                    current = bestParents.get(current);
                }
                Collections.reverse(path);

                return path;
            }

            open.remove(node);

            for (var child : node.getEdges()) {
                var candidate_best_g = node.G + child.getWeight();

                if(candidate_best_g >= child.G) {
                    // this path to child is better than any previous one. save it
                    bestParents.put(child, node);
                    child.G = candidate_best_g;

                    if(!open.contains(child)) {
                        open.add(child);
                    }
                }
            }
        }

        return null;
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
        table.add(new String[]{"file", "h(0) took ns", "traverse h(0) took ns", "h(random) took ns", "traverse h(random) took ns"});
        table.add(new String[]{"---------", "---------", "---------", "---------", "---------"});

        Random random = new Random();

        for (var testCase :
                graphsWithResults) {
            var filePath = testCase.getKey();
            List<Node> nodes = GraphLoader.loadFromFile(filePath);


            var artificialParent = nodes.get(nodes.size() - 2);
            var artificialChild = nodes.get(nodes.size() - 1);

            long startTime = System.nanoTime();
            artificialChild.precalculate_H0_Heuristic();
            long bruteForceHeuristicTime = System.nanoTime() - startTime;

            startTime = System.nanoTime();
            var path = Traverse(artificialParent, artificialChild);
            long bruteForceTraverseTime = System.nanoTime() - startTime;

            var totalCost = path.stream().mapToDouble(n -> n.getWeight()).sum();
            assert (testCase.getValue() - totalCost < 0.0000003);

            nodes.forEach(n -> n.resetHeuristic());

            System.out.println("\n\n\n");

            startTime = System.nanoTime();
            nodes.forEach(n -> n.precalculate_random_Heuristic(random));
            long randomHeuristicTime = System.nanoTime() - startTime;
            startTime = System.nanoTime();
            path = Traverse(artificialParent, artificialChild);
            totalCost = path.stream().mapToDouble(n -> n.getWeight()).sum();
            assert (testCase.getValue() - totalCost < 0.0000003);
            long randomTraverseTime = System.nanoTime() - startTime;

            table.add(new String[]{
                    filePath.substring(filePath.lastIndexOf("/") + 1),
                    String.valueOf(bruteForceHeuristicTime),
                    String.valueOf(bruteForceTraverseTime),
                    String.valueOf(randomHeuristicTime),
                    String.valueOf(randomTraverseTime)
            });


        }

        for (var row : table) {
            System.out.format("%-25s%-15s%-15s%-15s%-15s\n", row);
        }
    }
}
