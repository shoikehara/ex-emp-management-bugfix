package jp.co.sample.emp_management.controller;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.form.InsertAdministratorForm;
import jp.co.sample.emp_management.form.LoginForm;
import jp.co.sample.emp_management.service.AdministratorService;

/**
 * 管理者情報を操作するコントローラー.
 * 
 * @author igamasayuki
 *
 */
@Controller
@RequestMapping("/")
public class AdministratorController extends WebSecurityConfigurerAdapter{
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                            "/img/**",
                            "/css/**",
                            "/javascript/**");
	}
	
	@Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests().antMatchers("/","/toInsert").permitAll()
        .anyRequest().authenticated();
        http.formLogin()
        .loginProcessingUrl("/")   // 認証処理のパス
        .loginPage("/")            // ログインフォームのパス
        .failureUrl("/")       // 認証失敗時に呼ばれるハンドラクラス
        .defaultSuccessUrl("/employee/showList");     // 認証成功時の遷移先
    }

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
		return toLogin();
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
	public String toLogin() {
		return "administrator/login";
	}

	/**
	 * ログインします.
	 * 
	 * @param form
	 *            管理者情報用フォーム
	 * @param result
	 *            エラー情報格納用オブッジェクト
	 * @return ログイン後の従業員一覧画面
	 */
	@RequestMapping("/login")
	public String login(@Validated LoginForm form, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return toLogin();
		}
		Administrator administrator = administratorService.findMailAddress(form.getMailAddress());
		if (administratorService.matchedPassword(form.getPassword(),(administrator.getPassword()))) {
			session.setAttribute("administratorName", administrator.getName());
			return "redirect:employee/showList";
		}
		model.addAttribute("loginError", "メールアドレスまたはパスワードが不正です。");
		return toLogin();
	}
	
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
