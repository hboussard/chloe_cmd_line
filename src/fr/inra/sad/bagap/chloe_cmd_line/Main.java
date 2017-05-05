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
			
			// r�cup�ration des cartes
			inputMatrix = getSplitString(args[index++]);
				
			// r�cup�ration du type de process
			processType = getString(args[index++]);
					
			// r�cup�ration des m�triques
			metrics = getSplitString(args[index++]);
					
			// r�cup�ration du csv output
			csvOutput = getString(args[index++]);
			
			// lancement du traitement apiland
			Model.runMapWindow(inputMatrix, processType, metrics, csvOutput);
					
			break;
			
		case "sliding" :
			// ex : chloe sliding {c://hugues/temp/chloe/carte.asc} qualitative circle null null {21} 1 false 100 {SHDI} c://hugues/temp/chloe/output/ true true null null
			// inputMatrix processType shape friction frictionMatrix windowSizes delta interpolate minRate metrics outputFolder exportCsv exportAscii filters unfilters
			
			index = 2;
			
			// r�cup�ration des cartes
			inputMatrix = getSplitString(args[index++]);
							
			// r�cup�ration du type de process
			processType = getString(args[index++]);
			
			// r�cup�ration de la forme de fen�tre
			shape = getString(args[index++]);
			
			// r�cup�ration du fichier de friction
			friction = getString(args[index++]);
			
			// r�cup�ration de la carte de friction
			frictionMatrix = getString(args[index++]);
			
			// r�cup�ration des tailles de fen�tres
			windowSizes = getSplitString(args[index++]);
			
			// r�cup�ration du delta
			delta = getString(args[index++]);
			
			// r�cup�ration de l'interpolation
			interpolate = getString(args[index++]);
			
			// r�cup�ration du minRate
			minRate = getString(args[index++]);
			
			// r�cup�ration des m�triques
			metrics = getSplitString(args[index++]);
			
			// r�cup�ration du dossier d'output
			outputFolder = getString(args[index++]);
								
			// r�cup�ration de l'export en csv
			exportCsv = getString(args[index++]);
			
			// r�cup�ration de l'export en ascii
			exportAscii = getString(args[index++]);
			
			// r�cup�ration des filtres
			filters = getSplitString(args[index++]);
						
			// r�cup�ration des unfiltres
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
