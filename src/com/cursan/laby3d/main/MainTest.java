package com.cursan.laby3d.main;

import java.io.IOException;
import java.util.ArrayList;

import com.cursan.laby3d.engine.Floor;
import com.cursan.laby3d.engine.FloorLoaderSaver;

public class MainTest {
	public static Floor getLabyTest() {

		Floor laby = new Floor(6, 6);
		laby.addHole(5, 0);
		laby.addHole(4, 2);
		laby.addHole(1, 3);
		laby.addHole(1, 4);

		laby.addVerticalWall(1, 1);
		laby.addVerticalWall(4, 2);
		laby.addVerticalWall(5, 2);
		laby.addVerticalWall(4, 3);
		laby.addVerticalWall(2, 3);
		laby.addVerticalWall(2, 4);
		laby.addVerticalWall(2, 5);
		laby.addVerticalWall(3, 3);
		laby.addVerticalWall(3, 4);
		laby.addVerticalWall(5, 5);

		laby.addHorizontalWall(1, 1);
		laby.addHorizontalWall(2, 1);
		laby.addHorizontalWall(3, 1);
		laby.addHorizontalWall(4, 1);
		laby.addHorizontalWall(5, 1);
		laby.addHorizontalWall(1, 2);
		laby.addHorizontalWall(2, 2);
		laby.addHorizontalWall(3, 2);
		laby.addHorizontalWall(4, 2);
		laby.addHorizontalWall(4, 4);
		laby.addHorizontalWall(5, 4);
		laby.addHorizontalWall(0, 3);
		laby.addHorizontalWall(1, 3);
		laby.addHorizontalWall(2, 3);
		laby.addHorizontalWall(1, 4);
		laby.addHorizontalWall(0, 5);
		laby.addHorizontalWall(1, 5);
		laby.addHorizontalWall(3, 5);
		laby.addHorizontalWall(4, 5);
		laby.addHorizontalWall(2, 6);
		laby.addHorizontalWall(3, 6);
		
		laby.setFileName("labyTest.laby");
		return laby;

	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Floor laby = getLabyTest();
		FloorLoaderSaver.save("/users/cursan_a/laby3d", laby);
		ArrayList<Floor> labys = FloorLoaderSaver.loadAll(("/users/cursan_a/laby3d"));
		for (Floor l : labys) 
			System.out.println(l);
		//laby.getStlSolid("toot").generateStlFile("/Users/cursan_a/laby3d/toot.stl");
	}

}
