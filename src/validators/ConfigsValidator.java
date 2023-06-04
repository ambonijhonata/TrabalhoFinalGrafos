package validators;

import java.io.FileReader;
import java.io.IOException;

public  class ConfigsValidator {
	public static void validateConfigs(FileReader fileReader) throws Exception {
		if(!fileReader.ready()) {
			throw new IOException("O arquivo está vazio.");
		}
	}
}
