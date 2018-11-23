import java.util.HashMap;
import java.util.Map;

import org.jpl7.Query;
import org.jpl7.Term;

public class UseGorgiasPrologFromJava {
	public static void executeGorgias(String filename) {
		String prologQuery = "consult('" + filename + "').";
		Query q = new Query(prologQuery);
		if (q.hasNext()) {
			prologQuery = "prove([cook(friday,X)],Delta).";
			q = new Query(prologQuery);
			if (q.hasSolution()) {
				while (q.hasNext()) {
					Map<String, Term> result = q.next();
					String food = result.get("X").toString();
					System.out.print("You will cook: " +food+"#");
					System.out.print("------------------------#");
					Term delta = result.get("Delta");
					String deltaRulesExplanation = iterateDelta(delta);
					System.out.print("Because:#" + deltaRulesExplanation);
					System.out.print("==================================||");
				}
			} else {
				System.out.print("Query has no Solution#");

			}
		}
		System.out.println();
	}

	private final static HashMap<String, String> deltaRulesExplanation = new HashMap<String, String>();
	static {		
		deltaRulesExplanation.put("r1","On a simple day you cook something with meat.");
		deltaRulesExplanation.put("r2","Something special is on this day.");
		deltaRulesExplanation.put("r3","The day is in fasting period.");
		deltaRulesExplanation.put("r4","The day is in fasting period and is also a special day.");
		deltaRulesExplanation.put("r5","Because someone has an allergy on meat.");
		deltaRulesExplanation.put("r6","Because someone has an allergy on meat but not on molluscs.");

		deltaRulesExplanation.put("pr0","When you can, cook souvla rather than regular meat.");
		deltaRulesExplanation.put("pr1","You dont have a lot of money, so prefer to cook the cheaper 'regular' meat, rather than souvla.");
		deltaRulesExplanation.put("pr2","Always cook the cheaper meal if you dont have a lot of money.");

		deltaRulesExplanation.put("pr4","It is probably fasting period.");
		deltaRulesExplanation.put("pr3","It is probably fasting period.");

		deltaRulesExplanation.put("pr5","You can cook legumes and mollusks, so prefer mollusks, because they are better ;).");
		deltaRulesExplanation.put("pr6","You dont have a lot of money for mollusks, so prefer legumes :(.");

		deltaRulesExplanation.put("pr7","If you have enough money, you can cook mollusks.");

		deltaRulesExplanation.put("pr8","You can cook legumes and mollusks, so prefer legumes if you have an allergy and it is not a special occasion.");
		deltaRulesExplanation.put("pr9","If it is a special occasion and you are allergic to meat, cook mollusks.");

		deltaRulesExplanation.put("pr10","Always prefer the cheapest if it is not a special occasion.");
		deltaRulesExplanation.put("pr11","You can cook legumes and souvla but it is fasting period, so prefer legumes.");
	}

	public static String iterateDelta(Term delta) {
		StringBuilder deltaExplanation = new StringBuilder();
		if (delta != null & delta.isListPair())
			for (Term rule : delta.toTermArray()) {
				String ruleName = rule.toString().split("\\(")[0];
				if (deltaRulesExplanation.containsKey(ruleName)) {
					deltaExplanation.append(deltaRulesExplanation.get(ruleName) + "#");
				}
			}
		return deltaExplanation.toString();
	}

	public static void main(String[] args) {
		if (args.length >0)
			executeGorgias(args[0]);
	}
}
