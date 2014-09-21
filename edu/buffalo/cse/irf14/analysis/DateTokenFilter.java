package edu.buffalo.cse.irf14.analysis;

public class DateTokenFilter extends TokenFilter {

	public DateTokenFilter(TokenStream stream) {
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
			
		if(isDate(text))
		{
			text=getDate(text);
				System.out.println("Date");
			if(tstream.hasNext())
			{
				Token t1=tstream.next();
				String nextTerm=t1.getTermText();
				nextTerm=nextTerm.toLowerCase();
				System.out.println(nextTerm);
				//if(isMonth())
				{
					//nextTerm=getMonth(nextTerm);
					System.out.println("Month");
				
					if(tstream.hasNext())
					{
						tstream.saveCurrent();
						Token t2=tstream.next();
						String nextTerm1=t2.getTermText();
						if(nextTerm1.matches("[0-9]+[,]|[0-9]+|[,][0-9]+"))
						{
							System.out.println("Another Number");
							if(nextTerm1.length()==4)
							{
								tstream.saveCurrent();
								System.out.println("Year");
								int year=Integer.parseInt(nextTerm1);
						//		text=nextTerm1+mon+text;
								tstream.remove();
								tstream.moveBack();
								tstream.remove();
								tstream.setCurrent();
							}
						}
						else
						{
						//	text="1990"+mon+text;
							tstream.moveBack();
							tstream.remove();
							tstream.setCurrent();
						}
				}
			}
			}
			}
			else if(text.length()==4)
			{}
		}
		
		
//		
//		System.out.println(text);
//		t.setTermText(text);
//	}
//	
public boolean isDate(String s)
{
	if(s.matches("[0-9]+[,]|[0-9]+|[,][0-9]+"))
	{
		System.out.println("Number");
		s=s.replaceAll("[,]", "");
		int date=Integer.parseInt(s);
		if(date>=1 && date<=31)
		{
		return true;
		}
	}
	return false;
}

public String getDate(String s)
{
	s=s.replaceAll("[,]", "");
	int date=Integer.parseInt(s);
	if(date>=1 && date<=9)
		s="0"+s;
	System.out.println(s);
	return s;
}

public boolean isMonth(String s)
{
	if(s.matches("^jan[a-zA-Z]*|^feb|^mar|^apr|^may|^jun|^jul|^aug|^sep|^oct|^nov|^dec"))
	{
	return true;
}
	return false;
}
//public String getMonth(String s)
//{
//	String mon=null;
//	if(s.matches("^jan[a-zA-Z]*"))
//		mon="01";
//	else if(s.matches("^feb"))
//		mon="02";
//	else if(s.matches("^mar"))
//		mon="03";
//	else if(s.matches("^apr"))
//		mon="04";
//	else if(s.matches("^may"))
//		mon="05";
//	else if(s.matches("^jun"))
//		mon="06";
//	else if(s.matches("^jul"))
//		mon="07";
//	else if(s.matches("^aug"))
//		mon="08";
//	else if(s.matches("^sep"))
//		mon="09";
//	else if(s.matches("^oct"))
//		mon="10";
//	else if(s.matches("^nov"))
//		mon="11";
//	else if(s.matches("^dec"))
//		mon="12";
//}
//
//
}
