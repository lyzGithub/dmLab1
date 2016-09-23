package cleanSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
/**
 * 
 * 
*/
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
// import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class coreClean {
	private int allPaperNum;
	private HashMap<String,HashMap<String,Integer>> paperMap;//one paper, one key words is value list
	private HashMap<String,Integer> paperWordsNumMap;
	private List<String> allWordsList;//all words contain
	private String filePath = "ICML";
	public coreClean() throws IOException, InterruptedException{
		allPaperNum = 0;
		paperMap = new HashMap<String,HashMap<String,Integer>>();
		paperWordsNumMap = new HashMap<String,Integer>();
		allWordsList = new ArrayList<String>();
		traverseFolder(filePath);
	}
	
	public HashMap<String,HashMap<String,Integer>> getPapersMap(){
		return paperMap;
	}
	
	public HashMap<String,Integer> getPaperWordsForEach(){
		return paperWordsNumMap;
	}
	
	public List<String> getAllWordsMap(){
		Collections.sort(allWordsList);
		return allWordsList;
	}
	
	public int getPapersNum(){
		return allPaperNum;
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
					//System.out.println("文件:" + file2.getAbsolutePath());
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
						fileNum++;
						HashMap<String,Integer> paperWordsMap =readFileToMap(file2.getPath(),file.getName());
					    paperMap.put(file.getName(), paperWordsMap);
					}
				}
			}
		} else {
			System.out.println("文件不存在!");
		}
		System.out.println("文件夹共有:" + folderNum + ",文件共有:" + fileNum);
		allPaperNum = fileNum;
	}
   /* public static void tokenString(String str) throws IOException{
    	
        
        Properties props = new Properties();    
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");    // 七种Annotators
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);    // 依次处理
      
        String text = str;               // 输入文本
        text = massyCodeClean(text);
        //System.out.println(text + "dweqfwef");
        Annotation document = new Annotation(text);    // 利用text创建一个空的Annotation
        pipeline.annotate(document);                   // 对text执行所有的Annotators（七种）
        
        // 下面的sentences 中包含了所有分析结果，遍历即可获知结果。
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        
        HashMap<String,Integer> paperWordsMap = new HashMap<String,Integer>();
        HashMap<String,String> stopWordsMap = readStopWords();
        
        for(CoreMap sentence: sentences) {
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
            	String lemma = token.get(LemmaAnnotation.class); //还原词性
            	if(stopWordsMap.containsKey(lemma)){
            		continue;
            	}
            	if(false == paperWordsMap.containsKey(lemma)){
            		paperWordsMap.put(lemma, 1);
            	}
            	else {
            		int temp = paperWordsMap.get(lemma);
            		paperWordsMap.put(lemma, temp + 1);
            	}
            }
        }
        //System.out.println(paperWordsMap.toString());
        
      
    }
   */
    

    private   HashMap<String,Integer> readFileToMap(String filePath,String fileName) throws IOException{
    	int numOfWordsContain = 0;
    	
    	HashMap<String,Integer> paperWordsMap = new HashMap<String,Integer>();
    	HashMap<String,String> stopWordsMap = readStopWords();
        File file = new File(filePath);
        FileInputStream is=new FileInputStream(file);
        InputStreamReader isr= new InputStreamReader(is);
        BufferedReader in= new BufferedReader(isr);
    	String line = null;
    	
    	Properties props = new Properties();    
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");    // 七种Annotators
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);    // 依次处理
		
    	while(null != (line = in.readLine())){
    		line = line.toLowerCase(); 
    		line = massyCodeClean(line);
    		
    		
    		Annotation document = new Annotation(line);    // 利用text创建一个空的Annotation
    		pipeline.annotate(document);                   // 对text执行所有的Annotators（七种）
    	  
    	        // 下面的sentences 中包含了所有分析结果，遍历即可获知结果。
    		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
    	        
    	   
    		for(CoreMap sentence: sentences) {
    	            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
    	            	String lemma = token.get(LemmaAnnotation.class); //还原词性
    	            	//System.out.println(lemma);
    	            	if(stopWordsMap.containsKey(lemma)){
    	            		continue;
    	            	}
    	            	//System.out.println(lemma);
    	            	numOfWordsContain ++;
    	            	if(false == paperWordsMap.containsKey(lemma)){
    	            		
    	            		paperWordsMap.put(lemma, 1);
    	            	}
    	            	else {
    	            		int temp = paperWordsMap.get(lemma);
    	            		paperWordsMap.put(lemma, temp + 1);
    	            	}
    	            	
    	            	if(false == allWordsList.contains(lemma)){
    	            		allWordsList.add(lemma);//添加到总词库
    	            	}
    	            }
    		}
    		
    		
    		
    	}
    	in.close();
        is.close();
        paperWordsNumMap.put(fileName, numOfWordsContain);
        //System.out.println("dealed paper done:"+filePath);
       // System.out.println("paper words: "+ paperWordsMap.toString());
       // System.out.println("all words:" + allWords.toString());
    	return paperWordsMap;
    }
    
    private  String massyCodeClean(String st){
    	String tt = "";
    	tt = st.toLowerCase();
    	StringBuilder bu = new StringBuilder(tt);
    	int length = bu.length();
    	for(int i = 0; i<length; i++){
    		//System.out.println(bu.charAt(i));
    		if((bu.charAt(i)>'z' || bu.charAt(i)<'a') &&  bu.charAt(i) != ' '){
    			//System.out.println(bu.charAt(i));
    			bu.deleteCharAt(i);
    			i--;
    			length --;
    		}
    	}
    	//System.out.println(bu.toString());
    	return bu.toString();
    	
    }
    
    private  HashMap<String,String> readStopWords() throws IOException{
    	File file = new File("stopWords.txt");
    	FileInputStream is=new FileInputStream(file);
        InputStreamReader isr= new InputStreamReader(is);
        BufferedReader in= new BufferedReader(isr);
     	String line = null;
     	HashMap<String,String> hm = new HashMap<String,String>();
     	while(null != (line = in.readLine())){
     		//System.out.println(line);
     		hm.put(line, line);
     	}
     	in.close();
        is.close();
        return hm;
    }
}