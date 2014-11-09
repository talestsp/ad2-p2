import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import util.UtilReader;
import util.UtilWriter;

public class Main {

	public static void main(String[] args) {

		// ArrayList<String> usaData = getUSAData();
		// UtilWriter.salvarLinhas(usaData,
		// "v1,artist_id,lat,lon,v4,artist_name",
		// "/home/tales/development/Git/ad2/ad2-p2/data/eua_artists.csv");
		UtilWriter.salvarLinhas(addCountryLocationData(), "\"V0\",\"V1\",\"V2\",\"V3\",\"V4\",\"V5\",\"V6\"", "/home/tales/development/Git/ad2/ad2-p2/data/subset_artist_location.csv");

	}

	public static ArrayList<String> addCountryLocationData() {
		ArrayList<String> linhas = UtilReader
				.getLinhas(
						"/home/tales/development/Git/ad2/ad2-p2/data/subset_artist_location.csv",
						false);
		ArrayList<String> novas_linhas = new ArrayList<String>();

		int cont = 0;
		
		for (String s : linhas) {
			cont ++;
			
			String country = "";

			try {

				String lat = s.split(",")[2];
				String lon = s.split(",")[3];
				String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="	+ lat + "%2C" + lon + "&sensor=false%22";
				String json = Jsoup.connect(url).ignoreContentType(true).execute().body();
				
				for (String parte : json.split("},")){
					if(parte.contains("country")){
						country = (parte.split(":")[1].split(",")[0].replace(" ", ""));
						break;
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			novas_linhas.add(s + "," + country);
			System.out.println(cont);
		}

		return novas_linhas;
	}

	public static ArrayList<String> getUSAData() {
		ArrayList<String> novas_linhas = new ArrayList<String>();
		ArrayList<String> linhas = UtilReader
				.getLinhas(
						"/home/tales/development/Git/ad2/ad2-p2/data/subset_artist_location.csv",
						false);

		double up = 49.433233;
		double down = 24.7754117;
		double right = -62.1428729;
		double left = -126.0327306;

		int cont_ok = 0;
		int cont_bad = 0;

		for (String s : linhas) {
			double lat = Double.valueOf(s.split(",")[2]);
			double lon = Double.valueOf(s.split(",")[3]);

			if (lat < up && lat > down && lon > left && lon < right) {
				// System.out.println("OK" + s.split(",")[2] + " " +
				// s.split(",")[3]);
				cont_ok++;
				novas_linhas.add(s);
				System.out.println(novas_linhas.get(novas_linhas.size() - 1));
			} else {
				// System.out.println("BAD" + s.split(",")[2] + " " +
				// s.split(",")[3]);
				cont_bad++;
			}
		}

		System.out.println("cont_ok " + cont_ok);
		System.out.println("cont_bad " + cont_bad);

		return novas_linhas;
	}

}
