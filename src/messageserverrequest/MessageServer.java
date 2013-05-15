package messageserverrequest;

public class MessageServer {

	private String created_at;
	private String from_address;
	private int id;
	private String message;
	private String to_address;
	private String updated_at;
	
	public MessageServer(String created_at, String from_address, int id, String message, String to_address, String updated_at) {
		this.created_at = created_at;
		this.from_address = from_address;
		this.id = id;
		this.message = message;
		this.to_address = to_address;
		this.updated_at = updated_at;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getFrom_address() {
		return from_address;
	}

	public void setFrom_address(String from_address) {
		this.from_address = from_address;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTo_address() {
		return to_address;
	}

	public void setTo_address(String to_address) {
		this.to_address = to_address;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
}
