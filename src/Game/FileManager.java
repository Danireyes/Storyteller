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
			
			if(node.getAttribute("id").equals(id))
			{
				ret = node.getTextContent();
			}
		}
		
		return ret;
	}
	
	public String getBlock(String id)
	{
		String ret = "";
		for(int i = 0; i < blocksList.getLength(); i++)
		{
			Element node = (Element)blocksList.item(i);
			
			if(node.getAttribute("id").equals(id))
			{	
				NodeList childsList = node.getChildNodes();
				
				for(int j = 0; j < childsList.getLength(); j++)
				{
					Node nod = childsList.item(j);
					
					if(nod.getNodeName().equals("descriptionText"))
					{
						ret += nod.getTextContent();
					}
					
					NodeList options;
					
					if(nod.getNodeName().equals("optionList"))
					{
						options = nod.getChildNodes();
						
						ret += "\n";
						for(int h = 0; h < options.getLength(); h++)
						{
							if(options.item(h).getNodeName().equals("option"))
							{
								Element el = (Element)options.item(h);
								ret += el.getAttribute("id") + ". " + options.item(h).getTextContent();
								ret += "\n";
							}
						}
					}
				}
			}
		}
		
		return ret;
	}
	
	String callOptionId(String blockId, String optionId) //Llama a la opcion y devuelve el proximo bloque (si no es un bloque printea la linked option) y devuelve su bloque
	{
		String ret = "";
		
		for(int i = 0; i < blocksList.getLength(); i++)
		{
			Element node = (Element)blocksList.item(i);
			
			if(node.getAttribute("id").equals(blockId))
			{	
				NodeList childsList = node.getChildNodes();
				
				for(int j = 0; j < childsList.getLength(); j++)
				{
					Node nod = childsList.item(j);					
					NodeList options;
					
					if(nod.getNodeName().equals("optionList"))
					{
						options = nod.getChildNodes();
						
						for(int h = 0; h < options.getLength(); h++)
						{
							if(options.item(h).getNodeName().equals("option"))
							{
								Element el = (Element)options.item(h);
								if(el.getAttribute("id").equals(optionId))
								{
									//Solo coge uno
									ret = el.getAttribute("gotoBlock");
									
									if(!el.getAttribute("gotoLinkedResult").equals(""))
									{
										for(int k = 0; k < childsList.getLength(); k++)
										{
											Node nod2 = childsList.item(k);					
											NodeList links;
											
											if(nod2.getNodeName().equals("linkedResultsList"))
											{
												links = nod2.getChildNodes();
												
												for(int l = 0; l < links.getLength(); l++)
												{
													if(links.item(l).getNodeName().equals("linkedResult"))
													{
														Element el1 = (Element)links.item(l);
														
														if(el.getAttribute("gotoLinkedResult").equals(el1.getAttribute("id")))
														{
															ret = el1.getAttribute("gotoBlock");
															
															System.out.println(el1.getTextContent());
														}
												}
											}
											
										}
									}
									
								}
							}
						}
					}
				}
			}
		}
		}
		
		return ret;
	}

}
