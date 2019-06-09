package Controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Entety.User;
import Service.CourseService;
import Service.MessageService;
import Service.TaskService;
import Service.UserService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ksinn
 */
public abstract class HttpServletParent extends HttpServlet {

    abstract protected void doMyGet(HttpServletRequest request, HttpServletResponse response) throws Exception;

    abstract protected void doMyPost(HttpServletRequest request, HttpServletResponse response) throws Exception;

    static protected int OnlyForAuthorized = 1;
    static protected int OnlyForUnAuthorized = 2;
    static protected int ForAll = 0;
    static protected UserService userService = new UserService();
    static protected CourseService courseService = new CourseService();
    static protected TaskService taskService = new TaskService();
    protected MessageService messageService;

    abstract protected int PrivateMod();

    protected User user;

    protected void message(String mes, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        messageService.putMessage(mes);
        request.getRequestDispatcher("/message.jsp").forward(request, response);
    }
    
    private void Chose(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        try {
            
            this.messageService = (MessageService) request.getSession().getAttribute("messageService");
            if(messageService == null){
                this.messageService = new MessageService();
                request.getSession().setAttribute("messageService", messageService);
            }
            this.user = (User) request.getSession().getAttribute("user");
            boolean canContinue = false;
            String mes = "";
            switch (this.PrivateMod()) {
                case 0: {
                    canContinue = true;
                    break;
                }
                case 1: {
                    canContinue = user != null;
                    mes = "This page only for avtorized user.";
                    break;
                }
                case 2: {
                    canContinue = user == null;
                    mes = "This page only for not avtorized user.";
                    break;
                }
                default: {
                    throw new ServletException("Error in Private Politics rule in page " + request.getContextPath() + "!");
                }
            }

            if (user == null) {
                user = new User();
            }

            if (canContinue) {
                if (request.getMethod().equals("POST")) {
                    this.doMyPost(request, response);
                    return;
                } else {
                    this.doMyGet(request, response);
                    return;
                }
            } else if (this.PrivateMod() == 1) {
                response.sendRedirect(request.getContextPath() + "/user/signIn");
                return;
            } else {
                message(mes, request, response);
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        this.Chose(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        this.Chose(request, response);

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
