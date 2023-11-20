package com.project.team.SMS;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
public class SMSService {

    private String apiKey = "NCSEIS05KZ5NXZ4E";

    private String apiSecret = "2OKACTN9PVKAEH5BY8JLDDPKCCOHNE6P";

    public void sendMessage(String phoneNumber, String verKey) {
        Message coolsms = new Message(apiKey, apiSecret);
        HashMap<String, String> params = new HashMap();
        params.put("to", phoneNumber);
        params.put("from", "01024616781");
        params.put("type", "SMS");
        params.put("text", "[팀프로젝트] 인증번호는 " + "["+verKey+"]" + " 입니다.");

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
        } catch (CoolsmsException e) {
            e.printStackTrace();
        }
    }

}
