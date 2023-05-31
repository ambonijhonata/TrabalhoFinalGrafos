package application;

import controller.FileMananger;

public class Main {
	public static void main(String[] args) {		
		FileMananger fm = new FileMananger();
		//cria os diretorios, o .config, e os diretorios que constam nele
		fm.configProgram();		
		fm.copyFileToTestDirectory("a/rotas/rota01.txt");
		fm.copyFileToTestDirectory("a/rotas/rota02.txt");
		fm.copyFileToTestDirectory("a/rotas/rota03.txt");
		//copiar para o repositorio C:\Teste
	//	criarArquivo(("C:\\Teste\\Configuracao\\configs.txt"));	
	}
}
