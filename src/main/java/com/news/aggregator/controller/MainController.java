package com.news.aggregator.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.news.aggregator.model.Content;
import com.news.aggregator.model.MeduzaNews;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    private final String url = "https://meduza.io/api/v3/search?chrono=news&locale=ru&page=0&per_page=24";
    private List<Content> contentList = new ArrayList<>();


    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getMainPage(ModelMap modelMap) throws JsonProcessingException {

        WebClient webClient = WebClient.create();
        String responseJson = webClient.get()
                .uri(url)
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());
        MeduzaNews meduzaNews = objectMapper.readValue(responseJson, MeduzaNews.class);

        for (Map.Entry<String, Map<Object, Object>> entry: meduzaNews.getDocuments().entrySet()) {
            Map<Object, Object> map = entry.getValue();
            Content content = new Content();
            for (Map.Entry<Object, Object> obj:  map.entrySet()) {
                //String text = (String) obj.getValue();
                switch ((String) obj.getKey()) {
                    case "url": content.setUrl((String) obj.getValue());
                        break;
                    case "title": content.setTitle((String) obj.getValue());
                        break;
                    case "share_image_url": content.setShareImageUrl((String) obj.getValue());
                        break;

                    case "pub_date": content.setPubDate((String) obj.getValue());
                        break;
                        
                }


            }
            contentList.add(content);
        }

        //System.out.println(contentList);
        //modelMap.addAttribute("content", content);
        modelMap.addAttribute("newsList", contentList);

        return "mainpage";
    }

}
