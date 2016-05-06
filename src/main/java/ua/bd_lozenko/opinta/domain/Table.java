package ua.bd_lozenko.opinta.domain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import ua.bd_lozenko.opinta.exceptions.MyDBException;
import ua.bd_lozenko.opinta.helpers.Service;

public class Table {
	private String name;
	private Database database;
	
	public boolean insertRow(String command) throws MyDBException {
		List<String> commandWords = Service.getListOfStringBySplitter(command, "\\(");
		List<String> tableCommandWords = Service.getListOfStringBySplitter(commandWords.get(0), " ");
		
		this.name = tableCommandWords.get(2);
		
		List<String> columnsCommandWords = Service.getListOfStringBySplitter(commandWords.get(1), "\\,");
		
		String lastElement = columnsCommandWords.get(columnsCommandWords.size()-1);
		
		columnsCommandWords.set(columnsCommandWords.size()-1, lastElement.substring(0, lastElement.length()-1));
		
		if (! validateTableRow(columnsCommandWords)) {
			System.out.println("Incorrect insert command: " + Service.getStringFromListBySplitter(columnsCommandWords, ","));
			return false;
		}
		
		addRow(Service.getStringFromListBySplitter(columnsCommandWords, " "));
		
		return true;
	}
	
	public void selectAllRows(String command) throws MyDBException {
		List<String> commandWords = Service.getListOfStringBySplitter(command, " ");
		
		this.name = commandWords.get(3);
		
		List<String> lines = new ArrayList<String>();
		try {
			lines = Files.readAllLines(getFullPath(), Charset.defaultCharset());
		} catch (IOException e) {
			throw new MyDBException("Can't read from table " + getFullPath().toString(), e);
		}
		
		for (String line : lines) {
			System.out.println(line);
		}
	}
	
	public void deleteAllRows(String command) throws MyDBException {
		List<String> commandWords = Service.getListOfStringBySplitter(command, " ");
		
		this.name = commandWords.get(3);
		
		String contentRow = getHeader();
		
		try {
			FileWriter fw = new FileWriter(getFullPath().toFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(contentRow);
			bw.newLine();
			bw.close();
			
			System.out.println("All data deleted from the table " + name + ". DB: " + database.getName());
		} catch (IOException e) {
			throw new MyDBException("Error while writing row to the table " + getFullPath().toString(), e);
		}
	}
	
	public void setHeader(List<String> columns) throws MyDBException {
		String tablePath = database.getPath() + Service.PATH_SPLITTER + database.getName();
		String fullpathOfTable = tablePath + Service.PATH_SPLITTER + name;
		Path path = Paths.get(fullpathOfTable);
        
		if (!Files.exists(path)) {
            throw new MyDBException("Error while writing header to the table " + name + ". DB: " + tablePath);
        }
		
		for (int i = 0; i < columns.size(); i++) {
			List<String> typeData = Service.getListOfStringBySplitter(columns.get(i), " ");
			String type = typeData.get(0);
			
			Field<?> field = getFieldByType(type);
			int spacesToAdd = field.getValueLength() - columns.get(i).length();
			columns.set(i, Service.addSymbolsToTheRight(columns.get(i), spacesToAdd, " "));
		}
		
		String content = Service.getStringFromListBySplitter(columns, "");
		
		try {
			FileWriter fw = new FileWriter(path.toFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			throw new MyDBException("Error while writing header to the table " + name + ". DB: " + tablePath, e);
		}
	}
	
	private Field<?> getFieldByType(String type) {
		if (type == null || type.isEmpty()) {
			return null;
		}
		
		for (Field<?> field : Service.TYPES_SUPPORTED) {
			if (type.equals(field.getTypeDescription())) {
				return field;
			}
		}
		
		return null;
	}
	
	private void addRow(String content) throws MyDBException {
		List<Field> tableFields = getFields();
		
		List<String> contentWords = Service.getListOfStringBySplitter(content, " ");
		
		for (int i = 0; i < contentWords.size(); i++) {
			Field field = tableFields.get(i);
			
			int spacesToAdd = field.getValueLength() - contentWords.get(i).length();
			contentWords.set(i, Service.addSymbolsToTheRight(contentWords.get(i), spacesToAdd, " "));
		}
		
		String contentRow = Service.getStringFromListBySplitter(contentWords, "");
		System.out.println("Content:" + contentRow);
		
		try {
			FileWriter fw = new FileWriter(getFullPath().toFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(contentRow);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			throw new MyDBException("Error while writing row to the table " + getFullPath().toString(), e);
		}
	}

	private boolean validateTableRow(List<String> columnsCommandWords) throws MyDBException {
		List<Field> tableFields = getFields();
		
		for (int i = 0; i < columnsCommandWords.size(); i++) {
			Field field = tableFields.get(i);
			String value = columnsCommandWords.get(i);
			
			if (! field.validate(value)) {
				return false;
			}
		}
		
		return true;
	}
	
	private String getHeader() throws MyDBException {
		String line = "";
		try {
			FileReader fr = new FileReader(getFullPath().toFile());
			BufferedReader br = new BufferedReader(fr);
			line = br.readLine();
			br.close();
		} catch (IOException e) {
			throw new MyDBException("Can't read header from table " + getFullPath().toString(), e);
		}
		
		return line;
	}
	
	private List<Field> getFields() throws MyDBException {
		List<Field> result = new ArrayList<Field>();
		
		String header = getHeader();
		
		List<String> headerWords = Service.getListOfStringBySplitter(header, " ");
		for (int i = 0; i < headerWords.size(); i++) {
			if (i % 2 != 0) {
				continue;
			}
			
			String type = headerWords.get(i);
			Field field = getFieldByType(type);
			
			result.add(field);
		}
		
		return result;
	}
	
	public Path getFullPath() {
		return Paths.get(database.getPath() + Service.PATH_SPLITTER + database.getName() + Service.PATH_SPLITTER + name);
	}

	public Table(Database database) {
		this.database = database;
	}

	public Table(String name, Database database) {
		this.name = name;
		this.database = database;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
