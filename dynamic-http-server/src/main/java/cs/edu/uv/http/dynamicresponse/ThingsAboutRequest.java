package cs.edu.uv.http.dynamicresponse;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

import cs.edu.uv.http.common.Utils;

public class ThingsAboutRequest {
	private HashMap<String, String> params;
	private HashMap<String, String> headers;
	private InputStream in;

	public ThingsAboutRequest(HashMap<String, String> h,
			HashMap<String, String> p, InputStream in) {
		params = p;
		headers = h;
		this.in = in;
	}

	public String[] getParamValues(String c) {
		return params.get(c).split("\\"+Utils.DELIM_ARRAY);
	}
	
    public HashMap<String,String> getHeaders(){
		return headers;
	}

	public String getParam(String c) {
		return params.get(c);
	}

	public String getHeader(String c) {
		return headers.get(c);
	}

	public Set<String> getParamNames() {
		return params.keySet();
	}

	public Set<String> getPresentHeaders() {
		return headers.keySet();
	}

	public InputStream getInputStream() {
		return in;
	}


}
