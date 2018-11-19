package cs.edu.uv.http.dynamicresponse;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.net.Socket;

import cs.edu.uv.http.common.UtilsHTTP;

public class ThreadDynamic implements Runnable {
	private Socket canal;
	private String clase;
	private String request;

	public ThreadDynamic(Socket s, String cl, String req) {
		canal = s;
		clase = cl;
		request = req;
	}

	public void run() {
		try {
			Class<?> c = Class.forName(clase);
			System.out.println("   Creating dynamically an instance of " + c.getName());
			Constructor<?> con = c.getConstructor(new Class<?>[] {});
			ResponseClass rc = (ResponseClass) con.newInstance(new Object[] {});
			rc.setMethod(UtilsHTTP.getMethod(request));
			rc.setResource(UtilsHTTP.getResource(request));
			rc.setSocket(canal);
			rc.dealWithCall();
			System.out.println("   Finish dealing with call");
			canal.close();
			rc = null;
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				UtilsHTTP.writeResponseServerError(new PrintWriter(canal
						.getOutputStream()));
			} catch (Exception ex2) {
			}
		}
	}
}
