package pl.put.poznan.transformer.web;   // <— under the root package

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class PageController {

    @GetMapping("/choose-option")
    public String chooseOptions(String encodedJson, Model model) {
        model.addAttribute("encodedJson", encodedJson);
        return "choose-option";          // logical view name
    }

    @GetMapping("/final-json")
    public String finalJson(String finalJson, Model model) {
        model.addAttribute("encodedJson", finalJson);
        return "final-json";          // logical view name
    }
    @GetMapping("/reset")
    public String reset() {
        return "";          // logical view name
    }
}