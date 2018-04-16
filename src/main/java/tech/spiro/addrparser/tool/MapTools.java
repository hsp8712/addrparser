package tech.spiro.addrparser.tool;

import tech.spiro.addrparser.common.Point;

import java.awt.Polygon;
import java.awt.geom.Line2D;

/**
 * Tools related with map calculating.
 * @author Spiro Huang
 * @since 1.0
 */
public class MapTools
{
	private static double EARTH_RADIUS = 6378137;


	private static double rad(double d)
	{
		return d * Math.PI / 180.0;
	}

	/**
	 * Calculate point to point distance (meter)
	 * @param p1   one of 2 points
	 * @param p2   one of 2 points
	 * @return  Distance of 2 points.
	 */
	public static double getPoint2PointDistance(Point p1, Point p2)
	{
		double radLat1 = rad( p1.getLat() );
		double radLat2 = rad( p2.getLat() );
		double a = radLat1 - radLat2;
		double b = rad( p1.getLon() ) - rad( p2.getLon() );
		double s = 2 * Math.asin( Math.sqrt( Math.pow( Math.sin( a / 2 ), 2 ) + Math.cos( radLat1 ) * Math.cos( radLat2 )
				* Math.pow( Math.sin( b / 2 ), 2 ) ) );
		s = s * EARTH_RADIUS;
		s = (double) Math.round( s * 10000 ) / 10000;
		return s;
	}



	/**
	 * Calculate point to line distance (meter)
	 * @param p                 point
	 * @param lineStartPoint    line start point
	 * @param lineEndPoint      line end point
	 * @return  distance of point to line.
	 */
	public static double getPoint2LineDistance(Point p, Point lineStartPoint, Point lineEndPoint)
	{
		double du = Line2D.ptSegDist( lineStartPoint.getLon(), lineStartPoint.getLat(), lineEndPoint.getLon(), lineEndPoint.getLat(), p.getLon(), p.getLat() );
		Point p2 = new Point( p.getLon(), p.getLat() + du );
		return getPoint2PointDistance( p, p2 );
	}

	/**
	 * To determine whether a point in a circle
	 * @param p            The point to determine
	 * @param centerPoint  Circle center point
	 * @param radius       Circle radius
	 * @return  true, point in circle, otherwise false.
	 */
	public static boolean inCircleArea(Point p, Point centerPoint, double radius)
	{
		double dis = getPoint2PointDistance( p, centerPoint );
		if (dis < radius)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * To determine whether a point in a rectangle
	 * @param p                The point to determine
	 * @param upLeftPoint      Rectangle up left point
	 * @param downRightPoint   Rectangle down right point
	 * @return  true, point in rectangle, otherwise false.
	 */
	public static boolean inRectangleArea(Point p, Point upLeftPoint, Point downRightPoint)
	{
		if ((p.getLon() > upLeftPoint.getLon() && p.getLon() < downRightPoint.getLon())
				&& (p.getLat() < upLeftPoint.getLat() && p.getLat() > downRightPoint.getLat()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * To determine whether a point in a polygon
	 * @param p        The point to determine
	 * @param area     Polygon 
	 * @return  true, point in polygon, otherwise false.
	 */
	public static boolean inPolygonArea(Point p, Point area[])
	{
		Polygon pol = new Polygon();
		for ( int i = 0; i < area.length; i++ )
		{
			pol.addPoint( (int) (area[i].getLon() * 100000), (int) (area[i].getLat() * 100000) );
		}
		return pol.contains( (int) (p.getLon() * 100000), (int) (p.getLat() * 100000) );
	}

	/**
	 * To determine whether a point is yawing
	 * @param p            The point to determine
	 * @param line         Track points
	 * @param distance     Yawing distance
	 * @return true, point yawing, otherwise false.
	 */
	public static boolean yawing(Point p, Point line[], double distance)
	{
		for ( int i = 0; i < line.length - 1; i++ )
		{
			if (getPoint2LineDistance( p, line[i], line[i + 1] ) < distance)
			{
				return false;
			}
		}
		return true;
	}

}