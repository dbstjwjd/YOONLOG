package com.project.team;

import com.project.team.User.UserCreateForm;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/")
    public String index(UserCreateForm userCreateForm) {
        return "start";
    }

    @GetMapping("/main")
    public String mainPage() {
        return "redirect:/map/view";
    }

    @GetMapping("/test")
    public String test(Model model) throws IOException, ParseException {
        // Document doc = Jsoup.connect().get();
        String doc = Jsoup.connect("https://place.map.kakao.com/main/v/1874862905").ignoreContentType(true).execute().body();
        JSONParser jsonParser = new JSONParser();
        JSONObject json = (JSONObject) jsonParser.parse(doc);
        JSONArray comments = (JSONArray)((JSONObject)json.get("comment")).get("list");
        model.addAttribute("doc", comments);
        return "test";
    }
}
