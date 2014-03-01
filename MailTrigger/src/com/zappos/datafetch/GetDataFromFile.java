package com.zappos.datafetch;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import com.zappos.bean.ProductAndUser;

public class GetDataFromFile {
	static Logger log = Logger.getLogger(
			GetDataFromFile.class.getName());

	BufferedReader in = null;
	String line ;

	public GetDataFromFile() {
		try {
			in = new BufferedReader(new FileReader("customer.dat"));
		} catch (FileNotFoundException e) {
			log.severe("Exception ocurred"+e.getMessage());
		}
	}

	public HashMap<String, ProductAndUser> readUserInfo(
			HashMap<String, ProductAndUser> hm) {
		try {
			while ((line = in.readLine()) != null) {
				String parts[] = line.split("\t");
				log.info(parts[0]);
				if (hm.containsKey(parts[0])) {
					hm.get(parts[0]).getEmailIds().add(parts[1]);
				} else {
					ArrayList<String> eid = new ArrayList<String>();
					eid.add(parts[1]);
					if (parts[0] != null) {
						hm.put(parts[0], new ProductAndUser(eid));
						hm.get(parts[0]).setPid(parts[0]);
					}
				}
			}
		} catch (Exception e) {
			log.severe("Exception ocurred"+e.getMessage());
		} finally {
			try {
				in.close();
				PrintWriter pw = new PrintWriter("customer.dat");
				pw.close();
			} catch (Exception e) {
				log.severe("Exception ocurred"+e.getMessage());
			}
		}
		return hm;
	}

}
