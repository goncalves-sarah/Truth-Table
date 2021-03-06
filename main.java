import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

class Main {

    public static void main(String[] args) {
        String prep;
        Scanner keyboard = new Scanner(System.in);
        while(true) {
            System.out.println("\n\t\t\t\tMENU");
            System.out.println("Regras:\n\n"
                    + "-> Máximo de 3 variáveis\n"
                    + "-> Só serão aceitas as operações v (OR),^ (AND) e ~ (NOT)\n"
                    + "-> Não será aceita a letra 'v' como variável\n"
                    + "-> As operações de OR e AND deverão ser colocadas entre variáveis!\n"
                    + "-> Quando negar uma variável, lembre de seguir o modelo (~p)\n"
                    + "-> Digite 'bye bye' para sair do programa!\n");

            System.out.println("Digite a preposição (no máximo 3 váriáveis): ");
            prep = keyboard.nextLine();

            if(prep.equalsIgnoreCase("bye bye")){
                keyboard.close();
                System.out.println("Bye bye pra você também ;)");
                System.exit(0);
            }

            Preposition preposition = new Preposition();

            if (preposition.isAcceptable(prep)) {
                ArrayList<String> variables = preposition.getVariables();
                ArrayList<Character> operations = preposition.getOperations();
                ArrayList<String> distinctVariables = preposition.getDistinctVariables();

                //variables.forEach(System.out::println);
                //operations.forEach(System.out::println);
                //distinctVariables.forEach(System.out::println);
                //System.out.println(preposition.getHowManyDistinctVariables());

                Calculator cal = new Calculator();
                cal.run(variables, operations, distinctVariables, prep);


            }

            System.out.print("\nPressione <ENTER> para continuar.");
            keyboard.nextLine();
            System.out.println();
        }
    }
}