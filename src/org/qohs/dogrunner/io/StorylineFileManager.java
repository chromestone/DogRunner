package org.qohs.dogrunner.io;

import java.util.HashMap;
import java.util.LinkedList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * 
 * @author Derek Zhang
 *
 */
public class StorylineFileManager implements FileManager {

	public HashMap<Integer, LinkedList<String>> scoreToStoryline = null;

	@Override
	public void load() {
		
		scoreToStoryline = new HashMap<>();
		try {	
			FileHandle inputFile = Gdx.files.local(FileManager.PARENT_DIR + "storyline.xml");
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			MyHandler userhandler = new MyHandler();
			saxParser.parse(inputFile.file(), userhandler);     
		}
		catch (Exception e) {
			
			Gdx.app.log("DogRunner-chromestone", e.getMessage());
		}
	}

	@Override
	public void save() {
	}

	private class MyHandler extends DefaultHandler {
		
		private ElementType elementType = ElementType.UNDEFINED;
		
		private Integer score = null;
		private LinkedList<String> dialogs = null;
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
						throws SAXException {
			if (qName.equals("chapter")) {
				
				elementType = ElementType.CHAPTER;
				
				score = new Integer(attributes.getValue("score"));
				dialogs = new LinkedList<String>();
			}
			else if (qName.equals("d")) {
				
				elementType = ElementType.DIALOG;
			}
			else {

				elementType = ElementType.UNDEFINED;
			}
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			
			if (qName.equals("chapter")) {
				
				StorylineFileManager.this.scoreToStoryline.put(score, dialogs);
			}
		}

		@Override
		public void characters(char ch[], int start, int length) throws SAXException {

			switch (elementType) {
			
			case DIALOG: {
				
				String dialog = new String(ch, start, length).trim();
				if (dialog.length() > 0) {
					
					dialogs.add(new String(ch, start, length));
				}
				
				break;
			}
			default: {
				
				break;
			}
			}
		}
	}
	
	private enum ElementType {
			
		CHAPTER, DIALOG, UNDEFINED;
	}
}
