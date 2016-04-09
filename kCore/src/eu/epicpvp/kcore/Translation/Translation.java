package eu.epicpvp.kcore.Translation;

import java.io.File;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.avaje.ebean.enhance.asm.Attribute;

import eu.epicpvp.kcore.Util.UtilFile;
import lombok.Getter;

public class Translation {

	@Getter
	private Language language;
	@Getter
	private HashMap<String,String> translation;
	@Getter
	private Document document;
	private File file;
	
	public Translation(Language language){
		this.language=language;
		this.file=new File( UtilFile.getPluginFolder(TranslationManager.handler.getInstance())+File.separator+"translations"+File.separator+language.getFolder()+File.separator+"EpicPvPMC Text.xml" );
	}
	
	public String getVersion(){
		return document.getXmlVersion();
	}
	
	public boolean load(){
		if(this.file.exists()){
			try {
				document = TranslationManager.handler.getBuilder().parse( file );
				translation=new HashMap<>();
				
				NodeList list = document.getDocumentElement().getElementsByTagName("string");
				for(int i = 0; i<list.getLength() ; i++){
					Node n = list.item(i);
	                if(n.getNodeType() == Node.ELEMENT_NODE){
	                	Element e = (Element)n;
	                	translation.put(e.getAttribute("name"), ((Node)e.getChildNodes().item(0)).getNodeValue().trim());
	                }
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	
	public String get(String name){
		return get(name,null);
	}
	
	public String get(String name,Object... args){
		if(translation.containsKey(name)){
			if(args==null)return translation.get(name);
			return String.format(translation.get(name), args);
		}
		return null;
	}
}
