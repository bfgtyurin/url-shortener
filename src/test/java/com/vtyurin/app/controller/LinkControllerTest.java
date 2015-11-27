package com.vtyurin.app.controller;

import com.vtyurin.app.config.ApplicationContext;
import com.vtyurin.app.config.Profiles;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnitParamsRunner.class)
@ContextConfiguration(classes = {ApplicationContext.class})
@WebAppConfiguration
@ActiveProfiles(Profiles.INTEGRATION_TEST)
public class LinkControllerTest {

    private TestContextManager testContextManager;

    @Autowired
    LinkController linkController;

    private static final Object[][] getInvalidShortUrlString() {
        return new String[][]{{""}, {"asdfghz"}, {"1234567:123456"}};
    }

    @Before
    public void setUp() throws Exception {
        testContextManager = new TestContextManager(getClass());
        testContextManager.prepareTestInstance(this);
    }

    @Test
    public void ShortUrlInvalidTest() {
        String shortURL = "12345aS";
        assertFalse(linkController.shortUrlInvalid(shortURL));
    }

    @Test
    @Parameters(method = "getInvalidShortUrlString")
    public void tryCreateLinkListWithShortUrlsShouldReturnEmptyList(String invalidShortUrlString) {
        List list = linkController.tryCreateLinkListWithShortUrls(invalidShortUrlString);
        assertTrue(list.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    @Parameters(method = "getInvalidShortUrlString")
    public void createLinkListWithShortUrlsShouldIAXForInvalidString(String invalidShortUrlString) {
        linkController.createLinkListWithShortUrls(invalidShortUrlString);
    }

    @Test
    public void createLinkListWithShortUrls() {
        List list = linkController.createLinkListWithShortUrls("zxcvb12/qwert34/asdfg56");
        assertEquals(3, list.size());
    }

    @Test
    public void sendLinkJsonResponseTest() {

    }

    @Test
    public void createNewLinkObjectWithUrlTest() {
    }
}
