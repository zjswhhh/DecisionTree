package dt.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import dt.util.ArraySet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;


/**
 * Implementation of the decision-tree learning algorithm in AIMA Fig 18.5.
 * This is based on ID3 (AIMA p. 758).
 */
public class DecisionTreeLearner extends AbstractDecisionTreeLearner {
	
	/**
	 * Construct and return a new DecisionTreeLearner for the given Problem.
	 */
	public DecisionTreeLearner(Problem problem) {
		super(problem);
	}
	
	/**
	 * Main recursive decision-tree learning (ID3) method.  
	 */
	@Override
	protected DecisionTree learn(Set<Example> examples, List<Variable> attributes, Set<Example> parent_examples) {
		if (examples.isEmpty()) {
			return new DecisionTree(pluralityValue(parent_examples));
		}
		else if (!uniqueOutputValue(examples).equals("null")) {
			return new DecisionTree(uniqueOutputValue(examples));
		}
		else if (attributes.isEmpty()) {
			return new DecisionTree(pluralityValue(examples));
		}
		else {
			Variable A = mostImportantVariable(attributes, examples);
			DecisionTree tree = new DecisionTree(A);
			for(String vk:A.domain){
				Set<Example> exs = examplesWithValueForAttribute(examples, A, vk);
				
				List<Variable> remainAttribute = new ArrayList<Variable>();
				for (Variable var: attributes) {
					if (!var.equals(A)) {
						remainAttribute.add(var);
					}
				}
				
				DecisionTree subtree = learn(exs, remainAttribute, examples);
				tree.children.add(subtree);
			}	
			return tree;
		}
	}
	
	/**
	 * Returns the most common output value among a set of Examples,
	 * breaking ties randomly.
	 * I don't do the random part yet.
	 */
	@Override
	protected String pluralityValue(Set<Example> examples) {
		HashMap<String, Integer> cnt = new HashMap<String, Integer>();
		for(Example e:examples){
			String output = e.getOutputValue();
			if(!cnt.containsKey(output)){
				cnt.put(output, 1);
			}
			else{
				cnt.put(output, cnt.get(output)+1);
			}
		}
		
		String mostCommon = null;
		int maxCnt = 0;
		for(String str:cnt.keySet()){
			if(cnt.get(str)>maxCnt){
				maxCnt = cnt.get(str);
				mostCommon = str;
			}
		}
		return mostCommon;
	}
	
	/**
	 * Returns the single unique output value among the given examples
	 * is there is only one, otherwise null.
	 */
	@Override
	protected String uniqueOutputValue(Set<Example> examples) {
		Iterator<Example> iter = examples.iterator();
	    
	    String output = iter.next().getOutputValue();
	    while(iter.hasNext()){
	    	if(!output.equals(iter.next().getOutputValue())){
	    		return "null";
	    	}
	    }
	    return output;
	}
	
	//
	// Utility methods required by the AbstractDecisionTreeLearner
	//

	/**
	 * Return the subset of the given examples for which Variable a has value vk.
	 */
	@Override
	protected Set<Example> examplesWithValueForAttribute(Set<Example> examples, Variable a, String vk) {
	    Set<Example> result = new ArraySet<Example>();
	    for(Example e:examples){
	    	if(e.getInputValue(a).equals(vk)){
	    		result.add(e);
	    	}
	    }
	    return result;
	}
	
	/**
	 * Return the number of the given examples for which Variable a has value vk.
	 */
	@Override
	protected int countExamplesWithValueForAttribute(Set<Example> examples, Variable a, String vk) {
		int result = 0;
		for (Example e : examples) {
			if (e.getInputValue(a).equals(vk)) {
				result += 1;
			}
		}
		return result;
		
	}

	/**
	 * Return the number of the given examples for which the output has value vk.
	 */
	@Override
	protected int countExamplesWithValueForOutput(Set<Example> examples, String vk) {
	    int result = 0;
	    for(Example e:examples){
	    	if(e.getOutputValue().equals(vk))
	    		result += 1;
	    }
	    return result; 
	}

}
