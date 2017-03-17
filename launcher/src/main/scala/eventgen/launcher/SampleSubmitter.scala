package eventgen.launcher

import cakesolutions.kafka.{KafkaProducer, KafkaProducerRecord}
import com.typesafe.config.Config
import org.apache.kafka.common.serialization.StringSerializer

/**
  * Created by Andrew on 21.02.2017.
  */

class SampleSubmitter(config: Config) {

  private val producer = KafkaProducer(
    KafkaProducer.Conf(
      config,
      keySerializer = new StringSerializer,
      valueSerializer = new StringSerializer)
  )

  private val topic = config.getString("topic")

  def submitSample(key: String, value: String) = producer.send(
    KafkaProducerRecord(topic, key, value)
  )

  def close() = producer.close()

}