package jp.co.sample.emp_management.service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jp.co.sample.emp_management.domain.Employee;
import jp.co.sample.emp_management.repository.EmployeeRepository;

/**
 * 従業員情報を操作するサービス.
 * 
 * @author igamasayuki
 *
 */
@Service
@Transactional
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	public void saveFile(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		Path uploadfile = Paths.get("src/main/resources/static/img/" + fileName);
		try (OutputStream os = Files.newOutputStream(uploadfile, StandardOpenOption.CREATE)) {
			byte[] bytes = file.getBytes();
			os.write(bytes);
		} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	/**
	 * 従業員情報を全件取得します.
	 * 
	 * @return　従業員情報一覧
	 */
	public List<Employee> showList() {
		List<Employee> employeeList = employeeRepository.findAll();
		return employeeList;
	}
	public List<Employee> showList(Integer offset) {
		if(offset == null) {
			offset = 1;
		}
		offset = offset *10 - 10;
		List<Employee> employeeList = employeeRepository.findAll(offset);
		return employeeList;
	}
	
	/**
	 * 従業員情報を取得します.
	 * 
	 * @param id ID
	 * @return 従業員情報
	 * @throws 検索されない場合は例外が発生します
	 */
	public Employee showDetail(Integer id) {
		Employee employee = employeeRepository.load(id);
		return employee;
	}
	
	/**
	 * 従業員情報を更新します.
	 * 
	 * @param employee　更新した従業員情報
	 */
	public void update(Employee employee) {
		employeeRepository.update(employee);
	}
	
	public List<Employee> findByLikeName(String name){
		return employeeRepository.findByLikeName(name);
	}
	
	public void insert(Employee employee) {
		employeeRepository.insert(employee);
	}
	
	public List<String> getAllNames(){
		return employeeRepository.getAllNames();
	}	
}
