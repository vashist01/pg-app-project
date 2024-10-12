package com.kunj.config;//package com.kunj.config;
//
//import java.util.HashMap;
//import java.util.Map;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//
///**
// * The type Kafka consumer config.
// */
//@Configuration
//@EnableKafka
//public class KafkaConsumerConfig {
//
//
//  /**
//   * Kafka listener container factory concurrent kafka listener container factory.
//   *
//   * @param bootstrapAddress the bootstrap address
//   * @param consumerGroup    the consumer group
//   * @return the concurrent kafka listener container factory
//   */
//  @Bean
//  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
//      @Value("${kafka.bootstrap.address}") String bootstrapAddress,
//      @Value("${kafka.consumer.group}") String consumerGroup) {
//    ConcurrentKafkaListenerContainerFactory<String, String>
//        factory = new ConcurrentKafkaListenerContainerFactory<>();
//    factory.setConsumerFactory(consumerFactory(bootstrapAddress, consumerGroup));
//    return factory;
//  }
//
//  /**
//   * Consumer factory consumer factory.
//   *
//   * @param bootstrapAddress the bootstrap address
//   * @param consumerGroup    the consumer group
//   * @return the consumer factory
//   */
//  public ConsumerFactory<String, String> consumerFactory(String bootstrapAddress,
//      String consumerGroup) {
//    Map<String, Object> props = new HashMap<>();
//    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
//    props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
//    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//    return new DefaultKafkaConsumerFactory<>(props);
//  }
//}
