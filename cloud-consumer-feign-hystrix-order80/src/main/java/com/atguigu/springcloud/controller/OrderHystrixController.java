package com.atguigu.springcloud.controller;

import cn.hutool.core.util.IdUtil;
import com.atguigu.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")
public class OrderHystrixController {
    @Autowired
    PaymentHystrixService paymentHystrixService;

    @GetMapping("/consumer/hystrix/ok/{id}")
    public String paymentInfo_ok(@PathVariable(value = "id") Integer id){
        String s = paymentHystrixService.paymentInfo_ok(id);
        return s;
    }

    @GetMapping("/consumer/hystrix/timeout/{id}")
//    @HystrixCommand(fallbackMethod = "paymentTimeOutFallBackMethod",commandProperties =
//    @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="1500"))
    @HystrixCommand
    public String paymentInfo_timeout(@PathVariable(value = "id") Integer id){
//        int x = 10/0;
        String s = paymentHystrixService.paymentInfo_timeout(id);
        return s;
    }

    public String paymentTimeOutFallBackMethod(Integer id){//这里的参数要一样不然会报错
        return "我是消费者80,支付系统繁忙,请稍后重试 o(╥﹏╥)o";
    }

    //下面是全局fallback方法
    public String payment_Global_FallbackMethod(){
        return "Global异常处理信息,请稍后再试！！";
    }


}
