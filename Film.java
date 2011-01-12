import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Film {
	String name;
	String imdbid;
	Date releaseDate;

	public Film(String name, String imdbid, String releaseDate) {
		this.name = name;
		this.imdbid = imdbid;
		DateFormat formatter = new SimpleDateFormat("yyyy/dd/MM");
		try {
		Date parsedDate = formatter.parse(releaseDate);
		this.releaseDate = parsedDate;
		}
		catch (ParseException e){
			System.out.printf("Unable to parse: %s", releaseDate);
			e.printStackTrace();
		}
	}	
}
