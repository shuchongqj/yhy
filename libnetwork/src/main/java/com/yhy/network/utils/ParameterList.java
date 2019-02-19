package com.yhy.network.utils;

import java.util.HashMap;
import java.util.Set;

public class ParameterList {
    private HashMap<String, String> params;

    public ParameterList() {
        this.params = new HashMap<>();
    }

    public ParameterList(int initialCapacity) {
        this.params = new HashMap<>(initialCapacity);
    }

    public final void put(String name, String value) {
        if (name != null && name.length() != 0 && value != null) {
            this.params.put(name, value);
        }
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public final Set<String> keySet() {
        return this.params.keySet();
    }

    public final String get(String key) {
        return this.params.get(key);
    }

    public final boolean containsKey(String key) {
        return this.params.containsKey(key);
    }

    public final int size() {
        return this.params.size();
    }
}

