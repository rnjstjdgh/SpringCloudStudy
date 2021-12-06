package com.example.configservice.ymlGen;

import lombok.Data;

@Data
public class Spring {
    private Cloud cloud;

    public Spring(){}

    public Spring(Cloud cloud){
        this.cloud = cloud;
    }
}
