package traildown;
import java.util.ArrayList;


public class LibraryPagesParse {
	private ArrayList<String> libraryPageUrls = new ArrayList<String>();
	private ArrayList<String> movieUrls = new ArrayList<String>();
	
	public LibraryPagesParse(){
		populateLibraryPages();
		for (String libraryPageUrl:libraryPageUrls){
			LibraryPageParse lPParse = new LibraryPageParse(libraryPageUrl);
			System.out.printf("Fetching urls from page %s of %s\n", libraryPageUrls.indexOf(libraryPageUrl), libraryPageUrls.size());
			movieUrls.addAll(lPParse.getMovieUrls());
		}
	}
	
	public ArrayList<String> getMovieUrls(){
		return movieUrls;
	}
	
	private void populateLibraryPages(){
		char letter;
		letter = '0';
		libraryPageUrls.add(buildLibraryUrl(letter));
		for (letter='A'; letter <= 'Z'; letter++) { 
			libraryPageUrls.add(buildLibraryUrl(letter));
			} 
	}
	
	private String buildLibraryUrl(char page) {
		return String.format("http://www.hd-trailers.net/Library/%s/", page);
	}
}
