package rs.smart.house.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class Windows extends AbstractEntity {

    @JsonIgnore
    @ManyToOne
    Devices device;
    private String direction;
    String room;

    @Override
    public Long getId() {
        return id;
    }

    @Override
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
     * @return the direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String changeDirection() {
        if (getDirection().equalsIgnoreCase("up")) {
            setDirection("down");
            return getDirection();
        }else{
            setDirection("up");
            return getDirection();
        }
    }
}
