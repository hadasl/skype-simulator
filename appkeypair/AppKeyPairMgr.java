/**
 * Copyright (C) 2011, 2012 Skype
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

package appkeypair;

import java.io.*;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;

import com.skype.util.PemReader;

import util.MySession;


	/**
	 * Encapsulates validating the pathname and format of the AppKeyPair required to access the SkypeKit runtime.
	 * <br /><br />
	 * However, a valid pathname and format does <i>not</i> guarantee that the AppKeyPair is valid for this application. 
	 * 
	 * @author Andrea Drane
	 * 
	 * @since 1.0
	 */
	public class AppKeyPairMgr {
		/**
		 * Info/Debug console output message prefix/identifier tag.
		 * Corresponds to class name.
		 * 
		 * @since 1.0
		 */
	    public static final String MY_CLASS_TAG = "AppKeyPairMgr";

	    /**
	     * Expected suffixes (case insensitive) for the file containing the AppKeyPair data.
	     * Used by {@link #resolveAppKeyPairPath(String)}.
	     * 
	     * @since 1.0
	     */
	    public static final String[] KEY_PAIR_FILE_SUFFIXES = {
	    	"crt",
	    	"pem",
	    	"CRT",
	    	"PEM"
	    };

	    /**
	     * Default name of the PEM file containing the AppKeyPair data string.
	     * Used by {@link #resolveAppKeyPairPath(String)}.
	     * 
	     * @since 1.0
	     */
	    public static final String KEY_PAIR_DEFAULT_PATHNAME = "skypekit-cert.pem";

	    /**
	     * The resolved name of the PEM file containing the certificate.
	     * Used by {@link #resolveAppKeyPairPath(String)}.
	     * 
	     * @since 2.0
	     */
	    public String pemFilePathname = KEY_PAIR_DEFAULT_PATHNAME;

		
		/**
		 * Retrieves the fully resolved PEM file path.
		 * This reflects the resultant path (valid or invalid) used
		 * by the latest invocation of
		 * <code>@link{#resolveAppKeyPairPath(String)}</code>, or its default
		 * if <code>resolveAppKeyPairPath(String)</code> has not been invoked.
		 * 
		 * @return
		 * 	The the fully resolved PEM file path, which might not be a valid PEM file.
		 * 
		 * @see #KEY_PAIR_DEFAULT_PATHNAME
		 * @see #pemFilePathname
		 * 
		 * @since 2.0
		 */
		public String getPemFilePathname() {

			return (this.pemFilePathname);
		}

		/**
		 * Resolves the AppKeyPair X.509 certificate file name based on the <i>configured default</i> pathname of the PEM file containing the actual AppKeyPair data.
		 * Sets @link{pemFilePathname} to the resolved name.
		 * 
		 * @return
		 * 	<ul>
		 *	  <li>true: AppKeyPair certificate file exists</li>
		 *	  <li>false:
		 *	    <ul>
		 *	      <li>Could not resolve the pathname</li>
		 *	      <li>The specified certificate file does not exist</li>
		 *	    </ul>
		 *	    pemFilePathname is unchanged.
		 *	  </li>
		 *	</ul>
		 * 
		 * @see #KEY_PAIR_DEFAULT_PATHNAME
		 * @see #resolveAppKeyPairPath(String)
		 * 
		 * @since 2.0
		 */
		public boolean resolveAppKeyPairPath() {
			return (resolveAppKeyPairPath(KEY_PAIR_DEFAULT_PATHNAME));
		}

		/**
		 * Common method to resolve the AppKeyPair certificate file name.
		 * <br /><br />
		 * Initially assumes the name of the path of the file containing the AppKeyPair data includes a
		 * path component. If it doesn't end in one of the recognized suffixes, assumes its a
		 * path-only component and appends the default file pathname {@link #KEY_PAIR_DEFAULT_PATHNAME},
		 * which it assumes is configured as a filename. Writes diagnostic messages to the console
		 * if the specified file does not exist.
		 * <br /><br />
		 * This is useful since SkypeKit currently reports only "TLS handshake failure" (with no further explanation)
		 * if the certificate can't be found/accessed or is not validly formatted.
		 * 
		 * @param pathName
		 * 	The path of the file containing the actual certificate data.
		 * 
		 * @return
		 * 	<ul>
		 *	  <li>true: AppKeyPair successfully read (and set) from the file data</li>
		 *	  <li>false:
		 *	    <ul>
		 *	      <li>Could not open the file</li>
		 *	      <li>Could not read the file</li>
		 *	      <li>Not a valid certificate file</li>
		 *	    </ul>
		 *	    AppKeyPair is unchanged.
		 *	  </li>
		 *	</ul>
		 * 
		 * @see #KEY_PAIR_FILE_SUFFIXES
		 * @see #KEY_PAIR_DEFAULT_PATHNAME
		 * 
		 * @since 2.0
		 */
		public boolean resolveAppKeyPairPath(String pathName) {
			int	i;
			int	j;
			File tmpFile;
			
			j = KEY_PAIR_FILE_SUFFIXES.length;
			for (i = 0; i < j; i++) {
				if (pathName.endsWith(KEY_PAIR_FILE_SUFFIXES[i])) {
					break;
				}
			}
			
			// Check for no suffix match - either an unrecognized suffix or just a path name.
			if (i >= j) {
				if (pathName.charAt((pathName.length() - 3)) == '.') {
					// Unrecognized suffix (if you mean a path that includes ".xyz" as it's last
					// four characters, pass it as ".xyz/"
					MySession.myConsole.printf("%s/resolveAppKeyPairPath: Unrecognized Certificate file suffix.%n\tPathname: %s%n\tRecognized suffixes:%n", 
							MY_CLASS_TAG, pathName);
					j = KEY_PAIR_FILE_SUFFIXES.length;
					for (i = 0; i < j; i++) {
						MySession.myConsole.printf("\t\t%s%n", KEY_PAIR_FILE_SUFFIXES[i]);
					}
					return (false);
				}
				else {
					// Just a path name, so append the default portion of the path.
					pemFilePathname = pathName + File.separator + KEY_PAIR_DEFAULT_PATHNAME;
				}
			}
			else {
				// Found a matching suffix, so go with what we have.
				pemFilePathname = pathName;
			}

		    try {
				tmpFile = new File(pathName);
		    }  
		    catch (NullPointerException e) {
		    	// This should really never happen...
		    	MySession.myConsole.printf("%s/resolveAppKeyPairPath: Specified pathname is NULL%n", 
		    			MY_CLASS_TAG);
		    	pemFilePathname = KEY_PAIR_DEFAULT_PATHNAME;
		    	return (false);
			}

	    	if (!tmpFile.exists()) {
	    		MySession.myConsole.printf("%s/resolveAppKeyPairPath: Certificate file doesn't exist:%n\t%s%n",
	    				MY_CLASS_TAG, pemFilePathname);
               return (true);
		    }

	    	MySession.myConsole.printf("%s/resolveAppKeyPairPath: Found certificate file:%n\t%s%n",
         		   MY_CLASS_TAG, pemFilePathname);
		    return (true);
		}
	
	/**
	 * Determines whether the current AppKeyPair (as specified by {@link #pemFilePathname}) is a valid X.509 certificate.
	 * <br /><br />
	 * Writes diagnostic messages to the console if the the current AppKeyPair does not exist or is not valid.
	 * This is useful since SkypeKit currently reports only "TLS handshake failure" (with no further explanation)
	 * if the certificate can't be found/accessed or is not validly formatted.
	 * 
	 * @return
	 * 	<ul>
	 *	  <li>true: Valid certificate file</li>
	 *	  <li>false:
	 *	    <ul>
	 *	      <li>Could not open the file</li>
	 *	      <li>Could not read the file</li>
	 *	      <li>Not a valid certificate file</li>
	 *	    </ul>
	 *	  </li>
	 *	</ul>
	 * 
	 * @see #pemFilePathname
	 * @see com.skype.tutorial.util.MySession#myClientConfiguration
	 * @since 2.0
	 */
	public boolean isValidCertificate() {
		X509Certificate x509CertificateData;
		PrivateKey privateKeyData;
		
		String pathName = getPemFilePathname();

	    try {
	        PemReader myPemReader = new PemReader(getPemFilePathname());
	        x509CertificateData = myPemReader.getCertificate();	
	        privateKeyData = myPemReader.getKey();
	    }  
          catch (FileNotFoundException e1) {
               MySession.myConsole.printf("%s/isValidCertificate: Could not find certificate file:%n\t%s%n", 
            		   MY_CLASS_TAG, pathName);
               return (false);
           }
	    catch (IOException e2) {
	    	MySession.myConsole.printf("%s/isValidCertificate: Unable to read certificate file:%n\t%s%n",
            		   MY_CLASS_TAG, pathName);
               return (false);
	    }
	    catch (InvalidKeySpecException e3) {
	    	MySession.myConsole.printf("%s/isValidCertificate: Certificate file %s contains invalid certficate data.%n",
		    		MY_CLASS_TAG, pathName);
		    return(false);
	    }
	    
	    if ((x509CertificateData != null) && (privateKeyData != null)) {
	    	MySession.myConsole.printf("%s/isValidCertificate: Certificate has valid format%n",
	         	   MY_CLASS_TAG);
	    	return (true);
	    }
	    else {
	    	MySession.myConsole.printf("%s/isValidCertificate: Certificate has invalid format%n",
		            MY_CLASS_TAG);
	    	return (false);
	    }
	}
}
