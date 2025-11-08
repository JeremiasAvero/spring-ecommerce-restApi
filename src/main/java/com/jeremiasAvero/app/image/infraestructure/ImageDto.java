package com.jeremiasAvero.app.image.infraestructure;


public class ImageDto {
	private Long id;
	private String url; 
	private String alt;
	private int position;
	private boolean primary;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public boolean isPrimary() {
		return primary;
	}
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
	
	

}
