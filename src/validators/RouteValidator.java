package validators;

public class RouteValidator implements Runnable{
	
	private String filePath;
	public RouteValidator(String filePath) {
		this.filePath = filePath;		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//AQUI TERAO TODAS AS VALIDAÇÕES QUE VIRAO DO ARQUIVO
		validarLinhas(filePath);
	}
	
	public boolean validarLinhas(String path) {
		System.out.println(path);		
		
		return false;
	}
}
