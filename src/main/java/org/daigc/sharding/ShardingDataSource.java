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
@EnableConfigurationProperties({ShardingDataSourceProperties.Slavers.class, ShardingDataSourceProperties.class})
public class ShardingDataSource extends AbstractRoutingDataSource implements EnvironmentAware {

    private final static char Q = '?';
    @Autowired
    private DataSourceProperties master;
    @Autowired
    private ShardingDataSourceProperties.Slavers slavers;
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
        MasterSlaversShard globe = new MasterSlaversShard(master.determineUrl());
        ShardingContext.setGlobe(globe);
        buildAndPut(master, targetDataSources);
        setDefaultTargetDataSource(targetDataSources.get(master.determineUrl()));
        if (slavers != null) {
            for (DataSourceProperties slaver : slavers) {
                globe.addSalver(slaver.determineUrl());
                buildAndPut(slaver, targetDataSources);
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
            MasterSlaversShard shard = new MasterSlaversShard(e.getMaster().determineUrl());
            if (e.getSlavers() != null) {
                for (DataSourceProperties salver : e.getSlavers()) {
                    shard.addSalver(salver.determineUrl());
                    buildAndPut(salver, targetDataSources);
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
