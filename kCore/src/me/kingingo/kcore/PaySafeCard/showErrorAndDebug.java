package me.kingingo.kcore.PaySafeCard;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class showErrorAndDebug
 */
@WebServlet("/showErrorAndDebug")
public class showErrorAndDebug extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		//Auslesen des Error-Logs's und des Debugs sowie dem OS spezifischen Zeichen für einen Zeilenumbruch
		@SuppressWarnings("unchecked")
		ArrayList<HashMap<String, String>> error_log = (ArrayList<HashMap<String, String>>) session.getAttribute("error_log");
		String debug = (String) session.getAttribute("debug");
		String newLine = System.getProperty("line.separator");
		//Ausgeben des Debug's
		out.println(debug);
		//Ausgeben einer Trennzeile
		out.println("---------------------------------------------");
		//Ausgaben der msg-Variable jedes Eintrags im Error-Log
		for(int i=0; i<error_log.size(); i++){
			out.println(error_log.get(i).get("msg") + newLine);
		}
	}
}
