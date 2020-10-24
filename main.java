import java.util.Scanner;

class Main {

  public static void main(String[] args) {
    String prep;
    Scanner keyboard = new Scanner(System.in);
    
    System.out.println("\n\t\t\t\tMENU");
    System.out.println("Regras:\n\n"
    		+ "-> Máx de 3 variáveis\n"
    		+ "-> Só serão aceitas as operações v,^,~\n"
    		+ "-> Não será aceita a letra 'v' como variável\n"
    		+ "-> As operações v e ^ deverão ser colocadas entre variáveis!\n");
    
    System.out.println("Digite a preposição(max 3 váriaveis):");
    prep = keyboard.nextLine();
    
    Preposition preposition = new Preposition();
    
    preposition.isAcceptable(prep);
    
    keyboard.close();
  }
}