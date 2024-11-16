package studying.java.with.calculator.project.model;

import java.util.ArrayList;
import java.util.List;

public class Memory {

	private enum CommandType {
		TOZERO, SIGN, NUMBER, DIV, MULT, SUB, SUM, EQUALS, COMMA;
	};

	private static final Memory instance = new Memory();

	private final List<ObserverMemory> observers = new ArrayList<>();

	private CommandType lastOperation = null;
	private boolean replace = false;
	private String currentText = "";
	private String bufferText = "";

	private Memory() {
	}

	public static Memory getInstance() {
		return instance;
	}

	public void addObserver(ObserverMemory o) {
		observers.add(o);

	}

	public String getCurrentText() {
		return currentText.isEmpty() ? "0" : currentText;
	}

	public void processCommand(String theText) {

		CommandType commandType = toDetectCommandType(theText);

		if (commandType == null) {
			return;
		} else if (commandType == CommandType.TOZERO) {
			currentText = "";
			bufferText = "";
			replace = false;
			lastOperation = null;
		} else if (commandType == commandType.SIGN && currentText.contains("-")) {
			currentText = currentText.substring(1);
			
		} else if (commandType == commandType.SIGN && !currentText.contains("-")) {
			currentText = "-" + currentText;
			
		}else if (commandType == commandType.NUMBER || commandType == commandType.COMMA) {
			currentText = replace ? theText : currentText + theText;
			replace = false;
		} else {
			replace = true;

			currentText = getOperationResult();

			bufferText = currentText;

			lastOperation = commandType;
		}

		observers.forEach(o -> o.modifiedValue(getCurrentText()));
	}

	private String getOperationResult() {

		if (lastOperation == null || lastOperation == CommandType.EQUALS) {
			return currentText;
		}

		double bufferNumber = Double.parseDouble(bufferText.replace(",", "."));

		double currentNumber = Double.parseDouble(currentText.replace(",", "."));

		double result = 0;

		if (lastOperation == CommandType.SUM) {
			result = bufferNumber + currentNumber;
		} else if (lastOperation == CommandType.SUB) {
			result = bufferNumber - currentNumber;
		} else if (lastOperation == CommandType.MULT) {
			result = bufferNumber * currentNumber;
		} else if (lastOperation == CommandType.DIV) {
			result = bufferNumber / currentNumber;
		}

		String stringResult = Double.toString(result).replace(".", ",");
		
		boolean intNumber = stringResult.endsWith(",0");
		return intNumber ? stringResult.replace(",0", "") : stringResult;

	}

	private CommandType toDetectCommandType(String theText) {

		if (currentText.isEmpty() && theText == "0") {
			return null;
		}

		try {
			Integer.parseInt(theText);
			return CommandType.NUMBER;

		} catch (NumberFormatException e) {

			if ("AC".equals(theText)) {
				return CommandType.TOZERO;
			} else if ("/".equals(theText)) {
				return CommandType.DIV;
			} else if ("*".equals(theText)) {
				return CommandType.MULT;
			} else if ("+".equals(theText)) {
				return CommandType.SUM;
			} else if ("-".equals(theText)) {
				return CommandType.SUB;
			} else if ("=".equals(theText)) {
				return CommandType.EQUALS;
			} else if ("±".equals(theText)) {
				return CommandType.SIGN;
			} else if (",".equals(theText) && !currentText.contains(",")) {
				return CommandType.COMMA;
			}
		}
		return null;
	}

}
