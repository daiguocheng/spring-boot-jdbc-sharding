package org.daigc.sharding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableConfigurationProperties({ShardingDataSourceProperties.Slaves.class, ShardingDataSourceProperties.class})
public class ShardingDataSource extends AbstractRoutingDataSource implements EnvironmentAware {

    private final static char Q = '?';
    @Autowired
    private DataSourceProperties master;
    @Autowired
    private ShardingDataSourceProperties.Slaves slaves;
    @Autowired
    private ShardingDataSourceProperties shards;

    @Override
    protected Object determineCurrentLookupKey() {
        return ShardingContext.getCurrent();
    }

    @Override
    public void setEnvironment(Environment environment) {
        if (master == null) {
            throw new NullPointerException();
        }
        Map<Object, Object> targetDataSources = new HashMap<>();
        setTargetDataSources(targetDataSources);
        MasterSlavesShard globe = new MasterSlavesShard(master.determineUrl());
        ShardingContext.setGlobe(globe);
        buildAndPut(master, targetDataSources);
        setDefaultTargetDataSource(targetDataSources.get(master.determineUrl()));
        if (slaves != null) {
            for (DataSourceProperties slave : slaves) {
                globe.addSalve(slave.determineUrl());
                buildAndPut(slave, targetDataSources);
            }
        }
        if (shards == null || shards.getShards() == null || shards.getShards().length == 0) {
            return;
        }
        for (ShardingDataSourceProperties.Shard e : shards.getShards()) {
            if (e.getMaster() == null) {
                throw new RuntimeException("The master is required for a shard.");
            }
            buildAndPut(e.getMaster(), targetDataSources);
            MasterSlavesShard shard = new MasterSlavesShard(e.getMaster().determineUrl());
            if (e.getSlaves() != null) {
                for (DataSourceProperties salve : e.getSlaves()) {
                    shard.addSalve(salve.determineUrl());
                    buildAndPut(salve, targetDataSources);
                }
            }
            ShardingContext.add(shard);
        }

    }

    private void buildAndPut(DataSourceProperties dsp, Map<Object, Object> targetDataSources) {
        String url = dsp.determineUrl();
        String masterUrl = master.determineUrl();
        if (url.indexOf(Q) == -1 && masterUrl.indexOf(Q) > -1) {
            url += masterUrl.substring(masterUrl.indexOf(Q));
        }
        if (targetDataSources.containsKey(url)) {
            return;
        }
        DataSourceBuilder builder = master.initializeDataSourceBuilder();
        if (dsp.getUsername() != null) {
            builder.username(dsp.getUsername());
        }
        if (dsp.getPassword() != null) {
            builder.password(dsp.getPassword());
        }
        targetDataSources.put(dsp.determineUrl(), builder.url(url).build());
    }
}
