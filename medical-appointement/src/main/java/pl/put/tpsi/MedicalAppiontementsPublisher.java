package pl.put.tpsi;

import static spark.Spark.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.google.gson.Gson;

import pl.put.tpsi.data.Appointement;
import pl.put.tpsi.data.Doctor;
import pl.put.tpsi.data.MockedDatebaseConnection;
import pl.put.tpsi.data.Opinion;
import pl.put.tpsi.data.Person;
import pl.put.tpsi.data.Speciality;
import pl.put.tpsi.utils.Utils;
import spark.ResponseTransformer;

public class MedicalAppiontementsPublisher {	
	
	private static EntityManagerFactory factory;
	private static EntityManager em;
	
	private static boolean generated = true;


	private static class JSONTransformer implements ResponseTransformer {
		Gson gson = new Gson();

		@Override
		public String render(Object model) throws Exception {
			return gson.toJson(model);
		}
	}
	
	
	public static void main(String[] args) {
		MockedDatebaseConnection connection = MockedDatebaseConnection.create();
		
		factory = Persistence.createEntityManagerFactory("$objectdb/db/points.odb");
		em = factory.createEntityManager();
		
		get("/test", (req, res) -> "This is the test of spark working");	
		
		get("/gen", (req,res) -> {
			return generated ? "Already generated" : Utils.generate(em);
		});
		
		post("/appointements/*/*/*/*", (req, res) -> {			
			System.out.println("post appointement");
			String result = "Done";
			Map<String, String> attr = getAttrs(req.body());
			int argsNum = req.splat().length;
			if (argsNum == 0) {
				res.status(404);
				res.body("Choose city");			
			} else if (argsNum == 4) {
				res.status(200);
				em.getTransaction().begin();
				Query q = em.createQuery("SELECT d FROM Doctor d where d.id = :id and d.city = :city and d.speciality = :speciality", Doctor.class);
				System.out.println(req.splat()[0]);
				q.setParameter("city", req.splat()[0]);
				q.setParameter("id", Long.parseLong(req.splat()[2]));
				q.setParameter("speciality", Speciality.getByName(req.splat()[1]));				
				Query q2 = em.createQuery("SELECT a FROM Appointement a where a.doctor = :doctor and a.date = :date", Appointement.class);
				Doctor d = (Doctor) q.getSingleResult();
				q2.setParameter("doctor", d);
				q2.setParameter("date", new SimpleDateFormat("yyyymmddhh").parse(req.splat()[3]));
				Appointement a = null;
				try {
					a = (Appointement) q2.getSingleResult();
					System.out.println(a);
					em.getTransaction().commit();
				} catch (Exception e) {
					em.persist(new Appointement(d, attr.get("name") + " " + attr.get("surname"), new SimpleDateFormat("yyyymmddhh").parse(req.splat()[3])));
					em.getTransaction().commit();
					return "Added";
				}
				em.getTransaction().begin();
				try {
					System.out.println(a.getPatient().equals(attr.get("name") + " " + attr.get("surname")));					
					System.err.println(attr.get("name") + " " + attr.get("surname"));
					if (("remove".equals(attr.get("action")) || a != null) && a.getPatient().equals(attr.get("name") + " " + attr.get("surname"))) {
						em.remove(a);
						result = "Removed";
					} else {
						result = "ALredy exists";
					}
				em.getTransaction().commit();
				} catch (Exception e) {
					em.getTransaction().rollback();
				}
				return result;
			}
			return "B³¹d";
			
		}, new JSONTransformer());
		
		get("/appointements/*/*/*/*", (req, res) -> {			
			System.out.println("get appointement");
			int argsNum = req.splat().length;
			if (argsNum == 0) {
				res.status(404);
				res.body("Choose city");			
			} else if (argsNum == 4) {
				res.status(200);
				em.getTransaction().begin();
				Query q = em.createQuery("SELECT d FROM Doctor d where d.id = :id and d.city = :city and d.speciality = :speciality", Doctor.class);
				System.out.println(req.splat()[0]);
				q.setParameter("city", req.splat()[0]);
				q.setParameter("id", Long.parseLong(req.splat()[2]));
				q.setParameter("speciality", Speciality.getByName(req.splat()[1]));				
				Query q2 = em.createQuery("SELECT a FROM Appointement a where a.doctor = :doctor and a.date = :date", Appointement.class);
				q2.setParameter("doctor", (Doctor) q.getSingleResult());
				q2.setParameter("date", new SimpleDateFormat("yyyymmddhh").parse(req.splat()[3]));
				Object ret = q2.getResultList();
				em.getTransaction().commit();
				return ret;
			}
			return "B³¹d";
			
		}, new JSONTransformer());
		get("/appointements/*/*/*", (req, res) -> {
			System.out.println("get appointements");
			int argsNum = req.splat().length;
			if (argsNum == 0) {
				res.status(404);
				res.body("Choose city");			
			} else if (argsNum == 3) {
				em.getTransaction().begin();
				Query q = em.createQuery("SELECT d FROM Doctor d where d.id = :id and d.city = :city and d.speciality = :speciality", Doctor.class);
				System.out.println(req.splat()[0]);
				q.setParameter("city", req.splat()[0]);
				q.setParameter("id", Long.parseLong(req.splat()[2]));
				q.setParameter("speciality", Speciality.getByName(req.splat()[1]));				
				Query q2 = em.createQuery("SELECT a FROM Appointement a where a.doctor = :doctor", Appointement.class);
				q2.setParameter("doctor", (Doctor) q.getSingleResult());
				Object ret = q2.getResultList();
				em.getTransaction().commit();
				return ret;
			}
			return "B³¹d";
			
		}, new JSONTransformer());
		
		post("/doctors/*/*/*", (req, res) -> {
			Map<String, String> attr = getAttrs(req.body());
			int argsNum = req.splat().length;
			if (argsNum == 0) {
				res.status(404);
				res.body("Choose city");			
			} else if (argsNum == 3) {
				res.status(200);
				em.getTransaction().begin();
				Query q = em.createQuery("SELECT d FROM Doctor d where d.id = :id and d.city = :city and d.speciality = :speciality", Doctor.class);
				System.out.println(req.splat()[0]);
				q.setParameter("city", req.splat()[0]);
				q.setParameter("id", Long.parseLong(req.splat()[2]));
				q.setParameter("speciality", Speciality.getByName(req.splat()[1]));
				Doctor o = (Doctor) q.getSingleResult();
				String opinion = attr.get("comment").toString();
				Byte rate = (byte) Math.max(Math.min(Integer.parseInt(attr.get("rate") != null ? attr.get("rate").toString() : "0"), 10), 0);				
				o.addOpinion(new Opinion(attr.get("name") + " " + attr.get("surname"), rate, opinion, new Date(System.currentTimeMillis())));
				em.getTransaction().commit();
				return "opinion added";
			}
			return "B³¹d";
			
		}, new JSONTransformer());
		
		get("/doctors/*/*/*", (req, res) -> {
			int argsNum = req.splat().length;
			if (argsNum == 0) {
				res.status(404);
				res.body("Choose city");			
			} else if (argsNum == 3) {
				res.status(200);
				em.getTransaction().begin();
				Query q = em.createQuery("SELECT d FROM Doctor d where d.id = :id and d.city = :city and d.speciality = :speciality", Doctor.class);
				System.out.println(req.splat()[0]);
				q.setParameter("city", req.splat()[0]);
				q.setParameter("id", Long.parseLong(req.splat()[2]));
				q.setParameter("speciality", Speciality.getByName(req.splat()[1]));
				Object o = q.getResultList();
				em.getTransaction().commit();
				return o;
				//return connection.getDoctor(Speciality.getByName(req.splat()[1]), req.splat()[0],Long.parseLong(req.splat()[2]));
			}
			return "B³¹d";
			
		}, new JSONTransformer());
		get("/doctors/*/*", (req, res) -> {
			int argsNum = req.splat().length;
			if (argsNum == 0) {
				res.status(404);
				res.body("Choose city");			
			} else if (argsNum == 2) {
				res.status(200);
				em.getTransaction().begin();
				Query q = em.createQuery("SELECT d FROM Doctor d where d.city = :city and d.speciality = :speciality", Doctor.class);
				System.out.println(req.splat()[0]);
				q.setParameter("city", req.splat()[0]);
				q.setParameter("speciality", Speciality.getByName(req.splat()[1]));
				Object o = q.getResultList();
				em.getTransaction().commit();
				return o;/*connection.getAllSpecialistInCity(Speciality.getByName(req.splat()[1]), req.splat()[0]);*/
			}
			return "B³¹d";
			
		}, new JSONTransformer());
		get("/doctors/*", (req, res) -> {
			int argsNum = req.splat().length;
			if (argsNum == 0) {
				res.status(404);
				res.body("Choose city");
			} else if (argsNum == 1) {
				res.status(200);
				em.getTransaction().begin();
				Query q = em.createQuery("SELECT d FROM Doctor d where d.city = :city", Doctor.class);
				System.out.println(req.splat()[0]);
				q.setParameter("city", req.splat()[0]);
				Object o = q.getResultList();
				em.getTransaction().commit();
				return o;
			} 
			return "B³¹d";
			
		}, new JSONTransformer());
				
	}
	
	private static Map<String, String> getAttrs(String body) {
		Map<String, String> attrs = new HashMap<String, String>();
		String[] attr = body.split("&"); 
		for (String s : attr) {
			System.out.println("\t" + s);
			String[] currAttr = s.split("=");
			attrs.put(currAttr[0], currAttr[1]);
		}
		return attrs;
	}

	@Override
	protected void finalize() throws Throwable {
		em.close();
		factory.close();
		System.out.println("Connection closed");
		super.finalize();
	}

}
