package com.templarknightsmc.tkmccontacts;

public class Contact {
	private String _id;
	private String _firstName;
	private String _lastName;
	private String _displayName;
	private String _nickname;
	private String _email;
	private String _phone;
	private String _rank;
	private String _lodge;
	
	public String getId() { return _id; }
	public String getFirstName() { return _firstName; }
	public String getLastName() { return _lastName; }
	public String getDisplayName() { return _displayName; }
	public String getNickname() { return _nickname; }
	public String getEmail() { return _email; }
	public String getPhone() { return _phone; }
	public String getRank() { return _rank; }
	public String getLodge() { return _lodge; }
	
	public void setId(String id) { _id = id; }
	public void setFirstName(String firstName) { _firstName = firstName; }
	public void setLastName(String lastName) { _lastName = lastName; }
	public void setDisplayName(String displayName) { _displayName = displayName; }
	public void setNickname(String nickname) { _nickname = nickname; }
	public void setEmail(String email) { _email = email; }
	public void setPhone(String phone) { _phone = phone; }
	public void setRank(String rank) { _rank = rank; }
	public void setLodge(String lodge) { _lodge = lodge; }
	
}
