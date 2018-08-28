package com.example.web.controller;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * created by liuxv on 2018/08/22
 * email:liuxver444@gmail.com
 * qq:1369058574
 */
@Controller
public class MainsiteErrorController implements ErrorController {

    private static final String ERROR_PATH="/error";

    @RequestMapping(value = ERROR_PATH)
    public String handleError(){
        return "403";
    }

    /**
     * Returns the path of the error page.
     *
     * @return the error path
     */
    @Override
    public String getErrorPath() {
        return "403";
    }

    @RequestMapping(value = "/deny")
    public String deny(){
        return "deny";
    }
}
