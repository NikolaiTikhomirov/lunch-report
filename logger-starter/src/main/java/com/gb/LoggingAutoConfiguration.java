package com.gb;

import com.gb.aspect.RecoverExceptionAspect;
import com.gb.aspect.TimeCounterAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnProperty(value = "logging.enabled", havingValue = "true")
public class LoggingAutoConfiguration {

  @Bean
  RecoverExceptionAspect recoverExceptionAspect(LoggingProperties loggingProperties) {
    return new RecoverExceptionAspect(loggingProperties);
  }

  @Bean
  TimeCounterAspect timeCounterAspect(LoggingProperties loggingProperties) {
    return new TimeCounterAspect(loggingProperties);
  }

}
