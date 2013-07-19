package rs.smart.house.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import rs.smart.house.domain.AbstractEntity;
import rs.smart.house.domain.Aircondition;
import rs.smart.house.domain.Boiler;
import rs.smart.house.domain.Devices;
import rs.smart.house.domain.Light;
import rs.smart.house.domain.TV;
import rs.smart.house.domain.Temperature;
import rs.smart.house.domain.User;
import rs.smart.house.domain.Windows;
import rs.smart.house.exceptions.SenergyException;
import rs.smart.house.service.GenericDaoImpl;
import rs.smart.house.util.JsonUtil;

/**
 * Handles and retrieves person request
 */
@Controller
// @RequestMapping("/router")
public class MainController {

	protected static Logger logger = Logger.getLogger("controller");
	@Resource(name = "genericDao")
	private GenericDaoImpl genericDao;

	//----------------------------------------------------------------------------------------------
	
	//----------------------------------------------------------------------------------------------
	
	//----------------------------------------------------------------------------------------------
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public void home(HttpServletResponse response) {
		Date datum = new Date();

		try {
			
			Light light = new Light();
			light.setPower(true);
			light.setRoom("Spavaca");
			light = (Light)genericDao.save(light);
			Boiler boiler = new Boiler();
			boiler.setPower(false);
			boiler.setRoom("Kupatilo");
			boiler = (Boiler)genericDao.save(boiler);
			Devices dev = new Devices();
			dev.getBoilerList().add(boiler);
			dev.getLightList().add(light);
			dev = (Devices)genericDao.save(dev);
			boiler.setDevice(dev);
			light.setDevice(dev);
			boiler = (Boiler)genericDao.save(boiler);
			light = (Light)genericDao.save(light);
			
			User userObj = new User();
			userObj.setPassword("tuljan");
			userObj.setUserName("tuljko");
			userObj.setDevice(dev);
			userObj = (User)genericDao.save(userObj);
			dev.setUser(userObj);
			dev = (Devices)genericDao.save(dev);
			response.getWriter().println(JsonUtil.convertObjectToJSON(userObj) + "\n" + JsonUtil.convertObjectToJSON(dev));
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(new Date() + " GRESKA! " + e.getMessage());
			//return "addpage";
		}

	}
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public void authenticate(@RequestParam("username") String username,@RequestParam("password") String password, HttpServletResponse response) throws IOException {

		Date datum = new Date();

		try {
			List<User> userList = ((List<User>) genericDao.loadByColumnRestriction(User.class, "username", username));

			if (userList.size() < 1)
				throw new SenergyException("User not found");
			User userObj = userList.get(0);

			if(userObj.getPassword().equals(password)){
				response.getWriter().println(userObj.getId());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(new Date() + " GRESKA! " + e.getMessage());
			response.setStatus(404);
			response.getWriter().println(new Date() + " GRESKA! -  " + e.getMessage());
		}

	}
	
	@RequestMapping(value = "/getDevices", method = RequestMethod.POST)
	public void getDevices(@RequestParam("userID") String userId, HttpServletResponse response) throws IOException {

		Date datum = new Date();

		try {
			List<User> userList = ((List<User>) genericDao.loadByColumnRestriction(User.class, "id", Long.parseLong(userId)));

			if (userList.size() < 1)
				throw new SenergyException("User not found");
			User userObj = userList.get(0);
			String poruka = JsonUtil.convertObjectToJSON(userObj.getDevice());
			response.getWriter().println(poruka);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(new Date() + " GRESKA! " + e.getMessage());
			response.setStatus(404);
			response.getWriter().println(new Date() + " GRESKA! -  " + e.getMessage());
		}

	}
	
	@RequestMapping(value = "/turnOnOff", method = RequestMethod.POST)
	public void turnOnOff(@RequestParam("deviceID") String deviceId, @RequestParam("deviceType") String deviceType, HttpServletResponse response) throws IOException {

		Date datum = new Date();

		try {
			List<AbstractEntity> deviceList = new ArrayList<AbstractEntity>();
			if(deviceType.equals("Aircondition")){
				deviceList = ((List<AbstractEntity>) genericDao.loadByColumnRestriction(Aircondition.class, "id", Long.parseLong(deviceId)));
			} else if (deviceType.equals("Boiler")){
				deviceList = ((List<AbstractEntity>) genericDao.loadByColumnRestriction(Boiler.class, "id", Long.parseLong(deviceId)));
			} else if (deviceType.equals("Light")){
				deviceList = ((List<AbstractEntity>) genericDao.loadByColumnRestriction(Light.class, "id", Long.parseLong(deviceId)));
			} else if (deviceType.equals("Windows")) {
				deviceList = ((List<AbstractEntity>) genericDao.loadByColumnRestriction(Windows.class, "id", Long.parseLong(deviceId)));
			} else if (deviceType.equals("TV")) {
				deviceList = ((List<AbstractEntity>) genericDao.loadByColumnRestriction(TV.class, "id", Long.parseLong(deviceId)));
			}
			if (deviceList.size() < 1)
				throw new SenergyException("Device not found");
			AbstractEntity deviceObj = deviceList.get(0);
			boolean poruka;
			if(deviceObj instanceof Aircondition) poruka = ((Aircondition) deviceObj).changePower();
			else if(deviceObj instanceof Boiler) poruka = ((Boiler) deviceObj).changePower();
			else if(deviceObj instanceof Light) poruka = ((Light) deviceObj).changePower();
			else if(deviceObj instanceof TV) poruka = ((TV) deviceObj).changePower();
			else poruka = ((Windows)deviceObj).changePower();
			
			deviceObj = (AbstractEntity)genericDao.saveOrUpdate(deviceObj);
			
			response.getWriter().println(poruka);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(new Date() + " GRESKA! " + e.getMessage());
			response.setStatus(404);
			response.getWriter().println(new Date() + " GRESKA! -  " + e.getMessage());
		}

	}
	
	@RequestMapping(value = "/getTemperature", method = RequestMethod.POST)
	public void getTemperature(@RequestParam("deviceID") String deviceId, HttpServletResponse response) throws IOException {

		try {
			List<Temperature> tempList = ((List<Temperature>) genericDao.loadByColumnRestriction(Temperature.class, "id", Long.parseLong(deviceId)));

			if (tempList.size() < 1)
				throw new SenergyException("User not found");
			Temperature tempObj = tempList.get(0);
			String poruka = String.valueOf(tempObj.getTemperature());
			response.getWriter().println(poruka);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(new Date() + " GRESKA! " + e.getMessage());
			response.setStatus(404);
			response.getWriter().println(new Date() + " GRESKA! -  " + e.getMessage());
		}

	}
	
	@RequestMapping(value = "/tvChannelUp", method = RequestMethod.POST)
	public void tvChannelUp(@RequestParam("deviceID") String deviceId, HttpServletResponse response) throws IOException {

		try {
			List<TV> tvList = ((List<TV>) genericDao.loadByColumnRestriction(TV.class, "id", Long.parseLong(deviceId)));

			if (tvList.size() < 1)
				throw new SenergyException("User not found");
			TV tvObj = tvList.get(0);
			String poruka = String.valueOf(tvObj.channelUp());
			response.getWriter().println(poruka);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(new Date() + " GRESKA! " + e.getMessage());
			response.setStatus(404);
			response.getWriter().println(new Date() + " GRESKA! -  " + e.getMessage());
		}

	}
	
	@RequestMapping(value = "/tvChannelDown", method = RequestMethod.POST)
	public void tvChannelDown(@RequestParam("deviceID") String deviceId, HttpServletResponse response) throws IOException {

		try {
			List<TV> tvList = ((List<TV>) genericDao.loadByColumnRestriction(TV.class, "id", Long.parseLong(deviceId)));

			if (tvList.size() < 1)
				throw new SenergyException("User not found");
			TV tvObj = tvList.get(0);
			String poruka = String.valueOf(tvObj.channelDown());
			response.getWriter().println(poruka);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(new Date() + " GRESKA! " + e.getMessage());
			response.setStatus(404);
			response.getWriter().println(new Date() + " GRESKA! -  " + e.getMessage());
		}

	}
	
	@RequestMapping(value = "/tvVolumeUp", method = RequestMethod.POST)
	public void tvVolumeUp(@RequestParam("deviceID") String deviceId, HttpServletResponse response) throws IOException {

		try {
			List<TV> tvList = ((List<TV>) genericDao.loadByColumnRestriction(TV.class, "id", Long.parseLong(deviceId)));

			if (tvList.size() < 1)
				throw new SenergyException("User not found");
			TV tvObj = tvList.get(0);
			String poruka = String.valueOf(tvObj.volumeUp());
			response.getWriter().println(poruka);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(new Date() + " GRESKA! " + e.getMessage());
			response.setStatus(404);
			response.getWriter().println(new Date() + " GRESKA! -  " + e.getMessage());
		}

	}
	
	@RequestMapping(value = "/tvVolumeDown", method = RequestMethod.POST)
	public void tvVolumeDown(@RequestParam("deviceID") String deviceId, HttpServletResponse response) throws IOException {

		try {
			List<TV> tvList = ((List<TV>) genericDao.loadByColumnRestriction(TV.class, "id", Long.parseLong(deviceId)));

			if (tvList.size() < 1)
				throw new SenergyException("User not found");
			TV tvObj = tvList.get(0);
			String poruka = String.valueOf(tvObj.volumeDown());
			response.getWriter().println(poruka);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(new Date() + " GRESKA! " + e.getMessage());
			response.setStatus(404);
			response.getWriter().println(new Date() + " GRESKA! -  " + e.getMessage());
		}

	}
	
	@RequestMapping(value = "/airTemperatureUp", method = RequestMethod.POST)
	public void airTemperatureUp(@RequestParam("deviceID") String deviceId, HttpServletResponse response) throws IOException {

		try {
			List<Aircondition> tvList = ((List<Aircondition>) genericDao.loadByColumnRestriction(Aircondition.class, "id", Long.parseLong(deviceId)));

			if (tvList.size() < 1)
				throw new SenergyException("User not found");
			Aircondition tvObj = tvList.get(0);
			String poruka = String.valueOf(tvObj.airTemperatureUp());
			response.getWriter().println(poruka);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(new Date() + " GRESKA! " + e.getMessage());
			response.setStatus(404);
			response.getWriter().println(new Date() + " GRESKA! -  " + e.getMessage());
		}

	}
	
	@RequestMapping(value = "/airTemperatureDown", method = RequestMethod.POST)
	public void airTemperatureDown(@RequestParam("deviceID") String deviceId, HttpServletResponse response) throws IOException {

		try {
			List<Aircondition> tvList = ((List<Aircondition>) genericDao.loadByColumnRestriction(Aircondition.class, "id", Long.parseLong(deviceId)));

			if (tvList.size() < 1)
				throw new SenergyException("User not found");
			Aircondition tvObj = tvList.get(0);
			String poruka = String.valueOf(tvObj.airTemperatureDown());
			response.getWriter().println(poruka);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(new Date() + " GRESKA! " + e.getMessage());
			response.setStatus(404);
			response.getWriter().println(new Date() + " GRESKA! -  " + e.getMessage());
		}

	}
	
}
