package validators;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

import controller.ApplicationConfigurator;

public class RouteValidator implements Runnable {

	private String filePath;
	private String naoProcessadoPath = "C:\\Teste\\NaoProcessado";
	private String processadoPath = "C:\\Teste\\Processado";	
	
	public RouteValidator(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// AQUI TERAO TODAS AS VALIDAÇÕES QUE VIRAO DO ARQUIVO
		// validarHeader();
		try {
			validateHeaderSize();
			validateQtdNodesFomFile();
			validateSomaPesos();			
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
	}

	public void finalizarExecucao() {
		try {
			ApplicationConfigurator.copyFile(filePath, processadoPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void validateQtdNodesFomFile() throws IllegalArgumentException, IOException {
		HashSet<String> nodes = new HashSet<>();
		String qtdNodesReferecenceHeaderString = null;

		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line;
		int contLinhas = 0;		
		;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("00")) {
				qtdNodesReferecenceHeaderString = line.substring(2, 4);
				continue;
			}
			
			if (line.startsWith("01")) {								
				if (line.length() > 7 || line.length() < 7) {
					reader.close();
					ApplicationConfigurator.copyFile(filePath, naoProcessadoPath);
					throw new IllegalArgumentException("Arquivo: " + filePath
							+ "\nO sistem não está preparado para aceitar uma quantidade maior de caracteres no resumo de conexões.");
				}

				String[] parts = line.split("=");
				String node1 = parts[0].substring(2);
				String node2 = parts[1];

				// Adiciona os nós ao HashSet
				nodes.add(node1);
				nodes.add(node2);

				contLinhas++;
			}
		}			

		if (Integer.parseInt(qtdNodesReferecenceHeaderString) != nodes.size()) {
			reader.close();
			ApplicationConfigurator.copyFile(filePath, naoProcessadoPath);
			throw new IllegalArgumentException("Arquivo: " + filePath + "\nTotal de nós inválido");
		}
		
		if(contLinhas != getQtdLinesResumoConexoesAtLastLine()) {
			reader.close();
			ApplicationConfigurator.copyFile(filePath, naoProcessadoPath);
			throw new IllegalArgumentException("Arquivo: " + filePath + "\nNúmero de linhas "
					+ "do resumo de conexões diferente da referência do Trailer.");
		}
		
		reader.close();
	}

	public void validateSomaPesos() throws IllegalArgumentException, IOException {
		int somaPesosReferecenceHeaderString = 0;
		int somaPesosArquivo = 0;

		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line;
		int contLinhas = 0;
		
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("00")) {
				somaPesosReferecenceHeaderString = Integer.parseInt(line.substring(5));
				continue;
			}

			if (line.startsWith("02")) {
				contLinhas++;
				String[] parts = line.split("=");
				somaPesosArquivo += Integer.parseInt(parts[1]);
			}
		}

		if (somaPesosReferecenceHeaderString != somaPesosArquivo) {
			reader.close();
			ApplicationConfigurator.copyFile(filePath, naoProcessadoPath);
			throw new IllegalArgumentException("Arquivo: " + filePath + "\nSoma dos pesos difere(Valor do header: "
					+ somaPesosReferecenceHeaderString + " e Soma dos pesos = " + somaPesosArquivo + ")");
		}
		
		if(contLinhas != getQtdLinesResumoPesosAtLastLine()) {
			reader.close();
			ApplicationConfigurator.copyFile(filePath, naoProcessadoPath);
			throw new IllegalArgumentException("Arquivo: " + filePath + "\nNúmero de linhas "
					+ "do resumo de pesos diferente da referência do Trailer.");
		}

		reader.close();
		finalizarExecucao();
	}

	public void validateHeaderSize() throws IllegalArgumentException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line;

		while ((line = reader.readLine()) != null) {
			if (line.startsWith("00")) {
				if (line.length() > 8 || line.length() < 8) {
					reader.close();
					ApplicationConfigurator.copyFile(filePath, naoProcessadoPath);
					throw new IllegalArgumentException(
							"Arquivo: " + filePath + "\nO " + "sistema não está preparado para aceitar uma quantidade "
									+ "maior de caracteres no HEADER.");
				} else {
					break;
				}
			}
		}

		reader.close();
	}
	
	public int getQtdLinesResumoConexoesAtLastLine() {
		Path path = Path.of(filePath);
		int retorno = 0;
		try {
			List<String> lines = Files.readAllLines(path);
			String lastLine = lines.get(lines.size() - 1).substring(5,7);
			retorno = Integer.parseInt(lastLine);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retorno;
	}
	
	public int getQtdLinesResumoPesosAtLastLine() {
		Path path = Path.of(filePath);
		int retorno = 0;
		try {
			List<String> lines = Files.readAllLines(path);
			String lastLine = lines.get(lines.size() - 1).substring(11,13);
			retorno = Integer.parseInt(lastLine);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
	
}
