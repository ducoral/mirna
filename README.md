# `mirna`

(documentação incompleta)

Bem vindo ao projeto **mirna**.
 
> Um _framework_ simples escrito em Java para manipulação de arquivos textos tipo 
> _flat-file_, em que são utilizadas annotações e classes para mapear a relação
> objeto-texto.
>
> **mirna** utiliza um conceito de [Documento](#documento) que deve conter uma ou 
> mais [Linhas](#linha). Determinada [Linha](#linha), todavia, deve conter um ou 
> mais [Campos](#campo).

## Documento

> Um documento consiste de uma classe anotada com 
> [@Document](#configurando-classe-documento-com-document),
> contendo campos que representam as linhas do documento. Cada campo deve estar 
> anotado com [@Header](#configurando-cabealho-com-header), 
> [@Footer](#configurando-rodap-com-footer) ou [@Item](#configurando-itens-com-item) 
> para configurar a posição e a ocorrência das linhas correspondentes.  
 
Segue abaixo um exemplo de configuração de um documento **mirna**:

```java
@Document
public class Remessa {

    @Header
    private Cabecalho cabecalho;

    @Item
    private List<Titulo> titulos;

    @Footer
    private Rodape rodape;

    // constructor
    // getters
    // setters
}
``` 

## Linha

> Cada linha de um documento é representada por uma classe anotada com 
>[@Line](#configurando-classe-linha-com-line), contendo [Campos](#campo) que representam 
> cada trecho de tamanho fixo do texto da linha do arquivo. Cada [Campo](#campo) deve 
> estar anotado com 
> [@FieldStr](#configurando-campo-_string_-com-fieldstr), 
> [@FieldInt](#configurando-campo-inteiro-com-fieldint), 
> [@FieldDec](#configurando-campo-decimal-com-fielddec),
> [@FieldDtm](#configurando-campo-data-com-fielddtm) 
> ou 
> [@FieldCtm](#configurando-campo-personalizado-com-fieldctm) 
> para configurar como o objeto será convertido para texto.

Segue abaixo a declaração das classes `Cabecalho`, `Titulo` e `Rodape`, 
correspondentes às linhas configuradas no documento acima:

```java
@Line(identifier = "1")
public class Cabecalho {

    @FieldDtm(position = 1, format = "ddMMyy")
    private Date dataCriacao;

    @FieldStr(position = 2, length = 13)
    private String descricao;
    
    // [...] construtores, getters e setters.
}
```
  
```java
@Line(identifier = "2")
public class Titulo {

    @FieldStr(position = 1, length = 14)
    private String cpfCnpj;

    @FieldDec(position = 2, length = 5)
    private BigDecimal valor;

    // [...] construtores, getters e setters.
}
```

```java
@Line(identifier = "3")
public class Rodape {

    @FieldStr(position = 1, length = 15)
    private String texto;

    @FieldInt(position = 2, length = 5)
    private Integer qtdeItens;

    // [...] construtores, getters e setters.
}
```

> A anotação [@FieldCtm](#fieldctm) é utilizada para configurar campos personalizados.
> Para configurar campos personalizados, é necessário implementar a _interface_ 
>[Converter](#personalizando-converso-objetotexto-implementando-interface-converter) 
> e especificar no atributo [converter](#configurando-conversor-personalizado-com-converter).

## Campo

> Especificação

## Relatório de configuração

> **mirna** contém o método estático `Mirna.report` que imprime no _console_ o relatório 
> de configuração de um [Documento](#documento), da classe correspondente especificada
> por parâmetro.

Segue abaixo a saída da execução de `Mirna.report(Remessa.class)`:

```
              _
    _ __ ___ (_)_ __ _ __   __ _
   | '_ ` _ \| | '__| '_ \ / _` |
   | | | | | | | |  | | | | (_| |
   |_| |_| |_|_|_|  |_| |_|\__,_|
   :: flat-file parser ::  (v1.0)

=== Relatório de Configuração =====

Remessa document
    Cabecalho header
    Titulo list<item>
    Rodape footer

+-------------------------------------------+
| Cabecalho                |
+-------------+----+-----+---------+--------+
| Campo       | De | Até | Tamanho | Valor  |
+-------------+----+-----+---------+--------+
| ID da linha |  1 |   1 |       1 | 1      |
+-------------+----+-----+---------+--------+
| dataCriacao |  2 |   7 |       6 | Date   |
+-------------+----+-----+---------+--------+
| descricao   |  8 |  20 |      13 | String |
+-------------+----+-----+---------+--------+

+-----------------------------------------------+
| Titulo                       |
+-------------+----+-----+---------+------------+
| Campo       | De | Até | Tamanho | Valor      |
+-------------+----+-----+---------+------------+
| ID da linha |  1 |   1 |       1 | 2          |
+-------------+----+-----+---------+------------+
| cpfCnpj     |  2 |  15 |      14 | String     |
+-------------+----+-----+---------+------------+
| valor       | 16 |  20 |       5 | BigDecimal |
+-------------+----+-----+---------+------------+

+--------------------------------------------+
| Rodape                    |
+-------------+----+-----+---------+---------+
| Campo       | De | Até | Tamanho | Valor   |
+-------------+----+-----+---------+---------+
| ID da linha |  1 |   1 |       1 | 3       |
+-------------+----+-----+---------+---------+
| texto       |  2 |  15 |      14 | String  |
+-------------+----+-----+---------+---------+
| qtdeItens   | 16 |  20 |       5 | Integer |
+-------------+----+-----+---------+---------+
``` 

## Documentação
- [Configuração de Documento](#configurao-de-documento)
    - [Configurando classe Documento com `@Document`](#configurando-classe-documento-com-document)
    - [Configurando cabeçalho com `@Header`](#configurando-cabealho-com-header)
    - [Configurando rodapé com `@Footer`](#configurando-rodap-com-footer)
    - [Configurando itens com `@Item`](#configurando-itens-com-item)
- [Configuração de Linha e Campos](#configurao-de-linha-e-campos)
    - [Configurando classe Linha com `@Line`](#configurando-classe-linha-com-line)
    - [Configurando campo _string_ com `@FieldStr`](#configurando-campo-_string_-com-fieldstr)
    - [Configurando campo inteiro com `@FieldInt`](#configurando-campo-inteiro-com-fieldint)
    - [Configurando campo decimal com `@FieldDec`](#configurando-campo-decimal-com-fielddec)
    - [Configurando campo data com `@FieldDtm`](#configurando-campo-data-com-fielddtm)
- [Atributos de configuração](#atributos-de-configurao)
    - [Identificando uma linha com `identifier`](#identificando-uma-linha-com-identifier)
    - [Configurando posição do campo com `position`](#configurando-posio-do-campo-com-position)
    - [Configurando largura do campo em texto com `length`](#configurando-comprimento-do-campo-em-texto-com-length)
    - [Configurando alinhamento do valor do campo com `align`](#configurando-alinhamento-do-valor-do-campo-com-align)
    - [Configurando preenchimento do campo com `fill`](#configurando-preenchimento-do-campo-com-fill)
    - [Configurando formato para data com `format`](#configurando-formato-para-data-com-format)
    - [Configurando quantidade de casas decimais com `decimal`](#configurando-quantidade-de-casas-decimais-com-decimal)
    - [Configurando separador de casas decimais com `separator`](#configurando-separador-de-casas-decimais-com-separator)
    - [Configurando ordem do item no documento com `order`](#configurando-ordem-do-item-no-documento-com-order)
- [Escrevendo e lendo documentos](#escrevendo-e-lendo-documentos)
    - [Convertendo documento para texto com `Main.toText()`](#convertendo-documento-para-texto-com-maintotext)
    - [Escrevendo documento para texto com `Main.writeDocument()`](#escrevendo-documento-para-texto-com-mainwritedocument)
    - [Convertendo texto para documento com `Main.fromText()`](#convertendo-texto-para-documento-com-mainfromtext)
    - [Lendo documento a partir de texto com `Main.readDocument()`](#lendo-documento-a-partir-de-texto-com-mainreaddocument)
- [Personalização de configuração](#personalizao-de-configurao)
    - [Personalizando a conversão objeto/texto implementando a interface `Converter`](#personalizando-a-converso-objetotexto-implementando-a-interface-converter)
    - [Configurando campo personalizado com `@FieldCtm`](#configurando-campo-personalizado-com-fieldctm)
    - [Configurando conversor personalizado com `converter`](#configurando-conversor-personalizado-com-converter)
- [Valores `null` e texto vazio](#valores-null-e-textos-vazios)

## Configuração de Documento

### Configurando classe Documento com `@Document`
> Determinado POJO (classe Java) que pretende ser tratado como um 
> [Documento](#documento) deve estar anotado com [@Document](#documento). 

Exemplo:
```java
@Document
public class MyDocument {
    // configuração de linhas
}
```

> A partir do exemplo, um objeto MyFlatFile pode ser convertido para texto ou,
> um texto ser pode convertido para objeto MyFlatFile, da sequinte forma:

```java
public class Main {
    public static void main(String... args) {
        MyDocument myDocument = new MyDocument();
        
        // convertendo objeto para texto
        Mirna.writeDocument(myDocument, new FileWriter("/path/flat-file.txt"));
        String text = Mirna.toText(myDocument);
        
        // convertendo texto para objeto
        myDocument = Mirna.readDocument(MyDocument.class, new FileReader("/path/flat-file.txt"));
        myDocument = Mirna.fromText(MyDocument.class, "flat file text");
    }
}
```

### Configurando cabeçalho com `@Header`

> A anotação **@Header** configura a linha do documento que deverá ocorrer 
> primeiro e uma única vez no Documento. 
>
> O campo do [Documento](#documento) anotado com **@Header** deve ser uma 
> classe anotada com [@Line](#configurando-classe-linha-com-line).

### Configurando rodapé com `@Footer`

> A anotação **@Footer** configura a linha do documento que deverá ocorrer
> uma única vez e por último no Documento.
>
> O campo do [Documento](#documento) anotado com **@Footer** deve ser uma 
> classe anotada com [@Line](#configurando-classe-linha-com-line).

### Configurando itens com `@Item`

> Configura qualquer outra [Linha](#linha) do [Documento](#documento) que não seja
> um [`@Header`](#configurando-cabealho-com-header) ou um 
> [`@Footer`](#configurando-rodap-com-footer), e que poderá ocorrer no 
> documento uma única ou múltiplas vezes, conforme o tipo utilizado na declaração
> do campo na classe. 
>
> Não há limites para a quantidade de [Linhas](#linha) anotadas com `@Item`. Para
> configurar uma linha para aceitar múltiplas ocorrências, declare o campo com
> o tipo `java.util.List<`[`Linha`](#linha)`>`, conforme exemplo abaixo:

```java
public class MyDocument {
    
    @Item(order = 1)
    private MyLineType1 lineType1;
    
    @Item(order = 2)
    private List<MyLineType2> linesType2;
}
```

> O atributo opcional [`order`](#configurando-ordem-do-item-no-documento-com-order) 
> permite configurar a ordem em que determinada linha deverá ser escrita, em relação 
> às outras linhas do [Documento](#documento), quando houver mais de uma linha anotada com
> `@Item`.

## Configuração de Linha e Campos

### Configurando classe Linha com `@Line`

> Anotação utilizada para configurar determinada classe para ser tratada como uma 
> [Linha](#linha) pelo _framework_. Essa configuração requer valor para o atributo
> [`identifier`](#identificando-uma-linha-com-identifier), pois é necessário para 
> identificação do tipo da [Linha](#linha) quando representada em texto.
>
> Uma classe anotada com `@Line` deve ter, obrigatoriamente, um construtor _default_. 
> Segundo a especificação [8.8.9. Default Constructor](https://docs.oracle.com/javase/specs/jls/se8/html/jls-8.html#jls-8.8.9) 
> do Java, um construtor _default_ será gerado pelo compilador automaticamente se não
> houver nenhum construtor declarado na classe implementada pelo usuário. Portanto,
> nesse caso, deixar a classe sem nenhum construtor está OK também.

A classe, portanto, pode estar sem construtora:

 ```java
@Line(identifier = "id")
public class MyLine {

    // field declarations
}
```

Ou deve ter uma construtora _default_ declarada, quando houver outra construtora com
parâmetros definida:

 ```java
@Line(identifier = "id")
public class MyLine {
    
    // Default constructor required
    public MyLine() {
    }
   
    // Constructor with params
    public MyLine(String param1, Integer param2) {
    }

    // field declarations
}
```

### Configurando campo _string_ com `@FieldStr`

> A anotação `@FieldStr` é utilizada para configurar campos _string_, dando 
> suporte para os tipos Java `char`, `Character` e `String`.

> **Configuração obrigatória:** 
> [`position`](#configurando-posio-do-campo-com-position)
> [`length`](#configurando-comprimento-do-campo-em-texto-com-length)
> 
> **Configuração opcional:**
> [`align`](#configurando-alinhamento-do-valor-do-campo-com-align)
> [`fill`](#configurando-preenchimento-do-campo-com-fill)

### Configurando campo inteiro com `@FieldInt`

> A anotação `@FieldInt` é utilizada para configurar campos inteiros, dando 
> suporte para os tipos Java `byte`, `short`, `int`, `long`, `Byte`, `Short`,
> `Integer`, `Long` e `BigInteger`.

> **Configuração obrigatória:** 
> [`position`](#configurando-posio-do-campo-com-position)
> [`length`](#configurando-comprimento-do-campo-em-texto-com-length)
> 
> **Configuração opcional:**
> [`align`](#configurando-alinhamento-do-valor-do-campo-com-align)
> [`fill`](#configurando-preenchimento-do-campo-com-fill)

### Configurando campo decimal com `@FieldDec`

> A anotação `@FieldDec` é utilizada para configurar campos decimais, dando 
> suporte para os tipos Java `float`, `double`, `Float`, `Double`, e `BigDecimal`.

> **Configuração obrigatória:** 
> [`position`](#configurando-posio-do-campo-com-position)
> [`length`](#configurando-comprimento-do-campo-em-texto-com-length)
> 
> **Configuração opcional:**
> [`align`](#configurando-alinhamento-do-valor-do-campo-com-align)
> [`decimals`](#configurando-quantidade-de-casas-decimais-com-decimal)
> [`fill`](#configurando-preenchimento-do-campo-com-fill)
> [`separator`](#configurando-separador-de-casas-decimais-com-separator)

### Configurando campo data com `@FieldDtm`

> A anotação `@FieldDtm` é utilizada para configurar campos de data, dando 
> suporte para o tipo Java `java.util.Date`.

> **Configuração obrigatória:** 
> [`position`](#configurando-posio-do-campo-com-position)
> 
> **Configuração opcional:**
> [`format`](#configurando-formato-para-data-com-format)

## Atributos de configuração

### Identificando uma linha com `identifier`

> O valor especificado em `identifier` será utilizado pelo _framework_ para
> identificar o tipo da linha quando estiver no formato texto. O identificador
> é o primeiro valor no texto da linha. 

Por exemplo, se temos um objeto [Linha](#linha) da sequinte forma:
```java
@Line(identifier = "myidvalue")
public class MyLine {

    // field declarations
}
```

O texto dessa linha sempre começará com o literal `myidvalue`:

```
   "myidvalue<field-1><field-2><field-3>..."
```

> Quando o _framework_ estiver efetuando a conversão de texto para objeto, ao 
> identificar que determinada linha texto começa com o literal `myidvalue`, ele
> saberá que deverá ser criada uma instância de `MyLine` para carregar os
> campos dessa linha.

> O valor de `identifier` pode ser qualquer _string_.

### Configurando posição do campo com `position`

> Ao converter os [Campos](#campo) de determinada [Linha](#linha) para texto, o
> _framework_ iniciará uma _string_ com o literal configurado em 
> [`identifier`](#identificando-uma-linha-com-identifier) e, para cada campo 
> declarado na [Linha](#linha), seguindo a ordem crescente do valor configurado
> em `position`, a partir da `position` com valor `1`, irá concatenar à direita dessa _string_
> o valor do campo convertido para texto, conforme sua configuração.

> Em vez de ter essa configuração, o _framework_ poderia apenas seguir a ordem de
> cima para baixo em que o campos fossem declarados na classe. Porém, a especificação
> do Java não define, ou garante, a ordem em que os campos recuperados de uma
> classe via _reflection_ são retornados. Dessa forma essa implementação ficaria 
>dependente da implementação do JDK. Por isso a necessidade desse atributo. 
>A configuração 
> [`order`](#configurando-ordem-do-item-no-documento-com-order) existe pelo mesmo
> motivo.

Portanto, idependentemente da ordem em que os campos são declarados em uma
[Linha](#linha), por exemplo:

```java
@Line(identifier = "A")
public class MyLine {

    @FieldStr(position = 3, length = 10)
    private String strF;

    @FieldInt(position = 1, length = 5)
    private Integer intF;

    @FieldDec(position = 2, length = 8)
    private BigDecimal decF;

    public MyLine() { }
    
    public MyLine(String strF, Integer intF, BigDecimal decF) {
        this.strF = strF;
        this.intF = intF;
        this.decF = decF;
    }   

    // getters and setters
}
```

Ao converter para texto eles serão concatenados seguindo a ordem configurada
em `position`, do menor para o maior, da seguinte forma:

```
    "A<intF_value><decF_value><strF_value>"
     ||           |           | 
     ||           |           +--> position = 3                                          
     ||           +--------------> position = 2
     |+--------------------------> position = 1
     +---------------------------> identifier (position = 0)
```

### Configurando comprimento do campo em texto com `length`

> sa

### Configurando alinhamento do valor do campo com `align`

### Configurando preenchimento do campo com `fill`

### Configurando formato para data com `format`

### Configurando quantidade de casas decimais com `decimal`

### Configurando separador de casas decimais com `separator`

### Configurando ordem do item no documento com `order`

## Escrevendo e lendo documentos

### Convertendo documento para texto com `Main.toText()`

### Escrevendo documento para texto com `Main.writeDocument()`

### Convertendo texto para documento com `Main.fromText()`

### Lendo documento a partir de texto com `Main.readDocument()`

## Personalização de configuração

## Personalizando a conversão objeto/texto implementando a interface `Converter`

### Configurando campo personalizado com `@FieldCtm`

### Configurando conversor personalizado com `converter`

## Valores `null` e textos vazios