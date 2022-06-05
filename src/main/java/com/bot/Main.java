package com.bot;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.bot.dto.ApiAdviceResponse;
import com.bot.dto.ApiJokeChuckNorrisResponse;
import com.bot.dto.ApiTranslateResponse;
import com.bot.dto.ApiWeatherResponse;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

public class Main {
	
	//Variaveis do ambiente
	private static Map<String, String> env = System.getenv();
	
	//Tokens
	private static String token_telegram = env.get("token_telegram");
	private static String token_rapid_key = env.get("token_rapid_key");
	private static String token_weather = env.get("token_weather");
	
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
						
						if (commandLowerCase.equals("/start")) {
							
							TelegramRespostas.telegramRespostaPadrao(update, sendResponse, bot);
							
						} else if (commandLowerCase.equals("/tempo")) {
							
							tempoFunction(update);
							
							logRespostaEnviada();

						} else if (commandLowerCase.equals("/conselho")) {
							
							conselhoFunction(update);
							
							logRespostaEnviada();

						} else if (commandLowerCase.equals("/piada_Chuck_Norris".toLowerCase())) {
							
							piadaChuckNorrisFunction(update);
							
							logRespostaEnviada();

						} else if (commandLowerCase.startsWith("/ordenar")) {
							
							ordenarFunction(update);
							
							logRespostaEnviada();
							
						} else if (commandLowerCase.startsWith("/calcula_sua_idade")) {
								
							idadeFunction(update);
							
							logRespostaEnviada();
							
						} else {
							
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(), Constantes.BOT_NAO_ENTENDI));
							
							logRespostaEnviada();

						}
						
						
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


	//FUNCIONALIDADES
	
	//Funcionalidade de idade
	private static void idadeFunction(Update update) {
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

	//Funcionalidade de ordenação
	private static void ordenarFunction(Update update) {
		try {
			TelegramRespostas.telegramRespostaOrdenar(update, sendResponse, bot);
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(), Constantes.BOT_SE_QUISER + Constantes.BOT_FUNCIONALIDADES));
		} catch (Exception e) {
			System.out.println("LOG - ERRO FUNCIONALIDADE ORDENAR");
			System.out.println(e);
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(), Constantes.BOT_ORDENAR_FUNC_ERRO));
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(), Constantes.BOT_SE_QUISER + Constantes.BOT_FUNCIONALIDADES));
		}
			
	}
	
	//Funcionalidade de piada do Chuck Norris
	
	//Existes dois try catchs pois a resposta vem em inglês, e caso ocorra um erro na api de tradução devido limite de uso
	//será retornado apenas a piada do Chuck Norris em inglês.
	private static void piadaChuckNorrisFunction(Update update) {
		try {
			
			ResponseEntity<ApiJokeChuckNorrisResponse> requestJokeChuckNorrisAPI = APIs.requestJokeChuckNorrisAPI();
			String telegramRespostaPiadaChuckNorris = TelegramRespostas.telegramRespostaPiadaChuckNorris(update,requestJokeChuckNorrisAPI, sendResponse, bot);
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
	
	//Funcionalidade de conselho
	
	//Existes dois try catchs pois a resposta vem em inglês, e caso ocorra um erro na api de tradução devido limite de uso
	//será retornado apenas o conselho em inglês.
	private static void conselhoFunction(Update update) {
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
	
	
	//Funcionalidade de previsão do tempo
	private static void tempoFunction(Update update) {
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
