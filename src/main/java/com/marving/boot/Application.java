package com.marving.boot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@ServletComponentScan
@MapperScan("com.marving.boot.dao")
public class Application {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(Application.class);
		if(args != null && args.length > 0) {
			application.setWebApplicationType(WebApplicationType.NONE);
		}
		application.run(args);
	}
}


