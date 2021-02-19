package com.example.demo.mock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class MockTest {
    @Autowired
    Greeting greeting;
    // @Autowired
    @MockBean
    PersonPublisher personPublisher;

    @Test
    public void test() {
        Mockito.when(personPublisher.publish("Hello John")).thenReturn(true);

        Mockito.when(personPublisher.publish(Mockito.anyString())).thenReturn(true);
        Assertions.assertTrue(greeting.hi("John"));

        greeting.cautiousHi("Paul");
        Mockito.verify(personPublisher).publish("Cautious hello Paul");
    }

    @Configuration
    static class Config {
        @Bean
        public Greeting greeting(PersonPublisher personPublisher) {
            return new GreetingEnglishImpl(personPublisher);
        }

//        @Bean
//        public PersonPublisher personPublisher() {
//            return new PersonPublisher();
//        }

//        @Bean
//        public PersonPublisher personPublisher() {
//            return Mockito.mock(PersonPublisher.class);
//        }
    }
}

interface Greeting {
    boolean hi(String name);
    void cautiousHi(String name);
}

class GreetingEnglishImpl implements Greeting {
    private PersonPublisher personPublisher;

    public GreetingEnglishImpl(PersonPublisher personPublisher) {
        this.personPublisher = personPublisher;
    }

    @Override
    public boolean hi(String name) {
        return personPublisher.publish("Hello " + name);
    }

    @Override
    public void cautiousHi(String name) {
        personPublisher.publish("Cautious hello " + name);
    }
}

class PersonPublisher {
    public boolean publish(String message) {
        // When it works, should return true
        throw new RuntimeException("There's no queue so I cannot test!!!");
    }
}