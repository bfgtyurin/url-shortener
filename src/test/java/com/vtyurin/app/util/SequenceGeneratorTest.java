package com.vtyurin.app.util;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SequenceGeneratorTest {

    @Test
    public void testGenerate() throws Exception {
        String generatedStirng = SequenceGenerator.generate();
        assertEquals(7, generatedStirng.length());
    }

    @Test
    public void check() {
        Set<String> set = new HashSet<>();
        int total = 1_000_000;
        for (int i = 0; i < total; i++) {
            set.add(SequenceGenerator.generate());
        }

        assertEquals(total, set.size());
    }

}