package com.bot.dto.commandos.impl;

import com.bot.APIs;
import com.bot.Constantes;
import com.bot.TelegramRespostas;
import com.bot.dto.ApiWeatherResponse;
import com.bot.dto.commandos.Comando;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.http.ResponseEntity;

public class Tempo extends Comando {

    private static String token_weather = System.getenv().get("token_weather");

    @Override
    public void executaComando(Update update, SendResponse sendResponse, TelegramBot bot) {
        try {

            ResponseEntity<ApiWeatherResponse> requestTempoAPI = APIs.requestTempoAPI(token_weather);
            TelegramRespostas.telegramRespostaTempo(update, requestTempoAPI, sendResponse, bot);
            sendResponse = bot.execute(new SendMessage(update.message().chat().id(), Constantes.BOT_SE_QUISER + Constantes.BOT_FUNCIONALIDADES));

        } catch (Exception e) {

            System.out.println("TELEGRAM - ERRO FUNCIONALIDADE TEMPO");
            System.out.println(e);
            sendResponse = bot.execute(new SendMessage(update.message().chat().id(), Constantes.BOT_PIADA_TEMPO_FUNC_ERRO));
            sendResponse = bot.execute(new SendMessage(update.message().chat().id(), Constantes.BOT_SE_QUISER + Constantes.BOT_FUNCIONALIDADES));
        }

    }

}
