package com.es.elasticsearch.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.es.elasticsearch.Repository.ElasticSearchQueryEmployee;
import com.es.elasticsearch.Repository.ElasticSearchQueryTask;
import com.es.elasticsearch.entity.Employee;
import com.es.elasticsearch.entity.Task;
import com.es.elasticsearch.service.EmployeeService;

@Controller
public class SignInUpController {

	@Autowired
	private ElasticSearchQueryEmployee elasticSearchQuery;

	@Autowired
	private ElasticSearchQueryTask elasticSearchQueryTask;

	@Autowired
	EmployeeService employeeService;

	@GetMapping("/")
	public String viewHomePage(Model model) throws IOException {
		return "index";
	}

	private String empID;

	// To navigate to signup page
	@GetMapping("/registration")
	public String registrationForm() {
		return "signUp";
	}

	// To save/add employee data in elastic search index
	@PostMapping("/addEmployee")
	public String addEmployee(@ModelAttribute Employee employee, HttpSession session) throws IOException {
		elasticSearchQuery.createUpdateDocumentEmployee(employee);
		session.setAttribute("message", "Employee registered successfully..");
		return "signUp";
	}

	// To navigate to signup page
	@PostMapping("/login")
	public String signIn(@RequestParam(value = "id") String employeeId,
			@RequestParam(value = "password") String password, Model model, HttpSession session) throws IOException {
		// System.out.println("employeeId : " + employeeId + "password :" + password);

		Employee employee = employeeService.checkValidEmployee(employeeId, password);
		if (null != employee) {
			empID = employee.getId();
			model.addAttribute("employee", employee);
			model.addAttribute("listTaskDocuments", elasticSearchQueryTask.getTasksByEmpId(employeeId));
			return "employeeDashboard";
		} else {
			session.setAttribute("message", "Invalid login details, Please enter valid Details!!");
			return "index";
		}
	}

	@GetMapping("/employeeDashboard")
	public String employeeDashboardAndSearch(Model model, String keyword, @RequestParam(defaultValue = "0") int page)
			throws IOException {

		model.addAttribute("employee", elasticSearchQuery.getDocumentByEmployeeId(empID));

		if (keyword != null) {
			List<Task> tasklist = elasticSearchQueryTask.searchTaskByKeyword(keyword);
			model.addAttribute("listTaskDocuments", elasticSearchQueryTask.searchTaskByKeyword(keyword));
			Page<Task> tasks = elasticSearchQueryTask.findPaginated(page, 10);
			model.addAttribute("currentPage", page);
			model.addAttribute("totalPages", tasks.getTotalPages());
			model.addAttribute("totalItems", tasklist.size());
		} else {
			List<Task> tasklist = elasticSearchQueryTask.getTasksByEmpId(empID);
			model.addAttribute("listTaskDocuments", elasticSearchQueryTask.getTasksByEmpId(empID));
			Page<Task> tasks = elasticSearchQueryTask.findPaginated(page, 10);
			model.addAttribute("currentPage", page);
			model.addAttribute("totalPages", tasks.getTotalPages());
			model.addAttribute("totalItems", tasks.getTotalElements());
		}
		return "employeeDashboard";
	}

	@GetMapping("/deleteEmpTask/{id}")
	public String deleteEmpTask(@PathVariable(value = "id") String id, Model model, HttpSession session)
			throws IOException {
		model.addAttribute("employee", elasticSearchQuery.getDocumentByEmployeeId(empID));
		model.addAttribute("listTaskDocuments", elasticSearchQueryTask.getTasksByEmpId(empID));
		this.elasticSearchQueryTask.deleteDocumentById(id);
		session.setAttribute("message", "Task " + id + " Deleted sucessfully. Please refresh the page");
		return "employeeDashboard";
	}

	@GetMapping("/forgotPassword")
	public String forgotPassword(Model model) {
		return "forgotPassword";
	}

	@PostMapping("/resetPassword")
	public String resetPassword(@ModelAttribute Employee employee, HttpSession session) throws IOException {
		Employee emp = elasticSearchQuery.getDocumentByEmployeeId(employee.getId());
		employee.setFirstName(emp.getFirstName());
		employee.setLastName(emp.getLastName());
		employee.setEmail(emp.getEmail());
		elasticSearchQuery.createUpdateDocumentEmployee(employee);
		session.setAttribute("message", "Password reset successfully..");
		return "forgotPassword";
	}
}