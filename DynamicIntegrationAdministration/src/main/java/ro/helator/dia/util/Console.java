package ro.helator.dia.util;

import java.io.IOException;
import java.io.OutputStream;
import javafx.scene.control.TextArea;

public class Console extends OutputStream {

	private static Console _instance;
	
	private final StringBuffer buffer = new StringBuffer();
	private TextArea output;
	
	private Console(){
		
	}
	
	public static Console getInstance(){
		if(_instance == null){
			_instance = new Console();
		}
		return _instance;
	}
	
	
	@Override
	public void write(int b) throws IOException {
		String value = String.valueOf((char)b);
		if(output == null){
			buffer.append(value);
		} else {
			if(buffer.length() > 0){
				output.appendText(buffer.toString());
				buffer.delete(0, buffer.length());
			}
			output.appendText(value);
		}
		
	}
	

	public void setOutput(TextArea output){
		this.output = output;
		if(buffer.length() > 0){
			this.output.appendText(buffer.toString());
			buffer.delete(0, buffer.length());
		}
	}
}
