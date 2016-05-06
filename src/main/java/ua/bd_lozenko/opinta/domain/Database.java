package ua.bd_lozenko.opinta.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import ua.bd_lozenko.opinta.exceptions.MyDBException;
import ua.bd_lozenko.opinta.helpers.Service;

public class Database {
	private String path;
	private String name;
	
	public boolean createTable(String command) throws MyDBException {
		List<String> commandWords = Service.getListOfStringBySplitter(command, "\\(");
		List<String> tableCommandWords = Service.getListOfStringBySplitter(commandWords.get(0), " ");
		
		String tableName = tableCommandWords.get(2);
		
		List<String> columnsCommandWords = Service.getListOfStringBySplitter(commandWords.get(1), "\\,");
		
		String lastElement = columnsCommandWords.get(columnsCommandWords.size()-1);
		
		columnsCommandWords.set(columnsCommandWords.size()-1, lastElement.substring(0, lastElement.length()-1));
		
		if (! validateTableHeader(columnsCommandWords)) {
			return false;
		}
		
		Table table = createTableByName(tableName);
		table.setHeader(columnsCommandWords);
		
		return true;
	}
	
	public boolean renameTable(String command) throws MyDBException {
		List<String> commandWords = Service.getListOfStringBySplitter(command, " ");
		
		String name = commandWords.get(2);
		String newName = commandWords.get(4);
		
		String pathUrl = getPath() + Service.PATH_SPLITTER + getName();
		Path sourcePath      = Paths.get(pathUrl + Service.PATH_SPLITTER + name);
		Path destinationPath = Paths.get(pathUrl + Service.PATH_SPLITTER + newName);

		try {
		    Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
		    System.out.println("Table renamed from " + name + " to " + newName + ". DB: " + pathUrl);
		} catch (IOException e) {
			throw new MyDBException("Error while renaming a Table from " + name + " to " + newName + ". DB: " + pathUrl, e);
		}

		return true;
	}
	
	private Table createTableByName(String name) throws MyDBException {
		String tablePath = this.getPath() + Service.PATH_SPLITTER + this.getName();
		String fullpathOfTable = tablePath + Service.PATH_SPLITTER + name;
		Path path = Paths.get(fullpathOfTable);
        
		if (!Files.exists(path)) {
            try {
                Files.createFile(path);
                System.out.println("Table " + name + " created successfully. DB: " + tablePath);
            } catch (IOException e) {
                throw new MyDBException("Error while creating a Table" + name + ". DB: " + tablePath, e);
            }
        } else {
        	System.out.println("Table " + name + " already exists. DB: " + tablePath);
        }
		
		Table table = new Table(name, this);
		
		return table;
	}
	
	private boolean validateTableHeader(List<String> columnsCommandWords) {
		String type = "";
		
		for (int i = 0; i < columnsCommandWords.size(); i++) {
			List<String> fieldData = Service.getListOfStringBySplitter(columnsCommandWords.get(i), " ");
			
			type = fieldData.get(0);
			
			if (! Service.dbSupportsType(type)) {
				System.out.println("Isn't supported type: " + type);
				return false;
			}
		}
		
		return true;
	}
	
	public Database(String path, String name) {
		this.path = path;
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
