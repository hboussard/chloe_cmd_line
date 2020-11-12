/* Copyright SAD Paysage - INRA
 * contributeur : Hugues Boussard,
 * 				  Nicolas Schermann,
 * 				  Jacques Baudry
 * 
 * date de cr�ation : 08/2006
 * 
 * Ce logiciel est un programme informatique servant � effectuer des analyses
 * spatiales multi-�chelles sur des cartes.
 * 
 * Ce logiciel est r�gi par la licence CeCILL soumise au droit fran�ais et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffus�e par le CEA, le CNRS et l'INRIA
 * sur le site "http://www.cecill.info".
 * 
 * En contrepartie de l'accessibilit� au code source et des droits de copie,
 * de modification et de redistribution accord�s par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limit�e.  Pour les m�mes raisons,
 * seule une responsabilit� restreinte p�se sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les conc�dants successifs.
 * A cet �gard  l'attention de l'utilisateur est attir�e sur les risques
 * associ�s au chargement,  � l'utilisation,  � la modification et/ou au
 * d�veloppement et � la reproduction du logiciel par l'utilisateur �tant
 * donn� sa sp�cificit� de logiciel libre, qui peut le rendre complexe �
 * manipuler et qui le r�serve donc � des d�veloppeurs et des professionnels
 * avertis poss�dant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invit�s � charger  et  tester  l'ad�quation  du
 * logiciel � leurs besoins dans des conditions permettant d'assurer la
 * s�curit� de leurs syst�mes et ou de leurs donn�es et, plus g�n�ralement,
 * � l'utiliser et l'exploiter dans les m�mes conditions de s�curit�.
 * 
 * Le fait que vous puissiez acc�der � cet en-t�te signifie que vous avez
 * pris connaissance de la licence CeCILL, et que vous en avez accept� les
 * termes.
 * 
*/
package fr.inra.sad.bagap.chloe.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ChloeContext implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static final String repSystem = "./system/";
	
	private static final String repFile = "./system/file/";

	private static final String repData = ".";
	
	private static final String repImages = "./system/images/"; 
	
	private static final String icon = repImages+"chloe_icon.jpg";
	
	private static final String logo = repImages+"chloe_logo.jpg";
	
	private static final String documentation = "system/documentation/documentation_chloe.pdf";
	
	private String repUserData = "";

	private static ChloeContext instance = new ChloeContext();
	
	private ChloeContext(){
		repUserData = repData;
	}

	public static ChloeContext get(){
		return instance;
	}
	
	public static void setInstance(ChloeContext ls){
		instance = ls;
	}

	public static void save() throws IOException{
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(repSystem+"local.ctx"));
		try{
			out.writeObject(ChloeContext.get());
		}
		finally{
			out.flush();
			out.close();
		}
	}
	
	public static void load() {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(repSystem+"local.ctx"));
			setInstance((ChloeContext)in.readObject());
			in.close();
		} catch (IOException e) {
			// do nothing
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
	}
	
	public void setRepData(String rep){
		repUserData = getFolder(rep);
	}

	public String getRepImages(){
		return repImages;
	}

	public String getRepData() {
		return repUserData;
	}

	public static String getRepsystem() {
		return repSystem;
	}

	public static String getRepfile() {
		return repFile;
	}
	
	public String getDocumentation(){
		return documentation;
	}

	private static String getFolder(String file){
		File f = new File(file);
		if(f.isDirectory()){
			return f.toString()+"/";
		}
		return f.getParent()+"/";
	}
	
	public String getIcon(){
		return icon;
	}
	
	public String getLogo(){
		return logo;
	}
	
}
