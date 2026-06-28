package org.ifolks.generator.bash;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "org.ifolks.generator")
@PropertySource("classpath:application.properties")
public class ApplicationConfig {

}
