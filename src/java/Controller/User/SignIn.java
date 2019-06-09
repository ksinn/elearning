package Controller.User;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import API.HTTPClient;
import Auth.SMSAuthenticator;
import Auth.Secret;
import Controller.HttpServletParent;
import Entety.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ksinn
 */
public class SignIn extends HttpServletParent {

    @Override
    protected void doMyGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String myURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

        User user = new User();
        String userMail = null;

        if (Boolean.parseBoolean(request.getServletContext().getInitParameter("oAuth"))) {
            userMail = getGoogleData(request.getParameter("code"), myURL);
        } else {
            userMail = request.getParameter("mail");
        }
        if (userMail != null) {
            userMail = userMail.toLowerCase();
            if (user.getByMail(userMail)) {
                if (Boolean.parseBoolean(request.getServletContext().getInitParameter("secondFactor"))) {
                    request.getSession().setAttribute("1s_user", user);
                    Secret secret = userService.getSecondFactor(user);
                    if (secret != null) {
                        if ("phone".equals(secret.Type)) {
                            SMSAuthenticator sms = new SMSAuthenticator();
                            if (!sms.sendSMS(user.getId(), secret.Secret, myURL)) {
                                message("SMS gateway is fail!", request, response);
                                return;
                            }
                        }
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                    } else {
                        response.sendRedirect("signUp");
                    }
                } else {
                    request.getSession().setAttribute("user", user);
                    response.sendRedirect("cabinet");
                }
            } else {
                message("This mail not registred in system", request, response);
            }
        } else {
            request.getRequestDispatcher("mailLogin.jsp").forward(request, response);
        }
    }

    private String getGoogleData(String code, String my_url) throws JSONException {

        String client_id = "1006393654499-p8mr2fj0fkg43ifvl68eo2k18o6u2qgm.apps.googleusercontent.com";
        String client_secret = "lU9JFY65Oy7Oas33THOn_CUN";
        String redirect_uri = my_url + "/user/signIn";
        String grant_type = "authorization_code";

        String url = "https://accounts.google.com/o/oauth2/token";
        String param = "client_id=" + client_id
                + "&client_secret=" + client_secret
                + "&redirect_uri=" + redirect_uri
                + "&grant_type=" + grant_type
                + "&code=" + code;

        HTTPClient client = new HTTPClient(url, param, "POST");
        client.sendRequest();
        JSONObject requestJSON = client.getRequestJSON();
        HTTPClient client1 = new HTTPClient("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + requestJSON.getString("access_token"), null, "GET");
        client1.sendRequest();
        JSONObject user_data = client1.getRequestJSON();
        return user_data.getString("email");

    }

    @Override
    protected void doMyPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long t = System.currentTimeMillis();
        User user = (User) request.getSession().getAttribute("1s_user");
        if (user == null) {
            response.sendRedirect(request.getContextPath());
            return;
        }

        String codestr = request.getParameter("code");
        long code = Long.parseLong(codestr);
        boolean p = userService.LogIn(user, code, t, request.getHeader("user-agent") + "; " + request.getHeader("x-forwarded-for"));
        String mess;
        if (p) {
            request.getSession().removeAttribute("1s_user");
            request.getSession().setAttribute("user", user);
            response.sendRedirect("cabinet");
        } else {
            request.getSession().removeAttribute("1s_user");
            request.setAttribute("message", "Invalid code!");
            request.getRequestDispatcher("/message.jsp").forward(request, response);
        }

    }

    @Override
    protected int PrivateMod() {
        return HttpServletParent.OnlyForUnAuthorized;
    }

}
