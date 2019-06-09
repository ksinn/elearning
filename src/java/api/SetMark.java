/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import Entety.Service;
import Entety.Work;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.oauth.jsontoken.Checker;
import net.oauth.jsontoken.JsonToken;
import net.oauth.jsontoken.JsonTokenParser;
import net.oauth.jsontoken.crypto.HmacSHA256Signer;
import net.oauth.jsontoken.crypto.HmacSHA256Verifier;
import net.oauth.jsontoken.crypto.SignatureAlgorithm;
import net.oauth.jsontoken.crypto.Verifier;
import net.oauth.jsontoken.discovery.VerifierProvider;
import net.oauth.jsontoken.discovery.VerifierProviders;
import org.joda.time.Instant;

/**
 *
 * @author ksinn
 */
public class SetMark extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String myURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        String myName = "elearning";
        try {
            Service service = new Service();
            service.getById(1);

            JsonToken token = pars(parseRequest(request));
            if (token.getIssuer().equalsIgnoreCase(service.getName()) && token.getExpiration().getMillis() > System.currentTimeMillis()) {

                HmacSHA256Signer signer;
                signer = new HmacSHA256Signer(myName, null, service.getMyKey().getBytes());
                JsonToken res_token = new JsonToken(signer);
                res_token.setAudience(service.getName());
                res_token.setIssuedAt(Instant.now());
                res_token.setExpiration(Instant.now().plus(60 * 1000));
                JsonObject payload = token.getPayloadAsJsonObject();

                Work work = new Work();
                work.setWorkKey(token.getParamAsPrimitive("work_key").getAsString());
                if (work.getByKey()) {
                    if (work.putMark(token.getParamAsPrimitive("mark").getAsInt())) {
                        payload.addProperty("status", "200");
                    }

                    payload.addProperty("status", "401");
                    payload.addProperty("errorMessage", "Work time is ended");
                }
                payload.addProperty("status", "401");
                payload.addProperty("errorMessage", "Work with this kay not find");
            }

            PrintWriter out = response.getWriter();
            out.write(token.serializeAndSign());
            
        } catch (Exception ex) {
            response.sendError(500);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>// </editor-fold>

    private JsonToken pars(String token) throws Exception {
        try {
            Service service = new Service();
            service.getById(1);
            final Verifier hmacVerifier = new HmacSHA256Verifier(service.getServiceKey().getBytes());

            VerifierProvider hmacLocator = new VerifierProvider() {

                @Override
                public List<Verifier> findVerifier(String id, String key) {
                    return Lists.newArrayList(hmacVerifier);
                }
            };
            VerifierProviders locators = new VerifierProviders();
            locators.setVerifierProvider(SignatureAlgorithm.HS256, hmacLocator);
            net.oauth.jsontoken.Checker checker = new Checker() {

                @Override
                public void check(JsonObject payload) throws SignatureException {
                    // don't throw - allow anything
                }

            };
            //Ignore Audience does not mean that the Signature is ignored
            JsonTokenParser parser = new JsonTokenParser(locators, checker);
            JsonToken jt;
            try {
                jt = parser.verifyAndDeserialize(token);
                return jt;
            } catch (SignatureException e) {
                throw new RuntimeException(e);
            }

        } catch (InvalidKeyException e1) {
            throw new RuntimeException(e1);
        }
    }

    private String parseRequest(HttpServletRequest request) throws IOException {
        StringBuffer jb = new StringBuffer();
        String line = null;

        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            jb.append(line);
        }
        return jb.toString();

    }
}
