import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.io.File;

public class Downloader implements Runnable {
	
	private DownloadMe downMe;
	
	public Downloader(DownloadMe download) {
		downMe = download; 
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			DownloadFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("DownloadFile() crashed");
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
	
	private void DownloadFile() throws Exception {
		//Set up connection
		String url = downMe.downloadUrl;
		String saveLocation = downMe.downloadTo;
		
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
