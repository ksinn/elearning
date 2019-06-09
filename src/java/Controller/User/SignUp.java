package Controller.User;

import Controller.HttpServletParent;
import Entety.User;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

public class SignUp extends HttpServletParent {

    @Override
    protected void doMyPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute("1s_user");
        request.getSession().removeAttribute("1s_user");
        if (user == null) {
            response.sendRedirect(request.getServletContext().getContextPath());
            return;
        }
        String s = userService.setSecretKey(user);
        if (s != null) {
            ByteArrayOutputStream outs = QRCode.from(s).to(ImageType.PNG).stream();

            response.setHeader("Cache-Control", "no-store");
            response.setContentType("image/png");
            response.setContentLength(outs.size());

            OutputStream outStream = response.getOutputStream();

            outStream.write(outs.toByteArray());

            outStream.flush();
            outStream.close();
        } else {
            message("Key has not created for user " + user.getId(), request, response);
        }

    }

    @Override
    protected int PrivateMod() {
        return HttpServletParent.OnlyForUnAuthorized;
    }

    @Override
    protected void doMyGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("signUp.jsp").forward(request, response);
    }

}
