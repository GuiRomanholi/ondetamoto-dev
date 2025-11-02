package br.com.fiap.ondetamoto;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

//http://localhost:8081/swagger-ui/index.html
//http://localhost:8081/register
//http://localhost:8081/h2-console
//JDBC URL:	jdbc:h2:mem:testdb, User Name: sa, Password: deixar em branco
@SpringBootApplication
@EnableCaching
@OpenAPIDefinition(info =
@Info(title = "API OndeTaMoto", description = "API RESTful com Swagger para OndeTaMoto", version = "v1"))
public class OndetamotoApplication {

	public static void main(String[] args) {
		SpringApplication.run(OndetamotoApplication.class, args);
	}

}
