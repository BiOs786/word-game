package wordGame;
import dictionary.*;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class AutoCompleteDictionaryTrie implements  Dictionary, AutoComplete {

    private TrieNode root,curr,prev;
    private int size=0;
    private int count=0;
    private int num=0;
    private LinkedList<TrieNode> queue = new LinkedList<TrieNode>();
    List<String> predictions = new ArrayList<String>();
    List<String> completions;
    public AutoCompleteDictionaryTrie()
	{
		root = new TrieNode();
	}
	
	/* Insert a word into the trie. 
	 * This method adds a word by creating and linking the necessary trie nodes 
	 * into the trie, as described outlined in the videos for this week. It 
	 * should appropriately use existing nodes in the trie, only creating new 
	 * nodes when necessary. E.g. If the word "no" is already in the trie, 
	 * then adding the word "now" would add only one additional node 
	 * (for the 'w').
	 * 
	 * return true if the word was successfully added or false if it already exists
	 * in the dictionary.
	 */
	public boolean addWord(String word)
	{
		int i=0;
		char[] c = word.toLowerCase().toCharArray();
		curr = root;
		prev = root;
		Set<Character> temp = curr.getValidNextCharacters();
		while(temp.contains(word.charAt(i)) && i < word.length())
		{
			prev = curr;
			curr = curr.getChild(c[i]);
			temp = curr.getValidNextCharacters();
			i++;
			if( i==word.length())
			{
				break;
			}
			
		}
		if(i==word.length())
		{
			curr.setEndsWord(true);
			return false;
		}
		else
		{
			for(int j=i;j<word.length();j++)
			{
				
				TrieNode ref;
				if((ref = curr.insert(c[j]))!= null)
				{
					curr = ref;
				}
				else
				{
					curr.setEndsWord(false);
					return false;
				}
			}
		}
		curr.setEndsWord(true);
		return true;
	}
	
	/** 
	 * Return the number of words in the dictionary.
	 */
	public int size()
	{
	    //TODO: Implement this method
		size = 0;
		printTree();
		return size;
	}
	
	
	/** Returns whether the string is a word in the trie, using the algorithm.*/
	@Override
	public boolean isWord(String s) 
	{
	    // TODO: Implement this method
		curr = root;
		if(s.equals(""))
			return false;
		char[] c = s.toLowerCase().toCharArray();
		Set<Character> temp = curr.getValidNextCharacters();
		for(char t: c)
		{
			if(temp.contains(t))
			{
				curr = curr.getChild(t);
				temp = curr.getValidNextCharacters();
			}
			else
			{

				return false;
			}
		}
		if(curr.endsWord())
		{
			count++;
			return true;
		}
		else
		{
			return false;
		}
			
	}

	/** 
     * Return a list, in order of increasing word length,
     * containing the numCompletions shortest legal completions 
     * of the prefix string. All legal completions must be valid words in the 
     * dictionary. If the prefix itself is a valid word, it is included 
     * in the list of returned words. 
     * 
     * The list of completions must contain 
     * all of the shortest completions, but when there are ties, it may break 
     * them in any order. For example, if there the prefix string is "ste" and 
     * only the words "step", "stem", "stew", "steer" and "steep" are in the 
     * dictionary, when the user asks for 4 completions, the list must include 
     * "step", "stem" and "stew", but may include either the word 
     * "steer" or "steep".
     * 
     * If this string prefix is not in the trie, it returns an empty list.
     * 
     * prefix: The text to use at the word stem
     * numCompletions: The maximum number of predictions desired.
     * return a list containing the up to numCompletions best predictions
     */
	@Override
     public List<String> predictCompletions(String prefix, int numCompletions) 
     {
    	 // TODO: Implement this method
    	 predictions = new ArrayList<String>();
    	 if(isWord(prefix))
    		 predictions.add(prefix); 
    	 curr = root;
    	 num = numCompletions;
    	 if(num==0)
    		 return new ArrayList<String>();
    	 char[] pref = prefix.toLowerCase().toCharArray();
    	 for(char s: pref)
    	 {
    		 curr = curr.getChild(s);
    		 if(curr == null)
    			 return predictions;
    	 }
    	 if(curr!=null)
    	 {
    		 performBFSfromNode(curr);
    	 }
         return predictions.subList(0, completions.size());
     }

 	public void printTree()
 	{
 		printNode(root);
 	}
 	
 	public void BFS()
 	{
 		performBFSfromNode(root);
 	}
 	
 	/*
 	 * Perform BFS fromt the current node
 	 * current: current node passed.
 	 */
 	public void performBFSfromNode(TrieNode current)
 	{
 		int count=0;
 		curr = current;
 		Set<Character> temp = curr.getValidNextCharacters();
 		for(Character s: temp)
 		{
 			queue.add(curr.getChild(s));
 			
 		}
 		while(queue.size() != 0)
 		{
 			TrieNode t = queue.remove();
 			if(isWord(t.getText()))
 			{
 				predictions.add(t.getText());
 				if(predictions.size()==num)
 					completions = new ArrayList<String>(predictions);
 			}
 			performBFSfromNode(t);	
 		}
 	}
 	/** Do a pre-order traversal from this node down 
 	 * 	curr: current node passed to start the in-order traversal.
 	 **/
 	public void printNode(TrieNode curr)
 	{
 		if (curr == null) 
 		{
 			return;
 		}
 		
 		//System.out.println(curr.getText());
 		
 		TrieNode next = null;
 		for (Character c : curr.getValidNextCharacters()) {
 			next = curr.getChild(c);
 			if(isWord(next.getText()))
 				size++;
 			printNode(next);
 		}
 	}
 	

	
}