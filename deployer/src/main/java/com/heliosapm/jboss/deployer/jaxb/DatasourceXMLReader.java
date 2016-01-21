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

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/**
 * <p>Title: DatasourceXMLReader</p>
 * <p>Description: </p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.jboss.deployer.jaxb.DatasourceXMLReader</code></p>
 */

public class DatasourceXMLReader implements XMLReader {
	
	/** The delegate reader */
	private XMLReader reader;

	/**
	 * Creates a new DatasourceXMLReader
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public DatasourceXMLReader()  throws SAXException, ParserConfigurationException {
		SAXParserFactory parserFactory;
		parserFactory = SAXParserFactory.newInstance();
		parserFactory.setNamespaceAware(false);
		parserFactory.setValidating(false);
		reader = parserFactory.newSAXParser().getXMLReader();	}

	/**
	 * {@inheritDoc}
	 * @see org.xml.sax.XMLReader#getFeature(java.lang.String)
	 */
	@Override
	public boolean getFeature(final String name) throws SAXNotRecognizedException, SAXNotSupportedException {
		return reader.getFeature(name);
	}

	/**
	 * {@inheritDoc}
	 * @see org.xml.sax.XMLReader#setFeature(java.lang.String, boolean)
	 */
	@Override
	public void setFeature(final String name, final boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
		reader.setFeature(name, value);
	}

	/**
	 * {@inheritDoc}
	 * @see org.xml.sax.XMLReader#getProperty(java.lang.String)
	 */
	@Override
	public Object getProperty(final String name) throws SAXNotRecognizedException, SAXNotSupportedException {
		return reader.getProperty(name);
	}

	/**
	 * {@inheritDoc}
	 * @see org.xml.sax.XMLReader#setProperty(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setProperty(final String name, final Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
		reader.setProperty(name, value);
	}

	/**
	 * {@inheritDoc}
	 * @see org.xml.sax.XMLReader#setEntityResolver(org.xml.sax.EntityResolver)
	 */
	@Override
	public void setEntityResolver(final EntityResolver resolver) {
		reader.setEntityResolver(resolver);

	}

	/**
	 * {@inheritDoc}
	 * @see org.xml.sax.XMLReader#getEntityResolver()
	 */
	@Override
	public EntityResolver getEntityResolver() {
		return reader.getEntityResolver();
	}

	/**
	 * {@inheritDoc}
	 * @see org.xml.sax.XMLReader#setDTDHandler(org.xml.sax.DTDHandler)
	 */
	@Override
	public void setDTDHandler(final DTDHandler handler) {
		reader.setDTDHandler(handler);
	}

	/**
	 * {@inheritDoc}
	 * @see org.xml.sax.XMLReader#getDTDHandler()
	 */
	@Override
	public DTDHandler getDTDHandler() {
		return reader.getDTDHandler();
	}

	/**
	 * {@inheritDoc}
	 * @see org.xml.sax.XMLReader#setContentHandler(org.xml.sax.ContentHandler)
	 */
	@Override
	public void setContentHandler(final ContentHandler handler) {
		reader.setContentHandler(new DatasourceContentHandler(handler));
//		reader.setContentHandler(handler);
	}

	/**
	 * {@inheritDoc}
	 * @see org.xml.sax.XMLReader#getContentHandler()
	 */
	@Override
	public ContentHandler getContentHandler() {
		return reader.getContentHandler();
	}

	/**
	 * {@inheritDoc}
	 * @see org.xml.sax.XMLReader#setErrorHandler(org.xml.sax.ErrorHandler)
	 */
	@Override
	public void setErrorHandler(final ErrorHandler handler) {
		reader.setErrorHandler(handler);
	}

	/**
	 * {@inheritDoc}
	 * @see org.xml.sax.XMLReader#getErrorHandler()
	 */
	@Override
	public ErrorHandler getErrorHandler() {
		return reader.getErrorHandler();
	}

	/**
	 * {@inheritDoc}
	 * @see org.xml.sax.XMLReader#parse(org.xml.sax.InputSource)
	 */
	@Override
	public void parse(final InputSource input) throws IOException, SAXException {
		reader.parse(input);
	}

	/**
	 * {@inheritDoc}
	 * @see org.xml.sax.XMLReader#parse(java.lang.String)
	 */
	@Override
	public void parse(final String systemId) throws IOException, SAXException {
		reader.parse(systemId);
	}

}
