package com.scoperetail.commons.ginti.Config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan({"com.scoperetail.commons.ginti"})
@EnableJpaRepositories(basePackages = {"com.scoperetail.commons.ginti"})
public class PersistenceConfig {

}

