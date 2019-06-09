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
public class Edit extends HttpServletParent {

    @Override
    protected void doMyPost(HttpServletRequest request, HttpServletResponse response) throws Exception {

        int id = Integer.parseInt(request.getParameter("id"));
        Course course = new Course();
        course.getById(id);

        if (course.getUser().getId() == user.getId()) {
            String name = request.getParameter("name");
            String discription = request.getParameter("discription");
            int duration = Integer.parseInt(request.getParameter("duration"));
            long startdate = Long.parseLong(request.getParameter("start_date"));
            boolean opened = request.getParameter("opened")!=null;

            //Part img = request.getPart("picture");
            course.setName(name);
            course.setOpen(opened);
            course.setDiscription(discription);
            course.setDuration(duration);
            //course.setStartDate(new Date(startdate));

            if (course.Update()) {
                //program.SaveIco(img);
                response.sendRedirect("render?id=" + course.getId());
                return;
            } else {
                request.setAttribute("course", course);
                request.getRequestDispatcher("course_form.jsp").forward(request, response);
                return;
            }
        } else {
            request.setAttribute("message", "You cannot edit this component!");
            request.getRequestDispatcher("/message.jsp").forward(request, response);
        }

    }

    @Override
    protected int PrivateMod() {
        return HttpServletParent.OnlyForAuthorized;
    }

    @Override
    protected void doMyGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        Course course = new Course();
        course.getById(id);
        if (course.getUser().getId() == user.getId()) {
            request.setAttribute("course", course);
            request.getRequestDispatcher("course_form.jsp").forward(request, response);
        } else {
            request.setAttribute("message", "You cannot edit this component!");
            request.getRequestDispatcher("/message.jsp").forward(request, response);
        }
    }

}
