package traildown;

public class NameBuilder {
	static String TITLE_PATT = "<title>";
	static String REZ_PATT = "<res>";
	
	static String buildFileName(Film f, String rez, String pattern) {
		pattern.replaceAll(TITLE_PATT, f.name);
		pattern.replaceAll(REZ_PATT, rez);
		return pattern;		
	}
}
