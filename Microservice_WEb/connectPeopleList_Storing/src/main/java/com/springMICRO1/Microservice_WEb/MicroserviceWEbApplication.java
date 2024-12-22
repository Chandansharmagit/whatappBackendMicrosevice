package com.springMICRO1.Microservice_WEb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicroserviceWEbApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceWEbApplication.class, args);
	}

}
