package edu.uiowa.slis.graphtaglib;

import java.util.Enumeration;
import java.util.Vector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class NodeIterator extends BodyTagSupport {
    private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(NodeIterator.class);

    Vector<GraphNode> nodes = null;
    Enumeration<GraphNode> nodeEnum = null;
    GraphNode currentNode = null;

    boolean pruneOrphans = false;

    public int doStartTag() throws JspException {
	Graph theGraph = (Graph) findAncestorWithClass(this, Graph.class);

	logger.debug("doStartTag pruneOrphans: " + pruneOrphans);
	if (pruneOrphans)
	    theGraph.pruneOrphans();

	nodes = theGraph.nodes;

	if (nodes.size() == 0)
	    return SKIP_BODY;

	nodeEnum = nodes.elements();

	if (nodeEnum.hasMoreElements()) {
	    currentNode = nodeEnum.nextElement();
	    pageContext.setAttribute("isLastNode", !nodeEnum.hasMoreElements());
	    return EVAL_BODY_INCLUDE;
	}

	return SKIP_BODY;
    }

    public int doAfterBody() throws JspTagException {
	if (nodeEnum.hasMoreElements()) {
	    currentNode = nodeEnum.nextElement();
	    pageContext.setAttribute("isLastNode", !nodeEnum.hasMoreElements());
	    return EVAL_BODY_AGAIN;
	}

	return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
    	logger.debug("doEndTag");
	clearServiceState();
	return super.doEndTag();
    }

    private void clearServiceState() {
	nodes = null;
	nodeEnum = null;
	currentNode = null;

	pageContext.removeAttribute("isLastNode");
    }

    public boolean isPruneOrphans() {
	return pruneOrphans;
    }

    public void setPruneOrphanThreshold(int pruneOrphanThreshold) {
	Graph theGraph = (Graph) findAncestorWithClass(this, Graph.class);
	this.pruneOrphans = theGraph.nodes.size() > pruneOrphanThreshold;
    }
}
