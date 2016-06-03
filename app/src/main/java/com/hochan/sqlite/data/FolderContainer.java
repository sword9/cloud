package com.hochan.sqlite.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

public class FolderContainer {

	@JsonProperty("directory")
	private List<String> directory;
	@JsonProperty("file")
	private List<String> file;
	public FolderContainer() {
		directory = new LinkedList<>();
		file = new LinkedList<>();
	}
	public List<String> getDirectory(){
		return directory;
	}
	public List<String> getFile(){
		return file;
	}
	public void setDirectory(List<String> d){
		directory = d;
	}
	public void setFile(List<String> f){
		file = f;
	}
}
