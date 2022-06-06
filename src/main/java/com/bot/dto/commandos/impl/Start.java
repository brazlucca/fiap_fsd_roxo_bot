package com.bot.dto.commandos.impl;

import com.bot.TelegramRespostas;
import com.bot.dto.commandos.Comando;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.response.SendResponse;

public class Start extends Comando {

    @Override
    public void executaComando(Update update, SendResponse sendResponse, TelegramBot bot) {
        TelegramRespostas.telegramRespostaPadrao(update, sendResponse, bot);
    }
}
