package jp.co.sample.emp_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * エラー時のページ遷移を行うcommonクラス.
 * 
 * @author sho.ikehara
 *
 */
@Controller
@RequestMapping("/common")
public class CommonController {
	
	@RequestMapping("/maintenance")
	public String maintenance() {
		return "error";
	}
}
