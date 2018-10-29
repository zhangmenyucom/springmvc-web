package com.xy.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(TestServlet.class);

    /**
     *
     */
    private static final long serialVersionUID = -4263672728918819141L;

    @Override
    public void init() throws ServletException {
        logger.info("...TestServlet init() init..........");
        super.init();
    }

    @Override
    public void destroy() {
        logger.info("...TestServlet init() destory..........");
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("...TestServlet doPost() start..........");
        //操作attribute
        request.setAttribute("a", "a");
        request.setAttribute("a", "b");
        request.getAttribute("a");
        request.removeAttribute("a");
        //操作session
        request.getSession().setAttribute("a", "a");
        request.getSession().getAttribute("a");
        request.getSession().invalidate();
        logger.info("...TestServlet doPost() end..........");
    }
}
