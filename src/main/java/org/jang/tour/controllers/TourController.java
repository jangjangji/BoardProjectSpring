package org.jang.tour.controllers;

import org.jang.global.exceptions.ExceptionProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tour")
public class TourController implements ExceptionProcessor {
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Long id, Model model) {
        model.addAttribute("addCss", List.of("tour/style3"));
        model.addAttribute("addScript", List.of("tour/view3"));

        return "front/tour/view3";
    }
}
