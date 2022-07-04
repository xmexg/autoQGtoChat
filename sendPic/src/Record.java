import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class Record {
	File file = new File("record");
	
	{
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("无法初始化record文件");
				e.printStackTrace();
			}
		}
	}
	
	public int readrecord() throws IOException {	
		Reader in = new FileReader(this.file);
		String num = "";
		int number = 0;
		int text;
		while((text = in.read()) != -1) {
			num += (char)text;
		}
		in.close();
		try {
			number = Integer.valueOf(num);
		}catch (NumberFormatException e) {
			number = 0;
		}
		return number;
	}
	
	public void writerecord(int num) throws IOException {
		Writer out = new FileWriter(file);
		out.write(""+num);
		out.close();
	}
	
	public String nextrecord() throws IOException {
		int num = readrecord() + 1;
		writerecord(num);
		return num+"";
	}
}
