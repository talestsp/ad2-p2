package io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * 
 * @author Tales Pimentel
 * 
 */

public class UtilWriter {

	public static void salvarException(Exception e, String url, String sFileName) {
		try {

			OutputStreamWriter bufferOut = new OutputStreamWriter(
					new FileOutputStream(sFileName + ".txt"), Charset.forName(
							"UTF-8").newEncoder());

			bufferOut.write("<" + url + ">");
			bufferOut.write("\n");
			bufferOut.write(e.toString());
			bufferOut.write("\n");

			bufferOut.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void salvarLinhas(ArrayList<String> linhas, String sFileName,
			boolean cabecalho) {
		try {

			OutputStreamWriter bufferOut = new OutputStreamWriter(
					new FileOutputStream(sFileName), Charset.forName("UTF-8")
							.newEncoder());

			if (cabecalho) {
				for (String linha : linhas) {
					bufferOut.write(linha);
					bufferOut.write("\n");
				}

			} else {
				for (int i = 1; i < linhas.size(); i++) {
					bufferOut.write(linhas.get(i));
					bufferOut.write("\n");
				}
			}

			bufferOut.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void salvarHashMapDeArrayList(
			HashMap<String, ArrayList<String>> mapa, String sFileName) {
		try {

			OutputStreamWriter bufferOut = new OutputStreamWriter(
					new FileOutputStream(sFileName), Charset.forName("UTF-8")
							.newEncoder());

			for (String empresa : mapa.keySet()) {
				System.out.println(empresa
						+ ":"
						+ Arrays.toString(mapa.get(empresa).toArray())
								.replace("[", "").replace("]", ""));
				bufferOut.write(empresa
						+ ":"
						+ Arrays.toString(mapa.get(empresa).toArray())
								.replace("[", "").replace("]", ""));
				bufferOut.write("\n");
			}

			bufferOut.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void salvarMapaDeMapa(
			HashMap<String, HashMap<String, Integer>> mapaEmpPalavra,
			String sFileName) {
		try {

			OutputStreamWriter bufferOut = new OutputStreamWriter(
					new FileOutputStream(sFileName + ".txt"), Charset.forName(
							"UTF-8").newEncoder());

			for (String empresa : mapaEmpPalavra.keySet()) {
				HashMap<String, Integer> palavrasDaEmpresa = mapaEmpPalavra
						.get(empresa);
				for (String palavra : palavrasDaEmpresa.keySet()) {
					int quantidade = palavrasDaEmpresa.get(palavra);
					bufferOut.write(empresa + "," + palavra + "," + quantidade);
					bufferOut.write("\n");
				}
			}

			bufferOut.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
