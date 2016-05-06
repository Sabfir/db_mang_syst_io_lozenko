package ua.bd_lozenko.opinta;

import ua.bd_lozenko.opinta.exceptions.MyDBException;
import ua.bd_lozenko.opinta.view.DatabaseManagementSystem;

public class App {
	public static void main( String[] args ) {
    	try {
			DatabaseManagementSystem databaseManagementSystem = new DatabaseManagementSystem();
			databaseManagementSystem.start();
		} catch (MyDBException e) {
			System.out.println("An error occurred while working with the database!" + e.getMessage());
			e.printStackTrace();
		}
	}
}
