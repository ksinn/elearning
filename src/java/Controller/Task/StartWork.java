/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Task;

import Controller.HttpServletParent;
import Entety.Service;
import Entety.Task;
import Entety.Study;
import Entety.Work;
import com.google.gson.JsonObject;
import java.security.InvalidKeyException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.oauth.jsontoken.JsonToken;
import net.oauth.jsontoken.crypto.HmacSHA256Signer;
import org.joda.time.Instant;

/**
 *
 * @author ksinn
 */
public class StartWork extends HttpServletParent {

    @Override
    protected void doMyPost(HttpServletRequest request, HttpServletResponse response) throws Exception {

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    protected int PrivateMod() {
        return HttpServletParent.OnlyForAuthorized;
    }

    @Override
    protected void doMyGet(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String myURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        String myName = "elearning";
        int idTask = Integer.parseInt(request.getParameter("t"));
        Task task = new Task();
        task.getById(idTask);

        int studyId = Integer.parseInt(request.getParameter("s"));
        Study study = new Study();
        study.getById(studyId);

        Work work = taskService.start(study, task);
        if (work != null) {
            Service service = new Service();
            service.getById(task.getServiceId());

            HmacSHA256Signer signer;
            try {
                signer = new HmacSHA256Signer(myName, null, work.getTask().getService().getMyKey().getBytes());
            } catch (InvalidKeyException e) {
                throw new RuntimeException(e);
            }
            JsonToken token = new JsonToken(signer);
            token.setAudience(work.getTask().getService().getName());
            token.setIssuedAt(Instant.now());
            token.setExpiration(Instant.now().plus(60 * 1000));
            JsonObject payload = token.getPayloadAsJsonObject();

            payload.addProperty("work_key", work.getWorkKey());

            response.setHeader("Location", task.getService().getWorkStartPointURL() + "?t=" + token.serializeAndSign());
            response.setHeader("Cache-Control", "no-store");
            response.setStatus(301);
        } else {
            message("Error!", request, response);
            return;
        }

    }

}
