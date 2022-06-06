package com.bot.dto.commandos;

import com.bot.dto.commandos.impl.*;

public enum ComandoEnum implements ComandoInterface {

	COMANDO_START("/start"){
		@Override
		public Comando retornaNovoComando() {
			return new Start();
		}
	},
	COMANDO_TEMPO("/tempo"){
		@Override
		public Comando retornaNovoComando() {
			return new Tempo();
		}
	},
	COMANDO_CONSELHO("/conselho"){
		@Override
		public Comando retornaNovoComando() {
			return new Conselho();
		}
	},
	COMANDO_CALCULA_IDADE("/calcula_sua_idade"){
		@Override
		public Comando retornaNovoComando() {
			return new CalculaIdade();
		}
	},
	COMANDO_PIADA_CHUCK_NORRIS("/piada_chuck_norris"){
		@Override
		public Comando retornaNovoComando() {
			return new PiadaChuckNorris();
		}
	},
	COMANDO_ORDENAR("/ordenar"){
		@Override
		public Comando retornaNovoComando() {
			return new Ordenar();
		}
	},
	COMANDO_NAO_ENCONTRADO("$$$%%%###"){
		@Override
		public Comando retornaNovoComando() {
			return new ComandoNaoEncontrado();
		}
	};

	private String value;

	private ComandoEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return value;
	}

	public static ComandoEnum fromString(String comandoString) {
		for (ComandoEnum comandoEnum : ComandoEnum.values()) {
			if (comandoString.startsWith(comandoEnum.getValue())) {
				return comandoEnum;
			}
		}
		return COMANDO_NAO_ENCONTRADO;
	}

}
