package com.saintdan.framework.config;

import com.saintdan.framework.config.bean.ServerConfigBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.ipc.netty.http.server.HttpServer;

/**
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 12/10/2017
 * @since JDK1.8
 */
@Configuration
public class HttpServerConfig {

  @Bean public HttpServer httpServer(RouterFunction<?> routerFunction) {
    HttpHandler httpHandler = RouterFunctions.toHttpHandler(routerFunction);
    ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
    HttpServer server = HttpServer
        .create(serverConfigBean.getAddress(), serverConfigBean.getPort());
    server.newHandler(adapter);
    return server;
  }

  private final ServerConfigBean serverConfigBean;

  @Autowired public HttpServerConfig(ServerConfigBean serverConfigBean) {
    this.serverConfigBean = serverConfigBean;
  }
}
