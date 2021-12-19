package edu.uiowa.slis.graphtaglib;

import java.util.Enumeration;
import java.util.Vector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class EdgeIterator extends BodyTagSupport {
    private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(EdgeIterator.class);

    Vector<GraphEdge> edges = null;
    Enumeration<GraphEdge> edgeEnum = null;
    GraphEdge currentEdge = null;

    public int doStartTag() throws JspException {
	Graph theGraph = (Graph) findAncestorWithClass(this, Graph.class);
	edges = theGraph.edges;

	if (edges.size() == 0)
	    return SKIP_BODY;

	edgeEnum = edges.elements();

	if (edgeEnum.hasMoreElements()) {
	    currentEdge = edgeEnum.nextElement();
	    logger.trace("edge source: " + currentEdge.source.ID + " " + currentEdge.source.uri + "\ttarget: " + currentEdge.target.ID + " "
		    + currentEdge.target.uri + "\tweight: " + currentEdge.weight);
	    pageContext.setAttribute("isLastEdge", !edgeEnum.hasMoreElements());
	    return EVAL_BODY_INCLUDE;
	}

	return SKIP_BODY;
    }

    public int doAfterBody() throws JspTagException {
	if (edgeEnum.hasMoreElements()) {
	    currentEdge = edgeEnum.nextElement();
	    logger.trace("edge source: " + currentEdge.source.ID + "\ttarget: " + currentEdge.target.ID + "\tweight: " + currentEdge.weight);
	    logger.trace("edge source: " + currentEdge.source.ID + " " + currentEdge.source.uri + "\ttarget: " + currentEdge.target.ID + " "
			    + currentEdge.target.uri + "\tweight: " + currentEdge.weight);
	    pageContext.setAttribute("isLastEdge", !edgeEnum.hasMoreElements());
	    return EVAL_BODY_AGAIN;
	}

	return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
	clearServiceState();
	return super.doEndTag();
    }

    private void clearServiceState() {
	edges = null;
	edgeEnum = null;
	currentEdge = null;

	pageContext.removeAttribute("isLastEdge");
    }
}
