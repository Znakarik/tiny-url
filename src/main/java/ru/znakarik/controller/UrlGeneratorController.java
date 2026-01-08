package ru.znakarik.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Рендерит форму ввода урла для последующей генерации
 */
@Controller
@RequestMapping("url/v1")
public class UrlGeneratorController {

    @GetMapping(value = "/showForm")
    public String showForm(Model model) {
        model.addAttribute("form", new Object());
        return "form";
    }

}
