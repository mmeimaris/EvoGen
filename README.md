# EvoGen


To start the generator, run the main class in EvoGenerator.java. The parameters are the original LUBM generator parameters:
-univ 
-index 
-onto 

plus the following, EvoGen specific arguments:
-evo       #flag denoting whether to run EvoGen or not 
-change    #desired shift, as a number between [0,1] inclusive
-versions  #number of versions to be generated
-schemaEvo #the evolution of the schema, as a number between [0,1] . 

An example insantiation of the parameters is as follows:

-univ 
2
-index 
0 
-onto 
http://swat.cse.lehigh.edu/onto/univ-bench.owl
-evo
0
-change
0.3
-versions
2
-schemaEvo
0.7
