import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

public class Drawer {
	public void watermarking_cover_old(File file, String num) throws IOException{		
		/* 导员改了要求，已经不使用这里了 */
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
        Font font = new Font("黑体", Font.PLAIN, 80);
        graphics.setFont(font);
        graphics.drawString(num, 70, 630);//这里为积分数字的位置
        
        // 绘图逻辑 END
        //处理绘图
        graphics.dispose();
        //将绘制好的图片写入到图片
        ImageIO.write(bimage, "jpg", new File("out.jpg"));
	}
	
	public void watermarking_cover(File file) throws IOException {
		Image image = ImageIO.read(file);
		BufferedImage bimage = (BufferedImage)image;
		
		Graphics2D  graphics = bimage.createGraphics();
		
		//日期涂白
		graphics.setColor(new Color(255,255,255));
		Rectangle bgTOWhite = new Rectangle(820, 970, 210, 70);
		graphics.fill(bgTOWhite);
		
		//获取今天的日期
		Date date = new Date();
		SimpleDateFormat Fdate = new SimpleDateFormat("yyyy.MM.dd");
		String Edate = Fdate.format(date);
		
		//写上今天的日期
		graphics.setColor(new Color(169, 174, 180));
		Font font = new Font("黑体",Font.PLAIN,45);
		graphics.setFont(font);
		graphics.drawString(Edate, 800, 1020);
		
		graphics.dispose();
		ImageIO.write(bimage, "jpg", new File("out.jpg"));
		
	}
	
}
