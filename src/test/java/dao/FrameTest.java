package dao;

import org.junit.Test;

import static org.junit.Assert.*;

public class FrameTest {

    @Test
    public void getFramePrice() throws Exception {
        Window window = new Window(160,100,300);
        Frame frame = new Frame(window,100);

        double expected = 520.0;
        double actual = frame.getFramePrice();

        assertEquals(expected,actual,0);
    }

}