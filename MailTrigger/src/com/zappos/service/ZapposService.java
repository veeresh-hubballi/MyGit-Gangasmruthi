package com.zappos.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zappos.bean.ProductAndUser;

public class ZapposService {

	static Logger log = Logger.getLogger(ZapposService.class.getName());

	public static String retrieveProductStatus(String pid) {
		StringBuilder sb = new StringBuilder();
		String line;
		BufferedReader rd = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(
					"http://api.zappos.com/Search?&filters={\"productId\":[\""
							+ pid
							+ "\"]}&key=67d92579a32ecef2694b74abfc00e0f26b10d623");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setAllowUserInteraction(false);
			if (conn.getResponseCode() != 200) {
				log.severe("Problem connecting zappos");
			} else {
				// buffer the response into a string
				rd = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				rd.close();
			} catch (IOException e) {
				log.severe("Problem occurred closing connection");
			}
			conn.disconnect();
		}
		return sb.toString();
	}

	/**
	 * Given a JSON object of the response after searching a product, return a
	 * list with the default images of the products in the response.
	 * 
	 * @param ImageJSON
	 * @return
	 * @return
	 */
	public static HashMap<String, ProductAndUser> parseProductJSON(
			String ProdJSON, String pid, HashMap<String, ProductAndUser> map) {
		JSONObject JSONresponse;
		String tempPoff = null;
		String tPoff = null;
		String turl = null;
		String tpname = null;
		String comparedUrl = null;
		String comparedPname = null;
		Integer toCompTmp = 0, toComp;
		try {
			JSONresponse = new JSONObject(ProdJSON);
			JSONArray JSONproducts = JSONresponse.getJSONArray("results");
			if (JSONproducts.length() < 1) {
				log.info("The product does not exist !");
			} else {
				for (int i = 0; i < JSONproducts.length(); i++) {
					tPoff = ((JSONObject) JSONproducts.get(i))
							.getString("percentOff");
					tempPoff = String.valueOf(tPoff.substring(0,
							tPoff.lastIndexOf('%')));
					turl = ((JSONObject) JSONproducts.get(i))
							.getString("productUrl");
					tpname = ((JSONObject) JSONproducts.get(i))
							.getString("productName");
					toComp = Integer.parseInt(tempPoff);
					if (toComp > toCompTmp) {
						toCompTmp = toComp;
						comparedPname = tpname;
						comparedUrl = turl;
					}
				}
				if (map.containsKey(pid)) {// add the email address to the
											// arraylist of map
					log.info("percent off in hashmap updated to " + toCompTmp);
					map.get(pid).setPoff(toCompTmp.toString());
					map.get(pid).setPname(comparedPname);
					map.get(pid).setPurl(comparedUrl);
					// map.get(pid).set(0, toCompTmp.toString());
				}
			}
			log.info(map.entrySet().toString());
		} catch (JSONException e) {
			log.severe("Exception ocurred" + e.getMessage());
		} catch (Exception e) {
			log.severe("Exception ocurred" + e.getMessage());
		}
		return map;
	}

}
