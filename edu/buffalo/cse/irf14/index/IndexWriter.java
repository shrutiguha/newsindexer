/**
 * 
 */
package edu.buffalo.cse.irf14.index;

import edu.buffalo.cse.irf14.analysis.TokenFilter;
import edu.buffalo.cse.irf14.analysis.TokenFilterFactory;
import edu.buffalo.cse.irf14.analysis.TokenFilterType;
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
	//This parameter contains the address of the folder where everything needs to be stored.
	/**
	 * Default constructor
	 * @param indexDir : The root directory to be sued for indexing
	 */
	public IndexWriter(String indexDir) {
		//TODO : YOU MUST IMPLEMENT THIS
		//If there is a parameter passed, assign it to the variable.
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
		TokenStream stream = new TokenStream();
		// An array list is created here.
				
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
			
			TokenFilterFactory factory = TokenFilterFactory.getInstance();
			TokenFilter filter = factory.getFilterByType(TokenFilterType.DATE, stream);
			
			System.out.println("s");

			System.out.println(stream);
			if(filter != null)
			{
				while (filter.increment()) {
					
				}
				stream = filter.getStream();
				System.out.println(stream);
			}
			
			stream.reset();
			
			
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
