package pl.put.tpsi.data;

import java.text.DateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import pl.put.tpsi.data.MockedDatebaseConnection.Sequencer;
@Entity
public class Appointement implements DatebaseObject {
	
	@GeneratedValue
	private long id;
	private Doctor doctor;
	private String patient;
	private Date date;
	
	
	public Appointement(Doctor doctor, String patient, Date date) {
		super();
		this.doctor = doctor;
		this.patient = patient;
		this.date = date;
	}


	public Doctor getDoctor() {
		return doctor;
	}


	public String getPatient() {
		return patient;
	}

	public String getDateString() {
		return DateFormat.getDateTimeInstance(DateFormat.FULL,
				DateFormat.MEDIUM).format(date);
	}
	
	public Date getDate() {
		return date;
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
	
	public long getId() {
		return id;
	}
	
}
