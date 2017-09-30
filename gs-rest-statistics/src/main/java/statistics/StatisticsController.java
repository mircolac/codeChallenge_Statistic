package statistics;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "/transactions", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> createTransaction(@RequestBody Map<String, Object> transaction) {
		log.info("Received " + transaction.toString());

		if (!transaction.containsKey("timestamp") || !transaction.containsKey("amount")) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		try {
			double amount = (Double) transaction.get("amount");
			log.info("Amount is " + amount);
			long timestamp = (long) transaction.get("timestamp");
			log.info("timestamp is  " + timestamp);

			String result = TransactionDB.addToList(new Transaction(timestamp, amount));

			log.info("result is " + result);
			if (result == "204") {
				log.info("Returning " + HttpStatus.NO_CONTENT);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else
				return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (ClassCastException e) {
			log.info("ClassCastException");
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@RequestMapping(value = "/statistics", method = RequestMethod.GET)
	public String getStatistics() {
		return String.format(TransactionDB.getStatistics());
	}
}