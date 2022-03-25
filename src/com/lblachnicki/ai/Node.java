package com.lblachnicki.ai;

import java.util.*;

public class Node {
    private int _index;
    private double _weight;
    public double G;
    private double H;

    private List<Node> successors;
    private List<Node> parents;

    public Node(int index, double weight) {
        this._index = index;
        this._weight = weight;
        this.successors = new ArrayList<>();
        this.parents = new ArrayList<>();
    }

    public void addEdge(Node edge) {
        this.successors.add(edge);
        edge.parents.add(this);
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
                ", _h=" + H +
                ", F=" + getF() +
//                ", _edges=" + _edges +
                "}\n";
    }

    public List<Node> getEdges() {
        return this.successors;
    }

    public double getF() {
        return G + H;
    }

    public void precalculate_H0_Heuristic() {
        if (!successors.isEmpty()) {
            System.out.println("NOT USING ARTIFICIAL CHILD!");
            return;
        }
        precalculate_H0_Heuristic(0);
    }

    private void precalculate_H0_Heuristic(double currentWeight) {
        if (H == 0) {
            H = currentWeight + this._weight;
        } else if (H - this._weight < currentWeight) {
            H = currentWeight + this._weight;
        } else {
            return;
        }

        for (Node p : parents) {
            p.precalculate_H0_Heuristic(H);
        }
    }

    public void precalculate_random_Heuristic(Random random) {
        H = random.nextDouble();
    }

    public void precalculate_edge_count_Heuristic() {
        H = successors.size();
    }

    public void precalculate_parent_count_Heuristic() {
        H = parents.size();
    }

    public void precalculate_zero_Heuristic() {
        H = 0;
    }

    public double getWeight() {
        return this._weight;
    }

    public void resetHeuristic() {
        H = 0;
    }
}
