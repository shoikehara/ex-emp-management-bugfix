package jp.co.sample.emp_management.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sample.emp_management.constant.Constant;
import jp.co.sample.emp_management.domain.Employee;
import jp.co.sample.emp_management.form.InsertEmployeeForm;
import jp.co.sample.emp_management.form.UpdateEmployeeForm;
import jp.co.sample.emp_management.service.EmployeeService;

/**
 * 従業員情報を操作するコントローラー.
 * 
 * @author igamasayuki
 *
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;
	
	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public UpdateEmployeeForm setUpForm() {
		return new UpdateEmployeeForm();
	}
	@ModelAttribute
	public InsertEmployeeForm insertForm() {
		return new InsertEmployeeForm();
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員一覧を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員一覧画面を出力します.
	 * 
	 * @param model モデル
	 * @return 従業員一覧画面
	 */
	@RequestMapping("/showList")
	public String showList(Model model,Integer page) {
		List<Employee> allEmployeeList = employeeService.showList();
		List<Employee> employeeList = employeeService.showList(page);
		List<Integer> pageList = new ArrayList<>();
		for(int i = 1;i <=(allEmployeeList.size()/Constant.ELEMENT_COUNT)+1;i++) {
			pageList.add(i);
		}
		List<String> nameList = employeeService.getAllNames();
		model.addAttribute("employeeList", employeeList);
		model.addAttribute("nameList",nameList);
		model.addAttribute("pageList",pageList);
		return "employee/list";
	}

	
	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細画面を出力します.
	 * 
	 * @param id リクエストパラメータで送られてくる従業員ID
	 * @param model モデル
	 * @return 従業員詳細画面
	 */
	@RequestMapping("/showDetail")
	public String showDetail(String id, Model model) {
		Employee employee = employeeService.showDetail(Integer.parseInt(id));
		model.addAttribute("employee", employee);
		return "employee/detail";
	}
	
	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を更新する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細(ここでは扶養人数のみ)を更新します.
	 * 
	 * @param form
	 *            従業員情報用フォーム
	 * @return 従業員一覧画面へリダクレクト
	 */
	@RequestMapping("/update")
	public String update(@Validated UpdateEmployeeForm form, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return showDetail(form.getId(), model);
		}
		Employee employee = new Employee();
		employee.setId(form.getIntId());
		employee.setDependentsCount(form.getIntDependentsCount());
		employeeService.update(employee);
		return "redirect:/employee/showList";
	}
	
	@RequestMapping("/findByLikeName")
	public String findByLikeName(String search,Model model) {
		List<Employee> employeeList = employeeService.findByLikeName(search);
		if(employeeList.size()==0) {
			model.addAttribute("notFindEmployee","一致する従業員が見つかりませんでした。");
			employeeList = employeeService.showList();
			model.addAttribute("employeeList",employeeList);
		}else {
			model.addAttribute("employeeList", employeeList);
		}
		return "employee/list";
	}
	
	@RequestMapping("/toInsert")
	public String toInsert() {
		return "employee/insert";
	}
	
	/**
	 * 従業員登録をする.
	 * 
	 * @param form　入力値
	 * @param model　モデル
	 * @return　従業員一覧
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping("/insert")
	public String insert(InsertEmployeeForm form,Model model) throws IllegalStateException, IOException {
		Employee employee = new Employee();
		BeanUtils.copyProperties(form, employee);
		employee.setImage(form.getImage().getOriginalFilename());
		employee.setHireDate(Date.valueOf(form.getHireDate()));
		employee.setSalary(Integer.parseInt(form.getSalary()));
		employee.setDependentsCount(Integer.parseInt(form.getDependentsCount()));
		employeeService.saveFile(form.getImage());
		employeeService.insert(employee);
		return "redirect:/employee/showList";
	}
}
