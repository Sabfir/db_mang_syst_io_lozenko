package ua.bd_lozenko.opinta.helpers;

import java.util.ArrayList;
import java.util.List;

import ua.bd_lozenko.opinta.domain.Field;
import ua.bd_lozenko.opinta.domain.FieldInteger;
import ua.bd_lozenko.opinta.domain.FieldString;

public class Service {
	public static final String PRODUCT_NAME = "Database Management System";
	public static final String PATH_SPLITTER = "\\";
	public static final Field[] TYPES_SUPPORTED = {new FieldInteger(), new FieldString()};
	
	
	public static final String COMMAND_EXIT = "EXIT";
	public static final String COMMAND_HELP = "HELP";
	public static final String COMMAND_CREATE_DATABASE = "CREATE DATABASE";
	public static final Object COMMAND_CONNECT_DATABASE = "CONNECT";
	public static final String COMMAND_CREATE_TABLE = "CREATE TABLE";
	public static final Object COMMAND_RENAME_TABLE = "RENAME TABLE";
	public static final Object COMMAND_INSERT_INTO = "INSERT INTO";
	public static final Object COMMAND_SELECT_ALL = "SELECT *";
	public static final Object COMMAND_DELETE_ALL = "DELETE ALL";
	
	public static String getDesktopPath() {
		 if (System.getProperty("os.name").toLowerCase().indexOf("win")<0) {
	            System.err.println("Sorry, Windows only!");
	            return "";
	        }
		 
		 return System.getProperty("user.home")+"\\Desktop";
	}
	
	public static List<String> getListOfStringBySplitter(String string, String splitter) {
		List<String> commandWords = new ArrayList<String>();
		
		String[] splittedWords = string.trim().split(splitter);
		
		for (int i = 0; i < splittedWords.length; i++) {
			String currentValue = splittedWords[i];
			if (currentValue.isEmpty()) {
				continue;
			}
			
			commandWords.add(currentValue.trim().toUpperCase());
		}
		
		return commandWords;
	}
	
	public static String getStringFromListBySplitter(List<String> list, String splitter) {
		String result = "";
		
		if (list.size() == 0) {
			return result;
		}
		
		for (String string : list) {
			result += splitter + string;
		}
		
		result = result.substring(splitter.length());
		
		return result;
	}

	public static void showHelpHints() {
		System.out.println("------------HELP------------");
		
		System.out.println("Create DB: CREATE DATABASE DATABASE_NAME");
		System.out.println("Connect to DB: CONNECT DATABASE_NAME");
		System.out.println("Create table: CREATE TABLE TABLE_NAME (INT NUMBER, STRING NAME)");
		System.out.println("Rename table: RENAME TABLE TABLE_NAME INTO ANOTHER_TABLE");
		System.out.println("Rename table: INSERT INTO TABLE_NAME VALUES (1, CHECK)");
		System.out.println("Rename table: SELECT * FROM TABLE_NAME");
		System.out.println("Rename table: DELETE ALL FROM TABLE_NAME");
		System.out.println("Exit: 'exit'");
		
		System.out.println("----------------------------");
	}

	public static void showHintForCreateTableError() {
		System.out.println("You are not connected to any base.");
		System.out.println("Hint, use command 'CONNECT DATABASE_NAME' to connect to the db");
		System.out.println("Hint, use command 'CREATE DATABASE DATABASE_NAME' to create db");
	}

	public static boolean dbSupportsType(String type) {
		if (type == null || type.isEmpty()) {
			return false;
		}
		
		for (Field<?> field : TYPES_SUPPORTED) {
			if (type.equals(field.getTypeDescription())) {
				return true;
			}
		}
		
		return false;
	}
	
	public static String addSymbolsToTheRight(String string, int symbolsToAdd, String symbol) {
		String result = string;
		
		for (int i = 0; i < symbolsToAdd; i ++) {
			result += symbol;
		}
		
		return result;
	}
}
