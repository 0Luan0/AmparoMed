# AmparoMed — registro do projeto

App de lembrete de remedios (trabalho ADS/UNICID). Java + XML, Material 3, minSdk 26.
Reconstruido em 2026-11-02 (a versao anterior, de 23/09, foi perdida quando a pasta
~/Downloads foi limpa). Local: ~/Downloads/AmparoMed.

## Stack / build
- Gradle 8.9 / AGP 8.7.3, compileSdk 35, minSdk 26, targetSdk 35, Java 17.
- Deps: appcompat 1.7.0, material 1.12.0, recyclerview 1.3.2, activity 1.9.3.
- Build por CLI: JAVA_HOME = JBR-21 do Studio
  (~/Library/Java/JavaVirtualMachines/jbr-21.0.11/Contents/Home), rodar `bash gradlew assembleDebug`.
  (Gradle binario em cache: ~/.gradle/wrapper/dists/gradle-8.9-bin/.../gradle-8.9/bin/gradle,
  usado para gerar o wrapper com `gradle wrapper --gradle-version 8.9`.)

## Estrutura (33 classes Java, 22 layouts, 63 drawables)
- model/ : FormaFarmaceutica, StatusDose, Vinculo (enums); Medicamento, Lembrete,
  RegistroDose, Ocorrencia, Paciente, Cuidador.
- data/  : AmparoRepository (singleton em memoria), EstoqueCalculadora, SeedData.
- util/  : Constantes, Formatos, InsetsHelper.
- ui/    : LoginActivity, InicioFragment, HistoricoFragment, EstoqueFragment, PessoasFragment,
  AlarmeActivity, EmergenciaActivity, PerfilCuidadorActivity, FormCuidadorActivity,
  FormPacienteActivity, LembreteAdapter, MedicamentoAdapter, HistoricoAdapter, ContatoAdapter,
  AdicionarLembreteSheet, CadastrarMedicamentoSheet, Dialogos.
- MainActivity (base) : casca das 4 abas (BottomNavigationView).

## Fluxo de telas
LoginActivity (LAUNCHER, apenas visual, botao Entrar -> MainActivity + finish) ->
MainActivity com 4 abas (Inicio/Historico/Estoque/Pessoas). Telas de detalhe (Alarme,
Emergencia, Perfil, Forms) sao Activities abertas por Intent.

## Regras de negocio-chave
- estoqueBaixo: soma das PROXIMAS 3 doses futuras; estoque <= soma -> baixo (so a Losartana acende).
- confirmarDose desconta do estoque + grava tomado; rejeitarDose nao desconta.
- Paciente.idade e Cuidador.iniciais/rotuloVinculo CALCULADOS, nunca digitados.
- Dados em memoria por design (demo sempre comeca igual); seed com datas relativas a hoje.
- Seed: 5 meds, 5 lembretes diarios (inclui Lactulose 21h -> contador do Inicio le "X de 5").

## Pendencias
- Fontes Plus Jakarta Sans (hoje usa Roboto; trocar android:fontFamily no styles.xml).
- Login e so visual: nao valida nada, qualquer toque em Entrar abre o app.

## IMPORTANTE
Fazer backup fora do ~/Downloads (essa pasta ja foi limpa uma vez e levou o projeto).
