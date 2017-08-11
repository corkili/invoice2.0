package org.hld.invoice.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Base64;

@Controller
public class UserController {
    @RequestMapping(value = "test", method = RequestMethod.GET)
    public ModelAndView test() {
        return new ModelAndView("test");
    }

    @RequestMapping(value = "test", method = RequestMethod.POST)
    public ModelAndView test(@RequestParam("file") MultipartFile file) {
        ModelAndView modelAndView = new ModelAndView("test");
        try {
            String base64 = Base64.getEncoder().encodeToString(file.getBytes());
            modelAndView.addObject("base64", base64);
        } catch (IOException ignored) {
            modelAndView.addObject("base64", "");
        }
        return modelAndView.addObject("type", file.getContentType())
                .addObject("name", file.getName())
                .addObject("originName", file.getOriginalFilename());
    }
}
