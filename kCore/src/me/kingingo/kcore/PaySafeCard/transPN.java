package me.kingingo.kcore.PaySafeCard;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.paysafecard.merchant.SOPGClassicMerchantClient;

/**
 * Servlet implementation class transSuccess
 */
@WebServlet("/transPN")
public class transPN extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SOPGClassicMerchantClient client;
    private String currency;
    private String amount;
    
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */ 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		currency = request.getParameter("currency");
		amount = request.getParameter("amount");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//doGet ausführen um die  Werte für die Get-Vrablen currency und amount auszulesen
		doGet(request, response);
		//Werte für debug, autokorrektur & lang aus der Session auslesen
		HttpSession session = request.getSession();
		boolean debug = (boolean) session.getAttribute("param_debug");
		String lang = (String) session.getAttribute("param_lang");
		boolean autokorrektur = (boolean) session.getAttribute("param_autokorrektur");
		//Weitere Werte auslesen
		String mtid = request.getParameter("mtid");
		// Initlaisieren des Clients
		client = new SOPGClassicMerchantClient("test", debug, lang, autokorrektur);
		// Setzen der Merchant-Zugangsdaten
		client.merchant("psc_teststeinweber_test", "KKIJSC8GXB75F");
		// getserialNumbers ausführen und den Status in einer Variable speichern
		try {
			String status = client.getSerialNumbers(mtid, currency, "").get("return");
			// Wenn Status execute ist: executeDebit ausführen
			if(status == "execute"){
				//Rückgabewert von executeDebit in Variable speichern und später berücksichtigen
				boolean retVal = client.executeDebit(amount, "1");
				if(retVal){
					//Wenn retVal true ist, kann die Transaktion als erfolgreich angesehen werden
				}else{
					//Andernfalls nicht
				}
			}
		} catch (Exception e) {
			// nothing
		}
	}
}
