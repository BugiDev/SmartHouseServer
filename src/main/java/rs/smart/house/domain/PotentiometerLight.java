/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.smart.house.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Bogdan
 */
@Entity
public class PotentiometerLight  extends AbstractEntity  {
    
    
	@JsonIgnore
	@ManyToOne
	Devices device;
	
	private double powerMeter;
	
	String room;

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
     * @return the powerMeter
     */
    public double getPowerMeter() {
        return powerMeter;
    }

    /**
     * @param powerMeter the powerMeter to set
     */
    public void setPowerMeter(double powerMeter) {
        this.powerMeter = powerMeter;
    }
	
    public double powerMeterUp(){
        if(!(getPowerMeter() + 0.25 > 1)){
            setPowerMeter(getPowerMeter() + 0.25 );
        }
        return getPowerMeter();
    }
    
    public double powerMeterDown(){
        if(!(getPowerMeter() - 0.25 < 0)){
            setPowerMeter(getPowerMeter() - 0.25 );
        }
        return getPowerMeter();
    }
    
}
