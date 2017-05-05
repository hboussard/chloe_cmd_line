package fr.inra.sad.bagap.chloe_cmd_line;

import java.util.StringTokenizer;

public class Main {

	public static void main(String[] args) {
		treatParamsAndRun(args);
	}
	
	private static void treatParamsAndRun(String[] args){
		
		int index;
		
		String[] inputMatrix, metrics, windowSizes, filters, unfilters;
		
		String processType, csvOutput, shape, friction, frictionMatrix, delta, interpolate, minRate, exportCsv, exportAscii, outputFolder;
		
		switch(args[1]){
		case "map" : 
			// ex : chloe map {c://hugues/temp/chloe/carte.asc} qualitative {SHDI} c://hugues/temp/chloe/maptest.csv
					
			index = 2;
			
			// récupération des cartes
			inputMatrix = getSplitString(args[index++]);
				
			// récupération du type de process
			processType = getString(args[index++]);
					
			// récupération des métriques
			metrics = getSplitString(args[index++]);
					
			// récupération du csv output
			csvOutput = getString(args[index++]);
			
			// lancement du traitement apiland
			Model.runMapWindow(inputMatrix, processType, metrics, csvOutput);
					
			break;
			
		case "sliding" :
			// ex : chloe sliding {c://hugues/temp/chloe/carte.asc} qualitative circle null null {21} 1 false 100 {SHDI} c://hugues/temp/chloe/output/ true true null null
			// inputMatrix processType shape friction frictionMatrix windowSizes delta interpolate minRate metrics outputFolder exportCsv exportAscii filters unfilters
			
			index = 2;
			
			// récupération des cartes
			inputMatrix = getSplitString(args[index++]);
							
			// récupération du type de process
			processType = getString(args[index++]);
			
			// récupération de la forme de fenêtre
			shape = getString(args[index++]);
			
			// récupération du fichier de friction
			friction = getString(args[index++]);
			
			// récupération de la carte de friction
			frictionMatrix = getString(args[index++]);
			
			// récupération des tailles de fenêtres
			windowSizes = getSplitString(args[index++]);
			
			// récupération du delta
			delta = getString(args[index++]);
			
			// récupération de l'interpolation
			interpolate = getString(args[index++]);
			
			// récupération du minRate
			minRate = getString(args[index++]);
			
			// récupération des métriques
			metrics = getSplitString(args[index++]);
			
			// récupération du dossier d'output
			outputFolder = getString(args[index++]);
								
			// récupération de l'export en csv
			exportCsv = getString(args[index++]);
			
			// récupération de l'export en ascii
			exportAscii = getString(args[index++]);
			
			// récupération des filtres
			filters = getSplitString(args[index++]);
						
			// récupération des unfiltres
			unfilters = getSplitString(args[index++]);
			
			// lancement du traitement apiland
			Model.runSlidingWindow(inputMatrix, processType, shape, friction, frictionMatrix, windowSizes, delta, interpolate, minRate, metrics, outputFolder, 
					exportCsv, exportAscii, filters, unfilters);
			
			break;
			
		default : throw new IllegalArgumentException (args[1]+" n'est pas reconnu comme commande pour Chloe");	
		}
	}

	
	private static String getString(String param){
		if(!param.equalsIgnoreCase("null")){
			return param;
		}
		return "";
	}
	
	/**
	 * recuperation des String multiples
	 */
	private static String[] getSplitString(String param){
		if(!param.equalsIgnoreCase("null")){
			StringTokenizer st = new StringTokenizer(param.replace(" ", "").replace("{", "").replaceAll("}", ""));
			String[] tab = new String[st.countTokens()];
			int index = 0; 
			while(st.hasMoreTokens()){
				tab[index++] = st.nextToken();
			}
			return tab;
		}
		return null;
	}
	
}
