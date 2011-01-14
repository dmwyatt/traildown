import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class Film implements Serializable {
	String name;
	String imdbid;
	Date releaseDate;
	HashMap<Date, Trailer> trailers= new HashMap<Date, Trailer>();
	
	public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
	  List<T> list = new ArrayList<T>(c);
	  java.util.Collections.sort(list);
	  return list;
	}

	public Film(String name, String imdbid, Date releaseDate, ArrayList<Trailer> trailers) {
		this.name = name;
		this.imdbid = imdbid;
		this.releaseDate = releaseDate;
		storeTrailers(trailers);
	}
	
	public Trailer mostRecentTrailer(){		
		List<Date> trailerDates = asSortedList(trailers.keySet());
		Date mostRecent = trailerDates.get(trailerDates.size()-1);
		return trailers.get(trailerDates.get(0));
	}
	
	public ArrayList<Trailer> getTrailersOnOrAfter(Date d){
		List<Date> trailerDates = asSortedList(trailers.keySet());
		ArrayList<Trailer> ret = new ArrayList<Trailer>();
		for (Date trailerDate:trailerDates) {
			if (trailerDate.after(d) || trailerDate.equals(d)) {
				ret.add(trailers.get(trailerDate));
			}
		}		
		return ret;
	}
	
	public ArrayList<String> getTrailerUrlsAtRez(String rez) {
		ArrayList<String> ret = new ArrayList<String>();
		Set<Date> trailerDates = trailers.keySet();
		for (Date trailerDate:trailerDates){
			ret.add(trailers.get(trailerDate).getUrl(rez));
		}
		return ret;
	}
	
	private void storeTrailers(ArrayList<Trailer> trailers) {
		for (Trailer t:trailers) {
			this.trailers.put(t.trailerDate, t);
		}
	}
	
	public String toString() {
		if (releaseDate == null) {
			return String.format("<Film '%s (%s)' with %s trailers.>", name, "no year found", trailers.size());
		} else {
			SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
			String year = yearFormat.format(releaseDate);
			return String.format("<Film '%s (%s)' with %s trailers.>", name, year, trailers.size());
		}
	}
}
