package edu.buffalo.cse.irf14.analysis;

public class ContentAnalyzer implements Analyzer{
	
	TokenStream tstream;
	TokenFilterFactory factory;

	public ContentAnalyzer(TokenStream tstream) {
		// TODO Auto-generated constructor stub
		this.tstream = tstream;
	}
	
	public void setTokenFilterFactory(TokenFilterFactory factory)
	{
		this.factory = factory;
	}

	@Override
	public boolean increment() throws TokenizerException {
		// TODO Auto-generated method stub
		if(this.tstream.hasNext())
		{
			//analyze(this.tstream.next());
			return true;
		}
		return false;
	}

	@Override
	public TokenStream getStream() {
		// TODO Auto-generated method stub
		return this.tstream;
	}
	
	public void analyze(){
		try{
			TokenFilter filter = factory.getFilterByType(TokenFilterType.SYMBOL, this.tstream);
			if(filter != null)
			{
				while (filter.increment()) {
					
				}
				this.tstream = filter.getStream();
			}
			
			this.tstream.reset();
			
			filter = factory.getFilterByType(TokenFilterType.CAPITALIZATION, this.tstream);
			if(filter != null)
			{
				while (filter.increment()) {
					
				}
				this.tstream = filter.getStream();
			}
			
			this.tstream.reset();
			
			
			
			filter = factory.getFilterByType(TokenFilterType.NUMERIC, this.tstream);
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
			
			filter = factory.getFilterByType(TokenFilterType.STOPWORD, this.tstream);
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
