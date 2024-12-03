package com.example.waitingroom;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.util.backoff.FixedBackOff;

import com.example.waitingroom.domain.ParticipantsInfo;

@SpringBootApplication
public class WaitingroomApplication {

	private final Logger logger = LoggerFactory.getLogger(WaitingroomApplication.class);

	private final TaskExecutor exec = new SimpleAsyncTaskExecutor();

	public static void main(String[] args) {
		SpringApplication.run(WaitingroomApplication.class, args);
	}

	@Bean
	public CommonErrorHandler errorHandler(KafkaOperations<Object, Object> template) {
		return new DefaultErrorHandler(
				new DeadLetterPublishingRecoverer(template), new FixedBackOff(1000L,2)
		);
	}

	@Bean
	public RecordMessageConverter converter() {
		return new JsonMessageConverter();
	}

	@KafkaListener(id="groupId", topics="user")
	public void listen(ParticipantsInfo userId) {
		logger.info("Received: " + userId);
		if(userId.getUserId().startsWith("fail")) {
			throw new RuntimeException("failed");
		}
		this.exec.execute(() -> System.out.println("Hit Enter to terminate...."));
	}

	@KafkaListener(id = "dltGroup", topics = "user.DLT")
	public void dltListen(byte[] in) {
		logger.info("Received from DLT : " + new String(in) );
		this.exec.execute(()->System.out.println("Hit Enter to terminate..."));
	}

	@Bean
	public NewTopic topic() {
		return new NewTopic("user", 1, (short) 1);
	}

	@Bean
	public NewTopic dlt() {
		return new NewTopic("user.DLT", 1, (short) 1);
	}

}
