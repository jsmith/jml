[![Build Status](https://travis-ci.org/jacsmith21/jml.png?branch=master)](https://travis-ci.org/jacsmith21/jml)

# Java Machine Learning (JML)
A machine library built in pure Java. Built for the CS6735 programming assignment.

## Algorithms
- ID3 (& some C4.5)
- Naive Bayes
- AdaBoost (SAMME)
- Random Forest
- K-Nearest Neighbors
- K-Fold Cross Validation

## Data
5 UCI Machine Learning Repositories come built into the framework. 
* [Breast Cancer](http://archive.ics.uci.edu/ml/datasets/Breast+Cancer+Wisconsin+%28Diagnostic%29) 
* [Car](http://archive.ics.uci.edu/ml/datasets/Car+Evaluation) 
* [E. Coli](http://archive.ics.uci.edu/ml/datasets/Ecoli)  
* [Letter Recognition](http://archive.ics.uci.edu/ml/datasets/Letter+Recognition)  
* [Mushroom](http://archive.ics.uci.edu/ml/datasets/Mushroom)

## Usage
### Loading Data
```java
import ca.jacob.jml.Dataset;

import static ca.jacob.jml.Dataset.CONTINUOUS;
import static ca.jacob.jml.Dataset.DISCRETE;
import static ca.jacob.cs6735.DataUtil.*;

public class Main() {
	public static void main(String[] args)  {
		DataSet d = loadBreastCancerData(Main.class);
		d = loadCarData(Main.class);
        	d = loadEColiData(Main.class);
        	d = loadLetterData(Main.class);
        	d = loadMushroomData(Main.class);
		...
		String[][] data = loadYourOwnData();
		Matrix mat = new Matrix(data); // last column are labels
		d = new DataSet(mat, DISCRETE); // create dataset and initilize to DISCRETE or CONTINUOUS or mixture of the two
	}
}
```

### Training Model
```java
import ca.jacob.jml.Dataset;
import ca.jacob.jml.Model;
import ca.jacob.jml.KFold;
import ca.jacob.jml.bayes.NaiveBayes;
import ca.jacob.jml.ensemble.AdaBoost;
import ca.jacob.jml.math.distance.Euclidean;
import ca.jacob.jml.math.distance.Hamming;
import ca.jacob.jml.math.distribution.Gaussian;
import ca.jacob.jml.tree.ID3;
import ca.jacob.jml.ensemble.RandomForest;
import ca.jacob.jml.neighbors.KNN;

import static ca.jacob.cs6735.DataUtil.*;

public class Main() {
	public static void main(String[] args)  {
		DataSet d = loadBreastCancerData(Main.class);
		algorithms = new ArrayList<>();
		Algorithm a = new ID3();
		a = new NaiveBayes();
		a = new AdaBoost(new ID3(1), 100, 0.1); // decision stump w/ 100 leaners and 10% of samples each time 
		a = new AdaBoost(new NaiveBayes(), 100, 0.1);
		a = new RandomForest(new ID3(), 100, 0.1);
		a = new KNN(1, new Hamming()); // or Eucliden() for continuous data
		
		// Training
		Model m = a.fit(d);
		
		// Predicting
		Vector predictions = m.predict(d);
		double accuracy = m.accuracy(d);
		
		// Or KFold
		KFold k = new KFold(5); // 5-fold cross validation
		Report r = k.generateReport(a, d);
		accuracy = r.accuracy();
	}
}

```
