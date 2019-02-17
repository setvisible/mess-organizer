package com.github.setvisible.messorganizer.core;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to wrap a list of softwares. This is used for saving the list of
 * softwares to XML.
 * 
 */
@XmlRootElement(name = "softwares")
public class SoftwareListWrapper {

	private List<Software> softwares;

	@XmlElement(name = "softwares")
	public List<Software> getSoftwares() {
		return softwares;
	}

	public void setSoftwares(List<Software> softwares) {
		this.softwares = softwares;
	}
}
