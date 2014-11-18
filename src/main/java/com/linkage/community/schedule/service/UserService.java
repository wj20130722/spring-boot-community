package com.linkage.community.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkage.community.schedule.dbutils.CommonDBTemplate;

@Service
public class UserService {
	
	@Autowired
	private CommonDBTemplate commonDBTemplate;
	
	
	//@Transactional
	public void findAll()
	{
		try
		{
			commonDBTemplate.beginTransaction();
			long id = commonDBTemplate.insertAndReturnPK("insert into user (name,age,sex) values (?,?,?)", new Object[]{"xiaojie",25,1});
			System.out.println(id);
			//throw new RuntimeException("this is a exception!");
			commonDBTemplate.commitTransaction();
		}
		catch(Exception e)
		{
			commonDBTemplate.rollbackTransaction();
			e.printStackTrace();
		}
		finally
		{
			commonDBTemplate.recoverTransction();
			commonDBTemplate.closeConnection();
		}
		
		//userDao.save(new User("xiaojun", 10, 10));
		//userDao.save(new User("xiaoqiang", 20, 10));
		//return userDao.findAll();
	}
}
