package rs.smart.house.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class TV extends AbstractEntity {

	
	@JsonIgnore
	@ManyToOne
	Devices device;
	
	boolean power;
	
	String room;

	int chanell = 1;
	
	int volume = 0;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getChanell() {
		return chanell;
	}

	public void setChanell(int chanell) {
		this.chanell = chanell;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public Devices getDevice() {
		return device;
	}

	public void setDevice(Devices device) {
		this.device = device;
	}

	public boolean isPower() {
		return power;
	}

	public void setPower(boolean power) {
		this.power = power;
	}

	public boolean changePower(){
		power = !power;
		return power;
	}
	
	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}
	
	public int channelUp() {
		return chanell++;
	}
	
	public int channelDown() {
		return chanell--;
	}
	
	public int volumeUp() {
		return volume++;
	}
	
	public int volumeDown() {
		return volume--;
	}
}
