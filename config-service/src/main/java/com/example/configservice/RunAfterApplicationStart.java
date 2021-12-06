package com.example.configservice;

import com.example.configservice.ymlGen.BootstrapYmlObj;
import com.example.configservice.ymlGen.Cloud;
import com.example.configservice.ymlGen.Config;
import com.example.configservice.ymlGen.Spring;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class RunAfterApplicationStart implements ApplicationRunner {

    Environment env;

    @Autowired
    public RunAfterApplicationStart(Environment env){
        this.env = env;
    }

    @Override
    public void run(ApplicationArguments args) throws IOException {
        String endPoint = "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + env.getProperty("local.server.port");
        log.info(endPoint);
        Map<String,String> map = new HashMap<>();
//        map.put("user_service", "C:\\Exception\\spring_cloud\\SpringCloudStudy\\user-service\\");
//        map.put("discovery_service", "C:\\Exception\\spring_cloud\\SpringCloudStudy\\discoveryservice\\");
//        map.put("catalog_service", "C:\\Exception\\spring_cloud\\SpringCloudStudy\\catalog-service\\");
//        map.put("gateway_service", "C:\\Exception\\spring_cloud\\SpringCloudStudy\\apigateway-service\\");
//        map.put("order_service", "C:\\Exception\\spring_cloud\\SpringCloudStudy\\order-service\\");

        map.put("user_service", "..\\user-service\\");
        map.put("discovery_service", "..\\discoveryservice\\");
        map.put("catalog_service", "..\\catalog-service\\");
        map.put("gateway_service", "..\\apigateway-service\\");
        map.put("order_service", "..\\order-service\\");

        for(String key : map.keySet()) {
            generateBootStrap(key, endPoint, map.get(key) + "src\\main\\resources\\bootstrap.yml");
        }
    }

    private void generateBootStrap(String serviceName, String configServerEndpoint, String filePath)
            throws IOException {
        Config config = new Config();
        config.setName(serviceName);
        config.setUri(configServerEndpoint);

        Cloud cloud = new Cloud(config);
        Spring spring = new Spring(cloud);
        BootstrapYmlObj bootstrapYmlObj = new BootstrapYmlObj(spring);

        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        om.writeValue(new File(filePath), bootstrapYmlObj);
    }
}