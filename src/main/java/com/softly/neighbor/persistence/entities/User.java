package com.softly.neighbor.persistence.entities;

import java.util.Date;
import java.util.Set;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "personId", referencedColumnName="id", insertable = true, updatable = true)
	@Fetch(FetchMode.JOIN)
	private Person person;
	
	@Column 
	private String email;

	@Column
	private String password;

	@Column
	private boolean enabled;
	
	@Column
	private Date creationDate;
	
	@Column 
	private Date lastLogin;
	
	@Column
	private String clientRegistration;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name="authorities_users",
		joinColumns=@JoinColumn(name="usuario_id"),
		inverseJoinColumns=@JoinColumn(name="authority_id"))
	private Set<Authority> authority;

	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((id == null) ? 0 : id.hashCode());
	    return result;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null) return false;
	    if (getClass() != obj.getClass())
	        return false;
	    User other = (User) obj;
	    if (id == null) {
	        if (other.id != null) return false; 
	    } else {
	    	if (!id.equals(other.id)) return false;
	    }
	    return true;
	}

	@Override
	public String toString() {
	    return "User [id=" + id + ", email=" + email + "]";
	}

	/**
	 * This method is invoked in the registration (creation) of a new User
	 * @param username
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param password
	 * @param phoneNumber
	 * @param birthDate
	 */
	public User(String email, String password, Person person) {
		this.email = email;
		this.password = password;
		this.creationDate = new Date();
		this.lastLogin = this.creationDate;
		this.enabled = true;
		this.person = person;
	}
	
	/**
	 * This method is invoked in creation of a new user through a third party (OAuth)
	 * @param email
	 * @param clientRegistration
	 */
	public User(String email, String clientRegistration) {
		this.clientRegistration = clientRegistration;
		this.email = email;
		this.enabled = true;
	}
}
