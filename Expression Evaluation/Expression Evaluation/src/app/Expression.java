package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	
    	//takes care of all the spaces
    	expr = expr.replaceAll(" ", ""); 
    	
    	//We are going to separate the string using the stringtokenizer
    	StringTokenizer str = new StringTokenizer(expr, delims, true);
    	
    	//Create a Stack to hold variables 
    	Stack<String> wordHolder = new Stack<String>();
    	
    	//Create variable to hold current token
    	String token = null;
    	
    	//loop through tokens
    	while(str.hasMoreTokens()) {
    		token = str.nextToken();
    		System.out.println(token);
    		
    		//add token to stack if it is a letter or a open bracket
    		if (Character.isLetter(token.charAt(0)) || token.charAt(0) == '[')  {
    			wordHolder.push(token);
    		}	
    	}
    	
    	//This will hold the string that we pop
    	String popHolder = null;
    	
    	//This will pop out the stack to see if the string popped is a variable or an array
    	while (wordHolder.isEmpty() != true) {
    		popHolder = wordHolder.pop();
    		
    		System.out.print("Current popped character:");
    		System.out.println(popHolder);
    		
    		//.pop takes one away from size
    		if (popHolder.equals("[")) { //you could have done this "if (!(wordHolder.pop().equals("[")))" to see if string did not equal [ 
    			Array myArr = new Array(wordHolder.pop()); //arraylist holds same myArr?
    			
    			if (arrays.contains(myArr) == true) { //check to see if this variable is already in the arraylist called arrays
    				continue;
    			}
    			else {
    				arrays.add(myArr); //add to the arraylist called arrays an object of Array called myArr. Passing in reference
    			}
    		}
    		else {
    			
    			Variable myVar = new Variable(popHolder);
    			
    			if (vars.contains(myVar) == true) {
    				continue;
    			}
    			else {
    				vars.add(myVar);
    			}
    		}
    	}
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar currentSymbol
                vars.get(vari).value = num;
            } else { // array currentSymbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	
    	//FYI: remember that when you first tried to set your string variables to null, the result gave you a null pointer exception
    	
    	//This takes out all of the spaces in the expression 
    	expr = expr.replaceAll(" ", ""); 		
    	
    	//Set answer to 0
    	float answer = 0;
    	
    	//Turns expression into a stringtokenizer
    	StringTokenizer wordHolder = new StringTokenizer(expr, delims, true);
    	
    	//Points to current token
    	String currentToken = "";
    	
    	//This holds the current symbol and the temporary symbol Example: +, -, *, /
    	char operation = '\0';		//ask what this does?  why didnt it work when you did ' '
    	Character tempOperation = '\0';
    	
    	//This is used to make sure operations is an operation symbol
    	String validOperations = "+-*/";
    	
    	//Holds one of the numbers that will be worked on. Example 1: 
    	String num = ""; 											//change variables
    	String num2 = "";
    	float number = 0; //This holds the index number
    	String tempNumber = ""; //This will hold the number that will be worked on last 
    	
    	//This will account for any type of subexpression inside the brackets recursively 
  	  	while (wordHolder.hasMoreTokens()) {
  	  		
  	  		//This will hold the next token
		  	currentToken = wordHolder.nextToken();
		  	
		  	System.out.print("Current token being looked at: ");
		  	System.out.println(currentToken);
		  	
		  	if(currentToken.charAt(0) == '[') {
		  		
		  		//We are calling in our private method to get the subexpression when we encounter an open bracket
		  		number = evaluateSubExp('[', ']', wordHolder, vars, arrays);
		  		
		  		//Since num2 equals an empty string than we can 
		  		if (num2.equals("")) {
		  			for(int i = 0; i < arrays.size(); i++){
		  				if (arrays.get(i).name.equals(num)) {
		  					num = arrays.get(i).values[(int)number] + "";
		  					break;
		  				}
		  			}
		  		}
		  		else {
		  			for (int i = 0; i < arrays.size(); i++) {
		  				if (arrays.get(i).name.equals(num2)) {
		  					num2 = arrays.get(i).values[(int)number] + "";
		  					break;
		  				}
		  			}
		  		}
		  	} //end of if one line 178
		  	else if (currentToken.charAt(0) == '(') {
		  		
		  		//We are calling in our private method to get the subexpression when we encounter an open parenthesis
		  		if (num.equals("")) {
		  			num = evaluateSubExp('(', ')', wordHolder, vars, arrays) + "";
		  		}
		  		else {
		  			num2 = evaluateSubExp('(', ')', wordHolder, vars, arrays) + "";
		  		}
		  	} //end of else if on line 220
		  	else if (Character.isLetter(currentToken.charAt(0)) == true) { //^^^^
		  		if (num.equals("")) {
		  			num = currentToken;
		  		}
		  		else {
		  			num2 = currentToken;
		  		}
		  	}															  //^^^^
		  	else if (validOperations.indexOf(currentToken.charAt(0)) >= 0) {
		  		
		  		if (operation == '\0'){ 
		  			operation = currentToken.charAt(0);
		  		}
		  		else{																				//@@@@@
		  			
		  			//This changes variable into a number string
		  			if (Character.isLetter(num.charAt(0)) == true ) {
		  				num = variableToNumber(num, vars);
		  			}
		  			if (Character.isLetter(num2.charAt(0)) == true) {
		  				num2 = variableToNumber(num2, vars);
		  			}
		  			
		  			//This occurs when we are looking at next symbol
		  			if (operation == '+' || operation == '-') { //5 * 3 - 2 operation would be multiply and minus would be current
		  				if (currentToken.charAt(0) == '*' || currentToken.charAt(0) == '/') {
		  					tempOperation = operation; //plus or minus would turn into temp
		  					operation = currentToken.charAt(0);
		  					tempNumber = num;
		  					num = num2;
		  					num2 = "";
		  				}
		  				else { //current operation is not multiply or divide 
		  					if (operation == '+' || operation == '-') {
		  						answer = calculate(num, num2, operation);
		  						num = answer + "";
		  						num2 = "";
		  						operation = currentToken.charAt(0);
		  					}
		  				}
		  			}
		  			else { 									   //****
		  				
		  				//this takes care if the operation variable is a multiply or divide character
		  				if (validOperations.indexOf(operation) >= 2) {
		  					answer = calculate(num, num2, operation);
		  					num = answer + ""; //Now the answer is moved to be in num
		  					num2 = "";
		  					operation = currentToken.charAt(0);
		  				}
		  				
		  				if (tempOperation.equals('\0') == false) { //cant you say if it equals + or minus, charAt? No, we had to set tempOperation to a Character wrapper class
		  					if (operation == '+' || operation == '-') {
		  						
		  						//Remember that we are accounting for the tempOperation by passing it in (third parameter)
		  						answer = calculate(tempNumber, num, tempOperation);
		  					}
		  				}	
		  			} 											//****
		  		}																						//@@@@@
		  	}
		  	else {
		  		if (num.equals("")) {
		  			num = currentToken;
		  		}
		  		else {
		  			num2 = currentToken;
		  		}
		  	}
  	 }//end of while loop
  	
  	 
  	//Final part of code that will return the answer
  	if (Character.isLetter(num.charAt(0)) == true) {
  		num = variableToNumber(num, vars);
  	}
  	
	if (num2.equals("") == false && Character.isLetter(num2.charAt(0)) == true) {
		num = variableToNumber(num, vars);
	}
  	  
  	if (num2.equals("") == false) {
  		System.out.println(num + " " + num2);
  		answer = calculate(num, num2, operation);
  		num = answer + "";
  	}
  	
  	//This makes sure we take care of the tempOperation
  	if (tempOperation.equals('+') || tempOperation.equals('-')) {
			answer = calculate(tempNumber, num, tempOperation);
			num = answer + "";
			tempOperation = '\0';
			tempNumber = "";
	}
  	
  	//This returns answer
    answer = Float.valueOf(num);
   	return answer;
    }
    
