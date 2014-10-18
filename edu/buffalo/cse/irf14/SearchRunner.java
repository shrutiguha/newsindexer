package edu.buffalo.cse.irf14;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import edu.buffalo.cse.irf14.analysis.AnalyzerFactory;
import edu.buffalo.cse.irf14.analysis.AuthorAnalyzer;
import edu.buffalo.cse.irf14.analysis.CategoryAnalyzer;
import edu.buffalo.cse.irf14.analysis.ContentAnalyzer;
import edu.buffalo.cse.irf14.analysis.PlaceAnalyzer;
import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.Tokenizer;
import edu.buffalo.cse.irf14.document.FieldNames;
import edu.buffalo.cse.irf14.index.TermData;
import edu.buffalo.cse.irf14.query.BinaryTree;
import edu.buffalo.cse.irf14.query.Node;
import edu.buffalo.cse.irf14.query.Query;
import edu.buffalo.cse.irf14.query.QueryParser;

/**
 * Main class to run the searcher.
 * As before implement all TODO methods unless marked for bonus
 * @author nikhillo
 *
 */
public class SearchRunner {
	public enum ScoringModel {TFIDF, OKAPI};
	private String indexDir;
	private String corpusDir;
	private PrintStream stream;
	private int N;
	private double avgDocLen;
	private Map<Integer, String> documentDictionary;
	private Map<String, Integer> termDictionary;
	private Map<Integer, TermData> termIndex;
	
