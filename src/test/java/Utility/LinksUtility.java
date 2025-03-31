package Utility;

import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

public class LinksUtility {
	public void linksInPage(List<WebElement>links) {
		
		int totallinks=links.size();
		int correctlinks=0;
		int brokenlinks=0;
		int linkswithexception=0;
		int locallinks=0;
		int nohref=0;
		for(WebElement link:links) {
			//1 check the hef attribute there or not
			String hrefpath;
			try {
			if((hrefpath=link.getAttribute("href"))!=null) {

				//2 href addres start with http or https
				if(hrefpath.startsWith("http")||hrefpath.startsWith("https")) {
					//3 check the href address working or not
					try {
						URL u=new URL(hrefpath);
						HttpsURLConnection con=(HttpsURLConnection)u.openConnection();

						con.connect();
						//send the dummy request and respond
						if(con.getResponseCode()==200) {
							 correctlinks++;
						}
						else {
							brokenlinks++;
							System.out.println(hrefpath+"is btokenlink"+con.getResponseMessage());
						}
					
					}catch(Exception e) {
						linkswithexception++;
						System.out.println(hrefpath+"is raised"+e.getMessage());
					}
					
				}
				else {
					locallinks++;
				}
			}
			else {
				nohref++;
			}
		}catch(StaleElementReferenceException d) {
			// Custom error handling or logging
		    System.out.println("StaleElementReferenceException occurred: " + d.getMessage());
		    // Retry the action or take appropriate actions
		}
		}
		System.out.println("totallinks:"+totallinks);
		System.out.println("links with href:"+correctlinks);
		System.out.println("brokenlinks are:"+brokenlinks);
		System.out.println("linkswithexception raised"+linkswithexception);
		System.out.println("local links:"+locallinks);
		System.out.println("nohref"+nohref);
		


	}

	}


