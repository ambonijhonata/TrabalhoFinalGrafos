package controller;

import java.io.File;

import validators.RouteValidator;

public class ValidateRoutesController {	
	
	public static void lerArquivos(String diretorioPath) {
		File diretorio = new File(diretorioPath);
		File[] arquivos = diretorio.listFiles();

		if (arquivos != null) {
			for (File arquivo : arquivos) {
				if (arquivo.isFile() && arquivo.getName().matches("rota\\d{2}\\.txt")) {
					String caminhoArquivo = arquivo.getAbsolutePath();					
					Thread routeValidatorThread = new Thread(new RouteValidator(caminhoArquivo));
					routeValidatorThread.start();
				}
			}
		}
	}
		
		
}
