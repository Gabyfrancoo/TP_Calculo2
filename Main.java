import java.util.Scanner;
import java.util.Locale;

// classe para resolver integrais por fracoes parciais
public class Main {

    // Tolerancia para verificar se um double e zero
    final static double TOLERANCIA = 1e-9;

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);
        boolean executando = true;

        // Loop principal do menu
        while (executando) {
            exibirMenu();
            int escolha = sc.nextInt();

            switch (escolha) {
                case 1:
                    // Esta funcao contem o codigo para Grau 2 (Casos 1 e 2)
                    resolverIntegralGrau2(sc);
                    break;
                case 2:
                    // Esta e a nova funcao para os Casos 3 e 4
                    resolverIntegralGrau4(sc);
                    break;
                case 0:
                    executando = false;
                    System.out.println("Encerrando o programa.");
                    break;
                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }
            System.out.println("----------------------------------------");
        }
        sc.close();
    }

    // Exibe o menu de opcoes para o usuario.

    public static void exibirMenu() {
        System.out.println("Selecione o tipo de integral para resolver:");
        System.out.println("1. Denominador Grau 2: (Ax+B) / (ax^2+bx+c)  (Resolve Casos 1 e 2)");
        System.out.println("2. Denominador Grau 4: (Ax+B) / ((x^2+k1)(x^2+k2)) (Resolve Casos 3 e 4)");
        System.out.println("0. Sair");
        System.out.print("Sua escolha: ");
    }

    // --- OPCAO 1: RESOLVE CASOS 1 E 2 (DENOMINADOR GRAU 2) ---

    // Funcao principal para a Opcao 1. Pede A, B, a, b, c e resolve
    
    public static void resolverIntegralGrau2(Scanner sc) {
        System.out.println("\n--- Opcao 1: (Ax+B) / (ax^2+bx+c) ---");
        System.out.println("Resolve os Casos 1 e 2 do enunciado (Delta > 0 ou Delta < 0)");
        
        System.out.print("Digite o coeficiente A do numerador: ");
        double A = sc.nextDouble();
        System.out.print("Digite o coeficiente B do numerador: ");
        double B = sc.nextDouble();
        System.out.print("Digite o coeficiente a do denominador: ");
        double a = sc.nextDouble();
        System.out.print("Digite o coeficiente b do denominador: ");
        double b = sc.nextDouble();
        System.out.print("Digite o coeficiente c do denominador: ");
        double c = sc.nextDouble();

        // Imprime a integral 
        System.out.println(String.format(Locale.US,
                "\nCalculando: integral de (%.1fx + %.2f) / (%.1fx^2 + %.1fx + %.1f) dx",
                A, B, a, b, c));
        
        String resultado = getSolucaoGrau2(A, B, a, b, c);
        System.out.println(resultado);
    }

    //  Funcao que calcula a integral de Grau 2, decidindo pelo Delta
    public static String getSolucaoGrau2(double A, double B, double a, double b, double c) {
        if (a == 0) {
            return "Erro: 'a' nao pode ser zero (nao e um denominador quadratico).";
        }
        double delta = (b * b) - (4 * a * c);
        StringBuilder sb = new StringBuilder();

        if (delta < 0) {
            // --- CASO 2 (IRREDUTIVEL) ---
            sb.append("\n--> Caso da Integral: Denominador Quadratico Irredutivel (Delta < 0)\n");
            sb.append(getSolucaoCaso2(A, B, a, b, c)); // Chama a logica do Caso 2

        } else if (delta == 0) {
            // --- CASO RAIZ DUPLA ---
            sb.append("\n--> Caso da Integral: Denominador com Raiz Real Dupla (Delta = 0)\n");
            double raiz = -b / (2 * a);
            double C1 = A / a;
            double C2 = (B / a) + (C1 * raiz);
            
            sb.append("\n1. Decomposicao Aplicada:\n");
            sb.append(String.format(Locale.US, "(%.1f / %s) + (%.1f / %s^2)\n",
                    C1, criaFator(raiz), C2, criaFator(raiz)));
            
            //2. Apresentar Resultado Simbolico
            sb.append("\n2. Resultado Simbolico Final:\n");
            sb.append(construirResultado(C1, C2, raiz)); // Chama helper para construir string

        } else {
            // --- CASO 1 (RAIZES DISTINTAS) ---
            sb.append("\n--> Caso da Integral: Denominador com Raizes Reais Distintas (Delta > 0)\n");
            double raiz1 = (-b + Math.sqrt(delta)) / (2 * a);
            double raiz2 = (-b - Math.sqrt(delta)) / (2 * a);
            double C1 = (A * raiz1 + B) / (a * (raiz1 - raiz2));
            double C2 = (A * raiz2 + B) / (a * (raiz2 - raiz1));

            sb.append("\n1. Decomposicao Aplicada:\n");
            sb.append(String.format(Locale.US, "(%.1f / %s) + (%.1f / %s)\n",
                    C1, criaFator(raiz1), C2, criaFator(raiz2))); 

            // 2. Apresentar Resultado Simbolico
            sb.append("\n2. Resultado Simbolico Final:\n");
            sb.append(construirResultado(C1, C2, raiz1, raiz2)); // Chama helper para construir string
        }
        return sb.toString();
    }

    // --- OPCAO 2: RESOLVE CASOS 3 E 4 (DENOMINADOR GRAU 4) ---

    // Funcao principal para a Opcao 2. Pede A, B, k1, k2 e resolve.
    
    public static void resolverIntegralGrau4(Scanner sc) {
        System.out.println("\n--- Opcao 2: (Ax+B) / ((x^2+k1)(x^2+k2)) ---");
        System.out.println("Resolve os Casos 3 e 4 do enunciado.");
        
        System.out.print("Digite o coeficiente A do numerador: ");
        double A = sc.nextDouble();
        System.out.print("Digite o coeficiente B do numerador: ");
        double B = sc.nextDouble();
        System.out.print("Digite a constante k1 (de x^2 + k1): ");
        double k1 = sc.nextDouble();
        System.out.print("Digite a constante k2 (de x^2 + k2): ");
        double k2 = sc.nextDouble();

        if (Math.abs(k1 - k2) < TOLERANCIA) {
            System.out.println("Erro: k1 e k2 devem ser distintos. (Este e um caso de raizes repetidas)");
            return;
        }
        if (k1 <= 0 || k2 <= 0) {
            System.out.println("Aviso: Para serem irredutiveis, k1 e k2 devem ser positivos.");
        }

        // Decomposicao: (C1x+D1)/(x^2+k1) + (C2x+D2)/(x^2+k2)
        // C1*k2 + C2*k1 = A
        // D1*k2 + D2*k1 = B
        // C1+C2 = 0 => C1 = -C2
        // D1+D2 = 0 => D1 = -D2
        
        double C2 = A / (k1 - k2);
        double C1 = -C2;
        double D2 = B / (k1 - k2);
        double D1 = -D2;
        
        StringBuilder sb = new StringBuilder();
        sb.append("\n1. Decomposicao Aplicada:\n");
        sb.append(String.format(Locale.US, "(%.1fx + %.1f) / (x^2 + %.2f) + (%.1fx + %.1f) / (x^2 + %.2f)\n",
                C1, D1, k1, C2, D2, k2));

        // 2. Integrar cada termo
        // Cada termo e um "Caso 2" simples (a=1, b=0, c=k)
        String integral1 = getSolucaoCaso2(C1, D1, 1, 0, k1);
        String integral2 = getSolucaoCaso2(C2, D2, 1, 0, k2);

        sb.append("\n2. Resultado Simbolico Final:\n");
        
        // --- INICIO DA ATUALIZACAO ---
        // Define o cabecalho que queremos remover das strings filhas
        String header = "\n2. Resultado Simbolico Final:\n";
        
        // Remove o cabecalho e o "+ C" de cada parte
        String res1 = integral1.substring(integral1.indexOf(header) + header.length()).replace(" + C\n", "");
        String res2 = integral2.substring(integral2.indexOf(header) + header.length()).replace(" + C\n", "");
        // --- FIM DA ATUALIZACAO ---
        
        sb.append("(" + res1 + ") + (" + res2 + ") + C\n");
        System.out.println(sb.toString());
    }

    // --- FUNCOES AUXILIARES (HELPER) ---

    // Funcao auxiliar que retorna a string da solucao do Caso 2
    
    private static String getSolucaoCaso2(double A, double B, double a, double b, double c) {
        StringBuilder sb = new StringBuilder();
        double h = b / (2 * a);
        double kQuadrado = (4 * a * c - b * b) / (4 * a * a);
        double k = Math.sqrt(kQuadrado);
        double C_novo = A;
        double D_novo = B - A * h;

        sb.append("\n1. Decomposicao (Completando Quadrado):\n");
        sb.append(String.format(Locale.US, "A integral e reescrita como (1/a) * integral de (%.1f*u + %.1f) / (u^2 + %.1f) du\n",
                                C_novo, D_novo, kQuadrado));
        sb.append(String.format(Locale.US, "Onde u = (x + %.1f)\n", h));

        double termoLn_coef = C_novo / (2 * a);
        double termoAtan_coef = D_novo / (a * k);
        
        //2. Apresentar Resultado Simbolico
        sb.append("\n2. Resultado Simbolico Final:\n");
        sb.append(construirResultado(termoLn_coef, termoAtan_coef, a, b, c, h, k)); // Chama helper
        
        return sb.toString();
    }

    //  Auxiliar para construir a string de resultado do Caso 1 (Raizes Distintas).

    private static String construirResultado(double C1, double C2, double r1, double r2) {
        StringBuilder sb = new StringBuilder();
        // Termo C1
        if (Math.abs(C1) > TOLERANCIA) {
            sb.append(String.format(Locale.US, "%.1f * ln|%s|",
                    C1, criaFator(r1).replace("(", "").replace(")", "")));
        }
        // Termo C2
        if (Math.abs(C2) > TOLERANCIA) {
            if (sb.length() > 0 && C2 >= 0) sb.append(" + ");
            else if (C2 < 0) sb.append(" ");
            sb.append(String.format(Locale.US, "%.1f * ln|%s|",
                    C2, criaFator(r2).replace("(", "").replace(")", "")));
        }
        if (sb.length() == 0) sb.append("0");
        sb.append(" + C\n");
        return sb.toString();
    }
    
    // Auxiliar para construir a string de resultado do Caso de Raiz Dupla.
    private static String construirResultado(double C1, double C2, double r) {
        StringBuilder sb = new StringBuilder();
        // Termo Ln (C1)
        if (Math.abs(C1) > TOLERANCIA) {
            sb.append(String.format(Locale.US, "%.1f * ln|%s|",
                    C1, criaFator(r).replace("(", "").replace(")", "")));
        }
        // Termo Fracao (C2)
        if (Math.abs(C2) > TOLERANCIA) {
            if (sb.length() > 0) sb.append(String.format(Locale.US, " - (%.1f / %s)", C2, criaFator(r)));
            else sb.append(String.format(Locale.US, "- (%.1f / %s)", C2, criaFator(r)));
        }
        if (sb.length() == 0) sb.append("0");
        sb.append(" + C\n");
        return sb.toString();
    }

    // Auxiliar para construir a string de resultado do Caso 2 (Irredutivel)
    private static String construirResultado(double lnCoef, double atanCoef, double a, double b, double c, double h, double k) {
        StringBuilder sb = new StringBuilder();
        // Termo Ln
        if (Math.abs(lnCoef) > TOLERANCIA) {
            sb.append(String.format(Locale.US, "%.1f * ln(%.2fx^2 + %.2fx + %.2f)", lnCoef, a, b, c));
        }
        // Termo Atan - arco tangente
        if (Math.abs(atanCoef) > TOLERANCIA) {
            if (sb.length() > 0 && atanCoef >= 0) sb.append(" + ");
            else if (atanCoef < 0) sb.append(" ");
            sb.append(String.format(Locale.US, "%.1f * atan((x + %.1f) / %.1f)", atanCoef, h, k));
        }
        if (sb.length() == 0) sb.append("0");
        sb.append(" + C\n");
        return sb.toString();
    }

    // Funcao auxiliar para construir o fator (x - raiz) e para formatar a saida
    private static String criaFator(double raiz) {
        if (raiz >= 0) {
            // Se a raiz for 2, o fator e (x - 2.0)
            return String.format(Locale.US, "(x - %.1f)", raiz);
        } else {
            // Se a raiz for -3, o fator e (x + 3.0)
            return String.format(Locale.US, "(x + %.1f)", Math.abs(raiz));
        }
    }
}