package com.bot.dto.commandos;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.response.SendResponse;

public abstract class Comando {

    public abstract void executaComando(Update update, SendResponse sendResponse, TelegramBot bot);

}
