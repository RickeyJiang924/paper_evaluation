package com.nju.topics.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MapValueComparator implements Comparator<String> {
    Map<String, Integer> base;

    public MapValueComparator(Map<String, Integer> base) {
        this.base = base;
    }

    public int compare(String a, String b) {
        try {
            if (base.get(a).intValue() >= base.get(b).intValue()) {
                return -1;
            } else {
                return 1;
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            return -1;
        }

    }

    public static <K, V extends Comparable<V>> Map<K, V> sortByValues(
            final Map<K, V> map) {
        Comparator<K> valueComparator = new Comparator<K>() {
            public int compare(K k1, K k2) {
                int compare = map.get(k2).compareTo(map.get(k1));
                if (compare == 0)
                    return 1;
                else
                    return compare;
            }
        };
        Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }
}

