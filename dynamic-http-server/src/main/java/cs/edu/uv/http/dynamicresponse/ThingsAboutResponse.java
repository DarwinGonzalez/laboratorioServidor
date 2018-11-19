package cs.edu.uv.http.dynamicresponse;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import cs.edu.uv.http.common.UtilsHTTP;

public class ThingsAboutResponse {
	private OutputStream out;
	private PrintWriter pw;
	private HashMap<String, String> headers;
	private boolean statusSet = false;
	private boolean headersSet = false;

	public ThingsAboutResponse(OutputStream o) {
		out = o;
		pw = new PrintWriter(out);
		headers = new HashMap<String, String>();
	}

	public OutputStream getOutputStream() {
		return out;
	}

	public PrintWriter getWriter() {
		return pw;
	}

	public void setStatus(int s) {
		if (!statusSet) {
			switch (s) {
			case 200:
				pw.print("HTTP/1.1 " + s + " OK");
				pw.print('\r');
				pw.print('\n');
				break;
			case 400:
				pw.print("HTTP/1.1 " + s + " Bad request");
				pw.print('\r');
				pw.print('\n');
				break;
			case 500:
				pw.print("HTTP/1.1 " + s + " Internal error");
				pw.print('\r');
				pw.print('\n');
				break;
			}
			statusSet = true;
		}

	}

	public void flushResponseHeaders() {
		if (!headersSet) {
			headersSet = true;
			if (!statusSet) {
				pw.print("HTTP/1.1 200 OK");
				pw.print('\r');
				pw.print('\n');
				statusSet = true;
			}
			headers.put("Date", UtilsHTTP.getDate());
			headers.put("Connection", "close");

			for (String h : headers.keySet()) {
				pw.print(h);
				pw.print(": ");
				pw.print(headers.get(h));
				pw.print('\r');
				pw.print('\n');
			}
			pw.print('\r');
			pw.print('\n');
			pw.flush();
		}
	}

	public void setResponseHeader(String h, String v) {
		headers.put(h, v);
	}
}
