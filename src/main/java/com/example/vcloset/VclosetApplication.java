package com.example.vcloset;

import com.example.vcloset.logic.smpt.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class VclosetApplication {

	@Autowired
	private EmailService emailService;

	public static void main(String[] args) {
		SpringApplication.run(VclosetApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	void sentEmail(){
		emailService.sendSimpleMessage(
				"jbolanosh@ucenfotec.ac.cr",
				"Subject",
				"body"
		);
	}

}
