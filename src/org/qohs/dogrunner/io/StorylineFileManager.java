package org.qohs.dogrunner.io;

import java.util.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.qohs.dogrunner.DogRunner;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * 
 * SFM
 * 
 * @author Derek Zhang
 *
 */
public class StorylineFileManager implements FileManager {

	public LinkedList<DialogListRead> storylineDialogList = null;
	/**
	 * Technically, this is the next read position or the chapter
	 * index starting from zero that should next be displayed
	 */
	private int readPosition = -1;
	private int unlockedPosition;
	
	public int getReadPosition() {
		
		return readPosition;
	}
	
	public int getUnlockedPosition() {
		
		return unlockedPosition;
	}
	
	public void setReadPosition(int position) {
		
		readPosition = position;
		if (readPosition > unlockedPosition) {
			
			unlockedPosition = readPosition;
		}
	}

	@Override
	public void load() {
		
		storylineDialogList = new LinkedList<DialogListRead>();
		
		try {	
			
			FileHandle inputFile = Gdx.files.internal(DogRunner.PARENT_DIR + "storyline/storyline.xml");
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			MyHandler userhandler = new MyHandler();
			saxParser.parse(inputFile.file(), userhandler);
			
			/*
			InputStream inputStream = readFile.read();
			
			Iterator<DialogListRead> iterator = storylineDialogList.iterator();
			
			int num;
			while ((num = inputStream.read()) != -1 && iterator.hasNext()) {
				
				DialogListRead dLR = iterator.next();
				
				dLR.read = (num != 0);//System.out.println(dLR.dialogs);
			}
			
			inputStream.close();*/
		}
		catch (Exception e) {
			
			Gdx.app.log("DogRunner-chromestone-SFM", e.getMessage());
		}
		
		FileHandle readFile = Gdx.files.local(FileManager.PARENT_DIR + "storyline/read_story.txt");
		if (!readFile.exists()) {

			readPosition = 0;
			return;
		}
		
		try {

			readPosition = Integer.parseInt(readFile.readString());
		}
		catch (Exception e) {

			Gdx.app.log("DogRunner-chromestone", e.getMessage());
			readPosition = 0;
		}
	}

	@Override
	public void save() {
		
		/*
		FileHandle readFile = Gdx.files.local(FileManager.PARENT_DIR + "storyline/read_story.txt");
		OutputStream oS = readFile.write(false);
		try {
			
			for (DialogListRead dLR : storylineDialogList) {
				
				oS.write(dLR.read ? (byte) 1 : (byte) 0);
			}
		}
		*/
		try {
			
			FileHandle readFile = Gdx.files.local(FileManager.PARENT_DIR + "storyline/read_story.txt");
			readFile.writeString("" + readPosition, false);
		}
		catch (Exception e) {
			
			Gdx.app.log("DogRunner-chromestone-SFM", e.getMessage());
		}
	}
	
	public static class DialogListRead {
		
		/**
		 * This should hold a dialog image. The dialog image's image
		 * field guides the story line on when to switch pictures.
		 */
		public final LinkedList<DialogImage> dialogs;
		/**
		 * This is a reference list to all the non null values in dialogs.
		 */
		public final LinkedList<String> images;
		//public boolean read;
		
		/*
		private DialogListRead(LinkedList<DialogImage> dialogs, LinkedList<String> images) {
			
			this(dialogs, images, false);
		}
		*/
		
		private DialogListRead(LinkedList<DialogImage> dialogs, LinkedList<String> images) {//, boolean read) {
			
			this.dialogs = dialogs;
			this.images = images;
			//this.read = read;
		}
		/*
		@Override
		public String toString() {
			
			return dialogs.toString();
		}*/
	}
	
	public static class DialogImage {
		
		public final String dialog;
		public final String image;
		
		private DialogImage(String dialog, String image) {
			
			this.dialog = dialog;
			this.image = image;
		}
		/*
		@Override
		public String toString() {
			
			return dialog + "\n";
		}*/
	}

	private class MyHandler extends DefaultHandler {
		
		private ElementType elementType = ElementType.UNDEFINED;
		private ElementType innerElementType = ElementType.UNDEFINED;
		
		//private Integer score = null;
		private LinkedList<DialogImage> dialogs = null;
		private LinkedList<String> images = null;
		private String image = null;
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
						throws SAXException {
			
			switch (elementType) {
			case UNDEFINED: {

				if (qName.equals("chapter")) {

					elementType = ElementType.CHAPTER;

					//score = new Integer(attributes.getValue("score"));
					dialogs = new LinkedList<DialogImage>();
					images = new LinkedList<String>();
				}
				else {

					elementType = ElementType.UNDEFINED;
				}
				
				innerElementType = ElementType.UNDEFINED;
				
				break;
			}
			case CHAPTER: {

				if (qName.equals("d")) {

					innerElementType = ElementType.DIALOG;

					image = attributes.getValue("img");
					if (image != null) {
						
						image = DogRunner.PARENT_DIR + "storyline/" + image;
					}
				}
				else {
					
					innerElementType = ElementType.UNDEFINED;
				}
				
				break;
			}
			default: {
				
				elementType = ElementType.UNDEFINED;
				innerElementType = ElementType.UNDEFINED;
				
				break;
			}
			}
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			
			switch (elementType) {
			
			case CHAPTER: {

				if (qName.equals("chapter")) {

					//StorylineFileManager.this.scoreToStoryline.put(score, dialogs);
					StorylineFileManager.this.storylineDialogList.add(new DialogListRead(dialogs, images));
					elementType = ElementType.UNDEFINED;
				}
					
				innerElementType = ElementType.UNDEFINED;
				
				break;
			}
			default: {
				
				elementType = ElementType.UNDEFINED;
				innerElementType = ElementType.UNDEFINED;
				
				break;
			}
			}
		}

		@Override
		public void characters(char ch[], int start, int length) throws SAXException {

			switch (elementType) {
			
			case CHAPTER: {
				
				if (innerElementType == ElementType.DIALOG) {
					
					String dialog = new String(ch, start, length).trim();
					if (dialog.length() > 0) {

						dialogs.add(new DialogImage(new String(ch, start, length), image));
						if (image != null) {

							images.add(image);
						}
					}
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
