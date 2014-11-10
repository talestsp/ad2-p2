package io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Classe responsável por ler arquivos de texto.
 * @author Tales Pimentel
 *
 */

public class UtilReader {

	/**
	 * Retorna uma ArrayList<String> com todas as linhas do documento de texto
	 * @param path - endereço completo para o arquivo
	 * @param cabecalho - true, se a primeira linha deve ser lida também
	 * @return
	 */
	public static ArrayList<String> getLinhas(String path, boolean cabecalho) {
		BufferedReader arq;
		ArrayList<String> linhas = new ArrayList<String>();

		try {
			arq = abrirSource(path);
			String nextLine = "";
			if (!cabecalho) {
				// dipensar o cabe�alho
				nextLine = arq.readLine();
			}
			nextLine = arq.readLine();

			do {
				if (nextLine != null && nextLine != "") {
					String linha = nextLine;
					linhas.add(linha);
					nextLine = arq.readLine();
				}
			} while (nextLine != null);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return linhas;
	}
	
	private static BufferedReader abrirSource(String path)
			throws FileNotFoundException, UnsupportedEncodingException {
		FileInputStream stream = new FileInputStream(path);
		InputStreamReader streamReader = new InputStreamReader(stream);
		BufferedReader reader = new BufferedReader(streamReader);

		esperaUmPouco();
		
		return reader;
	}

	/**
	 * 	As vezes dá problema ao ler uma sequencia considerável de arquivos, este pequeno delay resolve isso
	 */
	private static void esperaUmPouco() {
		long tStart = System.currentTimeMillis();
		long tEnd = System.currentTimeMillis();
		long tDelta = tEnd - tStart;
		while(tDelta < 1){
			//esperar 10 milisegundos
			tEnd = System.currentTimeMillis();
			tDelta = tEnd - tStart;
			
		}
		
	}

}

