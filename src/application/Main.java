package application;

import controller.ApplicationConfigurator;
import controller.ValidateRoutesController;

public class Main {
	public static void main(String[] args) {		
		ApplicationConfigurator configurator = new ApplicationConfigurator();
		configurator.configProgram("C:\\a\\rotas");

		ValidateRoutesController.lerArquivos("C:\\Teste");
	}
}
