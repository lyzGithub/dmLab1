package mainSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

import cleanSource.coreClean;
import coreCompute.vectorCom;

public class doMain{
	
	public static void main(String args[]) throws IOException, InterruptedException{
		System.out.println("int the main");
		System.setOut(new PrintStream(new FileOutputStream("output.txt")));
		vectorCom vc = new vectorCom();
		vc.writeVerctorToDisk();
	}
	
	
}