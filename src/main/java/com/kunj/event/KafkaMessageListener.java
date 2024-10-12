package com.kunj.event;//package com.kunj.event;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.kafka.annotation.DltHandler;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.annotation.RetryableTopic;
//import org.springframework.retry.annotation.Backoff;
//import org.springframework.stereotype.Component;
//
///**
// * The type Kafka message listener.
// */
//@Component
//@Slf4j
//public class KafkaMessageListener {
//
//  /**
//   * Kafka message consumer.
//   *
//   * @param eventMessage the event message
//   */
//  @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 3000, multiplier = 2))
//  @KafkaListener(topics = "${kafka.topic.name}", groupId = "${kafka.consumer.group}", concurrency = "3")
//  public void kafkaMessageConsumer(String eventMessage) {
//    log.info("Kafka Message listener  --: {} " + eventMessage);
//    // if we can get error to process then send error to deadletter queue in kafka after attempt 3
//  }
//
//  /**
//   * Send message to dead letter queue.
//   *
//   * @param eventMessage the event message
//   */
//  @DltHandler
//  public void sendMessageToDeadLetterQueue(ConsumerRecord<String, Object> eventMessage) {
//    log.info("Dead letter kafka message key  {} | value   --: {} " + eventMessage.key(),
//        eventMessage.value());
//    // send notification to user
//  }
//}
