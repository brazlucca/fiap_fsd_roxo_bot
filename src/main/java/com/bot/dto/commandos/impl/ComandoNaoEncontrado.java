package com.bot.dto.commandos.impl;

import com.bot.Constantes;
import com.bot.dto.commandos.Comando;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

public class ComandoNaoEncontrado extends Comando {

    @Override
    public void executaComando(Update update, SendResponse sendResponse, TelegramBot bot) {
        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), Constantes.BOT_NAO_ENTENDI));
    }

}
