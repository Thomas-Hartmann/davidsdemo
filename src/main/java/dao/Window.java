package dao;

public class Window {

    private double width;
    private double height;
    private double price;

    public Window(double width, double height, double price) {
        this.width = width;
        this.height = height;
        this.price = price;
    }

    public double getWindowPrice() {
        double width = this.width / 100;
        double height = this.height / 100;
        double area = width * height;
        return area * price;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
