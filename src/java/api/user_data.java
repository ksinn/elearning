/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;


import Entety.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ksinn
 */
public class user_data extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            /*User user = new User();
            user.getById(Integer.parseInt(request.getParameter("id")));
            

                    
                    
            PrintWriter out = response.getWriter();
            out.write(res);*/
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
        

    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
           /* User user = new User();
            user.getById(Integer.parseInt(request.getParameter("id")));
            UserJWT wt = new UserJWT();
            String res = wt.getJson(user, AppInf.main);
            PrintWriter out = response.getWriter();
            out.write(res);*/
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
