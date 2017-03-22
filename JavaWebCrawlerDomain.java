import java.net.*;
import java.io.*;
import java.util.*;
class JavaWebCrawlerDomain{
	// get subdomain name
	public static String getDomainName(String url){
		if(url.length()!=0){
			try{
				URL u = new URL(url);
				String location = u.getHost();
				if(location.indexOf("forum")>-1){
					return " ";
				}
				String[] subs =  location.split("\\.");
				String domainName = "";
				int leng = subs.length;
				if(subs[leng-2].equals("co")){
					domainName = subs[leng-3]+"." +subs[leng-2]+"."+subs[leng-1];
				}else{
					domainName = subs[leng-2]+"."+subs[leng-1];
				}
				return domainName;
			}catch(Exception e){
				JavaWebCrawler.errorHandle("ERROR: Domain Name | "+url+" | " + e.getMessage());
				return "";
			}
		}else{
			return " ";
		}
		
	}
	public static void main(String[] args){
		System.out.print(getDomainName(args[0]));
	}
}