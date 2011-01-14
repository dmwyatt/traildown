import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import net.sf.jtmdb.GeneralSettings;
import net.sf.jtmdb.Movie;


public class MoviePageParse extends HtmlParse {
	private ArrayList<String> validResolutions = addResolutions();
	private ArrayList<String> validTypes = addTypes();
	
	private String movieTitle;
	private String imdbid;
	private Date movieRelease;
	private ArrayList<Trailer> trailers = new ArrayList<Trailer>();
	
	public Film getMovie(){
		return new Film(movieTitle, imdbid, movieRelease, trailers);
	}
	
	public String getTitle() {
		return movieTitle;
	}
	
	public String getImdbid() {
		return imdbid;
	}
	
	public ArrayList<Trailer> getTrailers() {
		return trailers;
	}
	
	public MoviePageParse(String url) {
		super(url);
		GeneralSettings.setApiKey("6d96a9efb4752ed0d126d94e12e52036");
		movieTitle = getMovieTitle();
		
		//Get info from tmdb
		try {
			List<Movie> movieResults = Movie.deepSearch(movieTitle);
			if (!movieResults.isEmpty()){
				imdbid = movieResults.get(0).getImdbID();
				movieRelease = movieResults.get(0).getReleasedDate();
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fetchTrailers();
	}
	
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
	
	private String getMovieTitle(){
		Elements previewTitles = doc.select("h1.previewTitle");
		return previewTitles.get(0).text();
	}
	
	private void fetchTrailers() {
		Element table = doc.select("table.bottomTable").get(0);
		Elements rows = table.children();
		for (Element row: rows){
			
			Elements date = row.select("td.bottomTableDate");
			// Only rows with a date contain trailers
			if (date.size() > 0) {
				String dte = date.text();
				String type = row.select("td.bottomTableName").text();
				//We only want teasers and trailers
				if (isValidType(type)) {
					Trailer t = new Trailer(imdbid, dte);
					
					Elements urls = row.select("td.bottomTableResolution");
					for (Element url: urls) {
						String u = url.select("a[href]").attr("href");
						String rez = url.text().toLowerCase();
						if (validResolutions.contains(rez)) {
							t.addRes(rez, u);
						}					
					}
					trailers.add(t);
				}
			}		 
		}		
	}
	
	private boolean isValidType(String typeText) {
		for (String t:validTypes) {
			if (typeText.toLowerCase().contains(t)) {
				return true;
			}
		}
		return false;
	}
}
