package cs.edu.uv.http.dynamicresponse;

public class JarAndClass {
	private String jarFile;
	private String classFile;

	public JarAndClass(String jar, String cl) {
		jarFile = jar;
		classFile = cl;
	}

	public String getJarFile() {
		return jarFile;
	}


	public String getClassFile() {
		return classFile;
	}

}
