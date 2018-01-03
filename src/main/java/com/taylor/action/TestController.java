package com.taylor.action;

import com.taylor.common.JSONUtil;
import com.taylor.common.SpringBeanUtils;
import com.taylor.entity.TestEntity;
import com.taylor.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/test")
@Controller
public class TestController extends BaseAction {

    @Autowired
    private TestService testService;

    @ResponseBody
    @RequestMapping("/query")
    public List<TestEntity> queryTest(TestEntity test, HttpServletRequest request, HttpServletResponse response) {
        log.debug("这只是一个测试12132132");
        return testService.find(test);
    }
    
    @ResponseBody
    @RequestMapping("/tf")
    public String testFactory(){
        Object demo = SpringBeanUtils.getBean("demo");
        return JSONUtil.toJSONString(demo);
    }
}
