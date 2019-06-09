/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entety;

import API.HTTPClient;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.oauth.jsontoken.Checker;
import net.oauth.jsontoken.JsonToken;
import net.oauth.jsontoken.JsonTokenParser;
import net.oauth.jsontoken.crypto.HmacSHA256Verifier;
import net.oauth.jsontoken.crypto.SignatureAlgorithm;
import net.oauth.jsontoken.crypto.Verifier;
import net.oauth.jsontoken.discovery.VerifierProvider;
import net.oauth.jsontoken.discovery.VerifierProviders;

/**
 *
 * @author ksinn
 */
public class Service extends Parent {

    private static String WorkStartPoint = "/exam/start";
    private static String AuthStartPoint = "/enter";
    private static String TaskListAPI = "/api/task/list";

    private String Name;
    private String URL;
    private String MyKey;
    private String ServiceKey;

    public static ArrayList<Service> getAll() {
        ArrayList<Service> list = new ArrayList<Service>();
        Service serv = new Service();
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("1", 1);
        try {
            ArrayList<HashMap<String, Object>> Params = serv.getObjectsParam(param);
            for (int i = 0; i < Params.size(); i++) {
                serv = new Service();
                try {
                    serv.getFromParam(Params.get(i));
                    list.add(serv);
                } catch (Exception ex) {
                }
            }
        } catch (Exception ex) {
        }
        return list;
    }

    public Service() {

    }

    @Override
    public String _getType() {
        return "task_service";
    }

    public String getServiceKey() {

        return this.ServiceKey;
    }

    public String getMyKey() {

        return this.MyKey;
    }

    public String getURL() {

        return this.URL;
    }

    public String getName() {

        return this.Name;
    }

    @Override
    protected HashMap<String, Object> _getParams() {
        HashMap<String, Object> list = new HashMap<String, Object>();
        list.put("name", this.Name);
        list.put("url", this.URL);
        list.put("my_key", this.MyKey);
        list.put("service_key", this.ServiceKey);

        return list;
    }

    @Override
    protected void _setParams(HashMap<String, Object> Params) throws Exception {
        this.Name = (String) Params.get("name");
        this.URL = (String) Params.get("url");
        this.MyKey = (String) Params.get("my_key");
        this.ServiceKey = (String) Params.get("service_key");
    }

    @Override
    protected boolean _isCorrect() {
        return true;
    }

    @Override
    public boolean MayChange() {
        return true;
    }

    public void getById(int id) throws Exception {
        if (id > 0) {
            this.ID = id;
            this._select();
        } else {
            throw new Exception("Invalid input data for teaching with id=" + id);
        }
    }

    public String getWorkStartPointURL() {
        return this.URL + WorkStartPoint;
    }

    public String getEnterStartPointURL() {
        return this.URL + AuthStartPoint;
    }

    public Map getTaskList(User user) {
        Map<String, String> task = new HashMap();
        HTTPClient client = new HTTPClient(this.getTaskListURL(), "user=" + user.getId(), "POST");
        client.sendRequest();
        //WorkJWT tok = new WorkJWT();
        JsonToken token = pars(client.getRequestText());
        if (token.getExpiration().getMillis() > System.currentTimeMillis()) {
            if (token.getParamAsPrimitive("status").getAsInt() == 200) {
                String strMap = token.getParamAsPrimitive("list").getAsString().replaceAll("\\{|\\}", "");
                strMap = strMap.replaceAll(", ", ",");
                task = Splitter.on(",").withKeyValueSeparator("=").split(strMap);
                return task;
            }
        }
        return task;

    }

    private String getTaskListURL() {
        return this.URL + TaskListAPI;
    }

    private JsonToken pars(String token) {
        try {
            final Verifier hmacVerifier = new HmacSHA256Verifier(this.ServiceKey.getBytes());

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

}
