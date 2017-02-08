package com.taylor.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taylor.annotation.ApiAuthInfo;
import com.taylor.annotation.ApiAuthInfo.UidFrom;
import com.taylor.common.AuthResult;
import com.taylor.entity.Test;
import com.taylor.service.TestService;

@RequestMapping("/test")
@Controller
public class TestController extends BaseAction {

	@Autowired
	private TestService testService;

	@ResponseBody
	@RequestMapping("/query")
	public List<Test> queryTest(@ApiAuthInfo(name = "userId", required = true, uidFrom = UidFrom.Parameter) AuthResult authResult, Test test, HttpServletRequest request, HttpServletResponse response) {
		log.debug("这只是一个测试");
		log.debug(authResult.getUid());
		return testService.queryTest(test);
	}
	
	@ResponseBody
	@RequestMapping("/query2")
	public List<Test> queryTest2(@ApiAuthInfo(name = "userId", required = true, uidFrom = UidFrom.Cookie) AuthResult authResult, Test test, HttpServletRequest request, HttpServletResponse response) {
		log.debug("这只是一个测试2");
		log.debug(authResult.getUid());
		return testService.queryTest(test);
	}
}
