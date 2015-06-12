package pl.put.tpsi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import pl.put.tpsi.data.Speciality;
import pl.put.tpsi.utils.Utils;

public class ClientRunnable {

	private static final String LOCALHOST = "http://localhost:4567";
	private static final String LOCALHOST_DOC = LOCALHOST + "/doctors/";
	private static final String LOCALHOST_APP = LOCALHOST + "/appointements/";
	private static final String SEPARATOR_BEGIN = "================Response==============";
	
	private static final String SEPARATOR_END =   "=============Nest=request=============";

	public static void main(String[] args) {
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			String city = args.length > 0 ? args[0] : "poznan";
			String name = args.length > 1 ? args[1] : "Tester";
			String surname = args.length > 2 ? args[2] : "Testerowicz";
			String spec = args.length > 3 ? args[3] : Speciality.ORTHOPEDIST
					.getName();

			HttpGet getListOfDoctorsReq = new HttpGet(LOCALHOST_DOC + city
					+ "/" + spec);			

			HttpResponse response = httpClient.execute(getListOfDoctorsReq);
			
			if (response.getStatusLine().getStatusCode() != 200) {
				System.out.println(response.getStatusLine().getStatusCode());
				return;
			}			
			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = null;
			List<Long> ids = new ArrayList<Long>();
			System.err.println(SEPARATOR_BEGIN);
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				getIdFrom(line, ids);
			}
			System.err.println(SEPARATOR_END);
			Random rand = new Random(System.nanoTime());
			Collections.shuffle(ids, rand);			
			String choosenId = ids.get(rand.nextInt(ids.size())).toString();
			System.out.println("Choosen id=" + choosenId);
			
			HttpGet getListOfDoctorsAppointementsReq = new HttpGet(LOCALHOST_APP + city
					+ "/" + spec +"/" + choosenId);			

			HttpResponse response1 = httpClient.execute(getListOfDoctorsAppointementsReq);
			
			if (response1.getStatusLine().getStatusCode() != 200) {
				System.out.println(response.getStatusLine().getStatusCode());
				return;
			}			
			br = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
			line = null;
			System.err.println(SEPARATOR_BEGIN);
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			System.err.println(SEPARATOR_END);
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int month = Calendar.getInstance().get(Calendar.MONTH);
			int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			int hour = rand.nextInt(10) + 7;
			int it = 0;
			boolean done = false;
			do {
				hour++;
				String randomDate = Integer.toString(year) + (month < 10 ? "0" :"") + Integer.toString(month) + (day < 10 ? "0" :"") + Integer.toString(day) + (hour < 10 ? "0" :"") + Integer.toString(hour);
				HttpPost addAppReq = new HttpPost(LOCALHOST_APP + city
						+ "/" + spec +"/" + choosenId + "/" + randomDate);
				addAppReq.setEntity(new StringEntity("name=" + name + "&surname=" + surname + "&action=add"));
				HttpResponse response2 = httpClient.execute(addAppReq);
				
				if (response1.getStatusLine().getStatusCode() != 200) {
					System.out.println(response.getStatusLine().getStatusCode());
					return;
				}
				br = new BufferedReader(new InputStreamReader(response2.getEntity().getContent()));
				line = null;
				System.err.println(SEPARATOR_BEGIN);
				while ((line = br.readLine()) != null) {
					System.out.println(line);
					if (line.toLowerCase().contains("added")) {
						done = true;
					}
				}
				System.err.println(SEPARATOR_END);				
			} while (!done && (it++) < 3);
						
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void getIdFrom(String line, List<Long> ids) {
		line = line.replaceAll("[\\[\\]]", "");
		String[] doctors = line.split("\\},\\{");
		for (String s : doctors) {
			s = s.replaceAll("[\\{\\}]", "");
			String[] doctorAttrs = s.split(",");
			for (String attr : doctorAttrs) {
				String[] attrVal = attr.split(":");
				if ("\"id\"".equals(attrVal[0])) {
					if (attrVal.length > 1) {
						ids.add(Long.parseLong(attrVal[1]));
					}
				} else {
					System.out.println(attrVal[0] + " " + (attrVal.length > 1 ? attrVal[1] : ""));
				}
			}
		}
	}

}
