package cleanSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
	public static int allPaper = 0;
	public static HashMap<String,HashMap<String,Integer>> paperMap = new HashMap<String,HashMap<String,Integer>>();//one paper, one key words is value list
	public static HashMap<String,String> allWords = new HashMap<String,String>();//all words contain
	
    public static void clean() throws IOException {
       
    }
    public static HashMap<String,HashMap<String,Integer>> paperToMaps(String filePath){
    	
    	
    	
    	return paperMap;
    	
    }
    public static void traverseFolder1(String path) throws IOException {
		int fileNum = 0, folderNum = 0;
		HashMap<String,String> stopWordsMap = readStopWords();
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
						HashMap<String,Integer> paperWordsMap = new HashMap<String,Integer>();
						readPaper(file2.getPath());
					    paperMap.put(file.getName(), paperWordsMap);
					}
				}
			}
		} else {
			System.out.println("文件不存在!");
		}
		System.out.println("文件夹共有:" + folderNum + ",文件共有:" + fileNum);
		allPaper = fileNum;
	}
    public static void readPaper(String filePath) throws IOException{
    	 /**
         * 创建一个StanfordCoreNLP object
         * tokenize(分词)、ssplit(断句)、 pos(词性标注)、lemma(词形还原)、
         * ner(命名实体识别)、parse(语法解析)、指代消解？同义词分辨？
         */
        StringBuffer paperBuffer = readFileToBuffer(filePath);
        String tempString = paperBuffer.toString();
       // System.
        System.out.println(tempString);
      
        
        
        
        
        Properties props = new Properties();    
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");    // 七种Annotators
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);    // 依次处理
      
        String text = "problem 伪虅 = [伪虅(1) , . . . , 伪虅(k) ], where 伪虅(c) is the optimal";               // 输入文本
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
   
    public static String massyCodeClean(String st){
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
    public static  StringBuffer readFileToBuffer(String filePath) throws IOException{
    	StringBuffer inBuffer = new StringBuffer();
    	  
        File file = new File(filePath);
        FileInputStream is=new FileInputStream(file);
        InputStreamReader isr= new InputStreamReader(is);
        BufferedReader in= new BufferedReader(isr);
    	String line = null;
    	while(null != (line = in.readLine())){
    		inBuffer.append(line);
    		//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+line);
    	}
    	in.close();
        is.close();
      
    	return inBuffer;
    }
    public static HashMap<String,String> readStopWords() throws IOException{
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