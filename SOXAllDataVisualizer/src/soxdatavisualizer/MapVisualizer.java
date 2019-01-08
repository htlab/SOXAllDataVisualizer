package soxdatavisualizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Displays countries of the world as simple polygons.
 * 
 * Reads from a GeoJSON file, and uses default marker creation. Features are
 * polygons.
 * 
 * Press SPACE to toggle visibility of the polygons.
 */
public class MapVisualizer extends PApplet {

	UnfoldingMap map;
	AbstractMapProvider cartoDBMap;
	MarkerManager markerManager;
	SimplePointMarker markerLondon;
	int w_width=600;
	int w_height=400;


	public void setup() {
		size(w_width, w_height, OPENGL);
		smooth();

		cartoDBMap = new MapProviderSet2.CartoDBPositionNoLabel();

		map = new UnfoldingMap(this, UUID.randomUUID().toString(), 0, 0, w_width, w_height, true, false, cartoDBMap, null);
		CustomMapUtils.createDefaultEventDispatcher(this, map);
		map.switchTweening();
		map.zoomAndPanTo(4, new Location(35.39f, 139.44f));

		markerManager = map.getDefaultMarkerManager();

		markerLondon = new SimplePointMarker(new Location(35.39f, 139.44f));

		Main main = new Main(this);
	}

	public void draw() {
		synchronized (this) {
			background(255);
			map.draw();

			ScreenPosition posLondon = markerLondon.getScreenPosition(map);
			strokeWeight(12);
			stroke(200, 0, 0, 200);
			strokeCap(SQUARE);
			noFill();
			// Zoom dependent marker size
			// float s = map.getZoom();
			float s = 44;
			arc(posLondon.x, posLondon.y, s, s, -PI * 0.9f, -PI * 0.1f);
			arc(posLondon.x, posLondon.y, s, s, PI * 0.1f, PI * 0.9f);
		}
	}

	public void addMarker(float lat, float lon) {
		synchronized (this) {
			SimplePointMarker marker = new SimplePointMarker(new Location(lat, lon));
			marker.setColor(color(80, 80, 80));
			marker.setRadius(5);
			map.addMarker(marker);

			markerLondon.setLocation(lat, lon);

			this.repaint();
		}
	}

}
