package ui;

import io.UtilReader;
import io.UtilWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.google.api.GoogleAPIException;
import main.Analize;

public class UserInterface {

	public static void main(String[] args) throws IOException,
			GoogleAPIException {
		// arquivo de treino - ja presente
		 String trainingFileName = "/home/tales/development/Git/ad2/ad2-p2/AnaliseDeSentimento/AnaliseSentimento/data/trainingTest-EN.csv";

		// arquivo com as noticias a serem classificadas
		 String sourceFileName = "/home/tales/development/Git/ad2/ad2-p2/AnaliseDeSentimento/AnaliseSentimento/data/15noticias-EN.csv";

		// certificar que o CSV possui um cabeçalho
		Analize.initAnalize(trainingFileName, sourceFileName, false, false);
		
		
		//Seleciona noticias aleatorias para treino e source
		//Analize.setTrain(randomNoticias(UtilReader.getLinhas(trainingFileName, false), 50));
		//Analize.setSource(randomNoticias(UtilReader.getLinhas(sourceFileName, false), 20));

		// arquivo lexicon do SentiWordNet que esta na pasta do projeto /data/SWN
		String pathToSWN = "/home/tales/Documentos/AD1/p7/AnaliseSentimento/data/SWN/SentiWordNet_3.0.0_20130122.txt";
		Analize.setSWD(pathToSWN);

		Analize.train();

		// arquivo com a classificação das noticias
		ArrayList<String> scores = new ArrayList<String>();
		scores = Analize.getScores();

		//Analize.mostrarResultados(scores);
		
		//salvar CSV com as classificacoes no mesmo diretorio do source
		salvarResultados(scores, sourceFileName.replace(".csv", "-classificacao.csv"));
		
}

	private static void salvarResultados(ArrayList<String> scores,
			String fileName) {
		ArrayList<String> temp = new ArrayList<String>();

		for (String linha : scores) {
			String[] linhaSplit = linha.split(",");
			String pol = linhaSplit[0];
			Double sc = Double.parseDouble(linhaSplit[1]);

			if (sc > 0) {
				temp.add(pol + ", +, " + sc);
			} else {
				temp.add(pol + ", -, " + sc);
			}

		}
		
		UtilWriter.salvarLinhas(temp, fileName, true);
	}
	
	private static ArrayList<String> randomNoticias(ArrayList<String> source, int n){
		Random r = new Random();
		ArrayList<String> temp = new ArrayList<String>();
		
		for (int i = 0; i < n; i++) {
			int randIndex = r.nextInt(source.size());
			temp.add(source.get(randIndex));
			
		}
		return temp;
	}
}
