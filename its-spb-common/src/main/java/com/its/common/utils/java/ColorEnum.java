package com.its.common.utils.java;

public enum ColorEnum {

	RED(255,0,0),BLUE(0,0,255),BLACK(0,0,0),YELLOW(255,255,0),GREEN(0,255,0); 
	
	private int redValue;
	private int greenValue;
	private int blueValue;
	ColorEnum(int red, int green, int blue){
		this.redValue = red;
		this.greenValue = green;
		this.blueValue = blue;
	}
	public int getRedValue() {
		return redValue;
	}
	public void setRedValue(int redValue) {
		this.redValue = redValue;
	}
	public int getGreenValue() {
		return greenValue;
	}
	public void setGreenValue(int greenValue) {
		this.greenValue = greenValue;
	}
	public int getBlueValue() {
		return blueValue;
	}
	public void setBlueValue(int blueValue) {
		this.blueValue = blueValue;
	}
	
	public static void main(String[] args) {
		ColorEnum colorEnum = ColorEnum.RED;
		System.out.println(colorEnum.getRedValue()+"--"+colorEnum.getGreenValue()+"--"+colorEnum.getBlueValue());
	}
	
}
