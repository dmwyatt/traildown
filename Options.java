import java.io.IOException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import static java.util.Arrays.*;
import static joptsimple.util.DateConverter.*;

public class Options {
	static OptionSet parseOptions (String[] args){
		OptionParser parser = new OptionParser();
		parser.acceptsAll(asList("r", "recent"));
		parser.acceptsAll(asList("m", "movie")).withRequiredArg().describedAs(
				"downloads trailer(s) only for films whose name is exactly 'moviename'");
		parser.acceptsAll(asList("s", "search")).withRequiredArg().describedAs(
				"downloads trailer(s) for films whose name contains 'searchtext'");
		parser.acceptsAll(asList("z", "resolution")).withRequiredArg().describedAs(
				"downloads trailers at specified resolution (480p<default>|720p|1080p)");
		parser.acceptsAll(asList("n", "since")).withOptionalArg().describedAs(
				"downloads trailers posted on or after this date.  If no arguments specfied, defaults to TODAY").withValuesConvertedBy( 
				datePattern( "MM/dd/yyyy" ) );
		parser.acceptsAll(asList("d", "movierelease")).withOptionalArg().describedAs(
				"downloads trailers for movies released on or after this date.  If no arguments specfied, defaults to TODAY").withValuesConvertedBy( 
				datePattern( "MM/dd/yyyy" ) );
		parser.acceptsAll(asList("f", "filename"));
		parser.acceptsAll( asList( "h", "?" ), "show help" );
		
		OptionSet options = parser.parse(args);
		
		if ( options.has( "?" ) ){
			try {
				System.out.println("Be aware that if you don't limit your results by using a date filter or a movie title search...\n...YOU WILL DOWNLOAD TRAILERS FOR 1000+ MOVIES!");
				parser.printHelpOn( System.out );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return options;
		
	}
}
