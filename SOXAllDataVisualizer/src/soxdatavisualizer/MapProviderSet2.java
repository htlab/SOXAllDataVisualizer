package soxdatavisualizer;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.MercatorProjection;
import de.fhpotsdam.unfolding.geo.Transformation;
import de.fhpotsdam.unfolding.providers.AbstractMapTileUrlProvider;

/**
 * Provider based on Leaflet-providers: http://leaflet-extras.github.io/leaflet-providers/preview/index.html
 * Various map tiles from Opencycle. http://www.opencyclemap.org
 */
public class MapProviderSet2 {
	public static abstract class GenericTestMapProvider extends AbstractMapTileUrlProvider {

		public GenericTestMapProvider() {
			super(new MercatorProjection(26, new Transformation(1.068070779e7, 0.0, 3.355443185e7, 0.0,
					-1.068070890e7, 3.355443057e7)));
		}

		public String getZoomString(Coordinate coordinate) {
			return (int) coordinate.zoom + "/" + (int) coordinate.column + "/" + (int) coordinate.row;
		}

		public int tileWidth() {
			return 256;
		}

		public int tileHeight() {
			return 256;
		}

		public abstract String[] getTileUrls(Coordinate coordinate);
	}


	
	public static class CartoDBPositionNoLabel extends GenericTestMapProvider{

		@Override
		public String[] getTileUrls(Coordinate coordinate) {
			// TODO Auto-generated method stub
			String url = "http://c.basemaps.cartocdn.com/light_nolabels/" + getZoomString(coordinate)+".png";
			return new String[] { url };
		}
		
		
	}
	
	public static class CyberJapanSatelliteMap1974To1978 extends GenericTestMapProvider{
		public String[] getTileUrls(Coordinate coordinate){
			String url="http://cyberjapandata.gsi.go.jp/xyz/gazo1/"+getZoomString(coordinate)+".jpg";
			return new String[]{ url};
		}
	}

	public static class CyberJapanSatelliteMap2004 extends GenericTestMapProvider{
		public String[] getTileUrls(Coordinate coordinate){
			String url="http://cyberjapandata.gsi.go.jp/xyz/airphoto/"+getZoomString(coordinate)+".png";
			return new String[]{ url};
		}
	}
	
	public static class CyberJapanMap extends GenericTestMapProvider{
		public String[] getTileUrls(Coordinate coordinate){
			String url="http://cyberjapandata.gsi.go.jp/xyz/std/"+getZoomString(coordinate)+".png";
			return new String[]{ url};
		}
	}
	
	public static class StatemanToner extends GenericTestMapProvider{
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://c.tile.stamen.com/toner/"+ getZoomString(coordinate)+".png";
			return new String[] { url };
		}
	}
	
	public static class OSMBlackAndWhite extends GenericTestMapProvider{
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://b.tiles.wmflabs.org/bw-mapnik/"+ getZoomString(coordinate)+".png";
			return new String[] { url };
		}
		
	}
	
	public static class DarkTerrain extends GenericTestMapProvider{
		public String[] getTileUrls(Coordinate coordinate){
			String url = "http://b.tiles.mapbox.com/v3/landplanner.gindn6nm/"+getZoomString(coordinate)+".png";
			return new String[] { url };
		}
	}
	
	

	public static class Transport extends GenericTestMapProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://tile.thunderforest.com/transport/" + getZoomString(coordinate) + ".png";
			return new String[] { url };
		}
	}
	
	public static class Landscape extends GenericTestMapProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://tile.thunderforest.com/landscape/" + getZoomString(coordinate) + ".png";
			return new String[] { url };
		}
	}
	
	public static class Outdoors extends GenericTestMapProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://tile.thunderforest.com/outdoors/" + getZoomString(coordinate) + ".png";
			return new String[] { url };
		}
	}

}