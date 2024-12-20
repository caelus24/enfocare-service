package com.enfocareservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "voximplant_account")
public class VoximplantAccountEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "user")
	private String user;

	@Column(name = "vox_username")
	private String voxUsername;

	@Column(name = "vox_pw")
	private String voxPw;

	@Column(name = "vox_id")
	private String voxId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getVoxUsername() {
		return voxUsername;
	}

	public void setVoxUsername(String voxUsername) {
		this.voxUsername = voxUsername;
	}

	public String getVoxPw() {
		return voxPw;
	}

	public void setVoxPw(String voxPw) {
		this.voxPw = voxPw;
	}

	public String getVoxId() {
		return voxId;
	}

	public void setVoxId(String voxId) {
		this.voxId = voxId;
	}

}
