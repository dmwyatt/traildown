
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
		
//		String d1url = "http://www.contriving.net/ftpd/zscreen/2010/screen-0023-12-29.png";
//		String d1fn = "kings_speech.png";
//		String d1id = "The King's Speech";
//		
//		String d2url = "http://www.contriving.net/ftpd/zscreen/2010/screen-0022-12-27.png";
//		String d2fn = "green_hornet.png";
//		String d2id = "The Green Hornet";
//		
//		String d3url = "http://www.contriving.net/ftpd/zscreen/2010/screen-0021-12-23.png";		
//		String d3fn = "star_wars.png";
//		String d3id = "Star Wars";
//		
//		String d4url = "http://www.contriving.net/ftpd/zscreen/2010/screen-0020-12-22.png";
//		String d4fn = "the_mechanic.png";
//		String d4id = "The Mechanic";
		
		Downloader dler = new Downloader();
		
		
		dler.addDownload(d4url, d4fn, d4id);
		dler.addDownload(d1url, d1fn, d1id);
		dler.addDownload(d2url, d2fn, d2id);		
		dler.addDownload(d3url, d3fn, d3id);
		dler.start();
	}
}
