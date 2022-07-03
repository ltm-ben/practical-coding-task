package com.example.demo.utils;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MapWrapperImplTest {
    @Test
    void givenKey_whenGettingValue_thenReturnValueOrThrowExceptionIfNotFound() {
        Map<String, String> testMap = new HashMap<>() {
            {
                put("1", "Value for key 1");
                put("2", "Value for key 2");
            }
        };
        MapWrapper mapWrapper = new MapWrapperImpl(testMap);
        assertEquals("Value for key 1", mapWrapper.getValueUsingKey("1"));
        assertThrows(RuntimeException.class, () -> {
            mapWrapper.getValueUsingKey("3");
        });
    }
}