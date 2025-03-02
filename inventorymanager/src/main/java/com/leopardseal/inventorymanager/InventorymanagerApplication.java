package com.leopardseal.inventorymanager;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class InventorymanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventorymanagerApplication.class, args);
	}

	// @Bean
    // ApplicationListener<ApplicationReadyEvent> basicsApplicationListener(MyUserRepository repository) {
    //     return event->repository.save(new com.leopardseal.inventorymanager.MyUser(null, "thadaniaarush@gmail.com", "None"));
    // }
	// @Bean
	// public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
	// 	return args -> {

	// 		System.out.println("Let's inspect the beans provided by Spring Boot:");

	// 		String[] beanNames = ctx.getBeanDefinitionNames();
	// 		Arrays.sort(beanNames);
	// 		for (String beanName : beanNames) {
	// 			System.out.println(beanName);
	// 		}

	// 	};
	// }

}

