package com.taylor.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taylor.entity.Test;
import com.taylor.service.TestService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = "/test", description = "H5购物车接口")
@RequestMapping("/test")
@Controller
public class TestController extends BaseAction {

	@Autowired
	private TestService testService;

	@ResponseBody
	@RequestMapping("/query")
	@ApiOperation(value = "测试", httpMethod = "POST", response = List.class, notes = "query test")
	public List<Test> queryTest(Test test, HttpServletRequest request, HttpServletResponse response) {
		log.debug("这只是一个测试");
		return testService.queryTest(test);
	}
}
