package core;

import io.UtilWriter;

import java.util.ArrayList;
import java.util.Arrays;

public class SentiWordNetTrain {


	private SentiWordNet sentiwordnet;
	private Double[] coeficientes;
	private ArrayList<String> scoreTable;

	public SentiWordNetTrain(SentiWordNet sentiwordnet) {
		this.sentiwordnet = sentiwordnet;
	}

	public void train(ArrayList<String> source) {
		ArrayList<String> scoreTable = new ArrayList<String>();
		

		Double[] coeficientes = new Double[3];

		scoreTable = generateScoreTable(source);
		coeficientes = extractCoeficientes(scoreTable);
		
		this.coeficientes = coeficientes;
		this.scoreTable = scoreTable;
		System.out.println(">> Treino realizado.");
	}

	private Double[] extractCoeficientes(ArrayList<String> scoreTable) {
		ArrayList<String> polPos = new ArrayList<String>();
		ArrayList<String> polNeg = new ArrayList<String>();
		
		for (String linha : scoreTable) {
			String[] linhaSplit = linha.split(",");
			String polaridade = linhaSplit[0].replace("\"", "");
			
			if (polaridade.equals("+")) {
				polPos.add(linha);
			}else if(polaridade.equals("-")){
				polNeg.add(linha);
			}else{
				System.err.println(polaridade);
			}
			
		}
		
		int acertosPosTxA = 0;
		int acertosPosTxN = 0;
		int acertosPosTxV = 0;
		
		for (String linha : polPos) {
			String[] linhaSplit = linha.split(",");
			double scoreTxA = Double.parseDouble(linhaSplit[4].replace("\"", ""));
			double scoreTxN = Double.parseDouble(linhaSplit[5].replace("\"", ""));
			double scoreTxV = Double.parseDouble(linhaSplit[6].replace("\"", ""));
			
			if (scoreTxA > 0) {
				acertosPosTxA++;
			}
			if (scoreTxN > 0) {
				acertosPosTxN++;
			}
			if (scoreTxV > 0) {
				acertosPosTxV++;
			}
		}
		
		int acertosNegTxA = 0;
		int acertosNegTxN = 0;
		int acertosNegTxV = 0;
		
		for (String linha : polNeg) {
			String[] linhaSplit = linha.split(",");
			double scoreTxA = Double.parseDouble(linhaSplit[4].replace("\"", ""));
			double scoreTxN = Double.parseDouble(linhaSplit[5].replace("\"", ""));
			double scoreTxV = Double.parseDouble(linhaSplit[6].replace("\"", ""));
			
			if (scoreTxA < 0) {
				acertosNegTxA++;
			}
			if (scoreTxN < 0) {
				acertosNegTxN++;
			}
			if (scoreTxV < 0) {
				acertosNegTxV++;
			}
		}
				
		double cA = new Double(new Double(acertosNegTxA + acertosPosTxA) / scoreTable.size());
		double cN = new Double(new Double(acertosNegTxN + acertosPosTxN) / scoreTable.size());
		double cV = new Double(new Double(acertosNegTxV + acertosPosTxV) / scoreTable.size());
		
		Double[] taxas = {cA, cN, cV};
		double min = getMin(taxas);
		
		System.out.println("=====================================");
		System.out.println("Coeficientes " );
		
		System.out.println("cA " + cA);
		System.out.println("cN " + cN);
		System.out.println("cV " + cV);
		
		Double[] coefs = {new Double(cA - min), new Double(cN - min), new Double(cV - min)};
		
		coefs = normalize(coefs);
		
		System.out.println(Arrays.toString(coefs));
		
		return coefs;
	}
	
	public ArrayList<String> analizar(){
		
		if(coeficientes == null){
			System.err.println("Nenhum treino foi realizado antes!");
		}
		
		ArrayList<String> finalScore = new ArrayList<String>();
		double coefA = coeficientes[0];
		double coefN = coeficientes[1];
		double coefV = coeficientes[2];
		
		for (String linha : scoreTable) {
			
			System.out.println(linha);
			
			String[] linhaSplit = linha.replace("\"","").split(",");
			String pol = linhaSplit[0];
			double scoreTxtA = coefA * Double.parseDouble(linhaSplit[4]);
			double scoreTxtN = coefN * Double.parseDouble(linhaSplit[5]);
			double scoreTxtV = coefV * Double.parseDouble(linhaSplit[6]);
			double score = scoreTxtA + scoreTxtN + scoreTxtV;
			
			finalScore.add(pol + "," + score);
		}
		
		return finalScore;
	}
	
	private Double[] normalize(Double[] coefs) {
		Double[] temp = new Double[3];

		for (int k = 0; k < coefs.length; k++) {

			if (coefs[k] == 0) {
				temp[k] = new Double(1);
			} else {
				temp[k] = coefs[k] * 100;
			}

		}

		return temp;
	}

	private ArrayList<String> generateScoreTable(ArrayList<String> source) {
		ArrayList<String> scoreTable = new ArrayList<String>();
		ArrayList<String> salvar = new ArrayList<String>();
		
		// scoreTable
		// .add("polaridade, tituloA, tituloN, tituloV, textoA, textoN, textoV");

		for (String noticia : source) {
			String[] noticiaSplit = noticia.split(",");

//			String polaridade = noticiaSplit[4].replace(" ", "");
//
//			if (polaridade.equals("+")) {
//				polaridade = "+";
//			} else if (polaridade.equals("-")) {
//				polaridade = "-";
//			}

			// System.out.println(noticia);
			
			System.out.println(noticia);
			try{
				String titulo = (noticiaSplit[2]);
				double tituloA = sentiwordnet.getScoreA(titulo.split(" "));
				double tituloN = sentiwordnet.getScoreN(titulo.split(" "));
				double tituloV = sentiwordnet.getScoreV(titulo.split(" "));

				String texto = (noticiaSplit[3]);
				double textoA = sentiwordnet.getScoreA(texto.split(" "));
				double textoN = sentiwordnet.getScoreN(texto.split(" "));
				double textoV = sentiwordnet.getScoreV(texto.split(" "));
				
				System.out.println("polaridade" + "," + tituloA + ", " + tituloN
						 + ", " + tituloV + ", " + textoA + ", " + textoN + ", "
						 + textoV);
						scoreTable.add("\"" + "polaridade" + "\"" + "," + "\"" + tituloA
								+ "\"" + ", " + "\"" + tituloN + "\"" + ", " + "\""
								+ tituloV + "\"" + ", " + "\"" + textoA + "\"" + ", "
								+ "\"" + textoN + "\"" + ", " + "\"" + textoV + "\"");
						
				salvar.add(noticiaSplit[0] + "," + noticiaSplit[1] + "," + (textoA + textoN + textoV));
			
			}catch(Exception e){
				e.printStackTrace();
			}

		}
		
		UtilWriter.salvarLinhas(salvar, "/home/tales/development/Git/ad2/ad2-p2/AnaliseDeSentimento/AnaliseSentimento/data/resultados", true);

		return scoreTable;
	}

	private double getMin(Double[] array) {
		Arrays.sort(array);
		return array[0];
	}

}
