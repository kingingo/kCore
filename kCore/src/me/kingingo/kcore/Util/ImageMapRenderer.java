package me.kingingo.kcore.Util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

class ImageMapRenderer extends MapRenderer {
    Image image;

    public ImageMapRenderer(Image image){
        this.image = image;
    }

//    public void render(MapView view, MapCanvas canvas, Player p) {
//          canvas.drawImage(128, 128, image);
//    }
    
    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
        BufferedImage img = new BufferedImage(128,128,BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        g.drawImage(image, 0, 0, 128, 128, 0, 0, image.getWidth(null), image.getHeight(null), null);
        g.dispose();

        canvas.drawImage(0, 0, img);
    }

}