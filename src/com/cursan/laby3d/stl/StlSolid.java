package com.cursan.laby3d.stl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class StlSolid {
	private ArrayList<StlCube> cubes = new ArrayList<StlCube>();
	private String name;

	public StlSolid(String name) {
		super();
		this.name = name;
	}

	public void addCube(StlCube cube) {
		this.cubes.add(cube);
	}

	public String generateStlContent() {
		String content = new String();
		content += "solid " + name + System.lineSeparator();
		for (StlCube cube : cubes)
			content += cube.generateStlFacets();
		content += "endsolid " + name + System.lineSeparator();
		return content;
	}

	public void generateStlFile(String path) {
		File file = new File(path);
		generateStlFile(file);
	}

	public void generateStlFile(File file) {
		BufferedWriter output = null;
		try {
			output = new BufferedWriter(new FileWriter(file));
			output.write(generateStlContent());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
