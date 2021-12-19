package edu.uiowa.slis.graphtaglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NodeAuxInt extends TagSupport {
    private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(NodeAuxInt.class);

    public int doStartTag() throws JspException {
	Node theNode = (Node) findAncestorWithClass(this, Node.class);
	logger.trace("");
	try {
	    pageContext.getOut().print(theNode.getAuxInt());
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
