package traildown;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;


public class Trailer implements Serializable {
	String imdbid;
	Date trailerDate;
	private HashMap<String, TrailerUrl> urlsRes = new HashMap<String, TrailerUrl>();
	
	public Trailer(String imdbid, String trailerDate) {		
		this.imdbid = imdbid;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date parsedDate = formatter.parse(trailerDate);
			this.trailerDate = parsedDate;
		}
		catch (ParseException e){
			e.printStackTrace();
		}	
	}
	
	public void addRes(String res, String url) {
		urlsRes.put(res, new TrailerUrl(url, false));
	}
	
	public TrailerUrl getRes(String res) {
		return urlsRes.get(res);
	}
	
	public void mergeUrlsRes(Trailer t) {
		Iterator<String> uIterate = t.getUrlsRes().keySet().iterator();
		while (uIterate.hasNext()) {
			String tNextKey = uIterate.next();
			TrailerUrl tNextTrailer = t.getRes(tNextKey);
			if (!urlsRes.containsKey(tNextKey)) {
				urlsRes.put(tNextKey, tNextTrailer);
			} else {
				continue;
			}			
		}
	}
	
	public HashMap<String, TrailerUrl> getUrlsRes() {
		return urlsRes;
	}
	
	public String getUrl(String res) {
		if (urlsRes.containsKey(res)) {
			return urlsRes.get(res).url;
		}
		return null;
	}
	
	public boolean isDownloaded(String res) {
		if (urlsRes.containsKey(res)) {
			return urlsRes.get(res).downloaded;
		}
		return false;
	}
	
	public String trailerSource() {
		String ret = null;
		Collection<TrailerUrl> urls = urlsRes.values();
		Object[] _urls = urls.toArray();
		TrailerUrl u = (TrailerUrl)_urls[0];
		try {
			
			URL urlO = new URL(u.url);
			return urlO.getHost();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public String toString() {
		return String.format("<Trailer() for %s.  Released on %s.  Resolutions available: %s>", imdbid, trailerDate, urlsRes.keySet()); 
	}
}