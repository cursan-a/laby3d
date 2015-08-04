package com.cursan.laby3d.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class LabyrinthLoaderSaver {
	public final static String EXTENSION_LABYRINTH = ".laby";
	
	public static Labyrinth load(String workstation, String fileName) throws IOException, ClassNotFoundException {
		FileInputStream fIn = new FileInputStream(workstation + File.separator + fileName);
		ObjectInputStream oIn = new ObjectInputStream(fIn);
		Labyrinth laby = (Labyrinth)oIn.readObject();
		oIn.close();
		return laby;
	}
	
	public static void save (String workstation, Labyrinth laby) throws IOException {
		FileOutputStream fOut = new FileOutputStream(workstation + File.separator + laby.getFileName(), false);
        ObjectOutputStream oOut = new ObjectOutputStream(fOut);
        oOut.writeObject(laby);
        oOut.close();
	}
	
	public static ArrayList<Labyrinth> loadAll(String workstation){
		ArrayList<Labyrinth> ret = new ArrayList<Labyrinth>();
		File folder = new File(workstation);
		for (File file : folder.listFiles()) {
			if (file.getName().endsWith(EXTENSION_LABYRINTH))
				try {
					ret.add(load(workstation, file.getName()));
				} catch (ClassNotFoundException | IOException e) {
					System.out.println("Unable to open : " + file.getAbsolutePath());
				}
		}
		return ret;
	}
	
	public static void delete(String workstation, Labyrinth laby){
		new File(workstation + File.separator + laby.getFileName()).delete();
	}
}
