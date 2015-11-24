package com.vtyurin.app.model;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class LinkTest {

    public static final Object[] getInvalidClicks() {
        return new Integer[][]{{-42}, {-1000}, {-1}};
    }

    @Test
    public void constructorShouldBeSetFullUrl() {
        Link link = new Link("https://google.com");
        assertEquals("https://google.com", link.getFullURL());
    }

    @Test
    public void constructorShouldBeSetFullUrlShorUrlClicks() {
        Link link = new Link("https://google.com", "123ZXCv", 42);
        assertEquals("https://google.com", link.getFullURL());
        assertEquals("123ZXCv", link.getShortURL());
        assertEquals(42, link.getClicks());
    }

    @Test(expected = IllegalArgumentException.class)
    @Parameters(method = "getInvalidClicks")
    public void constructorShouldThrowIAEForInvalidClicks(int invalidClicks) {
        new Link("https://google.com", "123ZXCv", invalidClicks);
    }

}