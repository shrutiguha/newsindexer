package edu.buffalo.cse.irf14.analysis;

public class NewsDateAnalyzer implements Analyzer{

	TokenStream tstream;
	TokenFilterFactory factory;

	public NewsDateAnalyzer(TokenStream tstream) {
		// TODO Auto-generated constructor stub
		this.tstream = tstream;
	}

	@Override
	public boolean increment() throws TokenizerException {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setTokenFilterFactory(TokenFilterFactory factory)
	{
		this.factory = factory;
	}

	@Override
	public TokenStream getStream() {
		// TODO Auto-generated method stub
		return this.tstream;
	}
	
	public void analyze(){
		try{	
			TokenFilter filter = factory.getFilterByType(TokenFilterType.DATE, this.tstream);
			if(filter != null)
			{
				while (filter.increment()) {
					
				}
				this.tstream = filter.getStream();
			}
			
			this.tstream.reset();
			
			filter = factory.getFilterByType(TokenFilterType.SPECIALCHARS, this.tstream);
			if(filter != null)
			{
				while (filter.increment()) {
					
				}
				this.tstream = filter.getStream();
			}
			this.tstream.reset();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


}
