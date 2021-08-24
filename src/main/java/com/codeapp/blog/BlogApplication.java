package com.codeapp.blog;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.codeapp.blog.model.AuditInfo;
import com.codeapp.blog.repo.AuditInfoRepo;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
		
	}
	
	@Bean
	public CommandLineRunner setupInitailData(final AuditInfoRepo auditRepo){
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				Date dt = new Date();
				
				auditRepo.save(new AuditInfo(11,12,"title","body","success", null,"admin", new Timestamp(dt.getTime())));
			}
		};
	}

}