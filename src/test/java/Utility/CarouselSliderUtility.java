package Utility;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CarouselSliderUtility {
	public boolean AreSliderMovingAutomaticallyInSlider(WebElement slider) throws Throwable {
		String oldvalue = slider.getCssValue("transform");

		if (oldvalue.equals("none")) {
			oldvalue = slider.getCssValue("transform-style");

		}
		//verification
		if (oldvalue.contains("3d")) {
			Thread.sleep(5000);
			String newvalue = slider.getCssValue("transform");
			if (newvalue.equals("none")) {
				newvalue = slider.getCssValue("transform-style");
			}
			//compare
			if (oldvalue.equals(newvalue)) {
				return (false);
			} else {
				return (true);
			}
		}
		String z = slider.getCssValue("text-align");
		if(z!=null) {

			if (z.equals("left")) {
				System.out.println("left to right");
				return true;
			} else if (z.equals("right")) {
				System.out.println("right to left");
				return true;
			} else {
				System.out.println("center no moment");
				return false;
			}

		}
		//2d
		else {
			String[] s = oldvalue.split(",");
			s[4] = s[4].trim();
			s[5] = s[5].replace(")", "");
			float tx = Float.parseFloat(s[4]);
			float ty = Float.parseFloat(s[5]);
			if (tx == 0 && ty == 0) {
				return (false);
			} else {
				return (true); //rotation
			}
		}
	}







		

   public int getCountOfSlider(WebElement slider) 
{
	int count=0;
	if(slider.getTagName().equalsIgnoreCase("ol")||
			slider.getTagName().equalsIgnoreCase("ul"))

	{
		count=slider.findElements(By.xpath("child::li")).size();
		}
	else if(slider.getTagName().equalsIgnoreCase("div")){
		List<WebElement>temp1=slider.findElements(By.xpath("child::div"));//collect all web elements
		List<WebElement>temp2=new ArrayList<WebElement>();
		for(WebElement e:temp1) 
		{
			try {
				if(!e.getAttribute("class").contains("clone")) {
					if(e.getAttribute("data-clone").equals("false")) {
						temp2.add(e);
					}
				
				}
			}catch(Exception ex) {
				
				temp2.add(e);
			}
			
		}//close for loop
		count=temp2.size();
	}
	else {
		System.out.println("unknow tag");
		System.exit(0);
	}
	return(count);
	
	
}
	   public String getDelayBetweenSlidesMove(WebElement slider)
	{
	    String x=slider.getCssValue("transition");
	    String[] y=x.split(" "); //Separator as space
	    if(y[3].contains("ms"))
	    {
	    	
	         String z=y[3];
	         z=z.replaceAll("[m][s][,]?",""); //remove "ms," or "ms" if exists
	         float duration=Float.parseFloat(z); //type conversion from string to float
	         if(duration<=1) return(duration+" millisecond");
	         else return(duration+" milliseconds"); 
	    }
	    else
	    {
	    	String resultString = String.join(" ", y); // You can change the space to any separator you prefer

	         String z=y[3];
	         z=z.replaceAll("[s][,]?",""); //remove "s," or "s" if exists
	         float duration=Float.parseFloat(z); //type conversion from string to float
	         if(duration<=1) return(duration+" second");
	         else return(duration+" seconds"); 
	    }
	}
	   public String getMovingDirectionOfSlider(WebElement slider) {
		   String value=slider.getCssValue("transform");
		   if(value.contains("3d")) {
			   String s=slider.getAttribute("style");
			   if(s.contains("rotateZ(-")) {
				   return("front to back rotation");
				   }
			   else if(s.contains("rotateZ(")) {
				   return("back to front rotation");
			   }
			   else if(s.contains("rotateX(-")) {
				   return("top to bottom rotation");
				   }
			   else if(s.contains("rotateX(")) {
				   return("bottom to  top rotation");
				   }
			   else if(s.contains("rotateY(-")) {
				   return("right to left rotation");
				   }
			   else if(s.contains("rotateY(")) {
				   return("left to right  rotation");
				   }
			   else {
				   return("not matching any specific rotation condition");
			   }
		   }
		   else {
			   String[]k=value.split(",");
			   k[4]=k[4].trim();
			   k[5]=k[5].replace(")", "");
			   float tx=Float.parseFloat(k[4]);
			   float ty=Float.parseFloat(k[5]);
			   if(tx<0&&ty==0) {
				   return("sliders moving  right to left");
			   }
			   else if(tx>0&&ty==0) {
				   return("sliders moving left to right ");
			   }
			   else if(ty>0&&tx==0) {
				   return("slider moving top to bottom");
			   }
             else if(ty>0&&tx==0) {
            	 return("slider moving bottom to top");
			   }
             else {
            	 return("not moving");
             }
			   
			   
		   }
		   
	   }
	   public String getslideMovingStyle(WebElement slider)
	    {
	        String x=slider.getCssValue("transition");
	        String[] y=x.split(" "); //space as seperator
	        y[2]=y[2].trim(); //remove leading and trailing spaces
	        if(y[2].equalsIgnoreCase("ease-in"))  return("Slow start");
	        else if(y[2].equalsIgnoreCase("ease-out")) return("Slow end");
	        else return("Slow start and slow end");
	    }
	   public  String getSliderMoveDuration(WebElement slider) throws Exception {
		   String x=slider.getCssValue("transition");
		  
		   
		   String []y=x.split(" ");
		   y[1]=y[1].trim();
		   if(y[1].contains("ms"))
		   {
			   String z=y[1];
			   z=z.replace("ms", "");
			   float duration=Float.parseFloat(z);
			   if(duration<=1)
				   return(duration+"mili seconds");
			   else
				   return(duration+"milli second");
		   }
		   else {
			   String z=y[1];
			   z=z.replace("s","");
			   float duration=Float.parseFloat(z);
			   if(duration<=1)
				   return(duration+" seconds");
			   else
				   return(duration+" second");
			   
		   }
	   }
	    public String getTypeOfSlider(WebElement slider) 
		{
			   String value=slider.getCssValue("transform");
			   if(value.equals("none")) {
				    value=slider.getCssValue("transform-style");
			   }
			   //validation
			   if(value.contains("3d")) {
				   return("3d slider");
			   }
			   else {
				   return("2d slider");
			   }}
	   
		
	   }
   
	


