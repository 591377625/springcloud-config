package com.atguigu.springcloud.service;

import org.springframework.stereotype.Service;

@Service
public class PaymentHysrixServiceImpl implements PaymentHystrixService{
    @Override
    public String paymentInfo_ok(Integer id) {
        return "服务器宕机了_ok  o(╥﹏╥)o ";
    }

    @Override
    public String paymentInfo_timeout(Integer id) {
        return "服务器宕机了_timeout  o(╥﹏╥)o ";
    }

}
