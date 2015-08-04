package com.cursan.laby3d.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import com.cursan.laby3d.engine.Floor;
import com.cursan.laby3d.engine.FloorLoaderSaver;
import com.cursan.laby3d.engine.Labyrinth;
import com.cursan.laby3d.engine.LabyrinthLoaderSaver;
import com.cursan.laby3d.stl.StlViewer;
import com.interactivemesh.jfx.importer.ImportException;

public class MainController {
	@FXML
	private ListView<String> lv_fc_floors;
	@FXML
	private ListView<String> lv_lc_floors;
	@FXML
	private ListView<String> lv_lc_labys;
	@FXML
	private ListView<String> lv_lc_selected;
	@FXML
	private AnchorPane ap_grid;

	private String workstation = "/Users/cursan_a/laby3d";
	private final static int DEFAULT_WIDTH = 6;
	private final static int DEFAULT_DEPTH = 6;

	private ArrayList<Floor> floors;
	private ArrayList<Labyrinth> labyrinths;

	private Floor currentLaby = null;

	private Stage primaryStage;

	@FXML
	private void initialize() {
		ap_grid.setDisable(false);
		TextInputDialog dialog = new TextInputDialog(workstation);
		dialog.setTitle("Workstation location");
		dialog.setHeaderText("Workstation location");
		dialog.setContentText("Please enter workstation:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent())
			workstation = result.get();
		floors = FloorLoaderSaver.loadAll(workstation);
		for (Floor floor : floors)
			lv_fc_floors.getItems().add(floor.getFileName());
		labyrinths = LabyrinthLoaderSaver.loadAll(workstation);
		for (Labyrinth laby : labyrinths)
			lv_lc_labys.getItems().add(laby.getFileName());
	}

	@FXML
	private void floorCreatorSelected() {

	}

	@FXML
	private void labyCreatorSelected() {
		lv_lc_floors.getItems().clear();
		for (Floor laby : floors)
			lv_lc_floors.getItems().add(laby.getFileName());
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	// F L O O R
	@FXML
	private void newFloor(ActionEvent e) {
		TextInputDialog dialog = new TextInputDialog("New File");
		dialog.setTitle("New labyrinth floor name");
		dialog.setHeaderText("New labyrinth floor name");
		dialog.setContentText("Please enter name:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			String fileName = result.get() + FloorLoaderSaver.EXTENSION_FLOOR;
			for (Floor laby : floors)
				if (laby.getFileName().equals(fileName)) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Erreur de nommage");
					alert.setHeaderText("Le nom de fichier choisi n'est pas disponible");
					alert.setContentText(fileName);
					alert.showAndWait();
					return;
				}
			Floor laby = new Floor(DEFAULT_WIDTH, DEFAULT_DEPTH);
			floors.add(laby);
			laby.setFileName(fileName);
			lv_fc_floors.getItems().add(fileName);
			lv_fc_floors.getSelectionModel().select(floors.size() - 1);
			this.floorSelected();
			this.isNotSaved();
		}
	}

	@FXML
	private void deleteFloor(ActionEvent e) {
		if (currentLaby == null)
			return;
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Suppression");
		alert.setHeaderText("Êtes-vous sûr de vouloir supprimer le fichier selectionner ?");
		alert.setContentText(currentLaby.getFileName());
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			FloorLoaderSaver.delete(workstation, currentLaby);
			floors.remove(currentLaby);
			currentLaby = null;
			lv_fc_floors.getItems().remove(lv_fc_floors.getSelectionModel().getSelectedIndex());
			clearGrid();
			lv_fc_floors.getSelectionModel().clearSelection();
		}
	}

