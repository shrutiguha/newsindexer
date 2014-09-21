package edu.buffalo.cse.irf14.analysis;

public class CapitalizationTokenFilter extends TokenFilter {

	public CapitalizationTokenFilter(TokenStream stream) {
		super(stream);
	}

	@Override
	public boolean increment() throws TokenizerException {
		// TODO Auto-generated method stub
		if(this.tstream.hasNext())
		{
			filter(this.tstream.next());
			return true;
		}
		return false;
	}

	@Override
	public TokenStream getStream() {
		// TODO Auto-generated method stub
		return this.tstream;
	}
	
	public void filter(Token t)
	{
		//Where i add the filter part
		String text = t.getTermText();
		System.out.println(text);
		int flag=0;
		boolean s=false;
		String temp=null;
		if(!tstream.hasPrevious())
		{
			System.out.println("First word");
			if(isUpper(text))
			{
				System.out.println("It is uppercase");
				tstream.saveCurrent();
			while(!isEnd())
			{ 
				s=checkNextToken();
				if(!s)
				{
					flag=1;
				    break;
				} 
			}
			if(flag==1)
			{
				tstream.setCurrent();
				System.out.println("The whole sentence is not uppercase");
//
//				text=text.toLowerCase();
//				t.setTermText(text);
//				System.out.println(text);
//while(!isEnd())
//{
//
//		Token t1=tstream.next();
//		text=t1.getTermText();
//				System.out.println(text);
//
//				
//					text=text.toLowerCase();
//					t1.setTermText(text);
//					
			
//}
			}
			else
			{
				System.out.println("Whole sentence is in uppercase");
				tstream.setCurrent();
				text=text.toLowerCase();
				t.setTermText(text);
				System.out.println(text);
while(!isEnd())
{

		Token t1=tstream.next();
		text=t1.getTermText();
				System.out.println(text);

				
					text=text.toLowerCase();
					t1.setTermText(text);
			}
			}
		}
			tstream.setCurrent();
			text=text.toLowerCase();
			t.setTermText(text);
		
	
		}
		else
		{
			tstream.saveCurrent();

			//System.out.println("Not the first word");
			if(!isLower(text))
			{
				System.out.println("T1 camel");
				if(tstream.hasNext())
				{
				Token t1=tstream.next();
				String nextToken=t1.getTermText();
				
				if(!isLower(nextToken))
				{
					System.out.println("T2 camel");
					t.merge(text,nextToken);
					String a=t.getTermText();
					System.out.println(a);
					tstream.remove();
				}

				}
			}
		

				
		}
		System.out.println(text);

		
	}
	public boolean isLower(String a)
	{
		for (char c : a.toCharArray()) {
		    if (Character.isUpperCase(c)) {
		    	return false;
		    }}
		return true;
	}
public boolean isUpper(String a)
{
	for (char c : a.toCharArray()) {
	    if (Character.isLowerCase(c)) {
	    	return false;
	    }}
	return true;
}
public boolean checkNextToken()
{
	String text=null;
	if(tstream.hasNext())
	{
		Token t1=tstream.next();
		text=t1.getTermText();
		if(isUpper(text))
			return true;
			
	}
	System.out.println("Nextword Not Uppercase");
	return false;
}
public boolean isEnd()
{
	if(!tstream.hasNext())
		return true;
	return false;
}
}
