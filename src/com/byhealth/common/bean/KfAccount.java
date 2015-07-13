package com.byhealth.common.bean;

import com.byhealth.common.bean.ToStringBase;

public class KfAccount extends ToStringBase {

	private static final long serialVersionUID = 3431997360800197137L;

	private String kf_account;

	private String nickname;

	private String password;

	public String getKf_account() {
		return kf_account;
	}

	public void setKf_account(String kf_account) {
		this.kf_account = kf_account;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getTableName() {
		return null;
	}

}