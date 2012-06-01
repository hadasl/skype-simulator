/**
 * Copyright (C) 2010-2012 Skype
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

import java.io.PrintStream;
import java.util.Locale;

import com.skype.api.Account;
import com.skype.api.Skype;
import com.skype.ipc.ClientConfiguration;

/**
 * Tutorial Session Object.
 *
 * Encapsulates common aspects of the SkypeKit-based Java tutorials.
 * These include:
 * <ul>
 * 	<li>target account name</li>
 *	<li>console PrintStream (based off <code>System.out</code>) that specifies
 *		<code>autoFlush</code> as true</li>
 *	<li>activity indicators, including (but not limited to):
 *		<ul>
 *		  <li>the associated Account's login status</li>
 *		  <li>whether a call is in progress</li>
 *		  <li>the state of SMS target and price analysis</li>
 *		  <li>whether an SMS was sent and/or delivered</li>
 *		</ul>
 *	</li>
 *	<li>transport-related members:
 *	  <ul>
 *	    <li>SkypeKit runtime IP address</li>
 *	    <li>SkypeKit runtime port number</li>
 *	    <li>com.skype.ipc.ClientConfiguration instance</li>
 *	  </ul>
 *	</li>
 *	<li>{@link com.skype.api.Skype} instance</li>
 *	<li>{@link com.skype.api.Account} instance</li>
 *	<li>{@link SignInMgr} instance</li>
 *	<li>{@link Listeners} instance (tutorials-specific "helper" class instance that
 *	 	implements the various "listener" interfaces)</li>
 *	<li>{@link #setupAudioDevices(int, int)} method to commonize assignment of microphone/speaker devices
 *		for calls</li>
 * </ul>
 * 
 * @author Andrea Drane (ported/refactored from existing C++ tutorial code)
 * 
 * @since 1.0
 */
public class MySession {
	/**
	 * Default value for the Info/Debug console output message prefix/identifier tag,
	 * in case we get passed a null or empty string.
	 * 
	 * @since 1.0
	 */
    public static final String T_TAG_DFLT = "Tutorial_X";
    
	/**
	 * Info/Debug console output message prefix/identifier tag.
	 * Corresponds to the tutorial's class name.
	 * 
	 * @since 1.0
	 */
    public String myTutorialTag;

    /**
	 * Console PrintStream.
	 * <br /><br />
	 * Based off <code>System.out</code>, but specifies <code>autoFlush</code> as true to ensure
	 * that console output does not intermingle since both the tutorial code and the
	 * event handlers write to the console.
	 * 
	 * @since 1.0
	 */
    public static PrintStream myConsole = new PrintStream(System.out, true);

	/**
	 * Name of the target Skype account, which is actually the Skype Name
	 * of the user that created it.
	 * 
	 * @since 1.0
	 */
    public String myAccountName;
    
    public SignInMgr mySignInMgr = new SignInMgr();

    /**
	 * Skype instance for this tutorial session.
	 * 
	 * @see com.skype.api.Skype
	 * 
	 * @since 1.0
	 */
    public Skype mySkype = null;

    /**
	 * SkypeKit configuration instance for this tutorial session.
	 * Contains transport port/IP address and certificate file data.
	 * 
	 * @since 2.0
	 */
    public ClientConfiguration myClientConfiguration = null;

    /**
	 * SkypeKit version number parse instance for this tutorial session.
	 * <br /><br />
	 * Do <em>not</em> attempt to instantiate this instance until <em>after intializing</em>
	 * {@link #mySkype}!
	 * 
	 * @see com.skype.tutorial.util.ParseSkypeKitVersion
	 * 
	 * @since 1.0
	 */
    public ParseSkypeKitVersion myParseSkypeKitVersion = null;
    
    /**
	 * Account instance for this tutorial session.
	 * Set on successful login, <i>not</i> during session creation!
	 * 
	 * @see com.skype.api.Account
	 * 
	 * @since 1.0
	 */
    public Account myAccount = null;
    
    /**
	 * Whether we are currently in a call.
	 * <br /><br />
	 * Set to true when a Conversation goes live (<code>Conversation.LOCAL_LIVEStatus.RINGING_FOR_ME</code>)
	 * after a successful <code>Conversation.join</code> or <code>Conversation.JoinLiveSession</code>;
	 * set to false when a Conversation goes non-live (<code>Conversation.LOCAL_LIVEStatus.RECENTLY_LIVE</code> or
	 * <code>Conversation.LOCAL_LIVEStatus.NONE</code>).
	 * 
	 * @see com.skype.tutorial.util.Listeners#onPropertyChange(com.skype.api.Conversation, com.skype.api.Conversation.Property, int, String)
	 * @see com.skype.tutorial.util.Listeners#onConversationListChange(Skype, com.skype.api.Conversation, com.skype.api.Conversation.ListType, boolean)
	 *  
	 * @since 1.0
	 */
    public boolean callActive = false;
    
