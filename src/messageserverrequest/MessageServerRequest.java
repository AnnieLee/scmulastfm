package messageserverrequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MessageServerRequest {

	private String server = "http://scmu-pendent-messages.herokuapp.com/";

	public MessageServerRequest() {

	}

	public boolean sendMessage(String from_address, String to_address, String message) {
		boolean result = false;
		InputStream is = null;
		try {
			String message_to_send = message.replace(" ", "%20");
			String urlParameters = from_address + "/" + to_address + "/" + message_to_send;
			String url_str = server + "messages/send_to/" + urlParameters + ".json";
			URL url = new URL(url_str); 

			URLConnection conn = url.openConnection();

			is = conn.getInputStream();

			byte[] buffer = new byte[256];
			is.read(buffer);

			String response_str = new String(buffer);
			result = Boolean.valueOf(response_str);

		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return result;
	}

	public List<String> getMessages(String from_address, String to_address) {
		List<String> m = new ArrayList<String>();
		InputStream is = null;
		try {

			String urlParameters = from_address + "/" + to_address;
			String url_str = server + "messages/" + urlParameters + ".json";
			URL url = new URL(url_str); 

			URLConnection conn = url.openConnection();

			is = conn.getInputStream();

			byte[] buffer = new byte[1024];
			is.read(buffer);

			//JSON parsing
			String json = new String(buffer);
			String json_without_brackets = json.replace("[", "").replace("]", "");
			String json_without_braces = json_without_brackets.replace('{', ' ').replace('}', ' ');
			String json_without_marks = json_without_braces.replace('"', ' ');
			String[] json_messages = json_without_marks.split(",");

			for (int i = 0; i < json_messages.length; i++) {
				String[] splitted_message = json_messages[i].split(":");
				String message = splitted_message[1].trim();
				m.add(message);
			}

		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return m; 

	}

	public Integer getCountMessages(String from_address, String to_address) {
		Integer result = 0;
		InputStream is = null;
		try {

			String urlParameters = from_address + "/" + to_address;
			String url_str = server + "messages/count/" + urlParameters + ".json";
			URL url = new URL(url_str); 

			URLConnection conn = url.openConnection();

			is = conn.getInputStream();

			byte[] buffer = new byte[1024];
			is.read(buffer);

			//JSON parsing
			String json = new String(buffer);
			String json_without_marks = json.replace('"', ' ');
			result = Integer.parseInt(json_without_marks.trim());

		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return result; 
	}
}
