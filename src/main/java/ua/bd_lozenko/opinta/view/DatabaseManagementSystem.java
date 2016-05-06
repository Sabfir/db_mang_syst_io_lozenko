package ua.bd_lozenko.opinta.view;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import ua.bd_lozenko.opinta.domain.Database;
import ua.bd_lozenko.opinta.domain.Table;
import ua.bd_lozenko.opinta.exceptions.MyDBException;
import ua.bd_lozenko.opinta.helpers.Service;
import ua.bd_lozenko.opinta.helpers.UserCommandHandler;

public class DatabaseManagementSystem {
	private Database database;

	public void start() throws MyDBException {
		Scanner scannerIn = new Scanner(System.in);
		
		System.out.println("This is " + Service.PRODUCT_NAME);
		System.out.println("Here is the list of commands you can use:");
		Service.showHelpHints();
		System.out.println("Have a nice day!");
		
		while (true) {
			System.out.println("Type comand ('Exit' to exit, 'Help' to help)");
			String command = scannerIn.nextLine().toUpperCase();
			
			if (UserCommandHandler.isInterruptUserWorkCommand(command)) {
				break;
			}
			
			if (UserCommandHandler.isHelpCommand(command)) {
				Service.showHelpHints();
				continue;
			} else if (UserCommandHandler.isCreateDatabaseCommand(command)) {
				createDatabaseFromCommand(command);
				continue;
			} else if (UserCommandHandler.isConnectToDatabaseCommand(command)) {
				connectToDatabase(command);
				continue;
			} else if (UserCommandHandler.isCreateTableCommand(command)) {
				if (database == null) {
					Service.showHintForCreateTableError();
					continue;
				}
				database.createTable(command);
			} else if (UserCommandHandler.isRenameTableCommand(command)) {
				if (database == null) {
					Service.showHintForCreateTableError();
					continue;
				}
				database.renameTable(command);
			} else if (UserCommandHandler.isInsertIntoTableCommand(command)) {
				if (database == null) {
					Service.showHintForCreateTableError();
					continue;
				}
				Table table = new Table(database);
				table.insertRow(command);
			} else if (UserCommandHandler.isSelectAllCommand(command)) {
				if (database == null) {
					Service.showHintForCreateTableError();
					continue;
				}
				Table table = new Table(database);
				table.selectAllRows(command);
			} else if (UserCommandHandler.isDeleteAllCommand(command)) {
				if (database == null) {
					Service.showHintForCreateTableError();
					continue;
				}
				Table table = new Table(database);
				table.deleteAllRows(command);
			} else {
				System.out.println("Incorrect command " + command);
				System.out.println("Use HELP command to see the list of commands available");
			}
		}
		scannerIn.close();
		
		System.out.println("Thankyou for using " + Service.PRODUCT_NAME);
	}
	
	private boolean createDatabaseFromCommand(String command) throws MyDBException {
		List<String> commandWords = Service.getListOfStringBySplitter(command, " ");
		
		String databaseName = commandWords.get(2);
		
		String fullpathOfDatabase = Service.getDesktopPath() + Service.PATH_SPLITTER + databaseName;
		Path path = Paths.get(fullpathOfDatabase);
        
		if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                System.out.println("DB " + databaseName + " created successfully");
            } catch (IOException e) {
                throw new MyDBException("Error while creating a DB. Path: " + path.toString(), e);
            }
        }

		return true;
	}

	private void connectToDatabase(String command) {
		List<String> commandWords = Service.getListOfStringBySplitter(command, " ");
		
		String databaseName = commandWords.get(1);
		
		String fullpathOfDatabase = Service.getDesktopPath() + Service.PATH_SPLITTER + databaseName;
		Path path = Paths.get(fullpathOfDatabase);
        
		if (!Files.exists(path)) {
            System.out.println("DB " + databaseName + " doesn't exist");
            return;
        }
		
		database = new Database(Service.getDesktopPath(), databaseName);
		System.out.println("Connected to the DB " + databaseName + " successfully");
	}
}
