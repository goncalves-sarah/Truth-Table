
import java.security.InvalidParameterException;
import java.util.*;


public class Calculator {
    /**
     * {@code HashMap} que armazena a tabela-verdade gerada
     */
    private Map<String, ArrayList<Character>> table;

    /**
     * Constrói um novo {@code Calculator} com um HashMap() vazio.
     */
    public Calculator() {
        this.table = new HashMap<>();
    }



    /**
     * @return um {@code HashMap} equivalente a tabela verdade sendo criada no código
     */
    public Map<String, ArrayList<Character>> getTable() {
        return table;
    }


    /**
     * Este método converte o valor de uma variável para um valor inteiro
     * dependendo da sua fórmula e valor atual.
     * @param var a variável sendo convertida
     * @param value o valor atual (true ou false)
     * @return um inteiro correspondente a conversão, 1 para verdadeiro e 0 para falso.
     */
    public int toInt(String var, Character value){
        if(var.startsWith("~"))
            return value.compareTo('V') == 0 ? 0 : 1;

        else
            return value.compareTo('V') == 0 ? 1 : 0;
    }



    /**
     * Este método calcula o resultado de uma operação entre duas variáveis.
     * @param var1 a primeira variável da equação
     * @param value1 o valor da primeira varíavel
     * @param op a operação sendo efetuada
     * @param var2 a segunda variável da equação
     * @param value2 o valor da segunda variável
     * @return o resultado da operação
     */
    public Character calculate(String var1, Character value1, Character op, String var2, Character value2){
        int v1 = toInt(var1, value1);
        int v2 = toInt(var2, value2);
        int result = -1;

        if(op == '^')
            result = v1 * v2;

        else if(op == 'v')
            result = v1 + v2;

        else
            throw new InvalidParameterException("Use of invalid operator!");

        return result > 0 ? 'V' : 'F';
    }



    /**
     * Remove uma parte de um {@code ArrayList}.
     * @param list o ArrayList
     * @param start a posição inicial (inclusiva)
     * @param end a posição final (exclusiva)
     * @return o ArrayList editado
     */
    public ArrayList<Character> removeInRange(ArrayList<Character> list, int start, int end) {
        if (end > start) {
            list.subList(start, end).clear();
        }
        return list;
    }



