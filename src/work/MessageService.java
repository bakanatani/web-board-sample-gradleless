package work;

import java.sql.*;
import java.util.List;

public class MessageService {
	/**
     * メッセージを取得するサービス
     *
	 * @return メッセージを格納したリスト
     */
	public List<MessageDto> getMessage() {
		
		MessageDao dao = new MessageDao();
		// DTOに格納する
		return dao.select();
	}

	/**
     * データベースにメッセージを保存する
     *
     * @param name 画面で入力された名前
     * @param name 画面で入力されたメッセージ
     */
	public void postMessage(String name, String message) {
		MessageDao dao = new MessageDao();
		// 新しいIDを取得する
		int nextId = dao.getNextId();
		
		// DTOに格納する
		MessageDto dto = new MessageDto();
		dto.setId(nextId);
		dto.setName(name);
		dto.setMessage(message);
		dto.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		// 登録を実行
		dao.insert(dto);
	}

}
