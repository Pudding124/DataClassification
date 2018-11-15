package controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import databeans.FlickrBean;
import formbeans.flickrsearchform;
import model.Model;

public class SearchFlickrAction extends Action {
	private FormBeanFactory<flickrsearchform> formBeanFactory = FormBeanFactory
			.getInstance(flickrsearchform.class);
	public SearchFlickrAction(Model model) {

	}
	public String[] getComments(String ids) throws MalformedURLException,
			IOException {
		URLConnection uc = new URL(
				"https://api.flickr.com/services/rest/?method=flickr.photos.comments.getList&api_key=f3e75ee9d97069d826d1225ef5190730&photo_id="
						+ ids).openConnection();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom = null;
		try {

			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			dom = db.parse(uc.getInputStream());

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		Element docEle = dom.getDocumentElement();

		// get a nodelist of

		NodeList nl = docEle.getElementsByTagName("comment");
		String comments[] = new String[nl.getLength()];
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {

				// get the employee element
				Element el = (Element) nl.item(i);

				// get the Employee object
				String comment = el.getTextContent();
				String author = el.getAttribute("authorname");
				comments[i] = author + ":" + comment;

			}
		}
		return comments;
	}

	// /////////////////////////////////
	public String getDescription(String ids) throws MalformedURLException,
			IOException {
		URLConnection uc = new URL(
				"https://api.flickr.com/services/rest/?method=flickr.photos.getInfo&api_key=f3e75ee9d97069d826d1225ef5190730&photo_id="
						+ ids).openConnection();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom = null;
		try {

			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			dom = db.parse(uc.getInputStream());

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		Element docEle = dom.getDocumentElement();

		// get a nodelist of

		NodeList nl = docEle.getElementsByTagName("description");
		String description = null;
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {

				// get the employee element
				Element el = (Element) nl.item(i);

				// get the Employee object
				String temp = el.getTextContent();

				description = temp;
				// add it to list

			}
		}
		return description;
	}

	public String getTitle(String ids) throws MalformedURLException,
			IOException {
		URLConnection uc = new URL(
				"https://api.flickr.com/services/rest/?method=flickr.photos.getInfo&api_key=f3e75ee9d97069d826d1225ef5190730&photo_id="
						+ ids).openConnection();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom = null;
		try {

			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			dom = db.parse(uc.getInputStream());

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("title");
		String title = null;
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				// get the employee element
				Element el = (Element) nl.item(i);
				// get the Employee object
				String temp = el.getTextContent();
				title = temp;
			}
		}
		return title;
	}

	// /////////////////////////////////
	public List<String> getTags() {
		URLConnection uc = null;
		System.out.println("In Method");
		try {
			uc = new URL(
					"https://api.flickr.com/services/rest/?method=flickr.tags.getListUser&api_key=f3e75ee9d97069d826d1225ef5190730&user_id=131367443@N02")
					.openConnection();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom = null;
		try {

			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			dom = db.parse(uc.getInputStream());

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		Element docEle = dom.getDocumentElement();
		Map<Integer, String> randomtags5 = new HashMap<Integer, String>();

		// get a nodelist of
		NodeList nl = docEle.getElementsByTagName("tag");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				String temp = el.getTextContent();
				randomtags5.put(i, temp);

			}
		}

		Random rn = new Random();
		List<String> randomtags = new ArrayList<String>();
		for (int j = 0; j < 5; j++) {
			int answer = rn.nextInt(randomtags5.size()) + 1;
			randomtags.add(randomtags5.get(answer));
			System.out.println(randomtags5.get(answer));
		}

		return randomtags;

	}

	@Override
	public String getName() {
		return "searchFlickr.do";
	}

	@Override
	public String perform(HttpServletRequest request) {
		System.out.println("searchFlickr action called");

		URLConnection uc;
		try {
			String urlList[] = new String[30];
			flickrsearchform form = formBeanFactory.create(request);
			request.setAttribute("form", form);

			String key = form.getSearchKey();
			if (key == null)
				key = "";
			System.out.println("key=" + key);
			uc = new URL(
					"https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=f3e75ee9d97069d826d1225ef5190730&per_page=30&user_id=131367443@N02&tags="
							+ key).openConnection();

			String nextline;
			String[] servers = new String[30];
			String[] ids = new String[30];
			String[] secrets = new String[30];

			XMLInputFactory factory = XMLInputFactory.newInstance();

			try {
				ArrayList<FlickrBean> flickr = new ArrayList<FlickrBean>();
				// FlickrBean[] flickr = new FlickrBean[30];
				XMLEventReader r = factory.createXMLEventReader(uc
						.getInputStream());
				int i = -1;
				while (r.hasNext()) {

					XMLEvent event = r.nextEvent();
					if (event.isStartElement()) {
						StartElement element = (StartElement) event;
						String elementName = element.getName().toString();
						if (elementName.equals("photo")) {
							i++;
							Iterator iterator = element.getAttributes();

							while (iterator.hasNext()) {

								Attribute attribute = (Attribute) iterator
										.next();
								QName name = attribute.getName();
								String value = attribute.getValue();
								if ((name.toString()).equals("server")) {
									servers[i] = value;

								}
								if ((name.toString()).equals("id")) {
									ids[i] = value;
								}
								if ((name.toString()).equals("secret")) {
									secrets[i] = value;
								}
							}
							// Printing url
							String flickrurl = "http://static.flickr.com/"
									+ servers[i] + "/" + ids[i] + "_"
									+ secrets[i] + ".jpg";
							String uri = flickrurl;
							FlickrBean temp = new FlickrBean();
							temp.setId(ids[i]);
							temp.setUrl(uri);
							// temp.setComment(getComments(ids[i]));
							// temp.setTitle(getTitle(ids[i]));
							// temp.setDescription(getDescription(ids[i]));
							temp.setComment(null);
							temp.setTitle("");
							temp.setDescription("");
							flickr.add(temp);

						}
						// End of Printing

					}
				}
				request.setAttribute("flickr", flickr);
				request.setAttribute("tags", getTags());

			} catch (XMLStreamException e) {

				e.printStackTrace();
			}

		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (FormBeanException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "showFlickr.jsp";

	}

}