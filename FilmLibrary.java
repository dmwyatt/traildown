import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;


public class FilmLibrary {
	private ArrayList<Film> films = new ArrayList<Film>();
	
	public void addFilm(Film f) {
		Film existingFilm = getFilmById(f.imdbid); 
		if (existingFilm == null) {
			films.add(f);
	
		} else {
			if (hasNewerTrailer(f, existingFilm)) {
				films.remove(existingFilm);
				films.add(f);
			}
		}
	}
	
	public ArrayList<Film> getFilmsAfter(Date d) {
		ArrayList<Film> ret = new ArrayList<Film>();
		for (Film f:films) {
			if (d.equals(f.releaseDate) || d.after(f.releaseDate)) {
				ret.add(f);
			}
		}
		return ret;
	}
	
	public ArrayList<String> getTrailersAfter(Date d, String rez) {
		ArrayList<String> ret = new ArrayList<String>();
		for (Film f:films) {
			ArrayList<Trailer> ts = f.getTrailersOnOrAfter(d);
			for (Trailer t:ts) {
				ret.add(t.getUrl(rez));
			}
		}
		return ret;
	}
	
	public Film getFilmByName(String moviename) {
		for (Film f:films) {
			if (f.name.toLowerCase() == moviename.toLowerCase()) {
				return f;
			}
		}
		return null;
	}
	
	public Film getFilmById(String id) {
		for (Film f:films) {
			if (f.imdbid.toLowerCase() == id) {
				return f;
			}
		}
		return null;
	}
	
	public ArrayList<Film> getFilmsWithText(String searchtext) {
		ArrayList<Film> ret = new ArrayList<Film>();
		for (Film f:films) {
			if (f.name.toLowerCase().contains(searchtext.toLowerCase())) {
				ret.add(f);
			}
		}
		return ret;
	}
	
	public boolean hasNewerTrailer(Film f1, Film f2) {
		if (f1.mostRecentTrailer().trailerDate.after(f2.mostRecentTrailer().trailerDate)) {
			return true;
		}
		return false;
	}
	
	public Film getMostRecentUpdated() {
		Film mostRecentUpdatedFilm = films.get(0);
		for (Film f: films) {
			if (f.mostRecentTrailer().trailerDate.after(mostRecentUpdatedFilm.mostRecentTrailer().trailerDate)) {
				mostRecentUpdatedFilm = f;
			}
		}
		return mostRecentUpdatedFilm;
	}
	
	public void persistFilms() {
		try{
			FileOutputStream fs = new FileOutputStream("films.dat");
			ObjectOutputStream os = new ObjectOutputStream(fs);
			for (Film f:films) {
				os.writeObject(f);
			}
			os.close();
		} catch (Exception e){
			e.printStackTrace();
		}		
	}
}
