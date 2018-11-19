package cs.edu.uv.http.dynamicresponse;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import cs.edu.uv.http.common.UtilsHTTP;

public class ResponseClass {
	private Socket canal;
	private String method;
	private String resource;

	void setSocket(Socket s) {
		canal = s;
	}

	void setMethod(String m) {
		method = m;
	}

	void setResource(String r) {
		resource = r;
	}

	public void ifGet(ThingsAboutRequest req, ThingsAboutResponse resp) throws Exception {
	}

	public void ifPost(ThingsAboutRequest req, ThingsAboutResponse resp) throws Exception {
	}

	public void dealWithCall() throws Exception {

		InputStream in = canal.getInputStream();
		OutputStream out = canal.getOutputStream();

		HashMap<String, String> headers = UtilsHTTP.getHeaders(in);
		HashMap<String, String> params = null;

		if (method.equals("GET"))
			params = UtilsHTTP.getParamsGet(resource);
		if (method.equals("POST"))
			if (!headers.get("Content-Type").contains("multipart"))
				params = UtilsHTTP.parseBody(UtilsHTTP.getBody(headers, in));

		ThingsAboutRequest req = new ThingsAboutRequest(headers, params, in);
		ThingsAboutResponse resp = new ThingsAboutResponse(out);

		UtilsHTTP.showHeaders(req.getHeaders());

		PrintWriter pw = new PrintWriter(out);
		UtilsHTTP.writeResponseLineOK(pw);

		if (method != null) {
			if (method.equals("GET")) {
				System.out.println("Calling ifGet...");
				ifGet(req, resp);
			} else if (method.equals("POST")) {
				String expect = req.getHeader("Expect");
				if (expect != null && expect.contains("100-continue")) {
					System.out.println("Sending write continue...");
					UtilsHTTP.writeContinue(pw);
					pw.close();
				} else {
					System.out.println("Calling ifPut...");
					ifPost(req, resp);
				}
			}
			// TODO Add more methods
		}

	}
}
