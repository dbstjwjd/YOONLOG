package com.ysblog.sbb;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        String tag = "[{\"value\":\"#태그\"},{\"value\":\"#태그2\"},{\"value\":\"#태그3\"}]";

        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(tag);

            List<String> tagList = new ArrayList<>();

            for (Object item : jsonArray) {
                JSONObject jsonObject = (JSONObject) item;
                String value = (String) jsonObject.get("value");
                if (value != null) {
                    tagList.add(value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
