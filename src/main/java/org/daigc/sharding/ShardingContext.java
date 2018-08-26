package org.daigc.sharding;

import java.util.*;

abstract class ShardingContext {

    private final static ThreadLocal<String> CURRENT = ThreadLocal.withInitial(() -> null);
    private final static SortedMap<Integer, MasterSlaversShard> SHARDS = new TreeMap<>();
    private static MasterSlaversShard GLOBE;


    static String getCurrent() {
        return CURRENT.get() == null ? GLOBE.getMaster() : CURRENT.get();
    }

    static Set<MasterSlaversShard> getShards() {
        return new HashSet<>(SHARDS.values());
    }

    static void bind(MasterSlaversShard shard, boolean writing) {
        if (writing) {
            CURRENT.set(shard.getMaster());
            return;
        }
        CURRENT.set(shard.getNextReadonly());
    }

    static void unbind() {
        CURRENT.set(null);
    }

    static void add(MasterSlaversShard shard) {
        for (int i = 0; i < Byte.MAX_VALUE; i++) {
            SHARDS.put(hash(shard.toString(i)), shard);
        }
    }

    static void setGlobe(MasterSlaversShard shard) {
        GLOBE = shard;
    }

    static void bind(Object key, boolean writing) {
        if (SHARDS.isEmpty()) {
            throw new RuntimeException("Not any data sources for sharding!");
        }
        SortedMap<Integer, MasterSlaversShard> tail = SHARDS.tailMap(hash(key));
        MasterSlaversShard shard = tail.isEmpty() ? SHARDS.get(SHARDS.firstKey()) : tail.get(tail.firstKey());
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
