package com.example.demo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner, ApplicationContextAware {
	@Autowired
	@Qualifier("greetingEnglishImpl")
	Greeting greetingEnglish;

	@Autowired
	PersonRepository personRepository; // give me a new bean!

	//@Autowired
	ApplicationContext applicationContext;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(greetingEnglish.hi()); // Hi John
		System.out.println(applicationContext.getBean("greetingSpanishImpl", Greeting.class).hi()); // Hola John

		personRepository.setName("Paul");

		greetingEnglish.getPersonRepository().setName("Ringo");
		System.out.println(greetingEnglish.hi()); // Hi Ringo
		System.out.println(applicationContext.getBean("greetingSpanishImpl", Greeting.class).hi()); // Hola John

		System.out.println(applicationContext.getBean("personRepository", PersonRepository.class).getName()); // John
		applicationContext.getBean("personRepository", PersonRepository.class).setName("George");
		System.out.println(applicationContext.getBean("personRepository", PersonRepository.class).getName()); // John
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Configuration
	static class Config {
		@Bean // name = greeting, scope = singleton
		public Greeting greetingEnglishImpl() {
			return new GreetingEnglishImpl();
		}
	}
}

interface Greeting {
	String hi();
	PersonRepository getPersonRepository();

}

class GreetingEnglishImpl implements Greeting {
	@Autowired
	PersonRepository personRepository;

	public String hi() {
		return "Hi " + personRepository.getName();
	}

	@Override
	public PersonRepository getPersonRepository() {
		return personRepository;
	}

}

@Component
class GreetingSpanishImpl implements Greeting {
	private final PersonRepository personRepository;

	public GreetingSpanishImpl(PersonRepository personRepository) { // try to use as much as you can constructor injection
		this.personRepository = personRepository;
	}

	public String hi() {
		return "Hola " + personRepository.getName();
	}

	@Override
	public PersonRepository getPersonRepository() {
		return personRepository;
	}
}

@Component
@Scope("prototype")
class PersonRepository {
	private String name;

	@PostConstruct // This executes once the object exists and all dependencies were injected
	public void init() {
		this.name = "John";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}