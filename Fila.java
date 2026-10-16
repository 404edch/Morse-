import java.util.Scanner;

public class Fila {

    private int tamanho;
    private int[] dados;
    private int primeiro;
    private int ultimo;
    private int capacidade;

    Fila(int tamanho) {
        this.tamanho = tamanho;
        this.dados = new int[tamanho];
        this.primeiro = 0;
        this.ultimo = -1;
        this.capacidade = 0;
    }

    boolean cheia() {
        if (capacidade == tamanho) {
            return true;
        } else {
            return false;
        }
    }

    boolean vazia() {
        if (capacidade == 0) {
            return true;
        } else {
            return false;
        }
    }

    void insere(int elemento) {
        if (cheia() == true) {
            System.out.println("Fila cheia. Nao foi possivel inserir o valor: " + elemento);
        } else {
            ultimo = ultimo + 1;
            if (ultimo == tamanho) {
                ultimo = 0;
            }
            dados[ultimo] = elemento;
            capacidade = capacidade + 1;
        }
    }

    void remove() {
        if (vazia() == true) {
            System.out.println("Fila vazia. Nao ha elemento para remover.");
        } else {
            int valorRemovido = dados[primeiro];
            primeiro = primeiro + 1;
            if (primeiro == tamanho) {
                primeiro = 0;
            }
            capacidade = capacidade - 1;
        }
    }

    void imprime() {
        if (vazia() == true) {
            System.out.println("Fila vazia.");
        } else {
            int indice = primeiro;
            int contador = 0;
            while (contador < capacidade) {
                System.out.print(dados[indice] + " ");
                indice = indice + 1;
                if (indice == tamanho) {
                    indice = 0;
                }
                contador = contador + 1;
            }
            System.out.println();
        }
    }

    int primeiro() {
        if (vazia()) {
            System.out.println("Fila vazia. Nao ha primeiro elemento.");
        }
        return dados[primeiro];
    }

    int ultimo() {
        if (vazia()) {
            System.out.println("Fila vazia. Nao ha ultimo elemento.");
        }
        return dados[ultimo];
    }

    static Fila merge(Fila a, Fila b) {
        Fila r = new Fila(a.tamanho + b.tamanho);

        while (a.vazia() == false && b.vazia() == false) {
            if (a.primeiro() < b.primeiro()) {
                r.insere(a.primeiro());
                a.remove();
            } else {
                r.insere(b.primeiro());
                b.remove();
            }
        }

        while (a.vazia() == false) {
            r.insere(a.primeiro());
            a.remove();
        }

        while (b.vazia() == false) {
            r.insere(b.primeiro());
            b.remove();
        }

        return r;
    }

    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);

        System.out.print("Digite o tamanho da fila: ");
        int tamanho = leitor.nextInt();

        Fila fila = new Fila(tamanho);

        fila.insere(10);
        fila.insere(20);
        fila.insere(30);
        fila.insere(40);
        fila.insere(50);

        fila.imprime();

        fila.remove();
        fila.remove();

        fila.imprime();

        fila.insere(60);
        fila.insere(70);

        fila.imprime();

        System.out.print("Primeiro " + fila.primeiro());
        System.out.print("| Ultimo " + fila.ultimo() + "\n");

        Fila a = new Fila(10);
        a.insere(12);
        a.insere(35);
        a.insere(52);
        a.insere(64);
        a.imprime();

        Fila b = new Fila(10);
        b.insere(5);
        b.insere(15);
        b.insere(23);
        b.insere(55);
        b.insere(75);
        b.imprime();

        Fila r = merge(a, b);
        r.imprime();

    }
}