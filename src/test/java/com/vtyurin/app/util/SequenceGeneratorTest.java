package com.vtyurin.app.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class SequenceGeneratorTest {

    @Test
    public void testGenerate() throws Exception {
        String generatedStirng = SequenceGenerator.generate();
        assertEquals(7, generatedStirng.length());
    }
}