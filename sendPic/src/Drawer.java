import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Drawer {
	public void watermarking_cover(File file, String num) throws IOException{		
		//File 转 BufferedImage
		Image image = ImageIO.read(file);
		BufferedImage bimage = (BufferedImage)image;
		
        //基于图片对象打开绘图
        Graphics2D graphics = bimage.createGraphics();
        //绘图逻辑 START （基于业务逻辑进行绘图处理）……

        //绘制圆形
        graphics.setColor(Color.white);
        Rectangle rec = new Rectangle(70, 570, 180, 70); //这里正好选中积分数字
        graphics.fill(rec);
        
        //写上积分
        graphics.setColor(new Color(228, 35, 20));
        Font font = new Font("黑体", Font.PLAIN, 90);
        graphics.setFont(font);
        graphics.drawString(num, 70, 630);//这里为积分数字的位置
        
        // 绘图逻辑 END
        //处理绘图
        graphics.dispose();
        //将绘制好的图片写入到图片
        ImageIO.write(bimage, "jpg", new File("out.jpg"));
	}
}
