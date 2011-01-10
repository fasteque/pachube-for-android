package com.fasteque.pachubewidget;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.util.Log;

public class RestClient
{
    public static ParsedFeed connect(String url, String pachubeKey)
    {
    	ParsedFeed feed = null;
    	
        HttpClient httpclient = new DefaultHttpClient();
 
        // Prepare a request object
        HttpGet httpget = new HttpGet(url + "?key=" + pachubeKey);
        //HttpGet httpget = new HttpGet(url+"?key=5c33ac9b89dbc95146217faeedc0e47a50f3bfda897ec7e245c74d6ef8ec124a"); 
 
        // Execute the request
        HttpResponse response;
        
        try
        {
            response = httpclient.execute(httpget);
            
            // Examine the response status
            Log.i("PW", response.getStatusLine().toString());
            
            HttpEntity entity = response.getEntity();
            
            if (entity != null)
            {
            	
                InputStream instream = entity.getContent();
                
                //String result = convertStreamToString(instream);
                //Log.i("PW", result);
                
	            SAXParserFactory spf = SAXParserFactory.newInstance();
	            SAXParser sp;
			
	            /* Get the XMLReader of the SAXParser we created. */
	            try
	            {
	            	sp = spf.newSAXParser();
					XMLReader xr = sp.getXMLReader();
					
					// Create a new ContentHandler and apply it to the XMLReader.
                    FeedDataHandler feedHandler = new FeedDataHandler();
                    xr.setContentHandler(feedHandler);
					
					InputSource is = new InputSource(instream);
					xr.parse(is);
					
					feed = feedHandler.getParsedFeed();
				}
	            catch (SAXException e)
	            {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (ParserConfigurationException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
		return feed;
    }

/*
    private static String convertStreamToString(InputStream is) {
        
         // To convert the InputStream to String we use the BufferedReader.readLine() method.
         
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
*/

}
