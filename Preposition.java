import java.util.ArrayList;

public class Preposition {
	
	static boolean isValidVariable(int letter) {
	    return letter > 96 && letter <= 122 && letter != 118; 
	  }
	  
	  static boolean isValidOperation(int letter) {
		  return letter == 126 || letter == 94 || letter == 118;
	 }
	  
	private ArrayList<String> variables;
    private ArrayList<Character> operations;
 
    Preposition() {
    	this.variables = new ArrayList<>();
    	this.operations = new ArrayList<>();
    }
    
    public void isAcceptable(String preposition) {
    	preposition = preposition.trim().replace(" ", "");
    	
    	int variable = 0;
        int index = 0;
        boolean invalidOperators = false;
        
        while(index < preposition.length() && invalidOperators == false) {
            int letter = (int)preposition.charAt(index);

            if(isValidVariable(letter)) {

              if(index != 0) {
                int previous = (int)preposition.charAt(index - 1 );
                
                if(isValidVariable(previous)) {
                	invalidOperators = true;
                } else {
                	if(previous == 126) {
                		variables.add("~" + (char)letter);
                	} else {
                		variables.add((char)letter + "");
                	}            	
                }
                
              } else {
                variables.add((char)letter + "");
              }
              variable++;

            }
            
            else if(isValidOperation(letter)) {
              if(index == preposition.length() - 1 || (letter != 126 && index == 0)) {
                invalidOperators = true;
              } else{
                if(index != 0) {
                  int previous = (int)preposition.charAt(index - 1 );
                  int after = (int)preposition.charAt(index + 1 );
                  
                  if(letter == 126 && (isValidOperation(after) || isValidVariable(previous))) {
                	  invalidOperators = true;
                  } else if (letter != 126 && ((isValidOperation(previous)))) {
                	  invalidOperators = true;
                  }
                  
                }
                if(letter != 126) {
                	operations.add((char)letter);            	
                }
              }
            } 
            
            else {
              invalidOperators = true;
            }
            index++;
        }

        if(variable > 3) {
          System.out.println("Número de variáveis foi excedido!");
        } else if(invalidOperators) {
          System.out.println("\n\t\tOperação Inválida!");
        } else {
        	variables.forEach(System.out::println);
        	operations.forEach(System.out::println);
        }

    }
}
