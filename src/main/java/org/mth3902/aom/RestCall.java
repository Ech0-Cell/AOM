package org.mth3902.aom;

import org.springframework.web.client.RestClient;

/*
* Increment 1 of project:
* create Rest API that call university name with bahce
*/
public class RestCall {

    private String uriBase = "http://universities.hipolabs.com/search?name=bahce";
    private RestClient restClient = RestClient.create();

    public String getBahce() {

        return restClient.get()
                .uri(uriBase)
                .retrieve()
                .body(String.class);

    }
}
