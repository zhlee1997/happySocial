package com.jeremylee.insta_user_service.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    //    every Bean must be public
    //    there is no Bean should be defined private
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
