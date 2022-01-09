package com.tads.vdlab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginPageController {

    @GetMapping("/login")
    public String login(Model model){

        model.addAttribute("cadastrarSucesso", model.getAttribute("cadastrarSucesso"));
        model.addAttribute("recuperarSucesso", model.getAttribute("recuperarSucesso"));

        return "login";
    }
}
