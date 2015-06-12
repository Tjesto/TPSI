package pl.put.tpsi.data;

import java.text.DateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
public class Opinion {
	
	@GeneratedValue
	private long id;
	private String author;
	private byte rate;
	private String comment;
	private Date date;
	
	
	public Opinion(byte rate) {
		this(null, rate);
	}
	
	public Opinion(String author, byte rate) {
		this(author, rate, new Date(System.currentTimeMillis()));
	}
	
	public Opinion(String author, byte rate, Date date) {
		this(author, rate, null, date);
	}


	public Opinion(String author, byte rate, String comment, Date date) {
		super();
		this.author = author;
		this.rate = rate;
		this.comment = comment;
		this.date = date;
	}


	public String getAuthor() {
		return author;
	}


	public byte getRate() {
		return rate;
	}


	public String getComment() {
		return comment;
	}

	public String getDate() {
		return DateFormat.getDateTimeInstance(DateFormat.FULL,
				DateFormat.MEDIUM).format(date);
	}	
	
	
	
}
