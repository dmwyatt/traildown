package traildown;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class HtmlParse {
	Document doc;
	String urlToParse;
	
	public HtmlParse(String url) {
		parseUrl(url);
		this.urlToParse = url;
	}
	
	public void parseUrl(String url) {
		try {
			this.doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			try {
				Thread.sleep(10*1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				this.doc = Jsoup.connect(url).get();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.printf("Error fetching %s", url);
				e1.printStackTrace();
			}
		}
	}
	
	public String getDocumentTitle() {
		return doc.title();
	}
}
