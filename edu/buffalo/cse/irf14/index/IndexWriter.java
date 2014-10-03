/**
 * 
 */
package edu.buffalo.cse.irf14.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import edu.buffalo.cse.irf14.analysis.AnalyzerFactory;
import edu.buffalo.cse.irf14.analysis.AuthorAnalyzer;
import edu.buffalo.cse.irf14.analysis.CategoryAnalyzer;
import edu.buffalo.cse.irf14.analysis.ContentAnalyzer;
import edu.buffalo.cse.irf14.analysis.NewsDateAnalyzer;
import edu.buffalo.cse.irf14.analysis.PlaceAnalyzer;
import edu.buffalo.cse.irf14.analysis.TitleAnalyzer;
import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.Tokenizer;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.TokenizerException;
import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.FieldNames;

/**
 * @author nikhillo
 * Class responsible for writing indexes to disk
 */
public class IndexWriter {
	
	private String indexDir;
	private int count;
	private int currentFileId;
	private String currentAuthorOrg;
	private Map<Integer, String> documentDictionary;
	private Map<String, Integer> termDictionary;
	private Map<Integer, List<Integer>> termIndex;
	private Map<String, Integer> categoryDictionary;
	private Map<Integer, List<Integer>> categoryIndex;
	private Map<String, Integer> placeDictionary;
	private Map<Integer, List<Integer>> placeIndex;
	private Map<String, String> authorDictionary;
	private Map<String, List<Integer>> authorIndex;
	//This parameter contains the address of the folder where everything needs to be stored.
	/**
	 * Default constructor
	 * @param indexDir : The root directory to be sued for indexing
	 */
	public IndexWriter(String indexDir) {
		//TODO : YOU MUST IMPLEMENT THIS
		//If there is a parameter passed, assign it to the variable.
		this.indexDir = indexDir;
		File dir = new File(indexDir);
		if(!dir.exists())
			dir.mkdir();
		else{
			String contents[] = dir.list();
			File folder;
			for(String content : contents)
			{
				folder = new File(indexDir + File.separator + content);
				if(folder.isDirectory())
				{
					String files[] = folder.list();
					for(String file : files)
					{
						new File(folder.getAbsolutePath() + File.separator + file).delete();
					}
				}
				folder.delete();
			}
		}
		this.documentDictionary = new TreeMap<Integer, String>();
		this.termDictionary = new TreeMap<String, Integer>();
		this.termIndex = new TreeMap<Integer, List<Integer>>();
		this.categoryDictionary = new TreeMap<String, Integer>();
		this.categoryIndex = new TreeMap<Integer, List<Integer>>();
		this.placeDictionary = new TreeMap<String, Integer>();
		this.placeIndex = new TreeMap<Integer, List<Integer>>();
		this.authorDictionary = new TreeMap<String, String>();
		this.authorIndex = new TreeMap<String, List<Integer>>();
		this.count = 1;
	}
	
	public String getIndexDir() {
		return indexDir;
	}

	public void setIndexDir(String indexDir) {
		this.indexDir = indexDir;
	}
	
