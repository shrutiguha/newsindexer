package edu.buffalo.cse.irf14.analysis;

public class StemmerTokenFilter extends TokenFilter{
	 public char[] b;
	   public int i,     /* offset into b */
	               i_end, /* offset to end of stemmed word */
	               j, k;
	   public static final int INC = 50;
	                     /* unit of size whereby b is increased */
	

	public StemmerTokenFilter(TokenStream stream) {
		super(stream);
		  b = new char[INC];
	      i = 0;
	      i_end = 0;
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
//		String n=new String();
		String text = t.getTermText();
		//System.out.println(text);
		      char[] w = new char[501];
		   //  Stemmer s = new Stemmer();
		     w=text.toCharArray();
		     b= new char[w.length];
for(int j=0;j<w.length;j++)
{
	//System.out.println(w[j]);
	b[j]=w[j];
	//System.out.println(b[j]);
}
if(text.matches("^[^a-zA-Z]+.*"))
{}
else if(text.matches(".+ies$"))
	text=text.replaceAll("ies$", "i");
else if(text.matches(".+ness$"))
	text=text.replaceAll("ness$", "");
else if(text.matches(".+ed$"))
	text=text.replaceAll("ed$", "");
else if(text.matches(".+es$"))
	text=text.replaceAll("es$", "");
else if(text.matches(".+ing$"))
	text=text.replaceAll("ing$", "");
else if(text.matches(".+y$"))
	text=text.replaceAll("y$", "i");
else if(text.matches(".+ator$"))
	text=text.replaceAll("ator$", "");
else if(text.matches(".+ement$"))
	text=text.replaceAll("ement$", "");
else if(text.matches(".+ic$"))
	text=text.replaceAll("ic$", "");
else if(text.matches(".+ional$"))
	text=text.replaceAll("ional$", "");
else if(text.matches(".+ent$"))
	text=text.replaceAll("ent$", "");
else if(text.matches(".+ings$"))
	text=text.replaceAll("ings$", "");
else if(text.matches(".+ational$"))
	text=text.replaceAll("ational$", "ate");
else if(text.matches(".+ful$"))
	text=text.replaceAll("ful$", "");
else if(text.matches(".+izer$"))
	text=text.replaceAll("izer$", "ize");
else if(text.matches(".+s$"))
	text=text.replaceAll("s$", "");
//System.out.println(text);
//stem();
//		 
//		                      for(int i=0;i<b.length;i++)
//		                    	n=n+getResultBuffer();
//		                      //System.out.println("Result:"+getResultBuffer());
        t.setTermText(text);
	}

	
   public void add(char ch)
   {  if (i == b.length)
      {  char[] new_b = new char[i+INC];
         for (int c = 0; c < i; c++) new_b[c] = b[c];
         b = new_b;
      }
      b[i++] = ch;
   }

   /**
    * After a word has been stemmed, it can be retrieved by toString(),
    * or a reference to the internal buffer can be retrieved by getResultBuffer
    * and getResultLength (which is generally more efficient.)
    */
   public String toString() { return new String(b,0,i_end); }

   /**
    * Returns the length of the word resulting from the stemming process.
    */
   public int getResultLength() { return i_end; }

   /**
    * Returns a reference to a character buffer containing the results of
    * the stemming process.  You also need to consult getResultLength()
    * to determine the length of the result.
    */
   public char[] getResultBuffer() { return b; }

   /* cons(i) is true <=> b[i] is a consonant. */

   private final boolean cons(int i)
   {  switch (b[i])
      {  case 'a': case 'e': case 'i': case 'o': case 'u': return false;
         case 'y': return (i==0) ? true : !cons(i-1);
         default: return true;
      }
   }

   /* m() measures the number of consonant sequences between 0 and j. if c is
      a consonant sequence and v a vowel sequence, and <..> indicates arbitrary
      presence,

         <c><v>       gives 0
         <c>vc<v>     gives 1
         <c>vcvc<v>   gives 2
         <c>vcvcvc<v> gives 3
         ....
   */

   private final int m()
   {  int n = 0;
      int i = 0;
      while(true)
      {  if (i > j) return n;
         if (! cons(i)) break; i++;
      }
      i++;
      while(true)
      {  while(true)
         {  if (i > j) return n;
               if (cons(i)) break;
               i++;
         }
         i++;
         n++;
         while(true)
         {  if (i > j) return n;
            if (! cons(i)) break;
            i++;
         }
         i++;
       }
   }

