package com.example.nhat.myapplication;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class TaskUtil {
    List<DataObject> data;
    private DataObject dato;
    private String text;

    public TaskUtil() {
        data = new ArrayList<DataObject>();
    }

    public List<DataObject> getEmployees() {
        return data;
    }

    public List<DataObject> parse(InputStream is) {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            parser.setInput(is, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("DataObject")) {
                            // create a new instance of employee
                            dato = new DataObject();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("DataObject")) {
                            // add employee object to list
                            data.add(dato);
                        } else if (tagname.equalsIgnoreCase("action")) {
                            dato.setTheAction(text);
                        } else if (tagname.equalsIgnoreCase("pattern")) {
                            dato.setThePartern(text);
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
