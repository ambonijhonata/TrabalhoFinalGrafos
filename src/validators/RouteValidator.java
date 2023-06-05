package validators;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class RouteValidator implements Runnable {

	private String filePath;

	public RouteValidator(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// AQUI TERAO TODAS AS VALIDAÇÕES QUE VIRAO DO ARQUIVO
		// validarHeader();
		validateQtdNodesFomFile();
	}

	public void validateQtdNodesFomFile() {
		HashSet<String> nodes = new HashSet<>();
		String qtdNodesReferecenceHeaderString = null;

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				if (line.startsWith("00")) {
					qtdNodesReferecenceHeaderString = line.substring(2, 4);
					continue;
				}

				if (line.startsWith("01")) {
					String[] parts = line.split("=");
					String node1 = parts[0].substring(2);
					String node2 = parts[1];

					// Adiciona os nós ao HashSet
					nodes.add(node1);
					nodes.add(node2);
				}
			}

			if (Integer.parseInt(qtdNodesReferecenceHeaderString) != nodes.size()) {
				throw new IllegalArgumentException("Número total de nós inválido");
			}					
			
		} catch (Exception e) {
			System.out.println(filePath);
			e.printStackTrace();
			System.exit(0);
		}
	}
}
