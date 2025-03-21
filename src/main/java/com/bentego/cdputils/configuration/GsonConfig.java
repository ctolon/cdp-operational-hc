package com.bentego.cdputils.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GsonConfig {

    @Bean
    public Gson gson() {
        // Create a GsonBuilder to configure Gson
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Add custom Gson configuration here
        //gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        gsonBuilder.serializeNulls();
        gsonBuilder.setPrettyPrinting();
        //gsonBuilder.disableHtmlEscaping();

        // Create and return the custom Gson instance
        return gsonBuilder.create();

    }
}