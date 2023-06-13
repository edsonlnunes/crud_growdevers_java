
**CRUD de Growdevers**


**Dados:**
- Nome
- CPF
- Email
- Telefone
- Status [ESTUDANDO, CANCELADO, FORMADO]
- Skills -> JS, TS, Html, Node, Java, Spring

**Classes:**
- Growdever 
- GrowdeverSkill

**Funcionalidades:**

- Cadastrar um growdever
    - Informações: Nome, CPF, E-mail, Telefone (opcional) , Status

- Listar todos os growdevers
    - Resposta: Uma lista contendo as seguintes informações: Identificador, nome, status, email

- Obter um growdever por um identificador
    - Resposta: Todas as informações do growdever

- Atualizar as informações de um growdever
    - Informações: Nome, e-mail, telefone, status
    - Regras: não é preciso informar todas as informações

- Deletar um growdever

- Filtrar os growdevers por nome e por status

- Remover uma skill individualmente
    - Receber a identificação da skill

- Adicionar uma skills
    - Informações: Uma lista de novas skills

**Regras:**

- CPF precisa ser validado.
- E-mail precisa ser validado
- Não pode existir dois growdevers com o mesmo CPF
- Não pode existir dois growdevers com o mesmo E-MAIL
