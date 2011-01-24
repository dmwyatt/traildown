package traildown;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class Film implements Serializable {
	String name;
	String imdbid;
	Date releaseDate;
	HashMap<Date, Trailer> trailers= new HashMap<Date, Trailer>();
	
	public boolean releasedAfter(Film f) {
		if (releaseDate.after(f.releaseDate)) {
			return true;
		} else {
			return false;
		}
	}
	
	public ArrayList<Trailer> getAllTrailers() {
		ArrayList<Trailer> ret = new ArrayList<Trailer>();
		Collection<Trailer> trails = trailers.values();
		Iterator<Trailer> tIterate = trails.iterator();
		while (tIterate.hasNext()) {
			ret.add(tIterate.next());
		}
		return ret;
	}
	
	public void mergeFilms(Film f) {
		if (f.name == this.name) {
			this.imdbid = f.imdbid;
			this.releaseDate = f.releaseDate;
			Iterator<Date> tIterate = f.trailers.keySet().iterator();
			while (tIterate.hasNext()) {
				addTrailer(f.trailers.get(tIterate));
			}
		}
	}
	
	public boolean releasedAfter(Date d) {
		if (releaseDate == null) {
//			System.out.println(name);
			return false;
		}
		if (releaseDate.after(d)) {
			return true;
		} else {
			return false;
		}
	}
	
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
	
	public Film() {		
	}
	
	public Trailer mostRecentTrailer(){
		List<Date> trailerDates = asSortedList(trailers.keySet());
		if (trailerDates.size() == 0) {
			return null;
		}
		Date mostRecent = trailerDates.get(trailerDates.size()-1);
		return trailers.get(mostRecent);
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
	
	public void addTrailer(Trailer trailer) {
		Trailer t = trailers.get(trailer.trailerDate);
		if (t == null) {
			trailers.put(trailer.trailerDate, trailer);
		} else {
			t.mergeUrlsRes(trailer);
			this.trailers.put(trailer.trailerDate, trailer);
		}
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
