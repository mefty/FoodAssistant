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
					System.out.print("You will cook: " +food+"\r\n");
					System.out.print("------------------------\r\n");
					Term delta = result.get("Delta");
					String deltaRulesExplanation = iterateDelta(delta);
					System.out.print("Because:\r\n" + deltaRulesExplanation);
					System.out.print("==================================\r\n");
				}
			} else {
				System.out.print("Query has no Solution\r\n");

			}
		}
		System.out.println();
	}

	private final static HashMap<String, String> deltaRulesExplanation = new HashMap<String, String>();
	static {		
		deltaRulesExplanation.put("r1","Normally you cook meat.");
		deltaRulesExplanation.put("r2","It is a special occasion so you can cook souvla.");
		deltaRulesExplanation.put("r3","if it is fasting period you should make legumes.");
		deltaRulesExplanation.put("r4","if it is fasting period AND a special occasion you can cook molluscs.");
		deltaRulesExplanation.put("r5","allergyMeat.");
		deltaRulesExplanation.put("r6","allergyMeat,not(allergyMolluscs).");

		deltaRulesExplanation.put("pr0","when you can cook souvla than regular meat , prefer souvla.");
		deltaRulesExplanation.put("pr1","if you dont have money prefer to cook the cheaper 'regular' meat than souvla.");
		deltaRulesExplanation.put("pr2","always prefer to satisfy the pr1 than pr0(if the noMoney predicate is true, else go with the pr0).");

		deltaRulesExplanation.put("pr4","prefer to cook legumes than meat because if you have both options it means that is fasting period.");
		deltaRulesExplanation.put("pr3","if you can cook mollusks and souvla , prefer mollusks because it means is a fasting period.");

		deltaRulesExplanation.put("pr5","if you can cook legumes and mollusks prefer mollusks, they are better :p");
		deltaRulesExplanation.put("pr6","if you dont have money for mollusks, go back to the legumes.");

		deltaRulesExplanation.put("pr7","always prefer to check your money than cook mollusks without money,if you have money you will cook mollusks.");

		deltaRulesExplanation.put("pr8","if you can cook legumes and mollusks prefer mollusks prefer legumes if you have an allergy and is not a special occasion.");
		deltaRulesExplanation.put("pr9","if its a special occasion and you are allergic to meat do mollusks.");

		deltaRulesExplanation.put("pr10","always prefer the cheapest if it is not a special occasion.");
		deltaRulesExplanation.put("pr11","if you can do legumes and souvla and its fasting prefer legumes.");
	}

	public static String iterateDelta(Term delta) {
		StringBuilder deltaExplanation = new StringBuilder();
		if (delta != null & delta.isListPair())
			for (Term rule : delta.toTermArray()) {
				String ruleName = rule.toString().split("\\(")[0];
				if (deltaRulesExplanation.containsKey(ruleName)) {
					deltaExplanation.append(deltaRulesExplanation.get(ruleName) + "\r\n");
				}
			}
		return deltaExplanation.toString();
	}

	public static void main(String[] args) {
		if (args.length >0)
			executeGorgias(args[0]);
	}
}
