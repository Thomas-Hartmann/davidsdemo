package dao;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataAccessObjectTest {

    private DataAccessObject dao;

    @Before
    public void setUp() {
        dao = new DataAccessObject();
    }

    @Test
    public void testGetWindow() {
        Window actual = dao.getWindow(100,100,"default");
        assertNotEquals(null, actual);
    }

    @Test
    public void testGetFrame() {
        Window window = dao.getWindow(100,100,"default");
        Frame actual = dao.getFrame("type1", window);
        assertNotEquals(null, actual);
    }
}