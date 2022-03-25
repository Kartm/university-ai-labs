package com.lblachnicki.ai;

import java.util.*;
import java.util.stream.Collectors;

public class Node {
    private int _index;
    private double _weight;
    public double G;
    private double _h;

    private List<Node> _edges;
    private List<Node> _parents;

    public Node(int index, double weight) {
        this._index = index;
        this._weight = weight;
        this._edges = new ArrayList<>();
        this._parents = new ArrayList<>();
    }

    public void addEdge(Node edge) {
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
                ", F=" + getF() +
//                ", _edges=" + _edges +
                "}\n";
    }

    public List<Node> getEdges() {
        return this._edges;
    }

    public double getF() {
        return G + _h;
    }

    public void precalculate_H0_Heuristic() {
        if(!_edges.isEmpty()) {
            System.out.println("NOT USING ARTIFICIAL CHILD!");
            return;
        }
        precalculate_H0_Heuristic(0);
    }

    private void precalculate_H0_Heuristic(double currentWeight)
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
            p.precalculate_H0_Heuristic(_h);
        }
    }

    public void precalculate_random_Heuristic(Random random) {
        _h = random.nextDouble();
    }

    public void precalculate_edge_count_Heuristic() {
        _h = _edges.size();
    }

    public void precalculate_parent_count_Heuristic() {
        _h = _parents.size();
    }

    public void precalculate_zero_Heuristic() {
        _h = 0;
    }

    public double getWeight() {
        return this._weight;
    }

    public boolean hasNext() {
        return this._edges.size() > 0;
    }

    public void resetHeuristic() {
        _h = 0;
    }
}
