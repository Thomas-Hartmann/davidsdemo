package dao;

import org.junit.Test;

import static org.junit.Assert.*;

public class WindowTest {
    @Test
    public void getWindowPrice() throws Exception {
        Window window = new Window(160,100,300);

        double expected = 480.0;
        double actual = window.getWindowPrice();

        assertEquals(expected,actual,0);
    }
}