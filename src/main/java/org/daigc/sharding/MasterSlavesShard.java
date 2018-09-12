package org.daigc.sharding;

import lombok.Getter;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Getter
class MasterSlavesShard {

    private String master;
    private Set<String> slaves;

    MasterSlavesShard(String master) {
        this.master = master;
    }

    void addSalve(String slave) {
        if (master.equals(slave) || master.startsWith(slave)) {
            return;
        }
        if (slaves == null) {
            slaves = new HashSet<>();
        }
        slaves.add(slave);
    }

    String getNextReadonly() {
        if (slaves == null || slaves.isEmpty()) {
            return master;
        }
        int weight = 2, j = 0;
        String[] urls = new String[weight * slaves.size() + 1];
        for (int i = 0; i < weight; i++) {
            for (String slaver : slaves) {
                urls[j++] = slaver;
            }
        }
        urls[j] = master;
        return urls[new Random().nextInt(urls.length)];
    }

    String toString(int i) {
        if (slaves == null || slaves.isEmpty()) {
            return String.format("%s-%d", master, i);
        }
        return String.format("%s-%d-%s", master, i, slaves);
    }

}
