package eu.epicpvp.kcore.ParticleManager.Particle;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import eu.epicpvp.kcore.Util.UtilParticle;
import eu.epicpvp.kcore.Util.UtilVector;
import lombok.Getter;
import lombok.Setter;

public class ParticlePicture extends Particle{

	private ArrayList<Vector> vectors;
	@Getter
	@Setter
	private BufferedImage image = null;
	@Getter
	@Setter
	private float size= 0.2F;
	@Getter
	@Setter
 	private boolean invert = false;
	

	public ParticlePicture(String name, UtilParticle type) {
		this(name,type,null);
	}
	
	public ParticlePicture(String name, UtilParticle type,File file) {
		super(name, type);
		this.vectors=new ArrayList<>();
		if(file!=null)loadPicture(file);
	}

	public void loadPicture(File file){
		try {
			this.image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
		
		System.out.println("VE: "+vectors.size());
		System.out.println("VE: "+vectors.size());
		System.out.println("VE: "+vectors.size());
		System.out.println("VE: "+vectors.size());
		System.out.println("VE: "+vectors.size());
		System.out.println("VE: "+vectors.size());
	}
	
	public void display(Player player) {
		Location location = player.getLocation();
		Vector vector;
		for(Vector v : vectors){
			vector=UtilVector.rotateAroundAxisY(v.clone(), - location.getYaw() * 0.01745329F);
			getType().display(0, 8, location.add(vector), 100);
			location.subtract(vector);
		}
	}

}
