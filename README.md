AmparoMed
=========

Aplicativo Android de apoio ao cuidado de pacientes dependentes — idosos, acamados ou pessoas com necessidades contínuas de saúde. Centraliza os lembretes de medicação, o controle de estoque de remédios, o histórico de doses, os contatos de familiares e cuidadores e um acionamento rápido de emergência, reduzindo a sobrecarga de quem cuida e aumentando a segurança de quem é cuidado.

![Java](https://img.shields.io/badge/Java-17-orange)
![Android](https://img.shields.io/badge/Android-minSdk%2026-3DDC84)
![Material 3](https://img.shields.io/badge/UI-Material%203-6750A4)
![SQLite](https://img.shields.io/badge/Banco-SQLite%20(Room)-003B57)
![Status](https://img.shields.io/badge/status-prot%C3%B3tipo%20acad%C3%AAmico-informational)

> Projeto acadêmico do curso de Análise e Desenvolvimento de Sistemas (ADS) da Universidade Cidade de São Paulo (UNICID).

Telas
-----

<p>
  <img src="design/screens/03-inicio.png" width="200" alt="Início" />
  <img src="design/screens/06-historico.png" width="200" alt="Histórico" />
  <img src="design/screens/07-estoque.png" width="200" alt="Estoque" />
  <img src="design/screens/05-emergencia.png" width="200" alt="Emergência" />
</p>

Sobre o projeto
---------------

Grande parte do cuidado domiciliar ainda é organizada à mão — anotações em papel, alarmes avulsos no celular ou a memória de quem cuida. Isso abre espaço para esquecimentos de medicação, falta de remédio em casa e demora para agir numa emergência. O **AmparoMed** nasceu para dar um apoio ativo ao cuidador, reunindo numa única plataforma simples o que antes ficava espalhado.

O app foi pensado em torno de três papéis:

| Papel | O que faz no app |
|---|---|
| **Paciente** | Pessoa cuidada. Tem perfil com dados, alergias e doenças; é o centro dos lembretes e do histórico. |
| **Cuidador** | Familiar ou cuidador profissional. Usa o app no dia a dia: confirma doses, controla estoque, aciona emergência. |
| **Contatos** | Familiares e cuidadores cadastrados, acionáveis rapidamente — inclusive na tela de emergência. |

Funcionalidades
---------------

- **Lembretes de medicação** por horário, com confirmação de dose (tomado / não tomado).
- **Controle de estoque** com aviso automático de remédio acabando e reposição rápida.
- **Histórico** de doses tomadas e não tomadas, com filtros (hoje / 7 dias / 30 dias).
- **Cadastro de medicamentos** (sólidos e líquidos) e de **lembretes**.
- **Pessoas**: perfil do paciente e lista de cuidadores/familiares, com cadastro e edição.
- **Emergência**: botão para o SAMU (192) e ligação rápida para os contatos (simulada no protótipo).
- **Login e cadastro** de usuário (telas visuais).
- **Persistência local** em banco de dados SQLite — os dados sobrevivem ao fechar o app.

Telas do app
------------

| Entrada | Início | Alarme |
|---|---|---|
| ![Login](design/screens/01-login.png) | ![Início](design/screens/03-inicio.png) | ![Alarme](design/screens/04-alarme.png) |

| Estoque | Cadastrar medicamento | Repor estoque |
|---|---|---|
| ![Estoque](design/screens/07-estoque.png) | ![Cadastrar](design/screens/08-cadastrar-medicamento.png) | ![Repor](design/screens/09-repor.png) |

| Histórico | Pessoas (paciente) | Perfil do cuidador |
|---|---|---|
| ![Histórico](design/screens/06-historico.png) | ![Paciente](design/screens/10-paciente.png) | ![Perfil](design/screens/12-perfil-cuidador.png) |

Arquitetura
-----------

O app segue uma separação clara entre interface, regra de negócio e persistência. A UI nunca fala direto com o banco: tudo passa pelo repositório.

```mermaid
flowchart LR
    UI["UI — Activities e Fragments"] --> REPO["AmparoRepository (singleton)"]
    REPO --> CALC["EstoqueCalculadora (regras)"]
    REPO --> DB[("SQLite — Room")]
    SEED["SeedData (dados de exemplo)"] --> REPO
```

- **UI**: 4 abas (Início, Histórico, Estoque, Pessoas) dentro de uma `MainActivity` com `BottomNavigationView`; telas de detalhe (Alarme, Emergência, Perfil, Formulários) são Activities próprias.
- **Repositório**: ponto único de acesso aos dados. Mantém um cache em memória para a UI e faz *write-through* no banco a cada alteração.
- **Banco**: SQLite via **Room**. Na primeira abertura, o banco é semeado com dados de exemplo; depois, é carregado do disco.

Estrutura do repositório
------------------------

| Caminho | Conteúdo |
|---|---|
| `app/src/main/java/.../model` | Entidades (Medicamento, Lembrete, Cuidador, Paciente, RegistroDose) e enums. |
| `app/src/main/java/.../data` | Repositório, calculadora de estoque e dados de exemplo. |
| `app/src/main/java/.../data/db` | Banco Room: `AppDatabase`, DAOs e conversores de tipo. |
| `app/src/main/java/.../ui` | Telas: fragments das abas, activities de detalhe, adapters e diálogos. |
| `app/src/main/res` | Layouts, cores, estilos, ícones e drawables. |
| `design/` | Documentação e capturas de tela. |

Tecnologias
-----------

- **Linguagem**: Java 17.
- **Plataforma**: Android (minSdk 26, compileSdk/targetSdk 35).
- **UI**: XML com Views tradicionais e **Material 3** (sem Jetpack Compose).
- **Banco de dados**: **SQLite** via **Room** 2.6.
- **Build**: Gradle 8.9 / Android Gradle Plugin 8.7.3.
- Sem dependências de rede: o app funciona 100% offline.

Como rodar
----------

**Pré-requisitos:** Android Studio, JDK 21 e um emulador ou dispositivo com Android 8.0 (API 26) ou superior.

```bash
git clone https://github.com/0Luan0/AmparoMed.git
```

1. Abra o projeto no Android Studio (`File > Open` apontando para a pasta `AmparoMed`).
2. Quando perguntado, selecione **Use JVM 21** (o JDK 25 embutido é incompatível com o Gradle 8.9).
3. Aguarde o **Gradle Sync**.
4. Clique em **Run** (▶) e escolha um emulador arm64.

Banco de dados
--------------

Banco SQLite local (`amparomed.db`), criado pelo Room. Principais tabelas:

| Tabela | Guarda |
|---|---|
| `medicamento` | Remédios do estoque (nome, dosagem, forma, tipo, quantidade). |
| `lembrete` | Regras de quando tomar cada remédio (horário, intervalo, duração). |
| `registro_dose` | Histórico de doses tomadas e não tomadas. |
| `cuidador` | Familiares e cuidadores (contatos). |
| `paciente` | Dados do paciente (linha única). |

O que é real e o que é simplificado
-----------------------------------

- **Login e cadastro**: telas **visuais**, sem autenticação real (qualquer entrada abre o app). É um protótipo acadêmico.
- **Dados**: na primeira abertura, o app é populado com um cenário de exemplo (paciente, remédios, cuidadores e histórico). A partir daí, os dados ficam salvos no banco.
- **Ligações e emergência**: **simuladas** — o app mostra um diálogo em vez de discar. Num app real, o acionamento do 192 usaria `Intent.ACTION_DIAL`.

## Equipe AmparoMed

<p align="center">
  <img src="docs/equipe/equipe-amparomed.jpeg" alt="Equipe AmparoMed" width="700">
</p>

<p align="center">
  <strong>Equipe responsável pelo desenvolvimento do AmparoMed</strong>
</p>

### Conheça os integrantes

<table>
  <tr>
    <td align="center" width="50%">
      <img src="docs/equipe/mateus.png" alt="Mateus dos Santos" width="150" height="150">
      <br>
      <strong>Mateus dos Santos Araújo</strong>
      <br>
      <sub>Função: Líder e projetista de Banco de Dados</sub>
      <br>
      <a href="https://github.com/araujosantos20">@araujosantos20</a>
    </td>
    <td align="center" width="50%">
      <img src="docs/equipe/igor.jpeg" alt="Igor Vicente" width="150" height="150">
      <br>
      <strong>Igor Vicente José da Silva</strong>
      <br>
      <sub>Função: Analista</sub>
      <br>
      <a href="https://github.com/HARD2407">@Igor Vicente</a>
    </td>
    <td align="center" width="50%">
      <img src="docs/equipe/lucas.jpeg" alt="Lucas Teixeira" width="150" height="150">
      <br>
      <strong>Lucas Teixeira Oliveira</strong>
      <br>
      <sub>Função: Desenvolvedor</sub>
      <br>
      <a href="https://github.com/Lucas0liveiraT">@Lucas0liveira</a>
    </td>
  </tr>
  <tr>
    <td align="center" width="50%">
      <img src="#" alt="Luan Lucena" width="150" height="150">
      <br>
      <strong>Luan Lucena dos Anjos</strong>
      <br>
      <sub>Função: Testador</sub>
      <br>
      <a href="https://github.com/0Luan0">@0Luan0</a>
    </td>
    <td align="center" width="50%">
      <img src="docs/equipe/rafhael.jpeg" alt="Rafhael César" width="150" height="150">
      <br>
      <strong>Rafhael César Araújo Paulino</strong>
      <br>
      <sub>Função: Documentador</sub>
      <br>
      <a href="https://github.com/_RafhaelCesar">@_RafhaelCesar</a>
    </td>
    <td></td>
  </tr>
</table>

---

### Informações acadêmicas

Projeto desenvolvido para a disciplina de **Desenvolvimento de Aplicações**, sob orientação do professor **Jadir Custodio Mendonça Júnior**, na **Universidade Cidade de São Paulo (UNICID)**.
