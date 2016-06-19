package eu.epicpvp.kcore.ParticleManager.Particle;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import eu.epicpvp.kcore.Util.UtilParticle;
import eu.epicpvp.kcore.Util.UtilString;
import lombok.Getter;
import lombok.Setter;

public class ParticleText extends Particle{

	private String text;
	private ArrayList<Vector> vectors;
	protected BufferedImage image = null;
	@Getter
	@Setter
 	private boolean invert = false;
	@Getter
	@Setter
	private Font font;
	@Getter
	@Setter
	private float size= 0.2F;
	
	public ParticleText(String name, UtilParticle type) {
		this(name,type,null,new Font("Tahoma", 0, 16),0.2F,false);
	}
	
	public ParticleText(String name, UtilParticle type,String text,Font font,float size,boolean invert) {
		super(name, type);
		this.vectors=new ArrayList<>();
		this.size=size;
		this.font=font;
		this.invert=invert;
		if(text!=null)loadText(text);
	}
	
	public void loadText(String text){
		this.text=text;
		this.image = UtilString.stringToBufferedImage(this.font, this.text);
		
		int clr=0;
		Vector vector;
		for (int y = 0; y < this.image.getHeight(); y += 1){
			for (int x = 0; x < this.image.getWidth(); x += 1) {
				clr = this.image.getRGB(x, y);
				if ((this.invert) || (Color.black.getRGB() == clr)){
		            if ((!this.invert) || (Color.black.getRGB() != clr)){
		            	vector = new Vector(this.image.getWidth() / 2.0F - x, this.image.getHeight() / 2.0F - y, 0.0F).multiply(this.size);
//		 			    vector = UtilVector.rotateAroundAxisY(vector, - location.getYaw() * 0.01745329F);
		 			    vectors.add(vector);
		            }
		        }
			}
		}
	}

	@Override
	public void display(Player player) {
		Location location = player.getLocation();
		for(Vector v : vectors){
			getType().display(0, 8, location.add(v), 100);
			location.subtract(v);
		}
	}

}
