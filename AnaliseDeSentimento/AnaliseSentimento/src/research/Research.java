package research;

import io.UtilWriter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import main.Analize;

public class Research {
	
	private static ArrayList<String> swnScoreTable = new ArrayList<String>();
	private static ArrayList<String> source;
	
	public static void main(String[] args) throws IOException {
		String pathToTraining = "/home/tales/Documentos/AD1/p7/AnaliseSentimento/data/trainingTest-EN.csv";
//		source = Analize.initAnalize(pathToTraining);
		
		String pathToSWN = "/home/tales/Documentos/AD1/p7/AnaliseSentimento/data/SWN/SentiWordNet_3.0.0_20130122.txt";
		Analize.setSWD(pathToSWN);
		
		UtilWriter.salvarLinhas(generateScoreTable(), "/home/tales/Documentos/AD1/p7/AnaliseSentimento/data/research/swn_score_table2.csv",true);
		System.out.println("fim - Research");

	}

	public static ArrayList<String> generateScoreTable(){
		ArrayList<String> scoreTable = new ArrayList<String>();
		scoreTable.add("polaridade, tituloA, tituloN, tituloV, textoA, textoN, textoV");
//		
//		for (String noticia : source) {
//			String[] noticiaSplit = noticia.split(",");
//			
//			String polaridade = noticiaSplit[4].replace(" ", "");
//			if(polaridade.equals("+")){
//				polaridade = "+";
//			}else if(polaridade.equals("-")){
//				polaridade = "-";
//			}
//			
//			String titulo = (noticiaSplit[2]);
//			double tituloA = Analize.sentiwordnet.getScoreA(titulo.split(" "));
//			double tituloN = Analize.sentiwordnet.getScoreN(titulo.split(" "));
//			double tituloV = Analize.sentiwordnet.getScoreV(titulo.split(" "));
//			
//			String texto = (noticiaSplit[3]);
//			double textoA = Analize.sentiwordnet.getScoreA(texto.split(" "));
//			double textoN = Analize.sentiwordnet.getScoreN(texto.split(" "));
//			double textoV = Analize.sentiwordnet.getScoreV(texto.split(" "));
//			
//			System.out.println(polaridade + "," + tituloA + ", " + tituloN + ", " + tituloV + ", " + textoA + ", " + textoN + ", " + textoV);
//			scoreTable.add("\"" + polaridade + "\"" + "," + "\"" +tituloA + "\"" + ", " + "\"" +tituloN + "\"" + ", " + "\"" +tituloV + "\"" + ", " + "\"" +textoA + "\"" + ", " + "\"" +textoN + "\"" + ", " + "\"" +textoV + "\"");
//		}
		
		return scoreTable;
		
	}
	
}
