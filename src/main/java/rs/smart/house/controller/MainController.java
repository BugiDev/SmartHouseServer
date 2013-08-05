package rs.smart.house.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import rs.smart.house.domain.AbstractEntity;
import rs.smart.house.domain.Aircondition;
import rs.smart.house.domain.Boiler;
import rs.smart.house.domain.Camera;
import rs.smart.house.domain.Devices;
import rs.smart.house.domain.Light;
import rs.smart.house.domain.PotentiometerLight;
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
            
            Devices dev = new Devices();
            dev = (Devices)genericDao.save(dev);

            Light light1 = new Light();
            light1.setPower(true);
            light1.setRoom("Spavaca");
            light1 = (Light) genericDao.save(light1);
            light1.setDevice(dev);
            light1 = (Light) genericDao.save(light1);
            dev.getLightList().add(light1);
            
            Light light2 = new Light();
            light2.setPower(true);
            light2.setRoom("Dnevna");
            light2.setDevice(dev);
            light2 = (Light) genericDao.save(light2);
            dev.getLightList().add(light2);
            
            Light light3 = new Light();
            light3.setPower(true);
            light3.setRoom("Kupatilo");
            light3.setDevice(dev);
            light3 = (Light) genericDao.save(light3);
            dev.getLightList().add(light3);
            
            Light light4 = new Light();
            light4.setPower(true);
            light4.setRoom("Terasa");
            light4.setDevice(dev);
            light4 = (Light) genericDao.save(light4);
            dev.getLightList().add(light4);
            
            PotentiometerLight potLight = new PotentiometerLight();
            potLight.setPowerMeter(0);
            potLight.setRoom("Terasa");
            potLight.setDevice(dev);
            potLight = (PotentiometerLight) genericDao.save(potLight);
            dev.getPotLightList().add(potLight);
            
            Boiler boiler = new Boiler();
            boiler.setPower(false);
            boiler.setRoom("Kupatilo");
            boiler.setDevice(dev);
            boiler = (Boiler) genericDao.save(boiler);
            dev.getBoilerList().add(boiler);
            
            TV tv = new TV();
            tv.setPower(false);
            tv.setRoom("Dnevna");
            tv.setChannel(4);
            tv.setVolume(15);
            tv.setDevice(dev);
            tv = (TV) genericDao.save(tv);
            dev.getTvList().add(tv);
            
            Aircondition air = new Aircondition();
            air.setPower(false);
            air.setRoom("Dnevna");
            air.setMode("cool");
            air.setTemperature(20);
            air.setDevice(dev);
            air = (Aircondition) genericDao.save(air);
            dev.getAirconditionList().add(air);
            
            Windows window = new Windows();
            window.setRoom("Dnevna");
            window.setDirection("up");
            window.setDevice(dev);
            window = (Windows) genericDao.save(window);
            dev.getWindowsList().add(window);
            
            Camera cam = new Camera();
            cam.setRoom("Dnevna");
            cam.setAlarmOnOff(false);
            URL url = new URL("http://213.251.201.196/anony/mjpg.cgi");
            cam.setPath(url.toString());
            cam.setDevice(dev);
            cam = (Camera) genericDao.save(cam);
            dev.getCameraList().add(cam);
            
            Temperature temperature = new Temperature();
            temperature.setRoom("Dnevna");
            temperature.setTemperature(24);
            temperature.setDevice(dev);
            temperature = (Temperature) genericDao.save(temperature);
            dev.getTemperatureList().add(temperature);

            dev = (Devices) genericDao.save(dev);
            
            User userObj = new User();
            userObj.setPassword("admin");
            userObj.setUserName("admin");
            userObj.setDevice(dev);
            userObj = (User) genericDao.save(userObj);
            dev.setUser(userObj);
            dev = (Devices) genericDao.save(dev);
            response.getWriter().println(JsonUtil.convertObjectToJSON(userObj) + "\n" + JsonUtil.convertObjectToJSON(dev));


        } catch (Exception e) {
            e.printStackTrace();
            logger.info(new Date() + " GRESKA! " + e.getMessage());
            //return "addpage";
        }

    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public void authenticate(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletResponse response) throws IOException {

        Date datum = new Date();

        try {
            List<User> userList = ((List<User>) genericDao.loadByColumnRestriction(User.class, "username", username));

            if (userList.size() < 1) {
                throw new SenergyException("User not found");
            }
            User userObj = userList.get(0);

            if (userObj.getPassword().equals(password)) {
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

            if (userList.size() < 1) {
                throw new SenergyException("User not found");
            }
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
            if (deviceType.equals("Aircondition")) {
                deviceList = ((List<AbstractEntity>) genericDao.loadByColumnRestriction(Aircondition.class, "id", Long.parseLong(deviceId)));
            } else if (deviceType.equals("Boiler")) {
                deviceList = ((List<AbstractEntity>) genericDao.loadByColumnRestriction(Boiler.class, "id", Long.parseLong(deviceId)));
            } else if (deviceType.equals("Light")) {
                deviceList = ((List<AbstractEntity>) genericDao.loadByColumnRestriction(Light.class, "id", Long.parseLong(deviceId)));
            } else if (deviceType.equals("Windows")) {
                deviceList = ((List<AbstractEntity>) genericDao.loadByColumnRestriction(Windows.class, "id", Long.parseLong(deviceId)));
            } else if (deviceType.equals("TV")) {
                deviceList = ((List<AbstractEntity>) genericDao.loadByColumnRestriction(TV.class, "id", Long.parseLong(deviceId)));
            }
            if (deviceList.size() < 1) {
                throw new SenergyException("Device not found");
            }
            AbstractEntity deviceObj = deviceList.get(0);
            boolean poruka;
            if (deviceObj instanceof Aircondition) {
                poruka = ((Aircondition) deviceObj).changePower();
            } else if (deviceObj instanceof Boiler) {
                poruka = ((Boiler) deviceObj).changePower();
            } else if (deviceObj instanceof Light) {
                poruka = ((Light) deviceObj).changePower();
            } else {
                poruka = ((TV) deviceObj).changePower();
            }

            deviceObj = (AbstractEntity) genericDao.saveOrUpdate(deviceObj);

            response.getWriter().println(poruka);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(new Date() + " GRESKA! " + e.getMessage());
            response.setStatus(404);
            response.getWriter().println(new Date() + " GRESKA! -  " + e.getMessage());
        }

    }

    @RequestMapping(value = "/windowUpDown", method = RequestMethod.POST)
    public void windowUpDown(@RequestParam("deviceID") String deviceId, HttpServletResponse response) throws IOException {

        try {
            List<AbstractEntity> deviceList = new ArrayList<AbstractEntity>();

            deviceList = ((List<AbstractEntity>) genericDao.loadByColumnRestriction(Windows.class, "id", Long.parseLong(deviceId)));

            if (deviceList.size() < 1) {
                throw new SenergyException("Device not found");
            }
            AbstractEntity deviceObj = deviceList.get(0);
            String poruka;
           
            poruka = ((Windows) deviceObj).changeDirection();

            deviceObj = (AbstractEntity) genericDao.saveOrUpdate(deviceObj);

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

            if (tempList.size() < 1) {
                throw new SenergyException("User not found");
            }
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

            if (tvList.size() < 1) {
                throw new SenergyException("User not found");
            }
            TV tvObj = tvList.get(0);
            String poruka = String.valueOf(tvObj.channelUp());
            genericDao.saveOrUpdate(tvObj);
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

            if (tvList.size() < 1) {
                throw new SenergyException("User not found");
            }
            TV tvObj = tvList.get(0);
            String poruka = String.valueOf(tvObj.channelDown());
            genericDao.saveOrUpdate(tvObj);
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

            if (tvList.size() < 1) {
                throw new SenergyException("User not found");
            }
            TV tvObj = tvList.get(0);
            String poruka = String.valueOf(tvObj.volumeUp());
            genericDao.saveOrUpdate(tvObj);
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

            if (tvList.size() < 1) {
                throw new SenergyException("User not found");
            }
            TV tvObj = tvList.get(0);
            String poruka = String.valueOf(tvObj.volumeDown());
            genericDao.saveOrUpdate(tvObj);
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

            if (tvList.size() < 1) {
                throw new SenergyException("User not found");
            }
            Aircondition tvObj = tvList.get(0);
            String poruka = String.valueOf(tvObj.airTemperatureUp());
            genericDao.saveOrUpdate(tvObj);
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

            if (tvList.size() < 1) {
                throw new SenergyException("User not found");
            }
            Aircondition tvObj = tvList.get(0);
            String poruka = String.valueOf(tvObj.airTemperatureDown());
            genericDao.saveOrUpdate(tvObj);
            response.getWriter().println(poruka);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(new Date() + " GRESKA! " + e.getMessage());
            response.setStatus(404);
            response.getWriter().println(new Date() + " GRESKA! -  " + e.getMessage());
        }

    }
    
    @RequestMapping(value = "/getMode", method = RequestMethod.POST)
    public void getMode(@RequestParam("deviceID") String deviceId, HttpServletResponse response) throws IOException {

        try {
            List<Aircondition> tvList = ((List<Aircondition>) genericDao.loadByColumnRestriction(Aircondition.class, "id", Long.parseLong(deviceId)));

            if (tvList.size() < 1) {
                throw new SenergyException("User not found");
            }
            Aircondition tvObj = tvList.get(0);
            String poruka = String.valueOf(tvObj.getMode());
            response.getWriter().println(poruka);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(new Date() + " GRESKA! " + e.getMessage());
            response.setStatus(404);
            response.getWriter().println(new Date() + " GRESKA! -  " + e.getMessage());
        }

    }
    
    @RequestMapping(value = "/getAirTemp", method = RequestMethod.POST)
    public void getAirTemp(@RequestParam("deviceID") String deviceId, HttpServletResponse response) throws IOException {

        try {
            List<Aircondition> tvList = ((List<Aircondition>) genericDao.loadByColumnRestriction(Aircondition.class, "id", Long.parseLong(deviceId)));

            if (tvList.size() < 1) {
                throw new SenergyException("User not found");
            }
            Aircondition tvObj = tvList.get(0);
            String poruka = String.valueOf(tvObj.getTemperature());
            response.getWriter().println(poruka);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(new Date() + " GRESKA! " + e.getMessage());
            response.setStatus(404);
            response.getWriter().println(new Date() + " GRESKA! -  " + e.getMessage());
        }

    }
    
    @RequestMapping(value = "/getChannel", method = RequestMethod.POST)
    public void getChannel(@RequestParam("deviceID") String deviceId, HttpServletResponse response) throws IOException {

        try {
            List<TV> tvList = ((List<TV>) genericDao.loadByColumnRestriction(TV.class, "id", Long.parseLong(deviceId)));

            if (tvList.size() < 1) {
                throw new SenergyException("User not found");
            }
            TV tvObj = tvList.get(0);
            String poruka = String.valueOf(tvObj.getChannel());
            response.getWriter().println(poruka);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(new Date() + " GRESKA! " + e.getMessage());
            response.setStatus(404);
            response.getWriter().println(new Date() + " GRESKA! -  " + e.getMessage());
        }

    }
    
    @RequestMapping(value = "/getVolume", method = RequestMethod.POST)
    public void getVolume(@RequestParam("deviceID") String deviceId, HttpServletResponse response) throws IOException {

        try {
            List<TV> tvList = ((List<TV>) genericDao.loadByColumnRestriction(TV.class, "id", Long.parseLong(deviceId)));

            if (tvList.size() < 1) {
                throw new SenergyException("User not found");
            }
            TV tvObj = tvList.get(0);
            String poruka = String.valueOf(tvObj.getVolume());
            response.getWriter().println(poruka);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(new Date() + " GRESKA! " + e.getMessage());
            response.setStatus(404);
            response.getWriter().println(new Date() + " GRESKA! -  " + e.getMessage());
        }

    }
    
    @RequestMapping(value = "/cameraAlarmOnOff", method = RequestMethod.POST)
    public void cameraAlarmOnOff(@RequestParam("deviceID") String deviceId, HttpServletResponse response) throws IOException {

        try {
            List<Camera> cameraList = ((List<Camera>) genericDao.loadByColumnRestriction(Camera.class, "id", Long.parseLong(deviceId)));

            if (cameraList.size() < 1) {
                throw new SenergyException("User not found");
            }
            Camera cameraObj = cameraList.get(0);
            String poruka = String.valueOf(cameraObj.changeAlarmOnOff());
            genericDao.saveOrUpdate(cameraObj);
            response.getWriter().println(poruka);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(new Date() + " GRESKA! " + e.getMessage());
            response.setStatus(404);
            response.getWriter().println(new Date() + " GRESKA! -  " + e.getMessage());
        }

    }
    
    @RequestMapping(value = "/potLightUp", method = RequestMethod.POST)
    public void potLightUp(@RequestParam("deviceID") String deviceId, HttpServletResponse response) throws IOException {

        try {
            List<PotentiometerLight> potLightList = ((List<PotentiometerLight>) genericDao.loadByColumnRestriction(PotentiometerLight.class, "id", Long.parseLong(deviceId)));

            if (potLightList.size() < 1) {
                throw new SenergyException("User not found");
            }
            PotentiometerLight  potLightObj = potLightList.get(0);
            String poruka = String.valueOf(potLightObj.powerMeterUp());
            genericDao.saveOrUpdate(potLightObj);
            response.getWriter().println(poruka);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(new Date() + " GRESKA! " + e.getMessage());
            response.setStatus(404);
            response.getWriter().println(new Date() + " GRESKA! -  " + e.getMessage());
        }

    }

    @RequestMapping(value = "/potLightDown", method = RequestMethod.POST)
    public void potLightDown(@RequestParam("deviceID") String deviceId, HttpServletResponse response) throws IOException {

        try {
            List<PotentiometerLight> potLightList = ((List<PotentiometerLight>) genericDao.loadByColumnRestriction(PotentiometerLight.class, "id", Long.parseLong(deviceId)));

            if (potLightList.size() < 1) {
                throw new SenergyException("User not found");
            }
            PotentiometerLight  potLightObj = potLightList.get(0);
            String poruka = String.valueOf(potLightObj.powerMeterDown());
            genericDao.saveOrUpdate(potLightObj);
            response.getWriter().println(poruka);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(new Date() + " GRESKA! " + e.getMessage());
            response.setStatus(404);
            response.getWriter().println(new Date() + " GRESKA! -  " + e.getMessage());
        }

    }
}
