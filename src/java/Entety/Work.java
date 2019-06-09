/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entety;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 *
 * @author ksinn
 */
public class Work extends Parent {

    private Date createDate;
    private Study study;
    private int studyId;
    private UUID WORK_KEY;
    private Task task;
    private int taskId;
    private int mark;

    public Work() {
        this.study = new Study();
        this.task = new Task();

    }

    @Override
    public String _getType() {
        return "work";
    }

    public void getById(int id) throws Exception {
        if (id > 0) {
            this.ID = id;
            this._select();
        } else {
            throw new Exception("Invalid input data for AcceptTask whith id=" + id);
        }
    }

    public boolean getByKey() throws Exception {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("work_key", this.WORK_KEY.toString());

        ArrayList<HashMap<String, Object>> Params = this.getObjectsParam(param);
        if(Params.isEmpty())
            return false;
        for (int i = 0; i < 1; i++) {
            this.getFromParam(Params.get(i));
        }
        return true;

    }

    public boolean Write() throws Exception {
        this.WORK_KEY = UUID.randomUUID();
        this.mark = -1;
        this.ReadTaskFromDB();
        this.ReadTeachingFromDB();
        return this._insert();
    }

    public Study getStudy() {
        return this.study;
    }

    public boolean isCompleated() {
        return mark != -1;
    }

    public int getMark() {
        return this.mark;
    }

    public Task getTask() {
        return this.task;
    }

    public String getWorkKey() {
        return WORK_KEY.toString();
    }

    public int getUser() {
        return this.study.getUser().getId();
    }

    public long getCreateTime() {
        return createDate.getTime();
    }

    public Date getTime() {
        return createDate;
    }

    public int getListId() {
        return task.getListId();
    }

    public int getCount() {
        return task.getCount();
    }

    public void setWorkKey(String key) {
        this._from_db = false;
        WORK_KEY = UUID.fromString(key);
    }

    public boolean putMark(int mark) throws Exception {
        this.mark = mark;
        return this._update();
    }

    public void setStudyId(int data) {
        this.studyId = data;
    }

    public void setTaskId(int data) {
        this.taskId = data;
    }

    @Override
    protected HashMap<String, Object> _getParams() {

        HashMap<String, Object> list = new HashMap<String, Object>();
        list.put("completed", this.mark);
        list.put("task", this.task.getId());
        list.put("study", this.study.getId());
        list.put("work_key", this.WORK_KEY.toString());
        return list;
    }

    @Override
    protected void _setParams(HashMap<String, Object> Params) throws Exception {

        this.createDate = (Date) Params.get("adddate");
        this.WORK_KEY = UUID.fromString((String) Params.get("work_key"));
        this.studyId = (int) Params.get("study");
        this.taskId = (int) Params.get("task");
        this.mark = (int) Params.get("completed");
        this.ReadTaskFromDB();
        this.ReadTeachingFromDB();

    }

    public void ReadTaskFromDB() throws Exception {
        this.task.getById(this.taskId);
    }

    public void ReadTeachingFromDB() throws Exception {
        this.study.getById(this.studyId);
    }

    @Override
    protected boolean _isCorrect() {
        return true;
    }

    @Override
    public boolean MayChange() {
        return false;
    }

    public boolean isAlive() {
        return (new Timestamp(System.currentTimeMillis())).before(task.getEnd());
    }

}
