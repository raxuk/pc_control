package utils;

import java.io.IOException;

/**
 * Apaga el ordenar 
 * Multiplataforma
 * 
 * @author raxuk
 *
 */
public class ApagarOrdenador {

	public static void shutdown() {
		String shutdownCommand;
		String operatingSystem = System.getProperty("os.name").toLowerCase();

		if (operatingSystem.contains("linux") || operatingSystem.contains("mac")) {
			shutdownCommand = "shutdown -h now";
		} else if (operatingSystem.contains("windows")) {
			shutdownCommand = "shutdown -s -t 300";
		} else {
			throw new RuntimeException("Unsupported operating system.");
		}

		try {
			Runtime.getRuntime().exec(shutdownCommand);
		} catch (IOException e) {
			e.printStackTrace();	
		}
	}
}
