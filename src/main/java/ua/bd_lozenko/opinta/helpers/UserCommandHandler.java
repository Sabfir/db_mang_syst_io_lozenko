package ua.bd_lozenko.opinta.helpers;

import java.util.List;

public class UserCommandHandler {
	
	public static boolean isInterruptUserWorkCommand(String command) {
		return command.trim().equals(Service.COMMAND_EXIT);
	}
	
	public static boolean isHelpCommand(String command) {
		return command.trim().equals(Service.COMMAND_HELP);
	}

	public static boolean isConnectToDatabaseCommand(String command) {
		List<String> commandWords = Service.getListOfStringBySplitter(command, " ");
		
		if (commandWords.size() != 2) {
			return false;
		}
		
		String commandToDo = commandWords.get(0);
		if (! commandToDo.equals(Service.COMMAND_CONNECT_DATABASE)) {
			return false;
		}
		
		return true;
	}

	public static boolean isCreateDatabaseCommand(String command) {
		List<String> commandWords = Service.getListOfStringBySplitter(command, " ");
		
		if (commandWords.size() != 3) {
			return false;
		}
		
		String commandToDo = commandWords.get(0) + " " + commandWords.get(1);
		if (! commandToDo.equals(Service.COMMAND_CREATE_DATABASE)) {
			return false;
		}
		
		return true;
	}
		
	public static boolean isCreateTableCommand(String command) {
		List<String> commandWords = Service.getListOfStringBySplitter(command, "\\(");
		if (commandWords.size() != 2) {
			return false;
		}
		
		List<String> tableCommandWords = Service.getListOfStringBySplitter(commandWords.get(0), " ");
		if (tableCommandWords.size() != 3) {
			return false;
		}
		
		String commandToDo = tableCommandWords.get(0) + " " + tableCommandWords.get(1);
		if (! commandToDo.equals(Service.COMMAND_CREATE_TABLE)) {
			return false;
		}
		
		List<String> columnsCommandWords = Service.getListOfStringBySplitter(commandWords.get(1), "\\,");
		
		String lastElement = columnsCommandWords.get(columnsCommandWords.size()-1);
		if (! lastElement.substring(lastElement.length()-1).equals(")")) {
			return false;
		}
		
		return true;
	}
	
	public static boolean isRenameTableCommand(String command) {
		List<String> commandWords = Service.getListOfStringBySplitter(command, " ");
		if (commandWords.size() != 5) {
			return false;
		}
		
		String commandToDo = commandWords.get(0) + " " + commandWords.get(1);
		if (! commandToDo.equals(Service.COMMAND_RENAME_TABLE)) {
			return false;
		}
		
		return true;
	}

	public static boolean isInsertIntoTableCommand(String command) {
		List<String> commandWords = Service.getListOfStringBySplitter(command, "\\(");
		if (commandWords.size() != 2) {
			return false;
		}
		
		List<String> tableCommandWords = Service.getListOfStringBySplitter(commandWords.get(0), " ");
		if (tableCommandWords.size() != 4) {
			return false;
		}
		
		String commandToDo = tableCommandWords.get(0) + " " + tableCommandWords.get(1);
		if (! commandToDo.equals(Service.COMMAND_INSERT_INTO)) {
			return false;
		}
		
		List<String> columnsCommandWords = Service.getListOfStringBySplitter(commandWords.get(1), "\\,");
		
		String lastElement = columnsCommandWords.get(columnsCommandWords.size()-1);
		if (! lastElement.substring(lastElement.length()-1).equals(")")) {
			return false;
		}
		
		
		return true;
	}

	public static boolean isSelectAllCommand(String command) {
		List<String> commandWords = Service.getListOfStringBySplitter(command, " ");
		if (commandWords.size() != 4) {
			return false;
		}
		
		String commandToDo = commandWords.get(0) + " " + commandWords.get(1);
		if (! commandToDo.equals(Service.COMMAND_SELECT_ALL)) {
			return false;
		}
		
		return true;
	}

	public static boolean isDeleteAllCommand(String command) {
		List<String> commandWords = Service.getListOfStringBySplitter(command, " ");
		if (commandWords.size() != 4) {
			return false;
		}
		
		String commandToDo = commandWords.get(0) + " " + commandWords.get(1);
		if (! commandToDo.equals(Service.COMMAND_DELETE_ALL)) {
			return false;
		}
		
		return true;
	}
}
