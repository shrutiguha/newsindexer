/**
 * 
 */
package edu.buffalo.cse.irf14.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author nikhillo
 * Class that emulates reading data back from a written index
 */
public class IndexReader {
	
	private String indexDir;
	private IndexType type;
	/**
	 * Default constructor
	 * @param indexDir : The root directory from which the index is to be read.
	 * This will be exactly the same directory as passed on IndexWriter. In case 
	 * you make subdirectories etc., you will have to handle it accordingly.
	 * @param type The {@link IndexType} to read from
	 */
	public IndexReader(String indexDir, IndexType type) {
		//TODO
		this.indexDir = indexDir;
		this.type = type;
	}
	
	/**
	 * Get total number of terms from the "key" dictionary associated with this 
	 * index. A postings list is always created against the "key" dictionary
	 * @return The total number of terms
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	public int getTotalKeyTerms() {
		//TODO : YOU MUST IMPLEMENT THIS
		File dir = new File(this.indexDir);
		Map<Integer, String> dictionary;
		ObjectInputStream ois;
		try{
			if(dir.exists())
			{
				switch(this.type)
				{
					case TERM: 
						ois = new ObjectInputStream(new FileInputStream(dir.getAbsolutePath() + File.separator +"Term Dictionary.ser"));
						dictionary = (TreeMap<Integer, String>) ois.readObject();
						ois.close();
						return dictionary.size();
					case AUTHOR: 
						ois = new ObjectInputStream(new FileInputStream(dir.getAbsolutePath() + File.separator +"Author Dictionary.ser"));
						dictionary = (TreeMap<Integer, String>) ois.readObject();
						ois.close();
						return dictionary.size();
					case CATEGORY: 
						ois = new ObjectInputStream(new FileInputStream(dir.getAbsolutePath() + File.separator +"Category Dictionary.ser"));
						dictionary = (TreeMap<Integer, String>) ois.readObject();
						ois.close();
						return dictionary.size();
					case PLACE: 
						ois = new ObjectInputStream(new FileInputStream(dir.getAbsolutePath() + File.separator +"Place Dictionary.ser"));
						dictionary = (TreeMap<Integer, String>) ois.readObject();
						ois.close();
						return dictionary.size();
					default: return -1;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return -1;
	}
	
	/**
	 * Get total number of terms from the "value" dictionary associated with this 
	 * index. A postings list is always created with the "value" dictionary
	 * @return The total number of terms
	 */
	@SuppressWarnings("unchecked")
	public int getTotalValueTerms() {
		//TODO: YOU MUST IMPLEMENT THIS
		try{
			File dir = new File(this.indexDir);
			Map<Integer, String> dictionary;
			if(dir.exists())
			{
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dir.getAbsolutePath() + File.separator +"Document Dictionary.ser"));
				dictionary = (TreeMap<Integer, String>) ois.readObject();
				ois.close();
				return dictionary.size();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return -1;
	}
	
	/**
	 * Method to get the postings for a given term. You can assume that
	 * the raw string that is used to query would be passed through the same
	 * Analyzer as the original field would have been.
	 * @param term : The "analyzed" term to get postings for
	 * @return A Map containing the corresponding fileid as the key and the 
	 * number of occurrences as values if the given term was found, null otherwise.
	 */
	public Map<String, Integer> getPostings(String term) {
		//TODO:YOU MUST IMPLEMENT THIS
		return null;
	}
	
	/**
	 * Method to get the top k terms from the index in terms of the total number
	 * of occurrences.
	 * @param k : The number of terms to fetch
	 * @return : An ordered list of results. Must be <=k for valid k values
	 * null for invalid k values
	 */
	@SuppressWarnings({ "resource", "unchecked" })
	public List<String> getTopK(int k) {
		//TODO YOU MUST IMPLEMENT THIS
		return null;
	}
	
	/**
	 * Method to implement a simple boolean AND query on the given index
	 * @param terms The ordered set of terms to AND, similar to getPostings()
	 * the terms would be passed through the necessary Analyzer.
	 * @return A Map (if all terms are found) containing FileId as the key 
	 * and number of occurrences as the value, the number of occurrences 
	 * would be the sum of occurrences for each participating term. return null
	 * if the given term list returns no results
	 * BONUS ONLY
	 */
	public Map<String, Integer> query(String...terms) {
		//TODO : BONUS ONLY
		return null;
	}
}
