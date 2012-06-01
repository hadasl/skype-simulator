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

import com.skype.api.Account;

import util.MySession;

/**
 * Collect common sign-in/sign-out fields as members and encapsulate common code as methods.
 * 
 * @author Andrea Drane
 * 
 * @since 1.0
 */
public class SignInMgr {
	/**
	 * Info/Debug console output message prefix/identifier tag.
	 * Corresponds to class name.
	 * 
	 * @since 1.0
	 */
    public static final String MY_CLASS_TAG = "SignInMgr";

    /**
  	 * Request polling interval (milliseconds).
  	 * 
  	 * @since 1.0
  	 */
    public static final int DELAY_INTERVAL = 1000;   // Equivalent to 1 second.

    /**
  	 * Request polling limit (iterations).
  	 * Results in a maximum total delay of <code>DELAY_CNT * DELAY_INTERVAL</code>
  	 * <em>milliseconds</em> before giving up and failing.
  	 * 
  	 * @since 1.0
  	 */
    public static final int DELAY_CNT = 45;
    
    /**
  	 * Delay interval prior to logout (milliseconds).
  	 * <br /><br />
  	 * Timing issue w/ some early versions of SkypeKit runtime - "immediate" logout
  	 * frequently causes the runtime to reflect an erroneous logout reason of
  	 * Account.LOGOUTREASON.APP_ID_FAILURE, so wait a few seconds... 
  	 * 
  	 * @since 1.0
  	 */
    public static final int LOGOUT_DELAY = (1 * 1000);   // Equivalent to 1 second.

	/**
	 * Common SkypeKit tutorial login processing.
	 * <ul>
	 *   <li>populates the session's Account instance</li>
	 *   <li>writes message to the console indicating success/failure/timeout</li>
	 *   <li>writes stack trace if I/O error setting up the transport!</li>
	 * </ul>
	 * 
	 * @param myTutorialTag
	 * 	Invoker's {@link #MY_CLASS_TAG}.
	 * @param mySession
	 *	Partially initialized session instance providing access to this sessions's Skype object.
	 * 
	 * @return
	 *   <ul>
	 * 	   <li>true: success; {@link com.skype.tutorial.util.MySession#myAccount} populated</li>
	 *	   <li>false: failure</li>
	 *   </ul>
	 * 
	 * @since 1.0
	 */
	public boolean Login(String myTutorialTag, MySession mySession, String myAccountPword) {

		if (mySession.isLoggedIn()) {
			// Already logged in...
			MySession.myConsole.printf("%s: %s already logged in! (IP Addr %s:%d)%n",
								myTutorialTag, mySession.myAccountName,
								MySession.IP_ADDR, MySession.PORT_NUM);
			return (true);
		}

		// Issue login request
		MySession.myConsole.printf("%s: Issuing login request%n", myTutorialTag);
		mySession.myAccount.loginWithPassword(myAccountPword, false, true);
			
		// Loop until AccountListener shows we are logged in or time-out...
		MySession.myConsole.printf("%s: Waiting for login to complete...%n", myTutorialTag);
		int i = 0;
		while ((i < SignInMgr.DELAY_CNT) && (!mySession.isLoggedIn())) {
			try {
				Thread.sleep(SignInMgr.DELAY_INTERVAL);
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return (false);
			}
			MySession.myConsole.printf("\t %d...%n", i++);
		}

		if (i < SignInMgr.DELAY_CNT) {
			// Successful Login
			MySession.myConsole.printf("%s: %s Logged In (IP Addr %s:%d)%n",
								myTutorialTag, mySession.myAccountName,
								MySession.IP_ADDR, MySession.PORT_NUM);
			return (true);
		}
		else {
			MySession.myConsole.printf("%s: Login timed out for %s! (IP Addr %s:%d)%n",
								myTutorialTag, mySession.myAccountName,
								MySession.IP_ADDR, MySession.PORT_NUM);
			return (false);
		}
	}
	

	/**
	 * Common SkypeKit tutorial logout processing.
	 * <ul>
	 *   <li>writes message to the console indicating success/failure/timeout</li>
	 *   <li>writes stack trace if I/O error setting up the transport!</li>
	 * </ul>
	 * 
	 * Delays the logout by a second or so to ensure that the SkypeKit runtime has fully settled in
	 * if the interval between sign-in and sign-out is really, really short (such as exists in
	 * {@link com.skype.tutorial.step1.Tutorial_1}). We don't want to see
  	 * Account.LOGOUTREASON.APP_ID_FAILURE unless our AppToken is truly bogus! 
	 * 
	 * @param myTutorialTag
	 * 	Invoker's {@link #MY_CLASS_TAG}.
	 * @param mySession
	 * 	Populated session object providing access to the invoker's
	 *  Skype and Account objects.
	 *  
	 * @see #LOGOUT_DELAY
	 * 
	 * @since 1.0
	 */
	public void Logout(String myTutorialTag, MySession mySession) {
		
		// Give the runtime a chance to catch its breath if it needs to...
		try {
			Thread.sleep(SignInMgr.LOGOUT_DELAY);
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!mySession.isLoggedIn()) {
			// Already logged out...
			MySession.myConsole.printf("%s: %s already logged out! (IP Addr %s:%d)%n",
								myTutorialTag, mySession.myAccountName,
								MySession.IP_ADDR, MySession.PORT_NUM);
			return;
		}

		// Issue logout request
		mySession.myAccount.logout(false);

		// Loop until AccountListener shows we are logged out or we time-out...
		MySession.myConsole.printf("%s: Waiting for logout to complete...%n", myTutorialTag);
		int i = 0;
/*
 * 		while ((i < SignInMgr.DELAY_CNT) && (SignInMgr.isLoggedIn(mySession.myAccount))) {
 */
		while ((i < SignInMgr.DELAY_CNT) && (mySession.isLoggedIn())) {
			try {
				Thread.sleep(SignInMgr.DELAY_INTERVAL);
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block#
				e.printStackTrace();
				return;
			}
			MySession.myConsole.printf("\t%d...%n", i++);
		}

		if (i < SignInMgr.DELAY_CNT) {
			// Successful Logout
			MySession.myConsole.printf("%s: %s logged out (IP Addr %s:%d)%n",
								myTutorialTag, mySession.myAccountName,
								MySession.IP_ADDR, MySession.PORT_NUM);
		}
		else {
			MySession.myConsole.printf("%s: Logout timed out for %s! (IP Addr %s:%d)%n",
								myTutorialTag, mySession.myAccountName,
								MySession.IP_ADDR, MySession.PORT_NUM);
		}
	}


	/**
	 * <em>Dynamically</em> determines if an Account is signed in.
	 * <br /><br />
	 * Specifically, this involves querying the DB to determine if the
	 * Account's status property reflects Account.STATUS.LOGGED_IN.
	 * For mobile devices, such activity can adversely affect battery life. 
	 * 
	 * @param myAccount
	 *  The target Account. 
	 *  
	 * @return
	 * <ul>
	 *   <li>true: currently signed in</li>
	 *   <li>false: currently signed out <em>or</em> target Account is null</li>
	 * </ul>
	 * 
	 * @see com.skype.tutorial.util.MySession#isLoggedIn()
	 * 
	 * @since 1.0
	 */
	public static boolean isLoggedIn(Account myAccount) {

		MySession.myConsole.println("Dynamically determining if Account is logged in...");
		
		if (myAccount != null) {
			if (myAccount.getStatusWithProgress().status == Account.Status.LOGGED_IN) {
				return (true);
			}
		}
		return (false);
	}
}
