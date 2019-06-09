/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Entety.Course;
import Entety.Study;
import Entety.User;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author ksinn
 */
public class CourseService {

    public CourseService() {

    }

    public boolean canStart(User user, Course course) throws Exception {
        return course.isOpened()
                && course.isActive()
                && course.getUser().getId() != user.getId() //&&!user.haveCourse(this)
                ;
    }

    public Study joinToCourse(Course course, User user) throws Exception {

        Study s = null;
        if (canStart(user, course)) {
            s = new Study();
            s.setCourse(course.getId());
            s.setUser(user.getId());
            if (!s.Write()) {
                s = null;
            }
        }
        return s;
    }

    public Study getStudy(Course course, User user) throws Exception {
        /*Study s = null;
        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            PreparedStatement stmt = conn.prepareStatement("select * from (users join study on users = users.id) join course on course = course.id where course.id=? and users.id=? and completed = 0 and closed = 0");
            stmt.setInt(1, course.getId());
            stmt.setInt(2, user.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                HashMap<String, Object> Params = new HashMap<String, Object>();
                for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
                    Params.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
                }
                s = new Study();
                s.getFromParam(Params);
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return s;
         */
        Study s = new Study();
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("course", course.getId());
        param.put("user", user.getId());
        ArrayList<HashMap<String, Object>> Params;
        Params = s.getObjectsParam(param);
        if (Params.isEmpty()) {
            return null;
        }
        for (int i = 0; i < 1; i++) {
            s.getFromParam(Params.get(i));
        }
        return s;
    }

}
