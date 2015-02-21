package me.kingingo.kcore.TextParticle;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilParticle;
import me.kingingo.kcore.Util.UtilString;
import me.kingingo.kcore.Util.UtilVector;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class PictureParticle extends kListener{

	@Getter
	@Setter
	private File file;
	@Getter
	@Setter
	private UtilParticle particle;
	@Getter
	@Setter
	private Location location;
	@Getter
	@Setter
	protected BufferedImage image = null;
	@Getter
	@Setter
	private int stepX = 1;
	@Getter
	@Setter
 	private int stepY = 1;
	@Getter
	@Setter
	private Vector vector;
	@Getter
	@Setter
	private Font font;
	@Getter
	@Setter
	private float size= 0.2F;
	@Getter
	@Setter
 	private int clr = 0;
	@Getter
	@Setter
 	private boolean invert = false;
	
	public PictureParticle(JavaPlugin instance,File file,Location location,UtilParticle particle){
		super(instance,"[TextParticle]:");
		this.file=file;
		this.location=location;
		this.particle=particle;
		this.font=new Font("Tahoma", 0, 16);
	}
	
	public void stop(){
		HandlerList.unregisterAll(this);
	}
	
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.FASTEST)return;
		try {
			this.image = ImageIO.read(getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int y = 0; y < this.image.getHeight(); y += this.stepY){
			for (int x = 0; x < this.image.getWidth(); x += this.stepX) {
				clr = this.image.getRGB(x, y);
				if ((this.invert) || (Color.black.getRGB() == clr)){
		            if ((!this.invert) || (Color.black.getRGB() != clr)){
		            	vector = new Vector(this.image.getWidth() / 2.0F - x, this.image.getHeight() / 2.0F - y, 0.0F).multiply(this.size);
		 			    UtilVector.rotateAroundAxisY(vector, -location.getYaw() * 0.01745329F);
		 			    particle.display(0, 8, location.add(vector), 100);
		 			    location.subtract(vector);
		            }
		        }
			}
		}       
	}
	
}
