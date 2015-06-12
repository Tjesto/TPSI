package pl.put.tpsi.data;

import java.util.ArrayList;
import java.util.List;

public interface DatebaseObject {
	void create(MockedDatebaseConnection conn);
	DatebaseObject read(MockedDatebaseConnection conn);
	DatebaseObject update(MockedDatebaseConnection conn, DatebaseObject newValue);
	DatebaseObject delete(MockedDatebaseConnection conn);
	
}


