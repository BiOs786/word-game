package dictionary;

import java.util.TreeSet;

public class DictionaryBST implements Dictionary 
{
   private TreeSet<String> dict;
	
	public DictionaryBST() {
		// TODO Auto-generated constructor stub
		dict = new TreeSet<String>();
	}
    
    /** Add this word to the dictionary.  Convert it to lowercase first
     * for the assignment requirements.
     * word:The word to add
     * true:if the word was added to the dictionary 
     * (it wasn't already there). 
     **/
    public boolean addWord(String word) {
    	// TODO: Implement this method
    	String w = word.toLowerCase();
    	if(dict.contains(w))
    		return false;
    	dict.add(w);
        return true;
    }


    /** Return the number of words in the dictionary */
    public int size()
    {
    	// TODO: Implement this method
        return dict.size();
    }

    // Is this a word according to this dictionary? 
    public boolean isWord(String s) {
    	//TODO: Implement this method
    	String s1 = s.toLowerCase();
    	if(dict.contains(s1))
    		return true;
    	else
    		return false;
    }

}
