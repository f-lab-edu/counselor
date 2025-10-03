package com.hyejin.counselor.app;

import com.hyejin.counselor.app.common.ApiResponse;
import com.hyejin.counselor.core.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("회원 컨트롤러")
@SpringBootTest(classes = com.hyejin.counselor.Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @DisplayName("user 정보 저장 후 성공여부 확인")
    @Test
    public void exampleTest() {
        String url = "http://localhost:" + port + "/user";

        // 요청 객체 생성
        User request = new User("userName", "01022992299");

        // 헤더 설정 (Content-Type: application/json)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 바디 설정
        HttpEntity<User> httpRequest = new HttpEntity<>(request, headers);

        // get 방식 요청
        // ApiResponse response = this.restTemplate.getForObject(url, ApiResponse.class);
        ResponseEntity<ApiResponse> response = this.restTemplate.postForEntity(url, httpRequest, ApiResponse.class);

        System.out.println("===============================");
        System.out.println(response.getStatusCode());
        System.out.println(response.getHeaders());
        System.out.println(response.getBody().getResultCode());
        System.out.println(response.getBody().getMessage());
        System.out.println(response.getBody().getData());
        System.out.println("===============================");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getResultCode()).isEqualTo("SUCCESS");
    }

}
