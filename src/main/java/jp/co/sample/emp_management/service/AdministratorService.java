package jp.co.sample.emp_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.repository.AdministratorRepository;

/**
 * 管理者情報を操作するサービス.
 * 
 * @author igamasayuki
 *
 */
@Service
@Transactional
public class AdministratorService {
	@Autowired
	PasswordEncoder passwordEncoder ;
    /**
     * passwordのハッシュ化を行う.
     * 
     * @param password 入力されたパスワード
     * @return　ハッシュ化後のパスワード
     */
    public String getHashedPassword(String password) {
    	String hashedPassword = passwordEncoder.encode(password);
    	return hashedPassword;
    }
    /**
     * 入力したpasswordとデータベースにあるハッシュ化されたpasswordの比較を行う.
     * 
     * @param password 入力されたパスワード
     * @param hashedPassword　データベースのハッシュ化済みパスワード
     * @return　一致または不一致
     */
    public boolean matchedPassword(String password,String hashedPassword) {
    	return passwordEncoder.matches(password, hashedPassword);
    }
	
	@Autowired
	private AdministratorRepository administratorRepository;

	/**
	 * 管理者情報を登録します.
	 * 
	 * @param administrator　管理者情報
	 */
	public void insert(Administrator administrator) {
		administrator.setPassword(getHashedPassword(administrator.getPassword()));
		administratorRepository.insert(administrator);
	}
	
	/**
	 * ログインをします.
	 * @param mailAddress メールアドレス
	 * @param password パスワード
	 * @return 管理者情報　存在しない場合はnullが返ります
	 */
	public Administrator login(String mailAddress, String password) {
		Administrator administrator = administratorRepository.findByMailAddressAndPassward(mailAddress, password);
		return administrator;
	}
	
	/**
	 * メールアドレスから従業員を検索する.
	 * 
	 * @param mailAddress 入力されたメールアドレス
	 * @return　従業員情報
	 */
	public Administrator findMailAddress(String mailAddress) {
		Administrator administrator = administratorRepository.findByMailAddress(mailAddress);
		return administrator;
	}
}
