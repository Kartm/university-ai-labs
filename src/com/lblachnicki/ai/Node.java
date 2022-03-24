package com.lblachnicki.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Node {
    private int _index;
    private double _weight;

    private List<Node> _edges;

    public Node(int index, double weight) {
        this._index = index;
        this._weight = weight;
        this._edges = new ArrayList<>();
    }

    public void addEdge(Node edge) {
        this._edges.add(edge);
    }

    public int getIndex() {
        return this._index;
    }

    @Override
    public String toString() {
        return "Node{" +
                "_index=" + _index +
                ", _weight=" + _weight +
                ", _edges=" + _edges.stream().map(e -> e._index).collect(Collectors.toList()) +
                '}';
    }

    public List<Node> getEdges() {
        return this._edges;
    }
}
