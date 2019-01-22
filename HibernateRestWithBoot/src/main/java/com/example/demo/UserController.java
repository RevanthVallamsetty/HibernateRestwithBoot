package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/app/users")
public class UserController {

@Autowired
private UserDao userDao;

@RequestMapping(method= RequestMethod.GET)
public Iterable list(Model model){
    Iterable userList = userDao.getAllUsers();
    return userList;
}

@RequestMapping(value = "/{id}", method= RequestMethod.GET)
public User showUser(@PathVariable Integer id, Model model){
   User user = userDao.getUserById(id);
    return user;
}

@RequestMapping(method = RequestMethod.POST)
public ResponseEntity saveProduct(@RequestBody User user){
	if (userDao.findByFirstName(user.firstName))
	{
    userDao.addUser(user);
    return new ResponseEntity("User saved successfully", HttpStatus.OK);
	}
	else
	{
		return new ResponseEntity("User already exists", HttpStatus.UNPROCESSABLE_ENTITY);
	}
}

@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
public ResponseEntity updateUser(@PathVariable Integer id, @RequestBody User user){
    User user1 = userDao.getUserById(id);
    user1.setFirstName(user.firstName);
    user1.setLastName(user.lastName);
    user1.setEmail(user.email);
    user1.setPhoneNo(user.phoneNo);
    user.setRole(user.role);
    userDao.updateUser(user1);
    return new ResponseEntity("User updated successfully", HttpStatus.OK); 
}

@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
public ResponseEntity delete(@PathVariable Integer id){
    userDao.deleteUser(id);
    return new ResponseEntity("User deleted successfully", HttpStatus.OK);

}


}
