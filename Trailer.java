import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class Trailer {
	String imdbid;
	Date trailerDate;
	private HashMap<String, String> urlsRes;
	
	public Trailer(String imdbid, String trailerDate) {		
		this.imdbid = imdbid;
		DateFormat formatter = new SimpleDateFormat("yyyy/dd/MM");
		try {
		Date parsedDate = formatter.parse(trailerDate);
		this.trailerDate = parsedDate;
		}
		catch (ParseException e){
			System.out.printf("Unable to parse: %s", trailerDate);
			e.printStackTrace();
		}	
	}
	
	public void addRes(String res, String url) {
		urlsRes.put(res, url);
	}
}