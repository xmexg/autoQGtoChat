import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Post {
	public String sendText(String Url, String wxid, String content) {
		//返回的结果
		String line;
		//创建json数据
		String json = "{\"wxid\":\"" + wxid + "\",\"content\":\"" + content + "\"}";
		line = sendpost(Url, json);
		return line;
	}

	public String sendImage(String Url, String wxid, String filepath){
		//返回的结果
		String line;
		//创建json数据
		String json = "{\"wxid\":\"" + wxid + "\",\"content\":\"" + filepath + "\"}";
		line = sendpost(Url, json);
		return line;
	}

	private String sendpost(String Url, String json){
		//结果
		StringBuffer result = new StringBuffer();
		//创建url对象
		try {
			URL url = new URL(Url);
			//打开连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			//设置连接属性
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json;");
			conn.setRequestProperty("Content-Length", json.length() + "");
			conn.setDoOutput(true);
			//获取输出流
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			//发送数据
			out.writeBytes(json);
			out.flush();
			out.close();
			//获取输入流
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			
//			sendText("http://127.0.0.1:8081/sendTextMsg", "wxid_3g81utfix76322", e.toString());
			/**
			System.out.println(e.toString());
			
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String sStackTrace = sw.toString(); // stack trace as a string
			sStackTrace = sStackTrace.replace("\n", "  ");
			sStackTrace = sStackTrace.replace("\r", "  ");
			sStackTrace = sStackTrace.replace("\t", "");
			sStackTrace = sStackTrace.replace("at", "@at");
			System.out.println(sStackTrace);
			sendText("http://127.0.0.1:8081/sendTextMsg", "21780526608@chatroom", sStackTrace);
			*/
		}
		return result.toString();
	}
}
