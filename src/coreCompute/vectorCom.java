package coreCompute;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cleanSource.coreClean;

public class vectorCom{
	private String kernerPath = "ICML/7. Kernel Methods";
	
	private int allPaper = 0;
	private HashMap<String,HashMap<String,Integer>> paperMap;//one paper, one key words is value list
	private HashMap<String,Integer> paperWordsNumMap;//file name, word count.
	
	private coreClean cc;
	private List<String> allWordsList;//all words contain
	private List<String> kernerFileNameList;//
	public vectorCom() throws IOException, InterruptedException{
		/*cc = new coreClean();
		allPaper = cc.getPapersNum();
		paperMap = cc.getPapersMap();
		paperWordsNumMap = cc.getPaperWordsForEach();
		allWordsList = cc.getAllWordsMap();*/
		kernerFileNameList = new ArrayList<String>();
		traverseFolder(kernerPath);
		dealEachKerner7();
		
	}
	private  void traverseFolder(String path) throws IOException, InterruptedException {
			int fileNum = 0, folderNum = 0;
			File file = new File(path);
			if (file.exists()) {
				LinkedList<File> list = new LinkedList<File>();
				File[] files = file.listFiles();
				for (File file2 : files) {
					if (file2.isDirectory()) {
						//System.out.println("文件夹:" + file2.getAbsolutePath());
						list.add(file2);
						folderNum++;
					} else {
						//System.out.println("文件:" + file2.getName());
						kernerFileNameList.add(file2.getName());
						fileNum++;
					}
				}
				File temp_file;
				while (!list.isEmpty()) {
					temp_file = list.removeFirst();
					files = temp_file.listFiles();
					for (File file2 : files) {
						if (file2.isDirectory()) {
							//System.out.println("文件夹:" + file2.getAbsolutePath());
							list.add(file2);
							folderNum++;
						} else {
							//System.out.println("文件:" + file2.getPath());
							kernerFileNameList.add(file2.getName());
							fileNum++;
						}
					}
				}
			} else {
				System.out.println("文件不存在!");
			}
			Collections.sort(kernerFileNameList);
			System.out.println("文件夹共有:" + folderNum + ",文件共有:" + fileNum +kernerFileNameList.toString());
		}
	
	private void dealEachKerner7(){
		int i =0;
		for(i = 0; i<kernerFileNameList.size(); i++ ){
			String fileName = kernerFileNameList.get(i);
			System.out.println(fileName);
		}
	}
	
	
}