	private void clearGrid() {
		for (int x = 0; x < DEFAULT_WIDTH; x++)
			for (int z = 0; z < DEFAULT_DEPTH; z++) {
				Button btn = (Button) primaryStage.getScene().lookup("#hole_" + x + "_" + z);
				btn.setStyle("-fx-background-color: #ffffffff; -fx-border-color: black;");
			}

		for (int x = 0; x <= DEFAULT_WIDTH; x++)
			for (int z = 0; z < DEFAULT_DEPTH; z++) {
				Button btn = (Button) primaryStage.getScene().lookup("#verticalWall_" + x + "_" + z);
				btn.setStyle("-fx-background-color: #ffffffff; -fx-border-color: black; -fx-border-style: dashed");
			}

		for (int x = 0; x < DEFAULT_WIDTH; x++)
			for (int z = 0; z <= DEFAULT_DEPTH; z++) {
				Button btn = (Button) primaryStage.getScene().lookup("#horizontalWall_" + x + "_" + z);
				btn.setStyle("-fx-background-color: #ffffffff; -fx-border-color: black; -fx-border-style: dashed");
			}
	}

	@FXML
	private void saveFloor(ActionEvent e) {
		if (currentLaby == null)
			return;
		try {
			FloorLoaderSaver.save(workstation, currentLaby);
			this.isSaved();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Sauvegarde");
			alert.setHeaderText("Sauvegarde correctement effectuée");
			alert.setContentText(currentLaby.getFileName() + System.lineSeparator() + currentLaby.getFileName() + ".stl");
			alert.showAndWait();
		} catch (IOException e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Impossible de sauvegarder le fichier");
			alert.setHeaderText("L'erreur suivante est survenue : ");
			alert.setContentText(e1.toString());
			alert.showAndWait();
		}
	}

	private void isNotSaved() {
		lv_fc_floors.getItems().set(lv_fc_floors.getSelectionModel().getSelectedIndex(), currentLaby.getFileName() + "*");
	}

	private void isSaved() {
		lv_fc_floors.getItems().set(floors.indexOf(currentLaby), currentLaby.getFileName());
	}

	@FXML
	private void showFloor(ActionEvent event) {
		if (currentLaby == null)
			return;
		try {
			new StlViewer().render(workstation + File.separator + currentLaby.getFileName() + ".stl");
		} catch (ImportException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Impossible de visualiser le fichier");
			alert.setHeaderText("Avez-vous pensez à sauvegarder le fichier ?");
			alert.setContentText(e.toString());
			alert.showAndWait();
		}
	}

	@FXML
	private void lc_showFloor(ActionEvent event) {
		Floor laby = floors.get(lv_lc_floors.getSelectionModel().getSelectedIndex());
		try {
			new StlViewer().render(workstation + File.separator + laby.getFileName() + ".stl");
		} catch (ImportException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Impossible de visualiser le fichier");
			alert.setHeaderText("Avez-vous pensez à sauvegarder le fichier ?");
			alert.setContentText(e.toString());
			alert.showAndWait();
		}
	}

	@FXML
	private void floorSelected() {
		Floor laby = floors.get(lv_fc_floors.getSelectionModel().getSelectedIndex());
		for (int x = 0; x < laby.getWidth(); x++)
			for (int z = 0; z < laby.getDepth(); z++) {
				Button btn = (Button) primaryStage.getScene().lookup("#hole_" + x + "_" + z);
				if (laby.isHole(x, z))
					btn.setStyle("-fx-background-color: #444444ff; -fx-border-color: black;");
				else
					btn.setStyle("-fx-background-color: #ffffffff; -fx-border-color: black;");
			}

		for (int x = 0; x <= laby.getWidth(); x++)
			for (int z = 0; z < laby.getDepth(); z++) {
				Button btn = (Button) primaryStage.getScene().lookup("#verticalWall_" + x + "_" + z);
				if (laby.isVerticalWall(x, z))
					btn.setStyle("-fx-background-color: #3fa7c6ff; -fx-border-color: black; -fx-border-style: dashed");
				else
					btn.setStyle("-fx-background-color: #ffffffff; -fx-border-color: black; -fx-border-style: dashed");
			}

		for (int x = 0; x < laby.getWidth(); x++)
			for (int z = 0; z <= laby.getDepth(); z++) {
				Button btn = (Button) primaryStage.getScene().lookup("#horizontalWall_" + x + "_" + z);
				if (laby.isHorizontalWall(x, z))
					btn.setStyle("-fx-background-color: #3fa7c6ff; -fx-border-color: black; -fx-border-style: dashed");
				else
					btn.setStyle("-fx-background-color: #ffffffff; -fx-border-color: black; -fx-border-style: dashed");
			}
		this.currentLaby = laby;
	}

