package work;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import work.MessageDto;

@SuppressWarnings("serial")
public class WebBoardServlet extends HttpServlet {
	/**
     * GETリクエストで呼ばれる処理
     *
     * @param request Httpリクエスト
     * @param response Httpレスポンス
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		request.setCharacterEncoding("UTF-8");

		MessageService service = new MessageService();
		List<MessageDto> list = service.getMessage();

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("  <style type=\"text/css\">");
        out.println("    <!-- body {font-family: meiryo, arial; max-width: 80%; margin: 0 auto; } -->");
        out.println("    <!-- h1 {text-align: center; background: lime; } -->");
        out.println("  </style>");
        out.println("  <meta charset=\"UTF-8\">");
        out.println("  <title>IT基礎研修掲示板</title>");
        out.println("</heda>");
        out.println("<body>");
        out.println("  <h1>IT基礎研修掲示板</h1>");
        out.println("  <form action=\"./\" method=\"post\">");
        out.println("    <input type=\"text\" name=\"name\" placeholder=\"name\" required></br>");
        out.println("    <textarea name=\"message\" rows=\"4\" cols=\"50\" maxlength=\"250\" placeholder=\"message\"></textarea></br>");
        out.println("    </br>");
        out.println("    <input type=\"submit\" value=\"投稿\">");
        out.println("  </form>");

        for (MessageDto message : list){
			out.println("<hr>");
			out.println("<p>No: " + message.getId() + " / Name: " + escape(message.getName()) + " / Time: "
					+ message.getCreatedAt() + "</p>");
			out.println("<p>" + escape(message.getMessage()) + "</p>");
        }

        out.println("</body>");
        out.println("</html>");
	}

	/**
     * POSTリクエストで呼ばれる処理
     *
     * @param request Httpリクエスト
     * @param response Httpレスポンス
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		
		String name = request.getParameter("name");
		String message = request.getParameter("message");

		MessageService service = new MessageService();
		service.postMessage(name, message);
	
		// リダイレクトを設定
		response.sendRedirect("./");
	}

	/**
     * エスケープ処理（XSS対策）
     *
     * @param input エスケープ処理前の文字列
     * @return エスケープ処理後の文字列
     */
	private String escape(String input) {
		String target = input;
		
		if (target == null ) {
			return "";
		}
		
		// 「&」を変換
        target = target.replace("&", "&amp;");
		// 「<」を変換
        target = target.replace("<", "&lt;");
		// 「>」を変換
        target = target.replace(">", "&gt;");
		// 「"」を変換
        target = target.replace("\"", "&quot;");
		// 「'」を変換
        target = target.replace("'", "&#039;");
        // 改行コードを変換
        target = target.replace("\n", "<br>");
        
		return target;
	}
}
