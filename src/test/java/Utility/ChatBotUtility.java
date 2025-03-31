package Utility;

import java.util.HashMap;
import java.util.Map;

public class ChatBotUtility {
	private static Map<String, String> responses;
	static{
		 responses = new HashMap<>();
	        responses.put("hi", "Hello!");
	        responses.put("how are you?", "Great to see you here!");
	        responses.put("what are you doing?", "I'm here to assist you.");
	    }
	 public static String getResponse(String question) 
	    {
	    	String answer=responses.getOrDefault(question.toLowerCase(),"Sorry, I don't understand.");
	        return(answer);
	    }
	}
   