	/**
	 * Indicates whether an SMS is ready to be sent.
	 * 
	 * When this count equals the number of items in the recipient list, all
	 * targets have been analyzed&mdash;for good or bad&mdash;and the SMS is ready to send.
	 * 
	 * @since 3.0
	 */
    public int smsReadyToSend = 0;

	/**
	 * Indicates whether an SMS was successfully sent.
	 * 
	 * A non-zero value indicates the number of recipients to which the SMS was
	 * successfully sent.
	 * 
	 * @since 3.0
	 */
    public boolean smsWasSent = false;

	/**
	 * Indicates whether an SMS failed to be sent.
	 * 
	 * A non-zero value indicates the number of recipients to which the SMS was
	 * could <em>not</em> be successfully sent/delivered.
	 * 
	 * @since 3.0
	 */
    public boolean smsHasFailed = false;

	/**
	 * Indicates whether an SMS was successfully received.
	 * 
	 * A non-zero value indicates the number of recipients to which the SMS was
	 * successfully delivered.
	 * @since 3.0
	 */
    public boolean smsWasDelivered = false;

	/**
	 * Indicates whether an App2App datagram is active.
	 * 
	 * @since 2.0
	 */
	public boolean quitDatagram = false;
	
    /**
	 * Indicates whether onApp2AppStreamListChange was ever fired
	 * with a non-zero stream count.
	 * <br />
	 * Basically, this is for "prettiness" - we'll only display the
	 * "connection" message <em>once</em> per session.
	 * 
	 * @since 2.0
	 */
	public boolean appConnected = false;

    /**
	 * Cached status of this session's associated Account.
	 * <br /><br />
	 * Initialized to <code>Account.Status.LOGGED_OUT</code>; updated by
	 * Account onPropertyChange handler.
	 * 
	 * @see com.skype.tutorial.util.Listeners#onPropertyChange(com.skype.api.Account, com.skype.api.Account.Property, int, String)
	 * @see com.skype.api.Account
	 * 
	 * @since 1.1
	 */
    public Account.Status loginStatus = Account.Status.LOGGED_OUT;
    
	/**
	 * Datagram stream ID, used by Tutorial 11.
	 * 
	 * @since 1.0
	 */
	public String streamName = new String("");

	/**
	 * Callbacks/event handlers for this tutorial session.
	 * 
	 * @since 1.0
	 */
    public Listeners myListeners = null;

	/**
	 * Server IP Address.
	 * 
	 * @since 1.0
	 */
    public static final String IP_ADDR = "127.0.0.1";

    /**
	 * Server Port.
	 * <br /><br />
	 * If you modify this compiled-in default, you will need to start the matching SkypeKit runtime with option:<br />
	 * &nbsp;&nbsp;&nbsp;&nbsp;<code>-p <em>9999</em></code><br />
	 * where <code>-p <em>9999</em></code> reflects this value. 
	 * 
	 * @since 1.0
	 */
    public static final int PORT_NUM = 8963;

