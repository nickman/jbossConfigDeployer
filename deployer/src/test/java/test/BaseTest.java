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
package test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import com.heliosapm.jboss.deployer.jaxb.SAXReaders;

/**
 * <p>Title: BaseTest</p>
 * <p>Description: </p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>test.deployer.BaseTest</code></p>
 */

public class BaseTest {
	
	public static final ClassLoader CL = BaseTest.class.getClassLoader();
	
	/** The currently executing test name */
	@Rule public final TestName name = new TestName();

	/**
	 * Prints the test name about to be executed
	 */
	@Before
	public void printTestName() {
		log("\n\t==================================\n\tRunning Test [%s]\n\t==================================\n", name.getMethodName());
	}
	
	
	/**
	 * Standard out logger 
	 * @param fmt The message format
	 * @param args The format args
	 */
	public static void log(final Object fmt, final Object...args) {
		System.out.println(String.format(fmt.toString(), args));
	}
	
	/**
	 * Standard err logger 
	 * @param fmt The message format
	 * @param args The format args
	 */
	public static void loge(final Object fmt, final Object...args) {
		System.err.println(String.format(fmt.toString(), args));
		if(args.length>0 && (args[args.length-1] instanceof Throwable)) {
			((Throwable)args[args.length-1]).printStackTrace(System.err);
		}
	}
}
	
