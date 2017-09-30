package statistics;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class StatisticsController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/transaction", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<?> createUser(@RequestParam("timestamp") String param1, @RequestParam("amount") String param2) {
    	    log.info("info level log");
    	    log.info(param1);
    	    log.info(param2);
    	try {
    	    long timestamp = Long.parseLong(param1);
    	    double amount = Double.parseDouble(param2);
            String result = TransactionDB.addToList(new Transaction(timestamp, amount));

            log.info("result is "+result);
            if (result == "204") {
            	log.info("Returning " +HttpStatus.NO_CONTENT);
            	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            else return new ResponseEntity<>(HttpStatus.CREATED);
            
    	}
    	catch (NumberFormatException e) {
    		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    	}
    }

    
    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public String getStatistics() {
        return  String.format(TransactionDB.getStatistics());
    }
    
	
}