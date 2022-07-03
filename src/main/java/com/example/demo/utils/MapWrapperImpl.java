package com.example.demo.utils;

import java.util.Map;

public class MapWrapperImpl implements MapWrapper {

    private Map<String, String> map;

    public MapWrapperImpl(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public String getValueUsingKey(String key) {
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            throw new RuntimeException("Could not find a mapping for key: " + key);
        }
    }
}
