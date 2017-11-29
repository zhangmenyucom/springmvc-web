package com.taylor.action;

import com.taylor.dto.TestBean;
import com.taylor.entity.Test;
import com.taylor.service.TestService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "/test", description = "H5购物车接口")
@RequestMapping("/test")
@Controller
public class TestController extends BaseAction {

    @Autowired
    private TestService testService;

    @ResponseBody
    @RequestMapping("/query")
    @ApiOperation(value = "获取购物车", httpMethod = "POST", response = Test.class, notes = "获取购物车")
    public Test queryTest(@RequestBody TestBean testBean, HttpServletRequest request, HttpServletResponse response) {
        Test test = new Test();
        BeanUtils.copyProperties(testBean, test);
        log.debug("这只是一个测试");
        return testService.queryTest(test).get(0);
    }

    @ResponseBody
    @RequestMapping("/test")
    @ApiOperation(value = "获取购物车", httpMethod = "POST", response = String.class, notes = "test")
    public String test(@RequestBody List<Integer> list, HttpServletRequest request, HttpServletResponse response) {
        return list.toString();
    }
}
