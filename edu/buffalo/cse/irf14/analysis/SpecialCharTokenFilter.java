package edu.buffalo.cse.irf14.analysis;

public class SpecialCharTokenFilter extends TokenFilter {

	public SpecialCharTokenFilter(TokenStream stream) {
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
				if(text.matches("[a-zA-Z]+-[a-zA-Z]+"))
			text=text.replaceAll("-", " ");
			else if(!text.matches("[^a-zA-Z0-9]*[a-zA-Z0-9]+[-][a-zA-Z0-9]+[^a-zA-Z0-9]*"))
				text=text.replaceAll("-+", "");
		text=text.replaceAll("[^a-zA-Z0-9.?!'-]", "");
		System.out.println(text);


        t.setTermText(text);
	}


}
