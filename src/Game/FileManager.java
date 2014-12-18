package Game;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileManager {
	
	private final int NUMSTORIES_MAX = 10;
	private String adventuresArray[][];
	
	private Document mXMLDocument;
	private NodeList displayersList, blocksList;
	
	public FileManager() throws IOException
	{
		Path filePath = Paths.get("Adventures_list.txt");
		Scanner scanner = new Scanner(filePath);
		
		adventuresArray = new String[NUMSTORIES_MAX][2];
		int i = 0;
		while(scanner.hasNext())
		{
			String aux = null;
			aux = scanner.next();
			adventuresArray[i][0] = aux;
			aux = scanner.next();
			adventuresArray[i][1] = aux;
			
			i++;
		}
		
		scanner.close();
		
		openXMLFile(adventuresArray[0][1]);
	}
	
	public void openXMLFile(String fileName)
	{
		try
		{
			//Cargar el archivo
		File XMLFile = new File("./DesertIsland.xml");
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		mXMLDocument = dBuilder.parse(XMLFile);
		
		mXMLDocument.getDocumentElement().normalize();
		
		displayersList = mXMLDocument.getDocumentElement().getElementsByTagName("displayer");
		blocksList = mXMLDocument.getDocumentElement().getElementsByTagName("block");
		
		System.out.println(getDisplayerText("introduction"));
		
	 } catch (Exception e) {
			e.printStackTrace();
		    }
	}
	
	public String[][] getAdventuresListArray()
	{
		return adventuresArray;
	}
	
	public String getDisplayerText(String id)
	{
		String ret = "NOT_FOUND";
		for(int i = 0; i < displayersList.getLength(); i++)
		{
			Element node = (Element)displayersList.item(i);
			
			if(node.getAttribute("id") == id)
			{
				ret = node.getTextContent();
			}
		}
		
		return ret;
	}

}