	/**
	 * Method to add the given Document to the index
	 * This method should take care of reading the filed values, passing
	 * them through corresponding analyzers and then indexing the results
	 * for each indexable field within the document. 
	 * @param d : The Document to be added
	 * @throws IndexerException : In case any error occurs
	 * @throws TokenizerException 
	 */
	public void addDocument(Document d) throws IndexerException {
		//TODO : YOU MUST IMPLEMENT THIS
		//Here we add a document for indexing so before we add it we need to tokenize it and filter it and that filtered one will be added.
		// Hence we call Tokenizer where the delim is set to default or to the specified one
		Tokenizer tokenizer = new Tokenizer();
		TokenStream fileIdStream = new TokenStream();
		TokenStream categoryStream = new TokenStream();
		TokenStream contentStream = new TokenStream();
		TokenStream authorStream = new TokenStream();
		//TokenStream authorOrgStream = new TokenStream();
		TokenStream placeStream = new TokenStream();
		TokenStream dateStream = new TokenStream();
		TokenStream titleStream = new TokenStream();
		TokenStream termStream = new TokenStream();
				
		try{
			String fileids[] = d.getField(FieldNames.FILEID);
			
			if(fileids != null){
				for (String fileid : fileids) {
					fileIdStream.append(tokenizer.consume(fileid));
				}
			}
			
			String categories[] = d.getField(FieldNames.CATEGORY);
			if(categories != null){
				for (String category : categories) {
					categoryStream.append(tokenizer.consume(category));
				}
			}
			
			String titles[] = d.getField(FieldNames.TITLE);
			if(titles != null){
				for (String title : titles) {
					titleStream.append(tokenizer.consume(title));
				}
			}
			
			String authors[] = d.getField(FieldNames.AUTHOR);
			if(authors != null){
				for (String author : authors) {
					authorStream.append(tokenizer.consume(author));
				}
			}
			
			String authorOrgs[] = d.getField(FieldNames.AUTHORORG);
			if(authorOrgs != null){
				for (String authorOrg : authorOrgs) {
					this.currentAuthorOrg = authorOrg;
				}
			}
			
			String places[] = d.getField(FieldNames.PLACE);
			if(places != null){
				for (String place : places) {
					placeStream.append(tokenizer.consume(place));
				}
			}
			
			String newsDate[] = d.getField(FieldNames.NEWSDATE);
			if(newsDate != null){
				for (String newsdate : newsDate) {
					dateStream.append(tokenizer.consume(newsdate));
				}
			}
			
			String content[] = d.getField(FieldNames.CONTENT);
			if(content != null){
				for (String con : content) {
					contentStream.append(tokenizer.consume(con));
				}
			}
			
			AnalyzerFactory analyzerFactory = AnalyzerFactory.getInstance();
			
			CategoryAnalyzer categoryAnalyzer = (CategoryAnalyzer) analyzerFactory.getAnalyzerForField(FieldNames.CATEGORY, categoryStream);
			categoryAnalyzer.increment();
			categoryStream = categoryAnalyzer.getStream();
			
			TitleAnalyzer titleAnalyzer = (TitleAnalyzer) analyzerFactory.getAnalyzerForField(FieldNames.TITLE, titleStream);
			titleAnalyzer.increment();			
			titleStream = titleAnalyzer.getStream();
			
			AuthorAnalyzer authorAnalyzer = (AuthorAnalyzer) analyzerFactory.getAnalyzerForField(FieldNames.AUTHOR, authorStream);
			authorAnalyzer.increment();
			authorStream = authorAnalyzer.getStream();
			
			/*AuthorOrgAnalyzer authorOrgAnalyzer = (AuthorOrgAnalyzer) analyzerFactory.getAnalyzerForField(FieldNames.AUTHORORG, authorOrgStream);
			authorOrgAnalyzer.setTokenFilterFactory(tokenFactory);
			authorOrgAnalyzer.increment();
			authorOrgStream = authorOrgAnalyzer.getStream();*/
						
			PlaceAnalyzer placeAnalyzer = (PlaceAnalyzer) analyzerFactory.getAnalyzerForField(FieldNames.PLACE, placeStream);
			placeAnalyzer.increment();
			placeStream = placeAnalyzer.getStream();
			
			NewsDateAnalyzer newsDateAnalyzer = (NewsDateAnalyzer) analyzerFactory.getAnalyzerForField(FieldNames.NEWSDATE, dateStream);
			newsDateAnalyzer.increment();
			dateStream = newsDateAnalyzer.getStream();
			
			ContentAnalyzer contentAnalyzer = (ContentAnalyzer) analyzerFactory.getAnalyzerForField(FieldNames.CONTENT, contentStream);
			contentAnalyzer.increment();
			contentStream = contentAnalyzer.getStream();
			
			termStream.append(titleStream);
			termStream.append(dateStream);
			termStream.append(contentStream);
			
			addToDocumentDictionary(fileIdStream);
			
			addToCategoryDictionary(categoryStream);
			categoryStream.reset();
			addToCategoryIndex(categoryStream);
			
			addToPlaceDictionary(placeStream);
			placeStream.reset();
			addToPlaceIndex(placeStream);
			
			addToAuthorDictionary(authorStream);
			authorStream.reset();
			addToAuthorIndex(authorStream);
			
			addToTermDictionary(termStream);
			termStream.reset();
			addToTermIndex(termStream);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		

	}
	
	private void addToDocumentDictionary(TokenStream fileIdStream)
	{
		try{			
			while(fileIdStream.hasNext())
			{
				this.currentFileId = this.documentDictionary.size() + 1;
				this.documentDictionary.put(this.currentFileId, fileIdStream.next().toString());
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void writeToDocumentDictionary()
	{
		try{
			File dir = new File(this.indexDir);
			if(!dir.exists())
				dir.mkdir();
			
			ObjectOutputStream oos = new ObjectOutputStream(
			        new FileOutputStream(dir.getAbsolutePath() + File.separator +"Document Dictionary.ser")
			);
			oos.writeObject(this.documentDictionary);
			oos.flush();
			oos.close();
			//System.out.println(this.documentDictionary);
			this.documentDictionary.clear();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void addToCategoryDictionary(TokenStream categoryStream)
	{
		try{			
			while(categoryStream.hasNext())
			{
				String text = categoryStream.next().toString();
				if(!this.categoryDictionary.containsKey(text))
					this.categoryDictionary.put(text, this.categoryDictionary.size()+1);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void writeToCategoryDictionary()
	{
		try{
			File dir = new File(this.indexDir);
			if(!dir.exists())
				dir.mkdir();
			
			ObjectOutputStream oos = new ObjectOutputStream(
			        new FileOutputStream(dir.getAbsolutePath() + File.separator +"Category Dictionary.ser")
			);
			oos.writeObject(this.categoryDictionary);
			oos.flush();
			oos.close();
			//System.out.println(this.categoryDictionary);
			this.categoryDictionary.clear();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void addToCategoryIndex(TokenStream categoryStream)
	{
		try{
			while(categoryStream.hasNext())
			{
				int categoryId = this.categoryDictionary.get(categoryStream.next().toString());
				if(this.categoryIndex.containsKey(categoryId)){
					List<Integer> tempList = this.categoryIndex.get(categoryId);
					tempList.add(this.currentFileId);
					this.categoryIndex.put(categoryId, tempList);
				}
				else{
					List<Integer> tempList = new ArrayList<Integer>();
					tempList.add(this.currentFileId);
					this.categoryIndex.put(categoryId, tempList);
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void writeToCategoryIndex()
	{
		try{
			File dir = new File(this.indexDir+ File.separator+ "category");
			if(!dir.exists())
				dir.mkdir();
				
			ObjectOutputStream oos = new ObjectOutputStream(
			        new FileOutputStream(dir.getAbsolutePath() + File.separator +"Category Index.ser")
			);
			oos.writeObject(this.categoryIndex);
			oos.flush();
			oos.close();
			//System.out.println(this.categoryIndex);
			this.categoryIndex.clear();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void addToPlaceDictionary(TokenStream placeStream)
	{
		try{			
			while(placeStream.hasNext())
			{
				String text = placeStream.next().toString();
				if(!this.placeDictionary.containsKey(text))
					this.placeDictionary.put(text, this.placeDictionary.size()+1);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void writeToPlaceDictionary()
	{
		try{
			File dir = new File(this.indexDir);
			if(!dir.exists())
				dir.mkdir();
			
			ObjectOutputStream oos = new ObjectOutputStream(
			        new FileOutputStream(dir.getAbsolutePath() + File.separator +"Place Dictionary.ser")
			);
			oos.writeObject(this.placeDictionary);
			oos.flush();
			oos.close();
			//System.out.println(this.placeDictionary);
			this.placeDictionary.clear();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void addToPlaceIndex(TokenStream placeStream)
	{
		try{
			while(placeStream.hasNext())
			{
				int placeId = this.placeDictionary.get(placeStream.next().toString());
				if(this.placeIndex.containsKey(placeId)){
					List<Integer> tempList = this.placeIndex.get(placeId);
					tempList.add(this.currentFileId);
					this.placeIndex.put(placeId, tempList);
				}
				else{
					List<Integer> tempList = new ArrayList<Integer>();
					tempList.add(this.currentFileId);
					this.placeIndex.put(placeId, tempList);
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void writeToPlaceIndex()
	{
		try{
			File dir = new File(this.indexDir+ File.separator+ "place");
			if(!dir.exists())
				dir.mkdir();
				
			ObjectOutputStream oos = new ObjectOutputStream(
			        new FileOutputStream(dir.getAbsolutePath() + File.separator +"Place Index.ser")
			);
			oos.writeObject(this.placeIndex);
			oos.flush();
			oos.close();
			//System.out.println(this.placeIndex);
			this.placeIndex.clear();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void addToAuthorDictionary(TokenStream authorStream)
	{
		try{		
			while(authorStream.hasNext())
			{
				Token token = authorStream.next();
				String text = token.toString();
				if(!this.authorDictionary.containsValue(text))
				{
					this.authorDictionary.put(text, this.currentAuthorOrg);
				}
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void writeToAuthorDictionary()
	{
		try{
			File dir = new File(this.indexDir);
			if(!dir.exists())
				dir.mkdir();
			
			ObjectOutputStream oos = new ObjectOutputStream(
			        new FileOutputStream(dir.getAbsolutePath() + File.separator +"Author Dictionary.ser")
			);
			oos.writeObject(this.authorDictionary);
			oos.flush();
			oos.close();
			//System.out.println(this.authorDictionary);
			this.authorDictionary.clear();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void addToAuthorIndex(TokenStream authorStream)
	{
		try{
			while(authorStream.hasNext())
			{
				Token token = authorStream.next();
				String authorName = token.toString();
				if(this.authorIndex.containsKey(authorName)){
					List<Integer> tempList = this.authorIndex.get(authorName);
					tempList.add(this.currentFileId);
					this.authorIndex.put(authorName, tempList);
				}
				else{
					List<Integer> tempList = new ArrayList<Integer>();
					tempList.add(this.currentFileId);
					this.authorIndex.put(authorName, tempList);
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void writeToAuthorIndex()
	{
		try{
			File dir = new File(this.indexDir+ File.separator+ "author");
			if(!dir.exists())
				dir.mkdir();
				
			ObjectOutputStream oos = new ObjectOutputStream(
			        new FileOutputStream(dir.getAbsolutePath() + File.separator +"Author Index.ser")
			);
			oos.writeObject(this.authorIndex);
			oos.flush();
			oos.close();
			//System.out.println(this.authorIndex);
			this.authorIndex.clear();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void addToTermDictionary(TokenStream termStream)
	{
		try{			
			while(termStream.hasNext())
			{
				String text = termStream.next().toString();
				if(!this.termDictionary.containsKey(text))
					this.termDictionary.put(text, this.termDictionary.size()+1);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void writeToTermDictionary()
	{
		try{
			File dir = new File(this.indexDir);
			if(!dir.exists())
				dir.mkdir();
			
			ObjectOutputStream oos = new ObjectOutputStream(
			        new FileOutputStream(dir.getAbsolutePath() + File.separator +"Term Dictionary.ser")
			);
			oos.writeObject(this.termDictionary);
			oos.flush();
			oos.close();
			System.out.println(this.termDictionary);
			this.termDictionary.clear();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void addToTermIndex(TokenStream termStream)
	{
		try{
			while(termStream.hasNext())
			{
				int termId = this.termDictionary.get(termStream.next().toString());
				if(this.termIndex.containsKey(termId)){
					List<Integer> tempList = this.termIndex.get(termId);
					tempList.add(this.currentFileId);
					this.termIndex.put(termId, tempList);
				}
				else{
					List<Integer> tempList = new ArrayList<Integer>();
					tempList.add(this.currentFileId);
					this.termIndex.put(termId, tempList);
				}
			}
			
			if(this.currentFileId%100 == 0)
				writeToTermIndex();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void writeToTermIndex()
	{
		try{
			File dir = new File(this.indexDir+ File.separator+ "term");
			if(!dir.exists())
				dir.mkdir();
				
			ObjectOutputStream oos = new ObjectOutputStream(
			        new FileOutputStream(dir.getAbsolutePath() + File.separator + "Term Index" + (this.count++) + ".ser")
			);
			oos.writeObject(this.termIndex);
			oos.flush();
			oos.close();
			System.out.println("Term index"+count+": "+this.termIndex);
			this.termIndex.clear();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Method that indicates that all open resources must be closed
	 * and cleaned and that the entire indexing operation has been completed.
	 * @throws IndexerException : In case any error occurs
	 */
	public void close() throws IndexerException {
		//TODO
		try{
			writeToCategoryIndex();
			writeToPlaceIndex();
			writeToAuthorIndex();
			writeToTermIndex();
			writeToDocumentDictionary();
			writeToCategoryDictionary();
			writeToPlaceDictionary();
			writeToAuthorDictionary();
			writeToTermDictionary();
			merge();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void merge()
	{
		//recursively merge
		try{
			File ipDirectory = new File(this.indexDir + File.separator + "term");
			String[] files = ipDirectory.list();
			Map<Integer, List<Integer>> mergedMap = new TreeMap<Integer, List<Integer>>();
			ObjectInputStream ois;
			
			for(String file : files)
			{
				if(file.equals("Term Index.ser"))
					continue;
				else{
					ois = new ObjectInputStream(new FileInputStream(ipDirectory.getAbsolutePath() + File.separator + file));				
					mergedMap = mergeFiles(mergedMap, (TreeMap<Integer, List<Integer>>) ois.readObject());
					ois.close();
					//System.out.println(mergedMap.size());
				}
			}
			
			File dir = new File(this.indexDir+ File.separator+ "term");
			if(!dir.exists())
				dir.mkdir();
				
			ObjectOutputStream oos = new ObjectOutputStream(
			        new FileOutputStream(dir.getAbsolutePath() + File.separator + "Term Index.ser")
			);
			oos.writeObject(mergedMap);
			oos.flush();
			oos.close();
			
			//System.out.println("Merge:"+mergedMap);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Map<Integer, List<Integer>> mergeFiles(Map<Integer, List<Integer>> m1, Map<Integer, List<Integer>> m2)
	{		
		Map<Integer, List<Integer>> mergedMap = new TreeMap<Integer, List<Integer>>();
		Iterator<Entry<Integer, List<Integer>>> i1 = m1.entrySet().iterator();
		while(i1.hasNext()){
			Entry<Integer, List<Integer>> entry = i1.next();
			mergedMap.put(entry.getKey(), entry.getValue());
		}
		
		Iterator<Entry<Integer, List<Integer>>> i2 = m2.entrySet().iterator();
		while(i2.hasNext()){
			Entry<Integer, List<Integer>> entry = i2.next();
			mergedMap.put(entry.getKey(), entry.getValue());
		}
		
		return mergedMap;
	}
	
}
