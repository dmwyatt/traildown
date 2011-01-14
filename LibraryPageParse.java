import java.util.ArrayList;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class LibraryPageParse extends HtmlParse{	
	private ArrayList<String> movieUrls = new ArrayList<String>();
	
	public LibraryPageParse(String url) {
		super(url);
		fetchMovieUrls(url);
	}
	
	public ArrayList<String> getMovieUrls() {
		return movieUrls;
	}
	
	private void fetchMovieUrls(String url) {
		Element table = doc.select("table.libraryTextIndexList").get(0);
		Elements rows = table.children();
//		System.out.println(rows);
		for (Element row: rows){
			String u = row.select("a[href]").attr("href");
			movieUrls.add("http://www.hd-trailers.net" + u);
		}
	}
}
