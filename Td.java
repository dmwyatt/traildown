import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import joptsimple.OptionSet;


public class Td {
	public static void main(String[] args){		
//		ExecutorService downloadPool = Executors.newFixedThreadPool(2); //Two simultaneous downloads
//		Downloader downloadJob4 = new Downloader(new DownloadMe(d4url, d4fn, d4id));
//		downloadPool.execute(downloadJob4);
//		
//		Downloader downloadJob1 = new Downloader(new DownloadMe(d1url, d1fn, d1id));
//		downloadPool.execute(downloadJob1);
//		
//		Downloader downloadJob2 = new Downloader(new DownloadMe(d2url, d2fn, d2id));
//		downloadPool.execute(downloadJob2);
//		
//		Downloader downloadJob3 = new Downloader(new DownloadMe(d3url, d3fn, d3id));
//		downloadPool.execute(downloadJob3);
		
//		String url = "http://www.hd-trailers.net/movie/2012/";
		
//		MoviePageParse moviePageParsed = new MoviePageParse(url);
//		Film f = moviePageParsed.getMovie();
//		System.out.println(f);
		
//		LibraryPageParse lpParsed = new LibraryPageParse("http://www.hd-trailers.net/Library/0/");
//		System.out.println(lpParsed.getMovieUrls());
//		for (String url:lpParsed.getMovieUrls()) {
//			MoviePageParse mpParsed = new MoviePageParse(url);
//			Film f = mpParsed.getMovie();
//			System.out.println(f);
//		}
//		LibraryPagesParse lpp = new LibraryPagesParse();
//		System.out.println(lpp.getMovieUrls().size());
//		for (String u:lpp.getMovieUrls()){
//			MoviePageParse mpp = new MoviePageParse(u);
//			Film f = mpp.getMovie();
//			System.out.println(f);
//		}
		OptionSet options = Options.parseOptions(args);
		LatestPageParse parse = new LatestPageParse("http://www.hd-trailers.net/Page/1/");
		
	}
}
