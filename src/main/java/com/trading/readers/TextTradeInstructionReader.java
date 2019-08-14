package com.trading.readers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.trading.enums.CurrencyType;
import com.trading.enums.InstructionType;
import com.trading.model.TradeInstruction;

/**
 * 
 * @author sandip.p.sangale
 *
 */
public class TextTradeInstructionReader implements TradeInstructionReader {
	private static final String DATE_FORMAT = "d MMM yyyy";
	private static final String DELIMITER = "\\|";
	private static final String INVALID_TRADE_INSTRUCTION_RECORD_8_PIPE_DELIMITED_VALUES_EXPECTED_IN_EACH_LINE = "Invalid TradeInstruction record, 8 Pipe delimited values expected in each line";
	private String fileName;
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

	public TextTradeInstructionReader(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Read file and process each line and mapToTradeInstruction
	 */
	@Override
	public List<TradeInstruction> readTradeInstructions() throws IOException {
		List<TradeInstruction> tradeInstructions;
		try (Stream<String> stream = Files.lines(Paths.get(this.fileName))) {
			tradeInstructions = stream.map(this::mapToTradeInstruction).collect(Collectors.toList());
		}
		return tradeInstructions;
	}

	/**
	 * Split each line on | (pipe) character and convert it into TradeInstruction
	 * Object
	 * 
	 * @param line
	 * @return
	 */
	private TradeInstruction mapToTradeInstruction(String line) {
		Pattern pattern = Pattern.compile(DELIMITER);
		String[] lineArray = pattern.split(line);

		if (lineArray.length < 8) {
			throw new IllegalArgumentException(
					INVALID_TRADE_INSTRUCTION_RECORD_8_PIPE_DELIMITED_VALUES_EXPECTED_IN_EACH_LINE);
		}

		String entity = lineArray[0].trim();
		InstructionType instructionType;
		if ("B".equalsIgnoreCase(lineArray[1].trim())) {
			instructionType = InstructionType.BUY;
		} else {
			instructionType = InstructionType.SELL;
		}
		Double agreedFx = Double.parseDouble(lineArray[2].trim());
		CurrencyType currency = CurrencyType.valueOf(lineArray[3].trim());
		LocalDate instructionDate = LocalDate.parse(lineArray[4].trim(), DATE_FORMATTER);
		LocalDate settlementDate = LocalDate.parse(lineArray[5].trim(), DATE_FORMATTER);
		int units = Integer.parseInt(lineArray[6].trim());
		Double pricePerUnit = Double.parseDouble(lineArray[7].trim());

		return new TradeInstruction(entity, instructionType, currency, agreedFx, instructionDate, settlementDate, units,
				pricePerUnit);
	}
}
