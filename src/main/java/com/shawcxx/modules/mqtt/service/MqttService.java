package com.shawcxx.modules.mqtt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author cjl
 * @date 2021/6/28 15:14
 * @description
 */
@Slf4j
@Service
public class MqttService {
    @Value("${mqtt.clientId}")
    private String clientId;
    @Value("${mqtt.topics}")
    private String[] topics;
    @Resource
    private MqttPahoClientFactory factory;

    private MqttPahoMessageDrivenChannelAdapter adapter;

    /**
     * 通道
     */
    @Bean(name = "mqttInputChannel")
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer messageProducer(@Qualifier("mqttInputChannel") MessageChannel messageChannel) {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId, factory, topics);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(0);
        adapter.setOutputChannel(messageChannel);
        adapter.setAutoStartup(true);
        this.adapter = adapter;
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            try {
                //这里拿到发布的消息内容，做具体的业务逻辑处理
                String data = message.getPayload().toString();
                String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
//                DataHandleService dataHandleService = SpringUtil.getBean(topic + "Service");
                log.info("主题:{},接收到的消息:{}", topic, data);
//                if (StrUtil.isNotBlank(data)) {
//                    dataHandleService.handleData(data);
//                }
            } catch (Exception ex) {
                log.error("数据处理失败,{}", message, ex);
            }
        };
    }
}
