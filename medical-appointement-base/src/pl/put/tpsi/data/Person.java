package pl.put.tpsi.data;

import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
public class Person {
	
	@GeneratedValue
	private long patientId;
	
	private String firstName;
	private String secondName;	
	private String lastName;	

	public Person(String firstName, String lastName) {
		this(firstName, null, lastName);
	}

	public Person(String firstName, String secondName, String lastName) {
		this.firstName = firstName;
		this.secondName = secondName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public String getLastName() {
		return lastName;
	}

}