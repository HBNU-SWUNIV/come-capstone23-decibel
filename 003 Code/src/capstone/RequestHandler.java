package exserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

 
public class RequestHandler extends Thread{
    
    private Socket connection;
 
    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }
    
    public void run(){
        
        try(InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            
            BufferedReader br = new BufferedReader( new InputStreamReader(in,"UTF-8") );
            
            String firstLine      = br.readLine();
            String arrFirstLine[] = firstLine.split(" ");
            if(arrFirstLine.length==3){
            String httpMethod     = arrFirstLine[0];
            String httpUrl        = arrFirstLine[1].equals("/")?"/index.html":arrFirstLine[1];
            String httpProtocol   = arrFirstLine[2];
            String respContextType= "";
            
            Map<String,String> headerMap = RequestUtill.readHeader(br);
            
            if(!httpMethod.equals("GET")){
                String requestBody=RequestUtill.readData(br,Integer.valueOf(headerMap.get("Content-Length")));
            }
            if(headerMap.containsKey("Accept")){
            	//System.out.println(headerMap.get("Accept").split(" ")[0]+"\n");
                respContextType = headerMap.get("Accept").split(" ")[0];
            	if(respContextType.equals("text/html,application/xhtml+xml,application/xml;q=0.9,"
            			+ "image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"))
            		respContextType = "text/html;charset=utf-8";
            	if(respContextType.equals("*/*"))
            		respContextType = "mp3";
            }
            //if(headerMap.containsKey("Request URL")) {
            	//respContextType = headerMap.get("Accept").split(" ")[0];
           // }
            
            DataOutputStream dos = new DataOutputStream(out);
            
            File file = new File("./webapp"+httpUrl);
            byte[] body;
            if(file.exists()){
                body = Files.readAllBytes(file.toPath());
            }else{
                body = Files.readAllBytes(new File("./webapp/404page.html").toPath());
            }            
            
            ResponseUtill.response200Header(respContextType,dos, body.length);
            ResponseUtill.responseBody(dos, body);
            }else{
                throw new Exception("INVALID FORMAT");
            }
            
        } catch (IOException e) {
        	
        } catch (Exception e){

        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block

            }
        }
    }
    
    
    
}