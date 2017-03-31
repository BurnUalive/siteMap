package JavaPageCrawler;
import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;
class JavaWebCrawler{
	static ClassLoader loader = Thread.currentThread().getContextClassLoader();     
	// makeDirectories 
	public static boolean makeDirs(String path){
		Path projectPath = Paths.get(path);
		if(!Files.exists(projectPath)){
			try{
				Files.createDirectories(projectPath);
				printLog("Dirs created | " + path);
				return true;
			}catch(Exception e){
				errorHandle(e.getMessage());
				return false;
			}
		}else{
			printLog("dirs exists | " + path);
			return false;
		}
	}
	// write file
	public static boolean writeFile(String path,String data){
		if(!Files.exists(Paths.get(path))){
			try(OutputStream out = Files.newOutputStream(Paths.get(path))){
				out.write(data.getBytes());
				printLog(path +" created for data | " + data.toString());
				return true;
			}catch(Exception e){
				errorHandle(path +"," + data.toString()+ " | write file err | " +e.getMessage() ,true);
				return false;
			}
		}else{
			printLog(path + " exists");
			return false;
		}
	}
	// Error handling delegated to following function
	public static void errorHandle(int e){
		errorHandle(e,false,"");
	}
	public static void errorHandle(String errMsg){
		errorHandle(2,false,errMsg);
	}
	public static void errorHandle(String errMsg,boolean breakFromProgram){
		errorHandle(2,breakFromProgram,errMsg);
	}
	public static void errorHandle(int e,boolean breakFromProgram){
		errorHandle(e,breakFromProgram,"");
	}
	public static void errorHandle(int e,boolean breakFromProgram,String errMsg){
		JavaWebCrawlerSpider.ERROR = true;
		if(e==1){
			printLog("Argument error");
		}else if(e==2){
			printLog(errMsg);
		}
		if(breakFromProgram){
			printLog("Exiting");
			System.exit(0);
		}
	}
	
	// Print line in console
	public static void printLog(String str){
		System.out.println(str);
	}
	// add datat to existing file
	public static void appendToFile(String path,String data){
		try(OutputStream out = Files.newOutputStream(Paths.get(path),StandardOpenOption.APPEND)){
			out.write((data+System.getProperty("line.separator")).getBytes());
			printLog("Path , Data append | " + path+" , "+data);
		}catch(Exception e){
			errorHandle("File append err | " + e.getMessage());
		}
	}
	// Delete contents of a file
	public static void deleteFileContents(String path){
		try(OutputStream out = Files.newOutputStream(Paths.get(path))){
			out.write(" ".getBytes());
			printLog("Cleared file data | " + path);
		}catch(Exception e){
			errorHandle("File content delete err | " + e.getMessage());
		}
	}
	// read a file and convert to set
	public static Set<String> fileToSet(String path){
		Set<String> results = new HashSet<String>();
		try(InputStream in = Files.newInputStream(Paths.get(path))){
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String data = "";
		/*	int byteValue = in.read();
			char byteData = System.getProperty("line.separator"); 
			while(byteValue!=-1){
				byteData = (char)byteValue; 
				if(byteData!=System.getProperty("line.separator")){
					data+=(char)byteValue;
				}else{
					results.add(data);
					data = "";
				}
				byteValue = in.read();
			}*/
			printLog(br.readLine());
			while((data = br.readLine())!=null){
				results.add(data);
			}
			return results;
		}catch(Exception e){
			errorHandle("fileToSet err | " + e.getMessage());
			return null;
		}
	}
	// save a set to file
	public static void setToFile(Set<String> links,String path){
		deleteFileContents(path);
		List<String> linkSet = new ArrayList<String>(links);
		Collections.sort(linkSet);
		Iterator<String> it = linkSet.iterator();
		String link = "";
		while(it.hasNext()){
			link = it.next();
			appendToFile(path,link);
		}
	}
	// delete a file 
	public static boolean deleteFile(String path){
		try{
			return Files.deleteIfExists(Paths.get(path));
		}catch(Exception e){
			errorHandle("Delete file | " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	// copy filepublic static boolean
	public static void copyFile(String src,String dst){
		try{
			Files.copy(Paths.get(src),Paths.get(dst));
		}catch(Exception e){
			errorHandle("File Copy | " + e.getMessage());
		}
	}
	public static void moveFile(String src,String dst){
		try{
			Files.move(Paths.get(src),Paths.get(dst));
		}catch(Exception e){
			errorHandle("File Copy | " + e.getMessage());
		}
	}
	// properties file handle write
	public static void setProperty(String property,String val,String path){
		try{
			Properties prop = new Properties();
			InputStream in = loader.getResourceAsStream(path);
			prop.load(in);
			in.close();
			prop.setProperty(property,val);
			prop.store(new FileOutputStream(path),null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	// init property files
	public static void setProperties(Properties prop,String path){
		try{
			prop.store(new FileOutputStream(path),null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	// properties file read
	public static Properties getProperties(String path){
		try{
			Properties prop = new Properties();
			InputStream in = new FileInputStream(System.getProperty("user.dir")+"\\"+path);
			prop.load(in);
			return prop;
		}catch(Exception e){
			e.printStackTrace();
			errorHandle("ERROR: " + e.getMessage());
			return null;
		}
	}
}