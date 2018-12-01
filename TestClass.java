package msjfxuicomponents;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Function;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TestClass extends Application {

	public static void main(String[] args) {
		TestClass.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		String[] values = { "apple", "ball", "cat", "doll", "elephant", "fight", "georgeous", "height", "ice", "jug",
				"aplogize", "bank", "call", "done", "ego", "finger", "giant", "hollow", "internet", "jumbo", "kilo",
				"lion", "for", "length", "primary", "stage", "scene", "zoo", "jumble", "auto", "text", "root", "box",
				"items", "hip-hop", "himalaya", "nepal", "kathmandu", "kirtipur", "everest", "buddha", "epic",
				"hotel" };
		
		Entity[] entities = new Entity[values.length];
		
		for(int i = 0; i < entities.length; i++)
			entities[i] = new Entity(values[i]); 

		Function<String, Collection<Entity>> entitiesLoader = new Function<String, Collection<Entity>>() {
			@Override
			public Collection<Entity> apply(String t) {
				Collection<Entity> result = new LinkedList<>();
				
				for(String value: values)
					if(value.contains(t))
						result.add(new Entity(value));
				
				return result;
			}
		};

		AutoCompleteComboBox<Entity> autoCompleteComboBox = new AutoCompleteComboBox<Entity>();
		autoCompleteComboBox.setEntitiesLoader(entitiesLoader);
		
		BorderPane root = new BorderPane();
		root.setCenter(autoCompleteComboBox);
		Scene scene = new Scene(root);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static class Entity {
		public String value;

		public Entity(String value){
			this.setValue(value);
		}
		
		public String getValue() {
			return this.value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return this.value;
		}
	}
}
