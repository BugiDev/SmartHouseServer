package rs.smart.house.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;


/**
 * An item in an order
 */
@Entity
public class User extends AbstractEntity {

	@OneToOne
	Devices device;

	@Column(name = "username")
	private String username;

	private String password;

	private String currentIP;
	
	public Devices getDevice() {
		return device;
	}


	public void setDevice(Devices device) {
		this.device = device;
	}


	public String getCurrentIP() {
		return currentIP;
	}


	public void setCurrentIP(String currentIP) {
		this.currentIP = currentIP;
	}

	public String getUserName() {
		return username;
	}


	public void setUserName(String userName) {
		this.username = userName;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	
	public Long getId() {
		return id;
	}

}
