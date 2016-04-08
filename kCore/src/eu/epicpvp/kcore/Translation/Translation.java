package eu.epicpvp.kcore.Translation;

import java.io.File;

import org.w3c.dom.Document;

import eu.epicpvp.kcore.Util.UtilFile;
import lombok.Getter;

public class Translation {

	@Getter
	private Language language;
	@Getter
	private Document document;
	private File file;
	
	public Translation(Language language){
		this.language=language;
		this.file=new File( UtilFile.getPluginFolder(TranslationManager.getInstance())+File.separator+"translations"+File.separator+language.name().toLowerCase()+".yml" );
	}
	
	public String getVersion(){
		return document.getXmlVersion();
	}
	
	public boolean load(){
		if(this.file.exists()){
			try {
				document = TranslationManager.getBuilder().parse( new File(language.name().toLowerCase()+".xml") );
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
		if(document.getDocumentElement().hasAttribute(name)){
			if(args==null)return document.getDocumentElement().getAttribute(name);
			return String.format(document.getDocumentElement().getAttribute(name), args);
		}
		return "";
	}
}
