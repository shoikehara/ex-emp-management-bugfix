package jp.co.sample.emp_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.sample.emp_management.service.EmployeeService;

@Controller
public class AutoCompleteController {
	@Autowired
	private EmployeeService employeeService;
	
	@RequestMapping(value = "autoComplete", method = RequestMethod.GET)
	@ResponseBody
	public List<String> autoCompleteList() {
		List<String> allNames = employeeService.getAllNames();
		return allNames;
	}
}
