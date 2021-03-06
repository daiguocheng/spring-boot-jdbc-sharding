package org.daigc.sharding;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedList;

@Getter
@Setter
@ConfigurationProperties("spring.datasource.shards")
public class ShardingDataSourceProperties {

    private Shard[] shards;

    @Getter
    @Setter
    static class Shard {
        private DataSourceProperties master;
        private Slaves slaves;
    }

    @ConfigurationProperties("spring.datasource.slaves")
    static class Slaves extends LinkedList<DataSourceProperties> {
    }
}
