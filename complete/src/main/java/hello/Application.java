package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.io.BufferedReader; 
import java.io.IOException; 
import java.io.InputStreamReader; 
import java.net.HttpURLConnection;
import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import org.json.*;
@SpringBootApplication
@CrossOrigin
@RestController
public class Application {
	@RequestMapping("/")
	public HashMap<String, String> home() {
                HashMap<String, String> map = new HashMap<>();
                map.put("categorry","car");
                
		  return map;
	 }
	
	@GetMapping("/hello")
	public String hello() {
                JSONObject jsonObject = new JSONObject();
                HashMap<String, String> map = new HashMap<>();
                map.put("categorry","car");
                try{
                URL url = new URL("http://nginx-flask/hello/");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader( con.getInputStream()));
                jsonObject = new JSONObject(in.readLine());
                //System.out.println(jsonObject.toString());
                in.close();
                }catch(MalformedURLException ex){
                	ex.printStackTrace();

                }catch(IOException ex){
                	ex.printStackTrace();
                }catch(JSONException ex){
                	ex.printStackTrace();
                }
                
		  return jsonObject.toString();
	 }
	
	@PostMapping("/transport/predict")
	public String predictTransport(@RequestParam("file") MultipartFile file) {
		//System.out.println(file.getSize());
		String attachmentFileName = "Image.jpg";
		String crlf = "\r\n";
		String twoHyphens = "--";
		String boundary =  "*****";
		
		
		//Setup the request:
		HttpURLConnection httpUrlConnection = null;
		URL url = null;
		try {
			url = new URL("http://nginx-flask/predict/");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("1");
			e.printStackTrace();
		}
		try {
			httpUrlConnection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("2");
			e.printStackTrace();
		}
		httpUrlConnection.setUseCaches(false);
		httpUrlConnection.setDoOutput(true);

		try {
			httpUrlConnection.setRequestMethod("POST");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			System.out.println("3");
			e.printStackTrace();
		}
		httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
		httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
		httpUrlConnection.setRequestProperty(
		    "Content-Type", "multipart/form-data;boundary=" + boundary);
		
		////////////////////////////
		
		
		//Start content wrapper:
		DataOutputStream request = null;
		try {
			request = new DataOutputStream(
				    httpUrlConnection.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("4");
			e.printStackTrace();
		}

			try {
				request.writeBytes(twoHyphens + boundary + crlf);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("5");
				e.printStackTrace();
			}
			try {
				request.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + 
				    attachmentFileName + "\"" + crlf);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("6");
				e.printStackTrace();
			}
			try {
				request.writeBytes(crlf);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("7");
				e.printStackTrace();
			}
			
			/////////////////////////////
			
			
			try {
				request.write(file.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("8");
				e.printStackTrace();
			}
		
			
			//End content wrapper:
			try {
				request.writeBytes(crlf);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("9");
				e.printStackTrace();
			}
			try {
				request.writeBytes(twoHyphens + boundary + 
				    twoHyphens + crlf);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("10");
				e.printStackTrace();
			}
			
			/////////////////////////
			
			//Flush output buffer:
			try {
				request.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("11");
				e.printStackTrace();
			}
			try {
				request.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("12");
				e.printStackTrace();
			}
			
			InputStream responseStream = null;
			try {
				responseStream = new 
					    BufferedInputStream(httpUrlConnection.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("13");
				e.printStackTrace();
			}

				BufferedReader responseStreamReader = 
				    new BufferedReader(new InputStreamReader(responseStream));

				String line = "";
				StringBuilder stringBuilder = new StringBuilder();

				try {
					while ((line = responseStreamReader.readLine()) != null) {
					    stringBuilder.append(line).append("\n");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("14");
					e.printStackTrace();
				}
				try {
					responseStreamReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("15");
					e.printStackTrace();
				}

				String response = stringBuilder.toString();
				
				try {
					responseStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("16");
					e.printStackTrace();
				}
			
		
		return response;
	}

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
	}

}
