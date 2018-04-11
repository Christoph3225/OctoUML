import org.junit.Test;

import plugin.ExtensionLoader;

public class ClassLoaderTest {
	
	//@Test
	public void func() {
		 ExtensionLoader<Object> exloader = new ExtensionLoader<Object>();  
		    Class somePlugin;
			try {
				somePlugin = (Class) exloader.LoadClass("/tmp/local-repository2/org/jsoup/jsoup/1.11.2/jsoup-1.11.2.jar", "org.jsoup.Connection", Object.class);
				System.out.println(somePlugin.getName());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
