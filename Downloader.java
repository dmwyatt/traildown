import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.File;;

public class Downloader {
	
	private BlockingQueue<DownloadMe> downloads;

	public Downloader() {
		downloads = new LinkedBlockingQueue<DownloadMe>(); 
	}
	
	public void addDownload(String url, String dlpath, String id) {
		DownloadMe download = new DownloadMe(url, dlpath, id);
		downloads.add(download);
		System.out.printf("Added %s to queue\n", download.ident);
	}
	
	public void start() {
		// Up to three concurrent downloads
//		Runnable downloadJob = new ThreadedDownloader(); 
//		Thread tDownloader = new Thread(downloadJob);
//		tDownloader.start();
		ThreadedDownloader tDowner = new ThreadedDownloader();
		tDowner.run();
	}
	
	public class ThreadedDownloader implements Runnable {
		public void run() {
			ExecutorService pool = Executors.newFixedThreadPool( 2 );
			while (true) {
				ThreadedDownload downloadThread = new ThreadedDownload();			
				pool.execute( downloadThread );
			}
		}
		
		public class ThreadedDownload implements Runnable {
			public void run() {
				// Get oldest url added to downloadUrls and download it
				try {
					// This blocks until there is a download
					DownloadMe downloading = downloads.take();
					System.out.printf("Fetching %s\n", downloading.ident);
					DownloadFile(downloading.downloadUrl, downloading.downloadTo);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}						
			}
			
			private URLConnection SetConxProps(URL url, URLConnection uc){
				if (url.getHost().toLowerCase().contains("apple.com")) {
					uc.setRequestProperty("User-Agent", "QuickTime");
				}
				if (url.getHost().toLowerCase().contains("yahoo.com")) {
					uc.setRequestProperty("User-Agent", "Firefox");
					uc.setRequestProperty("Referer", "http://movies.yahoo.com/");
				}
				return uc;
			}
			
			private void DownloadFile(String url, String saveLocation) throws Exception {
				//Set up connection
				
				
				URL u = new URL(url);
				URLConnection uc = u.openConnection();
				uc = SetConxProps(u, uc);
				
		        BufferedInputStream in = null;
		        FileOutputStream fout = null;
		        try
		        {
		        	System.out.println("Preparing conx...");
		        	InputStream ucStream = uc.getInputStream();
		            in = new BufferedInputStream(ucStream);
		            fout = new FileOutputStream(saveLocation);

		            byte data[] = new byte[1024];
		            int count;
		            System.out.printf("....fetching %d bytes from %s\n", uc.getContentLength(), url);
		            while ((count = in.read(data, 0, 1024)) != -1)
		            {
		                    fout.write(data, 0, count);
		            }
		        }
		        finally
		        {
		                if (in != null)
		                        in.close();
		                if (fout != null)
		                        fout.close();
		                File fn = new File(saveLocation);
		        	    System.out.println(fn.getCanonicalPath());
		        }

			}
		}
	}
}
