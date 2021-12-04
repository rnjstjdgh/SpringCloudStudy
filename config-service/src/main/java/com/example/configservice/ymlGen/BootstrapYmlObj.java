package com.example.configservice.ymlGen;

import lombok.Data;

@Data
public class BootstrapYmlObj {
    private Spring spring;

    public BootstrapYmlObj(){};

    public BootstrapYmlObj(Spring spring){
        this.spring = spring;
    }
}
