package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileMananger {

	public void criarDiretorio(String path) {

		Path diretorio = Paths.get(path);

		if (!Files.exists(diretorio)) {
			try {
				Files.createDirectories(diretorio);

				System.out.println("Diretório criado com sucesso!");
			} catch (Exception e) {
				System.out.println("Falha ao criar diretório.");
				e.printStackTrace();
			}
		} else {
			System.out.println("O diretório já existe.");
		}
	}

	public void criarArquivo(String path) {
		Path diretorio = Paths.get(path);

		if (!Files.exists(diretorio)) {
			try {
				Files.createFile(diretorio);
				System.out.println("Arquivo criado com sucesso!");
			} catch (Exception e) {
				System.out.println("Falha ao criar arquivo.");
				e.printStackTrace();
			}
		} else {
			System.out.println("O arquivo já existe.");
		}
	}

	public void excluirArquivo(String path) throws FileNotFoundException {
		File file = new File(path);
		if (file.exists()) {
			if (file.getParentFile().canWrite()) {
				file.delete();
			}
		} else {
			throw new FileNotFoundException("Erro ao deletar arquivo");
		}
	}

	public void escreverNoArquivo(String path, String text) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
			writer.write(text);
			writer.newLine();
			System.out.println("Texto gravado no arquivo com sucesso!");
		} catch (IOException e) {
			System.out.println("Erro ao escrever no arquivo: " + e.getMessage());
		}
	}

	private void createConfigs() {
		String configs = "Processado=C:\\Teste\\Processado\nNão Processado=C:\\Teste\\NaoProcessado\\";
		criarDiretorio(("C:\\Teste"));
		criarDiretorio(("C:\\Teste\\Configuracao"));
		criarArquivo(("C:\\Teste\\Configuracao\\configs.txt"));
		escreverNoArquivo("C:\\Teste\\Configuracao\\configs.txt", configs);
	}

	private void readConfigs() throws Exception {
		FileReader fileReader = new FileReader("C:\\Teste\\Configuracao\\configs.txt");
		BufferedReader bufferedReader = new BufferedReader(fileReader);
				
		boolean containsNaoProcessado = false;
		
		if (fileReader.ready()) {
			String linha;
			while ((linha = bufferedReader.readLine()) != null) {
				if(linha.contains("Não Processado=")) {
					containsNaoProcessado = true;
				}
				criarDiretorio(linha.substring(linha.indexOf("=") + 1));
			}
		} else {
			// O arquivo está em branco
			bufferedReader.close();
			fileReader.close();
			throw new IOException("O arquivo está vazio.");
		}
		
		if(!containsNaoProcessado) {
			throw new Exception("Arquivo de configuração inválido");
		}
	}

	public void configProgram() {
		createConfigs();
		try {
			readConfigs();
		} catch (Exception e) {
			System.out.println("Erro ao configurar o programa");
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void copyFileToTestDirectory(String path) {
		String diretorioDestino = "C:\\Teste\\";

		try {
			Path caminhoOrigem = Paths.get("C:\\" + path);
			File arquivoOrigem = new File(path);
			File diretorioTeste = new File(diretorioDestino);

			if (!diretorioTeste.exists()) {
				diretorioTeste.mkdirs(); // Cria o diretório de teste, se ainda não existir
			}

			Path destino = diretorioTeste.toPath().resolve(arquivoOrigem.getName());

			Files.copy(caminhoOrigem, destino, StandardCopyOption.REPLACE_EXISTING);

			System.out.println("Arquivo copiado para o diretório de teste: " + diretorioTeste);
		} catch (InvalidPathException e) {
			System.err.println("Caminho inválido: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Ocorreu um erro ao copiar o arquivo: " + e.getMessage());
		}
	}

}
