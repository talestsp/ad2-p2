package limpeza;

import java.util.ArrayList;

public class Limpeza {
	
	public static String retirarNumeros(String text) {
		String textTemp = "";
		textTemp = text.replace("1", "");
		textTemp = textTemp.replace("2", "");
		textTemp = textTemp.replace("3", "");
		textTemp = textTemp.replace("4", "");
		textTemp = textTemp.replace("5", "");
		textTemp = textTemp.replace("6", "");
		textTemp = textTemp.replace("7", "");
		textTemp = textTemp.replace("8", "");
		textTemp = textTemp.replace("9", "");
		textTemp = textTemp.replace("0", "");
		
		return textTemp;
	}
	
	public static String retirarOutros(String text) {
		String textTemp = "";
		textTemp = text.replace("R$", "");
		textTemp = textTemp.replace("R $", "");
		textTemp = textTemp.replace("%", "");
		return textTemp;
	}

	public static String limparLinhaTrainigTest(String linha) {
		
		String[] linhaSplit = linha.split(",");
		String empresaNome1 = linhaSplit[0];
		String empresaNome2 = linhaSplit[1];
		String titulo = linhaSplit[2];
		String polaridade = linhaSplit[linhaSplit.length - 1].replace(" ", "");
		
		String textoSemVirgolas = "";
		
		for (int i = 3; i < linhaSplit.length - 1; i++) {
			textoSemVirgolas += linhaSplit[i] + " ";
		}
		
		textoSemVirgolas = retirarNumeros(textoSemVirgolas);
		textoSemVirgolas = retirarOutros(textoSemVirgolas);
		
		return (empresaNome1 + "," + empresaNome2 + "," + titulo + "," + textoSemVirgolas + "," + polaridade);

	}

	public static ArrayList<String> limpar(ArrayList<String> linhas) {
		ArrayList<String> linhasTemp = new ArrayList<String>();
		
		for (String linha : linhas) {
			
			linhasTemp.add(limparLinhaTrainigTest(linha));
		}
		
		
		return linhasTemp;
	}

}
