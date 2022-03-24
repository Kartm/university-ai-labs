package com.lblachnicki.ai;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static List<Node> Traverse(Node parentNode, Node childNode)
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
            for(var n: open)
            {
                if (n.getF() > node.getF()) continue;
                node = n;
            }

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




//        stopWatch.Stop();
//        Console.WriteLine($"Traversing took {stopWatch.Elapsed}");


        // Create the path, going from the last node and it's parents
        List<Node> path = new ArrayList<>();
        node = childNode;
        while (node.bestParent != null)
        {
            path.add(node);
            node = node.bestParent;
        }
//        Collections.reverse(path);

        return path;
    }

    public static void main(String[] args) {
        try {
            String path = "/Users/lblachnicki/Documents/Repositories/university-ai-labs/data/test_from_pdf.dag";
            Scanner input = new Scanner(new FileReader(path));

            int numberOfNodes = Integer.parseInt(input.nextLine());

            List<Node> nodes = new ArrayList<>();

            for(int i = 0; i < numberOfNodes; i++) {
                nodes.add(new Node(i, Double.parseDouble(input.nextLine())));
            }

            for(int i = 0; i < numberOfNodes; i++) {
                var line = input.nextLine();

                for (String field : line.split(" ")) {
                    int destNodeIndex = Integer.parseInt(field);

                    // no successors
                    if(destNodeIndex == -1) {
                        continue;
                    }

                    nodes.get(i).addEdge(nodes.get(destNodeIndex));
                }
            }

            // create artificial child
            // find all nodes without parent
            var allDestinationIndexes = nodes.stream().map(n -> n.getEdges()).flatMap(List::stream).map(e -> e.getIndex()).collect(Collectors.toList());
            var allNodesWithoutParent = nodes.stream().filter(n -> !allDestinationIndexes.contains(n.getIndex())).collect(Collectors.toList());
            var artificialParent = new Node(numberOfNodes, 0);
            allNodesWithoutParent.forEach(n -> artificialParent.addEdge(n));
            nodes.add(artificialParent);

            // find all nodes without children
            var nodesWithoutChildren = nodes.stream().filter(n -> n.getEdges().size() == 0).collect(Collectors.toList());
            var artificialChild = new Node(numberOfNodes + 1, 0);
            nodesWithoutChildren.forEach(n -> n.addEdge(artificialChild));
            nodes.add(artificialChild);



            // run heuristic
            artificialParent.BruteForceHeuristic();

//            for (Node n: nodes
//            ) {
//                System.out.println(n);
//            }

            System.out.println(Traverse(artificialParent, artificialChild).stream().map(n -> n.getIndex()).collect(Collectors.toList()));
            System.out.println(Traverse(artificialParent, artificialChild).stream().mapToDouble(n -> n.getWeight()).sum());

//            for (Node n: nodes
//            ) {
//                System.out.println(n);
//            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // write your code here

        // 1. load file
        // 2. construct graph
        // 3. precalculate heuristics
        // 4. run A* on all heuristics
        // 5. benchmark results
    }
}
