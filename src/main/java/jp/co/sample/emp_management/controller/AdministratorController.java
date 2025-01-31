package jp.co.sample.emp_management.controller;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.form.InsertAdministratorForm;
import jp.co.sample.emp_management.form.LoginForm;
import jp.co.sample.emp_management.service.AdministratorService;

/**
 * 管理者情報を管理するコントローラクラス.
 * 
 * @author sho.ikehara
 *
 */
@Controller
@RequestMapping("/")
public class AdministratorController{
	@Autowired
	private AdministratorService administratorService;
	
	@Autowired
	private HttpSession session;

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public InsertAdministratorForm setUpInsertAdministratorForm() {
		return new InsertAdministratorForm();
	}
	
	//  (SpringSecurityに任せるためコメントアウトしました)
	@ModelAttribute
	public LoginForm setUpLoginForm() {
		return new LoginForm();
	}

	/////////////////////////////////////////////////////
	// ユースケース：管理者を登録する
	/////////////////////////////////////////////////////
	/**
	 * 管理者登録画面を出力します.
	 * 
	 * @return 管理者登録画面
	 */
	@RequestMapping("/toInsert")
	public String toInsert() {
		String token = UUID.randomUUID().toString();
		session.setAttribute("token", token);
		return "administrator/insert";
	}

	/**
	 * 管理者情報を登録します.
	 * 
	 * @param form
	 *            管理者情報用フォーム
	 * @return ログイン画面へリダイレクト
	 */
	@RequestMapping("/insert")
	public String insert(@Validated InsertAdministratorForm form,BindingResult result,String token,Model model) {
		Administrator administrator = administratorService.findMailAddress(form.getMailAddress());
		if(administrator!=null) {
			result.rejectValue("mailAddress",null, "既に登録されているメールアドレスです。");
		}
		if(!(form.getPassword().equals(form.getCheckPassword()))) {
			result.rejectValue("password",null, "入力したパスワードが一致しません");
		}
		if(result.hasErrors()) {
			return toInsert();
		}
		String checkToken = (String)session.getAttribute("token");
		if(checkToken.equals(token)) {	
			session.removeAttribute("token");
			administrator = new Administrator();
			BeanUtils.copyProperties(form, administrator);
			administratorService.insert(administrator);			
		}
		return toLogin(model,null);
	}

	/////////////////////////////////////////////////////
	// ユースケース：ログインをする
	/////////////////////////////////////////////////////
	/**
	 * ログイン画面を出力します.
	 * 
	 * @return ログイン画面
	 */
	@RequestMapping("/")
	public String toLogin(Model model,@RequestParam(required = false) String error) {
		if(error != null) {
			model.addAttribute("loginError", "メールアドレスまたはパスワードが不正です。");
		}
		return "administrator/login";
	}

//	/**
//	 * ログインします.
//	 * 
//	 * @param form
//	 *            管理者情報用フォーム
//	 * @param result
//	 *            エラー情報格納用オブッジェクト
//	 * @return ログイン後の従業員一覧画面
//	 */
//	@RequestMapping("/login")
//	public String login(@Validated LoginForm form, BindingResult result, Model model) {
//		if(result.hasErrors()) {
//			return toLogin();
//		}
//		Administrator administrator = administratorService.findMailAddress(form.getMailAddress());
//		if (administratorService.matchedPassword(form.getPassword(),(administrator.getPassword()))) {
//			return "redirect:employee/showList";
//		}
//		model.addAttribute("loginError", "メールアドレスまたはパスワードが不正です。");
//		return toLogin();
//	}
	
	/////////////////////////////////////////////////////
	// ユースケース：ログアウトをする
	/////////////////////////////////////////////////////
	/**
	 * ログアウトをします. (SpringSecurityに任せるためコメントアウトしました)
	 * 
	 * @return ログイン画面
	 */
	@RequestMapping(value = "/logout")
	public String logout() {
		session.invalidate();
		return "redirect:/";
	}
}
