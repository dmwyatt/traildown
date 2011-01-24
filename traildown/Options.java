package traildown;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;


import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import static java.util.Arrays.*;
import static joptsimple.util.DateConverter.*;

public class Options {
	static final String libraryFile = "films.dat";
	static OptionSet parseOptions (String[] args){
		OptionParser parser = new OptionParser();
		parser.accepts("update-library", "Updates local database of film/trailer information.  Film/trailer selection options look at this database for trailers to download.  The longer the period of time between uses of this option, the longer it takes to update the database.  The first run will take a LONG TIME.");
		
		//Select these films...
		OptionSpec<String> movieName = 	parser.acceptsAll(asList("m", "movie")).withRequiredArg().describedAs(
										"Select the film with the specified name.");
		OptionSpec<String> searchText = parser.acceptsAll(asList("s", "search")).withRequiredArg().describedAs(
										"Select the films with specfied text in their name.");
		OptionSpec<Date> filmsSince = 	parser.acceptsAll(asList("d", "movierelease")).withOptionalArg().describedAs(
										"Select films with release dates on or after the specfied date (MM/dd/yyyy).  If no date specfied defaults to today's date.").withValuesConvertedBy( 
										datePattern( "MM/dd/yyyy" ) );
		OptionSpec<Date> trailersSince = parser.acceptsAll(asList("n", "since")).withOptionalArg().describedAs(
										"downloads trailers posted on or after this date.  If no arguments specfied, defaults to TODAY").withValuesConvertedBy( 
										datePattern( "MM/dd/yyyy" ) );
		
		//Select these trailers from selected films
		OptionSpec recentOnly = 		parser.acceptsAll(asList("r", "recent"));
		OptionSpec<String> resolution = parser.acceptsAll(asList("z", "resolution")).withRequiredArg().describedAs(
										"downloads trailers at specified resolution (480p<default>|720p|1080p)");
		
		OptionSpec<String> filePattern = 	parser.acceptsAll(asList("f", "filename")).withRequiredArg();
		parser.acceptsAll( asList( "h", "?" ), "show help" );
		
		OptionSet options = parser.parse(args);
		
		//Provide help message
		if ( options.has( "?" ) || options.has("h")){
			try {
				System.out.println("Be aware that if you don't limit your results by using a date filter or a movie title search...\n...YOU WILL DOWNLOAD TRAILERS FOR 1000+ MOVIES!");
				parser.printHelpOn( System.out );
				return options;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		FilmLibrary filmLib = getFilmLib();
		
		System.out.println(filmLib.getFilmCount());
		
		//Update library if needed
		if (options.has("update-library")) {
			filmLib.updateLibrary();
			persistFilmLibrary(filmLib);
		}
		
		//ArrayList to hold all our downloads
		ArrayList<DownloadMe> dLoads = new ArrayList<DownloadMe>();
		//ArrayList to hold all films we're going to act on
		ArrayList<Film> filmsToProcess = new ArrayList<Film>();
		
		//Get film by name
		if (options.has(movieName)) {
			System.out.printf("Looking for movie '%s'...\n", options.valueOf(movieName));
			Film f = filmLib.getFilmByName(options.valueOf(movieName));
			filmsToProcess.add(f);
			dLoads.add(new DownloadMe(f));
		}
		
		//Get films with search text in their name
		if (options.has(searchText)) {
			System.out.printf("Searching for film titles containing '%s'\n", options.valueOf(searchText));
			ArrayList<Film> searchedFilms = filmLib.getFilmsWithText(options.valueOf(searchText));
			filmsToProcess = expandFilmList(filmsToProcess, searchedFilms);
			for (Film f:searchedFilms) {
				dLoads.add(new DownloadMe(f));
			}
						
		}
		
		//Get films with trailers after specified date
		if (options.has(trailersSince)) {
			System.out.printf("Searching for trailers relased after %s\n", options.valueOf(trailersSince));
			ArrayList<Film> filmsTrailersAfter = filmLib.getFilmsWithTrailersAfter(options.valueOf(trailersSince));
			filmsToProcess = expandFilmList(filmsToProcess, filmsTrailersAfter);
			for (Film f:filmsTrailersAfter) {
				dLoads.add(new DownloadMe(f));
			}
		}
		
		//Films after this date
		if (options.has(filmsSince)) {
			System.out.printf("Searching for films relased after %s\n", options.valueOf(filmsSince));
			ArrayList<Film> filmsAfter = filmLib.getFilmsAfter(options.valueOf(filmsSince));
			filmsToProcess = expandFilmList(filmsToProcess, filmsAfter);
			for (Film f:filmsAfter) {
				dLoads.add(new DownloadMe(f));
			}
		}
		
		System.out.println("Matched films: " + filmsToProcess.size());
		
//		//ArrayList to hold the trailers we're going to download
//		ArrayList<Trailer> trailersToDownload = new ArrayList<Trailer>();
//		
//		//Only want the most recent trailer for each movie
//		if (options.has(recentOnly)) {
//			System.out.println("Only downloading most recent trailer for film(s)");
//			for (Film f:filmsToProcess) {
//				trailersToDownload.add(f.mostRecentTrailer());
//			}
//		}
//		
//		//Only want trailers since this date
//		if (options.has(trailersSince)) {
//			for (Film f:filmsToProcess) {
//				ArrayList<Trailer> tDown = f.getTrailersOnOrAfter(options.valueOf(trailersSince)); 
//				for (Trailer t:tDown) {
//					trailersToDownload.add(t);
//				}				
//			}
//		}
//		
//		if (!options.has(recentOnly) || !options.has(trailersSince)) {
//			System.out.println("Downloading all trailers for each selected film");
//			for (Film f:filmsToProcess) {
//				ArrayList<Trailer> trailers = f.getAllTrailers();
//				if (trailers != null) {
//					for (Trailer t:trailers) {
//						trailersToDownload.add(t);
//					}
//				}
//			}
//		}
//		
//		System.out.println("Matched trailers: " + trailersToDownload.size());
		
		ArrayList<DownloadMe> newDLoads = new ArrayList<DownloadMe>();
		for (int i=dLoads.size();i>0;i--) {
			if (options.has(recentOnly)) {
				DownloadMe d = dLoads.get(i);
				Trailer t = d.film.mostRecentTrailer();
				d.trailer = t;
				dLoads.set(i, d);
			} else if (options.has(trailersSince)) {
				DownloadMe d = dLoads.get(i);
				ArrayList<> t = d.film.getTrailersOnOrAfter(options.valueOf(trailersSince));
				
			}
		}
		
		//ArrayList to hold the urls to download
		ArrayList<DownloadMe> downloads = new ArrayList<DownloadMe>();
	
		String rez = options.valueOf(resolution);
		if (rez == null) {
			rez = "480p";
		}
		System.out.printf("Selecting the %s url for each trailer\n", rez);
		for (Trailer t:trailersToDownload) {
			String url = t.getUrl(rez);
			if (url == null) { 
				continue;
			}
			downloads.add(new DownloadMe(url, "fake"));
		}
		
		System.out.println("Matched urls to download: " + downloads.size());
		
		for (DownloadMe d:downloads) {
			NameBuilder.buildFileName(f, rez, pattern)
		}
		
		return options;
		
	}
	
	private static ArrayList<Film> expandFilmList(ArrayList<Film> one, ArrayList<Film> two) {
		for (Film f:two) {
			if (!one.contains(f)) {
				one.add(f);
			}
		}
		return one;
	}
	
	private static FilmLibrary getFilmLib() {
		
		FilmLibrary filmLib = new FilmLibrary();
		try {
			FileInputStream fileStream = new FileInputStream("films.dat");
			ObjectInputStream os = new ObjectInputStream(fileStream);
			filmLib = (FilmLibrary) os.readObject();
			filmLib.readFromFile = true;
			os.close();
		} catch (Exception e) {
			System.out.println("Unable to read existing film library!  Must update library.");
			return new FilmLibrary();
		}
		return filmLib;		
	}
	
	private static void persistFilmLibrary(FilmLibrary filmLib) {
		try{
			FileOutputStream fs = new FileOutputStream("films.dat");
			ObjectOutputStream os = new ObjectOutputStream(fs);
			filmLib.readFromFile = false;
			os.writeObject(filmLib);
			os.close();
		} catch (Exception e){
			e.printStackTrace();
		}		
	}
}
