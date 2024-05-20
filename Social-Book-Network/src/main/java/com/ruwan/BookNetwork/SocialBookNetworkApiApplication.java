package com.ruwan.BookNetwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SocialBookNetworkApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialBookNetworkApiApplication.class, args);
	}

}
