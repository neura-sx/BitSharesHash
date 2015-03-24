package sx.neura.bts.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Jdenticon {
	
	public static void make(Canvas canvas, String hash, double padding) {
		double size = Math.min(canvas.getWidth(), canvas.getHeight()) - padding;
		int cell = ((int) (size / 8.0)) * 2;
		GraphicsContext ctx = canvas.getGraphicsContext2D();
		ctx.save();
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        ctx.translate(
            ((canvas.getWidth() - (cell * 4)) / 2.0),
            ((canvas.getHeight() - (cell * 4)) / 2.0));
        new Jdenticon(ctx, hash).drawIcon(cell);
        ctx.restore();
	}
	
	public static void clear(Canvas canvas) {
		GraphicsContext ctx = canvas.getGraphicsContext2D();
		ctx.save();
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        ctx.restore();
	}
	
	private static final int SELECTED_COLORS_SIZE = 3;
	
	private GraphicsContext ctx;
	private String hash;
	private int cell;
	private Color[] availableColors;
	private List<Color> selectedColors;
	
	private final Shape[] centerShapes = new Shape[] {
			(Path p, int cell, int index) -> {
				double k = cell * 0.42;
	            p.addPolygon(new double[]{
	                0, 0,
	                cell, 0,
	                cell, cell - k * 2,
	                cell - k, cell,
	                0, cell
	            });
			},
			(Path p, int cell, int index) -> {
				int k = (int) (cell * 0.4);
	            p.addTriangle(cell - k, cell - k * 2, k, k * 2, 1);
			},
			(Path p, int cell, int index) -> {
				 int s = (int) (cell / 3);
		         p.addRectangle(s, s, cell - s, cell - s);
			},
			(Path p, int cell, int index) -> {
				int inner = (int) (cell * 0.1);
		        int outer = (int) (cell * 0.25);
		        p.addRectangle(outer, outer, cell - inner - outer, cell - inner - outer);
			},
			(Path p, int cell, int index) -> {
				int m = (int) (cell * 0.15);
				int s = (int) (cell * 0.5);
		        p.addEllipse(cell - s - m, cell - s - m, s, s);
			},
			(Path p, int cell, int index) -> {
				double inner = cell * 0.1;
	            double outer = inner * 4;
	            p.addRectangle(0, 0, cell, cell);
	            p.addPolygon(new double[] {
	                outer, outer,
	                cell - inner, outer,
	                outer + (cell - outer - inner) / 2, cell - inner
	            }, true);
			},
			(Path p, int cell, int index) -> {
				p.addTriangle(0, 0, cell, cell, 0);
			},
			(Path p, int cell, int index) -> {
				p.addTriangle(cell / 2, cell / 2, cell / 2, cell / 2, 0);
			},
			(Path p, int cell, int index) -> {
				p.addRectangle(0, 0, cell, cell / 2);
	            p.addRectangle(0, cell / 2, cell / 2, cell / 2);
	            p.addTriangle(cell / 2, cell / 2, cell / 2, cell / 2, 1);
			},
			(Path p, int cell, int index) -> {
				int inner = (int) (cell * 0.14);
				int outer = (int) (cell * 0.35);
		        p.addRectangle(0, 0, cell, cell);
		        p.addRectangle(outer, outer, cell - outer - inner, cell - outer - inner, true);
			},
			(Path p, int cell, int index) -> {
				 double inner = cell * 0.12;
			     double outer = inner * 3;
			     p.addRectangle(0, 0, cell, cell);
			     p.addEllipse(outer, outer, cell - inner - outer, cell - inner - outer, true);
			},
			(Path p, int cell, int index) -> {
				p.addTriangle(cell / 2, cell / 2, cell / 2, cell / 2, 3);
			},
			(Path p, int cell, int index) -> {
				double m = cell * 0.25;
	            p.addRectangle(0, 0, cell, cell);
	            p.addRhombus(m, m, cell - m, cell - m, true);
			},
			(Path p, int cell, int index) -> {
				if (index > 0) {
					double m = cell * 0.4;
					double s = cell * 1.2;
	            	p.addEllipse(m, m, s, s);
				}
			}
	};
	
	private final Shape[] outerShapes = new Shape[] {
			(Path p, int cell, int index) -> {
				p.addTriangle(0, 0, cell, cell, 0);
			},
			(Path p, int cell, int index) -> {
				p.addTriangle(0, cell / 2, cell, cell / 2, 0);
			},
			(Path p, int cell, int index) -> {
				p.addRhombus(0, 0, cell, cell);
			},
			(Path p, int cell, int index) -> {
				double m = cell / 6.0;
	            p.addEllipse(m, m, cell - 2 * m, cell - 2 * m);
			},
	};

	private Jdenticon(GraphicsContext ctx, String hash) {
		this.ctx = ctx;
		this.hash = hash;
		this.selectedColors = new ArrayList<Color>(SELECTED_COLORS_SIZE);
	}
	
	private void drawIcon(int cell) {
		this.cell = cell;
        
        /**
         * Available colors
         * the first 15 characters of the hash control the pixels (even/odd)
         * they are drawn down the middle first, then mirrored outwards
         */
        double hue = (double) Integer.parseInt(hash.substring(hash.length() - 7), 16) / 0xfffffff;
        //System.out.println(String.format("hue: %f", hue));
        
        /** Available colors for this icon */
        availableColors = new Color[]{
        	/** Dark gray */
            getRGBColor(76, 76, 76),
            /** Mid color */
            getHSBColorCorrected(hue, 0.5, 0.6),
            /** Light gray */
            getRGBColor(230, 230, 230),
            /** Light color */
            getHSBColorCorrected(hue, 0.5, 0.8),
            /** Dark color */
            getHSBColor(hue, 0.5, 0.4),
        };
        //System.out.println(String.format("availableColors[0]: %f %f %f %f", availableColors[0].getRed() * 255.0, availableColors[0].getBlue() * 255.0, availableColors[0].getGreen() * 255.0, availableColors[1].getOpacity()));
        //System.out.println(String.format("availableColors[1]: %f %f %f %f", availableColors[1].getHue(), availableColors[1].getSaturation(), availableColors[1].getBrightness(), availableColors[1].getOpacity()));
        //System.out.println(String.format("availableColors[2]: %f %f %f %f", availableColors[2].getRed() * 255.0, availableColors[2].getBlue() * 255.0, availableColors[2].getGreen() * 255.0, availableColors[2].getOpacity()));
        //System.out.println(String.format("availableColors[3]: %f %f %f %f", availableColors[3].getHue(), availableColors[3].getSaturation(), availableColors[3].getBrightness(), availableColors[1].getOpacity()));
        //System.out.println(String.format("availableColors[4]: %f %f %f %f", availableColors[4].getHue(), availableColors[4].getSaturation(), availableColors[4].getBrightness(), availableColors[1].getOpacity()));

        List<Color> list1 = new ArrayList<Color>(Arrays.asList(new Color[]{availableColors[0], availableColors[4]}));
        List<Color> list2 = new ArrayList<Color>(Arrays.asList(new Color[]{availableColors[2], availableColors[3]}));
        for (int i = 0; i < SELECTED_COLORS_SIZE; i++) {
            int index = parseInteger(8 + i) % availableColors.length;
            if (isDuplicateColor(list1, index) || isDuplicateColor(list2, index)) {
            	/** Disallow (dark gray and dark color) combo and (light gray and light color) combo */
                index = 1;
            }
            selectedColors.add(availableColors[index]);
        }
        //System.out.println(String.format("selectedColorIndexes.length: %d", selectedColorIndexes.length));
        //System.out.println(String.format("selectedColorIndexes[0]: %d", selectedColorIndexes[0]));
        //System.out.println(String.format("selectedColorIndexes[1]: %d", selectedColorIndexes[1]));
        //System.out.println(String.format("selectedColorIndexes[2]: %d", selectedColorIndexes[2]));

        /** Actual rendering */
        /** Sides */
        ctx.setFill(selectedColors.get(0));
        renderShape(outerShapes, 2, 3, new int[][]{new int []{1, 0}, new int []{2, 0}, new int []{2, 3}, new int []{1, 3}, new int []{0, 1}, new int []{3, 1}, new int []{3, 2}, new int []{0, 2}});
        /** Corners */
        ctx.setFill(selectedColors.get(1));
        renderShape(outerShapes, 4, 5, new int[][]{new int []{0, 0}, new int []{3, 0}, new int []{3, 3}, new int []{0, 3}});
        /** Center */
        ctx.setFill(selectedColors.get(2));
        renderShape(centerShapes, 1, null, new int[][]{new int []{1, 1}, new int []{2, 1}, new int []{2, 2}, new int []{1, 2}});
    }
	
	private void renderShape(Shape[] shapes, int index, Integer rotation, int[][] positions) {
		int r = (rotation != null ? parseInteger(rotation) : 0);
		Shape shape = shapes[parseInteger(index) % shapes.length];
		
		//System.out.println(String.format("r: %d", r));
		//System.out.println(String.format("shape: %d", parseInt(index) % shapes.length));

		for (int i = 0; i < positions.length; i++) {
			ctx.save();
			ctx.translate(positions[i][0] * cell + cell / 2, positions[i][1] * cell + cell / 2);
			ctx.rotate((r++ % 4) * 90);
			ctx.translate(-cell / 2, -cell / 2);
			shape.draw(new Path(ctx), cell, i);
			ctx.fill();
			ctx.restore();
		}
	}

	private int parseInteger(int i) {
		return Integer.parseInt(new String(new char[] { hash.charAt(i) }), 16);
	}
	
	private boolean isDuplicateColor(List<Color> colors, int index) {
		if (colors.contains(availableColors[index])) {
			for (Color color : colors) {
				if (selectedColors.contains(color))
					return true;
			}
		}
		return false;
	}
	
	private static double[] splice(Double[] items, int index, int count) {
		List<Double> list = new ArrayList<Double>(Arrays.asList(items));
		for (int i = 0; i < count; i++)
			list.remove(index);
        double[] array = new double[list.size()];
        for (int i = 0; i < list.size(); i++)
        	array[i] = list.get(i).doubleValue();
        return array;
	}
	
	private static Color getRGBColor(int red, int green, int blue) {
		return Color.rgb(red, green, blue);
	}
	
	private static Color getHSBColor(double h, double s, double b) {
		return Color.hsb(h * 360, s, b);
	}
	
	private static Color getHSBColorCorrected(double h, double s, double b) {
		double[] correctors = new double[] {0.95, 1, 1, 1, 0.7, 0.8, 0.8};
		return Color.hsb(h * 360, s, 1 - correctors[(int) (h * 6 + 0.5)] * (1 - b));
	}

	private interface Shape {
		void draw(Path p, int cell, int index);
	}
	
	private class Path {
		
		private GraphicsContext ctx;
		
		private Path(GraphicsContext ctx) {
			this.ctx = ctx;
			this.ctx.beginPath();
		}
		
		/**
	     * Adds a polygon to the path.
	     * @param {Array} points The points of the polygon clockwise on the format [ x0, y0, x1, y1, ..., xn, yn ]
	     * @param {boolean=} invert Specifies if the polygon will be inverted.
	     */
		private void addPolygon(double[] points, boolean invert) {
			int di = (invert ? -2 : 2);
			int i = (invert ? points.length - 2 : 0);
			
			//System.out.println(String.format("moveTo: %f | %f", points[i], points[i + 1]));
			ctx.moveTo(points[i], points[i + 1]);
			
			for (i += di; i < points.length && i >= 0; i += di) {
				//System.out.println(String.format("lineTo: %f | %f", points[i], points[i + 1]));
			    ctx.lineTo(points[i], points[i + 1]);
			}
			ctx.closePath();
		}
		private void addPolygon(double[] points) {
			addPolygon(points, false);
		}
		
		/**
	     * Adds a polygon to the path.
	     * Source: http://stackoverflow.com/a/2173084
	     * @param {number} x The x-coordinate of the upper left corner of the rectangle holding the entire ellipse.
	     * @param {number} y The y-coordinate of the upper left corner of the rectangle holding the entire ellipse.
	     * @param {number} w The width of the ellipse.
	     * @param {number} h The height of the ellipse.
	     * @param {boolean=} invert Specifies if the ellipse will be inverted.
	     */
		private void addEllipse(double x, double y, double w, double h, boolean invert) {
	       double kappa = 0.5522848;
	       double ox = (w / 2) * kappa; 	/** control point offset horizontal */
	       double oy = (h / 2) * kappa; 	/** control point offset vertical */
	       double xe = x + w;          		/** x-end */
	       double ye = y + h;        		/** y-end */
	       double xm = x + w / 2;     		/** x-middle */
	       double ym = y + h / 2;       	/** y-middle */
	        if (invert) {
	            ye = y;
	            y = y + h;
	            oy = -oy;
	        }
	        ctx.moveTo(x, ym);
	        ctx.bezierCurveTo(x, ym - oy, xm - ox, y, xm, y);
	        ctx.bezierCurveTo(xm + ox, y, xe, ym - oy, xe, ym);
	        ctx.bezierCurveTo(xe, ym + oy, xm + ox, ye, xm, ye);
	        ctx.bezierCurveTo(xm - ox, ye, x, ym + oy, x, ym);
	        ctx.closePath();
	    }
		private void addEllipse(double x, double y, double w, double h) {
			addEllipse(x, y, w, h, false);
		}
		
		/**
	     * Adds a rectangle to the path.
	     * @param {number} x The x-coordinate of the upper left corner of the rectangle.
	     * @param {number} y The y-coordinate of the upper left corner of the rectangle.
	     * @param {number} w The width of the rectangle.
	     * @param {number} h The height of the rectangle.
	     * @param {boolean=} invert Specifies if the rectangle will be inverted.
	     */
		private void addRectangle(double x, double y, double w, double h, boolean invert) {
	        this.addPolygon(new double[]{
				x, y, 
		        x + w, y,
		        x + w, y + h,
		        x, y + h
	        }, invert);
	    }
		private void addRectangle(double x, double y, double w, double h) {
			addRectangle(x, y, w, h, false);
		}
		
		/**
	     * Adds a right triangle to the path.
	     * @param {number} x The x-coordinate of the upper left corner of the rectangle holding the triangle.
	     * @param {number} y The y-coordinate of the upper left corner of the rectangle holding the triangle.
	     * @param {number} w The width of the triangle.
	     * @param {number} h The height of the triangle.
	     * @param {number} r The rotation of the triangle (clockwise). 0 = right corner of the triangle in the lower left corner of the bounding rectangle.
	     * @param {boolean=} invert Specifies if the triangle will be inverted.
	     */
		private void addTriangle(double x, double y, double w, double h, int r, boolean invert) {
	        this.addPolygon(splice(new Double[]{
	    		x + w, y, 
	            x + w, y + h, 
	            x, y + h,
	            x, y
	        }, r % 4, 2), invert);
	    }
		private void addTriangle(double x, double y, double w, double h, int r) {
			addTriangle(x, y, w, h, r, false);
		}
		
		/**
	     * Adds a rhombus to the path.
	     * @param {number} x The x-coordinate of the upper left corner of the rectangle holding the rhombus.
	     * @param {number} y The y-coordinate of the upper left corner of the rectangle holding the rhombus.
	     * @param {number} w The width of the rhombus.
	     * @param {number} h The height of the rhombus.
	     * @param {boolean=} invert Specifies if the rhombus will be inverted.
	     */
		private void addRhombus(double x, double y, double w, double h, boolean invert) {
	        this.addPolygon(new double[]{
	            x + w / 2, y,
	            x + w, y + h / 2,
	            x + w / 2, y + h,
	            x, y + h / 2
	        }, invert);
	    }
		private void addRhombus(double x, double y, double w, double h) {
			addRhombus(x, y, w, h, false);
		}
	}
}
