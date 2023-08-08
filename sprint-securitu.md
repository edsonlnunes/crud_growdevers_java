===============================

## Spring Security
O Spring Security possui 3 grandes objetivos, sendo eles:
1. Autenticação
2. Autorização (controle de acesso)
3. Proteção contra ataques (CSRF, clickjacking, etc...)

===============================

## ATENÇÃO

**Autenticação em aplicações WEB (Statefull) != Autenticação em API Rest (Stateless)**

===============================

## JWT Token

O JWT (JSON Web Token) é uma pequena peça de informação que é usada para provar que alguém é quem diz ser. É como um carimbo digital que contém algumas informações sobre uma pessoa ou um sistema.

Imagine que você está em um evento e recebe uma pulseira com um adesivo que tem seu nome e informações importantes. Quando você quer entrar em uma área restrita, você mostra essa pulseira com o adesivo para provar que você é convidado.

Da mesma forma, um JWT é usado em aplicativos de computador e internet para verificar a identidade de alguém. Contém informações como quem é o usuário, quando o JWT foi emitido e quando ele expira. Isso ajuda os sistemas a garantir que você tenha permissão para acessar certas partes ou ações.

De forma geral, o JWT é um tipo de carimbo digital usado para confirmar a identidade de alguém em aplicativos e sistemas online, permitindo acesso seguro a recursos ou ações específicas.

===============================

## 1- Adicionar a dependencia do Spring Security
Ao adicionar a dependencia o Spring automaticamente irá bloquear todas as rotas e ao tentar acessar uma rota
ele vai mostrar uma página para realizar a autenticação. Para fazer login na página default basta utilizar as seguintes credenciais = usuário: user | senha: pegar no log

## 2- Utilizar ou criar uma classe para representar o usuário
É preciso criar ou utilizar uma classe que já existe para representar o usuário. Uma classe para representar o usuário deve ter 2 atributos obrigatórios, sendo eles: **email ou login ou username** e a **senha ou password.**. Por padrão o Spring Security irá procurar pelos atributos **username** e **password**, mas é possível utilizar outros nomes e isso será mostrado mais para frente.

Exemplo: Na aplicação CRUD de Growdevers a classe Growdever representa o usuário do sistema, logo, nao é preciso criar uma classe nova para isso.

Esta classe precisa implementar a interface **UserDetails** para o **SpringSecurity** poder reconher a entidade como sendo um usuário de. Ao implementar esta intercace, voce será obrigado a implementar alguns métodos, sendo eles:

```
@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_USER"));
}

@Override
public String getUsername() {
    return email;
}

@Override
public String getPassword() {
    return password;
}

@Override
public boolean isAccountNonExpired() {
    return true;
}

@Override
public boolean isAccountNonLocked() {
    return true;
}

@Override
public boolean isCredentialsNonExpired() {
    return true;
}

@Override
public boolean isEnabled() {
    return true;
}
```

Com o SpringSecurity é possível realizar diversas regras para dizer se um usuário pode ou não realizar o login, como se o usuário está habilitado, se a conta não está bloqueada ou se não está expirado... esses sao alguns dos exemplos, além de ter um padão para trabalhar com perfis diferentes. No nosso caso, nao vamos ter nenhuma dessas regras, então para esses métodos vamos retornar o valor true de forma fixa e na lista de perfis vamos retornar uma lista fixa também.

Dentre os métodos que a interface **UserDetails** obriga a implementar estão eles: **getUsername** e **getPassword**. Conforme informado acima, o SpringSecurity procura no usuário os atributos **username** e **password** para verificar as credenciais, no entanto, podemos ter outros nomes para os atributos, como **email** e **senha**, ou **login** e **pass**... Para podermos ensinar o Spring Security os valores que precisa utilizar, vamos nesses métodos **getUsername** e **getPassword** retornar os valores dos atributos que queremos. Conforme no exemplo acima, no método **getUsername** está sendo retornado o **email** e no **getPassword** está sendo retornado o **password**. Neste caso do password, como o nome do atributo que estamos utilizando é o mesmo que o SpringSecurity procura, poderiamos remover este método que nao teríamos problemas.

