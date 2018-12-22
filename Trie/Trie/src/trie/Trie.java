package trie;

import java.util.ArrayList;



/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		
		//Check to see if allWords array contains at least one string
		if (allWords.length == 0) {
			return null;
		}
		
		//This is the root of the tree
		TrieNode root = new TrieNode(null, null, null);
		TrieNode pointer = null;
		
		
		//This will loop through the allWords array
		for (int i = 0; i < allWords.length; i++) {
		
		Indexes newIndex;
		//Indexes newIndex2; this is not used
			
		
			if (i == 0) {
				newIndex = new Indexes( i, (short) 0, (short) (allWords[0].length() - 1) );
				root.firstChild = new TrieNode(newIndex, null, null);
				pointer = root.firstChild;
			}
			else {
				pointer = root.firstChild;
				//This holds the parent
				TrieNode previous = root;
				
				//
				TrieNode prefixPointer = null;
				
				//This tells we should go down (to the child) or right (to the sibling)
				boolean wentToChild = true;
				boolean wentInsidePrefix = false;
				
				
				boolean insert=false;
				
				while (pointer != null) {
					
					//this stores the longest common prefix 
					String word1 = allWords[pointer.substr.wordIndex].substring(0, pointer.substr.endIndex+1);
					String word2 = allWords[i];
					
					int commonLongestPrefix = commonPrefix(word1, word2);
				
					//if commonLongestPrefix is -1
					if (commonLongestPrefix == -1) {
						previous = pointer;
						pointer = pointer.sibling;
						wentToChild = false;
					}
					else {
						if (commonLongestPrefix - 1 == pointer.substr.endIndex) {
							//This means that we are in a prefix
							previous = pointer;
							pointer = pointer.firstChild;
							wentToChild = true;
							wentInsidePrefix = true;
							prefixPointer = previous;
						}
						else {
							Indexes prefix = null;
							TrieNode word = null;
							
							if (wentInsidePrefix == false) {
								prefix = new Indexes(pointer.substr.wordIndex, (short) 0, (short) (commonLongestPrefix - 1));
								Indexes newWord = new Indexes(i, (short) commonLongestPrefix, (short) (allWords[i].length() - 1)); 
								Indexes update = new Indexes(pointer.substr.wordIndex, (short) commonLongestPrefix, (short) pointer.substr.endIndex);
								pointer = new TrieNode(update, pointer.firstChild, pointer.sibling);
								word = new TrieNode(newWord, null, null);
								
								TrieNode newPrefix2= new TrieNode(prefix, pointer, pointer.sibling);
								pointer.sibling = word;
								if(wentToChild == false) {
									previous.sibling = newPrefix2;
								}
								else {
									previous.firstChild = newPrefix2;
								}
								insert = true;
								break;
							}
							else {
								if (commonLongestPrefix - 1 != prefixPointer.substr.endIndex) {
									prefix = new Indexes(pointer.substr.wordIndex, (short) (prefixPointer.substr.endIndex + 1), (short) (commonLongestPrefix - 1));
									Indexes newWord = new Indexes(i, (short) commonLongestPrefix, (short) (allWords[i].length() - 1)); 
									Indexes update = new Indexes(pointer.substr.wordIndex, (short) commonLongestPrefix, (short) pointer.substr.endIndex);
									pointer = new TrieNode(update, pointer.firstChild, pointer.sibling);
									word = new TrieNode(newWord, null, null);
								
									TrieNode newPrefix = new TrieNode(prefix, pointer, pointer.sibling);
									pointer.sibling = word;
								
									if (wentToChild == false) {
										previous.sibling = newPrefix;
									}
									else {
										previous.firstChild = newPrefix;
									}
								insert=true;
								break;
								}
								else {
									previous = pointer;
									pointer = pointer.sibling;
									wentToChild = false;
								}
							}
						}	
					}
				}//end of while loop
				
				//This occurs when there is no common prefix
				if (insert == false) {
					if (wentInsidePrefix) {
						Indexes newWord = new Indexes(i, (short) (prefixPointer.substr.endIndex+1), (short) (allWords[i].length() - 1));
						TrieNode word2 = new TrieNode(newWord, null, null);
						previous.sibling = word2;
					}
					else {
						Indexes newWord = new Indexes(i, (short) 0, (short) (allWords[i].length() - 1));
						TrieNode word2 = new TrieNode(newWord, null, null);
						previous.sibling = word2;
					}
				}
				
			}
			
		}
		return root;
	}
	
	//--------------Private Methods--------------//
	
	//this checks to find the longest common prefix between two strings with a common prefix
	private static int commonPrefix(String word1, String word2) {
		int commonPrefixWithSimilar = 0;
		int minLength = Math.min(word1.length(), word2.length());
		
		for (int i = 0; i < minLength; i++) {
			
			if (word1.charAt(0) != word2.charAt(0)) {
				return -1;
			}
			
			if (word1.charAt(i) == word2.charAt(i)) {
				commonPrefixWithSimilar++;
			}
			else {
				return commonPrefixWithSimilar;
			}
		}
		return commonPrefixWithSimilar;
	}
	
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		
		
		
		ArrayList<TrieNode> holder = new ArrayList<TrieNode>();
		TrieNode pointer = root.firstChild;
		
		int minlength = prefix.length();

		return leafSearcher(pointer, holder, allWords, minlength, prefix);
	}
	
	private static ArrayList<TrieNode> leafSearcher(TrieNode pointer, ArrayList<TrieNode> holder, String[]allWords, int minlength, String prefix){
		
		while(pointer != null) {
			String word=allWords[pointer.substr.wordIndex].substring(0, pointer.substr.endIndex + 1);
			int commonLongestPrefix = commonPrefix(word, prefix);
			if(commonLongestPrefix == -1) {
				pointer=pointer.sibling;
			}
			else{
				if(commonLongestPrefix < minlength) {
					if(pointer.firstChild != null) {
						holder = leafSearcher(pointer.firstChild, holder, allWords, minlength, prefix);
						if(holder==null) {
							holder = new ArrayList<TrieNode>();
						}
						pointer = pointer.sibling;
					}
					else {
						pointer = pointer.sibling;
					}
					
					//This is finding the answers
				}
				else {
					if(pointer.firstChild != null) {
						holder = leafSearcher(pointer.firstChild, holder, allWords, minlength, prefix);
						pointer=pointer.sibling;
						if(holder==null) {
							holder = new ArrayList<TrieNode>();
						}
					}
					else {
						holder.add(pointer);
						pointer = pointer.sibling;
					}	
				}	
			}
		}
		//Thats all me
		if (holder==null || holder.isEmpty() == true) {
			return null;
		}
		return holder;
	}
	
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
