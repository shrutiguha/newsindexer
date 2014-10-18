/**
 * 
 */
package edu.buffalo.cse.irf14.query;

import edu.buffalo.cse.irf14.document.ParserException;

/**
 * @author nikhillo
 * Static parser that converts raw text to Query objects
 */
public class QueryParser {
	/**
	 * MEthod to parse the given user query into a Query object
	 * @param userQuery : The query to parse
	 * @param defaultOperator : The default operator to use, one amongst (AND|OR)
	 * @return Query object if successfully parsed, null otherwise
	 */
	public static Query parse(String userQuery, String defaultOperator) throws ParserException {
		//TODO: YOU MUST IMPLEMENT THIS METHOD
		Query q=new Query();
		String query="";
		
		if(userQuery == null || userQuery.equals(""))
			throw new ParserException();
		
		int count=0;
		String b=null;
		String tempArray[] = userQuery.split(" ");
		int len=tempArray.length;
		
		if(userQuery == null || userQuery.equals(""))
			throw new ParserException();
		
		
		//HANDLING PHRASES
		
		for(int i=0;i<tempArray.length;i++)
		{ 
			int j=0;
			if(tempArray[i].matches(".*[\"].*"))
			{
				if(tempArray[i].matches(".*[\"].*[\"]"))
					break;
				for(j=i+1;j<len;j++)
				{
					if(!tempArray[j].matches(".*[\"].*"))
						tempArray[i]=tempArray[i]+" "+tempArray[j];
					else
						break;
					
				}
				tempArray[i]=tempArray[i]+" "+tempArray[j];
				
				len=i+1;
				//System.out.println(tempArray[i]+"len:"+len);
				if(len+1<tempArray.length)
				{
					for(int c=j+1;c<tempArray.length;c++)
					{
						tempArray[len++]=tempArray[c];
					}
				}
				else
					break;

			}
			
		}
		
		//RESOLVE BRACKETS
		for(int i=0;i<len;i++)
		{
			
		if(tempArray[i].matches(".*Category:[(].*"))
			{
				tempArray[i]=tempArray[i].replaceAll("Category:[(]", "(Category:");
								
				int j=i+1;
				if(j<len)
				{
				while(!tempArray[j].matches(".*[)]"))
				{
					if(!tempArray[j].matches("AND|NOT|OR"))
						tempArray[j]="Category:"+tempArray[j];
					j++;
				}
				tempArray[j]="Category:"+tempArray[j];
				}
			}
			else if(tempArray[i].matches(".*Place:[(].*"))
			{
				tempArray[i]=tempArray[i].replaceAll("Place:[(]", "(Place:");
				
				int j=i+1;
				while(!tempArray[j].matches(".*[)]"))
				{
					if(!tempArray[j].matches("AND|NOT|OR"))
						tempArray[j]="Place:"+tempArray[j];
					j++;
				}
				tempArray[j]="Place:"+tempArray[j];
			}
			else if(tempArray[i].matches(".*Author:[(].*"))
			{
				tempArray[i]=tempArray[i].replaceAll("Author:[(]", "(Author:");
				
				int j=i+1;
				while(!tempArray[j].matches(".*[)]"))
				{
					if(!tempArray[j].matches("AND|NOT|OR"))
						tempArray[j]="Author:"+tempArray[j];
					j++;
				}
				tempArray[j]="Author:"+tempArray[j];
			}
			else if(tempArray[i].matches("[(]+.*"))
			{
				String a=tempArray[i].replaceAll("[(]+", "");
				if(!a.matches("Category:.*"))
					if(!a.matches("Place:.*"))
						if(!a.matches("Author:.*"))
							if(!a.matches("Term:.*"))
						{
							b="Term:"+a;
				
							tempArray[i]=tempArray[i].replaceAll(a, b);
						}
				
				int j=i+1;
				if(j<len)
				{
				while(!tempArray[j].matches(".*[)]"))
				{
					if(tempArray[j].matches("[(]+.*"))
						break;
					if(!tempArray[j].matches("AND|NOT|OR"))
						if(!tempArray[j].matches("Category:.*"))
							if(!tempArray[j].matches("Place:.*"))
								if(!tempArray[j].matches("Author:.*"))
									if(!tempArray[j].matches("Term:.*"))
									{
											tempArray[j]="Term:"+tempArray[j];
									}
					
					
					j++;
				}
				if(!tempArray[j].matches("AND|NOT|OR"))
					if(!tempArray[j].matches("Category:.*"))
						if(!tempArray[j].matches("Place:.*"))
							if(!tempArray[j].matches("Author:.*"))
								if(!tempArray[j].matches("Term:.*"))
									if(!tempArray[j].matches("[(]+.*"))
							{
									tempArray[j]="Term:"+tempArray[j];
							}
				}
			}
			else
			{
				if(!tempArray[i].matches("AND|OR|NOT"))
				if(!tempArray[i].contains("Term:"))
					if(!tempArray[i].contains("Category:"))
						if(!tempArray[i].contains("Place:"))
							if(!tempArray[i].contains("Author:"))
					tempArray[i]="Term:"+tempArray[i];
			}
			
		}

		//ABSORB NOTs
		
		for(int i=0;i<len;i++)
		{
			if(tempArray[i].matches("NOT"))
			{
				tempArray[i]="AND";
				if(tempArray[i+1].matches("[(].*"))
					{
					tempArray[i+1]=tempArray[i+1].replaceAll("[(]", "(<");	
				////System.out.println(tempArray[i+1]);
				int j=i+2;
				while(!tempArray[j].matches(".*[)]"))
				{
					j++;
				}
				tempArray[j]=tempArray[j].replaceAll("[)]", ">)");	
					}
				else
				{
					if(tempArray[i+1].matches(".*[)]"))
						{
						tempArray[i+1]=tempArray[i+1].replaceAll("[)]", ">)");
						tempArray[i+1]="<"+tempArray[i+1];
						}
					else
					tempArray[i+1]="<"+tempArray[i+1]+">";

				}
			}
		}
		for(int i=0;i<len;i++)
		{
			tempArray[i]=tempArray[i].replaceAll("[(]", "[ ");
			tempArray[i]=tempArray[i].replaceAll("[)]", " ]");

		}
		
		int i=0;
		while(i<len)
		{
			if(!tempArray[i].matches("AND|OR"))
				if(i<len-1)
				if(!tempArray[i+1].matches("AND|OR"))
				{
					tempArray[i]="$"+tempArray[i]+" "+defaultOperator;
					count++;
					
					int j=i+1;
					
					while(j<len-1)
					{
						if(tempArray[j].matches("AND|OR"))
						{
							break;
						}
						else
						{
							if(tempArray[j+1].matches("AND|OR"))
							{
								break;
							}
							count++;
							tempArray[j]=tempArray[j]+" "+defaultOperator;
							
							
							j++;
						}
						i=j;
							
					}
					
					tempArray[j]=tempArray[j]+"$";
					
				}
				
			i++;
			
			for(int k=0;k<len;k++)
			{
				//System.out.println(tempArray[k]);
				if(tempArray[k].matches("[$].*"))
					if(count<len-1)
						{
						if(!tempArray[k].contains("["))
						{
							if(!tempArray[k].contains("]"))
						{
						tempArray[k]=tempArray[k].replaceAll("[$]", "[ ");
						for(int j=k+1;j<len;j++)
						{
							if(tempArray[j].matches(".*[$]"))
								tempArray[j]=tempArray[j].replaceAll("[$]", " ]");
						}
						}
							else
							{
								tempArray[k]=tempArray[k].replaceAll("[$]", "");
								for(int j=k+1;j<len;j++)
								{
									if(tempArray[j].matches(".*[$]"))
										tempArray[j]=tempArray[j].replaceAll("[$]", "");
								}
							}
						}
						
						else
						{
							tempArray[k]=tempArray[k].replaceAll("[$]", "");
							for(int j=k+1;j<len;j++)
							{
								if(tempArray[j].matches(".*[$]"))
									tempArray[j]=tempArray[j].replaceAll("[$]", "");
							}
						}
						
						}
					else if(count==len-1)
					{
						tempArray[k]=tempArray[k].replaceAll("[$]", "");
						for(int j=k+1;j<len;j++)
						{
							if(tempArray[j].matches(".*[$]"))
								tempArray[j]=tempArray[j].replaceAll("[$]", "");
						}
					}
			}
			
			
		}
		

		
		
		query="";

		for(int w=0;w<len;w++)
		{
			query=query+tempArray[w]+" ";
		}
		
		query="{ "+query+"}";
		//System.out.println("FINAL:"+query);
		
		q.setQuerystring(query);						
			
		return q;
		
	
	}
	
	/*public static void main(String[] args)
	{
		try
		{
			String s=parse("(((\"x y\" AND B) OR (C AND D)) AND E)","OR");
			if(s.contentEquals("{ [ [ [ Term:\"x y\" AND Term:B ] OR [ Term:C AND Term:D ] ] AND Term:E ] }"))
				Query q=parse("(Love NOT War) AND Category:(movies NOT crime)","OR");
				String s=q.toString();
			if(s.contentEquals("{ [ Term:A OR Term:B OR Term:C OR Term:D ] AND [ [ Term:E AND Term:F ] OR [ Term:G AND Term:H ] ] AND [ [ Term:I OR Term:J OR Term:K ] AND [ Term:L OR Term:M OR Term:N OR Term:O ] ] AND [ Term:P OR [ Term:Q OR [ Term:R OR [ Term:S AND Term:T ] ] ] ] }"))
				//System.out.println("PASS");
			else
				//System.out.println("FAIL");
	
	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}*/
}
