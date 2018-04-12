package dao;

public class Frame {

    private Window window;
    private double price;

    public Frame(Window window, int price) {
        this.window = window;
        this.price = price;
    }

    public double getFramePrice() {
        double circumference = (window.getWidth() * 2 + window.getHeight() * 2) / 100;
        return circumference * price;
    }
}
