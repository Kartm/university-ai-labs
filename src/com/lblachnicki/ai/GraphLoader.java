package com.lblachnicki.ai;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GraphLoader {
    public static List<Node> loadFromFile(String path) {
        try {
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

            return nodes;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
