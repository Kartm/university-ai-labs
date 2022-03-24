package com.lblachnicki.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Node {
    private int _index;
    private double _weight;
    public double G;
    private double _h;

    private List<Node> _edges;
    private List<Node> _parents;
    public Node bestParent;

    public Node(int index, double weight) {
        this._index = index;
        this._weight = weight;
        this._edges = new ArrayList<>();
        this._parents = new ArrayList<>();
    }

    public void addEdge(Node edge) {
//        System.out.println(edge);
        this._edges.add(edge);

        edge._parents.add(this);
    }

    public int getIndex() {
        return this._index;
    }

    @Override
    public String toString() {
        return "Node{" +
                "_index=" + _index +
                ", _weight=" + _weight +
                ", G=" + G +
                ", _h=" + _h +
//                ", _edges=" + _edges +
                "}\n";
    }

    public List<Node> getEdges() {
        return this._edges;
    }

    public void BruteForceHeuristic()
    {
        // make sure we're the top node
        DepthFirst(_weight);
    }

    public double getF() {
        return G + _h;
    }

    private void DepthFirst(double currentWeight)
    {
        if (_h == 0)
        {
            _h = currentWeight + this._weight;
        }
        else if (_h - this._weight < currentWeight)
        {
            _h = currentWeight + this._weight;
        }
        else
        {
            return;
        }

        for (Node p: _parents) {
            p.DepthFirst(_h);
        }
    }

    public double getWeight() {
        return this._weight;
    }

    public boolean hasNext() {
        return this._edges.size() > 0;
    }
}
