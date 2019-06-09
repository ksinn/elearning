package Entety;


import java.util.HashMap;


/**
 *
 * @author javlonboy
 */
public class Study extends Parent {
    
    private User User;
    private Course Course;
    private boolean Completed;
    private int CourseId;
    private int UserId;

    @Override
    public String _getType(){
        return "study";
    }
    
    public Study(){
        this.User = new User();
        this.Course = new Course();
    }
    
    public void getById(int id) throws Exception{
        if(id>0){
            this.ID = id;
            if(this._select()){
                this.ReadUserFromDB();
                this.ReadCourseFromDB();
            } else {
                throw new Exception("Invalid input data for teaching with id="+id);
            }
        } else 
            throw new Exception("Invalid input data for teaching with id="+id);
    }
    
    public Course getCourse(){
        return Course;
    }
    
    public User getUser(){
        return User;
    }

    public void setUser(int data) throws Exception {
        this._from_db = false;
        this.UserId = data;
    }

    public void setCourse(int data) throws Exception {
        this._from_db = false;
        this.CourseId = data;
    }

    /*public void setID(int ID) {
        this.ID = ID;
    }*/
    
    public boolean Write() throws Exception
    {
        return this._insert();
    }

    @Override
    public boolean MayChange() {
        return false;
    }

    @Override
    protected HashMap<String, Object> _getParams() {
        HashMap<String, Object> list = new HashMap<String, Object>();
        list.put("users", this.UserId);
        list.put("course", this.CourseId);
        list.put("completed", this.Completed?1:0);
        return list;
    }

    @Override
    protected void _setParams(HashMap<String, Object> Params) throws Exception {
        this.UserId = (int) Params.get("users");
        this.CourseId = (int) Params.get("course");
        this.Completed = (int) Params.get("completed")== 1;
    }

    @Override
    protected boolean _isCorrect() {
        return true;
    }

    public void ReadUserFromDB() throws Exception {
        this.User.getById(this.UserId);
    }

    public void ReadCourseFromDB() throws Exception {
        this.Course.getById(this.CourseId);
    }
    
}
