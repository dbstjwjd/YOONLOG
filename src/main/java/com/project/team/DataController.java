package com.project.team;

import com.project.team.Restaurant.Restaurant;
import com.project.team.Restaurant.RestaurantService;
import com.project.team.User.SiteUserService;
import com.project.team.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class DataController {
    private final RestaurantService restaurantService;
    private final SiteUserService siteUserService;
    private final ReviewService reviewService;
    private final String HEAD = "https://place.map.kakao.com/main/v";

    @GetMapping("/getData")
    public String getData() {
        return "getData";
    }

    @PostMapping(value = "/getData")
    public void receive(@RequestBody List<Map<String, String>> result) {
        boolean skip;
        for (Map<String, String> data : result) {
            skip = false;
            String address = data.get("road_address_name");
            String name = data.get("place_name");
            String url = data.get("place_url");
            List<Restaurant> restaurants = restaurantService.getByAddress(address);
            for (Restaurant res : restaurants) {
                if (res.getName().equals(name)) skip = true;
            }
            if (skip) continue;
            try {
                String doc = Jsoup.connect(this.HEAD + url.substring(url.lastIndexOf("/"))).ignoreContentType(true).execute().body();
                JSONParser jsonParser = new JSONParser();
                JSONObject detail = (JSONObject) jsonParser.parse(doc);
                String detailStr = detail.toString();

                JSONObject basicInfo = (JSONObject) detail.get("basicInfo");
                String realTime = null;
                try {
                    realTime = ((JSONObject) ((JSONArray) ((JSONObject) ((JSONArray) ((JSONObject) basicInfo.get("openHour"))
                            .get("periodList")).get(0)).get("timeList")).get(0)).get("timeSE").toString();
                } catch (Exception ignored) {
                }
                String info = null;
                LocalTime startTime = null;
                LocalTime endTime = null;
                if (realTime != null) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                        String[] times = realTime.split("~");
                        startTime = LocalTime.parse(times[0].trim(), formatter);
                        endTime = LocalTime.parse(times[1].trim(), formatter);
                    } catch (DateTimeParseException e) {
                        startTime = null;
                        endTime = null;
                    }
                }
                if (basicInfo.get("introduction") != null) info = basicInfo.get("introduction").toString();

                Restaurant restaurant = restaurantService.registerRestaurant(name, address, data.get("phone"),
                        null, null, null, startTime, endTime, info);
                restaurantService.setLocation(restaurant, data.get("x"), data.get("y"));

                if (detail.get("comment") == null)
                    continue;

                JSONArray comments = (JSONArray) ((JSONObject) detail.get("comment")).get("list");
                if (comments != null) reviewService.createTmp(restaurant, comments);

            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @GetMapping("/interprocess")
    public String interprocess(@RequestParam(value = "inputAddress", defaultValue = "aroundMe") String inputAddress,
                               Model model) {
        model.addAttribute("inputAddress", inputAddress);
        return "interprocess";
    }
}
