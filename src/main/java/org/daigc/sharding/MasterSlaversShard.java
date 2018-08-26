package org.daigc.sharding;

import lombok.Getter;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Getter
class MasterSlaversShard {

    private String master;
    private Set<String> slavers;

    MasterSlaversShard(String master) {
        this.master = master;
    }

    void addSalver(String slaver) {
        if (master.equals(slaver) || master.startsWith(slaver)) {
            return;
        }
        if (slavers == null) {
            slavers = new HashSet<>();
        }
        slavers.add(slaver);
    }

    String getNextReadonly() {
        if (slavers == null || slavers.isEmpty()) {
            return master;
        }
        int weight = 2, j = 0;
        String[] urls = new String[weight * slavers.size() + 1];
        for (int i = 0; i < weight; i++) {
            for (String slaver : slavers) {
                urls[j++] = slaver;
            }
        }
        urls[j] = master;
        return urls[new Random().nextInt(urls.length)];
    }

    String toString(int i) {
        if (slavers == null || slavers.isEmpty()) {
            return String.format("%s-%d", master, i);
        }
        return String.format("%s-%d-%s", master, i, slavers);
    }

}
