package com.cursan.laby3d.engine;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import com.cursan.laby3d.stl.StlCube;
import com.cursan.laby3d.stl.StlPoint;
import com.cursan.laby3d.stl.StlSolid;

public class Labyrinth implements Serializable {
	private static final long serialVersionUID = 4522879809396390751L;
	private ArrayList<Floor> floors = new ArrayList<Floor>();
	private String fileName;

	public Labyrinth(String fileName) {
		super();
		this.fileName = fileName;
	}

	public ArrayList<Floor> getFloors() {
		return floors;
	}

	public String getFileName() {
		return fileName;
	}

	public void generate(String workstation) {
		String path = workstation + File.separator + fileName + ".dir";
		File dir = new File(path);
		dir.mkdir();
		for (int i = 0; i < floors.size(); i++) {
			Floor floor = floors.get(i);
			String saveName = floor.getFileName();
			floor.setFileName(i + FloorLoaderSaver.EXTENSION_FLOOR);
			try {
				FloorLoaderSaver.save(path, floor);
			} catch (IOException e) {
				e.printStackTrace();
			}
			floor.setFileName(saveName);
		}
		generateBloc(path);
	}
	
	private void generateBloc(String path) {
		StlSolid solid = new StlSolid("bloc");
		solid.addCube(new StlCube(new StlPoint(0, 0, 0), new StlPoint(6, 0.1, 6)));
		solid.addCube(new StlCube(new StlPoint(0, 0.1, 0), new StlPoint(0.1, 0.1 + floors.size() * 0.7, 6)));
		solid.addCube(new StlCube(new StlPoint(5.9, 0.1, 0), new StlPoint(6, 0.1 + floors.size() * 0.7, 6)));
		solid.addCube(new StlCube(new StlPoint(0.1, 0.1, 0), new StlPoint(5.9, 0.1 + floors.size() * 0.7, 0.1)));
		solid.addCube(new StlCube(new StlPoint(0.1, 0.1, 5.9), new StlPoint(5.9, 0.1 + floors.size() * 0.7, 6)));
		solid.generateStlFile(path + File.separator + "bloc.stl");
	}
}
