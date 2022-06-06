package com.bot.dto.commandos.impl;

import com.bot.APIs;
import com.bot.Constantes;
import com.bot.TelegramRespostas;
import com.bot.dto.ApiJokeChuckNorrisResponse;
import com.bot.dto.ApiTranslateResponse;
import com.bot.dto.commandos.Comando;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.http.ResponseEntity;

public class PiadaChuckNorris extends Comando {

    private static String token_rapid_key = System.getenv().get("token_rapid_key");

    @Override
    public void executaComando(Update update, SendResponse sendResponse, TelegramBot bot) {

        try {

            ResponseEntity<ApiJokeChuckNorrisResponse> requestJokeChuckNorrisAPI = APIs.requestJokeChuckNorrisAPI();
            String telegramRespostaPiadaChuckNorris = TelegramRespostas.telegramRespostaPiadaChuckNorris(update, requestJokeChuckNorrisAPI, sendResponse, bot);
            try {

				ResponseEntity<ApiTranslateResponse> requestTradutorAPI = APIs.requestTradutorAPI(telegramRespostaPiadaChuckNorris, token_rapid_key);
				TelegramRespostas.telegramRespostaPiadaChuckNorrisTradutor(update, requestTradutorAPI, sendResponse, bot);

            } catch (Exception e) {

                System.out.println("LOG - ERRO FUNCIONALIDADE TRADUCAO PIADA_CHUCK_NORRIS");
                System.out.println(e);
            }

            sendResponse = bot.execute(new SendMessage(update.message().chat().id(), Constantes.BOT_SE_QUISER + Constantes.BOT_FUNCIONALIDADES));

        } catch (Exception e) {

            System.out.println("LOG - ERRO FUNCIONALIDADE PIADA_CHUCK_NORRIS");
            System.out.println(e);
            sendResponse = bot.execute(new SendMessage(update.message().chat().id(), Constantes.BOT_PIADA_CHUCK_FUNC_ERRO));
            sendResponse = bot.execute(new SendMessage(update.message().chat().id(), Constantes.BOT_SE_QUISER + Constantes.BOT_FUNCIONALIDADES));
        }

    }
}
