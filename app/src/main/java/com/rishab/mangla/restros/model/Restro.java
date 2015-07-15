package com.rishab.mangla.restros.model;

import java.util.ArrayList;

/**
 * Created by rishabmangla on 15/7/15.
 */
public class Restro {
	private String name, thumbnailUrl, location;
	private int year;
	private double distance;
	private double latitude;
	private double longitude;
	private ArrayList<String> cuisine;

	public Restro() {
	}

	public Restro(String name, String thumbnailUrl, int year, double distance,
                  ArrayList<String> cuisine) {
		this.name = name;
		this.thumbnailUrl = thumbnailUrl;
		this.year = year;
		this.distance = distance;
		this.cuisine = cuisine;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

    public String getLocation() {
        return location;
    }

	public void setLocation(String location) {
		this.location = location;
	}

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public ArrayList<String> getCuisine() {
		return cuisine;
	}

	public void setCuisine(ArrayList<String> cuisine) {
		this.cuisine = cuisine;
	}

}
