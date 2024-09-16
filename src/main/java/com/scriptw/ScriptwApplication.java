package com.scriptw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
//aqui agregamos 'extends SpringBootServletInitializer' para que pueda correr en tomcat
public class ScriptwApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(ScriptwApplication.class, args);
	}
}
