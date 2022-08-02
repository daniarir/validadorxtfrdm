package co.gov.igac.snc.structureXtf;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class ValidateStructureXtfApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValidateStructureXtfApplication.class, args);
	}
	
	@Bean
	public GroupedOpenApi publicApi() {
	      return GroupedOpenApi.builder()
	              .group("springshop-public")
	              .packagesToScan("co.gov.igac.snc.structureXtf.controller")
	              .build();
	}
}
