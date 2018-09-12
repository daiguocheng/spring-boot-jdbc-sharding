package org.daigc.sharding;

import java.util.*;

abstract class ShardingContext {

    private final static ThreadLocal<String> CURRENT = ThreadLocal.withInitial(() -> null);
    private final static SortedMap<Integer, MasterSlavesShard> SHARDS = new TreeMap<>();
    private static MasterSlavesShard GLOBE;


    static String getCurrent() {
        return CURRENT.get() == null ? GLOBE.getMaster() : CURRENT.get();
    }

    static Set<MasterSlavesShard> getShards() {
        return new HashSet<>(SHARDS.values());
    }

    static void bind(MasterSlavesShard shard, boolean writing) {
        if (writing) {
            CURRENT.set(shard.getMaster());
            return;
        }
        CURRENT.set(shard.getNextReadonly());
    }

    static void unbind() {
        CURRENT.set(null);
    }

    static void add(MasterSlavesShard shard) {
        for (int i = 0; i < Byte.MAX_VALUE; i++) {
            SHARDS.put(hash(shard.toString(i)), shard);
        }
    }

    static void setGlobe(MasterSlavesShard shard) {
        GLOBE = shard;
    }

    static void bind(Object key, boolean writing) {
        if (SHARDS.isEmpty()) {
            throw new RuntimeException("Not any data sources for sharding!");
        }
        SortedMap<Integer, MasterSlavesShard> tail = SHARDS.tailMap(hash(key));
        MasterSlavesShard shard = tail.isEmpty() ? SHARDS.get(SHARDS.firstKey()) : tail.get(tail.firstKey());
        bind(shard, writing);
    }

    static void bind(boolean writing) {
        bind(GLOBE, writing);
    }

    private static int hash(Object k) {
        String key = Objects.toString(k);
        return MurmurHash3.murmurhash3_x86_32(key, 0, key.length(), Integer.MAX_VALUE);
    }

}
