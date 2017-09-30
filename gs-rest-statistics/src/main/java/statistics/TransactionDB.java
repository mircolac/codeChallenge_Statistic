package statistics;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionDB {
	private static Logger log = LoggerFactory.getLogger(TransactionDB.class);

	private static ConcurrentNavigableMap<Long, ArrayList<Double>> map = new ConcurrentSkipListMap<>();
	private static final long ONE_MINUTE = 60000;
	private static DoubleSummaryStatistics stats = new DoubleSummaryStatistics();
	private static String statsAsString = new String();

	/*
	 * Retrieve the Transaction Object and - if the timestamp is too old, returns a
	 * "204" string to the controller - if the timestamp is recent, adds the
	 * timestamp and amount list to the map, (or if the timestamp already exists
	 * just add the 'amount' value to the timestamp existing key) Eventually, update
	 * the statistics and launch a timer to remove the value after 60 seconds.
	 */
	protected static String addToList(Transaction t) {
		double amount = t.getAmount();
		long timestamp = t.getTimestamp();

		ArrayList<Double> amountList = new ArrayList<>();
		long timeNow = Instant.now().toEpochMilli();

		log.info("Instant.now().toEpochMilli() is " + Instant.now().toEpochMilli());
		log.info("timestamp is " + Instant.now().toEpochMilli());

		if (timestamp < (timeNow - ONE_MINUTE)) {
			return "204";
		}
		synchronized (map) {
			if (map.containsKey(timestamp)) {
				amountList = map.get(timestamp);
				amountList.add(amount);
				map.put(timestamp, amountList);
			} else {
				amountList.add(amount);
				map.put(timestamp, amountList);
			}
			log.info("Added " + timestamp);
			updateStatistics();

			new Thread(() -> {
				flushAndCalculate(timestamp);
			}).start();
			return "201";
		}

	}

	/*
	 * This method create a timer to remove a value from the map, then updates the
	 * statistics
	 */
	protected synchronized static void flushAndCalculate(long timestamp) {
		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				synchronized (map) {
					log.info("Time out for " + timestamp + "-" + map.get(timestamp));
					map.remove(timestamp);
					updateStatistics();
				}
			}
		}, ONE_MINUTE);
	}

	/*
	 * This method updates the stats object
	 */
	protected static void updateStatistics() {
		ArrayList<ArrayList<Double>> list = new ArrayList<>(map.values());
		Stream<Double> stream = list.stream().flatMap(Collection::stream);
		List<Double> values = stream.collect(Collectors.toCollection(ArrayList::new));

		synchronized (stats) {
			stats = values.stream().collect(Collectors.summarizingDouble(Double::doubleValue));
			updateStatisticsString();
		}
	}

	/*
	 * I thought would be useful to have a ready statistics String to send to the
	 * client when requested. This method is called every time the stats objects
	 * changes, keeping the statistics String updated.
	 */
	protected static void updateStatisticsString() {
		String updated = "{\n";
		updated += "\"sum\": " + stats.getSum() + ",";
		updated += "\"avg\": " + stats.getAverage() + ",";
		updated += "\"max\": " + stats.getMax() + ",";
		updated += "\"min\": " + stats.getMin() + ",";
		updated += "\"count\": " + stats.getCount() + "\n}";
		synchronized (statsAsString) {
			statsAsString = updated;
		}
	}

	/*
	 * Returns the statistics string
	 */
	protected static String getStatistics() {
		if (map.isEmpty()) {
			return "{\r\n" + "\t\"sum\": 0,\"avg\": 0,\"max\": 0,\"min\": 0,\"count\": 0\r\n" + "}";
		}
		synchronized (statsAsString) {
			return statsAsString;
		}
	}

}
