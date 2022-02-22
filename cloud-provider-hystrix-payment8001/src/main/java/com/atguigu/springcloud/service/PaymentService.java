package com.atguigu.springcloud.service;


import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

/**
 * @author 卢意
 * @create 2020-10-25 13:45
 */
@Service
public class PaymentService {

    /**
     * 正常访问 的方法
     * @param id
     * @return
     */
    public String paymentInfo_ok(Integer id){
        return "线程池: "+Thread.currentThread().getName()+" paymentInfo_ok,Id "+id+"\t"+"O(∩_∩)O哈哈~";
    }

    /**
     * 会报错的方法  用于模拟触发降级
     * @param id
     * @return
     */
    //HystrixCommand注解表示异常，HystrixProperty注解表示超时
    @HystrixCommand(fallbackMethod = "paymentInfo_timeoutHandler",commandProperties =
    @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="3000"))
    public String paymentInfo_timeout(Integer id){
        Integer Timeout=5;
        try {
            TimeUnit.SECONDS.sleep(Timeout);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return "线程池: "+Thread.currentThread().getName()+" paymentInfo_timeout,Id "+id+"\t"+"O(∩_∩)O哈哈~  耗时"+Timeout+"秒钟";
    }


    public String paymentInfo_timeoutHandler(Integer id){
        return "线程池: "+Thread.currentThread().getName()+" paymentInfo_timeoutHandler,Id "+id+"\t"+"o(╥﹏╥)o 服务器出错了";
    }


    //====服务熔断
    /**
     * 在10秒窗口期中10次请求有6次是请求失败的,断路器将起作用
     *
     * @param id
     * @return
     */
    @HystrixCommand(
            fallbackMethod = "paymentCircuitBreaker_fallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),// 是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),// 请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),// 时间窗口期/时间范文
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60")// 失败率达到多少后跳闸
    }
    )
    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
        if (id < 0) {
            throw new RuntimeException("*****id不能是负数");
        }
        String serialNumber = IdUtil.simpleUUID();
        return Thread.currentThread().getName() + "\t" + "调用成功,流水号:" + serialNumber;
    }
    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id) {
        return "id 不能负数,请稍后重试,o(╥﹏╥)o id:" + id;
    }

}