    /**
	 * Creates <em>most</em> everything needed for a tutorial session; the Account instance is populated during sign-in. 
	 * 
	 * @param tutorialTag
	 *  The tutorial's class name. If null or the empty string, default it to <code>T_TAG_DFLT</code>.
	 * @param accountName
	 *  The <em>name</em> of the account to use for this tutorial. If null or the empty string,
	 *  <em>fail</em> by throwing a RuntimeException indicating that fact.
	 * @param pathName
	 * 	Pathname of the certificate file, which should be a PEM file.
	 * 
	 * @return
	 * <ul>
	 *   <li>true: session initialized</li>
	 *   <li>false: session initialization failed due to:
	 *   	<ul>
	 *   		<li>no or empty account name</li>
	 *   		<li>com.skype.api.Skype.Init failed - most likely from an invalid AppKeyPair</li>
	 *   		<li>could not obtain an Account instance</li>
	 *   	</ul>
	 *   </li>
	 * </ul>
	 *  
	 * @see com.skype.tutorial.util.SignInMgr
	 * 
	 * @since 1.0
	 */
/*	public boolean doCreateSession(String tutorialTag, String accountName, AppKeyPairMgr myAppKeyPairMgr) {
*/
    public boolean doCreateSession(String tutorialTag, String accountName, String pathName) {
		
 		if ((tutorialTag != null) && (tutorialTag.length() != 0)) {
			myTutorialTag = new String(tutorialTag);
		}
		else {
			myTutorialTag = T_TAG_DFLT;
		}

		if ((accountName != null) && (accountName.length() != 0)) {
			myAccountName = new String(accountName); // All tutorials minimally require an account name
		}
		else {
			throw new RuntimeException((myTutorialTag + ": Cannot initialize session instance - no account name!"));
		}
		
		// Set up our session with the SkypeKit runtime...
		// Note that most of the Skype methods - including static methods and GetVersionString - will
		// fail and/or throw an exception if invoked prior to successful initialization!
		mySkype = new Skype();
		myClientConfiguration = new ClientConfiguration();
		myClientConfiguration.setTcpTransport(IP_ADDR, PORT_NUM);
		myClientConfiguration.setCertificate(pathName);
		myListeners = new Listeners(this);
		myConsole.printf("%s: Instantiated Skype, ClientConfiguration, and Listeners instances...%n", myTutorialTag);

		mySkype.init(myClientConfiguration, myListeners);
		mySkype.start(); // You must invoke start --immediately-- after invoking init!
		
		myParseSkypeKitVersion = new ParseSkypeKitVersion(mySkype);
 		myConsole.printf("%s: Initialized MySkype instance - version = %s (%d.%d.%d)%n",
					myTutorialTag, myParseSkypeKitVersion.getVersionStr(),
					myParseSkypeKitVersion.getMajorVersion(),
					myParseSkypeKitVersion.getMinorVersion(),
					myParseSkypeKitVersion.getPatchVersion());
		
		
 		// Get the Account
		if ((myAccount = mySkype.getAccount(myAccountName)) == null) {
			myConsole.printf("%s: Could not get Account for %s!%n", myTutorialTag, myAccountName);
			myConsole.printf("%s: Session initialization failed!%n", myTutorialTag);
			return (false);
		}
		
		myConsole.printf("%s: Got Account for %s%n", myTutorialTag, myAccountName);
		myConsole.printf("%s: Initialized session!%n", myTutorialTag);

		return (true);
	}

	
	/**
	 * Tears down a tutorial session.
	 * <br /><br />
	 * Specifically, this involves:
	 * <ol>
	 *   <li>Un-registering the listeners</li>
	 *   <li>Disconnecting the transport</li>
	 *   <li>"Closing" our Skype instance, which terminates the SkypeKit runtime</li> 
	 * </ol> 
	 * 
	 * @see Listeners#unRegisterAllListeners()
	 * 
	 * @since 1.0
	 */
	public void doTearDownSession() {

       	if (myListeners != null) {
       		myListeners.unRegisterAllListeners();
           	myListeners = null;
       	}
       	// Closing Skype also disconnects the transport
       	if (mySkype != null) {
       		mySkype.stop();
       		mySkype = null;
       	}

		myConsole.printf("%s: Tore down session instance%n", myTutorialTag);
	}

	/**
	 * Retrieves the current login status of this session's Account.
	 * @return
	 * 	Cached login status of this session's Account.
	 * 
	 * @see com.skype.tutorial.util.Listeners#onPropertyChange(com.skype.api.Account, com.skype.api.Account.Property, int, String)
	 * 
	 * @since 1.0
	 */
	public Account.Status getLoginStatus() {

		return(this.loginStatus); 
	}

	/**
	 * Establishes the login status of this session's Account.
	 * @param loginStatus
	 * 	Reported login status of this session's Account.
	 * 
	 * @see com.skype.tutorial.util.Listeners#onPropertyChange(com.skype.api.Account, com.skype.api.Account.Property, int, String)
	 * 
	 * @since 1.0
	 */
	public void setLoginStatus(Account.Status loginStatus) {

		this.loginStatus = loginStatus;
		
		MySession.myConsole.printf(myTutorialTag + ": " + "setting loginStatus to %s%n", loginStatus.toString());
		return;
	}

