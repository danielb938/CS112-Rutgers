package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		
		//this will hold the keywords; remember the key is the String
		HashMap<String, Occurrence> keywordsTable = new HashMap<String, Occurrence>();   
		
		//this scans the document
		Scanner sc  = new Scanner(new File(docFile));
		
		while (sc.hasNext()) {
			//this will access the keyword and make sure that it is valid
			String wordHolder = getKeyword(sc.next());
			
			if (wordHolder != null) {
				if (keywordsTable.containsKey(wordHolder) == false) {
					//create an occurrence object to pass into the second parameter of the put method
					Occurrence firstFreq = new Occurrence(docFile, 1);
					
					//since the keywordsTable does not contain the wordHolder put it in the hashtable
					keywordsTable.put(wordHolder, firstFreq);
				}
				else {
					keywordsTable.get(wordHolder).frequency++;
				}
			}
		}
		//we have to close the scanner
		sc.close();

		return keywordsTable;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		
		//this will loop through all the string in the hashmap
		for (String keyWord : kws.keySet()) {
			
			//this holds the current keyword we are working on
			String wordHolder = keyWord;
			
			//to get the occurrence of the string in the keyset we have create an ArrayList
			Occurrence freq = kws.get(keyWord);
			
			if (keywordsIndex.containsKey(wordHolder) == true) {
				keywordsIndex.get(wordHolder).add(freq);
				insertLastOccurrence(keywordsIndex.get(wordHolder));
			}
			else {
				//this will create an ArrayList of occurrences to store 
				ArrayList<Occurrence> newOccurrencesHolder = new ArrayList<Occurrence>();
				newOccurrencesHolder.add(freq);
				keywordsIndex.put(wordHolder, newOccurrencesHolder);
			}	
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		        
        if (word == null || word.length() <= 0) {
            return null;
        }
        
        //ADDED. we return null for things that are not letters before the keyword
        if (Character.isLetter(word.charAt(0)) == false) {
        	return null;
        }
        
        //String allowedLetters = "abcdefghijklmnopqrstuvwxyz";
        int index = 0;
        int lastIndex = 0;
        
        //this variable will hold the length minus the first index of a character in the reversed string
        int lastPrimaryIndex;
        
        //find the first letter
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
             
            if (Character.isLetter(c) == true) {
                index = word.indexOf(c);
                break;
            }
            else {
                continue;
            }
        }
        
        //find the letter but first we must reverse the string 
        String reverse = "";
        for (int i = word.length() - 1; i >= 0; i--) {
            reverse = reverse + word.charAt(i);
        }
        
        //now we can find the last letter
        for (int i = 0; i < word.length(); i ++) {
            char c = reverse.charAt(i);
            
            if (Character.isLetter(c) == true) {
                lastIndex = reverse.indexOf(c);
                //System.out.println(lastIndex);
                break;
            }
            else {
                continue;
            }
        }
        
        lastPrimaryIndex = word.length() - lastIndex;
        
        //the string has trimmed of characters that are not letters
        word = word.substring(index, lastPrimaryIndex);
        
        //this will check if there is something other than a letter in the middle of the string
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            
            if (Character.isLetter(c) == false) {
                return null;
            }
        } 
        
        //now we turn the string into a string of all lowercase letters
        word = word.toLowerCase();
        
        //if the parameter word is contained in the hashtable filled with noise words then --> return null
        if (noiseWords.contains(word) == true) {
        	return null;
        }
        
        return word;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		
		if (occs.size() == 1) {
			return null;
		}
		
		//this will hold all the midpoints
		ArrayList<Integer> midpoints = new ArrayList<Integer>();
		
		//we have to conduct binary search to get all the midpoints, and then put them in the ArrayList
		int left = 0;
		
		//minus two because of the description on line 177
		int right = occs.size() - 2;
		
		//this will be the number to be inserted
		int numToBeInserted = occs.get(occs.size() - 1).frequency;
		
		int middleNumFreq = 0; 
		int middle = 0;
		
		//binary search
		while (left <= right) {
			
			middle = (left + right) / 2;
			midpoints.add(middle);
			
			//this will hold the frequency of the middle number
			middleNumFreq = occs.get(middle).frequency;
			
			//check to see if the numberToBeInserted equals the middle index
			if (middleNumFreq == numToBeInserted) {
				
				//create an occurrence object so that you can store it in the middle index plus one; also remember to remove middleNumFreq at the end of the list after inserting it 
				Occurrence occurrenceObjToBeInserted = occs.get(occs.size() - 1);
				
				//add it to the index one after the middle
				occs.add(middle + 1, occurrenceObjToBeInserted);
				occs.remove(occs.size() - 1);
				break;
			}
			if (middleNumFreq > numToBeInserted) {
				left = middle + 1;
			}
			else {
				right = middle - 1;
			}
		}
		
		//after numToBeInserted has not been found, we add it to the ArrayList
		if (middleNumFreq < numToBeInserted) {
			Occurrence occurrenceObjToBeInserted = occs.get(occs.size() - 1);
			occs.add(middle, occurrenceObjToBeInserted);
			occs.remove(occs.size() - 1);
		}
		else {
			if (middleNumFreq > numToBeInserted) {
				Occurrence occurrenceObjToBeInserted  = occs.get(occs.size() - 1);
				occs.add(middle + 1, occurrenceObjToBeInserted);
				occs.remove(occs.size() - 1);
			}
		}
		
		return midpoints;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, returns null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		
		//this will hold the documents in which either kw1 or kw2 occurs
		ArrayList<String> documents = new ArrayList<String>();
		
		//only five are allowed, we loop until numOfDocuments == five
		int numOfDocuments = documents.size();
		
		//first check if the keyword is in the keywordsIndex
		if (keywordsIndex.containsKey(kw1) == false && keywordsIndex.containsKey(kw2) == false) {
			return null;
		}
		ArrayList<Occurrence> occurrence1 = null;
		ArrayList<Occurrence> occurrence2 = null;
		
		//we need to get the occurrence of kw1 and kw2
		if (keywordsIndex.containsKey(kw1) == true) {
			occurrence1 = keywordsIndex.get(kw1);
		}
		
		if (keywordsIndex.containsKey(kw2) == true) {
			occurrence2 = keywordsIndex.get(kw2);
		}
		
		
		//this will see if one of the arraylists of occurrences is null
		if (occurrence2 == null) {
			for (int i = 0; i < occurrence1.size(); i++) {
				documents.add(occurrence1.get(i).document);
				numOfDocuments++;
				if (numOfDocuments == 5) {
					break;
				}
			}
		}
		else if (occurrence1 == null) {
			for (int j = 0; j < occurrence2.size(); j++) {
				documents.add(occurrence2.get(j).document);
				numOfDocuments++;
				if (numOfDocuments == 5) {
					break;
				}
			}
		}
		else {
			
			int ptr1 = 0;
			int ptr2 = 0;
			
			while (ptr1<occurrence1.size() && ptr2<occurrence2.size()) {
				
				int num1 = occurrence1.get(ptr1).frequency;
				int num2 = occurrence2.get(ptr2).frequency;
				
				if (num1 >= num2) {
					if (documents.contains(occurrence1.get(ptr1).document)) {
						ptr1++;
					}
					else {
						documents.add(occurrence1.get(ptr1).document);
						numOfDocuments++;
						ptr1++;
					}
				}
				else {
					if (documents.contains(occurrence2.get(ptr2).document)) {
						ptr2++;
					}
					else {
						documents.add(occurrence2.get(ptr2).document);
						numOfDocuments++;
						ptr2++;
					}
				}
				if (numOfDocuments == 5) {
					break;
				}
			}
			
			if (numOfDocuments < 5) {
				
				if (ptr1 <occurrence1.size()) {
					while (numOfDocuments < 5 && ptr1 < occurrence1.size()) {
						
						if (documents.contains(occurrence1.get(ptr1).document)) {
							ptr1++;
						}
						else {
							documents.add(occurrence1.get(ptr1).document);
							numOfDocuments++;
							ptr1++;
						}
					}
				}
				else {
					while (numOfDocuments < 5 && ptr2<occurrence2.size()) {
						
						if (documents.contains(occurrence2.get(ptr2).document)) {
							ptr2++;
						}
						else {
							documents.add(occurrence2.get(ptr2).document);
							numOfDocuments++;
							ptr2++;
						}
					}
				}
			}//end of if
		}
		return documents;
	}
}
