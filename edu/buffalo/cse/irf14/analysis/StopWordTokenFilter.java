package edu.buffalo.cse.irf14.analysis;

public class StopWordTokenFilter extends TokenFilter {

	public StopWordTokenFilter(TokenStream stream) {
		super(stream);
	}

	@Override
	public boolean increment() throws TokenizerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TokenStream getStream() {
		// TODO Auto-generated method stub
		return null;
	}

}
