package eu.epicpvp.kcore.Translation;

import java.io.File;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dev.wolveringer.dataserver.player.LanguageType;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.Util.UtilNumber;
import lombok.Getter;

public class Translation {

	@Getter
	private LanguageType language;
	@Getter
	private HashMap<String, String> translation;
	@Getter
	private Document document;
	private File file;
	@Getter
	private double percent = 0;

	public Translation(LanguageType language) {
		this.language = language;
		if(language==LanguageType.ENGLISH)this.percent=100;
		this.file = new File(File.separator+"root"+File.separator+"translations" + File.separator + language.getShortName() + File.separator + "EpicPvPMC Text.xml");
	}

	public String getVersion() {
		return document.getXmlVersion();
	}

	public double calculateDone(Translation source_language){
		double translated = 0;
		
		for(String name : this.translation.keySet()){
			if(!source_language.get(name).equalsIgnoreCase(this.translation.get(name))){
				translated++;
			}
		}
		this.percent = UtilMath.trim(2, translated / (UtilNumber.toDouble(this.translation.size())/100D));
		
		return this.percent;
	}
	
	public boolean load() {
		if (this.file.exists()) {
			try {
				TranslationManager.getInstance().getBuilder().reset();
				document = TranslationManager.getInstance().getBuilder().parse(file); //TODO create a new XML Paradiser here
				translation = new HashMap<>();

				NodeList list = document.getDocumentElement().getElementsByTagName("string");
				for (int i = 0; i < list.getLength(); i++) {
					Node n = list.item(i);
					if (n.getNodeType() == Node.ELEMENT_NODE) {
						Element e = (Element) n;
						translation.put(e.getAttribute("name"), ((Node) e.getChildNodes().item(0)).getNodeValue().trim().replaceAll("Â", ""));
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	
	public static String toText(String msg,Object... input){
		for(int i = 0 ; i < input.length ; i++){
			msg=msg.replaceAll("%s"+i, String.valueOf(input[i]));
		}
		return msg;
	}

	public String get(String name) {
		return get(name, null);
	}

	public String get(String name, Object... args) {
		if (translation.containsKey(name)) {
			if (args == null) return translation.get(name);
			return toText(translation.get(name), args);
		}
		return null;
	}
}
