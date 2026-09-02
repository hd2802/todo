package com.todo.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(HealthController.class)
@AutoConfigureRestTestClient
public class HealthControllerTest {

    @Autowired
    private RestTestClient restTestClient;

    @Test
    public void visitingHealthRoot_returnsPositiveStatusMessage() {
        String responseBody = restTestClient.get().uri("/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isEqualTo("OK");
    }
}
