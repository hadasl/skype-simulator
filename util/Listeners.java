package util;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import com.skype.api.Account;
import com.skype.api.AccountListener;
import com.skype.api.Contact;
import com.skype.api.ContactGroup;
import com.skype.api.ContactGroupListener;
import com.skype.api.ContactListener;
import com.skype.api.ContactSearchListener;
import com.skype.api.Conversation;
import com.skype.api.ConversationListener;
import com.skype.api.Message;
import com.skype.api.MessageListener;
import com.skype.api.Participant;
import com.skype.api.ParticipantListener;
import com.skype.api.Skype;
import com.skype.api.SkypeListener;
import com.skype.api.Sms;
import com.skype.api.SmsListener;
import com.skype.api.TransferListener;
import com.skype.api.VideoListener;
import com.skype.api.VoicemailListener;
import com.skype.ipc.ConnectionListener;
import com.skype.util.Log;


/**
 * SkypeKit API Event Handlers: Java Tutorials.
 * 
 * @author Andrea Drane (based on skype.javawrapper.client.JavaWrapperListeners and
 * 		   ported/refactored from existing C++ tutorial code)
 * 
 * @since 1.0
 */
public class Listeners implements 
							AccountListener, 
							SkypeListener,
							ContactListener, 
							ContactGroupListener, 
							ContactSearchListener,
							ConversationListener, 
							MessageListener, 
							ParticipantListener,
							SmsListener, 
							TransferListener, 
							VideoListener, 
							VoicemailListener,
							ConnectionListener {
	/**
	 * Initialized session instance providing access to this sessions's Skype instance,
	 * Account instance, tutorial tag, and so forth
	 * 
	 * @since 1.0
	 */
    private MySession mySession;

    /**
	 * Whether a call is in progress: Part I.
	 * <br /><br />
	 * Holds a reference to the affected Conversation instance so it doesn't get garbage-collected
	 * in the middle of the call. Initialized to null by caller; set to
	 * non-null by Conversation onPropertyChange and ConversationList onChange handlers.
	 * 
	 * @see com.skype.api.Conversation
	 * @see com.skype.api.SkypeListener#onConversationListChange(com.skype.api.Skype, com.skype.api.Conversation, com.skype.api.Conversation.ListType, boolean)
	 * 
	 * @since 1.0
	 */
    Conversation activeConversation = null;

    /**
	 * Whether a call is in progress: Part II.
	 * <br /><br />
	 * Holds a reference to <em>Participants</em> of the affected Conversation instance so they
	 * don't get garbage-collected in the middle of the call. Initialized to null by caller; set to
	 * non-null by Conversation onPropertyChange and ConversationList onChange handlers.
	 * 
	 * @see com.skype.api.Conversation
	 * @see com.skype.api.SkypeListener#onConversationListChange(com.skype.api.Skype, com.skype.api.Conversation, com.skype.api.Conversation.ListType, boolean)
	 * 
	 * @since 1.0
	 */
    Participant[] activeConversationParticipants = null;

	public Listeners (MySession mySession) {
		this.mySession = mySession;
		MySession.myConsole.println("\tRegistering the listeners...");
		registerAllListeners();
	}
	
    public void registerAllListeners()
    {
        mySession.mySkype.registerConnectionListener(this);
        mySession.mySkype.registerSkypeListener(this);
        mySession.mySkype.registerAccountListener(this);
        mySession.mySkype.registerContactListener(this);
        mySession.mySkype.registerContactGroupListener(this);
        mySession.mySkype.registerContactSearchListener(this);
        mySession.mySkype.registerConversationListener(this);
        mySession.mySkype.registerMessageListener(this);
        mySession.mySkype.registerParticipantListener(this);
        mySession.mySkype.registerSmsListener(this);
        mySession.mySkype.registerTransferListener(this);
        mySession.mySkype.registerVideoListener(this);
        mySession.mySkype.registerVoicemailListener(this);
    }

    public void unRegisterAllListeners()
    {
        mySession.mySkype.unRegisterSkypeListener(this);
        mySession.mySkype.unRegisterAccountListener(this);
        mySession.mySkype.unRegisterContactListener(this);
        mySession.mySkype.unRegisterContactGroupListener(this);
        mySession.mySkype.unRegisterContactSearchListener(this);
        mySession.mySkype.unRegisterConversationListener(this);
        mySession.mySkype.unRegisterMessageListener(this);
        mySession.mySkype.unRegisterParticipantListener(this);
        mySession.mySkype.unRegisterSmsListener(this);
        mySession.mySkype.unRegisterTransferListener(this);
        mySession.mySkype.unRegisterVideoListener(this);
        mySession.mySkype.unRegisterVoicemailListener(this);
        mySession.mySkype.unRegisterConnectionListener(this);
    }

	/**
	 * VoicemailListener Override.
	 *
	 * @since 1.0
	 * 
	 * @see com.skype.api.VoicemailListener#onPropertyChange(com.skype.api.Voicemail, com.skype.api.Voicemail.Property, int, String)
	 */
	public void onPropertyChange(com.skype.api.Voicemail obj, com.skype.api.Voicemail.Property prop, int value, String svalue) {
	}

	/**
	 * VideoListener Override.
	 *
	 * @since 1.0
	 * 
	 * @see com.skype.api.VideoListener#onPropertyChange(com.skype.api.Video, com.skype.api.Video.Property, int, String)
	 */
	public void onPropertyChange(com.skype.api.Video obj, com.skype.api.Video.Property prop, int value, String svalue) {
	}

	/**
	 * VideoListener Override.
	 *
	 * @since 1.0
	 * 
	 * @see com.skype.api.VideoListener#onCaptureRequestCompleted(com.skype.api.Video, int, boolean, byte[], int, int)
	 */
	public void onCaptureRequestCompleted(com.skype.api.Video obj, int requestId, boolean isSuccessful,
			byte[] image, int width, int height) {
		Log.d("Tutorial", "onCaptureRequestCompleted(" + requestId + ", " + isSuccessful + ", image.length:" + image.length
			  + ", " + width + ", " + height + ")");

	}

	/**
	 * TransferListener Override.
	 *
	 * @since 1.0
	 * 
	 * @see com.skype.api.TransferListener#onPropertyChange(com.skype.api.Transfer, com.skype.api.Transfer.Property, int, String)
	 */
	public void onPropertyChange(com.skype.api.Transfer obj, com.skype.api.Transfer.Property prop, int value, String svalue) {
	}

	/**
	 * SmsListener Override: Tutorial Handler - Target analysis, pricing, and status.
	 * <ul>
	 *   <li>P_TARGET_STATUSES changes track analysis of target PSTN numbers, and
	 *       increment MySession.smsReadyToSend whenever a target becomes acceptable or non-routable.</li>
	 *   <li>P_PRICE changes enable display of individual and total pricing for the specified targets, once
	 *       <i>all</i> targets have become acceptable or non-routable.</li>
	 *   <li>P_STATUS changes track send and receipt of the SMS.</li>
	 * </ul>
	 *
	 * @param obj
	 * 	The affected Sms.
	 * @param prop
	 * 	The Sms property that triggered this event.
	 * @param value
	 * 	Ignored.
	 * @param svalue
	 * 	Ignored.
	 * 
	 * @since 3.0
	 * 
	 * @see com.skype.api.SmsListener#onPropertyChange(com.skype.api.Sms, com.skype.api.Sms.Property., int, String)
	 */
	public void onPropertyChange(com.skype.api.Sms obj, com.skype.api.Sms.Property prop, int value, String svalue) {
	// As target statuses for all targets are kept in one Sms property (P_TARGET_STATUSES),
	// we need to loop through all targets in the list separately.
		int i;
		int price = 0;
		int totalPrice = 0;
		double actualPrice = 0.0;
		double precision= 0.0;
		String currency = "USD";
		String targetNumber;
		String[] targetList;
		Sms.TargetStatus targetStatus;

		if (prop == Sms.Property.P_TARGET_STATUSES) {
			targetList = obj.getTargetNumbers().split(" ");
			for (i = 0; i < targetList.length; i++) {
				targetNumber = targetList[i];

				targetStatus = obj.getTargetStatus(targetNumber);
				MySession.myConsole.printf("%s - %s%n", targetNumber, targetStatus.toString());

				if ((targetStatus == Sms.TargetStatus.TARGET_ACCEPTABLE) || (targetStatus == Sms.TargetStatus.TARGET_NOT_ROUTABLE)) {
					mySession.smsReadyToSend++;
				}

				if (targetStatus == Sms.TargetStatus.TARGET_NOT_ROUTABLE) {
					// At this point, we already know that the SMS delivery will fail since the target
					// status was TARGET_NOT_ROUTABLE. So, it looks like there is no reason to even attempt
					// sending it. However, at this point we still don't know what was the reason behind
					// "no routabilty" - The Sms FAILURE_REASON property will only get set during attempt
					// to send it.
					//
					// One common cause of failure would be an attempt to send SMS messages from account with
					// zero balance. That too would trigger TARGET_NOT_ROUTABLE. But your UI would not know
					// this - as while in composing state, the SMS failure reason will remain empty.
					//
					// Thus, it actually makes sense -not- to stop the sending attempt here, post the SMS,
					// knowing that it will fail, and then retrieve the actual error cause.
					MySession.myConsole.printf("WARNING: unroutable target %s!%n", targetNumber);
				}
			}
		}
		else if (prop == Sms.Property.P_PRICE) {
			targetList = obj.getTargetNumbers().split(" ");
			for (i = 0; i < targetList.length; i++) {

				// The price property as well, is common to all targets (targets may have different prices).
				// The total price we can look at via Sms.getPrice but to get to target specific prices
				// we will need to loop through targets and use Sms.getTargetPrice.
				// The price information is kept in three properties:
				// 1. price - integer, i.e. 123
				// 2. precision - integer, i.e. 2
				// 3. currency - string - i.e. "EUR"
				//
				// Calculate the actual price value as : price / 10^precision
				// For example, 123 / 10^2 = 01.23 EUR
				// Note that the price value for any given target (and the total
				// price of the SMS) will only become available once the target
				// has reached TARGET_ACCEPTABLE status. Until then the price
				// will remain at its default value: -1
				//
				// Other changes to an SMS instance can trigger "bogus" price change events, such as
				// adding a target (status changes to TARGET_UNDEFINED) and analyzing a target
				// (status changes to TARGET_ANALYZING), which result in the price being (re-)set to a
				// default initial value, for example, -1. So, we'll ignore price changes when the corresponding
				// status is other than TARGET_NOT_ROUTABLE and TARGET_ACCEPTABLE. 

				targetNumber = targetList[i];
				targetStatus = obj.getTargetStatus(targetNumber);
				if ((targetStatus != Sms.TargetStatus.TARGET_NOT_ROUTABLE) && (targetStatus != Sms.TargetStatus.TARGET_ACCEPTABLE)) {
/*
 					String s;
					if (targetNumber == null) {
						s = "null";
					}
					else if (targetNumber.length() == 0) {
						s = "empty string";
					}
					else {
						s = targetNumber;
					}
					MySession.myConsole.printf("Price change before final status change for \"%s\" - current price= %d; current status = %s%n",
						s, obj.getTargetPrice(targetNumber), targetStatus.toString());
*/
					return;
				}

				// Getting target price
				price = obj.getTargetPrice(targetNumber);
				if ((price < 0) || (price == Integer.MAX_VALUE)) {
					MySession.myConsole.printf("SMS price for %s is still unknown!%n", targetNumber);
					return;
				}
				totalPrice += price;

				precision = obj.getPricePrecision();
				currency = obj.getPriceCurrency();

				actualPrice = price / StrictMath.pow(10, precision);
				MySession.myConsole.printf("SMS Price for %s resolves to %.3f %s%n", 
						targetNumber, actualPrice, currency);
			}

			// Display total price for this SMS (we can reuse currency and precision)
			actualPrice = totalPrice / StrictMath.pow(10, precision);
			MySession.myConsole.printf("Total SMS price is now %.3f %s%n", 
					actualPrice, currency);
		}
		else if (prop == Sms.Property.P_STATUS) {
			// The sender is assumed to have set each of smsWasSent, smsHasFailed, and smsWasDelivered to
			// false prior to sending the SMS. Note the implication that only one SMS will ever be pending at
			// any one time.
			Sms.Status status = obj.getStatus();
			switch (status) {
			case SENT_TO_SERVER:
				MySession.myConsole.println(mySession.myTutorialTag + ": Sms - SENT_TO_SERVER");
				mySession.smsWasSent = true;
				break;
			case DELIVERED:
				MySession.myConsole.println(mySession.myTutorialTag + ": Sms - DELIVERED");
				mySession.smsWasDelivered = true;
				break;
			case SOME_TARGETS_FAILED:
				MySession.myConsole.println(mySession.myTutorialTag + ": Sms - SOME_TARGETS_FAILED");
				mySession.smsWasSent = true;
				break;
			case FAILED:
				MySession.myConsole.println(mySession.myTutorialTag + ": Sms - FAILED");
				mySession.smsHasFailed = true;
				break;
			case COMPOSING:
			case SENDING_TO_SERVER:
				break;
			default:
				 /*
				  * MySession.myConsole.println(mySession.myTutorialTag + ": Sms - Unexpected send/delivery status " + status.toString());
				  */
				 break;
			}
		}
		else {
		  // Logging class / property / new value
		  /*
		   * MySession.myConsole.println(mySession.myTutorialTag + ": Sms - Ignoring property " + prop.toString());
		   */
		}
	}

	/**
	 * ParticipantListener Override: Tutorial Handler - Sound Levels and Voice Status.
	 * <ul>
	 *   <li>Tutorial 5 - sound level property changes.</li>
	 *   <li>Tutorial 6/7 - voice status property changes.</li>
	 * </ul>
	 *
	 * @param obj
	 * 	The affected Participant.
	 * @param prop
	 * 	The Participant property that triggered this event.
	 * @param value
	 * 	Ignored.
	 * 
	 * @since 1.0
	 * 
	 * @see com.skype.api.ParticipantListener#onPropertyChange(com.skype.api.Participant, com.skype.api.Participant.Property, int, String)
	 */
	public void onPropertyChange(com.skype.api.Participant obj, com.skype.api.Participant.Property prop, int value, String svalue) {
		Participant affectedParticipant = (Participant)obj;

		if (prop == Participant.Property.P_SOUND_LEVEL)
		{
		//	MySession.myConsole.printf("Sound level changed to %d for %s%n",
			//		affectedParticipant.getSoundLevel(),
				//	affectedParticipant.getIdentity());
		}
		else if (prop == Participant.Property.P_VOICE_STATUS)
		{
			Participant.VoiceStatus voiceStatus = affectedParticipant.getVoiceStatus();
			MySession.myConsole.printf("Voice status changed to %s for %s%n",
					voiceStatus.toString(),
					affectedParticipant.getIdentity());
		}
    }

	/**
	 * ParticipantListener Override.
	 *
	 * @since 1.0
	 * 
	 * @see com.skype.api.ParticipantListener#onIncomingDtmf(com.skype.api.Participant, com.skype.api.Participant.Dtmf)
	 */
	public void onIncomingDtmf(com.skype.api.Participant obj, Participant.Dtmf dtmf) {
	}

	/**
	 * MessageListener Override.
	 *
	 * @since 1.0
	 * 
	 * @see com.skype.api.MessageListener#onPropertyChange(com.skype.api.Message, com.skype.api.Message.Property, int, String)
	 */
	public void onPropertyChange(com.skype.api.Message obj, com.skype.api.Message.Property prop, int value, String svalue) {
	}

	/**
	 * ConversationListener Override: Tutorial Handler - Conversation "Live Status".
	 * <br /><br />
	 * If it's <em>not</em> a "live status" change, ignore it. Otherwise:
	 * <ul>
	 *   <li>display changes to our live status</li>
	 *   <li>handle answering calls for us</li>
	 * </ul>
	 * 
	 * Tutorial 6/7 - Looks for:
	 * <ul>
	 *   <li>RINGING_FOR_ME so we can pick up the call</li>
	 *   <li>IM_LIVE to indicate that a call is in progress</li>
	 *   <li>RECENTLY_LIVE/NONE to indicate that a call has ended</li>
	 *  </ul>
	 * 
	 * @param obj
	 * 	The affected Conversation.
	 * @param prop
	 * 	The Conversation property that triggered this event.
	 * @param value
	 * 	Ignored.
	 * 
	 * @since 1.0
	 * 
	 * @see com.skype.api.ConversationListener#onPropertyChange(com.skype.api.Conversation, com.skype.api.Conversation.Property, int, String)
	 */
	public void onPropertyChange(com.skype.api.Conversation obj, com.skype.api.Conversation.Property prop, int value, String svalue) {

		if (prop == Conversation.Property.P_LOCAL_LIVE_STATUS) {
 			Conversation affectedConversation = (Conversation)obj;
			Conversation.LocalLiveStatus liveStatus = affectedConversation.getLocalLiveStatus();
			MySession.myConsole.printf("%s: Live status changed to %s%n",
								mySession.myTutorialTag, liveStatus.toString());
			switch (liveStatus) {
			case RINGING_FOR_ME:
				MySession.myConsole.println("Conversation: RING RING...");
				if (doPickUpCall()) {
					MySession.myConsole.println("Conversation: Call answered!");
					activeConversation = affectedConversation;
					activeConversationParticipants = affectedConversation.getParticipants(Conversation.ParticipantFilter.ALL);
					mySession.callActive = true;
				}
				break;
			case RECENTLY_LIVE:
			case NONE:
				MySession.myConsole.println("Conv: Call has ended/never started.");
				activeConversation = null;
				activeConversationParticipants = null;
				mySession.callActive = false;
				break;
			case IM_LIVE:
				MySession.myConsole.println("Conv: Live session is up!");
				break;
			default:
				/*
				 * MySession.myConsole.println(mySession.myTutorialTag + ": Conv - Ignoring LiveStatus " + liveStatus.toString());
				 */
				break;
			}
        }
	}

	/**
	 * ConversationListener Override.
	 *
	 * @since 1.0
	 * 
	 * @see com.skype.api.ConversationListener#onParticipantListChange(com.skype.api.Conversation)
	 */
	public void onParticipantListChange(com.skype.api.Conversation obj) {
	}

	/**
	 * ConversationListener Override.
	 *
	 * This event gets triggered during sync for <i>every</i> message in <i>every</i> chat that we are a part of.
     * Running the tutorials as a "real" Skype user can result in your spamming those chats with this
     * automated message <i>every</i> time you start up, so use a test ID and don't make that ID part of any real chats.
     * 
	 * @since 1.0
	 * 
	 * @see com.skype.api.ConversationListener#onMessage(com.skype.api.Conversation, com.skype.api.Message)
	 */
	public void onMessage(com.skype.api.Conversation obj, Message message) {

		Message.Type msgType = message.getType();
		if (msgType == Message.Type.POSTED_TEXT) {
		   String conversationID = message.getConvoGuid();
		   Conversation conversation =
			   mySession.mySkype.getConversationByIdentity(conversationID, false);
            String msgAuthor = message.getAuthor();
            String msgBody = message.getBodyXml();
            if (!msgAuthor.equals(mySession.myAccountName)) {
                // Get timestamp -- it's in seconds, and the Date constructor needs milliseconds!
            	Integer msgTimeStamp = new Integer(message.getTimestamp());
                Date dateTimeStamp = new Date((msgTimeStamp.longValue() * 1000L));
            	DateFormat targetDateFmt = DateFormat.getDateTimeInstance();
            	MySession.myConsole.printf("%s: [%s] %s posted message%n%s%n",
            			mySession.myTutorialTag, targetDateFmt.format(dateTimeStamp), msgAuthor, msgBody);
            	Calendar targetDate = Calendar.getInstance();
            }
        }
		else {
			/*
			 * MySession.myConsole.printf("%s: Ignoring ConversationListener.onMessage of type %s%n", mySession.myTutorialTag, msgType.toString());
			 */
		}	
	}

	/**
	 * ConversationListener Override.
	 *
	 * @since 1.0
	 * 
	 * @see com.skype.api.ConversationListener#onSpawnConference(com.skype.api.Conversation, com.skype.api.Conversation)
	 */
	public void onSpawnConference(com.skype.api.Conversation obj, Conversation spawned) {
        Log.d("Tutorial", "onSpawnConference(" + spawned + ")");
	}

	/**
	 * ContactSearchListener Override.
	 *
	 * @since 1.0
	 * 
	 * @see com.skype.api.ContactSearchListener#onPropertyChange(com.skype.api.ContactSearch, com.skype.api.ContactSearch.Property, int, String)
	 */
	public void onPropertyChange(com.skype.api.ContactSearch obj, com.skype.api.ContactSearch.Property prop, int value, String svalue) {
	}

	/**
	 * ContactSearchListener Override.
	 *
	 * @since 1.0
	 * 
	 * @see com.skype.api.ContactSearchListener#onNewResult(com.skype.api.ContactSearch, com.skype.api.Contact, int)
	 */
	public void onNewResult(com.skype.api.ContactSearch obj, Contact contact, int rankValue) {
        Log.d("Tutorial", "onNewResult(" + contact + ", " + rankValue + ")");
	}

	/**
	 * ContactGroupListener Override.
	 *
	 * @since 1.0
	 * 
	 * @see com.skype.api.ContactGroupListener#onPropertyChange(com.skype.api.ContactGroup, com.skype.api.ContactGroup.Property, int, String)
	 */
	public void onPropertyChange(com.skype.api.ContactGroup obj, com.skype.api.ContactGroup.Property prop, int value, String svalue) {
	}

	/**
	 * ContactGroup Override.
	 *
	 * @since 1.0
	 * 
	 * @see com.skype.api.ContactGroupListener#onChangeConversation(com.skype.api.ContactGroup, com.skype.api.Conversation)
	 */
	public void onChangeConversation(com.skype.api.ContactGroup obj, Conversation conversation) {
        Log.d("Tutorial", "onChangeConversation(" + conversation + ")");
	}

	/**
	 * ContactGroupListener Override: Tutorial Handler - ContactGroup TYPE.
	 * <br /><br />
	 * This handler fires for all ContactGroups. If it's <em>not</em>
	 * a pending authorization request, log and ignore it.
	 * <br /><br />
     * Tutorial 9 - Process authorization requests
     * 
	 * @since 1.0
	 * 
	 * @see com.skype.api.ContactGroupListener#onChange(com.skype.api.ContactGroup, com.skype.api.Contact)
	 */
	public void onChange(com.skype.api.ContactGroup obj, Contact contact) {
		
		String contactSkypeName = contact.getSkypeName(); // Find out who it's from
		String contactDisplayName = contact.getDisplayName();
		
		ContactGroup waitingAuth =
				mySession.mySkype.getHardwiredContactGroup(ContactGroup.Type.CONTACTS_WAITING_MY_AUTHORIZATION);
		
		if (waitingAuth.getContacts().length == 0) {
/*
 * 			MySession.myConsole.printf("%s: Ignoring ContactGroup change for %s (%s); no Contacts awaiting authorization %n", mySession.myTutorialTag, contactSkypeName, contactDisplayName);
 */
			return;
		}
		
		String authRequestText = contact.getReceivedAuthRequest();	// Get any intro text...
   		if ((authRequestText == null) || (authRequestText.length() == 0)) {						// ...and default it if missing
   			authRequestText = "-- NO INTRODUCTORY TEXT --";
   		}
		MySession.myConsole.printf("%s: Authorization request from: %s (%s):%n\t%s%n",
				mySession.myTutorialTag, contactSkypeName, contactDisplayName, authRequestText);
		contact.setBuddyStatus(true, true);
/*
 * SetBuddyStatus should really return a boolean...
 * If and when it does, replace the above SetBuddyStatus invocation with the following...
   			if (contact.setBuddyStatus(true, true)) {
   				MySession.myConsole.printf("%s: %s is now authorized%n", mySession.myTutorialTag, contactSkypeName);
   			}
   			else {
   				MySession.myConsole.printf("%s: Authorization failed.%n", mySession.myTutorialTag);
   			}
*/
   		MySession.myConsole.printf("%s: %s is now authorized!%n", mySession.myTutorialTag, contactSkypeName);
	}

	/**
	 * ContactListener Override: Tutorial Handler - Availability.
	 * <br /><br />
     * Maps an availability property ENUM code to a text string, and writes that string to
     * the console. If it's <em>not</em> an availability change, ignore it.
     * <br /><br />
     * The implementation here follows the C++ pattern - a switch statement with cases for each
     * defined code. For Java 5.0, the Enum class' <code>toString</code> method eliminates the
     * need for the entire switch statement. For example, you could code the printf as:
     * <pre>
     *     MySession.myConsole.printf("%s: Availability of %s is now %s%n",
	 *                       mySession.myTutorialTag,
	 *                       affectedContact.getDisplayName(),
	 *                       availability.toString());
	 * </pre>
	 * Implementing this functionality as a switch statement, however,
	 * enables you to perform additional processing for specific cases.
     * 
	 * @param affectedContact
	 * 	The affected Contact.
	 * @param prop
	 * 	The Contact property that triggered this event.
	 * @param value
	 * 	Ignored.
	 * 
	 * @since 1.0
	 * 
	 * @see com.skype.api.ContactListener#onPropertyChange(com.skype.api.Contact, com.skype.api.Contact.Property, int, String)
	 */
	public void onPropertyChange(com.skype.api.Contact affectedContact, com.skype.api.Contact.Property prop, int value, String svalue) {

		if (prop == Contact.Property.P_AVAILABILITY)
		{
			String statusAsText;
			Contact.Availability availability = affectedContact.getAvailability();

			switch (availability)
			{
			case UNKNOWN:
				statusAsText = "UNKNOWN";
				break;
			case PENDINGAUTH:
				statusAsText = "PENDINGAUTH";
				break;
			case BLOCKED:
				statusAsText = "BLOCKED";
				break;
			case BLOCKED_SKYPEOUT:
				statusAsText = "BLOCKED_SKYPEOUT";
				break;
			case SKYPEOUT:
				statusAsText = "SKYPEOUT";
				break;
			case OFFLINE:
				statusAsText = "OFFLINE";
				break;
			case OFFLINE_BUT_VM_ABLE:
				statusAsText = "OFFLINE_BUT_VM_ABLE";
				break;
			case OFFLINE_BUT_CF_ABLE:
				statusAsText = "OFFLINE_BUT_CF_ABLE";
				break;
			case ONLINE:
				statusAsText = "ONLINE";
				break;
			case AWAY:
				statusAsText = "AWAY";
				break;
			case NOT_AVAILABLE:
				statusAsText = "NOT_AVAILABLE";
				break;
			case DO_NOT_DISTURB:
				statusAsText = "DO_NOT_DISTURB";
				break;
			case SKYPE_ME:
				statusAsText = "SKYPE_ME";
				break;
			case INVISIBLE:
				statusAsText = "INVISIBLE";
				break;
			case CONNECTING:
				statusAsText = "CONNECTING";
				break;
			case ONLINE_FROM_MOBILE:
				statusAsText = "ONLINE_FROM_MOBILE";
				break;
			case AWAY_FROM_MOBILE:
				statusAsText = "AWAY_FROM_MOBILE";
				break;
			case NOT_AVAILABLE_FROM_MOBILE:
				statusAsText = "NOT_AVAILABLE_FROM_MOBILE";
				break;
			case DO_NOT_DISTURB_FROM_MOBILE:
				statusAsText = "DO_NOT_DISTURB_FROM_MOBILE";
				break;
			case SKYPE_ME_FROM_MOBILE:
				statusAsText = "SKYPE_ME_FROM_MOBILE";
				break;
			default:
				statusAsText = "UNKNOWN";
				break;
			}
			
			MySession.myConsole.printf("%s: Availability of %s is now %s (%s)%n",
					mySession.myTutorialTag,
					affectedContact.getDisplayName(), statusAsText,
					availability.toString());
		}
	}
	
	/**
	 * SkypeListener Override.
	 *
	 * @since 1.0
	 * 
	 * @see com.skype.api.SkypeListener#onNewCustomContactGroup(com.skype.api.Skype, com.skype.api.ContactGroup)
	 */
	public void onNewCustomContactGroup(Skype obj, ContactGroup group) {
        Log.d("Tutorial", "onNewCustomContactGroup(" + group + ")");
	}

	/**
	 * SkypeListener Override.
	 *
	 * @since 1.0
	 * 
	 * @see com.skype.api.SkypeListener#onContactOnlineAppearance(com.skype.api.Skype, com.skype.api.Contact)
	 */
	public void onContactOnlineAppearance(Skype obj, Contact contact) {
        Log.d("Tutorial", "onContactOnlineAppearance(" + contact + ")");
	}

	/**
	 * SkypeListener Override.
	 *
	 * @since 1.0
	 * 
	 * @see com.skype.api.SkypeListener#onContactGoneOffline(com.skype.api.Skype, com.skype.api.Contact)
	 */
	public void onContactGoneOffline(Skype obj, Contact contact) {
	}

	/**
	 * SkypeListener Override: Tutorial Handler - Conversation ListType.
	 * <br /><br />
	 * If it's <em>not</em> a LIVE_CONVERSATIONS change, ignore it; if it's not a
	 * type we're interested in, simply write it to the console.
	 * <ul>
	 *   <li>Tutorial 5 - Looks for RINGING_FOR_ME so we can join in.</li>
	 *   <li>Tutorial 6 - Looks for IM_LIVE or RECENTLY_LIVE/NONE. Former case, indicates
	 *       that a call is in progress; latter case, indicates that a call has ended.</li>
	 * </ul>
	 *
	 * @param conversation
	 * 	The affected Conversation.
	 * @param type
	 * 	The Conversation list type that triggered this event.
	 * @param added
	 * 	Ignored.
	 * 
	 * @since 1.0
	 * 
	 */
	public void onConversationListChange(Skype obj, Conversation conversation, Conversation.ListType type, boolean added) {
		
		MySession.myConsole.printf("%s: ConversationListChange fired on: %s%n",
				mySession.myTutorialTag, conversation.getDisplayName());

		if (type == Conversation.ListType.LIVE_CONVERSATIONS) {
			Conversation.LocalLiveStatus liveStatus = conversation.getLocalLiveStatus();
			MySession.myConsole.printf("%s: Live status changed to %s%n",
								mySession.myTutorialTag, liveStatus.toString());
			switch (liveStatus) {
			case RINGING_FOR_ME:
				MySession.myConsole.println("ConversationList: RING RING...");
				if (doPickUpCall()) {
					MySession.myConsole.println("ConversationList: Call answered!");
					activeConversation = conversation;
					activeConversationParticipants = conversation.getParticipants(Conversation.ParticipantFilter.ALL);
					mySession.callActive = true;
				}
				break;
			case RECENTLY_LIVE:
			case NONE:
				MySession.myConsole.printf("%s: Call finished.%n", mySession.myTutorialTag);
				activeConversation = null;
				activeConversationParticipants = null;
				mySession.callActive = false;
				break;
			case IM_LIVE:
				MySession.myConsole.printf("%s: Live session is up.%n", mySession.myTutorialTag);
				break;
			default:
/*
 * 				MySession.myConsole.printf("%s: Ignoring Conversation status %s%n", mySession.myTutorialTag, liveStatus.toString());
 */
				break;
			}
		}
	}

	/**
	 * SkypeListener Override: Tutorial Handler - Posted chat message.
	 * <br /><br />
	 * If it's <em>not</em> a POSTED_TEXT message, ignore it.
	 *
	 * @param message
	 * 	The affected Message.
	 * @param changesInboxTimestamp
	 * 	Ignored.
	 * @param supersedesHistoryMessage
	 * 	Ignored.
	 * @param conversation
	 * 	The affected Conversation.
	 * 
	 * @since 1.0
	 * 
	 * @see com.skype.api.SkypeListener#onMessage(com.skype.api.Skype, com.skype.api.Message, boolean, com.skype.api.Message, com.skype.api.Conversation)
	 */
	public void onMessage(Skype obj, Message message,
			boolean changesInboxTimestamp, Message supersedesHistoryMessage, Conversation conversation) {
		Message.Type msgType = message.getType();

        if (msgType == Message.Type.POSTED_TEXT) {
            String msgAuthor = message.getAuthor();
            String msgBody = message.getBodyXml();
            if (!msgAuthor.equals(mySession.myAccountName)) {
                // Get timestamp -- it's in seconds, and the Date constructor needs milliseconds!
            	Integer msgTimeStamp = new Integer(message.getTimestamp());
                Date dateTimeStamp = new Date((msgTimeStamp.longValue() * 1000L));
            	DateFormat targetDateFmt = DateFormat.getDateTimeInstance();
            	MySession.myConsole.printf("%s: [%s] %s posted message%n\t%s%n",
            			mySession.myTutorialTag, targetDateFmt.format(dateTimeStamp), msgAuthor, msgBody);
            	Calendar targetDate = Calendar.getInstance();
            	conversation.postText((targetDateFmt.format(targetDate.getTime()) + ": This is an automated reply"), false);
            }
        }
		else {
/*
 * 			MySession.myConsole.printf("%s: Ignoring SkypeListener.onMessage of type %s%n", mySession.myTutorialTag, msgType.toString());
 */
		}	
	}

	/**
	 * SkypeListener Override.
	 *
	 * @since 1.0
	 * 
	 * @see com.skype.api.SkypeListener#onAvailableVideoDeviceListChange(Skype)
	 */
	public void onAvailableVideoDeviceListChange(Skype obj) {
        Log.d("Tutorial", "onAvailableVideoDeviceListChange()");
	}

	/**
	 * SkypeListener Override.
	 *
	 * @since 1.0
	 * 
	 * @see com.skype.api.SkypeListener#onAvailableDeviceListChange(Skype)
	 */
	public void onAvailableDeviceListChange(Skype obj) {
        Log.d("Tutorial", "onAvailableDeviceListChange()");
	}

	/**
	 * SkypeListener Override.
	 *
	 * @since 1.0
	 * 
	 * @see com.skype.api.SkypeListener#onNrgLevelsChange(Skype)
	 */
	public void onNrgLevelsChange(Skype obj) {
        Log.d("Tutorial", "onNrgLevelsChange()");
	}

	/**
	 * SkypeListener Override.
	 *
	 * Fires on P2P connection failure during account login, which (by default)
	 * attempts direct connection to the network, then falls back to connection via proxy.
	 * 
	 * @since 1.0
	 * 
	 * @see com.skype.api.SkypeListener#onProxyAuthFailure(Skype, Skype.ProxyType)
	 */
	public void onProxyAuthFailure(Skype obj, Skype.ProxyType type) {
        Log.d("Tutorial", "onProxyAuthFailure()");
	}

	/**
	 * SkypeListener Override.
	 *
	 * 
	 * @see com.skype.api.SkypeListener#onH264Activated(Skype)
	 */
	@Override
    public void onH264Activated(Skype obj) {
        Log.d("Tutorial", "onH264Activated()");
    }

	
	/**
	 * SkypeListener Override: Tutorial Handler - Datagrams.
	 * <br /><br />
	 * In the context of Tutorial 11 datagrams, this event fires when we receive a datagram.
	 * Writes the name of the affected stream and the actual datagram content to the console.
	 *
	 * @param appname
	 * 	The name of the application associated with this datagram connection.
	 * @param stream
	 * 	The name of the affected stream.
	 * @param data
	 * 	The payload of this datagram.
	 * 
	 * @since 1.0
	 * 
	 * see com.skype.api.SkypeListener#onApp2AppDatagram(com.skype.api.Skype, java.lang.String, java.lang.String, byte[])
	 */
	public void onApp2AppDatagram(Skype obj, String appname, String stream, byte[] data) {
		String dataStr = new String(data); // Java 5.0 and up can use data.toString() directly

		MySession.myConsole.printf("Got datagram in stream %s for app %s: %s%n", stream, appname, dataStr);
	}

	/**
	 * SkypeListener Override: Tutorial Handler - Call Quality Information.
	 * <br /><br />
	 * New for SkypeKit 4.1 (Java2Wrapper).
	 *
	 * @param object
	 * 	The...
	 * @param testType
	 * 	The...
	 * @param testResult
	 * 	The...
	 * @param withUser
	 * 	The...
	 * @param details
	 * 	The...
	 * @param xmlDetails
	 * 	The...
	 * 
	 * @since 2.0
	 * 
	 * see com.skype.api.SkypeListener#onQualityTestResult(Skype, Skype.QualityTestType, Skype.QualityTestResult,
			String, String, String)
	 */
	public void onQualityTestResult(Skype object, Skype.QualityTestType testType, Skype.QualityTestResult testResult,
			String withUser, String details, String xmlDetails) {
		
		MySession.myConsole.println("Got Quality Test Result");
	}


 	/**
	 * SkypeListener Override: Tutorial Handler - App2App Stream List.
	 * <br /><br />
	 * In the context of Tutorial 11 datagrams, this event fires when:
	 * <ol>
	 *   <li>Connection is established between two app2app applications. That is, when
	 *	     both parties have an app up with the same name and <em>both</em> used App2AppConnect
	 *	     In that case, both parties get this event, with listType ALL_STREAMS</li>
	 *   <li>When a datagram is sent, the sender will get this event with listType SENDING_STREAMS
	 *	     Receiver of the datagram will get onApp2AppDatagram event instead.</li>
	 *   <li>When the remote party drops app2app connection, the local user will get
	 *	     onApp2AppStreamListChange with listType ALL_STREAMS and streams.length zero,
	 *	     which is useful for detecting remote drops.</li>
	 * </ol>
	 * 
	 * @param appname
	 * 	The name of the application associated with this datagram connection.
	 * @param listType
	 * 	The type of the affected stream(s) - sending, receiving, or "all"
	 * @param streams
	 * 	The names of the affected stream(s). In the context of Tutorial 11 datagrams,
	 *  we assume that there are only 2 participants and so th enumber of streams should be either
	 *  0 (zero; remote shutdown) or 1 (one; initiated/datagram sent).
	 * 
	 * @since 1.0
	 * 
	 * @see com.skype.api.SkypeListener#onApp2AppStreamListChange(Skype, String, Skype.App2AppStreams, String[], int[])
	 */
	public void onApp2AppStreamListChange(Skype object, String appname, Skype.App2AppStreams listType, String[] streams, int[] receivedSizes) {
		int streamsCount = streams.length;
		
		if (streamsCount != 0) {
	        // Normally the streamCount in this event should be either 1 or 0.
	        // More streams are possible when there are more than 2 connected
	        // participants running the same application. For purposes of this
	        // example, we will assume that there are only 2 participants.
			int i;
	        for (i = 0; i < streamsCount; i++) {
	            MySession.myConsole.printf("onApp2AppStreamListChange: %s %s %s%n",
	            		mySession.streamListType(listType), appname, streams[i]);
	            mySession.streamName = streams[i];	// We're assuming only one stream!
	            									// If two or more, last one wins!
	        }
	        if (!mySession.appConnected) {
	            mySession.appConnected = true;
	            MySession.myConsole.printf("You can now type and send datagrams to remote instance.%n");
	        }
	    }
	    else if (listType == Skype.App2AppStreams.ALL_STREAMS) {
	            // Remote side dropped connection.
	            MySession.myConsole.printf("No more app2app streams.%n");
	            mySession.streamName = "";
	    }
	}
	
	/**
	 * AccountListener Override: Tutorial Handler - Account Properties.
	 * <br /><br />
	 * Status changes: Specifically looks for/handles login/logout status changes, and reports
	 * those changes to the associated MySession instance. Writes notice of <em>all</em>
	 * property changes to both the log and the console.
	 * <br /><br />
	 * Logout reasons: Writes reason for logout to both the log and the console. Useful for
	 * differentiating explicit logout by user (Account.LogoutReason.LOGOUT_CALLED) and
	 * forced logout by the SkypeKit runtime.
	 * <br /><br />
	 * Other property changes: Writes the name of the changed property to the console.
	 * 
	 * @param affectedAccount
	 * 	Ignored - always assumes <em>our</em> account, so references effected through
	 * 	<code>mySession.myAccount</code>.
	 * @param prop
	 * 	The Account property that triggered this event.
	 * @param value
	 * 	Ignored.
	 *
	 * @since 1.0
	 * 
	 * @see com.skype.api.AccountListener#onPropertyChange(com.skype.api.Account, com.skype.api.Account.Property, int, String)
	 */
	public void onPropertyChange(com.skype.api.Account affectedAccount, com.skype.api.Account.Property prop, int value, String svalue) {
		MySession.myConsole.println(mySession.myTutorialTag + ": " + "onPropertyChange - Account" + "; " + prop.toString());
 
	    if (prop == Account.Property.P_STATUS) {
			Account.Status accountStatus = affectedAccount.getStatus();
            mySession.setLoginStatus(accountStatus);
	        if (value == Account.Status.LOGGED_IN_VALUE) {
	    		MySession.myConsole.println(mySession.myTutorialTag + ": " + "Login complete." );
	        }
	        else if ((accountStatus == Account.Status.LOGGED_OUT) ||
	        		(accountStatus == Account.Status.LOGGED_OUT_AND_PWD_SAVED)) {
	    		MySession.myConsole.println(mySession.myTutorialTag + ": " + "Login in progress/Logout complete." );
	        }
	        else {
	        	String otherStatus = new String("Account Status = " + accountStatus.toString());
	        	MySession.myConsole.printf("%s: %s%n", mySession.myTutorialTag, otherStatus);
	        }
	    }
	    else if (prop == Account.Property.P_LOGOUT_REASON) {
 	    	Account.LogoutReason logoutReason = affectedAccount.getLogoutReason();
	    	MySession.myConsole.printf("%s: logoutreason = %s%n",
	    						mySession.myTutorialTag, logoutReason.toString());
	    }
	    else if (prop == Account.Property.P_SKYPEOUT_BALANCE) {
	    	affectedAccount.getSkypeoutBalance();
			double actualBalance = affectedAccount.getSkypeoutBalance() / StrictMath.pow(10, affectedAccount.getSkypeoutPrecision());
			MySession.myConsole.printf("New SkypeOut balance is: %.3f %s%n", 
					actualBalance, affectedAccount.getSkypeoutBalanceCurrency());
	    }
	    else {
/*
 * 	    	MySession.myConsole.printf("%s: value = %d; svalue = %s%n", mySession.myTutorialTag, value, svalue);
 */
	    }
	}


	/**
	 * ConnectionListener Override - Disconnect.
	 * <br /><br />
	 * New for SkypeKit 4.1 (Java2Wrapper).
	 *
	 * @since 2.0
	 * 
	 * see com.skype.api.ConnectionListener#sidOnDisconnected(String)
	 */
    public void sidOnDisconnected(String cause) {
    	MySession.myConsole.printf("%s: Disconnected; reason = %s%n",
				mySession.myTutorialTag, cause);
    }

    /**
	 * ConnectionListener Override - Connect.
	 * <br /><br />
	 * New for SkypeKit 4.1 (Java2Wrapper).
	 *
	 * @since 2.0
	 * 
	 * see com.skype.api.ConnectionListener#sidOnDisconnected(String)
	 */
    public void sidOnConnected() {
    	MySession.myConsole.printf("%s: Connected!%n",
				mySession.myTutorialTag);
    }

    /**
	 * ConnectionListener Override - Connect in progress.
	 * <br /><br />
	 * New for SkypeKit 4.1 (Java2Wrapper).
	 *
	 * @since 2.0
	 * 
	 * see com.skype.api.ConnectionListener#sidOnDisconnected(String)
	 */

    public void sidOnConnecting() {
    	MySession.myConsole.printf("%s: Connecting...%n",
				mySession.myTutorialTag);
    }

	/**
	 * Business logic for answering a call (Tutorial_5 - Find conversation to join).
	 * <br /><br />
	 * Since this method is invoked from the Conversation event handler
	 * {@link #onPropertyChange(com.skype.api.Conversation, com.skype.api.Conversation.Property, int, String)},
 	 * it's most convenient to place it here in the JavaTutorialListeners class.
	 * 
	 * @return
	 * <ul>
	 *   <li>true: call picked up</li>
	 *   <li>false: no call to pick up/call not answered/error</li>
	 * </ul>
	 * 
	 * @since 1.0
	 */
	public boolean doPickUpCall() {
		Conversation[] liveConversations = mySession.mySkype.getConversationList(Conversation.ListType.LIVE_CONVERSATIONS);
		if (liveConversations.length == 0) {
			MySession.myConsole.printf("%s: No live conversations to pick up!%n", mySession.myTutorialTag);
			return (false);
		}
		
		Conversation targetConversation = liveConversations[0];

		Participant[] callerList = targetConversation.getParticipants(Conversation.ParticipantFilter.OTHER_CONSUMERS);
		StringBuffer displayParticipantsStr = new StringBuffer();

		displayParticipantsStr.setLength(0);
		int i;
		int j = callerList.length;
		for (i = 0; i < j; i++) {
			displayParticipantsStr.append((" " + callerList[i].getIdentity()));
		}

		Conversation.LocalLiveStatus liveStatus = targetConversation.getLocalLiveStatus();
		switch (liveStatus) {
		case RINGING_FOR_ME:
			MySession.myConsole.println("RING RING...");
			MySession.myConsole.printf("Incoming call from: %s %n", displayParticipantsStr.toString());
			targetConversation.joinLiveSession(targetConversation.getJoinBlob());
			return (true);
			// break;
		case IM_LIVE:
			MySession.myConsole.printf("Another call is coming in from : %s %n", displayParticipantsStr.toString());
			MySession.myConsole.println("As we already have a live session up, we will reject it.");
			targetConversation.leaveLiveSession(true);
			break;
		default:
			/*
			 * MySession.myConsole.println(mySession.myTutorialTag + ": Ignoring LiveStatus " + liveStatus.toString());
			 */
			break;
		}

		return (false);
	}
}
