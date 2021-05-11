package com.news.aggregator.controller;

import com.news.aggregator.model.Content;
import com.news.aggregator.model.ContentList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class MainController {

    private final String url = "http://localhost:8080/news";
    private final AtomicInteger atomicInteger;

    public MainController(AtomicInteger atomicInteger) {
        this.atomicInteger = atomicInteger;
    }

    @GetMapping("/")
    public String getMainPage(@RequestParam(value = "page", required = false, defaultValue = "0") String page, ModelMap modelMap) {

        atomicInteger.set(Integer.parseInt(page));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(url)
                .append("?page=")
                .append(page);

        String url = stringBuilder.toString();

        WebClient webClient = WebClient.create();
        ContentList responseJson = webClient.get()
                .uri(url)
                .exchange()
                .block()
                .bodyToMono(ContentList.class)
                .block();

        List<Content> contentList = responseJson.getList();
        modelMap.addAttribute("newsList",contentList);

        return "mainpage";
    }

    @PostMapping("/button")
    public String page(@ModelAttribute(name = "next") String nextButton,
                       @ModelAttribute(name = "previous") String previousButton) {
        if (nextButton.equals("next")) {
            atomicInteger.incrementAndGet();
            return "redirect:/?page=" + atomicInteger.get();
        }
        else {
            atomicInteger.decrementAndGet();
            return "redirect:/?page=" + atomicInteger.get();
        }
    }

}
