/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Course;

import Controller.HttpServletParent;
import Entety.Course;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ksinn
 */
public class Add extends HttpServletParent {

    @Override
    protected void doMyPost(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Course course = new Course();

        String name = request.getParameter("name");
        String discription = request.getParameter("discription");
        int duration = Integer.parseInt(request.getParameter("duration"));
        boolean opened = request.getParameter("opened") != null;
        long startdate = Long.parseLong(request.getParameter("start_date"));
        //Part img = request.getPart("picture");

        course.setName(name);
        course.setDiscription(discription);
        course.setDuration(duration);
        course.setUser(user);

        course.setOpen(opened);
        course.setStartDate(new Date(startdate));
        course.setStatus(0);

        if (course.Write()) {
            //program.SaveIco(img);
            response.sendRedirect("render?id=" + course.getId());
            return;
        } else {
            request.setAttribute("course", course);
            request.getRequestDispatcher("course_form.jsp").forward(request, response);
            return;
        }
    }

    @Override
    protected int PrivateMod() {
        return HttpServletParent.OnlyForAuthorized;
    }

    @Override
    protected void doMyGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("course_form.jsp").forward(request, response);
    }

}