	@FXML
	private void addHoleClick(ActionEvent e) {
		if (currentLaby == null)
			return;
		Button button = ((Button) e.getSource());
		String[] idSplit = button.getId().split("_");
		int x = Integer.parseInt(idSplit[1]);
		int z = Integer.parseInt(idSplit[2]);
		if (currentLaby.isHole(x, z)) {
			currentLaby.removeHole(x, z);
			button.setStyle("-fx-background-color: #ffffffff; -fx-border-color: black;");
		} else {
			currentLaby.addHole(x, z);
			button.setStyle("-fx-background-color: #444444ff; -fx-border-color: black;");
		}
		this.isNotSaved();
	}

	@FXML
	private void addVerticalWallClick(ActionEvent e) {
		if (currentLaby == null)
			return;
		Button button = ((Button) e.getSource());
		String[] idSplit = button.getId().split("_");
		int x = Integer.parseInt(idSplit[1]);
		int z = Integer.parseInt(idSplit[2]);
		if (currentLaby.isVerticalWall(x, z)) {
			currentLaby.removeVerticalWall(x, z);
			button.setStyle("-fx-background-color: #ffffffff; -fx-border-color: black; -fx-border-style: dashed");
		} else {
			currentLaby.addVerticalWall(x, z);
			button.setStyle("-fx-background-color: #3fa7c6ff; -fx-border-color: black; -fx-border-style: dashed");
		}
		this.isNotSaved();
	}

	@FXML
	private void addHorizontalWallClick(ActionEvent e) {
		if (currentLaby == null)
			return;
		Button button = ((Button) e.getSource());
		String[] idSplit = button.getId().split("_");
		int x = Integer.parseInt(idSplit[1]);
		int z = Integer.parseInt(idSplit[2]);
		if (currentLaby.isHorizontalWall(x, z)) {
			currentLaby.removeHorizontalWall(x, z);
			button.setStyle("-fx-background-color: #ffffffff; -fx-border-color: black; -fx-border-style: dashed");
		} else {
			currentLaby.addHorizontalWall(x, z);
			button.setStyle("-fx-background-color: #3fa7c6ff; -fx-border-color: black; -fx-border-style: dashed");
		}
		this.isNotSaved();
	}

