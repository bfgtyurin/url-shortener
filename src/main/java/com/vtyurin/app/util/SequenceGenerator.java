package com.vtyurin.app.util;

import org.apache.commons.lang3.RandomStringUtils;

public class SequenceGenerator {

    public static final int SEQUENCE_LENGTH = 7;

    public static String generate() {
        return RandomStringUtils.random(SEQUENCE_LENGTH, true, true);
    }
}
