package io.github.panjung99.panapi.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"io.github.panjung99.panapi"})
@MapperScan("io.github.panjung99.panapi.**.dao")
@EnableScheduling
@Async
public class PanApiApplication {

    public static void main(String[] args) {
        // 关闭 Reactor 的 Operator Debug，避免冗余的 Assembly trace 日志
        System.setProperty("reactor.trace.operatorStacktrace", "false");
        SpringApplication.run(PanApiApplication.class, args);
    }

}
