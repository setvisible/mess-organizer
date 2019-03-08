package com.github.setvisible.messorganizer.io;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.github.setvisible.messorganizer.core.Model;
import com.github.setvisible.messorganizer.core.SoftwareListWrapper;

public class Parser {

	/**
	 * Loads model data from the specified file. The current data will be replaced.
	 * 
	 * @param file
	 * @param model
	 * @throws JAXBException
	 */
	public static void loadDataFromFile(File file, Model model) throws JAXBException {

		JAXBContext context = JAXBContext.newInstance(SoftwareListWrapper.class);
		Unmarshaller um = context.createUnmarshaller();

		// Reading XML from the file and unmarshalling.
		SoftwareListWrapper wrapper = (SoftwareListWrapper) um.unmarshal(file);

		model.getSoftwareData().clear();
		model.getSoftwareData().addAll(wrapper.getSoftwares());
	}

	/**
	 * Saves the current model data to the specified file.
	 * 
	 * @param file
	 * @param model
	 * @throws JAXBException
	 */
	public static void saveDataToFile(File file, Model model) throws JAXBException {

		JAXBContext context = JAXBContext.newInstance(SoftwareListWrapper.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		// Wrapping our software data.
		SoftwareListWrapper wrapper = new SoftwareListWrapper();
		wrapper.setSoftwares(model.getSoftwareData());

		// Marshalling and saving XML to the file.
		m.marshal(wrapper, file);
	}
}