	/**
	 * Determines if an Account is signed in.
	 * <br /><br />
	 * Specifically, this involves examining the last cached value for
	 * the associated Account's status property. Essentially, <em>only</em>
	 * a current status of <code>Account.Status.LOGGED_IN</code> returns true
	 * <br /><br />
	 * Caching the status avoids having to query the DB. For mobile devices,
	 * WiFi-connected laptops running on battery power, and so forth this
	 * typically avoids expending battery charge to transmit the server request.
	 * 
	 * @return
	 * <ul>
	 *   <li>true: currently signed in</li>
	 *   <li>false: currently signed out <em>or</em> target Account is null</li>
	 * </ul>
	 * 
	 * @see com.skype.tutorial.util.SignInMgr#isLoggedIn(Account)
	 * 
	 * @since 1.0
	 */
	public boolean isLoggedIn() {

		if (this.loginStatus == Account.Status.LOGGED_IN) {
			return (true);
		}
		return (false);
	}

	/**
	 * Assigns active input and output devices from among those available.
	 * Notifies user regarding the name of the selected devices or whether the request failed.
	 * <em>Both</em> devices must exist for the request to succeed.
	 * 
	 * @param micIdx
	 * 	Index into the array of available recording devices of the requested input device.
	 * @param spkrIdx
	 * 	Index into the array of available playback devices of the requested output device.
	 * 
	 * @return
	 * <ul>
	 *   <li>true: success</li>
	 *   <li>false: failure</li>
	 * </ul>
	 * 
	 * @see com.skype.api.Skype#getAvailableRecordingDevices()
	 * @see com.skype.api.Skype#getAvailableOutputDevices()
	 * 
	 * @since 2.0
	 */
	public boolean setupAudioDevices(int micIdx, int spkrIdx) {
		boolean	passFail = true;	// Ever the optimist, assume success!
		
		Skype.GetAvailableRecordingDevicesResponse inputDevices = mySkype.getAvailableRecordingDevices();
		Skype.GetAvailableOutputDevicesResponse outputDevices = mySkype.getAvailableOutputDevices();

		if (micIdx > (inputDevices.handleList.length + 1)) {
			MySession.myConsole.printf("%s: Invalid mic device no. (%d) passed!%n", myTutorialTag, micIdx);
			passFail = false;
		}

		if (spkrIdx > (outputDevices.handleList.length + 1)) {
			MySession.myConsole.printf("%s: Invalid speaker device no. (%d) passed!%n", myTutorialTag, spkrIdx);
			passFail = false;
		}
		
		if (passFail) {
			MySession.myConsole.printf("%s: Setting mic to %s (%s)%n",
					myTutorialTag, inputDevices.nameList[micIdx], inputDevices.productIdList[micIdx]);
			MySession.myConsole.printf("%s: Setting speakers to %s  (%s)%n",
					myTutorialTag, outputDevices.nameList[spkrIdx], outputDevices.productIdList[spkrIdx]);
			mySkype.selectSoundDevices(inputDevices.handleList[micIdx],
					outputDevices.handleList[spkrIdx], outputDevices.handleList[spkrIdx]);
			mySkype.setSpeakerVolume(100);
		}

		return (passFail);
	}

