package com.weatherapp.model;

import java.util.List;

/**
 * Created by dharmaraj on 5/8/15.
 */
public class Weather extends SubWeather {

    private String country;
    private String sunrise;
    private String sunset;
    private List<SubWeather> subWeatherList;
    private String base;
    private String temp;
    private String tempMin;
    private String tempMax;
    private String pressure;
    private String seaLevel;
    private String grndLevel;
    private String humidity;
    private String windSpeed;
    private String windDeg;
    private String cloudsAll;
    private String rain3H;
    private String cityName;
    private String dt;
    private String id;
    private String snow3H;

    public String getSnow3H() {
        return snow3H;
    }

    public void setSnow3H(String snow3H) {
        this.snow3H = snow3H;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public List<SubWeather> getSubWeatherList() {
        return subWeatherList;
    }

    public void setSubWeatherList(List<SubWeather> subWeatherList) {
        this.subWeatherList = subWeatherList;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(String seaLevel) {
        this.seaLevel = seaLevel;
    }

    public String getGrndLevel() {
        return grndLevel;
    }

    public void setGrndLevel(String grndLevel) {
        this.grndLevel = grndLevel;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDeg() {
        return windDeg;
    }

    public void setWindDeg(String windDeg) {
        this.windDeg = windDeg;
    }

    public String getCloudsAll() {
        return cloudsAll;
    }

    public void setCloudsAll(String cloudsAll) {
        this.cloudsAll = cloudsAll;
    }

    public String getRain3H() {
        return rain3H;
    }

    public void setRain3H(String rain3H) {
        this.rain3H = rain3H;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}






