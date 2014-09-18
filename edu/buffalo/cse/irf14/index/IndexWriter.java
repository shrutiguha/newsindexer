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
		TokenStream stream = new TokenStream();
				
		try{
			String fileids[] = d.getField(FieldNames.FILEID);
			tokenizer.setFileId(fileids[0]);
			for (String fileid : fileids) {
				stream.append(tokenizer.consume(fileid));
			}
			
			String categories[] = d.getField(FieldNames.CATEGORY);
			for (String category : categories) {
				stream.append(tokenizer.consume(category));
			}
			
			String titles[] = d.getField(FieldNames.TITLE);
			for (String title : titles) {
				stream.append(tokenizer.consume(title));
			}
			
			String authors[] = d.getField(FieldNames.AUTHOR);
			for (String author : authors) {
				stream.append(tokenizer.consume(author));
			}
			
			String authorOrgs[] = d.getField(FieldNames.AUTHORORG);
			for (String authorOrg : authorOrgs) {
				stream.append(tokenizer.consume(authorOrg));
			}
			
			String places[] = d.getField(FieldNames.PLACE);
			for (String place : places) {
				stream.append(tokenizer.consume(place));
			}
			
			String newsDate[] = d.getField(FieldNames.NEWSDATE);
			for (String newsdate : newsDate) {
				stream.append(tokenizer.consume(newsdate));
			}
			
			String content[] = d.getField(FieldNames.CONTENT);
			for (String con : content) {
				stream.append(tokenizer.consume(con));
			}
			
		}
		catch(Exception e)
		{
			
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
