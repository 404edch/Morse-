import java.util.Scanner;

class Node {

    private char info;
    private Node esquerda;
    private Node direita;
    private int coluna;

    Node(char info) {
        this.info = info;
        this.esquerda = null;
        this.direita = null;
        this.coluna = 0;
    }

    char getInfo() {
        return info;
    }

    void setInfo(char info) {
        this.info = info;
    }

    Node getEsquerda() {
        return esquerda;
    }

    void setEsquerda(Node esquerda) {
        this.esquerda = esquerda;
    }

    Node getDireita() {
        return direita;
    }

    void setDireita(Node direita) {
        this.direita = direita;
    }

    int getColuna() {
        return coluna;
    }

    void setColuna(int coluna) {
        this.coluna = coluna;
    }
}

public class ArvoreBinariaMorse {

    private Node raiz;
    private char[][] grade;
    private int proximaColuna;

    void inicializar() {
        raiz = new Node(' ');
    }

    void inserir(String morse, char letra) {
        Node atual = raiz;
        int i = 0;
        while (i < morse.length()) {
            if (morse.charAt(i) == '.') {
                if (atual.getEsquerda() == null) {
                    atual.setEsquerda(new Node(' '));
                }
                atual = atual.getEsquerda();
            } else {
                if (atual.getDireita() == null) {
                    atual.setDireita(new Node(' '));
                }
                atual = atual.getDireita();
            }
            i = i + 1;
        }
        atual.setInfo(letra);
    }

    char buscar(String morse) {
        Node atual = raiz;
        int i = 0;
        while (i < morse.length() && atual != null) {
            if (morse.charAt(i) == '.') {
                atual = atual.getEsquerda();
            } else if (morse.charAt(i) == '-') {
                atual = atual.getDireita();
            } else {
                return '?';
            }
            i = i + 1;
        }
        if (atual == null || atual.getInfo() == ' ') {
            return '?';
        }
        return atual.getInfo();
    }

    String buscarMorse(char letra) {
        return buscarMorse(raiz, Character.toUpperCase(letra), "");
    }

    private String buscarMorse(Node no, char letra, String codigo) {
        if (no == null) {
            return null;
        }
        if (no.getInfo() == letra) {
            return codigo;
        }

        String resultado = buscarMorse(no.getEsquerda(), letra, codigo + ".");
        if (resultado != null) {
            return resultado;
        }
        return buscarMorse(no.getDireita(), letra, codigo + "-");
    }

    String traduzir(String mensagem) {
        String resultado = "";
        String codigo = "";
        int i = 0;
        while (i < mensagem.length()) {
            if (mensagem.charAt(i) == ' ') {
                resultado = resultado + buscar(codigo);
                codigo = "";
            } else {
                codigo = codigo + mensagem.charAt(i);
            }
            i = i + 1;
        }
        resultado = resultado + buscar(codigo);
        return resultado;
    }

    int altura(Node no) {
        if (no == null) {
            return 0;
        }
        int esquerda = altura(no.getEsquerda());
        int direita = altura(no.getDireita());
        if (esquerda > direita) {
            return 1 + esquerda;
        }
        return 1 + direita;
    }

    void exibir() {
        proximaColuna = 0;
        calcularColunas(raiz);

        int linhas = altura(raiz) * 2 - 1;
        int largura = proximaColuna * 2;
        grade = new char[linhas][largura];

        int i = 0;
        while (i < linhas) {
            int j = 0;
            while (j < largura) {
                grade[i][j] = ' ';
                j = j + 1;
            }
            i = i + 1;
        }

        desenhar(raiz, 0);

        i = 0;
        while (i < linhas) {
            System.out.println(new String(grade[i]));
            i = i + 1;
        }
    }

    void calcularColunas(Node no) {
        if (no != null) {
            calcularColunas(no.getEsquerda());
            no.setColuna(proximaColuna * 2);
            proximaColuna = proximaColuna + 1;
            calcularColunas(no.getDireita());
        }
    }

    void desenhar(Node no, int linha) {
        if (no.getInfo() == ' ') {
            grade[linha][no.getColuna()] = '*';
        } else {
            grade[linha][no.getColuna()] = no.getInfo();
        }

        Node esquerda = no.getEsquerda();
        if (esquerda != null) {
            int j = esquerda.getColuna() + 2;
            while (j < no.getColuna()) {
                grade[linha][j] = '_';
                j = j + 1;
            }
            grade[linha + 1][esquerda.getColuna() + 1] = '/';
            desenhar(esquerda, linha + 2);
        }

        Node direita = no.getDireita();
        if (direita != null) {
            int j = no.getColuna() + 1;
            while (j < direita.getColuna() - 1) {
                grade[linha][j] = '_';
                j = j + 1;
            }
            grade[linha + 1][direita.getColuna() - 1] = '\\';
            desenhar(direita, linha + 2);
        }
    }

    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);

        ArvoreBinariaMorse arvore = new ArvoreBinariaMorse();
        arvore.inicializar();

        arvore.inserir(".-", 'A');
        arvore.inserir("-...", 'B');
        arvore.inserir("-.-.", 'C');
        arvore.inserir("-..", 'D');
        arvore.inserir(".", 'E');
        arvore.inserir("..-.", 'F');
        arvore.inserir("--.", 'G');
        arvore.inserir("....", 'H');
        arvore.inserir("..", 'I');
        arvore.inserir(".---", 'J');
        arvore.inserir("-.-", 'K');
        arvore.inserir(".-..", 'L');
        arvore.inserir("--", 'M');
        arvore.inserir("-.", 'N');
        arvore.inserir("---", 'O');
        arvore.inserir(".--.", 'P');
        arvore.inserir("--.-", 'Q');
        arvore.inserir(".-.", 'R');
        arvore.inserir("...", 'S');
        arvore.inserir("-", 'T');
        arvore.inserir("..-", 'U');
        arvore.inserir("...-", 'V');
        arvore.inserir(".--", 'W');
        arvore.inserir("-..-", 'X');
        arvore.inserir("-.--", 'Y');
        arvore.inserir("--..", 'Z');

        arvore.inserir("-----", '0');
        arvore.inserir(".----", '1');
        arvore.inserir("..---", '2');
        arvore.inserir("...--", '3');
        arvore.inserir("....-", '4');
        arvore.inserir(".....", '5');
        arvore.inserir("-....", '6');
        arvore.inserir("--...", '7');
        arvore.inserir("---..", '8');
        arvore.inserir("----.", '9');

        arvore.exibir();

        System.out.print("Digite uma letra ou numero para buscar: ");
        String caractere = leitor.nextLine().trim();
        if (caractere.length() == 1) {
            String codigo = arvore.buscarMorse(caractere.charAt(0));
            if (codigo == null) {
                System.out.println("Caractere nao encontrado.");
            } else {
                System.out.println("Em morse: " + codigo);
            }
        } else {
            System.out.println("Digite apenas uma letra ou numero.");
        }

        System.out.print("Digite a mensagem em morse: ");
        String mensagem = leitor.nextLine();
        System.out.println("Decodificado: " + arvore.traduzir(mensagem));
    }
}