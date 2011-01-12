import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class HtmlParse {
	Document doc;
	String url;
	
	public HtmlParse(String url) {
		try {
			this.doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.printf("Error fetching %s", url);
			e.printStackTrace();
		}
		this.url = url;
	}
	
	public String getDocumentTitle() {
		return doc.title();
	}
}
