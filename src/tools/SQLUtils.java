package tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

public class SQLUtils {
	
	private Statement stmt = null;
	
	public SQLUtils(Statement stmt) {
		this.stmt = stmt;
	}
	
	public Boolean isTableExist(String tableName) {
		boolean isTableExist = true;  
        try {
			ResultSet rs = stmt.executeQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name= '" + tableName + "'");
			if (rs.getInt(0) == 0)
				isTableExist = false;
			
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}  
        
        return isTableExist;
	}
	
	public void creatTable() {
		String createAppTable = "CREATE TABLE APP " +
                "(ID INT PRIMARY KEY     AUTOINCREMENT," +
                " NAME           TEXT    NOT NULL)" ;
		String createActivityTable = "CREATE TABLE ACTIVITY " +
				"(ID INT PRIMARY KEY     AUTOINCREMENT," +
                " NAME           TEXT    NOT NULL)" ;;
		String createElementTable = "CREATE TABLE ELEMENT ";
		
		
	}
	
	public void insertEle(String tableName, ArrayList<Map<String, String>> patterns) {
//		StringBuilder value = new StringBuilder();
//		name.append(patterns.get(0).)
//		for (int i=1; i<patterns.size(); i++) {
//			if (i == 0) {
//				
//			}
//		}
		String app_sql = "INSERT INTO APP (ID,NAME,AGE,ADDRESS,SALARY) " +
                "VALUES (1, 'Paul', 32, 'California', 20000.00 );";
	}
	
	public void getElement() {
		
	}
}
