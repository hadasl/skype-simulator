/**
 * Copyright (C) 2010, Skype Limited
 *
 * All intellectual property rights, including but not limited to copyrights,
 * trademarks and patents, as well as know how and trade secrets contained in,
 * relating to, or arising from the internet telephony software of
 * Skype Limited (including its affiliates, "Skype"), including without
 * limitation this source code, Skype API and related material of such
 * software proprietary to Skype and/or its licensors ("IP Rights") are and
 * shall remain the exclusive property of Skype and/or its licensors.
 * The recipient hereby acknowledges and agrees that any unauthorized use of
 * the IP Rights is a violation of intellectual property laws.
 *
 * Skype reserves all rights and may take legal action against infringers of
 * IP Rights.
 *
 * The recipient agrees not to remove, obscure, make illegible or alter any
 * notices or indications of the IP Rights and/or Skype's rights and
 * ownership thereof.
 */

package util;

import java.text.*;

/**
 * Collect common "parse" XML document fields and encapsulate common code as methods.
 * 
 * @author Andrea Drane
 * 
 * @since 1.0
 */
public class XmlStrMgr {
	/**
	 * Info/Debug console output message prefix/identifier tag.
	 * Corresponds to class name.
	 * 
	 * @since 1.0
	 */
    public static final String MY_CLASS_TAG = "XmlStrMgr";

	/**
	 * Debug console output level: true for verbose; false for normal (default).
	 * 
	 * @since 1.0
	 */
    private boolean verboseDebugLvl = false;

    private static ParsePosition parsePos = new ParsePosition(0);
	private Number parseResult;
	private static DecimalFormat parseFmt = new DecimalFormat();


	/**
	 * "Parse" an XML document for the first occurrence of a particular tag and return its value.
	 * 
	 * The parse terminates upon encountering the <em>first</em> opening angle bracket (<) following
	 * the opening target tag. 
	 * 
	 * @param xmlDoc
	 * 	The target XML document. 
	 * @param xmlTag
	 * 	The target tag <em>including</em> the enclosing angle brackets.
	 * @param xmlStart
	 * 	The character position (from zero) in the document at which to start
	 *  looking for the target tag.
	 * 
	 * @return
	 * 	<ul>
	 * 		<li>a String representation of the element value, which will be the empty
	 * 			string if target tag is empty, for example, <code>&lt;part&gt;&lt;/part&gt;</code></li>
	 * 		<li>null if not found</li>
	 * </ul>
	 * 
	 * @since 1.0
	 */
	public String getXmlValueStr(String xmlDoc, String xmlTag, ParsePosition xmlStart) {
		
		if (verboseDebugLvl) {
			MySession.myConsole.printf("%s.getXmlValueStr: xmlDoc =%n%s%nxmlTag = %s%nstartIdx = %d%n",
							MY_CLASS_TAG, xmlDoc, xmlTag, xmlStart.getIndex());
		}
		
        int i = xmlDoc.indexOf(xmlTag, xmlStart.getIndex());
        if (i != -1)
        {
        	i += xmlTag.length(); 
            int j = xmlDoc.indexOf("</", i);
    		return (xmlDoc.substring(i, j));
        }
        return (null);
	}

	
	/**
	 * "Parse" an XML document for the first occurrence of a particular tag and return its value, which is assumed to be a number.
	 * 
	 * The parse terminates based on the rules for java.text.DecimalFormat, using the
	 * pattern and symbol sets for the default Locale.
	 * 
	 * @param xmlDoc
	 * 	The target XML document. 
	 * @param xmlTag
	 * 	The target tag <em>including</em> the enclosing angle brackets.
	 * @param xmlStart
	 * 	The character position (from zero) in the document at which to start
	 *  looking for the target tag.
	 * 
	 * @return
	 * 	<ul>
	 * 		<li>an integer representation of the element value</li>
	 * 		<li>-1 if:
	 * 			<ul>
	 * 				<li>not found</li>
	 * 				<li>the target tag is empty, for example, <code>&lt;part&gt;&lt;/part&gt;</code></li>
	 * 				<li>a parse error occurred</li>
	 * 			</ul>
	 * 		</li>
	 * 	</ul>
	 * 
	 * @since 1.0
	 */
	public int getXmlValueNum(String xmlDoc, String xmlTag, ParsePosition xmlStart) {
		
		if (verboseDebugLvl) {
			MySession.myConsole.printf("%s.getXmlValueNum: xmlDoc =%n%s%nxmlTag = %s%nstartIdx = %d%n",
							MY_CLASS_TAG, xmlDoc, xmlTag, xmlStart.getIndex());
		}

		parseFmt.setParseBigDecimal(false);
		parsePos.setErrorIndex(-1);
		
        int i = xmlDoc.indexOf(xmlTag, xmlStart.getIndex());
        if (i != -1)
        {
    		parsePos.setIndex((i + xmlTag.length()));
        	if ((parseResult = parseFmt.parse(xmlDoc, parsePos)) != null) {
        		return (parseResult.intValue());
        	}
        }
        return (-1);
	}
	
	/**
	 * "Parse" an XML document for the first occurrence of a particular substring and
	 * return the position of the character <em>following</em> it.
	 * 
	 * This enables the invoker to "count" its way through an XML document (by placing
	 * the invocation in a loop), access attribute values, skip over closing XML tags,
	 * and so forth.
	 * 
	 * @param xmlDoc
	 * 	The target XML document. 
	 * @param subStr
	 * 	The target substring, typically an opening XML tag and its initial attribute
	 * 	<em>or</em> a closing XML tag, such as:
	 * <ul>
	 * 	<li><code>&lt;part identity="</code></li>
	 * 	<li><code>&lt;/part&gt;</code></li>
	 * </ul>
	 * @param xmlStart
	 * 	The character position (from zero) in the document at which to start
	 *  looking for the target substring.
	 * 
	 * @return
	 * 	A ParsePosition instance with its index set to the position of the character
	 *  <em>following</em> the target substring or null if not found.
	 * 
	 * @since 1.0
	 */
	public ParsePosition getXmlSubStrPos(String xmlDoc, String subStr, ParsePosition xmlStart) {
		
		if (verboseDebugLvl) {
			MySession.myConsole.printf("%s.getXmlSubStrPos: xmlDoc =%n%s%nsubStr = %s%nstartIdx = %d%n",
					MY_CLASS_TAG, xmlDoc, subStr, xmlStart.getIndex());
		}

		parsePos.setErrorIndex(-1);
		
        int i = xmlDoc.indexOf(subStr, xmlStart.getIndex());
        if (i != -1)
        {
        	parsePos.setIndex((i + subStr.length()));
        	return (parsePos);
        }
        
        return (null);
	}


	/**
	 * Determine whether verbose debug is set.
	 * 
	 * @return
	 * 	<ul>
	 * 		<li>true: verbose debug set</li>
	 * 		<li>false: normal debug set</li>
	 * </ul>
	 * 
	 * @since 1.0
	 */
	public boolean getVerboseDebug() {
	
		return (this.verboseDebugLvl);
	}
	
	
	/**
	 * Turn verbose debug on/off.
	 * 
	 * @param onOff
	 * 	<ul>
	 * 		<li>true: turn on verbose debug</li>
	 * 		<li>false: turn off verbose debug</li>
	 * </ul>
	 * 
	 * @since 1.0
	 */
	public void setVerboseDebug(boolean onOff) {
	
		this.verboseDebugLvl = onOff;
	}
}

