package com.example.Huaqi.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan(value = "com.example.Huaqi")
@EnableScheduling
public class Config {

}