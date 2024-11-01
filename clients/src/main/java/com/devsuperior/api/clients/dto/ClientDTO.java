package com.devsuperior.api.clients.dto;

import java.time.Instant;

import com.devsuperior.api.clients.entities.Client;

public class ClientDTO {

	private Long id;
	private String login;
	private String name;
	private String location;
	private String avatarUrl;
	private Integer followers;
	private Integer following;
	private Instant createdAt;
	private String reposUrl;
	private String company;

	public ClientDTO() {
	}

	public ClientDTO(Long id, String login, String name, String location, String avatarUrl, Integer followers,
			Integer following, Instant createdAt, String reposUrl, String company) {
		super();
		this.id = id;
		this.login = login;
		this.name = name;
		this.location = location;
		this.avatarUrl = avatarUrl;
		this.followers = followers;
		this.following = following;
		this.createdAt = createdAt;
		this.reposUrl = reposUrl;
		this.company = company;
	}

	public ClientDTO(Client entity) {
		this.id = entity.getId();
		this.login = entity.getLogin();
		this.name = entity.getName();
		this.location = entity.getLocation();
		this.avatarUrl = entity.getAvatarUrl();
		this.followers = entity.getFollowers();
		this.following = entity.getFollowing();
		this.createdAt = entity.getCreatedAt();
		this.reposUrl = entity.getReposUrl();
		this.company = entity.getCompany();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public Integer getFollowers() {
		return followers;
	}

	public void setFollowers(Integer followers) {
		this.followers = followers;
	}

	public Integer getFollowing() {
		return following;
	}

	public void setFollowing(Integer following) {
		this.following = following;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public String getReposUrl() {
		return reposUrl;
	}

	public void setReposUrl(String reposUrl) {
		this.reposUrl = reposUrl;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
}
