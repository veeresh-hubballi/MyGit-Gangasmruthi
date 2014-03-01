package com.zappos.mailer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.zappos.bean.ProductAndUser;
import com.zappos.service.MailService;
import com.zappos.service.ZapposService;

public class MailExecuter {
	static Logger log = Logger.getLogger(MailExecuter.class.getName());
	
	public void execute(HashMap<String, ProductAndUser> hm) {

		String productJSON;
		for (Entry<String, ProductAndUser> entry : hm.entrySet()) {
			productJSON = ZapposService.retrieveProductStatus(entry.getKey());
			hm = ZapposService.parseProductJSON(productJSON, entry.getKey(), hm);
			sendEmails(hm);
		}
		deleteEntries(hm);
	}

	protected void sendEmails(HashMap<String, ProductAndUser> map) {

		log.info("Starting sendEmails ");
		for (Entry<String, ProductAndUser> e : map.entrySet()) {
			if (e.getValue().getPercentOff() != null) {
				int poff = Integer.parseInt(e.getValue().getPercentOff());
				if ((!e.getValue().getPercentOff().isEmpty())
						&& e.getValue().emailIds.size() > 0) {
					if (poff > 20) {
						MailService.sendMail(e.getValue());
					}
				}
			}
			log.info("Finishing sendEmails ");
		}
	}

	private void deleteEntries(HashMap<String, ProductAndUser> map) {
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			ProductAndUser tm = (ProductAndUser) pairs.getValue();
			if (Integer.parseInt(tm.getPercentOff()) > 20) {
				log.info("Removing entry for " + pairs.getKey());
				it.remove(); // avoids a ConcurrentModificationException
			}
		}
	}
}
