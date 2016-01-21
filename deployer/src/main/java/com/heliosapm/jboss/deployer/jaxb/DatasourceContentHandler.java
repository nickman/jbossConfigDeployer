/**
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
 */
package com.heliosapm.jboss.deployer.jaxb;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * <p>Title: DatasourceContentHandler</p>
 * <p>Description: </p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.jboss.deployer.jaxb.DatasourceContentHandler</code></p>
 */

public class DatasourceContentHandler implements ContentHandler {
	
	private ContentHandler handler;
	
	/**
	 * Creates a new DatasourceContentHandler
	 */
	public DatasourceContentHandler(final ContentHandler ch) {
		handler = ch;
	}

	/**
	 * @param locator
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	public void setDocumentLocator(Locator locator) {
		handler.setDocumentLocator(locator);
	}

	/**
	 * @throws SAXException
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	public void startDocument() throws SAXException {
		handler.startDocument();
	}

	/**
	 * @throws SAXException
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	public void endDocument() throws SAXException {
		handler.endDocument();
	}

	/**
	 * @param prefix
	 * @param uri
	 * @throws SAXException
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
	 */
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		handler.startPrefixMapping(prefix, uri);
	}

	/**
	 * @param prefix
	 * @throws SAXException
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	public void endPrefixMapping(String prefix) throws SAXException {
		handler.endPrefixMapping(prefix);
	}

	/**
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param atts
	 * @throws SAXException
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		if("local-tx-datasource".equals(qName)) {
			qName = "datasource";			
		} else if("connection-factories".equals(qName)) {
			qName = "datasources";
		} else if("tx-connection-factory".equals(qName)) {
			qName = "datasource";
		}
		if (!qName.equals(localName)) {
			handler.startElement("", localName, qName, atts);
		} else {
			handler.startElement(uri, localName, qName, atts);
		}
//		handler.startElement(uri, localName, qName, atts);
	}

	/**
	 * @param uri
	 * @param localName
	 * @param qName
	 * @throws SAXException
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		handler.endElement(uri, localName, qName);
	}

	/**
	 * @param ch
	 * @param start
	 * @param length
	 * @throws SAXException
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		handler.characters(ch, start, length);
	}

	/**
	 * @param ch
	 * @param start
	 * @param length
	 * @throws SAXException
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		handler.ignorableWhitespace(ch, start, length);
	}

	/**
	 * @param target
	 * @param data
	 * @throws SAXException
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
	 */
	public void processingInstruction(String target, String data) throws SAXException {
		handler.processingInstruction(target, data);
	}

	/**
	 * @param name
	 * @throws SAXException
	 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
	 */
	public void skippedEntity(String name) throws SAXException {
		handler.skippedEntity(name);
	}


}
