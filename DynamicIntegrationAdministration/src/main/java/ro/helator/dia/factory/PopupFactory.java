package ro.helator.dia.factory;

import static ro.helator.dia.util.Constants.SERRIAL_UUID;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class PopupFactory {

	private static <R> Dialog<R> newEmptyDialog(R obj, String title) {
		Dialog<R> dialog = new Dialog<R>();
		dialog.setTitle(title);
		return dialog;
	}

	private static <R> Dialog<R> newOKCancelDialog(R obj, String title) {
		Dialog<R> dialog = newEmptyDialog(obj, title);
		ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
		ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(ok, cancel);
		return dialog;
	}

	public static <R> Dialog<R> newOKCancelFormDialog(R obj, String title) {
		Dialog<R> dialog = newOKCancelDialog(obj, title);
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20, 150, 10, 10));

		if(obj instanceof Properties){
			Enumeration<String> keys = (Enumeration<String>) ((Properties) obj).propertyNames();
			int i = 0;
			while(keys.hasMoreElements()){
				String key = keys.nextElement();
				Label label = new Label(key);
				TextField textField = new TextField();
				textField.setPromptText(key);
				textField.setId(key.replace(".", "-"));
				Object val = ((Properties) obj).get(key);
				String value = "";
				if (val != null) {
					if (val instanceof String) {
						value = (String) val;
					} else if (val instanceof Integer) {
						value = ((Integer) val).toString();
					} else if (val instanceof Double) {
						value = ((Double) val).toString();
					} else if (val instanceof Long) {
						value = ((Long) val).toString();
					} else if (val instanceof Boolean) {
						value = ((Boolean) val).toString();
					}
				}
				textField.setText(value);
				gridPane.add(label, 0, i);
				gridPane.add(textField, 1, i++);
			}
		} else {
			Field[] fields = obj.getClass().getDeclaredFields();
			int i = 0;
			for (Field f : fields) {
				String name = f.getName();
				if (!name.equals(SERRIAL_UUID)) {
					Label label = new Label(name);
					TextField textField = new TextField();
					textField.setPromptText(name);
					textField.setId(name);
					Object val = null;
					try {
						Method m = obj.getClass().getMethod(toLowerCamelCase("get", name));
						if(m != null){
							val = m.invoke(obj, new Object[]{});
						}
					} catch (Exception e) {
					}
					String value = "";
					if (val != null) {
						if (val instanceof String) {
							value = (String) val;
						} else if (val instanceof Integer) {
							value = ((Integer) val).toString();
						} else if (val instanceof Double) {
							value = ((Double) val).toString();
						} else if (val instanceof Long) {
							value = ((Long) val).toString();
						} else if (val instanceof Boolean) {
							value = ((Boolean) val).toString();
						}
					}
					textField.setText(value);
					gridPane.add(label, 0, i);
					gridPane.add(textField, 1, i++);
				}
			}	
		}
		dialog.getDialogPane().setContent(gridPane);
		dialog.setResultConverter(new Callback<ButtonType, R>() {
			@Override
			public R call(ButtonType b) {
				if (b.getButtonData() == ButtonData.OK_DONE) {
					try {
						if(obj instanceof Properties){
							Enumeration<String> keys = (Enumeration<String>) ((Properties) obj).propertyNames();
							while(keys.hasMoreElements()){
								String key = keys.nextElement();
								TextField tf = (TextField) gridPane.getScene().lookup("#" + key.replace(".", "-"));
								((Properties) obj).setProperty(key, tf.getText().trim());
							}
						} else {
						Field[] fields = obj.getClass().getDeclaredFields();
							for (Field f : fields) {
								String name = f.getName();
								if (!name.equals(SERRIAL_UUID)) {
									TextField tf = (TextField) gridPane.getScene().lookup("#" + name);
									String value = tf.getText().trim();
									Method method = obj.getClass().getMethod(toLowerCamelCase("set", name),
											String.class);
									method.invoke(obj, value);
								}
							}
						}
						return obj;
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				return null;
			}
		});

		return dialog;
	}

	private static String toLowerCamelCase(String... strings) {
		StringBuilder sb = new StringBuilder();
		for (String s : strings) {
			if (sb.length() > 0) {
				int size = s.length();
				if (size > 0) {
					sb.append(s.substring(0, 1).toUpperCase());
					if (size > 1) {
						sb.append(s.substring(1));
					}
				}
			} else {
				sb.append(s);
			}
		}
		return sb.toString();
	}
}
