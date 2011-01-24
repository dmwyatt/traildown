package traildown;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class LatestPageParse extends HtmlParse {
	private HashMap<String, String> movieUrls = new HashMap<String, String>();
	
	public LatestPageParse(String url) {
		super(url);
		// TODO Auto-generated constructor stub
		fetchMovieUrls(url);
	}
	
	public ArrayList<String> getMovieUrls() {
		ArrayList<String> urls = new ArrayList<String>(movieUrls.values());
		
		return urls;
//		try {
//			return (ArrayList<String>) movieUrls.values();
//		}catch (Exception e) {
//			for (String s:movieUrls.values()) {
//				System.out.println(s);
//			}
//			e.printStackTrace();
//		}
//		return null;
	}
	
	private void fetchMovieUrls(String url) {
		Elements rows = doc.select("td.indexTableTrailerImage");
		for (Element row: rows){
			String u = row.select("a[href]").attr("href");
			String title = row.select("img[title]").attr("title");
			movieUrls.put(title, "http://www.hd-trailers.net" + u);			
		}
	}
}
