package Sekwang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SekwangApplication {
	public static void main(String[] args) {
		SpringApplication.run(SekwangApplication.class, args);
	}
}