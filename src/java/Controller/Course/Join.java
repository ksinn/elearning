package Controller.Course;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Controller.HttpServletParent;
import Entety.Course;
import Entety.Study;
import Entety.User;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ksinn
 */
public class Join extends HttpServletParent {

    @Override
    protected void doMyPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doMyGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        try {
            int id = 0, studentId = 0;
            id = Integer.parseInt(request.getParameter("id"));
            if (request.getParameter("studentId") != null) {
                studentId = Integer.parseInt(request.getParameter("studentId"));
            }

            Course course = new Course();
            course.getById(id);
            Study s = null;
            if (studentId != 0) {
                if (user.getId() == course.getUser().getId()) {
                    User student = new User();
                    student.getById(studentId);
                    s = courseService.joinToCourse(course, student);
                }
            } else {
                s = courseService.joinToCourse(course, user);
            }

            if (s != null) {
                out.write("{'result':'ok'}");
            } else {
                out.write("{'result':'no'}");
            }
        } catch (Exception ex) {
            out.write("{'result':'error'}");
        }
        out.flush();
        out.close();

    }

    @Override
    protected int PrivateMod() {
        return HttpServletParent.OnlyForAuthorized;
    }

}
