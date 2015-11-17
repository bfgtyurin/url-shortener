package com.vtyurin.app.util;

import org.apache.commons.lang3.RandomStringUtils;

public class SequenceGenerator {
    public static String generate() {
        return RandomStringUtils.random(7, true, true);
    }
}
