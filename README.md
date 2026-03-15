# Assignment 1 - Assisted code generation (MIP)
**Course:** Desenvolvimento de Aplicações Móveis (DAM)  
**Student(s):** Matilde Gonçalves (51706)  
**Date:** 15/03/2026  
**Repository URL:** https://github.com/MattGon13/DAM-TP1-MIP.git

## 1. Introduction
Este trabalho teve como objetivo desenvolver uma aplicação mobile utilizando o Android Studio e Kotlin com o auxílio de inteligência artificial de modo a realizar o processo de geração de código assistida. Para tal, poderíamos usar várias tecnologias de inteligência artificial como o Google Antigravity, Claude Code e as ferramentas de automatização do GitHub como o co-pilot. Com este trabalho deveríamos praticar a nossa interação com agentes e melhorar a nossa capacidade de prompting de modo a chegarmos aos resultados pretendidos.

## 2. System Overview
A aplicação desenvolvida, “Catlendar”, é uma aplicação Android desenhada para gerir eventos pessoais, através de um calendário, com um tema centrado em gatos. Para tal, o sistema utiliza uma base de dados local para armazenar, recuperar e pesquisar eventos do utilizador offline. Em segundo plano, a aplicação comunica com uma API externa, Cat Facts (https://alexwohlbruck.github.io/cat-facts/), para obter factos sobre gatos e enviá-los uma vez por dia ao utilizador através de notificações locais.

## 3. Architecture and Design
A aplicação segue o padrão de arquitetura MVVM (Model-View-ViewModel), que separa a lógica da interface gráfica, da lógica de backend e de dados, garantindo que o código seja modular, testável e de fácil manutenção.
* **View (UI Layer):** Composta por Fragmentos (CalendarFragment, SearchFragment) e Activities (MainActivity), escritos com layouts XML e suportados por ViewBinding. Estes componentes observam os dados do ViewModel e atualizam o ecrã, não contendo qualquer lógica de backend.
* **ViewModel:** Componentes como CalendarViewModel e SearchViewModel atuam como ponte entre a UI e a camada de dados, solicitando dados aos repositórios e expondo-os à interface.
* **Model (Data Layer):** O EventRepository funciona como a fonte de armazenamento para todos os dados de eventos.
  * **Local Database:** Suportada pelo Room Database (AppDatabase, EventDao), oferecendo uma camada de abstração sobre o SQLite para armazenamento local seguro.
  * **Network:** Suportada pelo Retrofit para efetuar chamadas à API e obter os factos sobre gatos diariamente.



## 4. Implementation
A aplicação foi desenvolvida em Kotlin (Minimum SDK 26, Target SDK 34) e tira partido das bibliotecas Jetpack:
* **UI/Views:** Desenvolvidas com ficheiros de layout XML e componentes de acordo com o Material Design 3. A navegação dentro da aplicação é gerida pelo Jetpack Navigation Component ligado a uma Bottom Navigation Bar que permite comutar entre o calendário e a página de pesquisas.
* **Networking:** Os pedidos à API utilizam o Retrofit e o OkHttp para processar e decodificar as respostas HTTP.
* **Background Tasks:** O WorkManager API é responsável por agendar o "Daily Cat Fact". Mesmo com o dispositivo reiniciado ou a aplicação fechada, o WorkManager acorda o sistema, obtém o facto em segundo plano e dispara uma Notificação Push.

## 5. Testing and Validation
Para testar cada versão da aplicação, foi utilizado o emulador Android Virtual Device (AVD) configurado para o Pixel 9 Pro e API 34 (UpsideDownCake) e um dispositivo móvel Android físico. A estratégia de teste passou por visualizar nestes dispositivos se todas as funcionalidades pedidas (criação/remoção de eventos, persistência de eventos, pesquisa de eventos) funcionam devidamente. 
Exemplos de test cases utilizados foram verificar se os eventos marcados no calendário mantinham-se no calendário após o encerramento da aplicação, confirmar que era possível adicionar e remover eventos e ter a certeza que um evento já marcado no calendário, apareceria na pesquisa.

## 6. Usage Instructions
1. Abrir o Android Studio (versão Panda 1 | 2025.3.1 ou superior) e selecionar `Open` apontando para a diretoria do projeto.
2. No Gestor de Dispositivos (Device Manager), iniciar um AVD (ex: Pixel 9 Pro) ou ligar um dispositivo android compatível por usb.
3. Clicar no botão "Play" (verde) na barra de ferramentas ou utilizar `Shift + F10` para compilar e instalar o APK no emulador.
4. Interagir com a interface

# Autonomous Software Engineering Sections

## 7. Prompting Strategy
A estratégia de prompting usada baseou-se em usar o método de direct prompting, de modo a permitir interações mais rápidas, colocar o agente em “Planning Mode”, para que este criasse planos de implementação mais estruturados e pedisse por aprovação durante o processo de desenvolvimento, criar prompts detalhadas com uma estrutura clara para o agente perceber exatamente o que tinha de fazer (foi usado como base o exemplo de prompt do enunciado), dar roles ao agente de modo a torná-lo um especialista na área de desenvolvimento de aplicações Android, e começando por pedir componentes mais gerais (interface e lógica) para depois ir mais para detalhes como correção de erros, persistência, API’s, etc.
A primeira prompt realizada, teve como objetivo contextualizar o agente quanto à aplicação a ser desenvolvida e criar a interface da aplicação. Encontra-se de seguida a prompt mencionada:

**Context:** You are a professional app developer and UI designer that is tasked to make the front end for a mobile app with Android studio using Kotlin, XML-based Views, Android Studio, and Material Design 3. Assume that all the information that you have is outdated and search what you need to know before starting to code. Follow the best coding practices.

**Goal:** Create the front end for an Android mobile app called "Catlendar". "Catlendar" is a calendar app that lets users add and remove events, search for previously added events and gives a random cat fact everyday by sending a notification to the user. The front end of this app has a cat theme and is supposed to be very aesthetic and modern.

**Constraints:**  Use the latest stable Android SDK, Kotlin, XML layouts (Views system), Material Design 3 components, and follow widely adopted Android best practices and architectural guidelines.

**Plan:** Before executing any commands or generating code, produce a detailed project plan artifact for approval that defines the recommended architecture, folder structure, key dependencies with justification, navigation strategy between screens, state management, assumptions, risks, and scalability considerations. 

**Verification:** Ensure the proposal is complete, maintainable, suitable for future expansion, and compatible with modern Android development standards.

**Deliverables:** Provide the detailed project plan artifact and wait for explicit approval before implementation. 

O segundo prompt enviado, teve como objetivo criar toda a lógica da aplicação:

**Context:** You are a professional app developer that is tasked to make the front end for a mobile app with Android studio using Kotlin, XML-based Views, Android Studio, and Material Design 3. Assume that all the information that you have is outdated and search what you need to know before starting to code. Follow the best coding practices.

**Goal:** Create the logic for an Android mobile app called "Catlendar". "Catlendar" is a calendar app that lets users add and remove events, search for previously added events and gives a random cat fact everyday by sending a notification to the user. The front end of this app has a cat theme and is supposed to be very aesthetic and modern.

**Constraints:**  Use the latest stable Android SDK, Kotlin, XML layouts (Views system), Material Design 3 components, and follow widely adopted Android best practices and architectural guidelines.

**Plan:** Before executing any commands or generating code, produce a detailed project plan artifact for approval that defines the recommended architecture, folder structure, key dependencies with justification, navigation strategy between screens, state management, assumptions, risks, and scalability considerations. 

**Verification:** Ensure the proposal is complete, maintainable, suitable for future expansion, and compatible with modern Android development standards.

**Deliverables:** Provide the detailed project plan artifact and wait for explicit approval before implementation. 

Os restantes prompts foram menos significativos e tiveram como função corrigir erros como a falta de persistência dos eventos adicionados pelo utilizador e falhas na pesquisa de eventos.

## 8. Autonomous Agent Workflow
O software Antigravity com o agente “Gemini 3.1 Pro (High)” foi usado ativamente para planear o desenvolvimento da aplicação de acordo com os requisitos/funcionalidades esperadas e para, posteriormente, executar esse plano tanto para a front end da aplicação, como para o back end. O agente também foi fazendo alguns testes unitários, no entanto a maior parte dos testes foi feita por mim.

## 9. Verification of AI-Generated Artifacts
Por cada prompt enviada, foi pedido ao agente para fornecer um plano detalhado do que iria realizar e esperar por aprovação antes de implementar alguma coisa. Estes planos/artefactos fornecidos pelo agente foram analisados tendo em conta a natureza da aplicação e foram aceites apenas os planos que se enquadravam com a mesma. 
O código gerado pelo agente, seguindo os planos aceites, foi submetido a testes para verificar se todas as funcionalidades solicitadas para a aplicação tinham uma boa prestação.

## 10. Human vs AI Contribution
A ideia da aplicação, incluindo as suas funcionalidades/requisitos e tema, foi criada por mim. Já toda a implementação lógica foi desenvolvida pelo agente através das prompts detalhadas que lhe foram fornecidas. Além disso, o agente também ajudou a corrigir alguns erros pontuais que foram aparecendo durante o desenvolvimento da aplicação. A maior parte dos testes realizados foram feitos por mim utilizando emulador Android Virtual Device (AVD) configurado para o Pixel 9 Pro ou o meu próprio telemóvel. Por fim, o ícone da aplicação foi adicionado por mim.

## 11. Ethical and Responsible Use
Tal como já foi dito, por cada prompt enviada, foi pedido ao agente para fornecer um plano detalhado do que iria realizar e esperar por aprovação antes de implementar alguma coisa. Isto permitiu ter mais controlo sobre o código gerado e utilizar o agente com mais responsabilidade, verificando se todas as ações que iria fazer eram seguras e apropriadas.

# Development Process

## 12. Version Control and Commit History
O desenvolvimento da aplicação foi seguido e guardado através de um repositório Git local. O primeiro commit realizado continha a primeira versão da aplicação com não só o front end, como o back end da mesma. De seguida, foram-se fazendo mais commits por cada erro resolvido ou funcionalidade adicionada.



## 13. Difficulties and Lessons Learned
O principal desafio foi, inicialmente, instalar e aprender a trabalhar com o software Antigravity e com os seus agentes. Outro desafio que surgiu foi o facto de o agente não estar programado para utilizar as versões mais recentes das ferramentas que utiliza, por exemplo o gradle, o que gerou alguns erros. Com este problema, aprendi a explicitar nas prompts para o agente usar as versões mais atualizadas das ferramentas que vai utilizar e para pesquisar todas as boas práticas e outras informações que precise para desenvolver a aplicação, antes de o fazer realmente. 

## 14. Future Improvements
Esta aplicação é bastante simples permitindo ao utilizador criar, remover e pesquisar por eventos. No futuro, um aspeto que iria evoluir a aplicação seria, por exemplo, mandar notificações aos utilizadores sobre os eventos que vão acontecer no dia atual. Também seria interessante fazer uma interface mais apelativa com animações e outros elementos.

## 15. AI Usage Disclosure (Mandatory)
Tal como foi recomendado pelos professores, foi utilizado o software Antigravity com o agente “Gemini 3.1 Pro (High)” para planear e desenvolver o front end e back end da aplicação. Como já foi mencionado, por cada prompt enviada, foi pedido ao agente para fornecer um plano detalhado do que iria realizar e esperar por aprovação antes de implementar alguma coisa, fazendo com que houvesse um maior controlo no uso do agente.  Assumo então responsabilidade por esta aplicação.
