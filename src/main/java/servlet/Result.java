package servlet;

import dao.DataAccessObject;
import dao.Frame;
import dao.Window;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "result", urlPatterns = {"/calculate"}, loadOnStartup = 1)
public class Result extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DataAccessObject dao = new DataAccessObject();

        String windowName = "default"; // if you want to expand, change default!
        int windowWidth = Integer.valueOf(request.getParameter("window-width"));
        int windowHeight = Integer.valueOf(request.getParameter("window-height"));
        Window window = dao.getWindow(windowWidth, windowHeight, windowName);
        Frame frame = null;

        String frameName = request.getParameter("frame-type");
        if (frameName != null) {
            frame = dao.getFrame(frameName, window);
        }

        String total;
        if (frame != null && window != null){
            total = window.getWindowPrice() + frame.getFramePrice() + " DKK";
        } else {
            total = "Error in calculations!";
        }
        request.setAttribute("total", total);
        request.getRequestDispatcher("response.jsp").forward(request, response);
    }
}