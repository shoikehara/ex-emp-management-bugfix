package jp.co.sample.emp_management.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

/**
 * ログイン時に使用するフォーム.
 * 
 * @author igamasayuki
 * 
 */
public class LoginForm {
	/** メールアドレス */
	@Size(min=1,max=127,message="Eメールアドレスは1文字以上127文字以下で入力してください")
	@Email(message="Eメールの形式が不正です")
	private String mailAddress;
	/** パスワード */
	@Size(min=4,max=30,message="パスワードは1文字以上30文字以下で入力してください")
	private String password;

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginForm [mailAddress=" + mailAddress + ", password=" + password + "]";
	}

}
