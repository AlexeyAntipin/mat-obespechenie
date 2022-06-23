package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Main {

	private static final double E = 0.1;
	private static double H = 0.4;

	public static void main(String[] args) {
		simplex();
		gradient();
		newton();
	}

	// Симплекс

	private static void simplex() {
		System.out.println("Симплекс метод:");
		Point startPoint = new Point(1, 1);
		int i = 0;
		List<Point> points = new ArrayList<>(List.of(startPoint, startPoint.createPoint1(), startPoint.createPoint2()));
		do {
			points.sort(Comparator.comparingDouble(Point::getF));
			points.add(getAntiPoint(points.get(2), findCenter(points.get(0), points.get(1))));
			points.remove(2);
			i++;
		} while (!check(points));
		System.out.println(points.get(points.size() - 1));
		System.out.println("Количество итераций: " + i + "\n");
	}

	private static Point findCenter(Point... points) {
		double x1 = Arrays.stream(points).mapToDouble(Point::getX1).sum();
		double x2 = Arrays.stream(points).mapToDouble(Point::getX2).sum();
		return new Point(x1 / points.length, x2 / points.length);
	}

	private static Point getAntiPoint(Point point, Point center) {
		return new Point(2 * center.getX1() - point.getX1(), 2 * center.getX2() - point.getX2());
	}

	private static Boolean check(List<Point> points) {
		Point center = findCenter(points.toArray(Point[]::new));
		return points.stream()
				.map(point -> Math.abs(point.getF() - center.getF()) < E)
				.reduce(true, (result, value) -> result && value);
	}

	// Градиентный спуск

	private static void gradient() {
		System.out.println("Градиентный спуск:");
		Point startPoint = new Point(1, 1);
		List<Point> points = new ArrayList<>();
		points.add(startPoint);
		int i = 0;
		while (!checkEndOfGradient(points.get(points.size() - 1))) {
			points.add(getNext(points));
			i++;
		}
		System.out.println(points.get(points.size() - 1));
		System.out.println("Количество итераций: " + i + "\n");
	}

	private static Point getNext(List<Point> points) {
		Point point = points.get(points.size() - 1);
		Point gradient = point.getGradient();
		Point nextPoint = new Point(point.getX1() - H * gradient.getX1(), point.getX2() - H * gradient.getX2());
		while (nextPoint.getF() > point.getF()) {
			H /= 2;
			nextPoint = new Point(point.getX1() - H * gradient.getX1(), point.getX2() - H * gradient.getX2());;
		}
		return nextPoint;
	}

	private static boolean checkEndOfGradient(Point point) {
		Point gradient = point.getGradient();
		return Math.sqrt(gradient.getX1() * gradient.getX1() + gradient.getX2() * gradient.getX2()) < E;
	}

	// Ньютон

	private static void newton() {
		System.out.println("Ньютон:");
		List<Point> points = new ArrayList<>();
		points.add(new Point(-0.25, 0.5));
		int i = 0;
		double det = det(getRevertMatrix(points.get(0).getGesseMatrix()));
		do {
			Point point = points.get(points.size() - 1);
			Point gradient = point.getGradient();
			points.add(new Point(point.getX1() - det * gradient.getX1(), point.getX2() - det * gradient.getX2()));
			i++;
		} while (!checkEndOfGradient(points.get(points.size() - 1)));
		System.out.println(points.get(points.size() - 1));
		System.out.println("Количество итераций: " + i + "\n");
	}

	private static double[][] getRevertMatrix(double[][] matrix) {
		double[][] clone = matrix.clone();
		double koef = 1d / det(clone);
		double t = clone[0][1];
		clone[0][1] = clone[1][0];
		clone[1][0] = t;
		for (int i = 0; i < clone.length; i++) {
			for (int j = 0; j < clone[0].length; j++) {
				clone[i][j] *= koef;
			}
		}
		return clone;
	}

	private static double det(double[][] matrix) {
		return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
	}

}
