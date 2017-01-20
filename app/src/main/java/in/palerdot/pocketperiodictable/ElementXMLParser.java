package in.palerdot.pocketperiodictable;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// extend the application object to store data 'globally' throughout the application

/**
 * Created by arun on 5/7/15.
 *
 * parses the Chemical Elements details from the res/xml/elements.xml into a Map (associative array)
 *
 * Structure:
 *
 * each element is enclosed in an element tag
 * each element has an id attribute which contains element's symbol (which is also used as tag in view)
 * each element has sections - general, discovery, atomic-properties etc
 * all the element properties are listed within the section
 *
 * Parser extends Application to store data and make it available throughout Application lifecycle
 */
public class ElementXMLParser extends Application {

    // singleton instance
    private static ElementXMLParser instance;

    // returns the singleton instance
    public static ElementXMLParser getInstance() {
        return instance;
    }

    // this will hold the final value
    private Map<String, Map<String, ArrayList<HashMap<String, String>>>> elementInfo = new HashMap<>();

    // get elements info through this method
    public Map<String, Map<String, ArrayList<HashMap<String, String>>>> getElementsData() {
        return elementInfo;
    }

    // flag to indicate if we are entering new element tag
    private Boolean isNewElementParsed = false;
    // flag to indicate we are entering new element's id tag
    private Boolean isIdElementParsed = false;

    // holds the current element, current section and current property being parsed in the XML
    private String currentElement = new String();
    private String currentSection = new String();
    private String currentProperty = new String();

    // when application is created parse the data from the xml file and save it
    public void onCreate() {
        super.onCreate();
        // create and assign an instance
        instance = this;
        // parse the data from the xml file; it will be saved in elementInfo
        try {
            parse( getApplicationContext() );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    private void parse (Context context) throws IOException, XmlPullParserException {
        // get the resource
        Resources res = context.getResources();
        // getting the elements xml file handle
        XmlResourceParser xpp = res.getXml(R.xml.elements);
        // StringBuffer sb = new StringBuffer();

        xpp.next();
        int eventType = xpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            // handle the XML by parsing the necessary tags into the final structure
            handleXML(xpp, eventType);
            // go to next tag
            eventType = xpp.next();
        }//eof-while

    }

    // handles the parsing XML; creates new elements from the XML fle
    private void handleXML(XmlResourceParser parser, int eventType) {

        String tagName = new String();

        switch (eventType) {

            // an XML start tag
            case XmlPullParser.START_TAG:

                tagName = parser.getName();

                switch (tagName) {
                    case "element":
                        // new element tag is encountered
                        isNewElementParsed = true;
                        break;

                    case "id":
                        // new element's tag id is encountered
                        isIdElementParsed = true;
                        break;

                    // if the tagName is main sections like, general, discovery, atomic-properties etc
                    // update it as current section
                    // CAUTION: Manually enter tne case statement here for each major section or else there will be BUGS!!
                    case "general":
                    case "history":
                    case "physical-properties":
                    case "atomic-properties":
                    case "miscellaneous":
                    case "meta":
                    case "links":
                        // create a new section for the element
                        // elementInfo.get(currentElement).put(tagName, new HashMap<String, String>());
                        elementInfo.get(currentElement).put(tagName, new ArrayList<HashMap<String, String>>());

                        // update the current section
                        currentSection = tagName;
                        break;

                    default:
                        // At this point we will getting individual properties like name, symbol etc for various section
                        // store the property tag to insert things rightly

                        // save the current property
                        currentProperty = tagName;
                        break;

                }

                break;

            // text value of the XML tag
            case XmlPullParser.TEXT:

                // as a text only two things are got - element's id & different properties of sections of an element

                // if new element's id tag's text is encountered, a new element needs to be created
                if (isNewElementParsed && isIdElementParsed) {
                    String elementTag = parser.getText();
                    // create a new entry for the element in the map
                    // elementInfo.put(elementTag, new HashMap<String, Map<String, String>>());
                    elementInfo.put(elementTag, new HashMap<String, ArrayList<HashMap<String, String>>>());
                    // update the current element
                    currentElement = elementTag;
                }
                // else it is some property of some section of an element
                // just update it
                else {
                    // create a new property in the current section of the current element
                    // elementInfo.get(currentElement).get(currentSection).put(currentProperty, parser.getText().toString());
                    HashMap<String, String> property = new HashMap<>();
                    property.put(currentProperty, parser.getText().toString());
                    elementInfo.get(currentElement).get(currentSection).add( property );
                }
                break;

            // an XML end tag
            case XmlPullParser.END_TAG:

                tagName = parser.getName();

                switch (tagName) {
                    case "element":
                        // new element closing tag is encountered
                        isNewElementParsed = false;
                        break;

                    case "id":
                        // new element's id closing tag is encountered
                        isIdElementParsed = false;
                        break;
                }

                break;

        }

    }

}
