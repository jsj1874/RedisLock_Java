package com.djr.utils;

import java.io.Serializable;

import javax.print.attribute.standard.RequestingUserName;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int age;
	
	private String name;
	
	public User(String name,int age){
		this.name = name;
		this.age = age;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(this == obj){
			return true;
		}
		
		if(! (obj instanceof User)){
			return false;
		}
		User user = (User) obj;
		return user.name == this.name && user.age == this.age;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		int result = 17;
		result = result * 31 +result;
		result = result + name.hashCode();
		return result;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "user.name:" + name +" user.age:"+age;
	}
	
	
	
	

}
