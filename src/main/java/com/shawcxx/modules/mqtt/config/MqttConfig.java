package com.shawcxx.modules.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

/**
 * @author cjl
 * @date 2021/6/28 10:53
 * @description
 */
@Configuration
public class MqttConfig {
    @Value("${mqtt.host:}")
    private String mqttHost;

    @Value("${mqtt.username:}")
    private String mqttUserName;

    @Value("${mqtt.password:}")
    private String mqttPwd;



    /**
     * 构造一个默认的mqtt客户端操作bean
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{mqttHost});
        //自动重连
        options.setAutomaticReconnect(true);
        options.setUserName(mqttUserName);
        options.setPassword(mqttPwd.toCharArray());
        //是否删除会话
//        options.setCleanSession(false);
        factory.setConnectionOptions(options);
        return factory;
    }

}