   /* vowelinstem() is true <=> 0,...j contains a vowel */

   private final boolean vowelinstem()
   {  int i; for (i = 0; i <= j; i++) if (! cons(i)) return true;
      return false;
   }

   /* doublec(j) is true <=> j,(j-1) contain a double consonant. */

   private final boolean doublec(int j)
   {  if (j < 1) return false;
      if (b[j] != b[j-1]) return false;
      return cons(j);
   }

   /* cvc(i) is true <=> i-2,i-1,i has the form consonant - vowel - consonant
      and also if the second c is not w,x or y. this is used when trying to
      restore an e at the end of a short word. e.g.

         cav(e), lov(e), hop(e), crim(e), but
         snow, box, tray.

   */

   private final boolean cvc(int i)
   {  if (i < 2 || !cons(i) || cons(i-1) || !cons(i-2)) return false;
      {  int ch = b[i];
         if (ch == 'w' || ch == 'x' || ch == 'y') return false;
      }
      return true;
   }

   private final boolean ends(String s)
   {  int l = s.length();
      int o = k-l+1;
      if (o < 0) return false;
      for (int i = 0; i < l; i++) if (b[o+i] != s.charAt(i)) return false;
      j = k-l;
      return true;
   }

   /* setto(s) sets (j+1),...k to the characters in the string s, readjusting
      k. */

   private final void setto(String s)
   {  int l = s.length();
      int o = j+1;
      for (int i = 0; i < l; i++) b[o+i] = s.charAt(i);
      k = j+l;
   }

   /* r(s) is used further down. */

   private final void r(String s) { if (m() > 0) setto(s); }

   /* step1() gets rid of plurals and -ed or -ing. e.g.

          caresses  ->  caress
          ponies    ->  poni
          ties      ->  ti
          caress    ->  caress
          cats      ->  cat

          feed      ->  feed
          agreed    ->  agree
          disabled  ->  disable

          matting   ->  mat
          mating    ->  mate
          meeting   ->  meet
          milling   ->  mill
          messing   ->  mess

          meetings  ->  meet

   */

   private final void step1()
   {  if (b[k] == 's')
      {  if (ends("sses")) k -= 2; else
         if (ends("ies")) setto("i"); else
         if (b[k-1] != 's') k--;
      }
      if (ends("eed")) { if (m() > 0) k--; } else
      if ((ends("ed") || ends("ing")) && vowelinstem())
      {  k = j;
         if (ends("at")) setto("ate"); else
         if (ends("bl")) setto("ble"); else
         if (ends("iz")) setto("ize"); else
         if (doublec(k))
         {  k--;
            {  int ch = b[k];
               if (ch == 'l' || ch == 's' || ch == 'z') k++;
            }
         }
         else if (m() == 1 && cvc(k)) setto("e");
     }
   }

   /* step2() turns terminal y to i when there is another vowel in the stem. */

   private final void step2() { if (ends("y") && vowelinstem()) b[k] = 'i'; }

   /* step3() maps double suffices to single ones. so -ization ( = -ize plus
      -ation) maps to -ize etc. note that the string before the suffix must give
      m() > 0. */

   private final void step3() { if (k == 0) return; /* For Bug 1 */ switch (b[k-1])
   {
       case 'a': if (ends("ational")) { r("ate"); break; }
                 if (ends("tional")) { r("tion"); break; }
                 break;
       case 'c': if (ends("enci")) { r("ence"); break; }
                 if (ends("anci")) { r("ance"); break; }
                 break;
       case 'e': if (ends("izer")) { r("ize"); break; }
                 break;
       case 'l': if (ends("bli")) { r("ble"); break; }
                 if (ends("alli")) { r("al"); break; }
                 if (ends("entli")) { r("ent"); break; }
                 if (ends("eli")) { r("e"); break; }
                 if (ends("ousli")) { r("ous"); break; }
                 break;
       case 'o': if (ends("ization")) { r("ize"); break; }
                 if (ends("ation")) { r("ate"); break; }
                 if (ends("ator")) { r("ate"); break; }
                 break;
       case 's': if (ends("alism")) { r("al"); break; }
                 if (ends("iveness")) { r("ive"); break; }
                 if (ends("fulness")) { r("ful"); break; }
                 if (ends("ousness")) { r("ous"); break; }
                 break;
       case 't': if (ends("aliti")) { r("al"); break; }
                 if (ends("iviti")) { r("ive"); break; }
                 if (ends("biliti")) { r("ble"); break; }
                 break;
       case 'g': if (ends("logi")) { r("log"); break; }
   } }

