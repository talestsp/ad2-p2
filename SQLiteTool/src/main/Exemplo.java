package main;

import static java.lang.System.err;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import util.UtilReader;
import util.UtilWriter;

class Exemplo {
	private static Connection[] CONNECTIONS = new Connection[3];

	public static void main(String[] args) {

		try {
			// conexao com os três DBs
			CONNECTIONS[0] = DriverManager.getConnection("jdbc:sqlite:db/" + "subset_track_metadata.db");
			CONNECTIONS[1] = DriverManager.getConnection("jdbc:sqlite:db/" + "subset_artist_term.db");
			CONNECTIONS[2] = DriverManager.getConnection("jdbc:sqlite:db/" + "subset_artist_similarity.db");

			executeQueries(CONNECTIONS[1]);
			saveQueryAsCSV("SELECT * FROM artist_mbtag", CONNECTIONS[1]);

		} catch (Exception e) {
			err.println(e.getClass().getName() + ": " + e.getMessage());

		} finally {
			for (Connection conn : CONNECTIONS) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void saveQueryAsCSV(String query, Connection conn)
			throws SQLException {
		
		ArrayList<String> linhas = new ArrayList<String>();
		
		for (String result : runQuery(CONNECTIONS[1], query)) {
			linhas.add(result);

		}
		
		String header = "artist_id, mbtag";
		
		UtilWriter.salvarLinhas(linhas, header, "/home/tales/development/Git/ad2/ad2-p2/data/artist_mbtag.csv");

	}

	private static void executeQueries(Connection conn) throws SQLException {

		for (String query : readQueries()) {
			System.out.println(query);
			for (String result : runQuery(conn, query)) {
				System.out.println(result);
			}
			System.out.println("--/--");
		}

	}

	/**
	 * Carrega as consultas que estão em "/src/queries/queries.txt"
	 * 
	 */
	private static ArrayList<String> readQueries() {
		ArrayList<String> queries = new ArrayList<String>();
		String filePath = new File("").getAbsolutePath();
		filePath = filePath.concat("/src/queries/queries.txt");
		ArrayList<String> linhas = UtilReader.getLinhas(filePath, true);

		for (String linha : linhas) {
			queries.add(linha);
		}

		return queries;
	}

	/**
	 * Retorna o resultado da consulta passada. Parametros: Conexao com o DB e a
	 * consulta SQL
	 * 
	 */
	private static List<String> runQuery(Connection conn, String query)
			throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet resultSet = stmt.executeQuery(query);

		List<String> results = new ArrayList<String>();
		while (resultSet.next()) {
			String result = "";
			int index = 1;
			try {
				while (true) {
					result += resultSet.getString(index) + ", ";
					index++;
				}
			} catch (SQLException e) {
				if (!result.equals("")) {
					result = result.substring(0, result.length() - 2);
				}
				results.add(result);
			}
		}
		stmt.close();
		return results;
	}
}
