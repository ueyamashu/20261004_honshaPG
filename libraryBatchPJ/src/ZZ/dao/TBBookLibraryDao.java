package ZZ.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TBBookLibraryDao {
	/**
	 * 
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public ResultSet selectAllBooks() throws SQLException, ClassNotFoundException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		Class.forName("oracle.jdbc.OracleDriver");
		conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XEPDB1", "test", "test");

		String sql = "Select * from BOOK ORDER BY ISBN ASC";
		stmt = conn.createStatement();

		rs = stmt.executeQuery(sql);
		
		return rs;
	}

	
	/**
	 * 
	 * @param isbn
	 * @return rs
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public ResultSet libraryCount(String isbn) throws ClassNotFoundException, SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Class.forName("oracle.jdbc.OracleDriver");
		conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XEPDB1", "test", "test");

		String sql = "SELECT COUNT(*) as BOOK_CNT FROM library WHERE isbn = ? ";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, isbn);
	
		rs = pstmt.executeQuery();

		return rs;
	}

	/**
	 * 
	 * @param inputStr
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public boolean libraryInsert(String sql) throws ClassNotFoundException, SQLException {
		Connection conn = null;
		Statement stmt = null;

		Class.forName("oracle.jdbc.OracleDriver");
		conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XEPDB1", "test", "test");

		stmt = conn.createStatement();
		int rtn = stmt.executeUpdate(sql);

		if (rtn == 0) {
			return false;
		}

		return true;
	}
	
	/**
	 * 
	 * @param sql
	 * @return rs
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public ResultSet selectBookIdList() throws ClassNotFoundException, SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		Class.forName("oracle.jdbc.OracleDriver");
		conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XEPDB1", "test", "test");
		
		String sql = "SELECT BOOK_ID,ISBN,ARRIVAL_DATE FROM LIBRARY WHERE TO_CHAR(SYSDATE , 'YYYY-MM-DD') = TO_CHAR(REGISTER_DATE , 'YYYY-MM-DD') and UPDATE_ID = '9999'ORDER BY BOOK_ID ASC";
		stmt = conn.createStatement();

		rs = stmt.executeQuery(sql);

		return rs;
	}
}
