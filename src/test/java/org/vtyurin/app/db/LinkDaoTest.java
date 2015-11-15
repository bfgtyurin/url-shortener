package org.vtyurin.app.db;

import com.vtyurin.app.dao.LinkDao;
import com.vtyurin.app.model.Link;
import com.vtyurin.app.util.ConnectionUtil;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class LinkDaoTest {

    LinkDao linkDao;

    @Before
    public void setUp() throws SQLException {
        linkDao = new LinkDao(ConnectionUtil.getTestConnection());
    }

    @Test
    public void persistTest() {
        // TODO
        Link link = new Link("testFull", "testShort", 0);
        linkDao.persist(link);
    }
}
