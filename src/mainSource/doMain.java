package mainSource;
import java.io.File;
import java.io.FileNotFoundException;

class doMain{
	
	
	public static int allPaper = 0;
	public static void main(String args[]) throws FileNotFoundException{
		
		
		String filePath = "ICML";
		readFile(filePath);
		System.out.println(allPaper);
	}
	
	public static  StringBuilder readFile(String filePath) throws FileNotFoundException{
		
		StringBuilder re = new StringBuilder();
		File file = new File(filePath);
		if (!file.isDirectory()) {
		        System.out.println("文件");
		        System.out.println("path=" + file.getPath());
		        System.out.println("absolutepath=" + file.getAbsolutePath());
		        System.out.println("name=" + file.getName());
		        allPaper ++;

		} else if (file.isDirectory()) {
		        System.out.println("文件夹");
		        String[] filelist = file.list();
		        for (int i = 0; i < filelist.length; i++) {
		                File readfile = new File(filePath + "\\" + filelist[i]);
		                if (!readfile.isDirectory()) {
		                        System.out.println("path=" + readfile.getPath());
		                        System.out.println("absolutepath="
		                                        + readfile.getAbsolutePath());
		                        System.out.println("name=" + readfile.getName());
		                        allPaper ++;

		                } else if (readfile.isDirectory()) {
		                	readFile(filePath + "\\" + filelist[i]);
		                }
		        }

		}
		return re;
		
		
		
	}
}