package main;

import facade.GTranslateAPIFacade;
import io.UtilReader;
import io.UtilWriter;
import java.io.IOException;
import java.util.ArrayList;
import limpeza.Limpeza;
import com.google.api.GoogleAPIException;
import com.google.api.translate.Language;
import core.SentiWordNet;
import core.SentiWordNetTrain;

public class Analize {

	private static ArrayList<String> train;
	private static ArrayList<String> source;
	private static SentiWordNet sentiwordnet;
	private static SentiWordNetTrain swnTrain;

	public static void initAnalize(String trainningFileName, String sourceFileName, boolean traduzirTreino, boolean traduzirSource) throws IOException, GoogleAPIException {
		train = UtilReader.getLinhas(trainningFileName, false);
	//	train = Limpeza.limpar(train);
		if (traduzirTreino) {
			train = traduzirNoticias(train, Language.PORTUGUESE, Language.ENGLISH);
			String trainningFileNameEN = trainningFileName.replace(".csv", "-EN.csv");
			UtilWriter.salvarLinhas(Limpeza.limpar(train), trainningFileNameEN, true);
		}

		source = UtilReader.getLinhas(sourceFileName, false);
	//	source = Limpeza.limpar(source);
		if (traduzirSource) {
			source = traduzirNoticias(source, Language.PORTUGUESE, Language.ENGLISH);
			String sourceFileNameEN = sourceFileName.replace(".csv", "-EN.csv");
			UtilWriter.salvarLinhas(Limpeza.limpar(source), sourceFileNameEN, true);	
		}
		
		
	}
	
	public static void setSWD(String pathToSWN){
		sentiwordnet = new SentiWordNet(pathToSWN);
		swnTrain = new SentiWordNetTrain(sentiwordnet);
	}
	
	public static ArrayList<String> getScores(){
		return swnTrain.analizar();
	}
	
	public static void train(){
		swnTrain.train(source);
	}
	
	public static void salvarNoticias(ArrayList<String> noticias){
		UtilWriter.salvarLinhas(noticias, "/home/tales/Documentos/AD1/p7/AnaliseSentimento/data/trainingTest-EN.csv", false);
	}
	
	public static void mostrarResultados(ArrayList<String> scores) {
		int acertos = 0;
		int contadorLinha = 1;
		
		System.out.println();
		System.out.println("polaridade_original, polaridade_inferida, score");
		for (String linha : scores) {
			String[] linhaSplit = linha.split(",");
			String pol = linhaSplit[0];
			Double sc = Double.parseDouble(linhaSplit[1]);
			
			if(sc > 0){
				System.out.println(contadorLinha + ") (" + pol + ") (+) " + sc.toString());
			}else{
				System.out.println(contadorLinha + ") (" + pol + ") (-) " + sc.toString());
			}
			
			if (sc > 0 && pol.equals("+")) {
				acertos++;
			}
			
			if (sc < 0 && pol.equals("-")){
				acertos++;
			}
			
			
			contadorLinha++;
		}
		
		Double acertoPercent = new Double (new Double(acertos) / scores.size()) * 100;
		
		System.out.println("Acerto: " + acertoPercent.toString() + "%");
	}
	
	private static ArrayList<String> traduzirNoticias(ArrayList<String> noticias, Language fromLanguage, Language toLanguage) throws GoogleAPIException{
		ArrayList<String> traduzidas = new ArrayList<String>();
		
		for (String string : noticias) {
			
			String[] stringSplit = string.split(",");
			String nomeEmpresa1 = stringSplit[0];
			String nomeEmpresa2 = stringSplit[1];
			String titulo = stringSplit[2];
			String texto = stringSplit[3];
			String polaridade = stringSplit[4];
			try {
				if(texto.length() > 5000){
					texto = texto.substring(0, 5000);
				}
				titulo = GTranslateAPIFacade.translate(titulo.replace("\"", "").replace("'", "").replace("%", "").replace("+", ""), fromLanguage, toLanguage);
				texto = GTranslateAPIFacade.translate(texto.replace("\"", "").replace("'", "").replace("%", "").replace("+", ""), fromLanguage, toLanguage);
				System.out.println("Traduzindo: " + titulo + "," + polaridade);
				traduzidas.add(nomeEmpresa1 + "," + nomeEmpresa2 + "," + titulo.replace(",", "") + "," + texto.replace(",", "") + "," + polaridade);
				
			} catch (Exception e) {				
				traduzidas.add(nomeEmpresa1 + "," + nomeEmpresa2 + "," + "NULL" + "," + "NULL" + "," + polaridade);
				System.err.println("Erro em " + titulo);
				System.err.println("Null inserido no lugar do titulo e texto.");
				System.err.println("Possível problema na cota de requisições.");
				System.err.println("Contactar tales.tsp@gmail.com");
			}
			
			long tStart = System.currentTimeMillis();
			long tEnd = System.currentTimeMillis();
			long tDelta = tEnd - tStart;
			while(tDelta < 250){
				tEnd = System.currentTimeMillis();
				tDelta = tEnd - tStart;
				
			}
		}	
		
		System.out.println();
		
		return traduzidas;
	}
	
	public static void setSource(ArrayList<String> s){
		source = s;
	}
	
	public static void setTrain(ArrayList<String> t){
		train = t;
	}
}
