package rs.smart.house.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class Devices extends AbstractEntity {

	@JsonIgnore
	@OneToOne
	User user;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "device")
	private List<Light> lightList = new ArrayList<Light>();
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "device")
	private List<Aircondition> airconditionList = new ArrayList<Aircondition>();
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "device")
	private List<Boiler> boilerList = new ArrayList<Boiler>();
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "device")
	private List<Camera> cameraList = new ArrayList<Camera>();
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "device")
	private List<Temperature> temperatureList = new ArrayList<Temperature>();
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "device")
	private List<TV> tvList = new ArrayList<TV>();
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "device")
	private List<Windows> windowsList = new ArrayList<Windows>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Light> getLightList() {
		return lightList;
	}

	public void setLightList(List<Light> lightList) {
		this.lightList = lightList;
	}

	public List<Aircondition> getAirconditionList() {
		return airconditionList;
	}

	public void setAirconditionList(List<Aircondition> airconditionList) {
		this.airconditionList = airconditionList;
	}

	public List<Boiler> getBoilerList() {
		return boilerList;
	}

	public void setBoilerList(List<Boiler> boilerList) {
		this.boilerList = boilerList;
	}

	public List<Camera> getCameraList() {
		return cameraList;
	}

	public void setCameraList(List<Camera> cameraList) {
		this.cameraList = cameraList;
	}

	public List<Temperature> getTemperatureList() {
		return temperatureList;
	}

	public void setTemperatureList(List<Temperature> temperatureList) {
		this.temperatureList = temperatureList;
	}

	public List<TV> getTvList() {
		return tvList;
	}

	public void setTvList(List<TV> tvList) {
		this.tvList = tvList;
	}

	public List<Windows> getWindowsList() {
		return windowsList;
	}

	public void setWindowsList(List<Windows> windowsList) {
		this.windowsList = windowsList;
	}

	

}
