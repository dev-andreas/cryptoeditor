package com.andreas.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Utils {
    public static String bytesToHex(byte[] byteArray) {
		String string = "";

		for (int i = 0; i < byteArray.length; i++) {
			string += "" + String.format("%02x", byteArray[i] + 128);
		}

		return string;
	}

	public static byte[] hexToBytes(String string) {

		String[] stringArray = new String[string.length() / 2];

		for (int i = 0; i < stringArray.length; i++) {
			stringArray[i] = string.substring(i*2, i*2+2);
		}

		byte[] byteArray = new byte[stringArray.length];

		for (int i = 0; i < stringArray.length; i++) {

			int b = Integer.decode("0x"+stringArray[i]) - 128;
			byteArray[i] = (byte) b;
		}

		return byteArray;
    }
    
    public static boolean createXmlFile(String path, Element root) {

        try {

            Document doc = new Document(root);
            XMLOutputter xmlOutput = new XMLOutputter();
            FileWriter fileWriter = new FileWriter(path);

            // display nice nice
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, fileWriter);
            
            fileWriter.close();

            return true;

        } catch (IOException io) {
            io.printStackTrace();
        }
        
        return false;
    }

    public static Element readXmlFile(String path) {

        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(path);

        if (!xmlFile.exists()) {
            return null;
        }

        try {
            Document document = (Document) builder.build(xmlFile);
            return document.getRootElement();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        return null;
    }
}