import java.util.HashMap;
import java.util.Map;

import org.jpl7.Query;
import org.jpl7.Term;

public class UseGorgiasPrologFromJava {
	public static void executeGorgias(String filename) {
		String prologQuery = "consult('" + filename + "').";
		Query q = new Query(prologQuery);
		if (q.hasNext()) {
			prologQuery = "prove([cook(X)],Delta).";
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
		deltaRulesExplanation.put("r2","Your day is a special one so you can cook souvla.");
		deltaRulesExplanation.put("r3","On a simple day you can cook legumes.");
		deltaRulesExplanation.put("r4","Your day is a special one so you can cook molluscs.");

		deltaRulesExplanation.put("pr5","If you have allergy on molluscs prefer legumes.");
		deltaRulesExplanation.put("pr6","You prefer legumes because you want to avoid meat");
		deltaRulesExplanation.put("pr7","You prefer legumes because you want to avoid meat (souvla).");
		deltaRulesExplanation.put("pr8","You prefer molluscs because you want to avoid meat.");
		deltaRulesExplanation.put("pr9","You prefer molluscs because you want to avoid meat (souvla).");

		deltaRulesExplanation.put("e1","You have money, so prefer souvla than plain meat.");
		deltaRulesExplanation.put("e2","You don't have money so just cook plain meat rather than souvla");
		deltaRulesExplanation.put("e3","You have money, so prefer molluscs than legumes.");
		deltaRulesExplanation.put("e4","You dont have money so cook legumes rather than molluscs");
		
		deltaRulesExplanation.put("c1","Always cook based on your budget betweeen meat and souvla.");
		deltaRulesExplanation.put("c2","Always cook based on your budget betweeen legumes and molluscs.");
		deltaRulesExplanation.put("c3","Even if you have money for molluscs prefer legumes due to your allergy.");

		deltaRulesExplanation.put("m1","If you like meat, prefer meat than legumes.");
		deltaRulesExplanation.put("m2","If you like meat, prefer meat than molluscs.");
		deltaRulesExplanation.put("m3","If you like meat, prefer souvla than legumes.");
		deltaRulesExplanation.put("m4","If you like meat, prefer souvla than molluscs.");
		
		deltaRulesExplanation.put("d1","If you try to avoid meat, prefer vegan options.");
		deltaRulesExplanation.put("d2","If you try to avoid meat, prefer vegan options.");
		deltaRulesExplanation.put("d3","If you try to avoid meat, prefer vegan options.");
		deltaRulesExplanation.put("d4","If you try to avoid meat, prefer vegan options.");
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
