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
	public @ResponseBody ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) {
			log.info("Received " + transaction.toString());
			String result = TransactionDB.addToList(transaction);
			log.info("result is " + result);
			if (result == "204") {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else
				return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/statistics", method = RequestMethod.GET)
	public String getStatistics() {
		return String.format(TransactionDB.getStatistics());
	}
}