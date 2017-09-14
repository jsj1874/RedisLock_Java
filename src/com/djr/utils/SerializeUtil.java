package com.djr.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {
	
	/**
	 * 
	 *  序列化对象
	 *
	 */
	public static <T> byte[] serialize(T obj){
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		
		byte[] bytes = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			bytes = bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bytes;
	}
	
	/**
	 * 
	 *
	 * 反序列化对象
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T unserialize(byte[] bytes){
		T obj = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		
		
		try {
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			obj = (T) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return obj;
		
	}
	
	public static void main(String[] args) {
		User user= new User(String.valueOf(1874), 22);
		byte[] bytes = SerializeUtil.serialize(user);
		System.out.println("bytes.length:"+bytes.length +" bytes.tostring:"+bytes.toString());
		user = SerializeUtil.unserialize(bytes);
		System.out.println(user.toString());
	}

}
