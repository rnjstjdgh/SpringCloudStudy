package com.example.configservice.ymlGen;

import lombok.Data;

@Data
public class Config{
    private String uri;
    private String name;

    public Config(){}

    public Config(String uri, String name){
        this.uri = uri;
        this.name = name;
    }
}
