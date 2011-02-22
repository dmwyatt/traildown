package traildown;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
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
		
		//Select these trailers from selected films
		OptionSpec<Date> trailersSince = parser.acceptsAll(asList("n", "since")).withOptionalArg().describedAs(
										"downloads trailers posted on or after this date.  " +
										"If no arguments specfied, defaults to TODAY").withValuesConvertedBy( 
										datePattern( "MM/dd/yyyy" ) );
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
		
		System.out.printf("Know of %s films.", filmLib.getFilmCount());
		
		//Update library if needed
		if (options.has("update-library")) {
			filmLib.updateLibrary();
			persistFilmLibrary(filmLib);
			System.out.printf("Now know of %s films.", filmLib.getFilmCount());
		}
		
		String rez = "480p";
		if (options.has(resolution)) {
			rez = options.valueOf(resolution).toLowerCase();
		} 
		
		//Build list of films to pull trailers from
		ArrayList<Film> filmList = new ArrayList<Film>();
		
		if (options.has(movieName)) {
			filmList.add(filmLib.getFilmByName(options.valueOf(movieName)));
		}
		if (options.has(searchText)) {
			for (Film f:filmLib.getFilmsWithText(options.valueOf(searchText))) {
				filmList.add(f);
			}
		}
		if (options.has(filmsSince)) {
			for (Film f:filmLib.getFilmsAfter(options.valueOf(filmsSince))) {
				filmList.add(f);
			}
		}
		if (!options.has(movieName) || !options.has(searchText) || !options.has(filmsSince)) {
			filmList = filmLib.getFilms();
		}
		
		//Check each film for trailers that match our options...
		ArrayList<Film> filmFilteredList = new ArrayList<Film>();
		if (options.has(trailersSince)) {
			Date dateCheck = null;
			if (options.valueOf(trailersSince) == null) {
				dateCheck = Calendar.getInstance().getTime();
			} else {
				dateCheck = options.valueOf(trailersSince);
			}
			for (Film f:filmLib.getFilmsWithTrailersAfter(dateCheck)) {
				filmFilteredList.add(f);
			}
			for (Film f:filmLib.getFilms()) {
				f.downloadTrailersAfter(rez, dateCheck);
			}
			return options;
		} 
		if (options.has(recentOnly)) {
			for (Film f:filmLib.getFilms()) {
				f.downloadMostRecentTrailer(rez);
			}
		}
		

		
		
		return options;
		
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
