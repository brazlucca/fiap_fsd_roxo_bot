# Java Bot Telegram

Bem vindo ao bot do telegram em Java.

Para uso do bot, deve-se primeiro cadastrar um novo bot para seu teste dentro do próprio Telegram usando o @BotFather que é o criador de bots do Telegram, no final de sua criação ele enviará para você um token que serve para controlar e rodar sua aplicação/bot.

Fazer o clone do projeto e rodar a aplicação Main preenchendo as variáveis de ambiente:

    #variaveis de ambiente
        token_telegram={token_telegram}
        token_rapid_key={token_rapid_key}
        token_weather={token_weather}

A variável {token_telegram} é o token que foi gerado pelo BotFather para controlar e rodar seu bot.

As outras variáveis são de APIs que necessitam o token, seguem abaixo:

API de previsão do tempo, variável: {token_weather}
https://hgbrasil.com/status/weather

API de tradução, variável: {token_rapid_key}. 
https://rapidapi.com/sibaridev/api/google-unlimited-multi-translate/
Essa API de tradução não tem necessidade pois apesar de pública existe um limite de uso, então caso não queria utilizar deixar a variável com valor em branco. Apenas o texto que retorna não será traduzido.

Outras APIs que são públicas, sem necessidade de token:

API de conselhos:
https://api.adviceslip.com/

API de piadas sobre o Chuck Norris:
https://api.chucknorris.io/

Ao rodar a aplicação, procure pelo @nome_do_bot que você criou no Telegram e dê o comando /start. A patir daí será bem intuitivo.
O bot tem as seguintes funcionalidades:

    /tempo 
      Retorna a previsão do tempo de hoje e amanhã.
      
    /conselho 
      Retorna um conselho em inglês e em português e a API de tradução estiver ativa e disponível.
      
    /piada_Chuck_Norris 
      Retorna uma piada do Chuck Norris em inglês e em português e a API de tradução estiver ativa e disponível.

    /ordenar "Passe os números que você deseja ordenar "
      Ordena uma sequência de números que você passar após o comando com espaço entre cada número.
