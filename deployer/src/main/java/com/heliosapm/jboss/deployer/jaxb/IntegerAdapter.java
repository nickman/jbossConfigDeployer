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

import java.math.BigInteger;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * <p>Title: IntegerAdapter</p>
 * <p>Description: Adapter to switch the API BigIntegers to Integers</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.jboss.deployer.jaxb.IntegerAdapter</code></p>
 */

public class IntegerAdapter extends XmlAdapter<BigInteger, Integer> {

	/**
	 * {@inheritDoc}
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public Integer unmarshal(final BigInteger v) throws Exception {
		return v.intValue();
	}

	/**
	 * {@inheritDoc}
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public BigInteger marshal(final Integer v) throws Exception {
		return v==null ? null : new BigInteger(String.valueOf(v));
	}
	
	public static Integer toInt(final BigInteger v) throws Exception {
		return v.intValue();
	}

	public static BigInteger fromInt(final Integer v) throws Exception {
		return v==null ? null : new BigInteger(String.valueOf(v));
	}
	

}
