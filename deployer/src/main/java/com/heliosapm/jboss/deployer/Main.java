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
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.heliosapm.jboss.deployer.model.datasource.Datasource;
import com.heliosapm.jboss.deployer.model.datasource.Datasources;
import com.heliosapm.jboss.deployer.model.datasource.Pool;

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
		//File f = new File("/home/nwhitehead/services/jboss/wiex/docs/examples/jca/oracle-xa-ds.xml");
		File f = new File("./src/test/resources/datasources/h2-example-ds.xml");
		
		try {
			if(!f.canRead()) throw new Exception("Cannot read [" + f + "]");
			final JAXBContext ctx = JAXBContext.newInstance(Datasources.class);
			final Unmarshaller unm = ctx.createUnmarshaller();
			final Marshaller mar = ctx.createMarshaller();
			mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			Datasource dt = Datasource.builder()
				.withConnectable(true)
				.withConnectionUrl("http://mydb:84")
				.withDriver("com.heliosapm.mydb")
				.withEnabled(true)
				.withJndiName("java:ds/mydb")
				.withJta(true)
				.withNewConnectionSql("select foo from bar")
				.withPoolName("MyDBPool")
				.withPool(Pool.builder().withMaxPoolSize(new BigInteger("100")).withMinPoolSize(new BigInteger("5")).withPrefill(true).build())
				.withStatisticsEnabled(true)
				.withUseJavaContext(true)
				.build();
			Datasources ds = Datasources.builder().addDatasource(dt).build();
			log("DS: %s", ds.toString());
			log("======================================");
			final StringWriter sw = new StringWriter();			
			mar.marshal(ds, sw);
			log("XML: %s", sw.toString());		
			log("======================================");
			Datasources rds = (Datasources) unm.unmarshal(new StringReader(sw.toString()));
			log("RDS: %s", rds);
//			mar.marshal(new JAXBElement<Datasources>(new QName("uri","local"), Datasources.class, rds), sw);
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}

	}
	
	public static void log(final Object fmt, final Object...args) {
		System.out.println(String.format(fmt.toString(), args));
	}

}
