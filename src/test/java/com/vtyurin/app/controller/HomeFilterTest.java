package com.vtyurin.app.controller;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HomeFilterTest {

    @Test
    public void testIsValidUrl() throws Exception {
        HomeFilter homeFilter = new HomeFilter();

        assertTrue(homeFilter.isValidUrl("1234567"));
        assertTrue(homeFilter.isValidUrl("aaaaaaa"));
        assertTrue(homeFilter.isValidUrl("BBBBBBB"));
        assertTrue(homeFilter.isValidUrl("aaBB123"));

        assertFalse(homeFilter.isValidUrl("!@#$%^&!"));
        assertFalse(homeFilter.isValidUrl("*()_+{}"));
        assertFalse(homeFilter.isValidUrl("123456"));
        assertFalse(homeFilter.isValidUrl("12345678"));

        assertFalse(homeFilter.isValidUrl("shorten"));
    }
}