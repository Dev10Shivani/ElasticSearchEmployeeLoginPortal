package com.es.elasticsearch.controller;
import com.es.elasticsearch.Repository.ElasticSearchQuery;
import com.es.elasticsearch.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class SignInUpController {

    @Autowired
    private ElasticSearchQuery elasticSearchQuery;

    @GetMapping("/")
    public String viewHomePage(Model model) throws IOException {
        return "index";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") Employee employee) throws IOException {
        elasticSearchQuery.createOrUpdateDocument(employee);
        return "redirect:/";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") String id, Model model) throws IOException {

        Employee employee = elasticSearchQuery.getDocumentById(id);
        model.addAttribute("user", employee);
        return "updateUserDocument";
    }

    @GetMapping("/showNewUserForm")
    public String showNewEmployeeForm(Model model) {
        // create model attribute to bind form data
        Employee employee = new Employee();
        model.addAttribute("user", employee);
        return "newUserDocument";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable(value = "id") String id) throws IOException {

        this.elasticSearchQuery.deleteDocumentById(id);
        return "redirect:/";
    }
}