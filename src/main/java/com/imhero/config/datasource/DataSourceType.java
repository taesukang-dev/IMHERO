package com.imhero.config.datasource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.annotation.Profile;

@Profile("local")
@Getter
@AllArgsConstructor
public enum DataSourceType {
    SLAVE("slave"),
    MASTER("master"),
    ;

    private final String name;
}
