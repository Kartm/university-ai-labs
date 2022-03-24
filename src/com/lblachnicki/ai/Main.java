package com.lblachnicki.ai;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static List<Node> Traverse(Node parentNode)
    {
        List<Node> open = new ArrayList<>();

        parentNode.G = parentNode.getWeight();
        open.add(parentNode);
        var node = open.get(0);

//        var stopWatch = new Stopwatch();
//        stopWatch.Start();

        while (node.hasNext())
        {
            // Find the Node with biggest F
            node = open.get(0);
//            System.out.println(node.getIndex());

            for(var n: open)
            {
//                if (n.getF() > node.getF()) continue;
                if (n.getF() < node.getF()) continue;
                node = n;
            }

//            System.out.println(node.getIndex());

            // todo escape case

            open.remove(node);

            for(var child: node.getEdges())
            {
                var currentTime = node.G + node.getWeight();

                // If the time to get to child is smaller than current, continue checking, this is not the one
                if (currentTime < child.G) continue;
                child.bestParent = node;
                child.G = currentTime;
                if(open.contains(child)) continue;
                open.add(child);
            }
        }

        System.out.println(node);


//        stopWatch.Stop();
//        Console.WriteLine($"Traversing took {stopWatch.Elapsed}");


        // Create the path, going from the last node and it's parents
        List<Node> path = new ArrayList<>();
//        node = childNode;
        while (node.bestParent != null)
        {
            path.add(node);
            node = node.bestParent;

//            if(node.getIndex() == 61) {
//                System.out.println(node);
//            }
        }
        Collections.reverse(path);



        return path;
    }

    public static void main(String[] args) {
//        List<Node> nodes = GraphLoader.loadFromFile("/Users/lblachnicki/Documents/Repositories/university-ai-labs/data/test_from_pdf.dag");
        List<Node> nodes = GraphLoader.loadFromFile("/Users/lblachnicki/Documents/Repositories/university-ai-labs/data/test_medium.dag");

        var artificialParent = nodes.get(nodes.size() - 2);
        var artificialChild = nodes.get(nodes.size() - 1);
            // run heuristic
            artificialChild.BruteForceHeuristic();

//            for (Node n: nodes
//            ) {
//                System.out.println(n);
//            }

//            System.out.println(Traverse(artificialParent, artificialChild).stream().map(n -> n.getIndex()).collect(Collectors.toList()));
            System.out.println(Traverse(artificialParent));
            System.out.println(Traverse(artificialParent).stream().mapToDouble(n -> n.getWeight()).sum());


//            var node_52 = nodes.stream().

//            System.out.println(nodes);

//            for (Node n: nodes
//            ) {
//                System.out.println(n);
//            }




        // write your code here

        // 1. load file
        // 2. construct graph
        // 3. precalculate heuristics
        // 4. run A* on all heuristics
        // 5. benchmark results
    }
}
