package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import validators.ConfigsValidator;

public class ApplicationConfigurator {

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
		String configs = "Processado=C:\\Teste\\Processado\nNão Processado=C:\\Teste\\NaoProcessado";
		criarDiretorio(("C:\\Teste"));
		criarDiretorio(("C:\\Teste\\Configuracao"));
		criarArquivo(("C:\\Teste\\Configuracao\\configs.txt"));
		escreverNoArquivo("C:\\Teste\\Configuracao\\configs.txt", configs);
	}

	private void readConfigs() throws Exception {
		FileReader fileReader = new FileReader("C:\\Teste\\Configuracao\\configs.txt");
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		boolean containsNaoProcessado = false;
		boolean containsProcessado = false;

		ConfigsValidator.validateConfigs(fileReader);

		String linha;
		while ((linha = bufferedReader.readLine()) != null) {
			if (linha.contains("Não Processado=")) {
				containsNaoProcessado = true;
			} else if (linha.contains("Processado=")) {
				containsProcessado = true;
			}

			if (linha.startsWith("00")) {
				System.out.println("here");
			} else if (linha.startsWith("01")) {
				System.out.println("here 2");
			}

			criarDiretorio(linha.substring(linha.indexOf("=") + 1));
		}

		bufferedReader.close();
		fileReader.close();

		if (!containsNaoProcessado || !containsProcessado) {
			throw new Exception("Arquivo de configuração inválido");
		}
	}

	public void configProgram(String pathRotas) {
		try {
			createConfigs();
			readConfigs();
			copyFileToTestDirectory(pathRotas);
		} catch (Exception e) {
			System.out.println("Erro ao configurar o programa");
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void copyFileToTestDirectory(String path) {
		try {
			File diretorioOrigem = new File(path);
			File[] arquivos = diretorioOrigem.listFiles();

			if (arquivos != null) {
				for (File arquivo : arquivos) {
					if (arquivo.isFile() && arquivo.getName().matches("rota\\d{2}\\.txt")) {
						Path origem = arquivo.toPath();
						Path destino = Path.of("C:\\Teste", arquivo.getName());

						Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Ocorreu um erro ao copiar o arquivo: " + e.getMessage());
		}
	}	
}
