package com.example.redis.dao;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.redis.bean.User;



/**  
* <p>Title: UserDao.java</p>  
* <p>Description:DAO 数据处理类 </p>  
* <p>Copyright: Copyright (c) 2018</p>  
* @author Xiao Nong
* @date 2018年11月4日  
* @version 1.0  
*/  
@Transactional
public interface UserDao extends JpaRepository<User, Long> {
	/**
	 * 通过用户名称查询User对象
	 * @param userName
	 * @return
	 */
	User findByUserName(String userName);
	
	
	@Modifying  
	@Query(value = "update user set age = :age where id = :id",nativeQuery = true)  
    void updateNameById(@Param("id") Long id, @Param("age") Integer age);
 
}
