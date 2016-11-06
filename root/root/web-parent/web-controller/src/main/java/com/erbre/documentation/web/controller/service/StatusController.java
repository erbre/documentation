package com.erbre.documentation.web.controller.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/status")
public class StatusController {
 
    @Value("${version}")
    private String version;
    
    @RequestMapping(method = RequestMethod.GET)
    public String status(Model model) {
        Map<String, Object> statusInformation = new HashMap<String, Object>();

        Map<String, Object> globalInformation = new HashMap<String, Object>();
        globalInformation.put("date", new Date());
        globalInformation.put("version", version);
        statusInformation.put("globalInformation", globalInformation);

        Map<String, Object> vmInformation = new HashMap<String, Object>();
        vmInformation.put("freeMemory", Runtime.getRuntime().freeMemory());
        vmInformation.put("usedMemory", Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());
        vmInformation.put("totalMemory", Runtime.getRuntime().totalMemory());
        vmInformation.put("maxMemory", Runtime.getRuntime().maxMemory());
        vmInformation.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        
        statusInformation.put("vmInformation", vmInformation);

        
        Map<String, String> sysProperties = new HashMap<String, String>();
        System.getProperties().forEach((k, v) -> sysProperties.put(k.toString(), (v == null ? "" : v.toString())));
        statusInformation.put("systemProperties", sysProperties);

        Map<String, String> envProperties = new HashMap<String, String>();
        System.getenv().forEach((k, v) -> envProperties.put(k.toString(), (v == null ? "" : v.toString())));
        statusInformation.put("envProperties", envProperties);

        model.addAttribute("statusInformation", statusInformation);

        return "status";
    }
}
