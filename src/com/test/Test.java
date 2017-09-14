package com.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.djr.utils.RedisUtil;

public class Test {
	public static void main(String[] args) {
		Map<String,String> msg = new HashMap<>();
		msg.put("id", "18");
		msg.put("type", "1");
		msg.put("num", "2");
		msg.put("endTime", "2018-8-15 23:45:30");
		msg.put("startTime", "2017-8-15 23:45:30");
		msg.put("title", "你好");
		msg.put("content", "127.0.0.1");
		msg.put("channel","10000");
		msg.put("serverid","5001");
		msg.put("opername", "admin");
		msg.put("operid", "16");
		msg.put("isdel", "0");
		msg.put("contenType", "1");
		String result = RedisUtil.hmset("active.id.18", msg);
		System.out.println(result);
		/*StringBuilder sb = new StringBuilder(200);
		msg.forEach((key,val)->{
			sb.append(key).append(" ").append(val).append(" ");
		});
		System.out.println(sb.toString());*/
	}
}