## 3- Criar um Repository para trabalhar com a entidade que representa o usuário
Deve ser criado um repositório para trabalhar com o usuário no banco de dados. Este repositório precisa contar um método que busca o usuário pelo **email ou login ou username** e deve retornar um **UserDetails** que faz parte do Spring Security

Exemplo: Na aplicação CRUD de Growdevers já temos a interface GrowdeverRepository que trabalha com a entidade Growdever. Neste repositório criei um método chamado **findByEmail** para buscar o usuário pelo email e retornar o **UserDetails**
```
UserDetails findByEmail(String email);
```

## 4- Criar uma classe que será utilizada como um serviço
Está classe pode ser chamada de AuthService (pode ser outro nome) e está classe precisa implementar a interface **UserDetailsService**. Esta interface faz parte do Spring Security

Dentro dessa classe deve ser injetado o repositório que buscará as informações do usuário no banco de dados e deve ser implementado o método que a interface **UserDetailsService** obriga a implementar.

Está classe pode ser chamada de **AuthService**, deve ter a anotação **@services** e pode ser criada dentro de uma pasta chamada **services**

Exemplo do CRUD de Growdevers:

```
@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private GrowdeverRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username);
    }
}
```

## 5- Configurando o SpringSecurity
O SpringSecurity já vem com uma configuração default utilizando o padrão de autenticação **statefull**, o que nao faz sentido para uma API Restfull conforme explicado em aula em aula.

A configuração do SpringSecurity deve ser feita através de código e dentro de uma classe, para isso pode ser criado uma classe chamada **SecurityConfigurations** e pode ficar dentro da pasta **config**. Esta classe precisa ter as anotações **@Configuration** e **@EnableWebSecurity**

Está classe deve conter o seguinte método:

```
@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm ->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

O método **securityFilterChain** serve para mudar a configuração do SpringSecurity e a configuração que estamos fazendo é para:

- csrf(csrf -> csrf.disable()): Serve para desabilitar a proteção contra ataques do tipo CSRF (CROSS-SITE REQUEST FORGERY). Esta proteção pode ser desabilitada pois com a autenticação via tokens, o próprio token já é uma proteção contra esses tipos de ataques.

> Um ataque CSRF é como alguém tirando proveito da sua identidade já autenticada em um site para fazer coisas que você não quer, sem que você perceba. Para se proteger, é importante ser cuidadoso ao clicar em links e manter seu software e navegadores atualizados para reduzir o risco de tais ataques.

- .sessionManagement(sm ->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)): Serve para mudarmos a política do SpringSecurity para ser STATELESS, ou seja, cada requisição será tratada como uma nova requisição e cada requisição precisa ter seu meio de mostrar que está autenticado.

> #### API Stateless (Sem Estado):
> Uma API Stateless não guarda informações sobre interações anteriores. Cada solicitação feita para a API contém todas as informações necessárias para que a API entenda e responda. É como conversar com alguém sem que eles se lembrem do que vocês falaram antes. Cada solicitação é independente e autossuficiente.
>
> #### API Stateful (Com Estado):
> Uma API Stateful mantém um registro das interações anteriores. Ela lembra do contexto e das informações das solicitações anteriores. É como conversar com alguém que se lembra do que vocês falaram antes, tornando a comunicação contínua e lembrando de detalhes importantes.
>
> Resumindo, uma API Stateless não guarda informações passadas e cada solicitação é completa por si só, enquanto uma API Stateful mantém informações sobre interações anteriores, criando uma comunicação mais contínua e contextual.

## 6- Criar a controller para realizar o login
Criar um controller para receber a requisição de login e realizar a autenticação do usuário. Dentro deste controller deve ser adicionado o seguinte código:

```
@RestController
@RequestMapping("/login")
public class AuthController {
    @Autowired
    private AuthenticationManager manager;

