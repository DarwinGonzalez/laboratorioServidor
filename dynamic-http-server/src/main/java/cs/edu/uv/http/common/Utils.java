package cs.edu.uv.http.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class offers helper methods.
 * 
 * @author Juan Gutierrez Aguado (Dep. Informatica, Univ. Valencia, Spain)
 */
public class Utils {
	public static final String DELIM_ARRAY="|";
	

	public static String getDate(){
	  String formato="EEE, dd MMM yyyy HH:mm:ss zzz";
      return (new SimpleDateFormat(formato).format(new Date()));
	}
}	
