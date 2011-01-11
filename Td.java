import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Td {
	public static void main(String[] args){		
		String d1url = "http://playlist.yahoo.com/makeplaylist.dll?sdm=web&pt=rd&sid=114763383";
		String d1fn = "kings_speech.mov";
		String d1id = "The King's Speech";
		
		String d2url = "http://playlist.yahoo.com/makeplaylist.dll?sdm=web&pt=rd&sid=109546096";
		String d2fn = "green_hornet.mov";
		String d2id = "The Green Hornet";
		
		String d3url = "http://playlist.yahoo.com/makeplaylist.dll?sdm=web&pt=rd&sid=119914537";		
		String d3fn = "star_wars.mov";
		String d3id = "Star Wars";
		
		String d4url = "http://playlist.yahoo.com/makeplaylist.dll?sdm=web&pt=rd&sid=119871571";
		String d4fn = "the_mechanic.mov";
		String d4id = "The Mechanic";
		
		String d5url = "http://playlist.yahoo.com/makeplaylist.dll?sdm=web&pt=rd&sid=119904256";
		String d5fn = "cara.mov";
		String d5id = "Carancho";
		
		String d6url = "http://playlist.yahoo.com/makeplaylist.dll?sdm=web&pt=rd&sid=115387498";
		String d6fn = "true_grit.mov";
		String d6id = "True Grit";	
		
		ExecutorService downloadPool = Executors.newFixedThreadPool(2); //Two simultaneous downloads
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

	}
}
