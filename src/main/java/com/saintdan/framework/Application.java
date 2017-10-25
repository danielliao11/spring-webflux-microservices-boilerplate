package com.saintdan.framework;

import static com.saintdan.framework.constant.ResourcePath.PROFILE;
import static com.saintdan.framework.constant.ResourcePath.USERS;
import static com.saintdan.framework.constant.ResourcePath.USER_ID;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler;

import com.saintdan.framework.filter.LimitFilter;
import com.saintdan.framework.filter.ValidateFilter;
import com.saintdan.framework.handler.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebHandler;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

/**
 * Entrance of <p>spring-micro-services-boilerplate2</p> <p> "Engine start" </p>
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 09/10/2017
 * @since JDK1.8
 */
@SpringBootApplication
@EnableReactiveMongoRepositories
@EnableWebFlux
public class Application {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  @SuppressWarnings("unchecked")
  public RouterFunction<ServerResponse> routing() {
    return route(GET(USERS).and(accept(APPLICATION_JSON_UTF8)), userHandler::all)
        .andRoute(GET(USER_ID).and(accept(APPLICATION_JSON_UTF8)), userHandler::get)
        .andRoute(POST(USERS).and(accept(APPLICATION_JSON_UTF8)), userHandler::create)
        .andRoute(PUT(USER_ID).and(accept(APPLICATION_JSON_UTF8)), userHandler::update)
        .andRoute(DELETE(USER_ID).and(accept(APPLICATION_JSON_UTF8)), userHandler::delete)
        .andRoute(GET(PROFILE), userHandler::profile);
  }

  @Bean public HttpHandler handler() {
    return WebHttpHandlerBuilder
        .webHandler((WebHandler) toHttpHandler(routing()))
        .filter(limitFilter)
        .filter(validateFilter)
        .build();
  }

  private final UserHandler userHandler;
  private final LimitFilter limitFilter;
  private final ValidateFilter validateFilter;

  @Autowired public Application(UserHandler userHandler, LimitFilter limitFilter, ValidateFilter validateFilter) {
    this.userHandler = userHandler;
    this.limitFilter = limitFilter;
    this.validateFilter = validateFilter;
  }

}
