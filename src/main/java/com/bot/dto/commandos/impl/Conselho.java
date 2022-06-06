package com.bot.dto.commandos.impl;

import com.bot.APIs;
import com.bot.Constantes;
import com.bot.TelegramRespostas;
import com.bot.dto.ApiAdviceResponse;
import com.bot.dto.ApiTranslateResponse;
import com.bot.dto.commandos.Comando;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.http.ResponseEntity;

public class Conselho extends Comando {

    private static String token_rapid_key = System.getenv().get("token_rapid_key");

    @Override
    public void executaComando(Update update, SendResponse sendResponse, TelegramBot bot) {

        try {

            ResponseEntity<ApiAdviceResponse> requestConselhoAPI = APIs.requestConselhoAPI();
            String telegramRespostaConselho = TelegramRespostas.telegramRespostaConselho(update, requestConselhoAPI, sendResponse, bot);

            try {

                ResponseEntity<ApiTranslateResponse> requestTradutorAPI = APIs.requestTradutorAPI(telegramRespostaConselho, token_rapid_key);
                TelegramRespostas.telegramRespostaConselhoTradutor(update, requestTradutorAPI, sendResponse, bot);

            } catch (Exception e) {

                System.out.println("TELEGRAM - ERRO FUNCIONALIDADE TRADUCAO CONSELHO");
                System.out.println(e);
            }

            sendResponse = bot.execute(new SendMessage(update.message().chat().id(), Constantes.BOT_SE_QUISER + Constantes.BOT_FUNCIONALIDADES));

        } catch (Exception e) {

            System.out.println("TELEGRAM - ERRO FUNCIONALIDADE CONSELHO");
            System.out.println(e);
            sendResponse = bot.execute(new SendMessage(update.message().chat().id(), Constantes.BOT_PIADA_CONSELHO_FUNC_ERRO));
            sendResponse = bot.execute(new SendMessage(update.message().chat().id(), Constantes.BOT_SE_QUISER + Constantes.BOT_FUNCIONALIDADES));
        }

    }

}
