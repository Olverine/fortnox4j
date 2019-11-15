package org.notima.api.fortnox;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for Fortnox API.
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

public class FortnoxUtil {

	public static String  allowedPatternStr = "[\\p{L}\\’\\\\\\x{0308}\\x{030a}a-zåäöéáœæøüA-ZÅÄÖÉÁÜŒÆØ0-9 –:\\.`´’,;\\^¤#%§£$€¢¥©™°&\\/\\(\\)=\\+\\-\\*_\\!?²³®½\\@\\x{00a0}\\n\\r]";
	public static Pattern allowedPattern = Pattern.compile(allowedPatternStr);
	public static Pattern allowedChars = Pattern.compile("^" + allowedPatternStr + "*$");
	
	/**
	 * Removes non-allowed characters from given string by replacing it by a white space
	 * 
	 * @param src
	 * @return
	 */
	public static String removeNonAllowedCharacters(String src) {
		
		if (src==null) return null;

		Matcher matcher = allowedPattern.matcher(src);
		if (matcher.matches())
			// No illegal characters found (ie all are allowed)
			return src;
		else {
			
			StringBuffer dst = new StringBuffer();
			Matcher cMatcher;
			// Replace all non allowed chars with space
			String c;
			for (int i=0; i<src.length(); i++) {
				c = src.substring(i, i+1);
				cMatcher = allowedChars.matcher(c);
				if (cMatcher.matches()) {
					dst.append(c);
				} else {
					dst.append(" ");
				}
			}
			
			return dst.toString();
		}
		
	}
	
}
