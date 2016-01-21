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
package test.deployer;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;

import com.heliosapm.jboss.deployer.jaxb.SAXReaders;
import com.heliosapm.jboss.deployer.model.datasource.Datasource;
import com.heliosapm.jboss.deployer.model.datasource.Datasources;
import com.kscs.util.jaxb.BoundList;

import test.BaseTest;
import test.XMLHelper;

/**
 * <p>Title: H2UnmarshalTest</p>
 * <p>Description: Tests the marshalling and unmarshalling of a sample ds.xml</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>test.deployer.H2UnmarshalTest</code></p>
 */
@SuppressWarnings("static-method")
public class H2UnmarshalTest extends BaseTest {
	
	/** The base h2 datasource definition */
	public static final String BASE_H2 = "datasources/h2-ds.xml";

	/**
	 * Tests datasource unmarshaling
	 * @throws Exception thrown on any error
	 */
	
	@Test
	public void testDataSource() throws Exception {
		final Node node = XMLHelper.parseXML(CL.getResource(BASE_H2));
		Datasources ds = SAXReaders.unmarshal(BASE_H2, Datasources.class);
		log(ds);
		BoundList<Object> dsx = ds.getDatasourceOrXaDatasource();
		Assert.assertEquals("Datasource Count", 1, dsx.size());
		Datasource d = (Datasource)dsx.iterator().next();
		Assert.assertEquals("JNDI Name", XMLHelper.xGetAttribute(node, "jndi-name", "//datasource"), d.getJndiName());
		Assert.assertEquals("Pool Name", XMLHelper.xGetAttribute(node, "pool-name", "//datasource"), d.getPoolName());
		Assert.assertEquals("Enabled", XMLHelper.xGetAttribute(node, "enabled", "//datasource"), d.getEnabled().toString());
		Assert.assertEquals("UseJavaContext", XMLHelper.xGetAttribute(node, "use-java-context", "//datasource"), d.getUseJavaContext().toString());
		Assert.assertFalse("Connectable",  d.getConnectable());
		Assert.assertTrue("JTA",  d.getJta());
		Assert.assertFalse("Spy",  d.getSpy());
		Assert.assertEquals("ConnectionURL", XMLHelper.getNodeTextValue(XMLHelper.xGetNode(node, "//datasource/connection-url")), d.getConnectionUrl()); 
		Assert.assertEquals("Driver", XMLHelper.getNodeTextValue(XMLHelper.xGetNode(node, "//datasource/driver")), d.getDriver());
				
		
	}
	
//	<datasources xmlns="urn:jboss:domain:datasources:1.2" >
//    <datasource jndi-name="java:jboss/datasources/ExampleDS" pool-name="ExampleDS" enabled="true" use-java-context="true">
	
	
//    <datasource jndi-name="java:jboss/datasources/ExampleDS" pool-name="ExampleDS" enabled="true" use-java-context="true">
//    <connection-url>jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE</connection-url>
//    <driver>h2</driver>
//    <security>
//        <user-name>sa</user-name>
//        <password>sa</password>
//    </security>
//</datasource>
//<drivers>
//    <driver name="h2" module="com.h2database.h2">
//        <xa-datasource-class>org.h2.jdbcx.JdbcDataSource</xa-datasource-class>
//    </driver>
//</drivers>
	
	
}