//----------Private Helper Methods----------//
    
    //This handles both brackets and parenthesis
    private static float evaluateSubExp(char bracketOrParent, char brackOrParenthesis2, StringTokenizer wordHolderParam, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	
    	//This will hold the subexpression
    	String subExpression = "";
    	
    	//Keeps track of brackets and parenthesis
    	int count = 0;
    	
    	//This holds the current token
  		String currentToken = wordHolderParam.nextToken();
  		
  		if (currentToken.equals(bracketOrParent + "") ) {
  			count++;
  		}
  		
  		while (currentToken.charAt(0) != brackOrParenthesis2 && wordHolderParam.hasMoreTokens()) {
  			
  			subExpression = subExpression + currentToken;
  			currentToken = wordHolderParam.nextToken();
  			
  			//This will count the number of open brackets to get to the inner most subexpression
  			if (currentToken.charAt(0) == bracketOrParent) {
  				count++;
  			}
  			else if (currentToken.charAt(0) == brackOrParenthesis2 && count != 0) {
  	 			while (currentToken.charAt(0) == brackOrParenthesis2 && count != 0) {
  	  				subExpression = subExpression + currentToken;
  	  				currentToken = wordHolderParam.nextToken();
  	  				count--;
  	  			}	
  	  		}
  		}
  		//This will recursively take in a subExpression and get the expression in between the brackets
  		return evaluate(subExpression, vars, arrays);
  	}
    
    //This will turn the variables into a number string
    private static String variableToNumber(String variableName, ArrayList<Variable> vars) {
   			
    	for (int i = 0; i < vars.size(); i++){
					if (vars.get(i).name.equals(variableName)) {
						return vars.get(i).value + "";  					
	  			}
	  		}
		return null;
    }
    
    //This method does the mathematical operation based on the operation
    private static float calculate(String number1, String num2, char oper) {
    	if (oper == '\0') {
    		return 0;
    	}
    	else if (oper == '+') {
    		return Float.parseFloat(number1) + Float.parseFloat(num2);
    	}
    	else if (oper == '-') {
    		return Float.parseFloat(number1) - Float.parseFloat(num2);
    	}
    	else if (oper == '*') {
    		return Float.parseFloat(number1) * Float.parseFloat(num2);
    	}
    	else if (oper == '/') {
    		return Float.parseFloat(number1) / Float.parseFloat(num2);
    	}
    	return 0;
    }
}
