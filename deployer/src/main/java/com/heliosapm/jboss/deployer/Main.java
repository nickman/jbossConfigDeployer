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
package com.heliosapm.jboss.deployer;

import java.io.File;
import java.io.FileReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.heliosapm.jboss.deployer.datasource.DatasourceXMLReader;
import com.heliosapm.jboss.deployer.model.datasource.Datasource;
import com.heliosapm.jboss.deployer.model.datasource.Datasources;
import com.heliosapm.jboss.deployer.model.datasource.Subsystem;
import com.heliosapm.jboss.deployer.model.datasource.XaDatasource;

/**
 * <p>Title: Main</p>
 * <p>Description: </p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.jboss.deployer.Main</code></p>
 */

public class Main {

	/**
	 * Creates a new Main
	 */
	public Main() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		log("DS Test");
//		File f = new File("/home/nwhitehead/services/jboss/wiex/docs/examples/jca/oracle-xa-ds.xml");
		File f = new File("./src/test/resources/datasources/h2-ds.xml");
		File dsTestDir = new File("./src/test/resources/datasources");
		try {
			if(!f.canRead()) throw new Exception("Cannot read [" + f + "]");
			final JAXBContext ctx = JAXBContext.newInstance(Subsystem.class);
			final Unmarshaller unm = ctx.createUnmarshaller();
			final Marshaller mar = ctx.createMarshaller();
			mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			unm.setListener(new Unmarshaller.Listener(){
				@Override
				public void afterUnmarshal(Object target, Object parent) {
					log("Object: [%s], Parent: [%s]", target.getClass().getName(), parent);
					super.afterUnmarshal(target, parent);
				}
			});
			unm.setEventHandler(new ValidationEventHandler(){

				@Override
				public boolean handleEvent(ValidationEvent event) {
					// TODO Auto-generated method stub
					return false;
				}
				
			});
//			Datasources rds = (Datasources) unm.unmarshal(f);
			Object rds = unm.unmarshal(getSax(f));
//			Object rds = unm.unmarshal(f);
			log("DS:" + rds);
			for(File dsf : dsTestDir.listFiles()) {
				FileReader fr = null;
				try {
					log("File: %s", dsf.getName());
					fr = new FileReader(dsf);
					XMLReader reader = new DatasourceXMLReader();
					InputSource is = new InputSource(fr);
	        SAXSource ss = new SAXSource(reader, is);
	        					
					Datasources r = (Datasources) unm.unmarshal(ss);
					Object d = r.getDatasourceOrXaDatasource().iterator().next();
					String jndi = null;
					if(d instanceof XaDatasource) {
						jndi = ((XaDatasource)d).getJndiName();
					} else {
						jndi = ((Datasource)d).getJndiName();
					}
					log("DS [%s]  JNDI:[%s] \n\t%s", dsf, jndi,  r);
				} catch (UnmarshalException uex) {
					System.err.println(dsf.getName() + ": " + uex.getMessage());
				} finally {
					if(fr!=null) try { fr.close(); } catch (Exception x) {/* No Op */}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		
		
//		try {
//			if(!f.canRead()) throw new Exception("Cannot read [" + f + "]");
//			final JAXBContext ctx = JAXBContext.newInstance(Datasources.class);
//			final Unmarshaller unm = ctx.createUnmarshaller();
//			final Marshaller mar = ctx.createMarshaller();
//			mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//			
//			Datasource dt = Datasource.builder()
//				.withConnectable(true)
//				.withConnectionUrl("http://mydb:84")
//				.withDriver("com.heliosapm.mydb")
//				.withEnabled(true)
//				.withJndiName("java:ds/mydb")
//				.withJta(true)
//				.withNewConnectionSql("select foo from bar")
//				.withPoolName("MyDBPool")
//				.withPool(Pool.builder().withMaxPoolSize(100).withMinPoolSize(5).withPrefill(true).build())
//				.withStatisticsEnabled(true)
//				.withUseJavaContext(true)
//				.build();
//			Datasources ds = Datasources.builder().addDatasource(dt).build();
//			log("DS: %s", ds.toString());
//			log("======================================");
//			final StringWriter sw = new StringWriter();			
//			mar.marshal(ds, sw);
//			log("XML: %s", sw.toString());		
//			log("======================================");
//			Datasources rds = (Datasources) unm.unmarshal(new StringReader(sw.toString()));
//			log("RDS: %s", rds);
////			mar.marshal(new JAXBElement<Datasources>(new QName("uri","local"), Datasources.class, rds), sw);
//		} catch (Exception ex) {
//			ex.printStackTrace(System.err);
//		}

	}
	
	public static SAXSource getSax(final File f) {
		try {
			final FileReader fr = new FileReader(f);
			XMLReader reader = new DatasourceXMLReader();
			InputSource is = new InputSource(fr);
      SAXSource ss = new SAXSource(reader, is) {
      	
      };
      return ss;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public static void log(final Object fmt, final Object...args) {
		System.out.println(String.format(fmt.toString(), args));
	}

}
