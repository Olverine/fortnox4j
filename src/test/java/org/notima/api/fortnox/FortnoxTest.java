package org.notima.api.fortnox;

import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.slf4j.Logger;

/**
 * Base class for Fortnox Tests
 * 
 * Copyright 2019 Notima System Integration AB (Sweden)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Daniel Tamm
 *
 */

public class FortnoxTest {

	protected static FortnoxClient3 client;
	
	protected Logger	log = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	@BeforeClass
	public static void setUp() throws Exception {

		client = TestUtil.getFortnoxClient();
		if (client==null) {
			fail("Missing test-config3.xml. Please rename config3-template.xml and fill in your authorization details.");
		}
		
	}
	
	
}