	/**
	 * Normalizes a phone number and indicates that operation's success/failure.
	 * Used by Tutorial 8 and Tutorial 11.
	 * <br /><br />
	 * Determines the country code dialing prefix through {@link com.skype.api.Skype#getIsoCountryInfo()}
	 * by matching the default Locale country with an entry in the
	 * {@link com.skype.api.Skype.GetIsoCountryInfoResponse#countryCodeList}.
	 * Writes a message to the console indicating success/failure reason.
	 * 
	 * @param pstn
	 * 	Phone number to normalize.
	 * 
	 * @return
	 *   The normalization result, which includes:
	 *   <ul>
	 *     <li>an Enum instance detailing success/failure reason.</li>
	 *     <li>the normalized string (success) or error message string (failure)</li>
	 *   </ul>
	 * 
	 * @see com.skype.api.Skype#normalizePstnWithCountry(String, int)
	 * @see com.skype.api.Skype#getIsoCountryInfo()
	 * 
	 * @since 1.0
	 */
	public Skype.NormalizeIdentityResponse getNormalizationStr(String pstn) {
		Skype.NormalizeIdentityResponse nrmlResponseReturn = mySkype.new NormalizeIdentityResponse();
		
		Skype.GetIsoCountryInfoResponse isoInfo = mySkype.getIsoCountryInfo();
		int availCountryCodes = isoInfo.countryCodeList.length;
		int isoInfoIdx;
		String ourCountryCode = Locale.getDefault().getCountry();
		for (isoInfoIdx = 0; isoInfoIdx < availCountryCodes; isoInfoIdx++) {
			if (ourCountryCode.equalsIgnoreCase(isoInfo.countryCodeList[isoInfoIdx])) {
				break;
			}
		}
		if (isoInfoIdx >= availCountryCodes) {
			nrmlResponseReturn.result = Skype.NormalizeResult.IDENTITY_EMPTY; // Anything but IDENTITY_OK...
			nrmlResponseReturn.normalized = "Couldn't match Locale!";
			myConsole.printf("%s: Error! Couldn't match Locale %s in Skype.getIsoCountryInfo results%n",
					myTutorialTag, ourCountryCode);
			return (nrmlResponseReturn);
		}
		myConsole.printf("%n%s ISOInfo match (%d of %d):%n\tCode: %s%n\tDialExample: %s%n\tName: %s%n\tPrefix: %s%nLocale: %s%n%n",
				myTutorialTag, (isoInfoIdx + 1),
				mySkype.getIsoCountryInfo().countryCodeList.length,
				mySkype.getIsoCountryInfo().countryCodeList[isoInfoIdx],
				mySkype.getIsoCountryInfo().countryDialExampleList[isoInfoIdx],
				mySkype.getIsoCountryInfo().countryNameList[isoInfoIdx],
				mySkype.getIsoCountryInfo().countryPrefixList[isoInfoIdx],
				Locale.getDefault().getCountry());
		
		Skype.NormalizePstnWithCountryResponse nrmlResponse =
			mySkype.normalizePstnWithCountry(pstn, isoInfo.countryPrefixList[isoInfoIdx]);

		switch (nrmlResponse.result) {
		case IDENTITY_OK:
			nrmlResponseReturn.normalized = nrmlResponse.normalized;
			break;
		case IDENTITY_EMPTY:
			nrmlResponseReturn.normalized = "Identity input was empty";
			break;
		case IDENTITY_TOO_LONG:
			nrmlResponseReturn.normalized = "Identity string too long";
			break;
		case IDENTITY_CONTAINS_INVALID_CHAR:
			nrmlResponseReturn.normalized = "Invalid character(s) found in identity string";
			break;
		case PSTN_NUMBER_TOO_SHORT:
			nrmlResponseReturn.normalized = "PSTN number too short";
			break;
		case PSTN_NUMBER_HAS_INVALID_PREFIX:
			nrmlResponseReturn.normalized = "Invalid character(s) found in PSTN prefix";
			break;
		case SKYPENAME_STARTS_WITH_NONALPHA :
			nrmlResponseReturn.normalized = "Skype Name string starts with non-alphanumeric character";
			break;
		case SKYPENAME_SHORTER_THAN_6_CHARS:
			nrmlResponseReturn.normalized = "Skype Name too short";
			break;
		default:
			nrmlResponseReturn.normalized = "Cannot determine Skype.NORMALIZATION ?!?";
			break;
		}

		if (nrmlResponse.result != Skype.NormalizeResult.IDENTITY_OK) {
			myConsole.printf("%s: Error! Raw PSTN: %s - Normalized PSTN: %s%n",
									myTutorialTag, pstn, nrmlResponseReturn.normalized);
		}
		else {
			myConsole.printf("%s: Raw PSTN: %s / Normalized PSTN: %s%n",
									myTutorialTag, pstn, nrmlResponseReturn.normalized);
		}
		
		nrmlResponseReturn.result = nrmlResponse.result;
		return nrmlResponseReturn;
	}

	
	/*
 	 **
	 * Translates an APP2APP_STREAMS type to a displayable string.
	 * Used by Tutorial 11.
	 * 
	 * @param listType
	 * 	APP2APP_STREAMS enum to translate.
	 * 
	 * @return
	 *   A string representation of the enum value, or "unknown stream type" if not recognized.
	 * 
	 * @see com.skype.api.Skype.APP2APP_STREAMS
	 * 
	 * @since 2.0
	 *
	 */
	public String streamListType(Skype.App2AppStreams listType) {
		String listTypeAsText;
		
	    switch (listType) {
	        case ALL_STREAMS:
	        	listTypeAsText = "all streams";
	        	break;
	        case SENDING_STREAMS:
	        	listTypeAsText = "sending stream";
	        	break;
	        case RECEIVED_STREAMS:
	        	listTypeAsText = "receiving stream";
	        	break;
	        default:
	        	listTypeAsText = "unknown stream type";
	        	break;
	    }
	    return (listTypeAsText);
	}
}
