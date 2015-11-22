package com.vtyurin.app.controller;

import com.vtyurin.app.config.ApplicationContext;
import com.vtyurin.app.config.Profiles;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationContext.class})
@WebAppConfiguration
@ActiveProfiles(Profiles.APPLICATION)
public class LinkControllerTest {

    @Autowired
    LinkController linkController;

    @Test
    public void ShortUrlInvalidTest() {
        String shortURL = "12345aS";
        assertTrue(linkController.shortUrlInvalid(shortURL));
    }

    @Test
    public void sendLinkJsonResponseTest() {

    }

    @Test
    public void createNewLinkObjectWithUrlTest() {
    }
}