    /**
     * Constrói a tabela verdade baseado nas variáveis e operações de entrada
     * na equação original.
     * @param variables ArrayList das varíaveis
     * @param operations ArrayList das operações
     * @param distinctVariables ArrayList das variáveis únicas
     * @param formula fórmula original
     */
    public void run(ArrayList<String> variables, ArrayList<Character> operations, ArrayList<String> distinctVariables, String formula){
        ArrayList<Character> first  = new ArrayList<>(List.of('V', 'F', 'V', 'F', 'V', 'F', 'V', 'F'));
        ArrayList<Character> second = new ArrayList<>(List.of('V', 'V', 'F', 'F', 'V', 'V', 'F', 'F'));
        ArrayList<Character> third  = new ArrayList<>(List.of('V', 'V', 'V', 'V', 'F', 'F', 'F', 'F'));
        ArrayList<Character> result = new ArrayList<>();
        int size = distinctVariables.size();

        if(size == 1) {
            first = removeInRange(first, 2, 8);
            second.clear();
            third.clear();
            table.put(distinctVariables.get(0), first);
        }

        else if(size == 2){
            first = removeInRange(first, 4, 8);
            second = removeInRange(second, 4, 8);
            third.clear();
            table.put(distinctVariables.get(1), first);
            table.put(distinctVariables.get(0), second);
        }
        else{
            table.put(distinctVariables.get(2), first);
            table.put(distinctVariables.get(1), second);
            table.put(distinctVariables.get(0), third);
        }



        if(variables.size() == 1){
            String v = variables.get(0);
            for (Character c: first)
                result.add(calculate(v, c, '^', v, c));
        }

        else if(variables.size() == 2){
            Iterator<Character> f = first.iterator();
            Iterator<Character> s = second.iterator();
            String v = variables.get(0);
            String v2 = variables.get(1);

            if(size == 1) {
                for (Character c: first)
                    result.add(calculate(v, c, operations.get(0), v2, c));
            }
            else{
                while (f.hasNext() && s.hasNext())
                    result.add(calculate(v, s.next(), operations.get(0), v2, f.next()));
            }
        }

        else{
            Iterator<Character> f = first.iterator();
            Iterator<Character> s = second.iterator();
            Iterator<Character> t = third.iterator();
            String v = variables.get(0);
            String v2 = variables.get(1);
            String v3 = variables.get(2);
            Character cal1;

            if(size == 1) {
                for (Character c: first) {
                    cal1 = calculate(v, c, operations.get(0), v2, c);
                    result.add(calculate(" ", cal1, operations.get(1), v3, c));
                }
            }
            //==========================================
            if(size == 2) {
                if(variables.get(1).contains(variables.get(0)) || variables.get(0).contains(variables.get(1))){
                    while (f.hasNext() && s.hasNext()) {
                        Character temp = s.next();
                        cal1 = calculate(v, temp, operations.get(0), v2, temp);
                        result.add(calculate(" ", cal1, operations.get(1), v3, f.next()));
                    }
                }
                else if(variables.get(2).contains(variables.get(0)) || variables.get(0).contains(variables.get(2))){
                    while (f.hasNext() && s.hasNext()) {
                        Character temp = s.next();
                        cal1 = calculate(v, temp, operations.get(0), v2, f.next());
                        result.add(calculate(" ", cal1, operations.get(1), v3, temp));
                    }
                }
                else{
                    while (f.hasNext() && s.hasNext()) {
                        Character temp = f.next();
                        cal1 = calculate(v, s.next(), operations.get(0), v2, temp);
                        result.add(calculate(" ", cal1, operations.get(1), v3, temp));
                    }
                }
            }
            //==========================================
            else {
                while (f.hasNext() && s.hasNext() && t.hasNext()) {
                    cal1 = calculate(v, t.next(), operations.get(0), v2, s.next());
                    result.add(calculate("", cal1, operations.get(1), v3, f.next()));
                }
            }
        }

        table.put(formula, result);

        /**
        *Realiza a impressão dos resultados da tabela verdade, de acordo com tamanho de fórmula e quantidade de variáveis
         */



        if (distinctVariables.size() == 1){

            ArrayList<Character> firstnegado  = new ArrayList<>(List.of('F', 'V'));
            char[] var0 = variables.get(0).toCharArray();

            if (formula.length() == 1){
                System.out.println("_________");
                System.out.println("|_" + variables.get(0) + "_|_" + formula + "_|");
                System.out.println("|_" + first.get(0) + "_|_" + result.get(0) + "_|");
                System.out.println("|_" + first.get(1) + "_|_" + result.get(1) + "_|");
            }
            else if (formula.length() == 2){
                System.out.println("_______________");
                System.out.println("|_" + variables.get(0) + "_|_" + var0[1] + "_|_" + formula + "_|");
                System.out.println("|__" + firstnegado.get(0) + "_|_" + first.get(0) + "_|__" + result.get(0) + "_|");
                System.out.println("|__" + firstnegado.get(1) + "_|_" + first.get(1) + "_|__" + result.get(1) + "_|");
            }
        }
        else if (distinctVariables.size() == 2){

            ArrayList<Character> firstnegado  = new ArrayList<>(List.of('F', 'V', 'F', 'V'));
            ArrayList<Character> secondnegado = new ArrayList<>(List.of('F', 'F', 'V', 'V'));

            char[] var0 = variables.get(0).toCharArray();
            char[] var1 = variables.get(1).toCharArray();

            if (variables.get(1).length() == 1 && variables.get(0).length() == 1) {
                if (formula.length() == 5){
                    System.out.println("_________________");
                    System.out.println("|_" + variables.get(1) + "_|_" + variables.get(0) + "_|_" + formula + "_|");
                    for (int i = 0; i < 4; i++) {
                        System.out.println("|_" + first.get(i) + "_|_" + second.get(i) + "_|___" + result.get(i) + "___|");
                    }
                }
                else if (formula.length() == 3){
                    System.out.println("_______________");
                    System.out.println("|_" + variables.get(1) + "_|_" + variables.get(0) + "_|_" + formula + "_|");
                    for (int i = 0; i < 4; i++) {
                        System.out.println("|_" + first.get(i) + "_|_" + second.get(i) + "_|__" + result.get(i) + "__|");
                    }
                }
            }
            else if (variables.get(1).length() == 2 && variables.get(0).length() == 1){
                if (formula.length() == 6){
                    System.out.println("_______________________");
                    System.out.println("|_" + variables.get(1) + "_|_" + var1[1] + "_|_" + variables.get(0) + "_|_" + formula + "_|");
                    for (int i = 0; i < 4; i++) {
                        System.out.println("|__" + firstnegado.get(i) + "_|_" + first.get(i) + "_|_" + second.get(i) + "_|____" + result.get(i) + "___|");
                    }
                }
                else if (formula.length() == 4){
                    System.out.println("_____________________");
                    System.out.println("|_" + variables.get(1) + "_|_" + var1[1] + "_|_" + variables.get(0) + "_|_" + formula + "_|");
                    for (int i = 0; i < 4; i++) {
                        System.out.println("|__" + firstnegado.get(i) + "_|_" + first.get(i) + "_|_" + second.get(i) + "_|___" + result.get(i) + "__|");
                    }
                }
            }
            else if (variables.get(1).length() == 1 && variables.get(0).length() == 2){
                if (formula.length() == 6){
                    System.out.println("_______________________");
                    System.out.println("|_" + variables.get(1) + "_|_" + variables.get(0) + "_|_" + var0[1] + "_|_" + formula + "_|");
                    for (int i = 0; i < 4; i++) {
                        System.out.println("|_" + first.get(i) + "_|__" + secondnegado.get(i) + "_|_" + second.get(i) + "_|____" + result.get(i) + "___|");
                    }
                }
                else if (formula.length() == 4){
                    System.out.println("_____________________");
                    System.out.println("|_" + variables.get(1) + "_|_" + variables.get(0) + "_|_" + var0[1] + "_|_" + formula + "_|");
                    for (int i = 0; i < 4; i++) {
                        System.out.println("|_" + first.get(i) + "_|__" + secondnegado.get(i) + "_|_" + second.get(i) + "_|___" + result.get(i) + "__|");
                    }
                }
            }
            else if (variables.get(1).length() == 2 && variables.get(0).length() == 2){
                if (formula.length() == 7){
                    System.out.println("_____________________________");
                    System.out.println("|_" + variables.get(1) + "_|_" + var1[1] + "_|_" + variables.get(0) + "_|_" + var0[1] + "_|_" + formula + "_|");
                    for (int i = 0; i < 4; i++) {
                        System.out.println("|__" + firstnegado.get(i) + "_|_" + first.get(i) + "_|__" + secondnegado.get(i) + "_|_" + second.get(i) + "_|____" + result.get(i) + "____|");
                    }
                }
                else if (formula.length() == 5){
                    System.out.println("___________________________");
                    System.out.println("|_" + variables.get(1) + "_|_" + var1[1] + "_|_" + variables.get(0) + "_|_" + var0[1] + "_|_" + formula + "_|");
                    for (int i = 0; i < 4; i++) {
                        System.out.println("|__" + firstnegado.get(i) + "_|_" + first.get(i) + "_|__" + secondnegado.get(i) + "_|_" + second.get(i) + "_|____" + result.get(i) + "___|");
                    }
                }
            }
        }

        else if (distinctVariables.size() == 3){

            ArrayList<Character> firstnegado  = new ArrayList<>(List.of('F', 'V', 'F', 'V', 'F', 'V', 'F', 'V'));
            ArrayList<Character> secondnegado = new ArrayList<>(List.of('F', 'F', 'V', 'V', 'F', 'F', 'V', 'V'));
            ArrayList<Character> thirdnegado  = new ArrayList<>(List.of('F', 'F', 'F', 'F', 'V', 'V', 'V', 'V'));

            char[] var0 = variables.get(0).toCharArray();
            char[] var1 = variables.get(1).toCharArray();
            char[] var2 = variables.get(2).toCharArray();

            if (variables.get(2).length() == 1 && variables.get(1).length() == 1 && variables.get(0).length() == 1) {
                if (formula.length() == 9){
                    System.out.println("________________________");
                    System.out.println("|_" + variables.get(2) + "_|_" + variables.get(1) + "_|_" + variables.get(0) + "_|_" + formula + "_|");
                    for (int i = 0; i < 8; i++) {
                        System.out.println("|_" + first.get(i) + "_|_" + second.get(i) + "_|_" + third.get(i) + "_|_____" + result.get(i) + "_____|");
                    }
                }
                else if (formula.length() == 5){
                    System.out.println("____________________");
                    System.out.println("|_" + variables.get(2) + "_|_" + variables.get(1) + "_|_" + variables.get(0) + "_|_" + formula + "_|");
                    for (int i = 0; i < 8; i++) {
                        System.out.println("|_" + first.get(i) + "_|_" + second.get(i) + "_|_" + third.get(i) + "_|___" + result.get(i) + "___|");
                    }
                }
            }
            else if (variables.get(2).length() == 2 && variables.get(1).length() == 1 && variables.get(0).length() == 1){
                if (formula.length() == 10){
                    System.out.println("_______________________________");
                    System.out.println("|_" + variables.get(2) + "_|_" + var2[1] + "_|_" + variables.get(1) + "_|_" + variables.get(0) + "_|_" + formula + "_|");
                    for (int i = 0; i < 8; i++) {
                        System.out.println("|__" + firstnegado.get(i) + "_|_" + first.get(i) + "_|_" + second.get(i) + "_|_" + third.get(i) + "_|______" + result.get(i) + "_____|");
                    }
                }
                else if (formula.length() == 6){
                    System.out.println("___________________________");
                    System.out.println("|_" + variables.get(2) + "_|_" + var2[1] + "_|_" + variables.get(1) + "_|_" + variables.get(0) + "_|_" + formula + "_|");
                    for (int i = 0; i < 8; i++) {
                        System.out.println("|__" + firstnegado.get(i) + "_|_" + first.get(i) + "_|_" + second.get(i) + "_|_" + third.get(i) + "_|____" + result.get(i) + "___|");
                    }
                }
            }
            else if (variables.get(2).length() == 1 && variables.get(1).length() == 2 && variables.get(0).length() == 1){
                if (formula.length() == 10){
                    System.out.println("_______________________________");
                    System.out.println("|_" + variables.get(2) + "_|_" + variables.get(1) + "_|_" + var1[1] + "_|_" + variables.get(0) + "_|_" + formula + "_|");
                    for (int i = 0; i < 8; i++) {
                        System.out.println("|_" + first.get(i) + "_|__" + secondnegado.get(i) + "_|_" + "_|_" + second.get(i) + "_|_" + third.get(i) + "_|______" + result.get(i) + "_____|");
                    }
                }
                else if (formula.length() == 6){
                    System.out.println("___________________________");
                    System.out.println("|_" + variables.get(2) + "_|_"  + variables.get(1) + "_|_" + var1[1] + "_|_" + variables.get(0) + "_|_" + formula + "_|");
                    for (int i = 0; i < 8; i++) {
                        System.out.println("|_" + first.get(i) + "_|__" + secondnegado.get(i) + "_|_" + "_|_" + second.get(i) + "_|_" + third.get(i) + "_|____" + result.get(i) + "___|");
                    }
                }
            }
            else if (variables.get(2).length() == 1 && variables.get(1).length() == 1 && variables.get(0).length() == 2){
                if (formula.length() == 10){
                    System.out.println("_______________________________");
                    System.out.println("|_" + variables.get(2) + "_|_" + variables.get(1) + "_|_" + variables.get(0) + "_|_" + var0[1] + "_|_" + formula + "_|");
                    for (int i = 0; i < 8; i++) {
                        System.out.println("|_" + first.get(i) + "_|_" + second.get(i) + "_|__" + thirdnegado.get(i) + "_|_" + third.get(i) + "_|______" + result.get(i) + "_____|");
                    }
                }
                else if (formula.length() == 6){
                    System.out.println("___________________________");
                    System.out.println("|_" + variables.get(2) + "_|_" + variables.get(1) + "_|_" + variables.get(0) + "_|_" + var0[1] + "_|_" + formula + "_|");
                    for (int i = 0; i < 8; i++) {
                        System.out.println("|_" + first.get(i) + "_|_" + second.get(i) + "_|__" + thirdnegado.get(i) + "_|_" + third.get(i) + "_|____" + result.get(i) + "___|");
                    }
                }
            }
            else if (variables.get(2).length() == 2 && variables.get(1).length() == 2 && variables.get(0).length() == 1){
                if (formula.length() == 11){
                    System.out.println("_____________________________________");
                    System.out.println("|_" + variables.get(2) + "_|_" + var2[1] + "_|_" + variables.get(1) + "_|_" + var1[1] + "_|_" + variables.get(0) + "_|_" + formula + "_|");
                    for (int i = 0; i < 8; i++) {
                        System.out.println("|__" + firstnegado.get(i) + "_|_" + first.get(i) + "_|__" + secondnegado.get(i) + "_|_" + second.get(i) + "_|_" + third.get(i) + "_|______" + result.get(i) + "______|");
                    }
                }
                else if (formula.length() == 7){
                    System.out.println("_________________________________");
                    System.out.println("|_" + variables.get(2) + "_|_" + var2[1] + "_|_" + variables.get(1) + "_|_" + var1[1] + "_|_" + variables.get(0) + "_|_" + formula + "_|");
                    for (int i = 0; i < 8; i++) {
                        System.out.println("|__" + firstnegado.get(i) + "_|_" + first.get(i) + "_|__" + secondnegado.get(i) + "_|_" + second.get(i) + "_|_" + third.get(i) + "_|____" + result.get(i) + "____|");
                    }
                }
            }
            else if (variables.get(2).length() == 2 && variables.get(1).length() == 1 && variables.get(2).length() == 2){
                if (formula.length() == 11){
                    System.out.println("_____________________________________");
                    System.out.println("|_" + variables.get(2) + "_|_" + var2[1] + "_|_" + variables.get(1) + "_|_" + variables.get(0) + "_|_" + var0[1] + "_|_" + formula + "_|");
                    for (int i = 0; i < 8; i++) {
                        System.out.println("|__" + firstnegado.get(i) + "_|_" + first.get(i) + "_|_" + second.get(i) + "_|__" + thirdnegado.get(i) + "_|_" + third.get(i) + "_|______" + result.get(i) + "______|");
                    }
                }
                else if (formula.length() == 7){
                    System.out.println("_________________________________");
                    System.out.println("|_" + variables.get(2) + "_|_" + var2[1] + "_|_" + variables.get(1) + "_|_" + var1[1] + "_|_" + variables.get(0) + "_|_" + var0[1] + "_|_" + formula + "_|");
                    for (int i = 0; i < 8; i++) {
                        System.out.println("|__" + firstnegado.get(i) + "_|_" + first.get(i) + "_|_" +  second.get(i) + "_|__" + thirdnegado.get(i) + "_|_" + third.get(i) + "_|____" + result.get(i) + "____|");
                    }
                }
            }
            else if (variables.get(2).length() == 1 && variables.get(1).length() == 2 && variables.get(0).length() == 2){
                if (formula.length() == 11){
                    System.out.println("_____________________________________");
                    System.out.println("|_" + variables.get(2) + "_|_" + variables.get(1) + "_|_" + var1[1] + "_|_" + variables.get(0) + "_|_" + var0[1] + "_|_" + formula + "_|");
                    for (int i = 0; i < 8; i++) {
                        System.out.println("|_" + first.get(i) + "_|__" + secondnegado.get(i) + "_|_" + second.get(i) + "_|__" + thirdnegado.get(i) + "_|_" + third.get(i) + "_|______" + result.get(i) + "______|");
                    }
                }
                else if (formula.length() == 7){
                    System.out.println("_________________________________");
                    System.out.println("|_" + variables.get(2) + "_|_" + variables.get(1) + "_|_" + var1[1] + "_|_" + variables.get(0) + "_|_" + var0[1] + "_|_" + formula + "_|");
                    for (int i = 0; i < 8; i++) {
                        System.out.println("|_" + first.get(i) + "_|__" + secondnegado.get(i) + "_|_" + second.get(i) + "_|__" + thirdnegado.get(i) + "_|_" + third.get(i) + "_|____" + result.get(i) + "____|");
                    }
                }
            }
            else if (variables.get(2).length() == 2 && variables.get(1).length() == 2 && variables.get(0).length() == 2){
                if (formula.length() == 12){
                    System.out.println("___________________________________________");
                    System.out.println("|_" + variables.get(2) + "_|_" + var2[1] + "_|_" + variables.get(1) + "_|_" +
                            var1[1] + "_|_" + variables.get(0) + "_|_" + var0[1] + "_|_" + formula + "_|");
                    for (int i = 0; i < 8; i++) {
                        System.out.println("|__" + firstnegado.get(i) + "_|_" + first.get(i) + "_|__" + secondnegado.get(i) + "_|_" +
                                second.get(i) + "_|__" + thirdnegado.get(i) + "_|_" + third.get(i) + "_|_______" + result.get(i) + "______|");
                    }
                }
                else if (formula.length() == 8){
                    System.out.println("_______________________________________");
                    System.out.println("|_" + variables.get(2) + "_|_" + var2[1] + "_|_" + variables.get(1) + "_|_" +
                            var1[1] + "_|_" + variables.get(0) + "_|_" + var0[1] + "_|_" + formula + "_|");
                    for (int i = 0; i < 8; i++) {
                        System.out.println("|__" + firstnegado.get(i) + "_|_" + first.get(i) + "_|__" + secondnegado.get(i) + "_|_" +
                                second.get(i) + "_|__" + thirdnegado.get(i) + "_|_" + third.get(i) + "_|_____" + result.get(i) + "____|");
                    }
                }
            }
        }

        int counter = 0, counter2 = 0;
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i) == 'V'){
                counter++;
            }
            else if (result.get(i) == 'F'){
                counter2++;
            }
        }
        if (counter == result.size()){
            System.out.println("Essa expressão lógica resulta em uma TAUTOLOGIA");
        }
        else if (counter2 == result.size()){
            System.out.println("Essa expressão lógica resulta em uma CONTRADIÇÃO");
        }

    }
}
