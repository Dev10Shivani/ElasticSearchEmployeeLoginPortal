package com.es.elasticsearch.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.es.elasticsearch.Repository.ElasticSearchQueryEmployee;
import com.es.elasticsearch.Repository.ElasticSearchQueryTask;
import com.es.elasticsearch.entity.Employee;
import com.es.elasticsearch.entity.Task;

@Controller
public class EmployeeTaskController {

	@Autowired
	private ElasticSearchQueryTask elasticSearchQueryTask;

	@Autowired
	private ElasticSearchQueryEmployee elasticSearchQueryEmployee;

	@GetMapping("/taskDashboard")
	public String viewHomePage(Model model, String keyword) throws IOException {
		if (keyword != null) {
			model.addAttribute("listTaskDocuments", elasticSearchQueryTask.searchTaskByKeyword(keyword));
		} else {
			model.addAttribute("listTaskDocuments", elasticSearchQueryTask.searchAllDocuments());
		}
		return "taskDashboard";
	}

	// To navigate to signup page
	@GetMapping("/taskCreation")
	public String registrationForm(Model model) throws IOException {
		Task task = new Task();
		model.addAttribute("task", task);
		Map<String, String> empMap = elasticSearchQueryEmployee.FetchEmpIdName();
		model.addAttribute("empMapList", empMap);
		return "newTask";
	}

	// To save/add employee data in elastic search index
	@PostMapping("/addTask")
	public String addUser(@ModelAttribute Task task, Model model, HttpSession session) throws IOException {

		Map<String, String> empMap = elasticSearchQueryEmployee.FetchEmpIdName();

		Set set = empMap.entrySet();
		Iterator itr = set.iterator();
		while (itr.hasNext()) {
			Map.Entry entry = (Map.Entry) itr.next();
			if (entry.getValue().equals(task.getAssignTo())) {
				task.setEmpId(entry.getKey().toString());
			}
		}
		elasticSearchQueryTask.createOrUpdateDocument(task);
		session.setAttribute("message", "Task created successfully..");
		return "newTask";
	}

	@PostMapping("/saveTask")
	public String saveUser(@ModelAttribute("task") Task task, HttpSession session) throws IOException {
		Map<String, String> empMap = elasticSearchQueryEmployee.FetchEmpIdName();
		Set set = empMap.entrySet();
		Iterator itr = set.iterator();
		while (itr.hasNext()) {
			Map.Entry entry = (Map.Entry) itr.next();
			if (entry.getValue().equals(task.getAssignTo())) {
				task.setEmpId(entry.getKey().toString());
			}
		}
		elasticSearchQueryTask.createOrUpdateDocument(task);
		session.setAttribute("message", "Task Updated successfully..");
		return "updateTask";
	}

	@GetMapping("/updateTask/{id}")
	public String showFormForUpdate(@PathVariable(value = "id") String id, Model model) throws IOException {
		Task task = elasticSearchQueryTask.getDocumentById(id);
		Map<String, String> empMap = elasticSearchQueryEmployee.FetchEmpIdName();
		model.addAttribute("empMapList", empMap);
		model.addAttribute("task", task);
		return "updateTask";
	}

	@GetMapping("/fetchTask/{id}")
	public String showUserDetails(@PathVariable(value = "id") String id, Model model) throws IOException {
		Task task = elasticSearchQueryTask.getDocumentById(id);
		model.addAttribute("taskdata", task);
		return "showTask";
	}

	@GetMapping("/deleteTask/{id}")
	public String deleteTask(@PathVariable(value = "id") String id) throws IOException {
		this.elasticSearchQueryTask.deleteDocumentById(id);
		return "taskDashboard";
	}
	
	@GetMapping("/editProfile/{id}")
	public String editProfile(@PathVariable(value = "id") String id, Model model) throws IOException {
		Employee employee = elasticSearchQueryEmployee.getDocumentByEmployeeId(id);
		System.out.println("edit profile >>> "+employee);
		model.addAttribute("employee", employee);
		return "editProfile";
	}

	@PostMapping("/saveProfile")
	public String resetPassword(@ModelAttribute Employee employee, HttpSession session) throws IOException {
		System.out.println("save profile >>> "+employee);
		elasticSearchQueryEmployee.createUpdateDocumentEmployee(employee);
		session.setAttribute("message", "Profile updated successfully..");
		return "editProfile";
	}
	
	@GetMapping("/deleteEmpTask/{id}")
	public String deleteEmpTask(@PathVariable(value = "id") String id) throws IOException {
		this.elasticSearchQueryTask.deleteDocumentById(id);
		return "employeeDashboard";
	}
	
	@GetMapping("/empTaskCreation")
	public String TaskForm(Model model) throws IOException {
		Task task = new Task();
		model.addAttribute("task", task);
		Map<String, String> empMap = elasticSearchQueryEmployee.FetchEmpIdName();
		model.addAttribute("empMapList", empMap);
		return "newEmpTask";
	}

	// To save/add employee data in elastic search index
	@PostMapping("/addEmpTask")
	public String addTask(@ModelAttribute Task task, Model model, HttpSession session) throws IOException {

		Map<String, String> empMap = elasticSearchQueryEmployee.FetchEmpIdName();

		Set set = empMap.entrySet();
		Iterator itr = set.iterator();
		while (itr.hasNext()) {
			Map.Entry entry = (Map.Entry) itr.next();
			if (entry.getValue().equals(task.getAssignTo())) {
				// System.out.println("Emp entry >> " + entry.getKey() + " " + entry.getValue());
				task.setEmpId(entry.getKey().toString());
			}
		}
		elasticSearchQueryTask.createOrUpdateDocument(task);
		session.setAttribute("message", "Task created successfully..");
		return "newEmpTask";
	}
}