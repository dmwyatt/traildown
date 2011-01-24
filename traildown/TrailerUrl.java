package traildown;
import java.io.Serializable;


public class TrailerUrl implements Serializable {
	String url;
	boolean downloaded;
	public TrailerUrl (String url, boolean downloaded) {
		this.url = url;
		this.downloaded = downloaded;
	}
}
