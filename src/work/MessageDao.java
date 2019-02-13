package work;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class MessageDao {
	
    private final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    private final String JDBC_URL = "jdbc:mysql://localhost/develop?useSSL=false";
    private final String USER_ID = "root";
    private final String USER_PASS = "pass";
    
	/**
     * データベースにメッセージを保存する
     *
     * @param dto 投稿メッセージを格納したMessageDtoクラス
	 * @return TRUE:成功、FALSE:失敗
     */
	public boolean insert(MessageDto dto) {
		
        // JDBCドライバのロード
        try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO message_board ( ");
        builder.append("    id,                     ");
        builder.append("    name,                   ");
        builder.append("    message,                ");
        builder.append("    created_at              ");
        builder.append(") VALUES (                  ");
        builder.append("    ?,                      ");
        builder.append("    ?,                      ");
        builder.append("    ?,                      ");
        builder.append("    ?                       ");
        builder.append(")                           ");
		
		// データベースとの接続を行う(try-with-resourcesを使用することで、Finallyのclose処理は不要)
		try (Connection con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
				PreparedStatement ps = con.prepareStatement(builder.toString());) {
			
			// キーとなるIDには、新しいIDを取得して設定する
			ps.setInt(1, dto.getId());
			// その他の項目はそのまま格納
			ps.setString(2, dto.getName());
			ps.setString(3, dto.getMessage());
			ps.setTimestamp(4, dto.getCreatedAt());

			// SQLを実行
			int result = ps.executeUpdate();
			// 登録行数が1の場合にTRUEを返却
			return (result == 1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
    /**
     * データベースからメッセージを取得する
     *
     * @return メッセージを格納したリスト
     */
	public List<MessageDto> select() {
		List<MessageDto> list = new ArrayList<MessageDto>();
		
        // JDBCドライバのロード
        try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
		
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT          ");
		sqlBuilder.append("  id,           ");
		sqlBuilder.append("  name,         ");
		sqlBuilder.append("  message,      ");
		sqlBuilder.append("  created_at    ");
		sqlBuilder.append("FROM            ");
		sqlBuilder.append("  message_board ");
		sqlBuilder.append("ORDER BY        ");
		sqlBuilder.append("  id DESC       ");

		// データベースとの接続を行う(try-with-resourcesを使用することで、Finallyのclose処理は不要)
		try (Connection con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
				PreparedStatement ps = con.prepareStatement(sqlBuilder.toString());
				// SQLを実行して取得結果をリザルトセットに格納
				ResultSet rs = ps.executeQuery();) {
			// リザルトセットから1レコードずつデータを取り出す
			while (rs.next()) {
	              // 取得結果を格納するDtoをインスタンス化
                MessageDto dto = new MessageDto();
                // Dtoに取得結果を格納
                dto.setId(rs.getInt("id"));
                dto.setName(rs.getString("name"));
                dto.setMessage(rs.getString("message"));
                dto.setCreatedAt(rs.getTimestamp("created_at"));
                // Dtoに格納された1レコード分のデータをリストに詰める
                list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 呼び出し元に取得結果を返却
		return list;
	}

    /**
     * 新しいID（現在のIDの最大値＋１）を取得する
     *
     * @return 新しいID
     */
	public int getNextId() {
		int nextId = 0;
          
        // JDBCドライバのロード
        try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT              ");
		sqlBuilder.append("  MAX(id) as max_id ");
		sqlBuilder.append("FROM                ");
		sqlBuilder.append("  message_board     ");
		
		// データベースとの接続を行う(try-with-resourcesを使用することで、Finallyのclose処理は不要)
		try (Connection con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
				PreparedStatement ps = con.prepareStatement(sqlBuilder.toString());
				// SQLを実行して取得結果をリザルトセットに格納
				ResultSet rs = ps.executeQuery();) {
			nextId = rs.next() ? rs.getInt("max_id") + 1 : 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 呼び出し元に取得結果を返却
		return nextId;
	}
	
}
