package com.example.demo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mail.MailException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.ribbon.http.HttpRequestBuilder;

@CrossOrigin(origins = "*")
@RefreshScope
@RestController
@RequestMapping("/app/users")
public class UserController {

	@Autowired
	private UserDao userDao;
	@Autowired
	private EmailService emailService;

	@Autowired
	private AuthenticationClient authenticationClient;


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
			ResponseEntity<String> response=authenticationClient.validateEmail(user.email); 
			System.out.println(response.getBody());
			if (response.getBody().equals("User is valid")) {
				userDao.addUser(user);
				try {
					emailService.sendMail(user);
				}
				catch(MailException e) {
					System.out.println(e.getMessage());
				}

			}
			else {
				return new ResponseEntity("Email is invalid", HttpStatus.UNPROCESSABLE_ENTITY);
			}
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
	
	@GetMapping(value = "/download/users.xlsx")
    public ResponseEntity<InputStreamResource> excelCustomersReport() throws IOException {
        List<User> users = (List<User>) userDao.getAllUsers();
		
		ByteArrayInputStream in = ExcelGenerator.usersToExcel(users);
		// return IOUtils.toByteArray(in);
		
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=users.xlsx");
		
		 return ResponseEntity
	                .ok()
	                .headers(headers)
	                .body(new InputStreamResource(in));
    }
	
	@PostMapping("/import")
	public void mapReapExcelDatatoDB(@RequestParam("file") MultipartFile reapExcelDataFile) throws IOException {
           
		ExcelGenerator.excelReader(reapExcelDataFile);
		
	   
	}


}
