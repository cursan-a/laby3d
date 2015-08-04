package com.cursan.laby3d.engine;

import java.io.Serializable;

import com.cursan.laby3d.stl.StlCube;
import com.cursan.laby3d.stl.StlPoint;
import com.cursan.laby3d.stl.StlSolid;

public class Floor implements Serializable {
	private static final long serialVersionUID = -7327255066483850118L;
	private int width;
	private int depth;
	private boolean[][] holes;
	private boolean[][] verticalWalls;
	private boolean[][] horizontalWalls;
	private double thickness = 0.1;
	private double wallThickness = 0.6;
	private String fileName;

	public Floor(int width, int depth) {
		super();
		this.width = width;
		this.depth = depth;
		holes = new boolean[width][depth];
		verticalWalls = new boolean[width + 1][depth];
		horizontalWalls = new boolean[width][depth + 1];
	}

	public int getWidth() {
		return width;
	}

	public int getDepth() {
		return depth;
	}

	public double getThickness() {
		return thickness;
	}

	public void setThickness(double thickness) {
		this.thickness = thickness;
	}

	public double getWallThickness() {
		return wallThickness;
	}

	public void setWallThickness(double wallThickness) {
		this.wallThickness = wallThickness;
	}

	public void addHole(int x, int z) {
		holes[x][z] = true;
	}

	public void removeHole(int x, int z) {
		holes[x][z] = false;
	}

	public boolean isHole(int x, int z) {
		return holes[x][z];
	}

	public void addVerticalWall(int x, int z) {
		verticalWalls[x][z] = true;
	}

	public void removeVerticalWall(int x, int z) {
		verticalWalls[x][z] = false;
	}

	public boolean isVerticalWall(int x, int z) {
		return verticalWalls[x][z];
	}

	public void addHorizontalWall(int x, int z) {
		horizontalWalls[x][z] = true;
	}

	public void removeHorizontalWall(int x, int z) {
		horizontalWalls[x][z] = false;
	}

	public boolean isHorizontalWall(int x, int z) {
		return horizontalWalls[x][z];
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public StlSolid getStlSolid(String name) {
		StlSolid solid = new StlSolid(name);
		for (int x = 0; x < this.width; x++)
			for (int z = 0; z < this.depth; z++)
				if (!holes[x][z])
					solid.addCube(new StlCube(new StlPoint(x, 0, z), new StlPoint(x + 1, thickness, z + 1)));
		for (int x = 0; x <= this.width; x++)
			for (int z = 0; z < this.depth; z++)
				if (verticalWalls[x][z])
					solid.addCube(new StlCube(new StlPoint(x, 0.1, z), new StlPoint(x + thickness, thickness + wallThickness, z + 1)));
		for (int x = 0; x < this.width; x++)
			for (int z = 0; z <= this.depth; z++)
				if (horizontalWalls[x][z])
					solid.addCube(new StlCube(new StlPoint(x, 0.1, z), new StlPoint(x + 1, thickness + wallThickness, z + thickness)));
		return solid;
	}

	@Override
	public String toString() {
		int nbHoles = 0;
		for (int x = 0; x < this.width; x++)
			for (int z = 0; z < this.depth; z++)
				if (holes[x][z])
					nbHoles++;
		int nbVerticalWalls = 0;
		for (int x = 0; x <= this.width; x++)
			for (int z = 0; z < this.depth; z++)
				if (verticalWalls[x][z])
					nbVerticalWalls++;
		int nbHorizontalWalls = 0;
		for (int x = 0; x < this.width; x++)
			for (int z = 0; z <= this.depth; z++)
				if (horizontalWalls[x][z])
					nbHorizontalWalls++;
		return "Labyrinth [width=" + width + ", depth=" + depth + ", holes=" + nbHoles  + ", verticalWalls=" + nbVerticalWalls + ", horizontalWalls="
				+ nbHorizontalWalls + ", thickness=" + thickness + ", wallThickness=" + wallThickness + ", fileName=" + fileName + "]";
	}
	
	
}
