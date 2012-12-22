package de.zeltclan.tare.bukkitutils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FileFeedReader {
	
	private FileFeedReader() {
	}
	
	public static HashMap<String,String> checkUpdate(URL p_fileFeed, String p_version) {
		HashMap<String,String> result = new HashMap<String,String> ();
		try {
			InputStream input = p_fileFeed.openConnection().getInputStream();
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
			
			Node lastFile = document.getElementsByTagName("item").item(0);
			NodeList children = lastFile.getChildNodes();
			
			result.put("version", children.item(1).getTextContent().replaceAll("[a-zA-Z ]", ""));
			if (!needUpdate(p_version, result.get("version"))) {
				return result;
			}
			
			result.put("link", children.item(3).getTextContent());
			
			input.close();
			
			input = (new URL(result.get("link")).openConnection().getInputStream());
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line;
			
			while ((line = reader.readLine()) != null) {
				if (line.trim().startsWith("<li class=\"user-action user-action-download\">")) {
					result.put("jarLink", line.substring(line.indexOf("href=\"") + 6, line.lastIndexOf("\"")));
					break;
				}
			}
			
			reader.close();
			input.close();
		} catch (Exception e) {
			result.put("error", e.getMessage());
		}
		return result;
	}
	

	
	private static boolean needUpdate(String p_version_local, String p_version_online) {
		String[] temp = p_version_local.split("\\.");
		final int[] versionLocal = new int[temp.length];
		for (int i = 0; i < temp.length; i++) {
			versionLocal[i] = Integer.parseInt(temp[i]);
		}
		temp = p_version_online.split("\\.");
		final int[] versionOnline = new int[temp.length];
		for (int i = 0; i < temp.length; i++) {
			versionOnline[i] = Integer.parseInt(temp[i]);
		}
		if (versionLocal[0] < versionOnline[0]) {
			return true;
		} else if (versionLocal[0] == versionOnline[0] && versionLocal[1] < versionOnline[1]) {
			return true;
		} else if (versionLocal[1] == versionOnline[1] && versionLocal[2] < versionOnline[2]) {
			return true;
		}
		return false;
	}
}
