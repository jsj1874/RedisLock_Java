package com.djr.lock;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.djr.utils.RedisUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisLockUtil {
	public static String LOCK_KEY = "Lock_";
	private static Logger logger = Logger.getLogger(RedisLockUtil.class);

	private static int EXPIRED_TIME = 1;

	/**
	 * 锁在给定的等待时间内空闲，则获取锁成功 返回true， 否则返回false
	 * 
	 * @param subKey
	 * @param timeout
	 *            如果timeout=0,取不到锁时,不等待,直接返回.
	 * @param unit
	 * @return
	 */
	public static boolean tryLock(String subKey, long timeout, TimeUnit unit) {
		String key = LOCK_KEY + subKey;
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getJedis();
			if (jedis == null) {
				return Boolean.FALSE;
			}
			long nano = System.nanoTime();
			do {
				logger.debug("try lock key: " + key);
				long i = jedis.setnx(key, key);
				if (i == 1) {
					jedis.expire(key, EXPIRED_TIME);
					logger.debug("get lock, key: " + key + " , expire in " + EXPIRED_TIME + " seconds.");
					return Boolean.TRUE;
				} else { // 存在锁
					if (logger.isDebugEnabled()) {
						String desc = jedis.get(key);
						logger.debug("key: " + key + " locked by another business：" + desc);
					}
				}
				if (timeout == 0) { // 取不到锁时,不等待,直接返回.
					break;
				}
				Thread.sleep(200);
			} while ((System.nanoTime() - nano) < unit.toNanos(timeout));// 取不到锁时等待,直到timeout
			return Boolean.FALSE;
		} catch (JedisConnectionException je) {
			logger.error(je.getMessage(), je);
			RedisUtil.returnResource(jedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			RedisUtil.returnResource(jedis);
		}
		return Boolean.FALSE;
	}

	/**
	 * 如果锁空闲立即返回 获取失败 一直等待
	 * 
	 * @param subKey
	 */
	public static boolean lock(String subKey) {
		String key = LOCK_KEY + subKey;
		Jedis jedis = null;
		boolean isLock = false;
		try {
			jedis = RedisUtil.getJedis();
			if (jedis == null) {
				return isLock;
			}
			while (true) {
				logger.debug("lock key: " + key);
				Long i = jedis.setnx(key, key);
				if (i == 1) {
					jedis.expire(key, EXPIRED_TIME);
					logger.debug("get lock, key: " + key + " , expire in " + EXPIRED_TIME + " seconds.");
					isLock = true;
				} else {
					if (logger.isDebugEnabled()) {
						String desc = jedis.get(key);
						logger.debug("key: " + key + " locked by another business：" + desc);
					}
					isLock = false;
				}
				Thread.sleep(500);
			}
		} catch (JedisConnectionException je) {
			logger.error(je.getMessage(), je);
			RedisUtil.returnResource(jedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			RedisUtil.returnResource(jedis);
		}
		return isLock;
	}

	/**
	 * 释放锁
	 * 
	 * @param subKey
	 */
	public static void unLock(String subKey) {
		String key = LOCK_KEY + subKey;
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getJedis();
			if (jedis == null) {
				return;
			}
			jedis.del(key);
			logger.debug("release lock, keys :" + key);
		} catch (JedisConnectionException je) {
			logger.error(je.getMessage(), je);
			RedisUtil.returnResource(jedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}
	
	public static void main(String[] args) {

		
		RedisUtil.set("money", "10");
		Runnable runnable =() ->{
			while (true) {
				
				if (tryLock("money", 1000, TimeUnit.MICROSECONDS)) {
					long val = RedisUtil.decr("money");
					System.out.println("current val:" + val);
					System.out.println("thread:"+Thread.currentThread().getName()+" have lock");

				} else {

					try {
						Thread.currentThread().sleep(500L);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				if (Integer.valueOf(RedisUtil.get("money")) <= 0) {
					break;
				}
			}
		};

	
		new Thread(runnable).start();
		new Thread(runnable).start();
		new Thread(runnable).start();
		new Thread(runnable).start();
	}

}
