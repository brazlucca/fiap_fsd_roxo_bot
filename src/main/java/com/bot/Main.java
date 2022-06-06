package com.bot;

import com.bot.dto.*;
import com.bot.dto.commandos.Comando;
import com.bot.dto.commandos.ComandoEnum;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public class Main {
	
	//Variaveis do ambiente
	private static Map<String, String> env = System.getenv();
	
	//Tokens
	private static String token_telegram = env.get("token_telegram");
//	private static String token_rapid_key = env.get("token_rapid_key");
//	private static String token_weather = env.get("token_weather");
	
	// Criacao do objeto telegram bot com as informacoes de acesso.
	private static TelegramBot bot = new TelegramBot(token_telegram);

	// Objeto responsavel por receber as mensagens.
	private static GetUpdatesResponse updatesResponse;

	// Objeto responsavel por gerenciar o envio de respostas.
	private static SendResponse sendResponse;

	// Objeto responsavel por gerenciar o envio de acoes do chat.
	private static BaseResponse baseResponse;
	
	
	public static void main(String[] args) {

		// Controle de off-set, isto e, a partir deste ID sera lido as mensagens
		// pendentes na fila.
		int m = 0;

		// Loop infinito pode ser alterado por algum timer de intervalo curto.
		while (true) {

			// Executa comando no Telegram para obter as mensagens pendentes a partir de um
			// off-set (limite inicial).
			updatesResponse = bot.execute(new GetUpdates().limit(100).offset(m));

			// Lista de mensagens.
			List<Update> updates = updatesResponse.updates();

			// Analise de cada acao da mensagem.
			for (Update update : updates) {

				// Atualizacao do off-set.
				m = update.updateId() + 1;

				if (update.message() != null) {
					
					logRecebendoMensagem(update);

					enviandoMensagemDigitando(bot, update);

					try {

						String commandLowerCase = update.message().text().toLowerCase();
						Comando comando = ComandoEnum.fromString(commandLowerCase).retornaNovoComando();
						comando.executaComando(update, sendResponse, bot);

					} catch (Exception e) {
						TelegramRespostas.telegramRespostaErroGeral(update, e, sendResponse, bot);
					}

				} else {
//					TelegramRespostas.telegramRespostaErroGeral(update, sendResponse, bot);
				}

			}
		}

	}

	private static void enviandoMensagemDigitando(TelegramBot bot, Update update) {
		
		// CHAT ACAO - Envio de "Escrevendo" antes de enviar a resposta.
		baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing));

		// CHAT ACAO - Verificacao de acao de chat foi enviada com sucesso.
		System.out.println("LOG - Resposta de Chat Action Enviada? " + baseResponse.isOk());
		
	}

	private static void logRespostaEnviada() {
		
		//RESPOSTA ACAO - Verificacao de mensagem enviada com sucesso.
		System.out.println("LOG - Mensagem Enviada? " + sendResponse.isOk());
		System.out.println("--------------");
		
	}
	
	private static void logRecebendoMensagem(Update update) {
		
		// USUARIO - Recebendo mensagem do usuário.
		System.out.println("LOG - Recebendo mensagem: " + update.message().text());
		
	}

}
