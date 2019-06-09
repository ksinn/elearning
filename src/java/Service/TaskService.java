/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Entety.Study;
import Entety.Task;
import Entety.Work;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import javax.naming.NamingException;

/**
 *
 * @author ksinn
 */
public class TaskService {

    private boolean timeIsCome(Task task) {

        Timestamp now = new Timestamp(System.currentTimeMillis());
        return now.after(task.getStart()) && now.before(task.getEnd());
    }

    private boolean rightCourse(Study study, Task task) {

        return study.getCourse().getId() == task.getCourse().getId()
                && task.getCourse().isActive();
    }

    private Work getWork(Study study, Task task) throws NamingException, SQLException, Exception {

        Work work = new Work();
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("study", study.getId());
        param.put("task", task.getId());

        ArrayList<HashMap<String, Object>> Params = work.getObjectsParam(param);
        if (Params.isEmpty()) {
            return null;
        }
        for (int i = 0; i < 1; i++) {
            work.getFromParam(Params.get(i));
        }
        return work;

        /*Work w = null;
        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            PreparedStatement stmt = conn.prepareStatement("select * from work where study = ? and task = ? and date(now()) = date(addDate)");
            stmt.setInt(1, study.getId());
            stmt.setInt(2, task.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                HashMap<String, Object> Params = new HashMap<String, Object>();
                for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
                    Params.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
                }
                w = new Work();
                w.getFromParam(Params);
                w.ReadTaskFromDB();
                w.ReadTeachingFromDB();
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return w;*/
    }

    public Work start(Study study, Task task) throws SQLException, Exception {
        if (rightCourse(study, task) && timeIsCome(task)) {
            Work work = createWork(study, task);
            if (work.isCompleated()) {
                return null;
            } else {
                return work;
            }
        } else {
            return null;
        }

    }

    private Work createWork(Study study, Task task) throws Exception {
        Work work = getWork(study, task);
        if (work == null) {
            work = new Work();
            work.setStudyId(study.getId());
            work.setTaskId(task.getId());
            if (!work.Write()) {
                return null;
            }
        }
        return work;
    }

}
