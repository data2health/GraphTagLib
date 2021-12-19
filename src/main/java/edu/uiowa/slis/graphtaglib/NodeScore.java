package edu.uiowa.slis.graphtaglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NodeScore extends TagSupport {
    private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(NodeScore.class);

    @SuppressWarnings("unused")
    public int doStartTag() throws JspException {
	Graph theGraph = (Graph) findAncestorWithClass(this, Graph.class);
	Node theNode = (Node) findAncestorWithClass(this, Node.class);
	logger.trace("");
	try {
	    pageContext.getOut().print(theNode.getScore());
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
	clearServiceState();
	return super.doEndTag();
    }

    private void clearServiceState() {
    }
}
