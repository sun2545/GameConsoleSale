package sunhye;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sunhye Kwon
 */
@WebServlet(urlPatterns = {"/ConsoleSales"})
public class ConsoleSales extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //get category parameter
        String category = request.getParameter("category");
        
        //get JSON string
        ConsoleSalesDatabase db = new ConsoleSalesDatabase();
              
        response.setContentType("application/json"); //MIME
        PrintWriter out = response.getWriter();
       
        //send out JSON string
        out.println(db.getConsoleSales(category));

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
        doGet(request, response);
    }

   
}
