package me.kingingo.kcore.PaySafeCard;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.paysafecard.merchant.Debug;
import com.paysafecard.merchant.Log;
import com.paysafecard.merchant.SOPGClassicMerchantClient;

/**
 * Servlet implementation class index
 */
@WebServlet("/createDisposition")
public class createDisposition extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//Deklarieren einer privaten Variable, die sp�ter den Client enthalten wird
	private static SOPGClassicMerchantClient client;
	
	       
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * Hier werden die Formulareingaben ausgelesen und in Variablen gespeichert.
		 */
		String amount = request.getParameter("amount");
		String mtid = request.getParameter("mtid");
		String currency = request.getParameter("currency");
		String lang = request.getParameter("lang");
		boolean autokorrektur;
		// Je nach Wert des Feldes wird bei diesem und dem n�chsten Parameter ein boolean mit true bzw. false belegt. 
		if(request.getParameter("autokorrektur") != null && request.getParameter("autokorrektur").equals("on")){
			autokorrektur = true;
		}else{
			autokorrektur = false;
		}
		boolean debug;
		if(request.getParameter("debug") != null && request.getParameter("debug").equals("on")){
			debug = true;
		}else{
			debug = false;
		}
		
		// Initialisieren des SOPG-Clients
		client = new SOPGClassicMerchantClient("test", debug, lang, autokorrektur);
		// Setzen der Merchant-Zugangsdaten
		client.merchant("psc_teststeinweber_test", "KKIJSC8GXB75F");
		// �bergeben der Werte aus dem Formular an den Client
		client.setCustomer(amount, currency, mtid, "50000");
		// Setzen der URl's
		String baseurl;
		if(request.getServerPort() == 80){
			baseurl = request.getScheme() + "://" + request.getServerName() + request.getContextPath();
		}else{
			baseurl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		}
		String allParams = "?amount=" + amount + "&currency=" + currency + "&debug=" + debug + "&lang=" + lang + "&mtid=" + mtid + "&autokorrektur=" + autokorrektur;
		HttpSession session = request.getSession(true);
		session.setAttribute("param_debug", debug);
		session.setAttribute("params_autokorrektur", autokorrektur);
		session.setAttribute("param_lang", lang);
		try {
			client.setUrl(baseurl + "/transSuccess" + allParams, baseurl + "/transError", baseurl + "transPN?currency=" + currency + "&amount=" + amount);
			//URL zum Best�tigen des Zahlvorgangs auslesen und in der Variable confirmURL speichern
			String confirmURL = client.createDisposition();
			// Logeintr�ge und debug in der Session speichern
			session.setAttribute("debug", Debug.getDebug());
			//Debug leeren
			Debug.clear();
			session.setAttribute("error_log", Log.getLog("error"));
			session.setAttribute("info_log", Log.getInfoLog());
			//Pr�fen ob die Best�tigungs-URL nicht leer ist, wenn ja: weiterleiten
			if(confirmURL != "false"){
				response.sendRedirect(confirmURL);
			}else{
				response.sendRedirect(baseurl + "/transError");
			}
		} catch (Exception e) {
			// Do nothing
		}
	}

}
