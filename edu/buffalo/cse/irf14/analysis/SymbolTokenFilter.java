package edu.buffalo.cse.irf14.analysis;

public class SymbolTokenFilter extends TokenFilter {
	
	public SymbolTokenFilter(TokenStream stream) {
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
		String text = t.getTermText();
		
	}

}
