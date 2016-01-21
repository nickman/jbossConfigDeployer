/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.heliosapm.jboss.deployer.jaxb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.transform.sax.SAXSource;

import org.jboss.marshalling.ByteBufferInput;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.heliosapm.jboss.deployer.datasource.DatasourceXMLReader;

/**
 * <p>Title: SAXReaders</p>
 * <p>Description: Static sax reader generator factories</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.jboss.deployer.jaxb.SAXReaders</code></p>
 */

public class SAXReaders {
	
	private static final ClassLoader CL = SAXReaders.class.getClassLoader();
	private static final Map<String, JAXBContext> jaxbContexts = new ConcurrentHashMap<String, JAXBContext>();

	/**
	 * Returns a shared JAXBContext for the passed class
	 * @param clazz The class to acquire a JAXBContext for
	 * @return the shared JAXBContext
	 */
	public static JAXBContext getContext(final Class<?> clazz) {
		final String pack = clazz.getPackage().getName();
		JAXBContext ctx = jaxbContexts.get(pack);
		if(ctx==null) {
			synchronized(jaxbContexts) {
				ctx = jaxbContexts.get(pack);
				if(ctx==null) {
					try {
						ctx = JAXBContext.newInstance(pack);
						jaxbContexts.put(pack, ctx);
					} catch (Exception ex) {
						throw new RuntimeException("Failed to get JAXBContext for [" + pack + "]", ex);
					}
				}
			}
		}
		return ctx;		
	}
	
	/**
	 * Unmarshals an object from the passed file
	 * @param f The file to read from
	 * @param clazz The expected type of the class to unmarshal
	 * @return the unmarshalled object
	 * @param <T> the expected type of the class to unmarshal
	 */
	public static <T> T unmarshal(final File f, final Class<T> clazz) {
		final JAXBContext ctx = getContext(clazz);
		final SAXSource sax = getSax(f);
		try {
			return ctx.createUnmarshaller().unmarshal(sax, clazz).getValue();
		} catch (Exception ex) {
			throw new RuntimeException("Failed to unmarshal [" + clazz.getName() + "] from the file [" + f + "]", ex);
		}
	}
	
	/**
	 * Unmarshals an object from the resource loaded using the passed resource name
	 * @param resourceName The name of the resource to load
	 * @param clazz The expected type of the class to unmarshal
	 * @return the unmarshalled object
	 * @param <T> the expected type of the class to unmarshal
	 */
	public static <T> T unmarshal(final String resourceName, final Class<T> clazz) {
		final JAXBContext ctx = getContext(clazz);
		final SAXSource sax = getSax(resourceName);
		try {
			return ctx.createUnmarshaller()
					.unmarshal(sax, clazz)
					.getValue();
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			throw new RuntimeException("Failed to unmarshal [" + clazz.getName() + "] from the resource [" + resourceName + "]", ex);
		}
	}
	
	
	public static SAXSource getSax(final File f) {
		try {
			final ByteBuffer bb = read(f);
			XMLReader reader = new DatasourceXMLReader();
			InputSource is = new InputSource(new ByteBufferInput(bb));			
			return new SAXSource(reader, is);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public static SAXSource getSax(final String resourceName) {
		try {
			final ByteBuffer bb = read(CL.getResourceAsStream(resourceName));
			XMLReader reader = new DatasourceXMLReader();
			bb.flip();
			InputSource is = new InputSource(new ByteBufferInput(bb));			
			return new SAXSource(reader, is);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
	public static ByteBuffer read(final File file) {		
		FileInputStream fis = null;
		FileChannel fc = null;
		MappedByteBuffer mbb = null;
		try {
			fis = new FileInputStream(file);
			fc = fis.getChannel();
			final long size = fc.size();
			mbb = fc.map(MapMode.READ_ONLY, 0, size);
			mbb.load();
			ByteBuffer bb = ByteBuffer.allocateDirect((int)file.length());
			bb.put(mbb);
			bb.flip();
			return bb;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if(mbb!=null) try { NIOHelper.clean(mbb); } catch (Exception x) {/* No Op */}
			if(fis!=null) try { fis.close(); } catch (Exception x) {/* No Op */}
			if(fc!=null) try { fc.close(); } catch (Exception x) {/* No Op */}
		}
	}

	public static ByteBuffer read(final InputStream is) {
		try {
			final ByteBuffer bb = ByteBuffer.allocateDirect(is.available());
			Channels.newChannel(is).read(bb);
			return bb;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			try { is.close(); } catch (Exception x) {/* No Op */}
		}		
	}


}
