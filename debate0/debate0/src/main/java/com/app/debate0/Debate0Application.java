package com.app.debate0;

import com.app.debate0.pubsub.Publisher;
import com.app.debate0.pubsub.Subscriber;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Debate0Application {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Debate0Application.class, args);
		Publisher publisher = context.getBean(Publisher.class);
		Subscriber subscriber = context.getBean(Subscriber.class);

		Peer peer = new Peer("abc", publisher);
		peer.start();
	}

}
