# Árvore Binária de Código Morse

Cada nó é um símbolo do código Morse! Ponto (.) vai para a esquerda e traço (-) vai para a direita. 
O caminho da raiz até um nó é o código da letra ou número.

## Lógica

**`Node`**
Guarda o caractere (`info`) e os filhos `esquerda` e `direita`. 
O campo `coluna` só serve para desenhar a árvore. 
Nó sem letra guarda um espaço.

**`inicializar`**
Cria a raiz vazia.

**`inserir(morse, letra)`**
Começa na raiz e lê o código símbolo por símbolo.
Guarda a letra no último nó.

**`buscar(morse)`**
Devolve a letra do nó onde parou. 
`?` se o caminho não existe ou se o nó não tem letra.

**`buscarMorse(letra)`**
Faz o contrário: acha o código a partir da letra.
 Procura recursivamente pela esquerda e depois pela direita.
 `null` se a letra não existe.

**`traduzir(mensagem)`**
Lê a mensagem e chama `buscar`, adicionando a letra ao resultado. 
As letras da mensagem precisam estar separadas por um espaço.

**`altura(no)`**
Conta os níveis da árvore (serve para saber quantas linhas o desenho precisa).

**`exibir`**
Desenha a árvore em uma matriz de caracteres (`grade`) e imprime. Passos:
1. `calcularColunas` percorre a árvore em ordem (esquerda, nó, direita) e dá a cada nó uma coluna diferente.
2. Cria a grade com `altura * 2 - 1` linhas (um nível usa uma linha para os nós e outra para as ligações) e preenche com espaços.
3. `desenhar` escreve cada nó na sua coluna e liga ao filho com `_` e `/` ou `\`.
Nós sem letra aparecem como `*`.
