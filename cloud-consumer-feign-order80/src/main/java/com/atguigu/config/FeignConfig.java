package com.atguigu.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 卢意
 * @create 2020-10-25 10:57
 */
@Configuration
public class FeignConfig {
	@Bean
	Logger.Level feignLoggerLevel(){
		//详细日志
		return Logger.Level.FULL;
	}
}

