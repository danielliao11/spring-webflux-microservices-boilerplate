package com.saintdan.framework.mock;

import com.saintdan.framework.constant.ResourcePath;
import com.saintdan.framework.param.UserParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

/**
 * Test
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 25/10/2017
 * @since JDK1.8
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserTest {

  @Test
  public void testCreateUser() throws Exception {
    webTestClient.post()
        .uri(ResourcePath.USERS)
        .body(Mono.just(getParam()), UserParam.class)
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isCreated()
        .expectBody().consumeWith(System.out::println);
  }

  @Test
  public void testGetUser() throws Exception {
    webTestClient.get()
        .uri(ResourcePath.USERS)
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk()
        .expectBody().consumeWith(System.out::println);
  }

  @Autowired private WebTestClient webTestClient;

  private UserParam getParam() {
    return UserParam.builder()
        .usr("admin")
        .name("admin")
        .pwd("admin")
        .description("admin")
        .role("admin")
        .build();
  }
}
