import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Main {
	public static String wxid = ""; //要把图片发送给谁
	
	public static void main(String[] args) throws IOException {
		
		if(args.length != 0) {
			wxid = args[0];
		}else {
			System.out.println("没有填写发送地址");
			System.exit(-1);
		}
		
		File img_file = new File("in.jpg");	
		if(!img_file.exists()) {
			System.out.println("in.jpg图片不存在");
			System.exit(-2);
		}
		
		Drawer drawer = new Drawer();
		drawer.watermarking_cover(img_file, new Record().nextrecord());
			
		String path = new File("out.jpg").getAbsolutePath();
		//如果是windows系统，则转换为windows格式的路径
		if(System.getProperty("os.name").toLowerCase().contains("windows")) {
			path = path.replace("\\", "/");
		}
		
		Post post = new Post();
		post.sendImage("http://127.0.0.1:8081/sendImgMsg",wxid, path);

	}

}
