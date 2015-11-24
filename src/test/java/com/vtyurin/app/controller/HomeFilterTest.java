package com.vtyurin.app.controller;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnitParamsRunner.class)
public class HomeFilterTest {

    private Object[] getValidShorUrls() {
        return new String[][]{{"1234567"}, {"aaaaaaa"}, {"BBBBBBB"}, {"aaBB123"}};
    }

    private Object[] getInvalidShorUrls() {
        return new String[][]{{"!@#$%^&!"}, {"!@#$%^&!"}, {"*()_+{}"}, {"123456"}, {"12345678"}, {"shorten"}};
    }

    @Test
    @Parameters(method = "getValidShorUrls")
    public void testIsValidUrlShouldReturnTrue(String validShortUrl) throws Exception {
        HomeFilter homeFilter = new HomeFilter();
        assertTrue(homeFilter.isValidUrl(validShortUrl));

    }

    @Test
    @Parameters(method = "getInvalidShorUrls")
    public void testIsValidUrlShouldReturnFalse(String invalidShortUrl) throws Exception {
        HomeFilter homeFilter = new HomeFilter();
        assertFalse(homeFilter.isValidUrl(invalidShortUrl));

    }
}