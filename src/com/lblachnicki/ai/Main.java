package com.lblachnicki.ai;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {
    public static List<Node> Traverse(Node parentNode) {
        LinkedHashMap<Node, Node> bestParents = new LinkedHashMap<>();

        List<Node> open = new ArrayList<>();

        parentNode.G = parentNode.getWeight();
        open.add(parentNode);
        var node = open.get(0);

        while (node.hasNext()) {
            // Find the Node with biggest F
            node = open.get(0);

            for (var n : open) {
                if (n.getF() < node.getF()) continue;
                node = n;
            }

            open.remove(node);

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

        for (var testCase :
                graphsWithResults) {
            List<Node> nodes = GraphLoader.loadFromFile(testCase.getKey());

            var artificialParent = nodes.get(nodes.size() - 2);
            var artificialChild = nodes.get(nodes.size() - 1);

            long startTime = System.nanoTime();
            artificialChild.BruteForceHeuristic();
            long estimatedTime = System.nanoTime() - startTime;
//            System.out.println(estimatedTime);
            System.out.println(TimeUnit.NANOSECONDS.toMillis(estimatedTime));


            startTime = System.nanoTime();
            var path = Traverse(artificialParent);
            estimatedTime = System.nanoTime() - startTime;
//            System.out.println(estimatedTime);
            System.out.println(TimeUnit.NANOSECONDS.toMillis(estimatedTime));


            var totalCost = path.stream().mapToDouble(n -> n.getWeight()).sum();
//            System.out.println(path);
            System.out.println(totalCost);

            assert(totalCost == testCase.getValue());
        }


    }
}
