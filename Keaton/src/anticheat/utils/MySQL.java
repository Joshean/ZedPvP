package anticheat.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class MySQL {

	private Connection connection;

	public MySQL(String ip, String userName, String password, String db) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager
					.getConnection("jdbc:mysql://" + ip + "/" + db + "?user=" + userName + "&password=" + password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createPlayers() {
		try {
			if (!tableExist("players")) {
				String table = "CREATE TABLE player (uuid VARCHAR(64), number VARCHAR(10000), log VARCHAR(64);";
				PreparedStatement statement = connection.prepareStatement(table);
				statement.executeUpdate();
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addLog(Player player, String string) {
		try {
			PreparedStatement checkStatement = connection.prepareStatement("select number from player where uuid='" + player.getUniqueId() + "';");
			int results = 0;
			while(checkStatement.getResultSet().next()) {
				results++;
			}
			checkStatement.close();
			PreparedStatement statement = connection.prepareStatement("insert into player (uuid, number, log)\nvalues ('"+ player.getUniqueId() + "', '"+ results+1 + "', '" + string + "');");
            statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getLogs(Player player) {
		List<String> list = new ArrayList<String>();
		try {
			PreparedStatement checkStatement = connection.prepareStatement("select number from player where uuid='" + player.getUniqueId() + "';");
			int results = 1;
			while(checkStatement.getResultSet().next()) {
				checkStatement.getResultSet().getString(results);
				results++;
			}
			checkStatement.close();;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void removeLogs(Player player) {
		try {
			PreparedStatement checkStatement = connection.prepareStatement("delete from player where uuid='" + player.getUniqueId() +"';");
			
			checkStatement.close();;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean tableExist(String tableName) throws SQLException {
		boolean tExists = false;
		try (ResultSet rs = connection.getMetaData().getTables(null, null, tableName, null)) {
			while (rs.next()) {
				String tName = rs.getString("TABLE_NAME");
				if (tName != null && tName.equals(tableName)) {
					tExists = true;
					break;
				}
			}
		}
		return tExists;
	}

}