import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class Trailer {
	String imdbid;
	Date trailerDate;
	private HashMap<String, String> urlsRes = new HashMap<String, String>();
	
	public Trailer(String imdbid, String trailerDate) {		
		this.imdbid = imdbid;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date parsedDate = formatter.parse(trailerDate);
			this.trailerDate = parsedDate;
		}
		catch (ParseException e){
			e.printStackTrace();
		}	
	}
	
	public void addRes(String res, String url) {
		urlsRes.put(res, url);
	}
	
	public String getUrl(String res) {
		if (urlsRes.containsKey(res)) {
			return urlsRes.get(res);
		}
		return null;
	}
	
	public String toString() {
		return String.format("<Trailer() for %s.  Released on %s.  Resolutions available: %s>", imdbid, trailerDate, urlsRes.keySet()); 
	}
}