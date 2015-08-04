package com.cursan.laby3d.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FloorLoaderSaver {
	public final static String EXTENSION_FLOOR = ".floor";
	
	public static Floor load(String workstation, String fileName) throws IOException, ClassNotFoundException {
		FileInputStream fIn = new FileInputStream(workstation + File.separator + fileName);
		ObjectInputStream oIn = new ObjectInputStream(fIn);
		Floor laby = (Floor)oIn.readObject();
		oIn.close();
		return laby;
	}
	
	public static void save (String workstation, Floor laby) throws IOException {
		FileOutputStream fOut = new FileOutputStream(workstation + File.separator + laby.getFileName(), false);
        ObjectOutputStream oOut = new ObjectOutputStream(fOut);
        oOut.writeObject(laby);
        oOut.close();
		laby.getStlSolid(laby.getFileName()).generateStlFile(workstation + File.separator + laby.getFileName() + ".stl");
	}
	
	public static ArrayList<Floor> loadAll(String workstation){
		ArrayList<Floor> ret = new ArrayList<Floor>();
		File folder = new File(workstation);
		for (File file : folder.listFiles()) {
			if (file.getName().endsWith(EXTENSION_FLOOR))
				try {
					ret.add(load(workstation, file.getName()));
				} catch (ClassNotFoundException | IOException e) {
					System.out.println("Unable to open : " + file.getAbsolutePath());
				}
		}
		return ret;
	}
	
	public static void delete(String workstation, Floor laby){
		new File(workstation + File.separator + laby.getFileName()).delete();
		new File(workstation + File.separator + laby.getFileName() + ".stl").delete();
	}
}