    @PostMapping
    public ResponseEntity doLogin(@RequestBody @Valid LoginAuth data) {
        var token = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var authentication = manager.authenticate(token);

        return ResponseEntity.ok().build();
    }
}
```

## 7- Tentativa de login
Se você tentar fazer o login passando informações válidas e que existem no banco de dados voce vai ver que o login será realizado com sucesso... porém falta implementar o token de acesso para poder ser enviado nas próximas requisições.

## 8- Adicionar a dependencia do JWT Token
Para gerar o Token JWT é preciso adicionar uma das dependencias que estão disponíveis no site jwt.io

Em aula estamos utilizando a seguinte dependencia:

```
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>4.4.0</version>
</dependency>
```

## 9- Criar o serviço para gerar o Token
Depois de adicionar a dependencia do Jwt Token deve ser criado uma classe para servir de serviço para realizar a geração e a verificação do token.

Esta classe deve ficar dentro da pasta **services**, pode ser chamada de **TokenService** e precisa ter a anotação **@Service**

Você pode adicionar o seguinte código dentro da classe

```
@Service
public class TokenService {

    private String secret = "Growdev@2020";

    public String getToken(Growdever growdever){
        var algorithm = Algorithm.HMAC256(secret);
        var expiresDate = LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
        return JWT.create()
                .withIssuer("API Crud Growdevers")
                .withSubject(growdever.getEmail())
                .withClaim("id", growdever.getId().toString())
                .withExpiresAt(expiresDate)
                .sign(algorithm);
    }

    public String verifyToken(String token){
        var algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("API Crud Growdevers")
                .build()
                .verify(token)
                .getSubject();

    }
}
```

OBS: O método **verifyToken** será abordado mais para frente.

o método **getToken** possui a lógica para gerar o token JWT. Para gerar este token é preciso de algumas coisas, sendo elas:

- `var algorithm = Algorithm.HMAC256(secret);`
> Aqui, um algoritmo de assinatura HMAC com a chave secreta secret é escolhido para assinar o token. Isso garante que o token seja autêntico e seguro.

- `var expiresDate = LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));`
> É calculada uma data de expiração para o token. Neste caso, está sendo definido um prazo de validade de 2 horas a partir do momento atual. A zona horária utilizada é -03:00, indicando um deslocamento de tempo de 3 horas atrás do UTC (horário universal coordenado).

- `.withIssuer("API Crud Growdevers")`
> O emissor do token (issuer) é definido como "API Crud Growdevers". Isso identifica quem emitiu o token.

- `.withSubject(growdever.getEmail())`
> O assunto (subject) do token é definido como o e-mail do usuário growdever. Isso identifica o assunto do token, que geralmente é o usuário ao qual o token se refere.

- `.withClaim("id", growdever.getId().toString())`
> Um claim personalizado é adicionado ao token, neste caso, com a chave "id" e o valor sendo o identificador do growdever. Claims são informações adicionais que podem ser incluídas no token.

- `.withExpiresAt(expiresDate)`
> A data de expiração calculada anteriormente é definida como a data de expiração do token. Depois dessa data, o token não será mais válido.

- `.sign(algorithm)`
> Finalmente, todas as informações configuradas são usadas para criar e assinar o token JWT usando o algoritmo HMAC256 e a chave secreta secret. O token gerado é retornado como resultado da função.

## 10- Injetar o TokenService no AuthController
O TokenService deve ser injetado através do **@autowired** no controller AuthController e depois deve ser feito a seguinte alteração no método **doLogin** para poder gerar e retornar o token.

```
@PostMapping
public ResponseEntity doLogin(@RequestBody @Valid LoginAuth data) {
    var token = new UsernamePasswordAuthenticationToken(data.email(), data.password());
    var authentication = manager.authenticate(token);

    var jwt = tokenService.getToken((Growdever) authentication.getPrincipal());

    return ResponseEntity.ok().body(jwt);
}
```

Voce pode ver que antes de retornar a resposta para o client está sendo utilizado o tokenServie para gerar o token. Através do método **getPrincipal** da váriavel **authentication** é possível buscar os dados do usuário, neste caso, o Growdever.

Se você tentar fazer a requisição agora, voce vai ter o token JWT sendo retornado como resposta.

## 11-