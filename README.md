[![Build Status](https://travis-ci.org/jacsmith21/jml.png?branch=master)](https://travis-ci.org/jacsmith21/jml)

# JML
A machine library built in pure Java. This library was built for the CS6735 assignment.

## Instructions
Conduct an experimental study on the following machine learning ca.jacob.cs6735.assignment.utils.algorithms: (1) ID3; (2) Adaboost on ID3; (3) Random Forest; (4) Naïve Bayes; (5) Adaboost on naïve Bayes; (6) K-nearest neighbors (kNN).

1. Implement the six ca.jacob.cs6735.assignment.utils.algorithms using Java.
2. Evaluate your implementation on the datasets in dataSet.zip using 10 times 5-fold cross-validation, and report the average accuracy and standard deviation.
3. Compare and discuss your algorithms (implementations) based on your experimental results.

### Data
Links:
* [Breast Cancer](http://archive.ics.uci.edu/ml/datasets/Breast+Cancer+Wisconsin+%28Diagnostic%29) 
* [Car](http://archive.ics.uci.edu/ml/datasets/Car+Evaluation) 
* [E. Coli](http://archive.ics.uci.edu/ml/datasets/Ecoli)  
* [Letter Recognition](http://archive.ics.uci.edu/ml/datasets/Letter+Recognition)  
* [Mushroom](http://archive.ics.uci.edu/ml/datasets/Mushroom)

Target Variables
* Mushroom: first column (e, p)
* Letter: first column (A, B, ...)
* E. Coli: last column (cp, im, ..)
* Car: last column (acc, uacc, ..)
* Breast-cancer: last column (2, 4)

### Report
1. Description of the learning ca.jacob.cs6735.assignment.utils.algorithms you implement.  
1. Description of the datasets you use (number of examples, number of attribute, number of classes, type of attributes, etc.).  
1. Technical details of your implementation: pre-processing of dataSet sets (discretization, etc.), parameter setting, etc.  
1. Design of your programming implementation (dataSet structures, overall program structure).  
1. Report and analysis of your experimental results.  

### Submission

1. Hand in a hard copy report of your experimental study.
2. Submit your code via Desire2Learning.

### Deadline
Hand in your report to C114 and submit your source code no later than midnight, Dec. 12 (Tuesday), 2017.

### Data
#### Breast Cancer
   1. Sample code number            id number
   2. Clump Thickness               1 - 10
   3. Uniformity of Cell Size       1 - 10
   4. Uniformity of Cell Shape      1 - 10
   5. Marginal Adhesion             1 - 10
   6. Single Epithelial Cell Size   1 - 10
   7. Bare Nuclei                   1 - 10
   8. Bland Chromatin               1 - 10
   9. Normal Nucleoli               1 - 10
  10. Mitoses                       1 - 10
  11. Class:                        (2 for benign, 4 for malignant)
  
#### Ecoli
  1.  Sequence Name: Accession number for the SWISS-PROT database
  2.  mcg: McGeoch's method for signal sequence recognition.
  3.  gvh: von Heijne's method for signal sequence recognition.
  4.  lip: von Heijne's Signal Peptidase II consensus sequence score.
           Binary attribute.
  5.  chg: Presence of charge on N-terminus of predicted lipoproteins.
	   Binary attribute.
  6.  aac: score of discriminant analysis of the amino acid content of
	   outer membrane and periplasmic proteins.
  7. alm1: score of the ALOM membrane spanning region prediction program.
  8. alm2: score of ALOM program after excluding putative cleavable signal
	   regions from the sequence.
  9. Class:   
  >cp  (cytoplasm)                                    143  
    im  (inner membrane without signal sequence)        77                 
    pp  (perisplasm)                                    52  
    imU (inner membrane, uncleavable signal sequence)   35  
    om  (outer membrane)                                20  
    omL (outer membrane lipoprotein)                     5  
    imL (inner membrane lipoprotein)                     2  
    imS (inner membrane, cleavable signal sequence)      2 
    
### References
1. [Naive Bayes](https://stats.stackexchange.com/questions/136577/how-to-deal-with-mixture-of-continuous-and-discrete-features-when-using-naive-ba)
1. Other

