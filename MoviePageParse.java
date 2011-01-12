import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import net.sf.jtmdb.GeneralSettings;
import net.sf.jtmdb.Movie;



public class MoviePageParse extends HtmlParse {
	private ArrayList<String> validResolutions = addResolutions();
	private ArrayList<String> validTypes = addTypes();
	
	String movieTitle;
	String imdbid;
	ArrayList<Trailer> trailers;
	
	private ArrayList<String> addResolutions() {
		ArrayList<String> vRez = new ArrayList<String>(); 
		vRez.add("480p");
		vRez.add("720p");
		vRez.add("1080p");
		return vRez;
	}
	
	private ArrayList<String> addTypes() {
		ArrayList<String> vTypes = new ArrayList<String>();
		vTypes.add("trailer");
		vTypes.add("teaser");
		return vTypes;
	}
	
	public MoviePageParse(String url) {
		super(url);
		GeneralSettings.setApiKey("6d96a9efb4752ed0d126d94e12e52036");
		movieTitle = getMovieTitle();
		
		//Get imdb id
		try {
			List<Movie> movieResults = Movie.deepSearch(movieTitle);
			if (!movieResults.isEmpty()){
				imdbid = movieResults.get(0).getImdbID();
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		trailers = getTrailers();
	}
	
	private String getMovieTitle(){
		Elements previewTitles = doc.select("h1.previewTitle");
		return previewTitles.get(0).text();
	}
	
	private void getTrailers() {
		HashMap<String, String> urlRes = new HashMap<String, String>();
		Element table = doc.select("table.bottomTable").get(0);
		Elements rows = table.children();
		for (Element row: rows){
			
			Elements date = row.select("td.bottomTableDate");
			// Only rows with a date contain trailers
			if (date.size() > 0) {
				String dte = date.text();
				String type = row.select("td.bottomTableName").text();
				//We only want teasers and trailers
				if (validTypes.contains(type.toLowerCase())) {
					Elements urls = row.select("td.bottomTableResolution");
					System.out.print(dte + ", ");
					System.out.print(type + ", ");
					for (Element url: urls) {
						String u = url.select("a[href]").attr("href");
						String rez = url.text().toLowerCase();
						if (validResolutions.contains(rez)) {
							urlRes.put(rez, u);
						}					
					}
					System.out.println("");
				}
			}
		
		Trailer trailer = new Trailer(movieTitle, movieTitle); 
		}		
	}
}
