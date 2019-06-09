/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entety;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import javax.naming.NamingException;

/**
 *
 * @author ksinn
 */
public class Course extends Parent {

    public static int PAUSED = 2;
    public static int STOPED = 0;
    public static int PLAYED = 1;

    //дата начала курса
    private Date startDate;
    private String name;
    private String decsription;
    private int duration;
    private User user;
    private int userId;
    //флаг открытого курса, если истина, то на курс студенты могут записываться самостоятельно
    private boolean open;
    //флаг оконченности курса, если истина, то курс не ведеться
    private int status;

    @Override
    public String _getType() {
        return "course";
    }

    public Course() {
        this.user = new User();
        this.status = STOPED;
    }

    public void getById(int id) throws Exception {
        if (id > 0) {
            this.ID = id;
            this._select();
        } else {
            throw new Exception("Invalid input data for course whith id=" + id);
        }
    }

    public boolean Write() throws Exception {
        return this._insert();
    }

    public boolean Update() throws Exception {
        return this._update();
    }

    public boolean Create() throws NamingException, SQLException {
        return this._insert();
    }

    public User getUser() {
        return this.user;
    }

    public int getDuration() {
        return this.duration;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.decsription;
    }

    public String getShortDescription() {
        if (this.decsription.length() < 50) {
            return this.decsription;
        } else {
            return this.decsription.substring(0, 50);
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    /*public boolean isClosed() {
        return status == 0;
    }*/

    public boolean isOpened() {
        return this.open;
    }

    @Override
    protected boolean _isCorrect() {
        return true;
    }

    public void ReadUserFromDB() throws Exception {
        this.user.getById(this.userId);
    }

    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
    }

    public void setDiscription(String description) {
        if (description != null && !description.isEmpty()) {
            this.decsription = description;
        }
    }

    public void setDuration(int duration) {
        if (duration > 0 && duration < 367) {
            this.duration = duration;
        }
    }

    public void setOpen(boolean data) {
        this.open = data;
    }

    public void setStatus(int status) {
        if (status < 3) {
            this.status = status;
        }
    }

    public void setStartDate(Date start_date) {
        if (start_date != null) {
            this.startDate = start_date;
        }
    }

    public void setUser(User user) {
        if (user != null) {
            this.user = user;
            userId = user.ID;
        }
    }

    @Override
    public boolean MayChange() {
        return true;
    }

    @Override
    protected HashMap<String, Object> _getParams() {
        HashMap<String, Object> list = new HashMap<String, Object>();
        list.put("start_date", this.startDate);
        list.put("status", this.status);
        list.put("open", this.open ? 1 : 0);
        list.put("name", this.name);
        list.put("description", this.decsription);
        list.put("duration", this.duration);
        list.put("users", this.userId);
        return list;
    }

    @Override
    protected void _setParams(HashMap<String, Object> Params) throws Exception {
        this.startDate = (Date) Params.get("start_date");
        this.status = (int) Params.get("status");
        this.open = (int) Params.get("open") == 1;
        this.name = (String) Params.get("name");
        this.decsription = (String) Params.get("description");
        this.duration = (int) Params.get("duration");
        this.userId = (int) Params.get("users");
        ReadUserFromDB();

    }

    public boolean isActive() {
        return this.status == PLAYED;
    }
}
