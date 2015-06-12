package pl.put.tpsi.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.persistence.EntityManager;

import pl.put.tpsi.data.Appointement;
import pl.put.tpsi.data.Doctor;
import pl.put.tpsi.data.Person;
import pl.put.tpsi.data.Speciality;

public class Utils {

	public static String removePolishLetters(String string) {		
		return string.toLowerCase().replaceAll("¹", "a").replaceAll("æ", "c").replaceAll("ê", "e").replaceAll("³", "l").replaceAll("ñ", "n")
				.replaceAll("ó", "o").replaceAll("œ", "s").replaceAll("[Ÿ¿]", "z");
	}

	public static String generate(EntityManager em) {
		em.getTransaction().begin();
		em.clear();
		Doctor d;
		try {
		d = new Doctor("Marek", "Ostrowski", Speciality.ORTHOPEDIST, "gdansk");
		em.persist(d);
		em.persist(new Appointement(d, ("Anna Grodzisk"), new SimpleDateFormat("yyyymmddhh").parse("2015060316")));
		em.persist(new Appointement(d, ("Jan Kowalski"), new SimpleDateFormat("yyyymmddhh").parse("2015060317")));
		em.persist(new Appointement(d, ("Kazimierz Janik"), new SimpleDateFormat("yyyymmddhh").parse("2015052110")));
		em.persist(new Appointement(d, ("Janusz Fiszer"), new SimpleDateFormat("yyyymmddhh").parse("2015052111")));
		em.persist(new Appointement(d, ("Anita Nowicka"), new SimpleDateFormat("yyyymmddhh").parse("2015052112")));
		d = new Doctor("Jaros³aw", "Ska³a", Speciality.DHERMATHOLOGIST, "gdansk");
		em.persist(d);
		em.persist(new Appointement(d, ("Anna Grodzisk"), new SimpleDateFormat("yyyymmddhh").parse("2015060415")));
		em.persist(new Appointement(d, ("Jan Kowalski"), new SimpleDateFormat("yyyymmddhh").parse("2015060912")));
		em.persist(new Appointement(d, ("Kazimierz Janik"), new SimpleDateFormat("yyyymmddhh").parse("2015051211")));
		em.persist(new Appointement(d, ("Janusz Fiszer"), new SimpleDateFormat("yyyymmddhh").parse("2015051515")));
		em.persist(new Appointement(d, ("Anita Nowicka"), new SimpleDateFormat("yyyymmddhh").parse("2015051517")));
		d = new Doctor("Marta", "Leœniewska", Speciality.DHERMATHOLOGIST, "gdansk");
		em.persist(d);
		em.persist(new Appointement(d, ("Anna Grodzisk"), new SimpleDateFormat("yyyymmddhh").parse("2015070415")));
		em.persist(new Appointement(d, ("Jan Kowalski"), new SimpleDateFormat("yyyymmddhh").parse("2015070712")));
		em.persist(new Appointement(d, ("Kazimierz Janik"), new SimpleDateFormat("yyyymmddhh").parse("2015061211")));
		em.persist(new Appointement(d, ("Janusz Fiszer"), new SimpleDateFormat("yyyymmddhh").parse("2015051606")));
		em.persist(new Appointement(d, ("Anita Nowicka"), new SimpleDateFormat("yyyymmddhh").parse("2015051607")));
		em.persist(new Doctor("Sylwia", "Gruszka", Speciality.DHERMATHOLOGIST, "poznan"));
		em.persist(new Doctor("Marian","Hasa³a", Speciality.ORTHOPEDIST, "gdansk"));
		em.persist(new Doctor("Anna", "Grochowska", Speciality.PULMUNOLOGIST, "gdnask"));
		em.persist(new Doctor("Henryk", "Kaczmarski", Speciality.PULMUNOLOGIST, "gdansk"));
		em.persist(new Doctor("Jan", "Janicki", Speciality.ORTHOPEDIST, "warszawa"));
		em.persist(new Doctor("Janusz", "Kaczmarek", Speciality.DHERMATHOLOGIST, "warszawa"));
		em.persist(new Doctor("Krystyna", "Guga³a", Speciality.DHERMATHOLOGIST, "poznan"));
		em.getTransaction().commit();
		} catch (ParseException e1) {
			e1.printStackTrace();
			em.getTransaction().rollback();
			return "Error";
		}
		
		return "done";
	}

}
