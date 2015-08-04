package com.cursan.laby3d.stl;

import java.io.File;

import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import com.interactivemesh.jfx.importer.stl.StlMeshImporter;

public class StlViewer {

	private String stlPath;

	private static final double MODEL_SCALE_FACTOR = 400;
	private static final double MODEL_X_OFFSET = 0; // standard
	private static final double MODEL_Y_OFFSET = 0; // standard

	private static final int VIEWPORT_SIZE = 700;

	private static final Color lightColor = Color.rgb(102, 204, 255);
	private static final Color jewelColor = Color.rgb(82, 194, 255);

	private Group root;
	private PointLight pointLight;

	private MeshView[] loadMeshViews() {
		File file = new File(stlPath);
		StlMeshImporter importer = new StlMeshImporter();
		importer.read(file);
		Mesh mesh = importer.getImport();

		return new MeshView[] { new MeshView(mesh) };
	}

	private Group buildScene() {
		MeshView[] meshViews = loadMeshViews();
		for (int i = 0; i < meshViews.length; i++) {
			meshViews[i].setTranslateX(VIEWPORT_SIZE / 2 + MODEL_X_OFFSET);
			meshViews[i].setTranslateY(VIEWPORT_SIZE / 2 + MODEL_Y_OFFSET);
			meshViews[i].setTranslateZ(VIEWPORT_SIZE / 2);
			meshViews[i].setScaleX(MODEL_SCALE_FACTOR);
			meshViews[i].setScaleY(MODEL_SCALE_FACTOR);
			meshViews[i].setScaleZ(MODEL_SCALE_FACTOR);

			PhongMaterial sample = new PhongMaterial(jewelColor);
			meshViews[i].setMaterial(sample);
			meshViews[i].getTransforms().setAll(new Rotate(60, Rotate.X_AXIS), new Rotate(-10, Rotate.Z_AXIS));
		}

		pointLight = new PointLight(lightColor);
		pointLight.setTranslateX(VIEWPORT_SIZE * 3 / 4);
		pointLight.setTranslateY(VIEWPORT_SIZE / 2);
		pointLight.setTranslateZ(VIEWPORT_SIZE / 2);
		PointLight pointLight2 = new PointLight(lightColor);
		pointLight2.setTranslateX(VIEWPORT_SIZE * 1 / 4);
		pointLight2.setTranslateY(VIEWPORT_SIZE * 3 / 4);
		pointLight2.setTranslateZ(VIEWPORT_SIZE * 3 / 4);
		PointLight pointLight3 = new PointLight(lightColor);
		pointLight3.setTranslateX(VIEWPORT_SIZE * 5 / 8);
		pointLight3.setTranslateY(VIEWPORT_SIZE / 2);
		pointLight3.setTranslateZ(0);

		Color ambientColor = Color.rgb(80, 80, 80, 0);
		AmbientLight ambient = new AmbientLight(ambientColor);

		root = new Group(meshViews);
		root.getChildren().add(pointLight);
		root.getChildren().add(pointLight2);
		root.getChildren().add(pointLight3);
		root.getChildren().add(ambient);

		return root;
	}

	private PerspectiveCamera addCamera(Scene scene) {
		PerspectiveCamera perspectiveCamera = new PerspectiveCamera();
		scene.setCamera(perspectiveCamera);
		return perspectiveCamera;
	}

	public void render(String stlPath) {
		this.stlPath = stlPath;
		Stage stage = new Stage();
		Group group = buildScene();
		group.setScaleX(0.3);
		group.setScaleY(0.3);
		group.setScaleZ(0.3);
		group.setTranslateY(-1100);

		Scene scene = new Scene(group, VIEWPORT_SIZE, VIEWPORT_SIZE, true);
		scene.setFill(Color.rgb(180, 180, 180));
		this.addCamera(scene);
		stage.setTitle(stlPath);
		stage.setScene(scene);
		stage.show();
	}
}