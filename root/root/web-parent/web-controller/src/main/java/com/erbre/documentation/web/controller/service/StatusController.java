package com.erbre.documentation.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/status")
public class StatusController {

    private final String version = "1.0";

    @RequestMapping(method = RequestMethod.GET)
    public String status(Model model) {

        Map<String, Object> globalInformation = new HashMap<String, Object>();
        globalInformation.put("date", new Date());
        globalInformation.put("version", version);
        model.addAttribute("globalInformation", globalInformation);

        Map<String, String> sysProperties = new HashMap<String, String>();
        System.getProperties().forEach((k, v) -> sysProperties.put(k.toString(), (v == null ? "" : v.toString())));
        model.addAttribute("systemProperties", sysProperties);

        Map<String, String> envProperties = new HashMap<String, String>();
        System.getenv().forEach((k, v) -> envProperties.put(k.toString(), (v == null ? "" : v.toString())));
        model.addAttribute("envProperties", envProperties);
        
        return "status";
    }
}
