package com.bot;

public class Constantes {
	
	//CONSTANTES ESTÁTICAS
		public static final String BOT_NAO_ENTENDI = "Desculpe eu não entendi seu comando, pode dizer novamente? ";
		public static final String BOT_PROBLEMA_GERAL = "Desculpe estou com algum problema... Tente novamente mais tarde. ";
		public static final String BOT_SE_QUISER = "Se quiser continuar iteragindo comigo, selecione uma das opções: \n";
		public static String TAB = "     ";
		public static String BOT_FUNCIONALIDADES = new StringBuilder()
													.append(TAB).append("/tempo \n")
													.append(TAB).append("/conselho \n")
													.append(TAB).append("/calcula_sua_idade \"Passe sua data de nascimento padrão dd/MM/yyyy\"  \n")
													.append(TAB).append("/piada_Chuck_Norris \n")
													.append(TAB).append("/ordenar \"Passe os números que você deseja ordenar\" \n")
													.toString();

		public static final String BOT_ORDENAR_FUNC_ERRO = "Desculpe talvez minha funcionalidade de ordenação não esteja funcionando, certifique-se de que você tenha passado na frente do comando /ordenar apenas números com um espaço em branco entre cada número. Depois tente novamente . ";
		public static final String BOT_PIADA_CHUCK_FUNC_ERRO = "Desculpe talvez minha funcionalidade de piadas do Chuck Norris não esteja funcionando. Tente novamente mais tarde. ";
		public static final String BOT_PIADA_CONSELHO_FUNC_ERRO = "Desculpe talvez minha funcionalidade de conselho não esteja funcionando. Tente novamente mais tarde. ";
		public static final String BOT_PIADA_TEMPO_FUNC_ERRO = "Desculpe talvez minha funcionalidade de previsão do tempo não esteja funcionando. Tente novamente mais tarde. ";
		public static final String BOT_PIADA_IDADE_FUNC_ERRO = "Desculpe talvez minha funcionalidade de calculo de idadade do tempo não esteja funcionando. Tente novamente mais tarde. ";
}
