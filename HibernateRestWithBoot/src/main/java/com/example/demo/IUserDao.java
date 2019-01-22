package com.example.demo;

import java.util.List;

public interface IUserDao {

 public User getUserById(int userId);
 public List<User> getAllUsers();
 public boolean addUser(User newUser);
 public boolean updateUser(User user);
 public boolean deleteUser(int userId);
 public boolean findByFirstName(String firstName);
 

}
