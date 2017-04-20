package org.fomky.tasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import ratpack.spring.config.EnableRatpack;

@SpringBootApplication
@EnableRatpack
@ImportResource("classpath:applications.xml")
@ComponentScan("org.fomky.tasks")
public class TasksApplication {

	public static void main(String[] args) {
		SpringApplication.run(TasksApplication.class, args);
	}
}
