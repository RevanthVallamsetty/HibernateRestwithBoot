package com.example.demo;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao implements IUserDao {

	@Override
	public User getUserById(int userId) {
		SessionFactory sessionFactory = HiberanateUtil.getSessionFactory();
		Session  session = sessionFactory.openSession();
		User user = session.get(User.class, userId);
		session.close();
		return user;
	}

	@Override
	public List<User> getAllUsers() {
		SessionFactory sessionFactory = HiberanateUtil.getSessionFactory();
		Session  session = sessionFactory.openSession();
		List<User> users = (List<User>) session.createQuery("from User").list();
		session.close();
		return users;
	}

	@Override
	public boolean addUser(User newUser) {
		SessionFactory sessionFactory = HiberanateUtil.getSessionFactory();
		Session  session = sessionFactory.openSession();
		session.beginTransaction();
		session.saveOrUpdate(newUser);
		session.getTransaction().commit();
		session.close();
		return true;
	}

	@Override
	public boolean updateUser(User user) {
		SessionFactory sessionFactory = HiberanateUtil.getSessionFactory();
		Session  session = sessionFactory.openSession();
		session.beginTransaction();
		session.saveOrUpdate(user);
		session.getTransaction().commit();
		session.close();
		return true;
	}

	@Override
	public boolean deleteUser(int userId) {
		SessionFactory sessionFactory = HiberanateUtil.getSessionFactory();
		Session  session = sessionFactory.openSession();
		User user = this.getUserById(userId);
		session.beginTransaction();
		session.delete(user);
		session.getTransaction().commit();
		session.close();
		return false;
	}

	@Override
	public boolean findByFirstName(String firstName) {
		SessionFactory sessionFactory = HiberanateUtil.getSessionFactory();
		Session  session = sessionFactory.openSession();
		session.beginTransaction();
		List<User> users = (List<User>) session.createQuery("select u from User u where u.firstName = '"+firstName+"'").list();
		session.getTransaction().commit();
		session.close();
		return users.isEmpty();
	}

}
