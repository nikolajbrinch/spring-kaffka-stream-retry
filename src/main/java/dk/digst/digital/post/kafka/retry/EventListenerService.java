package dk.digst.digital.post.kafka.retry;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import dk.digst.digital.post.kafka.retry.KafkaStreamsRetryApplication.GroupNames;
import dk.digst.digital.post.kafka.retry.KafkaStreamsRetryApplication.TopicNames;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventListenerService {

  @StreamListener(Processor.INPUT)
  public void eventHandler(GenericMessage<?> message) {
    log.debug("INPUT, received: {}", message);

    throw new RuntimeException("Create fault! in INPUT");
  }

  @KafkaListener(topics = TopicNames.RETRY_1, groupId = GroupNames.RETRY_1,
      containerFactory = "retry1KafkaListenerContainerFactory")
  public void retry1Handler(@Payload byte[] record,
      @Header(KafkaHeaders.DELIVERY_ATTEMPT) int delivery) {
    log.info("Recieved (delivery: {}) in RETRY1", delivery);

    throw new RuntimeException("Create fault! on retry1");
  }

  @KafkaListener(topics = TopicNames.RETRY_2, groupId = GroupNames.RETRY_2,
      containerFactory = "retry2KafkaListenerContainerFactory")
  public void retry2Handler(@Payload byte[] record,
      @Header(KafkaHeaders.DELIVERY_ATTEMPT) int delivery) {
    log.info("Recieved (delivery: {}) in RETRY2", delivery);

    throw new RuntimeException("Create fault! on retry2");
  }

}
