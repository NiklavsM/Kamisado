package model;

import java.awt.Color;
import java.io.Serializable;

public enum MyColour implements Serializable {

	orange("orange"),
	blue("blue"),
	cyan("cyan"),
	pink("pink"),
	yellow("yellow"),
	red("red"),
	green("green"),
	brown("brown");

	private final String value;
	private Color colour;
	private GeneralSettingsManager manager = new GeneralSettingsManager();
	private GeneralSettings settings = manager.getGeneralSettings();
	
	private MyColour(String name){
		this.value = name;
		this.colour = settings.getColour(value);
		
	}
	
	public Color getColour() {
		return colour;
	}
	
	public void setColour(Color colour){
		this.colour = colour;
	}

	@Override
	public String toString() {
		return value;
	}	
}
