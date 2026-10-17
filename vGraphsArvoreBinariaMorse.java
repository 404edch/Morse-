import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

class GraphMorseNode {

    private char info;
    private GraphMorseNode esquerda;
    private GraphMorseNode direita;

    GraphMorseNode(char info) {
        this.info = info;
        this.esquerda = null;
        this.direita = null;
    }

    char getInfo() {
        return info;
    }

    void setInfo(char info) {
        this.info = info;
    }

    GraphMorseNode getEsquerda() {
        return esquerda;
    }

    void setEsquerda(GraphMorseNode esquerda) {
        this.esquerda = esquerda;
    }

    GraphMorseNode getDireita() {
        return direita;
    }

    void setDireita(GraphMorseNode direita) {
        this.direita = direita;
    }
}

public class vGraphsArvoreBinariaMorse {

    private GraphMorseNode raiz;

    void inicializar() {
        raiz = new GraphMorseNode(' ');
    }

    void inserir(String morse, char letra) {
        GraphMorseNode atual = raiz;
        int i = 0;
        while (i < morse.length()) {
            if (morse.charAt(i) == '.') {
                if (atual.getEsquerda() == null) {
                    atual.setEsquerda(new GraphMorseNode(' '));
                }
                atual = atual.getEsquerda();
            } else {
                if (atual.getDireita() == null) {
                    atual.setDireita(new GraphMorseNode(' '));
                }
                atual = atual.getDireita();
            }
            i = i + 1;
        }
        atual.setInfo(letra);
    }

    char buscar(String morse) {
        GraphMorseNode atual = raiz;
        int i = 0;
        while (i < morse.length() && atual != null) {
            if (morse.charAt(i) == '.') {
                atual = atual.getEsquerda();
            } else {
                atual = atual.getDireita();
            }
            i = i + 1;
        }
        if (atual == null) {
            return '?';
        } else {
            return atual.getInfo() == ' ' ? '?' : atual.getInfo();
        }
    }

    String traduzir(String mensagem) {
        StringBuilder resultado = new StringBuilder();
        String texto = mensagem.trim();
        if (texto.isEmpty()) {
            return "";
        }

        String[] codigos = texto.split("\\s+");
        for (String codigo : codigos) {
            if (codigo.equals("/")) {
                resultado.append(' ');
            } else {
                resultado.append(buscar(codigo));
            }
        }
        return resultado.toString();
    }

    void abrirJanela() {
        JFrame janela = new JFrame("Arvore Binaria Morse");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setContentPane(new MorsePanel(this));
        janela.setSize(1180, 800);
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
    }

    private static class MorsePanel extends JPanel {
        private static final int NODE_RADIUS = 23;
        private static final int HORIZONTAL_GAP = 70;
        private static final int LEVEL_GAP = 105;
        private static final int MARGIN = 55;
        private static final Color BACKGROUND = new Color(242, 246, 250);
        private static final Color TREE_COLOR = new Color(148, 163, 184);
        private static final Color PATH_COLOR = new Color(16, 150, 105);
        private static final Color TEXT_COLOR = new Color(35, 48, 65);

        private final vGraphsArvoreBinariaMorse arvore;
        private final Map<GraphMorseNode, java.awt.Point> posicoes = new IdentityHashMap<>();
        private final List<GraphMorseNode> caminhoAtivo = new ArrayList<>();
        private final JTextField entrada = new JTextField();
        private final JButton traduzir = new JButton("Traduzir e animar");
        private final JLabel resultado = new JLabel("Digite codigos Morse separados por espaco.");
        private final Timer timer;
        private List<String> codigos = new ArrayList<>();
        private final StringBuilder textoTraduzido = new StringBuilder();
        private int indiceCodigo;
        private int indicePasso;
        private GraphMorseNode atual;

