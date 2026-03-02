package com.seuprojeto.epidemiologia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EpidemiologiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EpidemiologiaApplication.class, args);
	}

}
