package edu.uiowa.slis.graphtaglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Edge extends TagSupport {
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(Edge.class);

	GraphNode sourceNode = null;
	GraphNode targetNode = null;
	GraphEdge currentEdge = null;
	String source = null;
	String target = null;
	double weight = 0.0;
	int auxInt = 0;
	String auxString = null;
	double auxDouble = 0.0;

	public int doStartTag() throws JspException {
		Graph theGraph = (Graph) findAncestorWithClass(this, Graph.class);
		EdgeIterator theIterator = (EdgeIterator) findAncestorWithClass(this, EdgeIterator.class);

		if (theIterator == null) {
			logger.trace("Adding edge source: " + source + "\ttarget: " + target + "\tweight: " + weight);
			GraphNode sourceNode = theGraph.getNode(source);
			GraphNode targetNode = theGraph.getNode(target);
			if (sourceNode == null) {
				logger.error("source node missing for edge <" + source + ", " + target + ">");
			} else if (targetNode == null) {
				logger.error("target node missing for edge <" + source + ", " + target + ">");
			} else if (weight == 0) {
				logger.error("edge weight cannot be 0");
			} else
				theGraph.addEdge(new GraphEdge(sourceNode, targetNode, weight, auxInt, auxString, auxDouble));
			return SKIP_BODY;
		} else {
			currentEdge = theIterator.currentEdge;
			sourceNode = theIterator.currentEdge.getSource();
			targetNode = theIterator.currentEdge.getTarget();
			weight = theIterator.currentEdge.getWeight();
			auxInt = theIterator.currentEdge.getAuxInt();
			auxString = theIterator.currentEdge.getAuxString();
			auxDouble = theIterator.currentEdge.getAuxDouble();
			// return EVAL_BODY_INCLUDE;
		}

		// currentEdge = theIterator.currentEdge;
		logger.trace("");
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspTagException, JspException {
		clearServiceState();
		return super.doEndTag();
	}

	private void clearServiceState() {
		sourceNode = null;
		targetNode = null;
		currentEdge = null;
		source = null;
		target = null;
		weight = 0;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
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
