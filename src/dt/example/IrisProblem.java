package dt.example;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import dt.core.DecisionTree;
import dt.core.DecisionTreeLearner;
import dt.core.Domain;
import dt.core.Example;
import dt.core.Problem;
import dt.core.Variable;
import dt.core.YesNoDomain;

/**
 * Run and pass dataset filename on cmd-line.
 */
public class IrisProblem extends Problem {
	
	public IrisProblem() {
		super();
		// Input variables
		Domain sizeDomain = new  Domain("S", "MS", "L", "ML");
		this.inputs.add(new Variable("sepal_length", sizeDomain));
		this.inputs.add(new Variable("sepal_width", sizeDomain));
		this.inputs.add(new Variable("petal_length", sizeDomain));
		this.inputs.add(new Variable("petal_width", sizeDomain));
		// Output variable
		this.output = new Variable("IrisClass", new Domain("Iris Setosa", "Iris Versicolor", "Iris Virginica"));
	}
	
	public static void main(String[] args) throws IOException {
		Problem problem = new IrisProblem();
		problem.dump();
		Set<Example> examples = problem.readExamplesFromCSVFile(new File(args[0]));
		for (Example e : examples) {
			System.out.println(e);
		}
		DecisionTree tree = new DecisionTreeLearner(problem).learn(examples);
		tree.dump();
		tree.test(examples);
	}

}
