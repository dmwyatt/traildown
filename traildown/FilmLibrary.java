package traildown;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;


public class FilmLibrary implements Serializable{
	boolean readFromFile = false;
//	private ArrayList<Film> films = new ArrayList<Film>();
	private HashMap<String, Film> films = new HashMap<String, Film>();
	
	public void updateLibrary() {
		LatestPagesParse latestPp = new LatestPagesParse();
		addFilms(latestPp.latestFilms);
		Film recentlyUpdated = getMostRecentUpdated();
		if (recentlyUpdated != null) {
			latestPp.filmsUpdatedAfter(recentlyUpdated.mostRecentTrailer().trailerDate);
			addFilms(latestPp.latestFilms);
			
		} else {
			System.out.println("No films on file");
			populateLibrary();
		}
		persistFilms();
	}
	
	public void populateLibrary() {
		LibraryPagesParse lpp = new LibraryPagesParse();
		ArrayList<String> movieUrls = lpp.getMovieUrls();
		ArrayList<Film> libraryFilms = new ArrayList<Film>();
		for (String url:movieUrls) {
			MoviePageParse mpp = new MoviePageParse(url);
			Film f = mpp.getMovie();
			libraryFilms.add(f);
			System.out.println("Got info for: " + f.name);
		}
		addFilms(libraryFilms);
		persistFilms();
	}
	
	public int getFilmCount() {
		return films.size();
	}
	
	public void addFilms(ArrayList<Film> films) {
		for (Film f:films) {
			addFilm(f);
		}
	}
	
	public void addFilm(Film f) {
		Film existingFilm = films.get(f.name);
		if (existingFilm == null) {
			films.put(f.name.toLowerCase(), f);
	
		} else {
			if (hasNewerTrailer(f, existingFilm)) {
				existingFilm.mergeFilms(f);				
				films.remove(existingFilm);
				films.put(existingFilm.name.toLowerCase(), existingFilm);
			}
		}
	}
	
	public ArrayList<Film> getFilmsAfter(Date d) {
		ArrayList<Film> ret = new ArrayList<Film>();
		Set<String> filmKeys = films.keySet();
		for (String f:filmKeys) {
			Film film = films.get(f);
			if (film.releasedAfter(d)) {
				ret.add(film);
			}
		}
		return ret;
	}
	
	public ArrayList<String> getTrailersAfter(Date d, String rez) {
		ArrayList<String> ret = new ArrayList<String>();
		Set<String> filmKeys = films.keySet();
		
		for (String f:filmKeys) {
			Film film = films.get(f);
			ArrayList<Trailer> ts = film.getTrailersOnOrAfter(d);
			for (Trailer t:ts) {
				ret.add(t.getUrl(rez));
			}
		}
		return ret;
	}
	
	public ArrayList<Film> getFilmsWithTrailersAfter(Date d) {
		ArrayList<Film> ret = new ArrayList<Film>();
		Set<String> filmKeys = films.keySet();
		
		for (String f:filmKeys) {
			Film film = films.get(f);
			ArrayList<Trailer> ts = film.getTrailersOnOrAfter(d);
			if (ts.size() > 0) {
				ret.add(film);
			}
		}
		return ret;
	}
	
	public Film getFilmByName(String moviename) {
		return films.get(moviename.toLowerCase());
	}
	
	public Film getFilmById(String id) {
		Set<String> filmKeys = films.keySet();
		
		for (String f:filmKeys) {
			Film film = films.get(f);
			if (film.imdbid != null) {
				if (film.imdbid.toLowerCase() == id) {
					return film;
				}
			}
		}
		return null;
	}
	
	public ArrayList<Film> getFilmsWithText(String searchtext) {
		ArrayList<Film> ret = new ArrayList<Film>();
		Set<String> filmKeys = films.keySet();
		
		for (String f:filmKeys) {
			Film film = films.get(f);
			if (film.name.toLowerCase().contains(searchtext.toLowerCase())) {
				ret.add(film);
			}
		}
		return ret;
	}
	
	public boolean hasNewerTrailer(Film f1, Film f2) {
	
		Trailer f1trailer = f1.mostRecentTrailer();
		Trailer f2trailer = f2.mostRecentTrailer();
		
		if (f1trailer == null) {
			return false;
		}
		if (f2trailer == null) {
			return true;
		}
		
		Date f1date = f1trailer.trailerDate;
		Date f2date = f2trailer.trailerDate;
		
		if (f1date.after(f2date)) {
			return true;
		}
		return false;
	}
	
	public Film getMostRecentUpdated() {
		if (films.size() == 0) {
			return null;
		}
		Film mostRecentUpdatedFilm = new Film();
		for (String f: films.keySet()) {
			Film film = films.get(f);
			if (hasNewerTrailer(film, mostRecentUpdatedFilm)) {
				mostRecentUpdatedFilm = film;
			}
		}
		return mostRecentUpdatedFilm;
	}
	
	public void persistFilms() {
		Set<String> filmKeys = films.keySet();
		try{
			FileOutputStream fs = new FileOutputStream("films.dat");
			ObjectOutputStream os = new ObjectOutputStream(fs);
			
			for (String f:filmKeys) {
				Film film = films.get(f);
				os.writeObject(film);
			}
			os.close();
		} catch (Exception e){
			e.printStackTrace();
		}		
	}
	
	public void getPersisted() {
		try {
			FileInputStream fileStream = new FileInputStream("films.dat");
			ObjectInputStream os = new ObjectInputStream(fileStream);
			while (true) {
				try {
					addFilm((Film) os.readObject());
				} catch (Exception e) {
					os.close();
					break;
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		readFromFile = true;
	}
}