        MorsePanel(vGraphsArvoreBinariaMorse arvore) {
            this.arvore = arvore;
            setLayout(new java.awt.BorderLayout(8, 8));
            setBackground(BACKGROUND);
            setBorder(BorderFactory.createEmptyBorder(16, 18, 14, 18));

            JLabel titulo = new JLabel("Arvore Morse");
            titulo.setFont(new Font("SansSerif", Font.BOLD, 25));
            titulo.setForeground(TEXT_COLOR);
            JLabel subtitulo = new JLabel("Explore como cada ponto e traco encontra sua letra.");
            subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
            subtitulo.setForeground(new Color(100, 116, 139));

            JPanel cabecalho = new JPanel(new java.awt.BorderLayout(0, 3));
            cabecalho.setOpaque(false);
            cabecalho.add(titulo, java.awt.BorderLayout.NORTH);
            cabecalho.add(subtitulo, java.awt.BorderLayout.CENTER);

            JLabel instrucao = new JLabel("MORSE  |  Espaco entre letras   /   barra entre palavras");
            instrucao.setFont(new Font("SansSerif", Font.BOLD, 12));
            instrucao.setForeground(new Color(71, 85, 105));

            entrada.setFont(new Font("Monospaced", Font.PLAIN, 17));
            entrada.setForeground(TEXT_COLOR);
            entrada.setBackground(Color.WHITE);
            entrada.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(203, 213, 225)),
                    BorderFactory.createEmptyBorder(9, 11, 9, 11)));

            traduzir.setFont(new Font("SansSerif", Font.BOLD, 14));
            traduzir.setForeground(Color.WHITE);
            traduzir.setBackground(new Color(37, 99, 235));
            traduzir.setFocusPainted(false);
            traduzir.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
            traduzir.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));

            JPanel linhaEntrada = new JPanel(new java.awt.BorderLayout(10, 0));
            linhaEntrada.setOpaque(false);
            linhaEntrada.add(entrada, java.awt.BorderLayout.CENTER);
            linhaEntrada.add(traduzir, java.awt.BorderLayout.EAST);

            JPanel controles = new JPanel(new java.awt.BorderLayout(0, 8));
            controles.setBackground(Color.WHITE);
            controles.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(226, 232, 240)),
                    BorderFactory.createEmptyBorder(12, 14, 12, 14)));
            controles.add(instrucao, java.awt.BorderLayout.NORTH);
            controles.add(linhaEntrada, java.awt.BorderLayout.CENTER);

            JPanel topo = new JPanel(new java.awt.BorderLayout(0, 12));
            topo.setOpaque(false);
            topo.add(cabecalho, java.awt.BorderLayout.NORTH);
            topo.add(controles, java.awt.BorderLayout.CENTER);

            resultado.setFont(new Font("SansSerif", Font.BOLD, 15));
            resultado.setForeground(TEXT_COLOR);
            resultado.setBorder(BorderFactory.createEmptyBorder(11, 14, 11, 14));
            resultado.setOpaque(true);
            resultado.setBackground(Color.WHITE);

            TreeCanvas canvas = new TreeCanvas();
            JScrollPane scroll = new JScrollPane(canvas);
            scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
            scroll.getViewport().setBackground(new Color(250, 252, 255));
            scroll.getHorizontalScrollBar().setUnitIncrement(35);
            scroll.getVerticalScrollBar().setUnitIncrement(35);

            add(topo, java.awt.BorderLayout.NORTH);
            add(scroll, java.awt.BorderLayout.CENTER);
            add(resultado, java.awt.BorderLayout.SOUTH);

            traduzir.addActionListener(this::iniciarTraducao);
            entrada.addActionListener(this::iniciarTraducao);
            timer = new Timer(700, event -> avancarAnimacao());
        }

        private void iniciarTraducao(ActionEvent event) {
            String mensagem = entrada.getText().trim();
            if (mensagem.isEmpty()) {
                resultado.setText("Digite uma mensagem Morse para traduzir.");
                return;
            }

            for (String codigo : mensagem.split("\\s+")) {
                if (!codigo.equals("/") && !codigo.matches("[.-]+")) {
                    resultado.setText("Entrada invalida: use ponto, traco, espacos e /.");
                    return;
                }
            }

            codigos = new ArrayList<>();
            for (String codigo : mensagem.split("\\s+")) {
                codigos.add(codigo);
            }
            indiceCodigo = 0;
            indicePasso = 0;
            textoTraduzido.setLength(0);
            caminhoAtivo.clear();
            caminhoAtivo.add(arvore.raiz);
            atual = arvore.raiz;
            traduzir.setEnabled(false);
            entrada.setEnabled(false);
            resultado.setText("Percorrendo a arvore...");
            repaint();
            timer.start();
        }

        private void avancarAnimacao() {
            if (indiceCodigo >= codigos.size()) {
                timer.stop();
                traduzir.setEnabled(true);
                entrada.setEnabled(true);
                resultado.setText("Mensagem traduzida: " + arvore.traduzir(entrada.getText()));
                return;
            }

            String codigo = codigos.get(indiceCodigo);
            if (codigo.equals("/")) {
                textoTraduzido.append(' ');
                indiceCodigo++;
                reiniciarCaminho();
                resultado.setText("Mensagem parcial: " + textoTraduzido);
                repaint();
                return;
            }

            if (indicePasso == 0) {
                reiniciarCaminho();
            }

            char passo = codigo.charAt(indicePasso);
            atual = passo == '.' ? atual.getEsquerda() : atual.getDireita();
            indicePasso++;

            if (atual == null) {
                textoTraduzido.append('?');
                indiceCodigo++;
                indicePasso = 0;
                resultado.setText("Codigo nao encontrado. Parcial: " + textoTraduzido);
            } else {
                caminhoAtivo.add(atual);
                if (indicePasso == codigo.length()) {
                    char letra = atual.getInfo() == ' ' ? '?' : atual.getInfo();
                    textoTraduzido.append(letra);
                    indiceCodigo++;
                    indicePasso = 0;
                    resultado.setText("Codigo " + codigo + " -> " + letra
                            + "     Mensagem parcial: " + textoTraduzido);
                }
            }
            repaint();
        }

        private void reiniciarCaminho() {
            caminhoAtivo.clear();
            caminhoAtivo.add(arvore.raiz);
            atual = arvore.raiz;
        }

        private class TreeCanvas extends JPanel {
            TreeCanvas() {
                setBackground(new Color(250, 252, 255));
                int[] ordem = {0};
                posicionar(arvore.raiz, 0, ordem);
                setPreferredSize(new Dimension(
                        Math.max(900, ordem[0] * HORIZONTAL_GAP + MARGIN * 2),
                        6 * LEVEL_GAP + MARGIN * 2));
            }

            private void posicionar(GraphMorseNode no, int nivel, int[] ordem) {
                if (no == null) {
                    return;
                }
                posicionar(no.getEsquerda(), nivel + 1, ordem);
                posicoes.put(no, new java.awt.Point(
                        MARGIN + ordem[0] * HORIZONTAL_GAP,
                        MARGIN + nivel * LEVEL_GAP));
                ordem[0]++;
                posicionar(no.getDireita(), nivel + 1, ordem);
            }

            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                Graphics2D g = (Graphics2D) graphics.create();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                desenharRamos(g, arvore.raiz);
                desenharNos(g, arvore.raiz);
                g.dispose();
            }

            private void desenharRamos(Graphics2D g, GraphMorseNode no) {
                if (no == null) {
                    return;
                }
                desenharRamo(g, no, no.getEsquerda(), ".");
                desenharRamo(g, no, no.getDireita(), "-");
                desenharRamos(g, no.getEsquerda());
                desenharRamos(g, no.getDireita());
            }

            private void desenharRamo(Graphics2D g, GraphMorseNode pai, GraphMorseNode filho, String simbolo) {
                if (filho == null) {
                    return;
                }
                java.awt.Point inicio = posicoes.get(pai);
                java.awt.Point fim = posicoes.get(filho);
                boolean ativo = caminhoAtivo.contains(pai) && caminhoAtivo.contains(filho);
                g.setColor(ativo ? PATH_COLOR : TREE_COLOR);
                g.setStroke(new BasicStroke(ativo ? 4f : 1.7f,
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.drawLine(inicio.x, inicio.y + NODE_RADIUS, fim.x, fim.y - NODE_RADIUS);
                g.setFont(new Font("SansSerif", Font.BOLD, 14));
                g.setColor(ativo ? PATH_COLOR : new Color(100, 116, 139));
                g.drawString(simbolo, (inicio.x + fim.x) / 2 + (fim.x < inicio.x ? -12 : 8),
                        (inicio.y + fim.y) / 2);
            }

            private void desenharNos(Graphics2D g, GraphMorseNode no) {
                if (no == null) {
                    return;
                }
                java.awt.Point ponto = posicoes.get(no);
                boolean ativo = caminhoAtivo.contains(no);
                g.setColor(new Color(15, 23, 42, 18));
                g.fillOval(ponto.x - NODE_RADIUS + 2, ponto.y - NODE_RADIUS + 3,
                        NODE_RADIUS * 2, NODE_RADIUS * 2);
                g.setColor(ativo ? new Color(209, 250, 229) : Color.WHITE);
                g.fillOval(ponto.x - NODE_RADIUS, ponto.y - NODE_RADIUS,
                        NODE_RADIUS * 2, NODE_RADIUS * 2);
                g.setColor(ativo ? PATH_COLOR : TREE_COLOR);
                g.setStroke(new BasicStroke(ativo ? 3f : 1.7f));
                g.drawOval(ponto.x - NODE_RADIUS, ponto.y - NODE_RADIUS,
                        NODE_RADIUS * 2, NODE_RADIUS * 2);

                String letra = no == arvore.raiz ? "raiz"
                        : (no.getInfo() == ' ' ? "" : String.valueOf(no.getInfo()));
                g.setFont(new Font("SansSerif", Font.BOLD, no == arvore.raiz ? 10 : 15));
                int larguraTexto = g.getFontMetrics().stringWidth(letra);
                g.setColor(TEXT_COLOR);
                g.drawString(letra, ponto.x - larguraTexto / 2, ponto.y + 5);

                desenharNos(g, no.getEsquerda());
                desenharNos(g, no.getDireita());
            }
        }
    }

    public static void main(String[] args) {
        vGraphsArvoreBinariaMorse arvore = new vGraphsArvoreBinariaMorse();
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

        SwingUtilities.invokeLater(arvore::abrirJanela);
    }
}