package com.kunj;

import com.kunj.service.PropertyCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class PgServiceApplication implements CommandLineRunner {
	private final PropertyCategoryService propertyCategoryService;

	/**
	 * Instantiates a new Kunj application.
	 *
	 * @param propertyCategoryService the property category service
	 */
	public PgServiceApplication(PropertyCategoryService propertyCategoryService) {
		this.propertyCategoryService = propertyCategoryService;
	}

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		log.info("This message will be written to the application's CloudWatch log");
		SpringApplication.run(PgServiceApplication.class, args);
	}

	/**
	 * Callback used to run the bean.
	 *
	 * @param args incoming main method arguments
	 * @throws Exception on error
	 */
	@Override
	public void run(String... args) throws Exception {
		propertyCategoryService.readDefaultPropertyCategory();
	}
}
