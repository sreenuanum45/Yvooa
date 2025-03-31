package Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.remote.RemoteWebDriver;

public class CookiesUtility {
    public void addNewCookie(RemoteWebDriver driver, String name, String value, String domain,
                             String path, Date expiryDate, boolean isSecure, boolean isHttpOnly, String sameSite)
    {
        Cookie c=new Cookie(name, value, domain, path, expiryDate, isSecure, isHttpOnly, sameSite);
        driver.manage().addCookie(c);
    }

    public void addNewCookieViaBuilder(RemoteWebDriver driver, String name, String value,
                                       String domain,String path,Date expiry,Boolean isSecure,Boolean isHttpOnly,String sameSite)
    {
        Cookie.Builder b=new Cookie.Builder(name,value);
        Cookie c=b.domain(domain).path(path).expiresOn(expiry).isSecure(isSecure)
                .isHttpOnly(isHttpOnly).sameSite(sameSite).build();

        driver.manage().addCookie(c);
    }
    public  void DeleteCookies(RemoteWebDriver driver){

        driver.manage().deleteAllCookies();
    }
    public int getCookiesCount(RemoteWebDriver driver) {
        int c = driver.manage().getCookies().size();
        return c;
    }

    public List<String> getCookiesList(RemoteWebDriver driver) {
        Set<Cookie> s = driver.manage().getCookies();
        List<Cookie> l = new ArrayList<Cookie>(s);
        List<String> names = new ArrayList<String>();

        for (Cookie c : l) {
            names.add(c.getName() + "  " + c.getDomain() + "   " + "   " + c.getExpiry());
        }
        return names;
    }
    public List<String>getTypeofCookies(RemoteWebDriver driver, String samedomain, String superdomain){
        Set<Cookie> s = driver.manage().getCookies();
        List<Cookie> l = new ArrayList<Cookie>(s);
        List<String> Types= new ArrayList<String>();
        for (Cookie c:l){
            if (c.isHttpOnly()){
                if (c.getExpiry()!=null){
                Types.add(c.getName()+"isHttpOnly session cookie");
                }
                else{
                    Types.add("isHttpOnly and persistance cookie and its expiry"+c.getExpiry());
                }
            }
            else if (c.isSecure()){
                if (c.getExpiry()!=null){
                    Types.add(c.getName()+"isHttps session cookie");
                }
                else{
                    Types.add("isHttps and persistance cookie and its expiry"+c.getExpiry());
                }
            } else if (c.getDomain().contains(samedomain)) {

                Types.add(c.getName()+" is Same-site cookie");
            } else if (c.getDomain().contains(superdomain)) {
                Types.add(c.getName()+" is Super-site cookie");
            }
            else {
               Types.add(c.getName()+" is third-party cookie and it came from "+c.getDomain());
            }

        }
    return Types;
    }



}
