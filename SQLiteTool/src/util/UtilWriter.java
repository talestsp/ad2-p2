package util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class UtilWriter {

	public static void salvarLinhas(ArrayList<String> linhas, String header,
			String sFileName) {
		try {

			OutputStreamWriter bufferOut = new OutputStreamWriter(
					new FileOutputStream(sFileName), Charset.forName("UTF-8")
							.newEncoder());

			if (header != null) {
				bufferOut.write(header);
				bufferOut.write("\n");
			}

			for (String linha : linhas) {
				bufferOut.write(linha);
				bufferOut.write("\n");
			}

			bufferOut.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
