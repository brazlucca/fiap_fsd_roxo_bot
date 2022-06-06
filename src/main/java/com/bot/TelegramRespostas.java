package com.bot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;

import com.bot.dto.ApiAdviceResponse;
import com.bot.dto.ApiJokeChuckNorrisResponse;
import com.bot.dto.ApiTranslateResponse;
import com.bot.dto.ApiWeatherResponse;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

public class TelegramRespostas {

	public static void telegramRespostaTempo(Update update, ResponseEntity<ApiWeatherResponse> requestTempoAPI, SendResponse sendResponse, TelegramBot bot) {
		ApiWeatherResponse body = requestTempoAPI.getBody();

		String resultado = new StringBuilder().append("Previsão do tempo em: " + body.getResults().getCity())
				.toString();

		String resultadoHoje = new StringBuilder().append("Hoje \n")
				.append("Data: " + body.getResults().getForecast().get(0).getDate() + "\n")
				.append("Descrição: " + body.getResults().getDescription() + "\n")
				.append("Temperatura: " + body.getResults().getTemp() + " graus" + "\n")
				.append("Velocidade do vento: " + body.getResults().getWind_speedy() + "\n")
				.append("Por do sol: " + body.getResults().getSunset() + "\n")
				.append("Máximo: " + body.getResults().getForecast().get(0).getMax() + " graus" + "\n")
				.append("Mínimo: " + body.getResults().getForecast().get(0).getMin() + " graus").toString();

		String resultadoAmanha = new StringBuilder().append("Amanhã \n")
				.append("Data: " + body.getResults().getForecast().get(1).getDate() + "\n")
				.append("Descrição: " + body.getResults().getForecast().get(1).getDescription() + "\n")
				.append("Máximo: " + body.getResults().getForecast().get(1).getMax() + " graus" + "\n")
				.append("Mínimo: " + body.getResults().getForecast().get(1).getMin() + " graus").toString();

		sendResponse = bot.execute(new SendMessage(update.message().chat().id(), resultado));
		sendResponse = bot.execute(new SendMessage(update.message().chat().id(), resultadoHoje));
		sendResponse = bot.execute(new SendMessage(update.message().chat().id(), resultadoAmanha));

	}

	public static void telegramRespostaErroGeral(Update update, Exception e, SendResponse sendResponse, TelegramBot bot) {
		System.out.println(e);
		System.out.println("LOG - PROBLEMA GERAL NAS FUNCIONALIDADES");
		sendResponse = bot.execute(new SendMessage(update.message().chat().id(), Constantes.BOT_PROBLEMA_GERAL));
	}

//	public static void telegramRespostaErroGeral(Update update, SendResponse sendResponse, TelegramBot bot) {
//		sendResponse = bot.execute(new SendMessage(update.message().chat().id(), Constantes.BOT_PROBLEMA_GERAL));
//	}

	public static void telegramRespostaOrdenar(Update update, SendResponse sendResponse, TelegramBot bot) {
		String usuarioString = update.message().text().toLowerCase();
		usuarioString = usuarioString.replace("/ordenar", " ");
		usuarioString = usuarioString.trim();
		List<String> listaStringParaOrdenar = Arrays.asList(usuarioString.split(" "));
		List<Integer> listaIntegerParaOrdenar = listaStringParaOrdenar.stream().map(Integer::parseInt)
				.collect(Collectors.toList());
		Collections.sort(listaIntegerParaOrdenar);
		sendResponse = bot.execute(
				new SendMessage(update.message().chat().id(), "Sua lista ordenada é " + listaIntegerParaOrdenar));
	}

	public static void telegramRespostaPadrao(Update update, SendResponse sendResponse, TelegramBot bot) {
		sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Bem vindo usuário " + nomeDoUsuario(update)
				+ " ao BOT, para melhor experiência, recomendamos o uso desse bot na versão mobile ao invés da desktop."));
		sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Escolha uma das opções: \n" + Constantes.BOT_FUNCIONALIDADES));
	}



	public static String telegramRespostaConselho(Update update, ResponseEntity<ApiAdviceResponse> requestConselhoAPI, SendResponse sendResponse, TelegramBot bot) {
		ApiAdviceResponse body = requestConselhoAPI.getBody();

		String resultado = new StringBuilder().append("Conselho do dia em inglês: " + body.getSlip().getAdvice())
				.toString();

		sendResponse = bot.execute(new SendMessage(update.message().chat().id(), resultado));

		return resultado;

	}

	public static void telegramRespostaConselhoTradutor(Update update, ResponseEntity<ApiTranslateResponse> requestConselhoTradutorAPI, SendResponse sendResponse, TelegramBot bot) {
		ApiTranslateResponse body = requestConselhoTradutorAPI.getBody();

		String resultado = new StringBuilder().append(body.getResult().getPt()).toString();

		sendResponse = bot.execute(new SendMessage(update.message().chat().id(), resultado));

	}

	public static String telegramRespostaPiadaChuckNorris(Update update, ResponseEntity<ApiJokeChuckNorrisResponse> requestJokeChuckNorrisAPI, SendResponse sendResponse, TelegramBot bot) {
		ApiJokeChuckNorrisResponse body = requestJokeChuckNorrisAPI.getBody();

		String resultado = new StringBuilder().append("Piada do Chuck Norris em inglês: " + body.getValue()).toString();

		sendResponse = bot.execute(new SendMessage(update.message().chat().id(), resultado));

		return resultado;

	}

	public static void telegramRespostaCaculoIdade(Update update, SendResponse sendResponse, TelegramBot bot) throws ParseException {
		
		String usuarioString = update.message().text().toLowerCase();
		usuarioString = usuarioString.replace("/calcula_sua_idade", " ");
		usuarioString = usuarioString.trim();
		
		Date birthday = new SimpleDateFormat("dd/MM/yyyy").parse(usuarioString);

		LocalDate ldirthday = birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		int years = Period.between(ldirthday, LocalDate.now()).getYears();

		sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Você tem " + years + " anos de idade"));
		
		
	}

	public static void telegramRespostaPiadaChuckNorrisTradutor(Update update, ResponseEntity<ApiTranslateResponse> requestTradutorAPI, SendResponse sendResponse, TelegramBot bot) {

		ApiTranslateResponse body = requestTradutorAPI.getBody();

		String resultado = new StringBuilder().append(body.getResult().getPt()).toString();

		sendResponse = bot.execute(new SendMessage(update.message().chat().id(), resultado));

	}

	// METODOS AUXILIARES
	public static String concatenaNomeDoUsuario(String idUser, String... names) {

		String user = "";

		for (String name : names) {
			if (name != null && !name.isEmpty()) {
				user += name + " ";
			}
		}

		if (user.isEmpty()) {
			user = idUser;
		}

		return user.trim();

	}

	public static String nomeDoUsuario(Update update) {

		return concatenaNomeDoUsuario(update.message().from().id().toString(), update.message().from().firstName(),
				update.message().from().lastName());

	}

}
