package traildown;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class LatestPagesParse {
	private String latestRoot = "http://www.hd-trailers.net/Page/";
	ArrayList<Film> latestFilms = new ArrayList<Film>();
	
	public void filmsUpdatedAfter(Date thisDate) {
		boolean keepGoing = true;
		int pageCounter = 1;
		int foundCount = 0;
		
		Calendar c = Calendar.getInstance();
		c.setTime(thisDate);
		c.add(Calendar.DATE, -1);
		Date checkDate = c.getTime();
		
		System.out.println("Most recent known trailer on: " + thisDate);
		System.out.println("Checking for trailers after: " + checkDate);
		
		while (keepGoing) {
			String url = latestRoot + Integer.toString(pageCounter) + "/";
			System.out.printf("Checking %s for new trailers.\t", url);
			LatestPageParse lpp = new LatestPageParse(url);
			
			for (String movieUrl:lpp.getMovieUrls()) {
				MoviePageParse mpp = new MoviePageParse(movieUrl);
				Film f = mpp.getMovie();
				System.out.println("Checking: " + f.name);
				Trailer fTrailer = f.mostRecentTrailer();
				
				if (fTrailer == null) {
					continue;
				}
				
				if (fTrailer.trailerDate.after(checkDate)) {
					System.out.printf("\t ...has a trailer after %s...adding\n", checkDate);
					latestFilms.add(f);
					foundCount += 1;
				} else {
					continue;
				}				
			}
			pageCounter += 1;
			if (foundCount == 0) {
				keepGoing = false;
			} else {
				foundCount = 0;
			}
		}
	}
	
	public void updateThisLibrary(FilmLibrary knownFilms) {
				
		Date mostRecentKnownUpdate = knownFilms.getMostRecentUpdated().mostRecentTrailer().trailerDate;
		
		filmsUpdatedAfter(mostRecentKnownUpdate);
	}
}