	// L A B Y
	@FXML
	private void newLabyrinth(ActionEvent e) {
		TextInputDialog dialog = new TextInputDialog("New Labyrinth");
		dialog.setTitle("New labyrinth name");
		dialog.setHeaderText("New labyrinth name");
		dialog.setContentText("Please enter name:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			String fileName = result.get() + LabyrinthLoaderSaver.EXTENSION_LABYRINTH;
			for (Labyrinth laby : labyrinths)
				if (laby.getFileName().equals(fileName)) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Erreur de nommage");
					alert.setHeaderText("Le nom de fichier choisi n'est pas disponible");
					alert.setContentText(fileName);
					alert.showAndWait();
					return;
				}
			Labyrinth laby = new Labyrinth(fileName);
			labyrinths.add(laby);
			lv_lc_labys.getItems().add(fileName);
			lv_lc_labys.getSelectionModel().select(labyrinths.size() - 1);
			this.lc_labySelected();
		}
	}

	@FXML
	private void saveLabyrinth(ActionEvent e) {
		int index = lv_lc_labys.getSelectionModel().getSelectedIndex();
		if (index < 0 || index >= labyrinths.size())
			return;
		try {
			LabyrinthLoaderSaver.save(workstation, labyrinths.get(index));
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Sauvegarde");
			alert.setHeaderText("Sauvegarde correctement effectuée");
			alert.setContentText(labyrinths.get(index).getFileName());
			alert.showAndWait();
		} catch (IOException e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Impossible de sauvegarder le fichier");
			alert.setHeaderText("L'erreur suivante est survenue : ");
			alert.setContentText(e1.toString());
			alert.showAndWait();
		}
	}

	@FXML
	private void generateLabyrinth(ActionEvent e) {
		int index = lv_lc_labys.getSelectionModel().getSelectedIndex();
		if (index < 0 || index >= labyrinths.size())
			return;
		labyrinths.get(index).generate(workstation);
	}

	@FXML
	private void deleteLabyrinth(ActionEvent e) {
		int index = lv_lc_labys.getSelectionModel().getSelectedIndex();
		if (index < 0 || index >= labyrinths.size())
			return;
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Suppression");
		alert.setHeaderText("Êtes-vous sûr de vouloir supprimer le fichier selectionner ?");
		alert.setContentText(labyrinths.get(index).getFileName());
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			LabyrinthLoaderSaver.delete(workstation, labyrinths.get(index));
			labyrinths.remove(index);
			lv_lc_labys.getItems().remove(index);
			lv_lc_labys.getSelectionModel().clearSelection();
		}
	}

	@FXML
	private void addFloorToLaby(ActionEvent e) {
		int indexLaby = lv_lc_labys.getSelectionModel().getSelectedIndex();
		int indexFloor = lv_lc_floors.getSelectionModel().getSelectedIndex();
		if (indexLaby < 0 || indexLaby >= labyrinths.size())
			return;
		if (indexFloor < 0 || indexFloor >= floors.size())
			return;
		Labyrinth laby = labyrinths.get(indexLaby);
		Floor floor = floors.get(indexFloor);
		laby.getFloors().add(floor);
		lv_lc_selected.getItems().add(floor.getFileName());
	}
	
	@FXML
	private void removeFloorOnLabyrinth(ActionEvent e) {
		int indexLaby = lv_lc_labys.getSelectionModel().getSelectedIndex();
		int indexFloor = lv_lc_selected.getSelectionModel().getSelectedIndex();
		if (indexLaby < 0 || indexLaby >= labyrinths.size())
			return;
		Labyrinth laby = labyrinths.get(indexLaby);
		if (indexFloor < 0 || indexFloor >= laby.getFloors().size())
			return;
		laby.getFloors().remove(indexFloor);
		resetFloorLaby();
	}
	
	@FXML
	private void upFloor(ActionEvent e) {
		int indexLaby = lv_lc_labys.getSelectionModel().getSelectedIndex();
		int indexFloor = lv_lc_selected.getSelectionModel().getSelectedIndex();
		if (indexLaby < 0 || indexLaby >= labyrinths.size())
			return;
		Labyrinth laby = labyrinths.get(indexLaby);
		if (indexFloor < 0 || indexFloor >= laby.getFloors().size() - 1)
			return;
		Floor save = laby.getFloors().get(indexFloor);
		laby.getFloors().set(indexFloor, laby.getFloors().get(indexFloor + 1));
		laby.getFloors().set(indexFloor + 1, save);
		resetFloorLaby();
	}
	
	@FXML
	private void downFloor(ActionEvent e) {
		int indexLaby = lv_lc_labys.getSelectionModel().getSelectedIndex();
		int indexFloor = lv_lc_selected.getSelectionModel().getSelectedIndex();
		if (indexLaby < 0 || indexLaby >= labyrinths.size())
			return;
		Labyrinth laby = labyrinths.get(indexLaby);
		if (indexFloor <= 0 || indexFloor >= laby.getFloors().size())
			return;
		Floor save = laby.getFloors().get(indexFloor);
		laby.getFloors().set(indexFloor, laby.getFloors().get(indexFloor - 1));
		laby.getFloors().set(indexFloor - 1, save);
		resetFloorLaby();
	}
	
	@FXML
	private void lc_labySelected() {
		resetFloorLaby();
	}
	
	private void resetFloorLaby() {
		int indexLaby = lv_lc_labys.getSelectionModel().getSelectedIndex();
		if (indexLaby < 0 || indexLaby >= labyrinths.size())
			return;
		Labyrinth laby = labyrinths.get(indexLaby);
		lv_lc_selected.getItems().clear();
		for (Floor floor : laby.getFloors())
			lv_lc_selected.getItems().add(floor.getFileName());
	}

	@FXML
	private void lc_floorLabySelected() {

	}

	@FXML
	private void lc_floorSelected() {

	}
}
