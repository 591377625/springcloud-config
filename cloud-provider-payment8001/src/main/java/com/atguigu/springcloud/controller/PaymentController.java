package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.sound.sampled.Line;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${server.port}")
    private String ServerPort;

    @PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int result = paymentService.create(payment);

        if (result>0){
            log.info("****插入成功****");
            return new CommonResult(200,"插入成功,端口号为"+ServerPort,payment);
        }else {
            log.info("****插入失败****");
            return new CommonResult(444,"插入失败",null);
        }
    }

    @GetMapping(value = "/payment/getPaymentById/{id}")
    public CommonResult getPaymentById(@PathVariable("id")Long id){
        Payment payment = paymentService.getPaymentById(id);

        if (payment!=null){
            log.info("****查询成功****");
            return new CommonResult(201,"查询成功,端口号为"+ServerPort,payment);
        }else {
            log.info("****查询失败****");
            return new CommonResult(445,"查询失败",null);
        }
    }

    @GetMapping(value = "/payment/discovery")
    public Object getDiscovery(){
        List<String> services = discoveryClient.getServices();//这个用来获取模块名称
//        //***element: cloud-order-service
        for (String element: services) {
            log.info("***element: "+element);
        }

        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info(instance.getServiceId()+"/t"+instance.getHost()+"/t"+instance.getPort()+"/t"+instance.getUri());
        }//CLOUD-PAYMENT-SERVICE 192.168.120.1 8001 http://192.168.120.1:8001

        return this.discoveryClient;
    }

    @GetMapping(value = "/payment/lb")
    public String getPaymentLB(){
        return ServerPort;
    }

    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeOut(){
        //暂停几秒钟线程
        try {
            TimeUnit.SECONDS.sleep(3);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ServerPort;
    }


}
