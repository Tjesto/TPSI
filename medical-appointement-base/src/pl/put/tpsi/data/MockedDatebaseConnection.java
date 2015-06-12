package pl.put.tpsi.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.put.tpsi.utils.Utils;

public final class MockedDatebaseConnection {
	
	public static class Sequencer {	
		private final List<Long> doctorsIds = new ArrayList<>();
		private final List<Long> appointementsIds = new ArrayList<>();
		
		private Sequencer() {
			for (long l = 0; l < Byte.MAX_VALUE; l++) {
				doctorsIds.add(l);
				appointementsIds.add(l);
			}
		}
		
		private static Sequencer instance;
		
		public static Sequencer getInstance() {
			if (instance == null) {
				instance = new Sequencer();
			}
			return instance;
		}
		
		public long getNextId(Class<?> c) {
			if (c.equals(Doctor.class)) {
				return doctorsIds.remove(0);
			} else if (c.equals(Appointement.class)) {
				return appointementsIds.remove(0);
			}
			return -1;
		}
	}

	private final List<Doctor> doctors;
	
	private final List<Appointement> appointements;	
	
	private MockedDatebaseConnection() {
		doctors = new ArrayList<>();
		appointements = new ArrayList<>();
	}
	
	public static MockedDatebaseConnection create() {
		Sequencer.getInstance();
		MockedDatebaseConnection connection = new MockedDatebaseConnection();
		connection.initialize();
		return connection;
	}
	
	public void add(Doctor doctor) {		
		doctors.add(doctor);
	}
	
	public void add(Appointement app) {
		appointements.add(app);
	}
	
	public void create(DatebaseObject object) {
		object.create(this);
	}
	
	public void read(DatebaseObject object) {
		object.read(this);
	}
	
	public void update(DatebaseObject object, DatebaseObject newObject) {
		if (!object.getClass().equals((newObject.getClass()))) {
			throw new IllegalArgumentException("Value not allowed");
		}
		object.update(this, newObject);
	}
	
	public void delete(DatebaseObject object) {
		object.delete(this);
	}
	
	public DatebaseObject read(Doctor doctor) {
		int doctorIndex = doctors.indexOf(doctor);
		return doctors.get(doctorIndex);
	}
	
	public DatebaseObject read(Appointement app) {
		int i = appointements.indexOf(app);
		return appointements.get(i);
	}
	
	public DatebaseObject update(Doctor doctor, DatebaseObject newDoctor) {
		int doctorIndex = doctors.indexOf(doctor);
		doctors.remove(doctor);
		doctors.add(doctorIndex, (Doctor) newDoctor);
		return doctors.get(doctorIndex);
	}
	
	public DatebaseObject update(Appointement app, DatebaseObject newApp) {
		int index = appointements.indexOf(app);
		appointements.remove(app);
		appointements.add(index, (Appointement) newApp);
		return appointements.get(index);
	}
	
	public DatebaseObject delete(Doctor doctor) {
		int index = doctors.indexOf(doctor);		
		return doctors.remove(index);
	}
	
	public DatebaseObject delete(Appointement app) {
		int index = appointements.indexOf(app);
		return appointements.remove(index);
	}
	
	public Map<Speciality, List<Doctor>> getAllDoctorsInCity(String city) {
		final Map<Speciality, List<Doctor>> result = new HashMap<Speciality, List<Doctor>>();
		for (Speciality s : Speciality.values()) {
			result.put(s, new ArrayList<>());
		}
		for (Doctor d : doctors) {
			if (Utils.removePolishLetters(d.getCity()).toLowerCase().equals(city.toLowerCase())) {
				result.get(d.getSpeciality()).add(d);
			}
		}
		return result;
	}
	
	public List<Doctor> getAllSpecialistInCity(Speciality speciality, String city) {
		final List<Doctor> result = new ArrayList<>();
		for (Doctor d : doctors) {
			if (Utils.removePolishLetters(d.getCity()).toLowerCase().equals(city.toLowerCase()) && d.getSpeciality().equals(speciality)) {
				result.add(d);
			}
		}
		return result;
	}
	
	public List<Appointement> getAllAppointemenstTo(Doctor d) {
		final List<Appointement> result = new ArrayList<>();
		for (Appointement a : appointements) {
			if (a.getDoctor().equals(d)) {
				result.add(a);
			}
		}
		
		return result;
	}
	
	public boolean makeAnAppointementTo(Doctor d, Date date, Person p) {
		create(new Appointement(d, p.getFirstName() + " " + p.getLastName(), date));
		return true;
	}
	
	public void addOpinionOf(Doctor d, Opinion o) {
		d.addOpinion(o);
	}
	
	public void resignFrom(Appointement a) {
		delete(a);
	}
	
	private void initialize() {
		create(new Doctor("Jan", "Nowak", Speciality.PULMUNOLOGIST, "Poznañ"));
		create(new Doctor("Piotr", "Wakulski", Speciality.PULMUNOLOGIST, "Warszawa"));
		create(new Doctor("Renata", "Burger", Speciality.PULMUNOLOGIST, "Poznañ"));
		create(new Doctor("Jerzy", "Nowaczek", Speciality.ORTHOPEDIST, "Poznañ"));
		create(new Doctor("Juliusz", "Marek", Speciality.ORTHOPEDIST, "Poznañ"));
		create(new Doctor("Juliusz", "Marek", Speciality.ORTHOPEDIST, "Poznañ"));
	}
	
	public Doctor getDoctor(Speciality speciality, String city, long id) {
		for (Doctor d : getAllSpecialistInCity(speciality, city)) {
			if (d.getId() == id) {
				return d;
			}
		}
		return null;
	}

	public Appointement getAllAppointemenstTo(Doctor doctor, Date from) {
		for (Appointement a : getAllAppointemenstTo(doctor)) {
			if (a.getDate().getDate() == from.getDate() && a.getDate().getMonth() == from.getMonth() && a.getDate().getYear() == from.getYear() && a.getDate().getHours() == from.getHours()) {
				return a;
			}
		}
		return null;
	}
	
}
