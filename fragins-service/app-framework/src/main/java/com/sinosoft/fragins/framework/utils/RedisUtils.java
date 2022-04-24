package com.sinosoft.fragins.framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis简单服务的静态包装
 */
@Slf4j
public class RedisUtils {

	private static RedisTemplate<String, Object> redisTemplate;

	/**
	 * 获取缓存值
	 *
	 * @param key 缓存键
	 * @return 缓存值
	 */
	public static Object get(String key) {
		Object o = null;
		try {
			o = redisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			log.info("redis获取数据异常");
		}
		return o;
	}

	/**
	 * 获取缓存值
	 *
	 * @param key 缓存键
	 * @return 缓存值
	 */
	public static <T> T get(String key, Class<T> tClass) {
		String o = null;
		try {
			o = (String) redisTemplate.opsForValue().get(key);
			if (o == null) {
				return null;
			}
			return JsonUtils.parse(o, tClass);
		} catch (Exception e) {
			log.info("redis获取数据异常");
		}
		return null;
	}

	/**
	 * 将key对应的value增加制定的值
	 *
	 * @param key   键
	 * @param delta 增量
	 */
	public static void increment(String key, Long delta) {
		try {
			redisTemplate.opsForValue().increment(key, delta);
		} catch (Exception e) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * 设置重复请求锁
	 *
	 * @param key     缓存键
	 * @param timeout 过期时间，单位分钟
	 * @return 是否成功获取锁
	 */
	public static Boolean lockMinutes(String key, long timeout) {
		return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key,
				"locked", timeout, TimeUnit.MINUTES));
	}

	/**
	 * 设置重复请求锁
	 *
	 * @param key     缓存键
	 * @param timeout 过期时间，单位秒
	 * @return 是否成功获取锁
	 */
	public static Boolean lockSecond(String key, long timeout) {
		return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key,
				"locked", timeout, TimeUnit.SECONDS));
	}

	/**
	 * 解锁
	 */
	public static void unlock(String key) {
		redisTemplate.delete(key);
	}

	/**
	 * 获取缓存自动过期时间
	 * 
	 * @param key 缓存键
	 * @return 剩余毫秒数，另外-2表示不存在，-1表示永远不过期
	 */
	public static Long getExpire(String key) {
		return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
	}

	/**
	 * 设置缓存值
	 * 
	 * @param key   缓存键
	 * @param value 缓存值
	 */
	public static void set(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 设置缓存值，带自动失效时间
	 * 
	 * @param key    缓存键
	 * @param value  缓存值
	 * @param expire 自动失效的毫秒数
	 */
	public static void set(String key, Object value, long expire) {
		redisTemplate.opsForValue().set(key, value, expire, TimeUnit.MILLISECONDS);
	}

	/**
	 * 如果key不存在，设置缓存值，带自动失效时间
	 * 
	 * @param key    缓存键
	 * @param value  缓存值
	 * @param expire 自动失效的毫秒数
	 * @return 是否设置成功
	 */
	@SuppressWarnings("unchecked")
	public static boolean setIfAbsent(final String key, final Object value, final long expire) {
		RedisSerializer<Object> keySerializer = (RedisSerializer<Object>) redisTemplate.getKeySerializer();
		RedisSerializer<Object> valueSerializer = (RedisSerializer<Object>) redisTemplate.getValueSerializer();
		byte[] rawKey = keySerializer.serialize(key);
		byte[] rawValue = valueSerializer.serialize(value);
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				connection.set(rawKey, rawValue, Expiration.milliseconds(expire), SetOption.SET_IF_ABSENT);
				byte[] rawValue2 = connection.get(rawKey);
				return Arrays.equals(rawValue, rawValue2);
			}
		});
	}

	/**
	 * 删除缓存
	 * 
	 * @param key 缓存键
	 */
	public static void delete(String key) {
		redisTemplate.delete(key);
	}

	/**
	 * 自增
	 *
	 * @param key        缓存键
	 */
	public static Long getAndIncrement(String key) {
		return redisTemplate.opsForValue().increment(key);
	}

	/**
	 * 自增
	 * 
	 * @param key        缓存键
	 * @param startValue 起始值
	 * @param increment  增加值
	 * @param expire     自动失效的毫秒数
	 */
	public static long getAndIncrement(String key, long startValue, long increment, long expire) {
		RedisSerializer<Object> keySerializer = (RedisSerializer<Object>) redisTemplate.getKeySerializer();
		byte[] rawKey = keySerializer.serialize(key);
		byte[] rawValue = new StringRedisSerializer().serialize("" + (startValue - 1));
		redisTemplate.execute(new RedisCallback<Void>() {
			@Override
			public Void doInRedis(RedisConnection connection) throws DataAccessException {
				connection.set(rawKey, rawValue, Expiration.milliseconds(expire), SetOption.SET_IF_ABSENT);
				return null;
			}
		});
		return redisTemplate.opsForValue().increment(key, increment);
	}

	public static byte[] getRawValue(String key) {
		RedisSerializer<Object> keySerializer = (RedisSerializer<Object>) redisTemplate.getKeySerializer();
		byte[] rawKey = keySerializer.serialize(key);
		byte[] rawValue = redisTemplate.execute(new RedisCallback<byte[]>() {
			@Override
			public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] value = connection.get(rawKey);
				return value;
			}
		});
		return rawValue;
	}

	public static String getRawValueAsString(String key) {
		byte[] rawValue = getRawValue(key);
		if (rawValue == null) {
			return null;
		} else {
			try {
				return new String(rawValue, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 获取一个同步锁，如果已经被锁住则进行等待
	 * 
	 * @param key    同步锁的缓存键
	 * @param expire 同步锁自动失效的毫秒数
	 * @return 返回RedisLock对象，包含unlock方法用于手动解锁。
	 */
	public static RedisLock lockWait(String key, long expire) {
		return lockWait(key, expire, 0);
	}

	/**
	 * 获取一个同步锁，如果已经被锁住则进行等待
	 *
	 * @param key         同步锁的缓存键
	 * @param expire      同步锁自动失效的毫秒数
	 * @param maxWaitTime 最长等待时间，如果设置为0或者负数则一直等待
	 * @return 如果获取成功，返回RedisLock对象，包含unlock方法用于手动解锁。如果等待锁超时，返回null
	 */
	public static RedisLock lockWait(String key, long expire, long maxWaitTime) {
		if (expire <= 0) {
			throw new RuntimeException("Redis同步锁自动失效时间(expire)必须为正数");
		}
		String value = RandomGenerator.randomString(8);
		long beginTime = System.currentTimeMillis();
		boolean success = setIfAbsent(key, value, expire);
		while (!success) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				break;
			}
			success = setIfAbsent(key, value, expire);
			if (maxWaitTime > 0 && System.currentTimeMillis() - beginTime > maxWaitTime) {
				break;
			}
		}
		return success ? new RedisLock(key, value) : null;
	}

	/**
	 * 获取一个同步锁，如果已经被锁住不等待直接跳过
	 *
	 * @param key         同步锁的缓存键
	 * @param expire      同步锁自动失效的毫秒数
	 * @param maxWaitTime 最长等待时间，如果设置为0或者负数则一直等待
	 * @return 如果获取成功，返回RedisLock对象，包含unlock方法用于手动解锁。如果等待锁超时，返回null
	 */
	public static RedisLock lockWaitNoWait(String key, long expire) {
		if (expire <= 0) {
			throw new RuntimeException("Redis同步锁自动失效时间(expire)必须为正数");
		}
		String value = RandomGenerator.randomString(8);
		long beginTime = System.currentTimeMillis();
		return setIfAbsent(key, value, expire) ? new RedisLock(key, value) : null;
	}

	/**
	 * 获取一个同步锁，如果已经被锁住则直接返回false
	 * 
	 * @param key    同步锁的缓存键
	 * @param expire 同步锁自动失效的毫秒数
	 * @return 如果获取成功，返回RedisLock对象，包含unlock方法用于手动解锁。如果获取失败，返回null
	 */
	public static RedisLock lockNoWait(String key, long expire) {
		if (expire <= 0) {
			throw new RuntimeException("Redis同步锁自动失效时间(expire)必须为正数");
		}
		String value = RandomGenerator.randomString(8);
		boolean success = setIfAbsent(key, value, expire);
		return success ? new RedisLock(key, value) : null;
	}

	/**
	 * 解除同步锁
	 * 
	 * @param lock 锁定时返回的对象
	 */
	public static void unlock(RedisLock lock) {
		lock.unlock();
	}

	@Autowired
	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		RedisUtils.redisTemplate = redisTemplate;
	}

	/**
	 * Redis同步锁对象，包含解锁的方法
	 */
	public static class RedisLock {

		private String key;
		private String value;

		private RedisLock(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public void unlock() {
			if (value.equals(get(key))) {
				delete(key);
			}
		}

	}


	/**
	 * 集合设置缓存值
	 * @param key    缓存键
	 * @param values  缓存值
	 */
	public static void sadd(String key, Object values) {
		redisTemplate.opsForSet().add(key, values);
	}

	public static void union(String key1, String key2,String key3) {
		redisTemplate.opsForSet().intersectAndStore(key1, key2, key3);
	}

	public static void sdiffstore(String key1, String key2,String key3) {
		redisTemplate.opsForSet().differenceAndStore(key1, key2, key3);
	}

	public static Set<Object> smembers(String key) {
		Set<Object>  values = redisTemplate.opsForSet().members(key);
		return values;
	}

	public static void remove(String key) {
		try {
			redisTemplate.delete(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 自增
	 *
	 * @param key        缓存键
	 * @param startValue 起始值
	 * @param increment  增加值
	 * @param expire     自动失效的毫秒数
	 */
	public static BigDecimal getAndIncrementBigDouble(String key, long startValue, BigDecimal increment, long expire) {

		Long beforeIncrement = increment.movePointRight(6).longValue();

		RedisSerializer<Object> keySerializer = (RedisSerializer<Object>) redisTemplate.getKeySerializer();
		byte[] rawKey = keySerializer.serialize(key);
		byte[] rawValue = new StringRedisSerializer().serialize("" + (startValue));
		redisTemplate.execute(new RedisCallback<Void>() {
			@Override
			public Void doInRedis(RedisConnection connection) throws DataAccessException {
				connection.set(rawKey, rawValue, Expiration.milliseconds(expire), SetOption.SET_IF_ABSENT);
				return null;
			}
		});
		return new BigDecimal(String.valueOf(redisTemplate.opsForValue().increment(key, beforeIncrement))).movePointLeft(6);
	}

	/**
	 * 模糊匹配前缀
	 * @param prefixKey
	 * @return
	 */
	public static Set<String> getCheckedPremiumByPrefixKey(String prefixKey){
		Set<String> keys = null;
		if (StringUtils.isNotBlank(prefixKey)) {
			keys = redisTemplate.keys(prefixKey);
		}
		return keys;
	}

	public static void addForList(String key, Object values) {
		redisTemplate.opsForList().rightPush(key, values);
	}

	public static List<Object> getList(String key) {
		List<Object> result = new ArrayList<>();
		ListOperations<String, Object>	operations = redisTemplate.opsForList();
			Long size = operations.size(key);
			for(int i=0; i<size; i++) {
				result.add(operations.index(key, i));
			}
		return result;
	}



}
