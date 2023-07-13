package edu.uiowa.slis.graphtaglib;

public class GraphEdge {
	GraphNode source = null;
	GraphNode target = null;
	double weight = 0;
	int auxInt = 0;
	String auxString = null;
	double auxDouble = 0.0;

	public GraphEdge(GraphNode source, GraphNode target, double weight) {
		super();
		this.source = source;
		this.target = target;
		this.weight = weight;
	}

	public GraphEdge(GraphNode source, GraphNode target, double weight, int auxInt, String auxString, double auxDouble) {
		super();
		this.source = source;
		this.target = target;
		this.weight = weight;
		this.auxInt = auxInt;
		this.auxString = auxString;
		this.auxDouble = auxDouble;
	}

	public GraphNode getSource() {
		return source;
	}

	public void setSource(GraphNode source) {
		this.source = source;
	}

	public GraphNode getTarget() {
		return target;
	}

	public void setTarget(GraphNode target) {
		this.target = target;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getAuxInt() {
		return auxInt;
	}

	public void setAuxInt(int auxInt) {
		this.auxInt = auxInt;
	}

	public String getAuxString() {
		return auxString;
	}

	public void setAuxString(String auxString) {
		this.auxString = auxString;
	}

	public double getAuxDouble() {
		return auxDouble;
	}

	public void setAuxDouble(double auxDouble) {
		this.auxDouble = auxDouble;
	}
}