   /* step4() deals with -ic-, -full, -ness etc. similar strategy to step3. */

   private final void step4() { switch (b[k])
   {
       case 'e': if (ends("icate")) { r("ic"); break; }
                 if (ends("ative")) { r(""); break; }
                 if (ends("alize")) { r("al"); break; }
                 break;
       case 'i': if (ends("iciti")) { r("ic"); break; }
                 break;
       case 'l': if (ends("ical")) { r("ic"); break; }
                 if (ends("ful")) { r(""); break; }
                 break;
       case 's': if (ends("ness")) { r(""); break; }
                 break;
   } }

   /* step5() takes off -ant, -ence etc., in context <c>vcvc<v>. */

   private final void step5()
   {   if (k == 0) return; /* for Bug 1 */ switch (b[k-1])
       {  case 'a': if (ends("al")) break; return;
          case 'c': if (ends("ance")) break;
                    if (ends("ence")) break; return;
          case 'e': if (ends("er")) break; return;
          case 'i': if (ends("ic")) break; return;
          case 'l': if (ends("able")) break;
                    if (ends("ible")) break; return;
          case 'n': if (ends("ant")) break;
                    if (ends("ement")) break;
                    if (ends("ment")) break;
                    /* element etc. not stripped before the m */
                    if (ends("ent")) break; return;
          case 'o': if (ends("ion") && j >= 0 && (b[j] == 's' || b[j] == 't')) break;
                                    /* j >= 0 fixes Bug 2 */
                    if (ends("ou")) break; return;
                    /* takes care of -ous */
          case 's': if (ends("ism")) break; return;
          case 't': if (ends("ate")) break;
                    if (ends("iti")) break; return;
          case 'u': if (ends("ous")) break; return;
          case 'v': if (ends("ive")) break; return;
          case 'z': if (ends("ize")) break; return;
          default: return;
       }
       if (m() > 1) k = j;
   }

   /* step6() removes a final -e if m() > 1. */

   private final void step6()
   {  j = k;
      if (b[k] == 'e')
      {  int a = m();
         if (a > 1 || a == 1 && !cvc(k-1)) k--;
      }
      if (b[k] == 'l' && doublec(k) && m() > 1) k--;
   }

   /** Stem the word placed into the Stemmer buffer through calls to add().
    * Returns true if the stemming process resulted in a word different
    * from the input.  You can retrieve the result with
    * getResultLength()/getResultBuffer() or toString().
    */
   public void stem()
   {  k = i;
   
//   for(int i=0;i<b.length;i++)
// 	  //System.out.println(b[i]);
      if (k > 1) { 
    	  step1(); 
      //System.out.println("1");
      for(int i=0;i<b.length;i++)
    	  //System.out.println("b:"+b[i]);
      step2();
//      //System.out.println("2");
//      for(int i=0;i<b.length;i++)
//    	  //System.out.println(b[i]);
step3(); 
////System.out.println("3");
//for(int i=0;i<b.length;i++)
//	  //System.out.println(b[i]);
step4();
////System.out.println("4");
//for(int i=0;i<b.length;i++)
//	  //System.out.println(b[i]);
step5(); 
////System.out.println("5");
//for(int i=0;i<b.length;i++)
//	  //System.out.println(b[i]);
step6(); 
//for(int i=0;i<b.length;i++)
//	  //System.out.println(b[i]);
}
      i_end = k+1; i = 0;
   }

   /** Test program for demonstrating the Stemmer.  It reads text from a
    * a list of files, stems each word, and writes the result to standard
    * output. Note that the word stemmed is expected to be in lower case:
    * forcing lower case must be done outside the Stemmer class.
    * Usage: Stemmer file-name file-name ...
    */
  
}





