package com.app.debate1;

import com.app.debate1.pubsub.Publisher;
import com.app.debate1.pubsub.Subscriber;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Debate1Application {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Debate1Application.class, args);
		Publisher publisher = context.getBean(Publisher.class);
		Subscriber subscriber = context.getBean(Subscriber.class);


		Peer peer = new Peer(null, publisher);
		peer.start();
	}


}
