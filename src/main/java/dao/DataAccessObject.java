package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DataAccessObject {

    private static final DBConnector DBC = new DBConnector();

    public Window getWindow(int width, int height, String type){
        Window window = null;
        String query = "SELECT `price` FROM `glass` WHERE `type`= ?";

        try {
            PreparedStatement preparedStatement = DBC.getConnection().prepareStatement(query);
            preparedStatement.setString(1,type);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int price = rs.getInt("price");
                window = new Window(width, height, price);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return window;
    }


    public Frame getFrame(String type, Window window){
        Frame frame = null;
        String query = "SELECT `price` FROM `frames` WHERE `type`= ?";

        try {
            PreparedStatement preparedStatement = DBC.getConnection().prepareStatement(query);
            preparedStatement.setString(1,type);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int price = rs.getInt("price");
                frame = new Frame(window, price);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return frame;
    }
}
