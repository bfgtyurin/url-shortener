package com.vtyurin.app.config.db;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class PostgresqlConfigTest {

    @Test
    public void shouldReturnSystemVariableTest() {
        assertNotNull(System.getenv("DATABASE_URL"));
    }

}