/**
 * 
 */
package edu.buffalo.cse.irf14.index;

import edu.buffalo.cse.irf14.analysis.Tokenizer;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.FieldNames;

/**
 * @author nikhillo
 * Class responsible for writing indexes to disk
 */
public class IndexWriter {
	/**
	 * Default constructor
	 * @param indexDir : The root directory to be sued for indexing
	 */
	public IndexWriter(String indexDir) {
		//TODO : YOU MUST IMPLEMENT THIS
	}
	
	/**
	 * Method to add the given Document to the index
	 * This method should take care of reading the filed values, passing
	 * them through corresponding analyzers and then indexing the results
	 * for each indexable field within the document. 
	 * @param d : The Document to be added
	 * @throws IndexerException : In case any error occurs
	 */
	public void addDocument(Document d) throws IndexerException {
		//TODO : YOU MUST IMPLEMENT THIS
		Tokenizer tokenizer = new Tokenizer();
				
		try{
			/*String fileids[] = d.getField(FieldNames.FILEID);
			for (String fileid : fileids) {
				
			}
			
			String categories[] = d.getField(FieldNames.CATEGORY);
			for (String category : categories) {
				
			}*/
			
			String titles[] = d.getField(FieldNames.TITLE);
			for (String title : titles) {
				TokenStream titleTS = tokenizer.consume(title);
			}
			
			/*String authors[] = d.getField(FieldNames.AUTHOR);
			for (String author : authors) {
				
			}
			
			String authorOrgs[] = d.getField(FieldNames.AUTHORORG);
			for (String authorOrg : authorOrgs) {
				
			}
			
			String places[] = d.getField(FieldNames.PLACE);
			for (String place : places) {
				
			}
			
			String newsDate[] = d.getField(FieldNames.NEWSDATE);
			for (String newsdate : newsDate) {
				
			}
			
			String content[] = d.getField(FieldNames.CONTENT);
			for (String con : content) {
				
			}*/
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
	}
}
