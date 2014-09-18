/**
 * 
 */
package edu.buffalo.cse.irf14.document;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author nikhillo
 * Class that parses a given file into a Document
 */
public class Parser {
	/**
	 * Static method to parse the given file into the Document object
	 * @param filename : The fully qualified filename to be parsed
	 * @return The parsed and fully loaded Document object
	 * @throws ParserException In case any error occurs during parsing
	 */
	public static Document parse(String filename) throws ParserException {
		// TODO YOU MUST IMPLEMENT THIS
		
		Document d = new Document();
		
		try 
		{
			String line = new String();
			String lowerCaseLine = new String();
			String content = new String();
			boolean title= false, placeDate = false;
			
			if(filename == null || filename.equals(""))
				throw new ParserException();
			else
			{
				BufferedReader br = new BufferedReader(new FileReader(filename));
				filename = filename.replace('\\', '/');
				String fileArray[] = filename.split("/");
				int fileArrayLength = fileArray.length;
				if(fileArrayLength > 0)
				{
					String fileid = fileArray[fileArrayLength-1]; 
//					if(!checkFile(fileid))
//						throw new ParserException();
					d.setField(FieldNames.FILEID, fileid);
				}
				if(fileArrayLength > 1)
					d.setField(FieldNames.CATEGORY, fileArray[fileArrayLength-2]);
							
				while ((line = br.readLine()) != null)   
				{
		            if(line.isEmpty())
		            	continue;
		            else if(!title)
		            {
		            	title = true;		
		            	d.setField(FieldNames.TITLE, line.trim());
		            }
		            else if((lowerCaseLine = line.toLowerCase()).contains("<author>"))
		            {
		            	String temp = line.substring(lowerCaseLine.indexOf("by")+2, lowerCaseLine.indexOf("</author>"));
		            	String tempArray[] = temp.split(",");
		            	int tempArrayLength = tempArray.length;
		            	if(tempArrayLength > 0)
		            		d.setField(FieldNames.AUTHOR, tempArray[0].trim());
		            	if(tempArrayLength > 1)
		            		d.setField(FieldNames.AUTHORORG, tempArray[1].trim());
		            }
		            else if(!placeDate)
		            {
		            	placeDate = true;
		            	String tempArray[] = line.split("-");
		            	int tempArrayLength = tempArray.length;
		            	if(tempArrayLength > 0)
		            		content += tempArray[tempArray.length-1];
		            	if(tempArrayLength > 1)
		            	{
		            		String placeDateArray[] = tempArray[0].split(",");
		            		int placeDateArrayLength = placeDateArray.length;
		            		if(placeDateArrayLength > 0)
		            			d.setField(FieldNames.NEWSDATE, placeDateArray[placeDateArrayLength-1].trim());
		            		if(placeDateArrayLength > 1)
		            		{
		            			String place = new String();
		            			for(int i=0;i<placeDateArrayLength-1;i++)
		            				place += placeDateArray[i].trim()+", ";
		            			d.setField(FieldNames.PLACE, place.substring(0, place.length()-2).trim());
		            		}
		            	}
		            }
		            else if(placeDate)
		            {
		            	content += line;
		            }
				}
				
				d.setField(FieldNames.CONTENT, content);
				
				br.close();
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return d;
	}
	
	public static boolean checkFile(String file)
	{
		Pattern pattern = Pattern.compile("[^/./\\:*?\"<>|]");
	    return !pattern.matcher(file).find();
	}
}
