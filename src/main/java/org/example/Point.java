package org.example;

import lombok.Data;

@Data
public class Point {

	public static final double N = 2;

	private static double M = 0.25d;

	private final double x1;

	private final double x2;

	private final double delta1;

	private final double delta2;

	private final double f;

	public Point(double x1, double x2) {
		this.x1 = x1;
		this.x2 = x2;
		delta1 = delta1();
		delta2 = delta2();
		f = f(this);
	}

	public Point createPoint1() {
		return new Point(x1 + delta1, x2 + delta2);
	}

	public Point createPoint2() {
		return new Point(x1 + delta2, x2 + delta1);
	}

	private double delta1() {
		return (Math.sqrt(N + 1) - 1) / (N * Math.sqrt(2)) * M;
	}

	private double delta2() {
		return (Math.sqrt(N + 1) + N - 1) / (N * Math.sqrt(2)) * M;
	}

	private double f(Point point) {
		double x1 = point.getX1();
		double x2 = point.getX2();
		return 7 * x1 * x1 + 2 * x1 * x2 + 5 * x2 * x2 + x1 - 10 * x2;
		//return x1 * x1 - x1 * x2 + 3 * x2 * x2 - x1;
		//return 2.8 * x2 * x2 + 1.9 * x1 + 2.7 * x1 * x1 + 1.6 - 1.9 * x2;
	}

	public Point getGradient() {
		//return new Point(5.4 * x1 + 1.9, 5.6 * x2 - 1.9);
		return new Point(14 * x1 + 2 * x2 + 1, 2 * x1 + 10 * x2 - 10);
	}

	public double[][] getGesseMatrix() {
		return new double[][] { { 14d, 2d }, { 2d, 10d } };
	}

	public static void redux() {
		M = M / 2d;
	}

	@Override
	public String toString() {
		return "(" + x1 + ", " + x2 + "). F = " + f;
	}

}