	/**
	 * Default (and only public) constuctor
	 * @param indexDir : The directory where the index resides
	 * @param corpusDir : Directory where the (flattened) corpus resides
	 * @param mode : Mode, one of Q or E
	 * @param stream: Stream to write output to
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	public SearchRunner(String indexDir, String corpusDir, 
			char mode, PrintStream stream) {
		//TODO: IMPLEMENT THIS METHOD
		this.indexDir = indexDir;
		this.corpusDir = corpusDir;
		this.stream = stream;
		
		try{
			File dir = new File(this.indexDir);
			if(dir.exists())
			{
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dir.getAbsolutePath() 
						+ File.separator +"Info.ser"));
				String info[] = ((String) ois.readObject()).split(" ");
				this.N = Integer.parseInt(info[0]);
				this.avgDocLen = Double.parseDouble(info[1]);
				ois.close();
			}
			
			if(this.documentDictionary == null && this.termDictionary == null && this.termIndex == null){
				dir = new File(this.indexDir);
				if(dir.exists()){
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dir.getAbsolutePath() + File.separator +"Term Dictionary.ser"));
					this.termDictionary = (TreeMap<String, Integer>) ois.readObject();
					ois = new ObjectInputStream(new FileInputStream(dir.getAbsolutePath() + File.separator +"Document Dictionary.ser"));
					this.documentDictionary = (TreeMap<Integer, String>) ois.readObject();
					dir = new File(this.indexDir + File.separator + "term");
					ois = new ObjectInputStream(new FileInputStream(dir.getAbsolutePath() + File.separator +"Term Index.ser"));
					this.termIndex = (TreeMap<Integer, TermData>) ois.readObject();
					ois.close();
				}
			}
			
			while(true){
				switch(mode)
				{
					case 'E': query(new File(corpusDir + File.separator + "query.txt"));
							  break;
					case 'Q': this.stream.print("Enter query: ");
							  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
							  String query = br.readLine();
							  query(query, ScoringModel.TFIDF);
							  break;
					default: System.out.println("Enter a valid option");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Method to execute given query in the Q mode
	 * @param userQuery : Query to be parsed and executed
	 * @param model : Scoring Model to use for ranking results
	 */
	public void query(String userQuery, ScoringModel model) {
		//TODO: IMPLEMENT THIS METHOD
		try{
			Query q = QueryParser.parse(userQuery, "AND");
			List<Integer> resultList = new ArrayList<Integer>();
			if(q != null){
				String tokenizedQuery = tokenize(q);
				//System.out.println("Query:"+tokenizedQuery);
				if(tokenizedQuery != null){
					resultList = traverse(tokenizedQuery);
				}
				
				if(resultList.size() == 0)
					this.stream.println("No matches found");
				else{
					Map<String, Integer> uniqueResultList = new HashMap<String, Integer>();
					for(Integer docId : resultList){
						uniqueResultList.put(this.documentDictionary.get(docId), docId);
					}			
					this.stream.println(uniqueResultList);
				}
			}
			switch(model){
				case TFIDF: break;
				case OKAPI: break;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to execute queries in E mode
	 * @param queryFile : The file from which queries are to be read and executed
	 */
	public void query(File queryFile) {
		//TODO: IMPLEMENT THIS METHOD
	}
	
	/**
	 * General cleanup method
	 */
	public void close() {
		//TODO : IMPLEMENT THIS METHOD
	}
	
	/**
	 * Method to indicate if wildcard queries are supported
	 * @return true if supported, false otherwise
	 */
	public static boolean wildcardSupported() {
		//TODO: CHANGE THIS TO TRUE ONLY IF WILDCARD BONUS ATTEMPTED
		return false;
	}
	
	/**
	 * Method to get substituted query terms for a given term with wildcards
	 * @return A Map containing the original query term as key and list of
	 * possible expansions as values if exist, null otherwise
	 */
	public Map<String, List<String>> getQueryTerms() {
		//TODO:IMPLEMENT THIS METHOD IFF WILDCARD BONUS ATTEMPTED
		return null;
		
	}
	
	/**
	 * Method to indicate if speel correct queries are supported
	 * @return true if supported, false otherwise
	 */
	public static boolean spellCorrectSupported() {
		//TODO: CHANGE THIS TO TRUE ONLY IF SPELLCHECK BONUS ATTEMPTED
		return false;
	}
	
	/**
	 * Method to get ordered "full query" substitutions for a given misspelt query
	 * @return : Ordered list of full corrections (null if none present) for the given query
	 */
	public List<String> getCorrections() {
		//TODO: IMPLEMENT THIS METHOD IFF SPELLCHECK EXECUTED
		return null;
	}
	
	public String tokenize(Query q){
		try{
			String query = q.toString();
			Tokenizer tokenizer = new Tokenizer();
			AnalyzerFactory analyzerFactory = AnalyzerFactory.getInstance();

			
			String tempFilter[] = query.split(" ");
			int l=tempFilter.length;
			
			for(int i=0;i<tempFilter.length;i++)
			{ 
				int j=0;
				if(tempFilter[i].matches(".*[\"].*"))
				{
					if(tempFilter[i].matches(".*[\"].*[\"]"))
						break;
					for(j=i+1;j<l;j++)
					{
						if(!tempFilter[j].matches(".*[\"].*"))
							tempFilter[i]=tempFilter[i]+" "+tempFilter[j];
						else
							break;
						
					}
					tempFilter[i]=tempFilter[i]+" "+tempFilter[j];
				
					l=i+1;
					//System.out.println(tempFilter[i]+"len:"+l);
					if(l+1<tempFilter.length)
					{
						for(int c=j+1;c<tempFilter.length;c++)
						{
							tempFilter[l++]=tempFilter[c];
						}
					}
					else
						break;
				}
			}
			   
			for(int i=0;i<l;i++)
			{
				if(tempFilter[i].contains("Term:"))
				{
					TokenStream contentStream = new TokenStream();
					int flag=0;
					if(tempFilter[i].matches("[<]Term:.*[>]"))
					{
						flag=1;
						String r=tempFilter[i];
						r=r.replaceAll("[<]Term:", "");

						r=r.replaceAll("[>]", "");
						contentStream.append(tokenizer.consume(r));
	                }
					else
					{
						String r=tempFilter[i];
						r=r.replaceAll("Term:", "");
						contentStream.append(tokenizer.consume(r));
					}
					
					ContentAnalyzer contentAnalyzer = (ContentAnalyzer) analyzerFactory.getAnalyzerForField(FieldNames.CONTENT, contentStream);
					contentAnalyzer.increment();
					contentStream = contentAnalyzer.getStream();
					if(contentStream.hasNext())
					{
						while(contentStream.hasNext())
						{
							Token t=contentStream.next();
							
							String ts=t.toString();
							//System.out.println("Stream: "+ts);
							if(flag==1)
							{
								tempFilter[i]="<Term:"+ts+">";
							}
							else
								tempFilter[i]="Term:"+ts;
						}
					}
					else
						tempFilter[i]=null;				
				}
				else if(tempFilter[i].contains("Category:"))
				{
					TokenStream categoryStream = new TokenStream();
					
					int flag=0;
					if(tempFilter[i].matches("[<]Category:.*[>]"))
					{
						flag=1;
						String r=tempFilter[i];
						r=r.replaceAll("[<]Category:", "");

						r=r.replaceAll("[>]", "");
						categoryStream.append(tokenizer.consume(r));
	                }
					else
					{
						String r=tempFilter[i];
						r=r.replaceAll("Category:", "");
						categoryStream.append(tokenizer.consume(r));
					}
					
					CategoryAnalyzer categoryAnalyzer = (CategoryAnalyzer) analyzerFactory.getAnalyzerForField(FieldNames.CATEGORY, categoryStream);
					categoryAnalyzer.increment();
					if(categoryStream.hasNext())
					{
						while(categoryStream.hasNext())
						{
							Token t=categoryStream.next();
							
							String ts=t.toString();
							//System.out.println("Stream: "+ts);
							if(flag==1)
							{
								tempFilter[i]="<Category:"+ts+">";
							}
							else
								tempFilter[i]="Category:"+ts;
						
						}
					}
					else
						tempFilter[i]=null;
				}
				else if(tempFilter[i].contains("Author:"))
				{
					TokenStream authorStream = new TokenStream();
					int flag=0;
					if(tempFilter[i].matches("[<]Author:.*[>]"))
					{
						flag=1;
						String r=tempFilter[i];
						r=r.replaceAll("[<]Author:", "");

						r=r.replaceAll("[>]", "");
						authorStream.append(tokenizer.consume(r));
	                }
					else
					{
						String r=tempFilter[i];
						r=r.replaceAll("Author:", "");
						authorStream.append(tokenizer.consume(r));
					}
					AuthorAnalyzer authorAnalyzer = (AuthorAnalyzer) analyzerFactory.getAnalyzerForField(FieldNames.AUTHOR, authorStream);
					authorAnalyzer.increment();
					authorStream = authorAnalyzer.getStream();
					
					if(authorStream.hasNext())
					{
						while(authorStream.hasNext())
						{
							Token t=authorStream.next();
							
							String ts=t.toString();
							//System.out.println("Stream: "+ts);
							if(flag==1)
							{
								tempFilter[i]="<Author:"+ts+">";
							}
							else
								tempFilter[i]="Author:"+ts;
						}
					}
					else
						tempFilter[i]=null;
				}
				else if(tempFilter[i].contains("Place:"))
				{
					TokenStream placeStream = new TokenStream();
					int flag=0;
					if(tempFilter[i].matches("[<]Place:.*[>]"))
					{
						flag=1;
						String r=tempFilter[i];
						r=r.replaceAll("[<]Place:", "");

						r=r.replaceAll("[>]", "");
						placeStream.append(tokenizer.consume(r));
	                }
					else
					{
						String r=tempFilter[i];
						r=r.replaceAll("Place:", "");
						placeStream.append(tokenizer.consume(r));
					}
					
					PlaceAnalyzer placeAnalyzer = (PlaceAnalyzer) analyzerFactory.getAnalyzerForField(FieldNames.PLACE, placeStream);
					placeAnalyzer.increment();
					placeStream = placeAnalyzer.getStream();
					if(placeStream.hasNext())
					{
						while(placeStream.hasNext())
						{
							Token t=placeStream.next();
							
							String ts=t.toString();
							//System.out.println("Stream: "+ts);
						
							if(flag==1)
							{
								tempFilter[i]="<Place:"+ts+">";
							}
							else
								tempFilter[i]="Place:"+ts;
						}
					}
					else
						tempFilter[i]=null;
				}
			}

			for(int i=0;i<l;i++)
			{
				if(tempFilter[i]==null)
				{
					if(i-1>=1)
					{
						if(tempFilter[i-1].matches("AND|OR"))
						{
							tempFilter[i]="";
							tempFilter[i-1]="";
						}					
						else
						{
							if(i+1<l)
								if(tempFilter[i+1].matches("AND|OR"))
								{
									tempFilter[i]="";
									tempFilter[i+1]="";
								}
						}
					}
					else
					{
						if(i+1<l)
							if(tempFilter[i+1].matches("AND|OR"))
							{
								tempFilter[i]="";
								tempFilter[i+1]="";
							}
					}
					if(i+1<l)
						if(tempFilter[i+1].contentEquals("]"))
						{
							
						}
				}
			}
			
			String newquery="";
			for(int i=0;i<l;i++)
				newquery=newquery+tempFilter[i]+" ";
			newquery=newquery.replaceAll("\\s+", " ").trim();
			//System.out.println(newquery);
			
			String temp[]=newquery.split(" ");
			
			l=temp.length;
			

			for(int i=0;i<temp.length;i++)
			{ 
				int j=0;
				if(temp[i].matches(".*[\"].*"))
				{
					if(temp[i].matches(".*[\"].*[\"]"))
						break;
					for(j=i+1;j<l;j++)
					{
						if(!temp[j].matches(".*[\"].*"))
							temp[i]=temp[i]+" "+temp[j];
						else
							break;
						
					}
					temp[i]=temp[i]+" "+temp[j];
				
					l=i+1;
					//System.out.println(temp[i]+"len:"+l);
					if(l+1<temp.length)
					{
						for(int c=j+1;c<temp.length;c++)
						{
							temp[l++]=temp[c];
						}
					}
					else
						break;			
				}
				
			}
			
			for(int i=0;i<l;i++)
			{
				if(temp[i].contentEquals("["))
				{
					if(i+1<l)
					if(temp[i+1].matches(".*Term:.*")||temp[i+1].matches(".*Category:.*")||temp[i+1].matches(".*Place:.*")||temp[i+1].matches(".*Author:.*"))
					if(i+2<l)
					if(temp[i+2].contentEquals("]"))
					{
						temp[i]="";
						temp[i+2]="";
						
					}
				}
			}
			
			newquery="";
			for(int i=0;i<l;i++)
				newquery=newquery+temp[i]+" ";
			newquery=newquery.replaceAll("\\s+", " ").trim();
			//System.out.println("FINAL:"+newquery);
			return newquery;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Integer> traverse(String newquery){
		String tempTree[] = newquery.split(" ");
		
		int l=tempTree.length;
		int i = 0;
		for(i=0;i<tempTree.length;i++)
		{ 
			int j=0;
			if(tempTree[i].matches(".*[\"].*"))
			{
				if(tempTree[i].matches(".*[\"].*[\"]"))
					break;
				for(j=i+1;j<l;j++)
				{
					if(!tempTree[j].matches(".*[\"].*"))
						tempTree[i]=tempTree[i]+" "+tempTree[j];
					else
						break;
					
				}
				tempTree[i]=tempTree[i]+" "+tempTree[j];
			
				l=i+1;
				if(l+1<tempTree.length)
				{
					for(int c=j+1;c<tempTree.length;c++)
					{
						tempTree[l++]=tempTree[c];
					}
				}	
				else
					break;
			}			
		}
		
		//System.out.println("--------------------------");
		for( i=0;i<l;i++)
		{
		//System.out.println(tempTree[i]);
		}
		
		BinaryTree t=new BinaryTree();
		t.setIndexDir(this.indexDir);
		t.setTermDictionary(this.termDictionary);
		t.setTermIndex(this.termIndex);

	    Stack operands=new Stack();
	    Stack operators=new Stack();
	    
	    Node root=t.root;
		Node left,r,right;
		//System.out.println(l);
		if(l>=5)
		{
		for(i=0;i<l;i++)
		{
			
			if(tempTree[i].contentEquals("]")||tempTree[i].contentEquals("}"))
			{
				if(tempTree[i].contentEquals("}"))
				{
					Node tempnode=(Node) operands.pop();
					Node braces=(Node) operands.pop();
					String check=(String)braces.name;
					if(check.contentEquals("{"))
					{
						operands.push(braces);
						operands.push(tempnode);
						break;
					}
					else
					{
						operands.push(braces);
						operands.push(tempnode);
					}
				}
				String tempstring=null;
				Node tempnode;
				while(!operands.isEmpty()&&!operators.isEmpty())
				{
				left=(Node) operands.pop();
				right=(Node) operands.pop();
				r=(Node) operators.pop();
				root=t.addNode(left, r, right);
				if(!operands.isEmpty())
				{
				 tempnode=(Node)operands.pop();
				 tempstring=tempnode.name;
				 if(tempstring.contentEquals("[")||tempstring.contentEquals("{"))
						break;
				 else
					 operands.push(tempnode);
				}
				
				
				operands.push(root);
				}
				operands.push(root);

			}
			else if(!tempTree[i].contentEquals("AND")&&!tempTree[i].contentEquals("OR"))
			{	
				Node z=new Node(tempTree[i]);
				operands.push(z);
				//System.out.println("operands"+i+":"+z.name);
			}
			
			else
			{
				operators.push(new Node(tempTree[i]));
				//System.out.println("operators"+i+":"+tempTree[i]);

			}
			
		}
		t.root=(Node) operands.pop();
		}
		else
		{
			if(l==3)
			{
				if(tempTree[0].contentEquals("{")&&tempTree[2].contentEquals("}"))
					t.root=new Node(tempTree[1]);
			}
			else{
				t.root=null;
				System.out.println(l);
			}
		}
	    
	    
		//System.out.println("INORDER");
        
		t.inOrderTraverseTree(t.root);
        //this.stream.println("Postings list: "+t.root.postingsList);
        return t.root.postingsList;
	}
}
