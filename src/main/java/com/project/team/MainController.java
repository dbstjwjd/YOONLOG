package com.project.team;

import com.project.team.User.UserCreateForm;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
    public String test(Model model) throws IOException {
        Document doc = Jsoup.connect("https://place.map.kakao.com/1874862905").get();
        model.addAttribute("doc", doc.html());
        return "test";
    }
}
