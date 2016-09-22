package mainSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cleanSource.coreClean;

public class doMain{
	
	
	public static int allPaper = 0;
	public static void main(String args[]) throws IOException{


		
		//coreClean.readFileToBuffer();
		//coreClean.clean();
		//coreClean.readStopWords();
		coreClean.traverseFolder1("ICML");
		//coreClean.massyCodeClean();
		/*
		
		System.out.println(allPaper);
		*/
	}
	
	
}