package com.taylor.filter;
 
import java.io.IOException;
 
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class TestFilter implements Filter {
	private Logger logger = LoggerFactory.getLogger(TestFilter.class);
 
	@Override
	public void destroy() {
		logger.info("..............execute TestFilter destory()..............");
	}
 
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
		logger.info("..............execute TestFilter doFilter()..............");
		arg2.doFilter(arg0, arg1);
	}
 
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		logger.info("..............execute TestFilter  init()..............");
	}
}
