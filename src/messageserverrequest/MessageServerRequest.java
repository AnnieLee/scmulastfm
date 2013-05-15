package messageserverrequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.stream.JsonReader;

public class MessageServerRequest {

	private String server = "http://scmu-pendent-messages.herokuapp.com/";

	public MessageServerRequest() {

	}

	public boolean sendMessage(String from_address, String to_address, String message) {
		boolean result = false;
		InputStream is = null;
		try {

			String urlParameters = from_address + "/" + to_address + "/" + message;
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

	public List<MessageServer> getMessages(String from_address, String to_address) {
		List<MessageServer> m = new ArrayList<MessageServer>();
		InputStream is = null;
		try {

			String urlParameters = from_address + "/" + to_address;
			String url_str = server + "messages/" + urlParameters + ".json";
			URL url = new URL(url_str); 

			URLConnection conn = url.openConnection();

			is = conn.getInputStream();

			byte[] buffer = new byte[1024];
			is.read(buffer);

			String json = new String(buffer);
//			Gson gson = new Gson();
			
			m = readJsonStream(is);
			
//			Type collectionType = new TypeToken<Collection<MessageServer>>(){}.getType();
//			m = gson.fromJson(json, collectionType);

		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return m; 

	}

	public List<MessageServer> readJsonStream(InputStream in) throws IOException {
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		try {
			return readMessagesArray(reader);
		} finally {
			reader.close();
		}
	}

	public List<MessageServer> readMessagesArray(JsonReader reader) throws IOException {
		List<MessageServer> messages = new ArrayList<MessageServer>();

		reader.beginArray();
		while (reader.hasNext()) {
			messages.add(readMessage(reader));
		}
		reader.endArray();
		return messages;
	}

	public MessageServer readMessage(JsonReader reader) throws IOException {
		int id = -1;
		String created_at = "";
		String updated_at = "";
		String message = "";
		String from_address = "";
		String to_address = "";

		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("id")) {
				id = reader.nextInt();
			} else if (name.equals("created_at")) {
				created_at = reader.nextString();
			} else if (name.equals("from_address")) {
				from_address = reader.nextString();
			} else if (name.equals("message")) {
				message = reader.nextString();
			}
			else if (name.equals("to_address")) {
				to_address = reader.nextString();
			}
			else if (name.equals("updated_at")) {
				updated_at = reader.nextString();
			}
			else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return new MessageServer(created_at, from_address, id, message, to_address, updated_at);
	}
}
