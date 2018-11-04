package com.example.redis.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.example.redis.bean.User;
import com.example.redis.dao.UserDao;
import com.example.redis.service.UserService;
import com.example.redis.util.JsonUtils;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDao userDao;
	/**
	 * Redis缓存工具类
	 */
	@Autowired
	private RedisTemplate<String,String> redisTemplate;

	@Override
	public User findUserByName(String userName) {
		return userDao.findByUserName(userName);
	}

	/**
	 * 获取用户逻辑： 如果缓存存在，从缓存中获取用户信息 如果缓存不存在，从 DB 中获取用户信息，然后插入缓存
	 */
	@Override
	public User findUserById(Long id) {
		User user = null;
		String key = "user_key_" + id;
		ValueOperations<String,String> ops = redisTemplate.opsForValue();
		Boolean flag = redisTemplate.hasKey(key);
		// 缓存存在
		if (flag) {
			Object object = ops.get(key);
			user = JsonUtils.json2Object(object.toString(), User.class);
		} else {
			// 缓存不存在，查询数据库，并把信息存进缓存里
			Optional<User> optionalUser = userDao.findById(id);
			if (null != optionalUser) {
				user = optionalUser.get();
				ops.set(key, JsonUtils.object2Json(user));
			}
		}
		return user;
	}

	/**
	 * 保存用户信息
	 */
	@Override
	public User saveUser(User user) {
		return userDao.save(user);
	}

	/**
	 * 更新用户逻辑： 如果缓存存在，删除 如果缓存不存在，不操作
	 */
	@Override
	public void updateUser(User user) {
		userDao.updateNameById(user.getId(),user.getAge());
		String key = "user_key_" + user.getId();
		// 判断是否存在key
		boolean haskey = redisTemplate.hasKey(key);
		if (haskey) {
			redisTemplate.delete(key);
			logger.info("UserServiceImpl.updateUser() : 从缓存中删除用户 >> " + user.toString());
		}
	}
	@Override
	public void deleteUser(Long id) {
		userDao.deleteById(id);
		// 缓存存在，删除缓存
		String key = "user_key_" + id;
		boolean hasKey = redisTemplate.hasKey(key);
		if (hasKey) {
			redisTemplate.delete(key);
			logger.info("UserServiceImpl.updateUser() : 从缓存中删除用户id >> " + id);
		}
	}
}
