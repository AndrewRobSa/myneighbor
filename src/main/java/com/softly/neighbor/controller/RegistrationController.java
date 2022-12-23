package com.softly.neighbor.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.softly.neighbor.persistence.entities.Person;
import com.softly.neighbor.persistence.entities.User;
import com.softly.neighbor.service.DocumentService;
import com.softly.neighbor.service.PersonService;
import com.softly.neighbor.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RestController
public class RegistrationController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	DocumentService documentService;
	
	
	private static String DATE_FORMAT = "dd/MM/yyyy";
	private static SimpleDateFormat FORMATTER = new SimpleDateFormat(DATE_FORMAT);
	
	@Getter @Setter @NoArgsConstructor
	private static class RegistrationUser{
		
		private String firstName;
		private String lastName;
		private String email;
		private String password;
		private String confirmPassword;
		private String phoneNumber;
		private String gender;
		private String birthDate;
		private List<MultipartFile> files;
	}
	
	@PostMapping("registration")
	public ResponseEntity<?> registrate(@ModelAttribute RegistrationUser user){
		HashMap<String, Object> result = new HashMap<>();
		
		//Make the validations. None of the fields should be left blank
		if(StringUtils.isBlank(user.getFirstName()) || StringUtils.isBlank(user.getLastName()) || 
				StringUtils.isBlank(user.getEmail())|| StringUtils.isBlank(user.getPassword()) || 
				StringUtils.isBlank(user.getConfirmPassword()) || StringUtils.isBlank(user.getPhoneNumber()) || 
				StringUtils.isBlank(user.getBirthDate())) {
			result.put("response", RESULT.ERROR);
			result.put("message", "All the fields must be fullfiled.");
			return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
		} 
		
		if(user.getFiles() == null) {
			result.put("response", RESULT.ERROR);
			result.put("message", "It's mandatory to add a credential with your registration.");
			return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
		}
		
		if(this.userService.exists(user.getEmail())) {
			result.put("response", RESULT.ERROR);
			result.put("message", "El correo ingresado ya esta registrado.");
			return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
		}
		
		if(!user.getPassword().equals(user.getConfirmPassword())) {
			result.put("response", RESULT.ERROR);
			result.put("message", "Las contraseñas ingresadas no coinciden.");
			return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
		}
		
		if(!(user.getGender().equals("M") || user.getGender().equals("F"))) {
			result.put("response", RESULT.ERROR);
			result.put("message", "El genero ingresado no es valido.");
			return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
		}
		
		Date birthDate = null;
		try {
			birthDate = FORMATTER.parse(user.getBirthDate());
		}catch (ParseException e) {
			result.put("response", RESULT.ERROR);
			result.put("message", "La fecha de nacimiento ingresada no es valida.");
			return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
		}

		Long phoneNumber = null;
		try {
			if(user.getPhoneNumber().length()!=10) throw new NumberFormatException(); 
			phoneNumber = Long.parseLong(user.getPhoneNumber());
		} catch (NumberFormatException e) {
			result.put("response", RESULT.ERROR);
			result.put("message", "El numero de telefono ingresado no es valido.");
			return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
		}
		
		try {
			user.getFiles().forEach(documentService::save);
		} catch (Exception e) {
			result.put("response", RESULT.ERROR);
			result.put("message", e.getMessage());
			return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
		}
		
		Person person = new Person(user.getFirstName(), user.getLastName(), String.valueOf(phoneNumber), birthDate, user.getGender());
		String respuesta = this.personService.savePerson(person);
		
		if(respuesta!= null && respuesta.equals("ok")) {
			User dbUser = new User(user.getEmail(), user.getPassword(), person);
			respuesta = this.userService.registrate(dbUser);
			if(respuesta!= null && respuesta.equals("ok") && person.getId()!=null) {
				result.put("response", RESULT.OK);
				result.put("message", "The user was registrated correctly! You now can login with your credentials...");
				return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
				
			}else {
				result.put("response", RESULT.ERROR);
				result.put("message", "The user could not been saved, verify your email and password.");
				return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
			}
		}else {
			result.put("response", RESULT.ERROR);
			result.put("message", "The user could not been saved, verify your personal information.");
			return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
		}
	}
}
