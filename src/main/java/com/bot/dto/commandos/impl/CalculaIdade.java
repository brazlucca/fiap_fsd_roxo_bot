package com.bot.dto.commandos.impl;

import com.bot.Constantes;
import com.bot.TelegramRespostas;
import com.bot.dto.commandos.Comando;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

public class CalculaIdade extends Comando {

    @Override
    public void executaComando(Update update, SendResponse sendResponse, TelegramBot bot) {
        try {

            TelegramRespostas.telegramRespostaCaculoIdade(update, sendResponse, bot);
            sendResponse = bot.execute(new SendMessage(update.message().chat().id(), Constantes.BOT_SE_QUISER + Constantes.BOT_FUNCIONALIDADES));

        } catch (Exception e) {

            System.out.println("LOG - ERRO FUNCIONALIDADE IDADE");
            System.out.println(e);
            sendResponse = bot.execute(new SendMessage(update.message().chat().id(), Constantes.BOT_PIADA_IDADE_FUNC_ERRO));
            sendResponse = bot.execute(new SendMessage(update.message().chat().id(), Constantes.BOT_SE_QUISER + Constantes.BOT_FUNCIONALIDADES));
        }


    }
}
