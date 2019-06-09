/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

/**
 *
 * @author ksinn
 */
public class MessageService {
    private String message;
    {
        message = "";
    }
    
    public void putMessage(String s){
        message += " " + s+ ";";
    }
    
    public String popMessage(){
        String m = message;
        message = "";
        return m;
    }
    
    public String popWebMessage(){
        String m = message;
        message = "";
        return m.replaceAll(";", "<br>");
    }
    
    
    
    
}
