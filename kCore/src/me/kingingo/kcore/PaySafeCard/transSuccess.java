package me.kingingo.kcore.PaySafeCard;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.paysafecard.merchant.Log;
import com.paysafecard.merchant.SOPGClassicMerchantClient;

/**
 * Servlet implementation class transSuccess
 */
@WebServlet("/transSuccess")
public class transSuccess extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SOPGClassicMerchantClient client;
     
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Auslesen der Variablen
		String amount = request.getParameter("amount");
		boolean debug = Boolean.getBoolean(request.getParameter("debug"));
		String lang = request.getParameter("lang");
		String currency = request.getParameter("currency");
		String mtid = request.getParameter("mtid");
		boolean autokorrektur = Boolean.getBoolean(request.getParameter("autokorrektur"));
		// Initlaisieren des Clients
		client = new SOPGClassicMerchantClient("test", debug, lang, autokorrektur);
		// Setzen der Merchant-Zugangsdaten
		client.merchant("psc_teststeinweber_test", "KKIJSC8GXB75F");
		// getserialNumbers ausführen und den Status in einer Variable speichern
		PrintWriter out = response.getWriter();
		try {
			String status = client.getSerialNumbers(mtid, currency, "").get("return");
			// Wenn Status execute: executeDebit ausführen
			if(status == "execute"){
				// Speichern des Rückgabewertes von executeDebit in der retVal-Variable
				boolean retVal = client.executeDebit(amount, "1");
				if(retVal){
					// Wenn executeDebit true zurückgab, dann kann die Transaktion als erfolgreich angesehen werden
				}else{
					// Andernfalls nicht
				}
			}
		} catch (Exception e) {
			// nothing
		}
		//Ausgabe des Logs (Typ: Info)
		out.println(Log.getInfoLog());
	}
}
