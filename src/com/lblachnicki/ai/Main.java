package com.lblachnicki.ai;

import java.util.*;

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

        System.out.println(node);


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
        // 1. load file and construct graph
//        List<Node> nodes = GraphLoader.loadFromFile("/Users/lblachnicki/Documents/Repositories/university-ai-labs/data/test_from_pdf.dag");
        List<Node> nodes = GraphLoader.loadFromFile("/Users/lblachnicki/Documents/Repositories/university-ai-labs/data/test_medium.dag");

        var artificialParent = nodes.get(nodes.size() - 2);
        var artificialChild = nodes.get(nodes.size() - 1);

        // 2. precalculate heuristics
        artificialChild.BruteForceHeuristic();

        // 4. run A* on all heuristics
        System.out.println(Traverse(artificialParent));
        System.out.println(Traverse(artificialParent).stream().mapToDouble(n -> n.getWeight()).sum());


        // 5. benchmark results
    }
}
