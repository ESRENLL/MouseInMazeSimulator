import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public enum ImagePatternMap {
	instance;
	
	private Map<String, ImagePattern> map = new HashMap<>();
	public ImagePattern get(String imgName) {
		if(map.get(imgName)!=null) return map.get(imgName);
		Image image = new Image(imgName);
		ImagePattern imgPattern = new ImagePattern(image);
		map.put(imgName, imgPattern);
		return imgPattern;
	}
}
