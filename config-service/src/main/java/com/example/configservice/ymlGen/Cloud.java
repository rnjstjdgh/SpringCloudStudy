package com.example.configservice.ymlGen;

import lombok.Data;

@Data
public class Cloud{
    private Config config;

    public Cloud(){};

    public Cloud(Config config){
        this.config = config;
    }
}