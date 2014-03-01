package com.zappos;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import com.zappos.bean.ProductAndUser;
import com.zappos.datafetch.GetDataFromFile;
import com.zappos.mailer.MailExecuter;

public class Controller extends TimerTask {
	static Logger log = Logger.getLogger(
            Controller.class.getName());
	public static HashMap<String, ProductAndUser> hm = new HashMap<String, ProductAndUser>();
	
	public static void main(String[] args) {

		Timer timer = new Timer();
		Calendar date = Calendar.getInstance();
		date.set(Calendar.DATE, Calendar.SUNDAY);
		date.set(Calendar.HOUR, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		// Schedule to run every Sunday in midnight
		timer.schedule(new Controller(), 0, 1000 * 60);

	}

	@Override
	public void run() {
		log.info("Zappo mailer triggered");
		GetDataFromFile get = new GetDataFromFile();
		hm = get.readUserInfo(hm);
		if(hm.size()>0){
		new MailExecuter().execute(hm);
		}
	}

}
