# `mirna`

Bem-vindo ao projeto **`mirna`**.
 
> Um _framework_ escrito em Java para manipulação de arquivos textos do tipo 
> [_Flat-file_](https://en.wikipedia.org/wiki/Flat-file_database) com suporte
> à subitens e extensão de funcionalidade. A estrutura e conteúdo dos arquivos 
> são mapeados em classes configuradas via anotação. 

**Documentação:**

- [Conceito](#conceito)
    - [Documento](#documento) 
    - [Item](#item)
    - [Linha](#linha)
    - [Campo](#campo)
- [Configurando Documento](#configurao-de-documento) 
    - [`@Document`](#document) 
    - [`@Header`](#header) 
    - [`@Footer`](#footer) 
    - [`@Item`](#item)
- [Configurando Linha](#configurao-de-linha-e-campos) 
    - [`@Line`](#line) 
    - [`@FieldStr`](#fieldstr) 
    - [`@FieldInt`](#fieldint) 
    - [`@FieldDec`](#fielddec) 
    - [`@FieldDtm`](#fielddtm)
- [Parâmetros](#atributos-de-configurao)
    - [`identifier`](#identifier)
    - [`position`](#position)
    - [`length`](#length)
    - [`align`](#align)
    - [`fill`](#fill)
    - [`format`](#format)
    - [`decimal`](#decimal)
    - [`separator`](#separator)
    - [`order`](#order)
- [Em ação](#em-ao)
    - [`Mirna.toText()`](#mirnatotext)
    - [`Mirna.writeDocument()`](#mirnawritedocument)
    - [`Mirna.fromText()`](#mirnafromtext)
    - [`Mirna.readDocument()`](#mirnareaddocument)
- [Estendendo a funcionalidade](#estendendo-a-funcionalidade)
    - [`Converter`](#_interface_-converter)
    - [`@FieldCtm`](#fieldctm)
    - [`converter`](#converter)
- [Avançado](#configurao-de-subitens-e-documentos-complexos)
    - [Subitens](#configurando-subitem-de-linha-com-item)
    - [Documentos complexos](#configurando-documentos-complexos-com-itens-e-subitens)

**Download:**

```xml
<dependencies>
    <dependency>
        <groupId>com.github.ducoral</groupId>
        <artifactId>mirna</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>
```

## Conceito

**mirna** é fundamentada em um conceito de [Documento](#documento). [Documento](#documento) é um
objeto convertível para texto _flat-file_ e é composto por [Linhas](#linha). Uma [Linha](#linha), 
por sua vez, é formada por [Campos](#campo) que são ordenados por determinada posição configurada.

## Documento

O _framework_ trata como **Documento** uma classe configurada com a anotação 
[@Document](#document). Um **Documento** deve conter campos
do tipo [Linha](#linha) anotados com
[@Header](#header), [@Footer](#footer) ou 
[@Item](#item). 

[@Header](#header) e [@Footer](#footer) configuram
[Linha](#linha) que deve ocorrer uma única vez no **Documento**. A primeira e a última, 
respectivamente. Porém, é possível mapear [Linha](#linha) com múltiplas ocorrências declarando
item com tipo `java.util.List<`[Linha](#linha)`>`.

Um **Documento** pode conter várias [Linhas](#linha) de vários tipos. Porém, um mesmo tipo de
[Linha](#linha) não pode estar configurado em mais de uma posição em um mesmo **Documento**.
 
Segue abaixo a declaração de [MyDocument](#mydocument), que será utilizada como exemplo de
**Documento** para ilustrar as funcionalidades descritas na documentação. 

###### MyDocument

```java
@Document
public class MyDocument {

    @Header
    private HeaderLine header;

    @Item
    private List<DetailLine> details;

    @Footer
    private FooterLine footer;

    public MyDocument() { }

    public MyDocument(HeaderLine header, List<DetailLine> details, FooterLine footer) {
        this.header = header;
        this.details = details;
        this.footer = footer;
    }

    // getters and setters
}
``` 

O **Documento** [MyDocument](#mydocument) é composto por um cabeçalho, do tipo [HeaderLine](#headerline),
seguido de uma lista de [DetailLine](#detailline) e por um rodapé do tipo [FooterLine](#footerline).

## Item

Falta definir conceito de Item no documento, que representa posição do documento que pode
gerar uma ou mais linhas de documento texto.

## Linha

**Linha** é a configuração que representa a ocorrência de determinada linha em um 
[Documento](#documento). Uma **Linha** corresponde à uma classe configurada com a anotação 
[@Line](#line), com uma identidade definida no atributo 
[identifier](#identifier) e com
[Campos](#campo) declarados para tipos Java, anotados com 
[@FieldStr](#fieldstr), 
[@FieldInt](#fieldint), 
[@FieldDec](#fielddec),
[@FieldDtm](#fielddtm) ou
[@FieldCtm](#fieldctm). 

[@FieldCtm](#fieldctm) permite extensão de funcionalidade
dando suporte à configuração de [Campo](#campo) para tipo não suportado pelo _framework_,
aceitando por parâmetro implementação personalizada de 
[Converter](#converter), que será a responsável pela
conversão objeto/texto.

Segue abaixo a declaração das classes [HeaderLine](#headerline), [DetailLine](#detailline) 
e [FooterLine](#footerline), correspondentes às **Linhas** configuradas em [MyDocument](#mydocument)
e que serão utilizadas como exemplo de **Linha** na ilustração de funcionalidades descritas 
na documentação. 

##### HeaderLine

```java
@Line(identifier = "H")
public class HeaderLine {

    @FieldStr(position = 1, length = 14)
    private String fieldStr;

    @FieldInt(position = 2, length = 5, fill = '0')
    private int fieldInt;

    public HeaderLine() { }

    public HeaderLine(String fieldStr, int fieldInt) {
        this.fieldStr = fieldStr;
        this.fieldInt = fieldInt;
    }
    
    // getters and setters
}
```
  
##### DetailLine

```java
@Line(identifier = "D")
public class DetailLine {

    @FieldStr(position = 1, length = 4)
    private String fieldStr;

    @FieldInt(position = 2, length = 5, fill = '0')
    private int fieldInt;

    @FieldDec(position = 3, length = 10, fill = '0')
    private BigDecimal fieldDec;

    public DetailLine() { }

    public DetailLine(String fieldStr, int fieldInt, BigDecimal fieldDec) {
        this.fieldStr = fieldStr;
        this.fieldInt = fieldInt;
        this.fieldDec = fieldDec;
    }

    // getters and setters
}
```

##### FooterLine

```java
@Line(identifier = "F")
public class FooterLine {

    @FieldDtm(position = 1)
    private Date fieldDtm;

    @FieldCtm(position = 2, length = 11, align = Align.RIGHT, converter = ColorConverter.class)
    private Color fieldCtm;

    public FooterLine() { }

    public FooterLine(Date fieldDtm, Color fieldCtm) {
        this.fieldDtm = fieldDtm;
        this.fieldCtm = fieldCtm;
    }

    // getters and setters
}
```

## Campo

**Campo** correspondente ao trecho de uma linha de um [Documento](#documento), com posições
inicial e final fixas. O que define um **Campo** é a declaração de campo para um tipo Java
em uma [Linha](#linha), anotada com 
[@FieldStr](#fieldstr), 
[@FieldInt](#fieldint), 
[@FieldDec](#fielddec),
[@FieldDtm](#fielddtm) ou
[@FieldCtm](#fieldctm). 
 
Um **Campo** correspondente à um trecho fixo de determinada [Linha](#linha), porém, nesse 
espaço reservado, o valor poderá estar formatado à esquerda ou à direita, ter espaço vazio
preenchido com determinado caractere configurado, etc, conforme configuração das propriedades: 
[position](#position), 
[length](#length),
[align](#align),
[fill](#fill),
[format](#format),
[decimal](#decimal) e
[separator](#separator).

## Relatório de configuração

> **mirna** contém o método estático `Mirna.report` que imprime no _console_ o relatório 
> de configuração de um [Documento](#documento), da classe correspondente especificada
> por parâmetro.

Segue abaixo a saída da execução de `Mirna.report(MyDocument.class)`:

```
              _
    _ __ ___ (_)_ __ _ __   __ _
   | '_ ` _ \| | '__| '_ \ / _` |
   | | | | | | | |  | | | | (_| |
   |_| |_| |_|_|_|  |_| |_|\__,_|
   :: flat-file parser ::  (v1.0)

=== configuration report ==========

com.github.ducoral.mirna.sample.MyDocument document
    com.github.ducoral.mirna.sample.HeaderLine header
    com.github.ducoral.mirna.sample.DetailLine list<item>
    com.github.ducoral.mirna.sample.FooterLine footer

+----------------------------------------------------------------------------+
| com.github.ducoral.mirna.sample.HeaderLine                                 |
+------------+------+----+-----+------+-------+--------+-----+------+--------+
| field      | from | to | len | fill | align | format | dec | sep  | value  |
+------------+------+----+-----+------+-------+--------+-----+------+--------+
| identifier |    1 |  1 |   1 | '\0' | LEFT  |        |   0 | '\0' | H      |
+------------+------+----+-----+------+-------+--------+-----+------+--------+
| fieldStr   |    2 | 15 |  14 | ' '  | LEFT  |        |   0 | '\0' | String |
+------------+------+----+-----+------+-------+--------+-----+------+--------+
| fieldInt   |   16 | 20 |   5 | '0'  | RIGHT |        |   0 | '\0' | int    |
+------------+------+----+-----+------+-------+--------+-----+------+--------+

+--------------------------------------------------------------------------------+
| com.github.ducoral.mirna.sample.DetailLine                                     |
+------------+------+----+-----+------+-------+--------+-----+------+------------+
| field      | from | to | len | fill | align | format | dec | sep  | value      |
+------------+------+----+-----+------+-------+--------+-----+------+------------+
| identifier |    1 |  1 |   1 | '\0' | LEFT  |        |   0 | '\0' | D          |
+------------+------+----+-----+------+-------+--------+-----+------+------------+
| fieldStr   |    2 |  5 |   4 | ' '  | LEFT  |        |   0 | '\0' | String     |
+------------+------+----+-----+------+-------+--------+-----+------+------------+
| fieldInt   |    6 | 10 |   5 | '0'  | RIGHT |        |   0 | '\0' | int        |
+------------+------+----+-----+------+-------+--------+-----+------+------------+
| fieldDec   |   11 | 20 |  10 | '0'  | RIGHT |        |   2 | '\0' | BigDecimal |
+------------+------+----+-----+------+-------+--------+-----+------+------------+

+-----------------------------------------------------------------------------+
| com.github.ducoral.mirna.sample.FooterLine                                  |
+------------+------+----+-----+------+-------+----------+-----+------+-------+
| field      | from | to | len | fill | align | format   | dec | sep  | value |
+------------+------+----+-----+------+-------+----------+-----+------+-------+
| identifier |    1 |  1 |   1 | '\0' | LEFT  |          |   0 | '\0' | F     |
+------------+------+----+-----+------+-------+----------+-----+------+-------+
| fieldDtm   |    2 |  9 |   8 | '\0' | LEFT  | ddMMyyyy |   0 | '\0' | Date  |
+------------+------+----+-----+------+-------+----------+-----+------+-------+
| fieldCtm   |   10 | 20 |  11 | ' '  | LEFT  |          |   0 | '\0' | Color |
+------------+------+----+-----+------+-------+----------+-----+------+-------+
``` 

## Configuração de Documento

### `@Document`

> Configurando classe [Documento](#documento).

Determinado POJO (classe Java) que pretende ser tratado como um [Documento](#documento) deve 
estar anotado com [@Document](#documento). 

Exemplo:
```java
@Document
public class MyDocument {
    // configuração de linhas
}
```

> A partir do exemplo, escrita e leitura de um objeto MyDocument pode ser feita da sequinte forma:

```java
public class Main {
    public static void main(String... args) {
        // converting to text
        Mirna.writeDocument(new MyDocument(), new FileWriter("/path/flat-file.txt"));
        // or
        String text = Mirna.toText(new MyDocument());
        
        // converting from text
        MyDocument document = Mirna.readDocument(MyDocument.class, new FileReader("/path/flat-file.txt"));
        // or
        MyDocument document = Mirna.fromText(MyDocument.class, "flat file text");
    }
}
```

### `@Header`

> Configurando cabeçalho do [Documento](#documento).

A anotação **@Header** configura a linha que deverá ocorrer primeiro e uma única 
vez no [Documento](#documento). 

O campo do [Documento](#documento) anotado com **@Header** deve ser uma 
classe anotada com [@Line](#line).

### `@Footer`

> Configurando rodapé do [Documento](#documento).

A anotação **@Footer** configura a linha que deverá ocorrer uma única vez e por 
último no [Documento](#documento).

O campo do [Documento](#documento) anotado com **@Footer** deve ser uma [Linha](#linha). 

### `@Item`

> Configurando itens do [Documento](#documento).

Configura qualquer outra [Linha](#linha) do [Documento](#documento) que não seja
um [`@Header`](#header) ou um 
[`@Footer`](#footer), e que poderá ocorrer no 
documento uma única ou múltiplas vezes, conforme o tipo utilizado na declaração
do campo na classe. 

Não há limites para a quantidade de [Linhas](#linha) anotadas com `@Item`. Para
configurar uma linha para aceitar múltiplas ocorrências, declare o campo com
o tipo `java.util.List<`[`Linha`](#linha)`>`, conforme exemplo abaixo:

```java
public class MyDocument {
    
    @Item(order = 1)
    private MyLineType1 lineType1;
    
    @Item(order = 2)
    private List<MyLineType2> linesType2;
}
```

> O atributo opcional [`order`](#order) 
> permite configurar a ordem em que determinada linha deverá ser escrita, em relação 
> às outras linhas do [Documento](#documento), quando houver mais de uma linha anotada com
> `@Item`.

## Configuração de Linha e Campos

### `@Line`

> Configurando [Linha](#linha).

A anotação `@Line` deve ser utilizada para configurar determinada classe para ser tratada 
como uma [Linha](#linha) pelo _framework_. Essa configuração requer valor para o atributo
[`identifier`](#identifier), pois, é necessário para 
identificação do tipo da [Linha](#linha) quando representada em texto.

Uma classe anotada com `@Line` deve ter, obrigatoriamente, um construtor _default_. 
Segundo a especificação [8.8.9. Default Constructor](https://docs.oracle.com/javase/specs/jls/se8/html/jls-8.html#jls-8.8.9) 
do Java, um construtor _default_ será gerado pelo compilador automaticamente se não
houver nenhum outro declarado na classe implementada. Portanto, nesse caso, deixar 
a classe sem nenhum construtor está OK também.

A classe, portanto, pode estar sem construtora:

 ```java
@Line(identifier = "id")
public class MyLine {

    // field declarations
}
```

Caso o contrário, deverá ter uma construtora _default_ declarada, se houver outra com parâmetros:

 ```java
@Line(identifier = "id")
public class MyLine {
    
    // default constructor required
    public MyLine() {
    }
   
    // constructor with params
    public MyLine(String param1, Integer param2) {
    }

    // field declarations
}
```

### `@FieldStr`

> Configurando [Campo](#campo) para tipo _string_.

A anotação `@FieldStr` é utilizada para configurar campos _string_, dando 
suporte para os tipos Java `char`, `Character` e `String`.

Requer | Opcional
-------|---------
[`position`](#position) [`length`](#length) | [`align`](#align) [`fill`](#fill)

### `@FieldInt`

> Configurando [Campo](#campo) para tipo inteiro.

A anotação `@FieldInt` é utilizada para configurar campos inteiros, dando 
suporte para os tipos Java `byte`, `short`, `int`, `long`, `Byte`, `Short`,
`Integer`, `Long` e `BigInteger`.

Requer | Opcional
-------|---------
[`position`](#position) [`length`](#length) | [`align`](#align) [`fill`](#fill)

### `@FieldDec`

> Configurando [Campo](#campo) para tipo decimal.

A anotação `@FieldDec` é utilizada para configurar campos decimais, dando 
suporte para os tipos Java `float`, `double`, `Float`, `Double`, e `BigDecimal`.

Requer | Opcional
-------|---------
[`position`](#position) [`length`](#length) | [`align`](#align) [`decimals`](#decimal) [`fill`](#fill) [`separator`](#separator)

### `@FieldDtm`

> Configurando [Campo](#campo) para tipo data.

A anotação `@FieldDtm` é utilizada para configurar campos de data, dando 
suporte para o tipo Java `java.util.Date`.

Requer | Opcional
-------|---------
[`position`](#position)| [`format`](#format)

## Atributos de configuração

### `identifier`

> Configurando identificador de [Linha](#linha).

O valor especificado em `identifier` será utilizado pelo _framework_ para
identificar o tipo da linha quando estiver no formato texto. O identificador
é o primeiro valor no texto da linha. 

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

Quando o _framework_ efetuar a conversão de texto para objeto, ao 
identificar que determinada linha começa com o literal `myidvalue`, ele
saberá que deverá ser criada uma instância de `MyLine` para carregar os
campos dessa linha.

O valor de `identifier` pode ser qualquer _string_.

Tipo | Valor | Utilizado por 
-----|-------|--------------
`String` | qualquer _string_ | `@Line`

### `position`

> Configurando posição do [Campo](#campo).

Configurando posição do campo na [Linha](#linha).

Ao converter os [Campos](#campo) de determinada [Linha](#linha) para texto, o
_framework_ iniciará uma _string_ com o literal configurado em 
[`identifier`](#identifier) e, para cada campo 
declarado na [Linha](#linha), seguindo a ordem crescente do valor configurado
em `position`, a partir da `position` com valor `1`, irá concatenar à direita dessa _string_
o valor do campo convertido para texto, conforme a sua configuração.

> Em vez de ter essa configuração, o _framework_ poderia apenas seguir a ordem de
> cima para baixo em que os campos fossem declarados na classe. Todavia, a especificação
> do Java não define, ou garante, a ordem em que os campos recuperados de uma
> classe via _reflection_ são retornados. Dessa forma essa abordagem ficaria 
> dependente da implementação do JDK. A configuração 
> [`order`](#order) existe pelo mesmo
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

Ao converter para texto eles serão concatenados conforme a ordem configurada
em `position`, do menor para o maior, da seguinte forma:

```
    "A<intF_value><decF_value><strF_value>"
     ||           |           | 
     ||           |           +--> position = 3                                          
     ||           +--------------> position = 2
     |+--------------------------> position = 1
     +---------------------------> identifier (position = 0)
```

Tipo | Valor | Utilizado por 
-----|-------|--------------
`int` | `1`, `2`, ..., `Integer.MAX_VALUE` | `@FieldStr` `@FieldInt` `@FieldDec` `@FieldDtm` `@FieldCtm`

### `length`

> Configurando comprimento do [Campo](#campo).

Um [Campo](#campo) corresponde a um trecho de texto da linha em determinada posição inicial e final, conforme
valor configurado em [`position`](#position), e comprimento em `length`.

`length` define a largura em caracateres que o campo irá reservar no texto da linha para o valor formatado.    

Tipo | Valor | Utilizado por 
-----|-------|--------------
`int` | `1`, `2`, ..., `Integer.MAX_VALUE` | `@FieldStr` `@FieldInt` `@FieldDec` `@FieldCtm`

### `align`

> Configurando alinhamento do valor do [Campo](#campo).

A propriedade `align` define se a _string_ resultante da conversão do valor do campo
para texto ficará posicionada à esquerda ou à direita no espaço reservado de 
determinada linha.

Por exemplo, se um campo _string_ com valor `"abc"`, configurado com comprimento 
`10` e preenchimento `'*'`, será convertido para texto como o seguinte, conforme
configuração de alinhamento:

```
                1234567890
Align.LEFT  -> "abc*******"
Align.RIGHT -> "*******abc"
``` 

Tipo | Valor | _Default_ | Utilizado por 
-----|-------|-----------|--------------
`com.github.ducoral.Align` | `LEFT`, `RIGHT` | `RIGHT` nos campos numéricos e `LEFT` nos demais | `@FieldStr` `@FieldInt` `@FieldDec` `@FieldDtm` `@FieldCtm`
> 

### `fill`

Configurando preenchimento do [Campo](#campo).

Provavelmente a _string_ resultante da conversão do valor do campo terá comprimento menor que o configurado
em [`length`](#length). Nesse caso, o _framework_ irá concatenar
o valor definido em `fill` ao valor do campo até que a _string_ atinja o comprimento determinado.

Tipo | Valor | _Default_ | Utilizado por 
-----|-------|-----------|--------------
`char` | qualquer _char_ | `' '` | `@FieldStr` `@FieldInt` `@FieldDec` `@FieldCtm`

### `format`

> Configurando formato de [Campo](#campo) data.

Define o formato de data para campo com valor do tipo `java.util.Date`. 

Tipo | Valor | _Default_ | Utilizado por 
-----|-------|-----------|--------------
`String` | `"dd/MM/yyyy"`, `"ddMMyy"`, padrão `SimpleDateFormat` | `"ddMMyyyy"` | `@FieldDtm`

### `decimal`

> Configurando precisão de [Campo](#campo) decimal.  

Define a quantidade de casas decimais aplicada na formatação do valor de 
determinado campo decimal.

Tipo | Valor | _Default_ | Utilizado por 
-----|-------|-----------|--------------
`int` | `1`, `2`, ..., mais do que `6` não é possível que precise | `2` | `@FieldDec`

### `separator`

> Configurando separador para [Campo](#campo) decimal.

Define o _char_ que será utilizado para separar as casas decimais na formatação do valor de
determinado campo decimal. O valor `'\0'` indica que não será utilizado separador.

Tipo | Valor | _Default_ | Utilizado por 
-----|-------|-----------|--------------
`char` | `'.'`, `','`, etc. | `'\0'` | `@FieldDec`

### `order`

> Configurando ordem de [Item](#item) no [Documento](#documento).

Define a ordem em que a [Linha](#linha) corrrespondente de determinado _item_ será 
gerado no arquivo em relação aos demais itens do [Documento](#documento). 

Ao converter determinado [Documento](#documento) em arquivo texto, o _framework_ esreverá
primeiro a [Linha](#linha) correspondente do _item_ anotado com [@Header](#header),
se existir, depois as linhas dos itens anotados com [@Item](#item)
e, por último, a linha do _item_ anotado com [@Footer](#footer).

Tipo | Valor | _Default_ | Utilizado por 
-----|-------|-----------|--------------
`int` | `1`, `2`, ..., `Integer.MAX_VALUE` | `0` | `@Item`

## Em ação

A conversão de objeto para texto e vice-versa é efetuada através dos métodos estáticos 
`Mirna.toText()`, `Mirna.writeDocument()`, `Mirna.fromText()` e `Mirna.readDocument()`.

Segue abaixo declaração de instância de [MyDocument](#mydocument), configurada com instâncias de
[HeaderLine](#headerline), [DetailLine](#detailline) e [FooterLine](#footerline) para ser utilizada
como caso para explicação abaixo dos métodos `Mirna.toText()` e `Mirna.writeDocument()`.

###### myDoc 

```java
MyDocument myDoc = new MyDocument(
    new HeaderLine("header", 123),
    Arrays.asList(
            new DetailLine("str1", 10, BigDecimal.valueOf(1.23)),
            new DetailLine("str2", 20, BigDecimal.valueOf(4.56)),
            new DetailLine("str3", 30, BigDecimal.valueOf(7.89))),
    new FooterLine(new GregorianCalendar(2020, Calendar.APRIL, 10).getTime(), Color.MAGENTA)
);
```

### `Mirna.toText()`

> Convertendo documento para texto.

O método `Mirna.toText()` recebe por parâmetro uma instância de [Documento](#documento) e retorna uma
_string_ correspondente ao contendo do arquivo texto gerado a partir dos dados do objeto. 
Para a instância de [MyDocument](#mydocument) declarada acima na variável `myDoc`, ao executar
o trecho de código abaixo:

```java
System.out.println(Mirna.toText(myDoc));
```

Seria impresso o seguinte texto no _console_:

```
Hheader        00123
Dstr1000100000000123
Dstr2000200000000456
Dstr3000300000000789
F10042020  255:0:255
```

### `Mirna.writeDocument()`.

> Escrevendo em documento texto.

O método recebe por parâmetro uma instância de [Documento](#documento), que será convertida para texto,
e uma instância de `java.io.Writer`, em que o texto resultante será escrito.

> Para escrever o documento em arquivo, basta atribuir uma instância de `java.io.FileWriter` ao
> parâmetro `writer`.

Para a instância de [MyDocument](#mydocument) declarada acima na variável `myDoc`, ao executar
o trecho de código abaixo:

```java
Mirna.writeDocument(myDoc, new PrintWriter(System.out));
```

Seria impresso o seguinte texto no _console_:

```
Hheader        00123
Dstr1000100000000123
Dstr2000200000000456
Dstr3000300000000789
F10042020  255:0:255
```

### `Mirna.fromText()`.

> Convertendo texto para [Documento](#documento).

O método `Mirna.fromText()` recebe por parâmetro uma _string_ correspondente ao conteúdo em texto
do documento e retorna instância do objeto configurado com os dados carregados.

Por exemplo, se fosse executado o trecho de código abaixo:

```java
String text =
    "Hheader        00123\n" +
    "Dstr1000100000000123\n" +
    "Dstr2000200000000456\n" +
    "Dstr3000300000000789\n" +
    "F10042020  255:0:255\n";

MyDocument myDocFromText = Mirna.fromText(MyDocument.class, text);
``` 

A instância de [MyDocument](#mydocument) resultante atribuída na variável `myDocFromText` teria a 
mesma configuração da instância configurada na variável [myDoc](#mydoc).

### `Mirna.readDocument()`.

> Lendo [Documento](#documento) a partir de texto.

O método `Mirna.readDocument()` recebe por parâmetro uma instância de `java.lang.Class`, correspondente ao
[Documento](#documento) que será recuperado, e uma instância de `java.io.Reader`, de onde o _framework_
fará a leitura do texto a ser convertido para objeto.

> Para efetuar leitura diretamente de arquivo, basta atribuir uma instância de `java.io.FileReader`
> ao parâmetro `reader`. 

Por exemplo, se fosse executado o seguinte trecho de código:

```java
String text =
    "Hheader        00123\n" +
    "Dstr1000100000000123\n" +
    "Dstr2000200000000456\n" +
    "Dstr3000300000000789\n" +
    "F10042020  255:0:255\n";

MyDocument myDocFromReader = Mirna.readDocument(MyDocument.class, new StringReader(text));
```

A instância de [MyDocument](#mydocument) resultante atribuída na variável `myDocFromReader` teria a 
mesma configuração da instância configurada na variável [myDoc](#mydoc).

## Configuração de subitens e Documentos complexos

**mirna** permite configurar [Linha](#linha) que contenha outra linha relacionada como subitem. Não há
limites na quantidade de subitens para configuração de [Linhas](#linha). 

### Configurando subitem de Linha com @Item

> O subitem pode ser uma única instância como poder uma lista de subitens declarando
> o campo da classe com `List`, da seguinte forma: `List<TipoSubitem> subitens;`

Segue abaixo exemplo de [Linha](#linha) que configura subitem anotando determinado campo com @Item:

###### WithSubItemLine
```java
@Line(identifier = "S")
public class WithSubItemLine {

    @FieldStr(position = 1, length = 15)
    private String fieldStr;

    @FieldInt(position = 2, length = 4)
    private int fieldInt;

    @Item
    private List<DetailLine> details;

    public WithSubItemLine() { }

    public WithSubItemLine(String fieldStr, int fieldInt, List<DetailLine> details) {
        this.fieldStr = fieldStr;
        this.fieldInt = fieldInt;
        this.details = details;
    }

    // getters and setters
}
``` 

O campo `details`, declarado como `List<`[`DetailLine`](#detailline)`>`, está anotado com `@Item`, que é a mesma anotação
utilizada para configurar itens no [Documento](#documento). 

O tipo `DetailLine` é uma [Linha](#linha), como pode ser observado na declaração abaixo:

### Configurando Documentos complexos com itens e subitens

Documentos complexos podem ser elaborados compondo items que contém subitens. 

Segue exemplo [Documento](#documento) complexo declarado como `MyComplexDocument`:

###### MyComplexDocument

```java
@Document
public class MyComplexDocument {

    @Header
    private HeaderLine header;

    @Item(order = 1)
    private List<WithSubItemLine> itemsWithDetails;

    @Item(order = 2)
    private List<AnotherLine> anotherLines;

    @Footer
    private FooterLine footer;

    public MyComplexDocument() { }

    public MyComplexDocument(
            HeaderLine header,
            List<WithSubItemLine> itemsWithDetails,
            List<AnotherLine> anotherLines,
            FooterLine footer) {
        this.header = header;
        this.itemsWithDetails = itemsWithDetails;
        this.anotherLines = anotherLines;
        this.footer = footer;
    }

    // getters and setters
}
```

`AnotherLine` também contém subitem, como pode ser observado em sua declaração abaixo:

###### AnotherLine

```java
@Line(identifier = "A")
public class AnotherLine {

    @FieldDtm(position = 1, format = "yyyyMMdd")
    private Date fieldDtm;

    @FieldDec(position = 2, length = 11, separator = '.', decimals = 4)
    private BigDecimal fieldDec;

    @Item
    private ItemLine item;

    public AnotherLine() { }

    public AnotherLine(Date fieldDtm, BigDecimal fieldDec, ItemLine item) {
        this.fieldDtm = fieldDtm;
        this.fieldDec = fieldDec;
        this.item = item;
    }

    // getters and setters
}
```

O subitem `item` é também uma [Linha](#linha), conforme declaração abaixo:

###### ItemLine

```java
@Line(identifier = "I")
public class ItemLine {

    @FieldStr(position = 1, length = 7, fill = '*')
    String fieldStr;

    @FieldInt(position = 2, length = 3, fill = '0')
    int fieldInt;

    @FieldDec(position = 3, length = 9, fill = '0', decimals = 4, separator = ',')
    BigDecimal fieldDec;

    public ItemLine() { }

    public ItemLine(String fieldStr, int fieldInt, BigDecimal fieldDec) {
        this.fieldStr = fieldStr;
        this.fieldInt = fieldInt;
        this.fieldDec = fieldDec;
    }

    // getters and setters
}
```

Ao executar o relatório de configuração para a classe [MyComplexDocument](#mycomplexdocument),
`Mirna.report(MyComplexDocument.class)`, seria impresso no console o seguinte texto:

```
              _
    _ __ ___ (_)_ __ _ __   __ _
   | '_ ` _ \| | '__| '_ \ / _` |
   | | | | | | | |  | | | | (_| |
   |_| |_| |_|_|_|  |_| |_|\__,_|
   :: flat-file parser ::  (v1.0)

=== configuration report ==========

com.github.ducoral.mirna.sample.MyComplexDocument document
    com.github.ducoral.mirna.sample.HeaderLine header
    com.github.ducoral.mirna.sample.WithSubItemLine list<item>
        com.github.ducoral.mirna.sample.DetailLine list<item>
    com.github.ducoral.mirna.sample.AnotherLine list<item>
        com.github.ducoral.mirna.sample.ItemLine item
    com.github.ducoral.mirna.sample.FooterLine footer

+----------------------------------------------------------------------------+
| com.github.ducoral.mirna.sample.HeaderLine                                 |
+------------+------+----+-----+------+-------+--------+-----+------+--------+
| field      | from | to | len | fill | align | format | dec | sep  | value  |
+------------+------+----+-----+------+-------+--------+-----+------+--------+
| identifier |    1 |  1 |   1 | '\0' | LEFT  |        |   0 | '\0' | H      |
+------------+------+----+-----+------+-------+--------+-----+------+--------+
| fieldStr   |    2 | 15 |  14 | ' '  | LEFT  |        |   0 | '\0' | String |
+------------+------+----+-----+------+-------+--------+-----+------+--------+
| fieldInt   |   16 | 20 |   5 | '0'  | RIGHT |        |   0 | '\0' | int    |
+------------+------+----+-----+------+-------+--------+-----+------+--------+

+----------------------------------------------------------------------------+
| com.github.ducoral.mirna.sample.WithSubItemLine                            |
+------------+------+----+-----+------+-------+--------+-----+------+--------+
| field      | from | to | len | fill | align | format | dec | sep  | value  |
+------------+------+----+-----+------+-------+--------+-----+------+--------+
| identifier |    1 |  1 |   1 | '\0' | LEFT  |        |   0 | '\0' | S      |
+------------+------+----+-----+------+-------+--------+-----+------+--------+
| fieldStr   |    2 | 16 |  15 | ' '  | LEFT  |        |   0 | '\0' | String |
+------------+------+----+-----+------+-------+--------+-----+------+--------+
| fieldInt   |   17 | 20 |   4 | ' '  | RIGHT |        |   0 | '\0' | int    |
+------------+------+----+-----+------+-------+--------+-----+------+--------+

+--------------------------------------------------------------------------------+
| com.github.ducoral.mirna.sample.DetailLine                                     |
+------------+------+----+-----+------+-------+--------+-----+------+------------+
| field      | from | to | len | fill | align | format | dec | sep  | value      |
+------------+------+----+-----+------+-------+--------+-----+------+------------+
| identifier |    1 |  1 |   1 | '\0' | LEFT  |        |   0 | '\0' | D          |
+------------+------+----+-----+------+-------+--------+-----+------+------------+
| fieldStr   |    2 |  5 |   4 | ' '  | LEFT  |        |   0 | '\0' | String     |
+------------+------+----+-----+------+-------+--------+-----+------+------------+
| fieldInt   |    6 | 10 |   5 | '0'  | RIGHT |        |   0 | '\0' | int        |
+------------+------+----+-----+------+-------+--------+-----+------+------------+
| fieldDec   |   11 | 20 |  10 | '0'  | RIGHT |        |   2 | '\0' | BigDecimal |
+------------+------+----+-----+------+-------+--------+-----+------+------------+

+----------------------------------------------------------------------------------+
| com.github.ducoral.mirna.sample.AnotherLine                                      |
+------------+------+----+-----+------+-------+----------+-----+------+------------+
| field      | from | to | len | fill | align | format   | dec | sep  | value      |
+------------+------+----+-----+------+-------+----------+-----+------+------------+
| identifier |    1 |  1 |   1 | '\0' | LEFT  |          |   0 | '\0' | A          |
+------------+------+----+-----+------+-------+----------+-----+------+------------+
| fieldDtm   |    2 |  9 |   8 | '\0' | LEFT  | yyyyMMdd |   0 | '\0' | Date       |
+------------+------+----+-----+------+-------+----------+-----+------+------------+
| fieldDec   |   10 | 20 |  11 | ' '  | RIGHT |          |   4 | '.'  | BigDecimal |
+------------+------+----+-----+------+-------+----------+-----+------+------------+

+--------------------------------------------------------------------------------+
| com.github.ducoral.mirna.sample.ItemLine                                       |
+------------+------+----+-----+------+-------+--------+-----+------+------------+
| field      | from | to | len | fill | align | format | dec | sep  | value      |
+------------+------+----+-----+------+-------+--------+-----+------+------------+
| identifier |    1 |  1 |   1 | '\0' | LEFT  |        |   0 | '\0' | I          |
+------------+------+----+-----+------+-------+--------+-----+------+------------+
| fieldStr   |    2 |  8 |   7 | '*'  | LEFT  |        |   0 | '\0' | String     |
+------------+------+----+-----+------+-------+--------+-----+------+------------+
| fieldInt   |    9 | 11 |   3 | '0'  | RIGHT |        |   0 | '\0' | int        |
+------------+------+----+-----+------+-------+--------+-----+------+------------+
| fieldDec   |   12 | 20 |   9 | '0'  | RIGHT |        |   4 | ','  | BigDecimal |
+------------+------+----+-----+------+-------+--------+-----+------+------------+

+-----------------------------------------------------------------------------+
| com.github.ducoral.mirna.sample.FooterLine                                  |
+------------+------+----+-----+------+-------+----------+-----+------+-------+
| field      | from | to | len | fill | align | format   | dec | sep  | value |
+------------+------+----+-----+------+-------+----------+-----+------+-------+
| identifier |    1 |  1 |   1 | '\0' | LEFT  |          |   0 | '\0' | F     |
+------------+------+----+-----+------+-------+----------+-----+------+-------+
| fieldDtm   |    2 |  9 |   8 | '\0' | LEFT  | ddMMyyyy |   0 | '\0' | Date  |
+------------+------+----+-----+------+-------+----------+-----+------+-------+
| fieldCtm   |   10 | 20 |  11 | ' '  | RIGHT |          |   0 | '\0' | Color |
+------------+------+----+-----+------+-------+----------+-----+------+-------+
```

Considerando o exemplo de determinada variável `myComplexDoc` do tipo [MyComplexDocument](#mycomplexdocument) 
contendo a instância:

###### myComplexDoc

```java
MyComplexDocument myComplexDoc = new MyComplexDocument(
        new HeaderLine("header", 123),
        Arrays.asList(
                new WithSubItemLine(
                        "item1", 10, Arrays.asList(
                        new DetailLine("sub1", 10, BigDecimal.valueOf(1.23)),
                        new DetailLine("sub2", 20, BigDecimal.valueOf(4.56)),
                        new DetailLine("sub3", 30, BigDecimal.valueOf(7.89)))),
                new WithSubItemLine(
                        "item2", 20, Arrays.asList(
                        new DetailLine("sub4", 40, BigDecimal.valueOf(1.23)),
                        new DetailLine("sub5", 50, BigDecimal.valueOf(4.56)),
                        new DetailLine("sub6", 60, BigDecimal.valueOf(7.89))))),
        Arrays.asList(
                new AnotherLine(
                        new GregorianCalendar(2020, Calendar.APRIL, 11).getTime(),
                        BigDecimal.valueOf(123.456),
                        new ItemLine("item1", 100, BigDecimal.valueOf(456.789))),
                new AnotherLine(
                        new GregorianCalendar(2020, Calendar.APRIL, 12).getTime(),
                        BigDecimal.valueOf(34.45),
                        new ItemLine("item2", 200, BigDecimal.valueOf(56.78))),
                new AnotherLine(
                        new GregorianCalendar(2020, Calendar.APRIL, 13).getTime(),
                        BigDecimal.valueOf(987.6543),
                        new ItemLine("item3", 300, BigDecimal.valueOf(555.333)))),
        new FooterLine(new GregorianCalendar(2020, Calendar.APRIL, 10).getTime(), Color.MAGENTA));
```

Ao executar a instrução `System.out.println(Mirna.toText(myComplexDoc))`, seria impresso o seguinte
texto no _console_:

```
Hheader        00123
Sitem1            10
Dsub1000100000000123
Dsub2000200000000456
Dsub3000300000000789
Sitem2            20
Dsub4000400000000123
Dsub5000500000000456
Dsub6000600000000789
A20200411   123.4560
Iitem1**1000456,7890
A20200412    34.4500
Iitem2**2000056,7800
A20200413   987.6543
Iitem3**3000555,3330
F10042020  255:0:255
```

## Estendendo a funcionalidade

`mirna` permite a extensão de funcionalidade através de especilização da _interface_ `Converter`, que
pode ser utilizada na configuração de campo personalizado anotado com `@FieldCtm`.

### _Interface_ `Converter`

> Implementando a _interface_ para conversão objeto/texto personalizada. 

A _interface_ `Converter` requer que sejam implementados dois métodos: `Converter.toText()` e
`Converter.fromText()`. O método `Converter.toText()` deverá retornar _string_ correspondente
da conversão para texto do objeto especificado no parâmetro `value`. O método `Converter.fromText()`, 
por sua, deverá retornar instância do objeto personalizado com valores carregados da _string_ 
especificada no parâmetro `text`.

Segue abaixo a declaração de `ColorConverter`, implementação de `Converter` utilizada na configuração
de campo personalizado em [FooterLine](#footerline), que dá suporte a objeto do tipo `java.awt.Color`:

###### ColorConverter

```java
import com.github.ducoral.mirna.Converter;

import java.awt.*;

import static java.lang.Integer.*;

public class ColorConverter implements Converter {

    @Override
    public String toText(Object value) {
        Color color = (Color) value;
        return color.getRed() + ":" + color.getGreen() + ":" + color.getBlue();
    }

    @Override
    public Object fromText(String text) {
        String[] rgb = text.split(":");
        return new Color(parseInt(rgb[0]), parseInt(rgb[1]), parseInt(rgb[2]));
    }
} 
```  

### `@FieldCtm`

> Configurando [Campo](#campo) personalizado.

Dada a implementação de [`Converter`](#_interface_-converter)
para determinado tipo personalizado, basta especificar a classe correspondente ao 
atributo `converter` da anotação `@FieldCtm`, ao configurar o campo de tipo específico, 
da forma com foi utilizado no exemplo [FooterLine](#footerline):

```java
@FieldCtm(position = 2, length = 11, align = Align.RIGHT, converter = ColorConverter.class)
private Color fieldCtm;
```   

### `converter`

> Configurando conversor personalizado.

O atributo `converter`, da anotação `@FieldCtm`, requer a instância de `java.lang.Class` 
correspondente à implementação de `Converter` que deverá ser utilizada pelo _framework_ ao
efetuar a conversão objeto/texto para o tipo declarado no campo personalizado.
