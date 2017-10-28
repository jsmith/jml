# CS6735 Programming Project

## Instructions
Conduct an experimental study on the following machine learning ca.jacob.cs6735.assignment.utils.algorithms: (1) ID3; (2) Adaboost on ID3; (3) Random Forest; (4) Naïve Bayes; (5) Adaboost on naïve Bayes; (6) K-nearest neighbors (kNN).

1. Implement the six ca.jacob.cs6735.assignment.utils.algorithms using Java.
<br>

2. Evaluate your implementation on the datasets in data.zip (downloadable from course website) using 10 times 5-fold cross-validation, and report the average accuracy and standard deviation. All datasets are for UCI machine learning repository. You can check the detailed descriptions from the following link:
http://www.ics.uci.edu/~mlearn/MLRepository.html
<br>
For breast cancer data see: http://archive.ics.uci.edu/ml/datasets/Breast+Cancer+Wisconsin+%28Diagnostic%29
For car data see: http://archive.ics.uci.edu/ml/datasets/Car+Evaluation
For ecoli data see: http://archive.ics.uci.edu/ml/datasets/Ecoli
For letter recognition data see: http://archive.ics.uci.edu/ml/datasets/Letter+Recognition
For mushroom data see: http://archive.ics.uci.edu/ml/datasets/Mushroom
<br>
For each data set, there is a target variable, the one your model predicts. The following are the target variable for each data set.
*Mushroom: first column (e, p)
Letter: first column (A, B, ...)
Ecoli: last column (cp, im, ..)
Car: last column (acc, uacc, ..)
Breast-cancer: last column (2, 4)*
<br>

3. Compare and discuss your ca.jacob.cs6735.assignment.utils.algorithms (implementations) based on your experimental results.

### Submission

1. Hand in a hard copy report of your experimental study, including:
a. Description of the learning ca.jacob.cs6735.assignment.utils.algorithms you implement.
b. Description of the datasets you use (number of examples, number of attribute, number of classes, type of attributes, etc.).
c. Technical details of your implementation: pre-processing of data sets (discretization, etc.), parameter setting, etc.
d. Design of your programming implementation (data structures, overall program structure).
e. Report and analysis of your experimental results.

Submit your code via Desire2Learning.

### Deadline
Hand in your report to C114 and submit your source code no later than midnight, Dec. 12 (Tuesday), 2017.
