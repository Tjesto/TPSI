package pl.put.tpsi.data;

import pl.put.tpsi.utils.Utils;

public enum Speciality {
	PULMUNOLOGIST("Pulmunolog"), ORTHOPEDIST("Ortopeda"), DHERMATHOLOGIST("Dermatolog");
	
	
	
	private String name;
	
	private Speciality(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public static Speciality getByName(String string) {
		for (Speciality s : values()) {
			if (s.getName().toLowerCase().equals(Utils.removePolishLetters(string.toLowerCase()))) {
				return s;
			}
		}
		return null;
	}

}
