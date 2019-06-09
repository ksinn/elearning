/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entety;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 *
 * @author ksinn
 */
public class Task extends Component {

    private int serviceId;
    private int groupId;
    private int count;
    private Service service;
    private Timestamp startDate;
    private Timestamp endDate;

    public Task() {
        service = new Service();
    }

    @Override
    public String _getType() {
        return "task";
    }

    @Override
    public void getById(int id) throws Exception {
        super.getById(id);
    }

    public int getServiceId() {
        return this.serviceId;
    }

    public int getCount() {
        return this.count;
    }

    public int getListId() {
        return groupId;
    }

    public Service getService() {
        return service;
    }

    public Timestamp getStart() {
        return this.startDate;
    }

    public Timestamp getEnd() {
        return this.endDate;
    }

    public Date getStartDate() {
        return (new Date(this.startDate.getTime()));
    }

    public Time getStartTime() {
        return (new Time(this.startDate.getTime()));
    }

    public Date getEndDate() {
        return (new Date(this.endDate.getTime()));
    }

    public Time getEndTime() {
        return (new Time(this.endDate.getTime()));
    }

    public void setTimeRange(Timestamp start, Timestamp end) {
        if (start != null && start.after(course.getStartDate())
                && end != null && end.after(start)) {
            this.startDate = start;
            this.endDate = end;
        }
    }

    public void setService(Service serv) {
        if (serv != null) {
            this.service = serv;
            this.serviceId = serv.getId();
        }
    }
    
    public void setServiceId(int period) {
        this.serviceId = period;
    }

    public void setCount(int count) {
        if (count >= 0) {
            this.count = count;
        }
    }

    public void setListId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    protected HashMap<String, Object> _getParams() {
        HashMap<String, Object> list = new HashMap<String, Object>();
        list.put("group_id", this.groupId);
        list.put("name", this.name);
        list.put("course", this.courseId);
        list.put("total_count", this.count);
        list.put("time", endDate.getTime() - startDate.getTime());
        list.put("start_time", this.startDate);
        list.put("service", this.serviceId);

        return list;
    }

    @Override
    protected void _setParams(HashMap<String, Object> Params) throws Exception {
        this.name = (String) Params.get("name");
        this.groupId = (int) Params.get("group_id");
        this.courseId = (int) Params.get("course");
        this.count = (int) Params.get("total_count");
        this.startDate = (Timestamp) Params.get("start_time");
        this.endDate = new Timestamp((long) Params.get("time") + startDate.getTime());
        this.serviceId = (int) Params.get("service");
        this.ReadCourseFromDB();
        this.readServiceFromDB();
    }

    @Override
    protected boolean _isCorrect() {
        return true;
    }

    private void readServiceFromDB() throws Exception {
        this.service.getById(this.serviceId);
    }

}
