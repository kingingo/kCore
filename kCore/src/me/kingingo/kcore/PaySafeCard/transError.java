package me.kingingo.kcore.PaySafeCard;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class transError
 */
@WebServlet("/transError")
public class transError extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Ausgabe des Info-Logs
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		out.println(session.getAttribute("info_log"));
	}
}
