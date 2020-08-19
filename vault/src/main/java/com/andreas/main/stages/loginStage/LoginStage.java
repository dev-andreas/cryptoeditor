package com.andreas.main.stages.loginStage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.andreas.main.App;
import com.andreas.main.app.AppStage;
import com.andreas.main.save.Save;
import com.andreas.main.save.SaveInformation;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class LoginStage extends AppStage {

    private ArrayList<SaveInformation> saves;
    private int nextId;

    public LoginStage(App app) {
        super(app, "stages/loginStage/login.fxml");
        setTitle("Login");

        loadSaves();

        init();
    }

    public void addSave(Save save) {
        save.setId(nextId);
        ((LoginController) getController()).savesList.getItems().add(save.getName());
        saves.add(new SaveInformation(nextId, save.getName()));
        save.writeToFile("data/saves/" + nextId++ + ".xml");
        updateSaves();
    }

    public void renameSave(Save save, int index, String name) {
        ((LoginController) getController()).savesList.getItems().set(index, name);
        saves.get(index).setName(name);
        updateSaves();
    }

    public void removeSave(int index) {

        File file = new File("data/saves/"+ saves.get(index).getId() +".xml");
          
        if(!file.delete()) {
            System.out.println("Could not delete the file!");
        }

        ((LoginController) getController()).savesList.getItems().remove(index);

        saves.remove(index);
        updateSaves();
    }

    public ArrayList<SaveInformation> getSaves() {
        return saves;
    }

    public void loadSaves() {
        saves = new ArrayList<>();

        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File("data/saves.xml");

        try {

            Document document = (Document) builder.build(xmlFile);
            Element rootNode = document.getRootElement();

            nextId = Integer.parseInt(rootNode.getChild("nextId").getText());

            ((LoginController) getController()).keyPath.setText(rootNode.getChildText("keyPath"));

            for (Element e : rootNode.getChildren("save")) {
                String name = e.getChildText("name");
                int id = Integer.parseInt(e.getChildText("id"));

                saves.add(new SaveInformation(id, name));
                ((LoginController) getController()).savesList.getItems().add(name);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
    }

    public void updateSaves() {
        try {

            Element root = new Element("saves");
            Document doc = new Document(root);
			
            Element nextId = new Element("nextId").setText("" + this.nextId);
            root.addContent(nextId);

            Element keyPath = new Element("keyPath").setText(((LoginController)getController()).keyPath.getText());
            root.addContent(keyPath);

            for (SaveInformation s : saves) {
                Element save = new Element("save");

                save.addContent(new Element("id"));
                save.addContent(new Element("name"));

                save.getChild("id").setText("" + s.getId());
                save.getChild("name").setText(s.getName());

                root.addContent(save);
            }

            XMLOutputter xmlOutput = new XMLOutputter();
            FileWriter fileWriter = new FileWriter("data/saves.xml");

            // display nice nice
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, fileWriter);

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}