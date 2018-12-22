package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer)
	throws IllegalArgumentException {
		
		//Create BigInteger Instance
		BigInteger answer = new BigInteger();
		
		//Remove leading and trailing spaces
		integer = integer.trim();
		
        //Takes care of input if length is zero
        if (integer.length() == 0) {
            throw new IllegalArgumentException("Incorrect Formula");
        }
        
        //This takes care of inputs of length 1 
        if (Character.isDigit(integer.charAt(0)) == false && integer.length() == 1) {
            throw new IllegalArgumentException("Incorrect Formula");
        }
        
		//Make sure the input does not contain letters or symbols 
		for (int i = 0; i < integer.length(); i++) {
            char c = integer.charAt(i);
			if (c != '+' && c != '-' && Character.isDigit(c) == false) {
				throw new IllegalArgumentException("Incorrect Format");
			}
		}
		
		//Takes care of extra plus and minus signs
		int plusCount = 0;
		int minusCount = 0;
		    
		for (int i = 0; i < integer.length(); i++) {
            char c = integer.charAt(i);
            
            if (c == '+') {
                plusCount++;
            }
            else if (c == '-') {
                minusCount++;
            }
		}
		
		if (plusCount > 1 || minusCount > 1 || plusCount > 0 && minusCount > 0) {
		    throw new IllegalArgumentException("Incorrect Format");
		}
		
		//Makes sure that plus or minus sign is at front
		if (integer.indexOf('-') > 0) {
			throw new IllegalArgumentException("Incorrect Format");
		}
		if (integer.indexOf('+') > 0) {
			throw new IllegalArgumentException("Incorrect Format");
		}
		
		//This takes care of all inputs of zeros
        int numOfZeros = 0; 
        
        for (int i = 0; i < integer.length(); i++) {
            char c = integer.charAt(i);
            
            if (c == '0') {
                numOfZeros++;
            }
        }
        if (numOfZeros == integer.length() || numOfZeros == integer.length() -1) {
            int numCount = 0; 
            for (int i = 0; i < integer.length(); i++) {
                char c = integer.charAt(i);
                if (Character.isDigit(c) == true && (c - '0') > 0) {
                    numCount++;
                }
            }
            if (numCount >= 1) {
                
            } else {
                
                integer = "0";
            }
        }
		
		//Takes care of extra zeros
		String digitsAllowed = "123456789";
		int firstDigitIndex = 0; 
		for (int i = 0; i < integer.length(); i++) {
		    char c = integer.charAt(i);
		    
		    if (digitsAllowed.indexOf(c) >= 0) {
		        firstDigitIndex = i;
		        break;
		    }
		}
		String extraZeros = integer.substring(0,firstDigitIndex);
		extraZeros = extraZeros.replaceAll("0","");
		integer = integer.substring(firstDigitIndex);
		
		//Takes care of the remaining plus sign
		if (extraZeros.length() != 0) {
			if (extraZeros.charAt(0) == '-') {
				answer.negative = true;
			}
		}
		
		//Set the number of digits in the integer
		answer.numDigits = integer.length();
		
		
		//Put single digit into a linked list
		int number = 0; 
		if (integer.length() == 1) {
			number = integer.charAt(0) - '0';
			answer.front = new DigitNode(number,answer.front);
			System.out.println(answer.negative + " ff");
			System.out.println(answer.numDigits + " ff");
			return answer;
		}
		
		//Put digit into a linked list
		for (int i = 0; i < integer.length(); i++) {
			number = integer.charAt(i) - '0';
			answer.front = new DigitNode(number,answer.front);
		}
		System.out.println(answer.negative + " gg");
		System.out.println(answer.numDigits + " gg");
		return answer;
	}
	
	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger first, BigInteger second) {
		
		//Create BigInteger Instance
		BigInteger answer = new BigInteger();
		
		//Create a new BigInteger Instance, these will be pointers
		BigInteger pointerFirst = first; 
		BigInteger pointerSecond = second;
		first.print("First: ");
		second.print("Second: ");
		
		//We are going to add the integers if they have the same sign (+,+ and -,-)
		if (first.negative == second.negative) {
			
			//First check to see if negative is true or false, then set appropriately
			if (first.negative == true ) {
				answer.negative = true;
			}
			
			//Match the number of digits 
			if (first.numDigits < second.numDigits) {
				while (first.numDigits != second.numDigits) {
					pointerFirst.addToEnd(0);
					pointerFirst.numDigits++;
				}
			}
			else {
				while (first.numDigits != second.numDigits) {
					pointerSecond.addToEnd(0);
					pointerSecond.numDigits++;
				}
			}
			
			//If number of digits is the same then we add
			if (first.numDigits == second.numDigits) {
			
				int sumOfDigits = 0; 
				int carry = 0;
				
				//Adding begins here
				while (pointerFirst.front != null && pointerSecond.front != null) {
					sumOfDigits = pointerFirst.front.digit + pointerSecond.front.digit + carry;
					
					if (sumOfDigits > 9) {
						carry = 1;
						sumOfDigits = sumOfDigits % 10;
						
						//when first linking a node to front of the answer instances
						if (answer.front == null) {
							answer.front = new DigitNode(sumOfDigits, null);
						} 
						else { //front from the answer instance is already pointing to a node
							answer.addToEnd(sumOfDigits);
						}
					}
					else {
						carry = 0;
						
						//when first linking a node to front of the answer instances
						if (answer.front == null) {
							answer.front = new DigitNode(sumOfDigits, null);
						}
						else { //front from the answer instance is already pointing to a node
							answer.addToEnd(sumOfDigits);
						}
					}
					//allows to traverse through the linked list 
					pointerFirst.front = pointerFirst.front.next;
					pointerSecond.front = pointerSecond.front.next;
				}
				if (carry == 1) {
					answer.addToEnd(1);
				}
			}
				
		}//end of on line 216
		
		//add when one number is negative 
		if (first.negative != second.negative) {			
			
			//match the numbers to same length
			if (first.numDigits < second.numDigits) {
				while (first.numDigits != second.numDigits) {
					pointerFirst.addToEnd(0);
					pointerFirst.numDigits++;
				}
			}
			else {
				while (first.numDigits != second.numDigits) {
					pointerSecond.addToEnd(0);
					pointerSecond.numDigits++;
				}
			}
			
			//find out which number is greater
			if (pointerFirst.greater(pointerSecond) == true) {
				
				//check to see if pointerFirst is negative or not, if so, apply changes
				if (pointerFirst.negative == true) {
					answer.negative = true;
				}
				
				//subtract appropriately
				int sumOfDigits = 0;
				
				while (pointerFirst.front != null && pointerSecond.front != null) {
					sumOfDigits = pointerFirst.front.digit - pointerSecond.front.digit;
					
					if (sumOfDigits < 0) { 
						pointerFirst.front.digit = pointerFirst.front.digit + 10; 
						sumOfDigits = pointerFirst.front.digit - pointerSecond.front.digit;
						pointerFirst.front.next.digit = pointerFirst.front.next.digit - 1;
						
						//when first linking a node to front of the answer instances
						if (answer.front == null) {
							answer.front = new DigitNode(sumOfDigits, null);
						}
						else { //front from the answer instance is already pointing to a node
							answer.addToEnd(sumOfDigits);
						}
					}
					else {
						if (answer.front == null) {
							answer.front = new DigitNode(sumOfDigits, null);
						}
						else { //front from the answer instance is already is already pointing to a node
							answer.addToEnd(sumOfDigits);
						}
					}
					
					//allows to traverse through the linked list
					pointerFirst.front = pointerFirst.front.next;
					pointerSecond.front = pointerSecond.front.next;
					
				}//end while loop
			}
			else {//second is bigger 
				
				if (pointerSecond.negative == true) {
					answer.negative = true; 
				}
				
				//subtract appropriately
				int sumOfDigits = 0;
				
				while (pointerSecond.front != null && pointerFirst != null) {
					sumOfDigits = pointerSecond.front.digit - pointerFirst.front.digit;
					
					if (sumOfDigits < 0) {
						pointerSecond.front.digit = pointerSecond.front.digit + 10; 
						sumOfDigits = pointerSecond.front.digit - pointerFirst.front.digit; 
						pointerSecond.front.next.digit = pointerSecond.front.next.digit - 1; 
						
						//when first linking a node to front of the answer instances
						if (answer.front == null) {
							answer.front = new DigitNode(sumOfDigits, null);
						}
						else { //front from the answer instance is already pointing to a node
							answer.addToEnd(sumOfDigits);
						}
					}
					else {
						if (answer.front == null) {
							answer.front = new DigitNode(sumOfDigits, null);
						}
						else { //front from the answer instance is already is already pointing to a node
							answer.addToEnd(sumOfDigits);
						}
					}
					
					//allows to traverse through the linked list
					pointerSecond.front = pointerSecond.front.next;
					pointerFirst.front = pointerFirst.front.next;
					
				}//end while loop
			}
		}
	
		//check if there are zeros at the end of the linked list
		if (answer.checkZeros(answer.front) == true) { 
			answer.reverseLL(); 
			answer.reverseAfterModified();
		}
		
		
		//set numDigits to length of answer
		answer.fillNumOfDigits();
		System.out.println(answer.negative);
		System.out.println("num: " + answer.numDigits);
		return answer; 
	}
	
	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	public static BigInteger multiply(BigInteger first, BigInteger second) {
		
		//Create BigInteger Instance
		BigInteger answer = new BigInteger();
		
		////Create a new BigInteger Instance, these will be pointers
		BigInteger pointerFirst = new BigInteger(); 
		BigInteger pointerSecond = new BigInteger();
		
		pointerFirst.addToFront(first.front.digit, first.front.next);
		pointerSecond.addToFront(second.front.digit, second.front.next);
		
		pointerFirst.numDigits = first.numDigits;
		pointerSecond.numDigits = second.numDigits;
		
		
		//Create a new BigInteger Instance to hold current
		BigInteger current = new BigInteger();
		
		//Create a new BigInteger Instance to hold total
		BigInteger total = new BigInteger();
		total.front = new DigitNode(0, null);
		
		//We are going to multiply the integers if they have the same sign (+,+ and -,-)
		if (first.negative == second.negative) {
			
			//First check to see if negative is true or false, then set appropriately
			if (first.negative == true) {
				answer.negative = true;
			}
			
			//Match the numbers to same length
			if (first.numDigits < second.numDigits) {
				while (pointerFirst.numDigits != pointerSecond.numDigits) {
					pointerFirst.addToEnd(0);
					pointerFirst.numDigits++;
				}
			}
			else {
				while (pointerFirst.numDigits != pointerSecond.numDigits) {
					pointerSecond.addToEnd(0);
					pointerSecond.numDigits++;
				}
			}
			
			//If number of digits is the same then we multiply
			if (pointerFirst.numDigits == pointerSecond.numDigits) {
				int sumOfDigits = 0;
				int carry = 0;
				int numOfTimesWeMult = 0;
				
				while (pointerSecond.front != null) {
					
					
					
					
					pointerFirst.addToFront(first.front.digit, first.front.next);
					
					//calculates one round
					while (pointerFirst.front != null) {

						
						
						sumOfDigits = (pointerFirst.front.digit * pointerSecond.front.digit) + carry;
						
						
						if (sumOfDigits > 9) { 
							carry = sumOfDigits / 10; 
							sumOfDigits = sumOfDigits % 10; 
							
							if (current.front == null) {
								current.front = new DigitNode(sumOfDigits, null);
								current.numDigits++;
							}
							else {
								current.addToEnd(sumOfDigits);
								current.numDigits++;
							}
						}
						else {
							carry = 0;
							
							//when first linking a node to front of the answer instances
							if (current.front == null) {
								current.front = new DigitNode(sumOfDigits, null); 
								current.numDigits++;
							}
							else { //front from the answer instance is already pointing to a node
								current.addToEnd(sumOfDigits); 
								current.numDigits++;
							}
						}
						
						//allows to traverse through the linked list
						pointerFirst.front = pointerFirst.front.next;
						
					}//end while loop
					
					if (carry < 10 && carry > 0) {
						current.addToEnd(carry); 
						carry = 0;
						current.numDigits++;
					}
					
					
					
					total = BigInteger.add(total,current); //doesnt work here
					
				
					
					numOfTimesWeMult++;
					
					current.numDigits = 0;
					
					//check how many times we multiplied
					for (int i = 0; i < numOfTimesWeMult; i++) {
						if ( i == 0) {
							current.front = new DigitNode(0, null);
							current.numDigits++;
						}
						else {
							current.addToEnd(0);
							current.numDigits++;
						}
					}
					
					
					pointerSecond.front = pointerSecond.front.next;
					
					if(pointerSecond.front != null) {
					
					
					
					}
					
				}//end while loop
			}
		}
		
		//We are going to multiply the integers if they have the same sign (+,+ and -,-)
				if (first.negative != second.negative) {
					
					answer.negative = true;
					
					//Match the numbers to same length
					if (first.numDigits < second.numDigits) {
						while (pointerFirst.numDigits != pointerSecond.numDigits) {
							pointerFirst.addToEnd(0);
							pointerFirst.numDigits++;
						}
					}
					else {
						while (pointerFirst.numDigits != pointerSecond.numDigits) {
							pointerSecond.addToEnd(0);
							pointerSecond.numDigits++;
						}
					}
					
					//If number of digits is the same then we multiply
					if (pointerFirst.numDigits == pointerSecond.numDigits) {
						int sumOfDigits = 0;
						int carry = 0;
						int numOfTimesWeMult = 0;
						
						while (pointerSecond.front != null) {
							
							
							current.print("Starting Current: ");
							
							pointerFirst.addToFront(first.front.digit, first.front.next);
							
							//calculates one round
							while (pointerFirst.front != null) {

								
								
								sumOfDigits = (pointerFirst.front.digit * pointerSecond.front.digit) + carry;
								
								
								if (sumOfDigits > 9) { 
									carry = sumOfDigits / 10; 
									sumOfDigits = sumOfDigits % 10; 
									
									if (current.front == null) {
										current.front = new DigitNode(sumOfDigits, null);
										current.numDigits++;
									}
									else {
										current.addToEnd(sumOfDigits);
										current.numDigits++;
									}
								}
								else {
									carry = 0;
									
									//when first linking a node to front of the answer instances
									if (current.front == null) {
										current.front = new DigitNode(sumOfDigits, null); 
										current.numDigits++;
									}
									else { //front from the answer instance is already pointing to a node
										current.addToEnd(sumOfDigits); 
										current.numDigits++;
									}
								}
								
								//allows to traverse through the linked list
								pointerFirst.front = pointerFirst.front.next;
								
							}//end while loop
							
							if (carry < 10 && carry > 0) {
								current.addToEnd(carry); 
								carry = 0;
								current.numDigits++;
							}
							
						
							
							total = BigInteger.add(total,current); //doesnt work here
							
							
							
							numOfTimesWeMult++;
							
							current.numDigits = 0;
							
							//check how many times we multiplied
							for (int i = 0; i < numOfTimesWeMult; i++) {
								if ( i == 0) {
									current.front = new DigitNode(0, null);
									current.numDigits++;
								}
								else {
									current.addToEnd(0);
									current.numDigits++;
								}
							}
							
							pointerSecond.front = pointerSecond.front.next;
							
							if(pointerSecond.front != null) {
							
							}
							
						}//end while loop
					}
				}
		
		System.out.println(answer.negative + " gg");
		System.out.println(answer.numDigits + " gg");
		answer = total;
		return answer; 
		
	}
	
	//this method will set answer to the correct numOfDigits
	private void fillNumOfDigits() {
		DigitNode ptr = front;
		
		while (ptr != null) {
			
			numDigits++;
			ptr = ptr.next;
		}
	}
	
	//This method will check if there are zeros at the end of the linked list
	private boolean checkZeros(DigitNode node) {
		DigitNode ptr = node; 
		DigitNode prev = null;
		
		while (ptr != null) {
			prev = ptr;
			ptr = ptr.next;
		}
		if (prev.digit == 0) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	//this method will reverse and take out zeros. ex; 3>2>0>0>1>0>0 --> 1>0>0>2>3
	private void reverseLL() {
		DigitNode ptr = front;
		DigitNode copy = front;
		DigitNode reverseLL = null;
		
		//reverse LinkedList
		while (ptr != null) {
			reverseLL = new DigitNode(ptr.digit, reverseLL);
			ptr = ptr.next;
		}
		
		//trim the zeros out 
		while (copy != null) {
			if (reverseLL.digit == 0) {
				reverseLL = reverseLL.next;
			}
			copy = copy.next;
		}
		front = reverseLL;
	}
	
	//this method reverses the linked list after reverse ex: 3>2>0>0>1
	private void reverseAfterModified() {
		DigitNode ptr = front;
		DigitNode reversedLL = null;
				
		while(ptr != null) {
			reversedLL = new DigitNode(ptr.digit, reversedLL);
			ptr = ptr.next;
		}
		
		front = reversedLL;
	}
	
	//This method will see which integer is greater
	private boolean greater(BigInteger other) {
		DigitNode ptr = front;
		DigitNode ptr2 = other.front;
		boolean largest = false;
		
		while (ptr != null && ptr2 != null) {
			if (ptr.digit == ptr2.digit) {
				ptr = ptr.next;
				ptr2 = ptr2.next;
				continue;
			}
			if (ptr.digit > ptr2.digit) {
				largest = true;
			}
			
			else {
				largest = false;
			}
			ptr = ptr.next;
			ptr2 = ptr2.next;
		}
		return largest; 
	}
	
	private void addToFront(int data, DigitNode link)
	{
		front = new DigitNode(data, link);
	}
	
	//This method will make the numDigits between both numbers equal
	private void addToEnd(int data) {
		DigitNode ptr = front;
		DigitNode prev = null;
		
			while(ptr != null) {
				prev = ptr;
				ptr = ptr.next;
			}
			prev.next = new DigitNode(data, null);
		
	}
	
	//This method will print out the entries in the array
	private void print(String s) {
		DigitNode ptr = front;
		
		System.out.print(s);
		
		while (ptr != null) {
			System.out.println(ptr.digit);
			ptr = ptr.next;
		}
		System.out.println();
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
	
}
