package rs.smart.house.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class Camera extends AbstractEntity {

	@JsonIgnore
	@ManyToOne
	Devices device;
	
	private String path;
	
	String room;
        
        private boolean alarmOnOff;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Devices getDevice() {
		return device;
	}

	public void setDevice(Devices device) {
		this.device = device;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the alarmOnOff
     */
    public boolean isAlarmOnOff() {
        return alarmOnOff;
    }

    /**
     * @param alarmOnOff the alarmOnOff to set
     */
    public void setAlarmOnOff(boolean alarmOnOff) {
        this.alarmOnOff = alarmOnOff;
    }
    
    public boolean changeAlarmOnOff(){
    
        if(isAlarmOnOff()){
            setAlarmOnOff(false);
        }else{
            setAlarmOnOff(true);
        }
        
        return isAlarmOnOff();
        
    }
	

}
