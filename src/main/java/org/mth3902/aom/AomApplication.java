package org.mth3902.aom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class AomApplication {

    public static void main(String[] args) {

        SpringApplication.run(AomApplication.class, args);

        RestClient restClient = RestClient.create();
        String uriBase = "http://universities.hipolabs.com/search?name=bahce";

        String result = restClient.get()
                .uri(uriBase)
                .retrieve()
                .body(String.class);

        System.out.println(result);
    }
}
