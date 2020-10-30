
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
    public ArrayList<Character> removeInRange(ArrayList<Character> list, int start,int end) {
        if (end > start) {
            list.subList(start, end).clear();
        }
        return list;
    }



    /**
     * Constrói a tabela verdade baseado nas variáveis e operações de entrada
     * na equação original.
     * @param var ArrayList das varíaveis
     * @param op ArrayList das operações
     * @param dist ArrayList das variáveis únicas
     * @param formula fórmula original
     */
    public void run(ArrayList<String> var, ArrayList<Character> op, ArrayList<String> dist, String formula){
        ArrayList<Character> first  = new ArrayList<>(List.of('V', 'F', 'V', 'F', 'V', 'F', 'V', 'F'));
        ArrayList<Character> second = new ArrayList<>(List.of('V', 'V', 'F', 'F', 'V', 'V', 'F', 'F'));
        ArrayList<Character> third  = new ArrayList<>(List.of('V', 'V', 'V', 'V', 'F', 'F', 'F', 'F'));
        ArrayList<Character> result = new ArrayList<>();
        int size = dist.size();

        if(size == 1) {
            first = removeInRange(first, 2, 8);
            second.clear();
            third.clear();
            table.put(dist.get(0), first);
        }

        else if(size == 2){
            first = removeInRange(first, 4, 8);
            second = removeInRange(second, 4, 8);
            third.clear();
            table.put(dist.get(1), first);
            table.put(dist.get(0), second);
        }
        else{
            table.put(dist.get(2), first);
            table.put(dist.get(1), second);
            table.put(dist.get(0), third);
        }



        if(var.size() == 1){
            String v = var.get(0);
            for (Character c: first)
                result.add(calculate(v, c, '^', v, c));
        }

        else if(var.size() == 2){
            Iterator<Character> f = first.iterator();
            Iterator<Character> s = second.iterator();
            String v = var.get(0);
            String v2 = var.get(1);

            if(size == 1) {
                for (Character c: first)
                    result.add(calculate(v, c, op.get(0), v2, c));
            }
            else{
                while (f.hasNext() && s.hasNext())
                    result.add(calculate(v, s.next(), op.get(0), v2, f.next()));
            }
        }

        else{
            Iterator<Character> f = first.iterator();
            Iterator<Character> s = second.iterator();
            Iterator<Character> t = third.iterator();
            String v = var.get(0);
            String v2 = var.get(1);
            String v3 = var.get(2);
            Character cal1;

            if(size == 1) {
                for (Character c: first) {
                    cal1 = calculate(v, c, op.get(0), v2, c);
                    result.add(calculate(" ", cal1, op.get(1), v3, c));
                }
            }
            //==========================================
            if(size == 2) {
                if(var.get(1).contains(var.get(0)) || var.get(0).contains(var.get(1))){
                    while (f.hasNext() && s.hasNext()) {
                        Character temp = s.next();
                        cal1 = calculate(v, temp, op.get(0), v2, temp);
                        result.add(calculate(" ", cal1, op.get(1), v3, f.next()));
                    }
                }
                else if(var.get(2).contains(var.get(0)) || var.get(0).contains(var.get(2))){
                    while (f.hasNext() && s.hasNext()) {
                        Character temp = s.next();
                        cal1 = calculate(v, temp, op.get(0), v2, f.next());
                        result.add(calculate(" ", cal1, op.get(1), v3, temp));
                    }
                }
                else{
                    while (f.hasNext() && s.hasNext()) {
                        Character temp = f.next();
                        cal1 = calculate(v, s.next(), op.get(0), v2, temp);
                        result.add(calculate(" ", cal1, op.get(1), v3, temp));
                    }
                }
            }
            //==========================================
            else {
                while (f.hasNext() && s.hasNext() && t.hasNext()) {
                    cal1 = calculate(v, t.next(), op.get(0), v2, s.next());
                    result.add(calculate("", cal1, op.get(1), v3, f.next()));
                }
            }
        }

        table.put(formula, result);
    }
}
