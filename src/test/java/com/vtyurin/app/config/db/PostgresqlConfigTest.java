package com.vtyurin.app.config.db;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class PostgresqlConfigTest {

    @Test
    public void shouldReturnSystemVasiableTest() {
        assertNotNull(System.getenv("DATABASE_URL"));
    }

}