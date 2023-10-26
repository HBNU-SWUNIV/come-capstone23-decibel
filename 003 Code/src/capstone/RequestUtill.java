package exserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestUtill {
	
	public static String readData(BufferedReader br, int contentLength) throws IOException {
		char[] body = new char[contentLength];
		br.read(body, 0, contentLength);
		return String.copyValueOf(body);
	}
	
	public static Map<String, String> readHeader(BufferedReader br) throws IOException{
		Map<String, String> map = new HashMap<String, String>();
		String line;
		while((line = br.readLine())!= null) {
			if(!line.equals("")) {
				String key = line.split(":")[0];
				String value = line.split(":")[1].trim();
				if (value.equals("localhost"))
					value = "localhost:1234";
				if (value.equals("\"Google Chrome\";v=\"111\", \"Not(A"))
					value = "\"Google Chrome\";v=\"111\", \"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"111\"";
				map.put(key, value);
			}else {break;}
		}
		return map;
	}
}
