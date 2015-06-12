package pl.put.tpsi.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import pl.put.tpsi.data.MockedDatebaseConnection.Sequencer;
import pl.put.tpsi.utils.Utils;

@Entity
public class Doctor implements DatebaseObject {
	
	@GeneratedValue
	private long id;
	
	@GeneratedValue
	private long patientId;
	private String firstName;
	private String secondName;	
	private String lastName;
	private List<Opinion> opinions;
	private Speciality speciality;
	private String city;
	
	public Doctor(String firstName, String lastName, Speciality speciality, String city) {
		this(firstName, null, lastName, speciality, city);
	}
	
	public Doctor(String firstName, String secondName, String lastName) {
		super();
		this.firstName = firstName;
		this.secondName = secondName;
		this.lastName = lastName;
	}

	public Doctor(String firstName, String secondName, String lastName, Speciality speciality, String city) {		
		this(firstName, secondName, lastName);
		opinions = Collections.emptyList();
		this.speciality = speciality;
		this.city = city;
	}

	public Speciality getSpeciality() {
		return speciality;
	}

	public void setSpeciality(Speciality speciality) {
		this.speciality = speciality;
	}

	public List<Opinion> getOpinions() {
		List<Opinion> result = new ArrayList<>();
		Collections.copy(result, this.opinions);
		return result;
	}
		
	public void addOpinion(Opinion o) {
		opinions.add(o);
	}

	@Override
	public void create(MockedDatebaseConnection conn) {
		conn.add(this);
		
	}

	@Override
	public DatebaseObject read(MockedDatebaseConnection conn) {		
		return conn.read(this);
	}

	@Override
	public DatebaseObject update(MockedDatebaseConnection conn,
			DatebaseObject newValue) {
		return conn.update(this, newValue);
	}

	@Override
	public DatebaseObject delete(MockedDatebaseConnection conn) {
		return conn.delete(this);
	}
	
	/**
	 * 
	 * @return city = Doctor has to have city so it must not return null
	 */
	public String getCity() {
		return Utils.removePolishLetters(city).toLowerCase();
	}
	
	public long getId() {
		return id;
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
