package com.taylor.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taylor.entity.Order;
import com.taylor.entity.Test;
import com.taylor.service.OrderService;
import com.taylor.service.TestService;

@RequestMapping("/test")
@Controller
public class TestController extends BaseAction {

    @Autowired
    private TestService testService;

    @Autowired
    private OrderService orderService;

    @ResponseBody
    @RequestMapping("/query")
    public List<Test> queryTest(Test test, HttpServletRequest request, HttpServletResponse response) {
        log.debug("这只是一个测试");
        return testService.queryTest(test);
    }

    @ResponseBody
    @RequestMapping("/order_list")
    public List<Order> queryOrderList(HttpServletRequest request, HttpServletResponse response) {
        Order order = new Order();
        order.setMerchantId(1129L);
        return orderService.queryOrderList(order);
    }
}
