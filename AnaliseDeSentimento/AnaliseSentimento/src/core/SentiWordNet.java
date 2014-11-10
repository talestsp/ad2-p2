package core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SentiWordNet {

	private Map<String, Double> mapWordScore;
	
	public SentiWordNet(String pathToSWN) {

		mapWordScore = createMapWordScore(pathToSWN);
	}

	public double getScore(String[] texto, String[] pos) {
		double score = 0;

		for (int i = 0; i < texto.length; i++) {
			score += getWordScore(texto[i], pos[i]);
		}

		return score;
	}
	
	public double getScoreA(String[] texto) {
		double score = 0;

		for (int i = 0; i < texto.length; i++) {
			score += getWordScore(texto[i], "a");
		}

		return score;
	}
	
	public double getScoreN(String[] texto) {
		double score = 0;

		for (int i = 0; i < texto.length; i++) {
			score += getWordScore(texto[i], "n");
		}

		return score;
	}
	
	public double getScoreV(String[] texto) {
		double score = 0;

		for (int i = 0; i < texto.length; i++) {
			score += getWordScore(texto[i], "v");
		}

		return score;
	}

	/**
	 * If the word is not found it returns 0;
	 * 
	 * @param word
	 * @param pos
	 * @return
	 */
	public double getWordScore(String word, String pos) {
		if (mapWordScore.get(word + "#" + pos) == null) {
			return 0;
		}
		if(mapWordScore.get(word + "#" + pos) < 0){
			return new Double(1.25) * mapWordScore.get(word + "#" + pos);
		}
		
		
		return mapWordScore.get(word + "#" + pos);
	}

	private HashMap<String, Double> createMapWordScore(String pathToSWN)
			 {
		// This is our main dictionary representation
		HashMap<String, Double> mapWordScoreTemp = new HashMap<String, Double>();

		// From String to list of doubles.
		HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();

		BufferedReader csv = null;
		try {
			csv = new BufferedReader(new FileReader(pathToSWN));
			int lineNumber = 0;

			String line;
			while ((line = csv.readLine()) != null) {
				lineNumber++;

				// If it's a comment, skip this line.
				if (!line.trim().startsWith("#")) {
					// We use tab separation
					String[] data = line.split("\t");
					String wordTypeMarker = data[0];

					// Example line:
					// POS ID PosS NegS SynsetTerm#sensenumber Desc
					// a 00009618 0.5 0.25 spartan#4 austere#3 ascetical#2
					// ascetic#2 practicing great self-denial;...etc

					// Is it a valid line? Otherwise, through exception.
					if (data.length != 6) {
						throw new IllegalArgumentException(
								"Incorrect tabulation format in file, line: "
										+ lineNumber);
					}

					// Calculate synset score as score = PosS - NegS
					Double synsetScore = Double.parseDouble(data[2])
							- Double.parseDouble(data[3]);

					// Get all Synset terms
					String[] synTermsSplit = data[4].split(" ");

					// Go through all terms of current synset.
					for (String synTermSplit : synTermsSplit) {
						// Get synterm and synterm rank
						String[] synTermAndRank = synTermSplit.split("#");
						String synTerm = synTermAndRank[0] + "#"
								+ wordTypeMarker;

						int synTermRank = Integer.parseInt(synTermAndRank[1]);
						// What we get here is a map of the type:
						// term -> {score of synset#1, score of synset#2...}

						// Add map to term if it doesn't have one
						if (!tempDictionary.containsKey(synTerm)) {
							tempDictionary.put(synTerm,
									new HashMap<Integer, Double>());
						}

						// Add synset link to synterm
						tempDictionary.get(synTerm).put(synTermRank,
								synsetScore);
					}
				}
			}

			// Go through all the terms.
			for (Map.Entry<String, HashMap<Integer, Double>> entry : tempDictionary
					.entrySet()) {
				String word = entry.getKey();
				Map<Integer, Double> synSetScoreMap = entry.getValue();

				// Calculate weighted average. Weigh the synsets according to
				// their rank.
				// Score= 1/2*first + 1/3*second + 1/4*third ..... etc.
				// Sum = 1/1 + 1/2 + 1/3 ...
				double score = 0.0;
				double sum = 0.0;
				for (Map.Entry<Integer, Double> setScore : synSetScoreMap
						.entrySet()) {
					score += setScore.getValue() / (double) setScore.getKey();
					sum += 1.0 / (double) setScore.getKey();
				}
				score /= sum;

				mapWordScoreTemp.put(word, score);
			}
		} catch (Exception e) {
			System.err.print("Arquivo não encontrado!");
		} finally {
			if (csv != null) {
				try {
					csv.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return mapWordScoreTemp;
	}
}