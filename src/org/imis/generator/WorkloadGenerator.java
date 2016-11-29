package org.imis.generator;

import java.io.PrintWriter;
import java.util.Random;

public class WorkloadGenerator {

	public int max = 0;
	
	public WorkloadGenerator(int max){
		this.max = max;
	}
	
	public void generateWorkload(){
				
		Random random = new Random();
		//Generate diachronic query
		try{
		    PrintWriter writer = new PrintWriter("diachronic.txt", "UTF-8");
		    writer.println("CONSTRUCT {GRAPH ?version {?s ?p ?o}} WHERE {GRAPH ?version {?s ?p ?o}}");		    
		    writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Generate version query
		try{
		    PrintWriter writer = new PrintWriter("version.txt", "UTF-8");
		    writer.println("CONSTRUCT {?s ?p ?o} WHERE {GRAPH lubm:v" + random.nextInt(max+1) + " {?s ?p ?o}}");		    
		    writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Generate original LUBM queries
		try{
		    PrintWriter writer = new PrintWriter("l1.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X WHERE{ GRAPH lubm:v" + random.nextInt(max+1) + " {?X rdf:type ub:GraduateStudent . ?X ub:takesCourse <http://www.Department0.University0.edu/GraduateCourse0>}}");		    
		    writer.close();
		    
		    writer = new PrintWriter("l2.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X ?Y ?Z WHERE { GRAPH lubm:v" + random.nextInt(max+1) + " {?X rdf:type ub:GraduateStudent . ?Y rdf:type ub:University . ?Z rdf:type ub:Department . ?X ub:memberOf ?Z . ?Z ub:subOrganizationOf ?Y . ?X ub:undergraduateDegreeFrom ?Y}}");		    
		    writer.close();
		    
		    writer = new PrintWriter("l3.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X WHERE { GRAPH lubm:v" + random.nextInt(max+1) + " {?X rdf:type ub:Publication . ?X ub:publicationAuthor  <http://www.Department0.University0.edu/AssistantProfessor0>}}");		    
		    writer.close();
		    
		    writer = new PrintWriter("l4.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X ?Y1 ?Y2 ?Y3 WHERE { GRAPH lubm:v" + random.nextInt(max+1) + " {?X rdf:type ub:Professor . ?X ub:worksFor <http://www.Department0.University0.edu> . ?X ub:name ?Y1 . ?X ub:emailAddress ?Y2 . ?X ub:telephone ?Y3}}");		    
		    writer.close();
		    
		    writer = new PrintWriter("l5.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X WHERE { GRAPH lubm:v" + random.nextInt(max+1) + " {?X rdf:type ub:Person . ?X ub:memberOf <http://www.Department0.University0.edu>}}");		    
		    writer.close();
		    
		    writer = new PrintWriter("l6.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X WHERE { GRAPH lubm:v" + random.nextInt(max+1) + " {?X rdf:type ub:Student}}");		    
		    writer.close();
		    
		    writer = new PrintWriter("l7.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X ?Y WHERE { GRAPH lubm:v" + random.nextInt(max+1) + " {?X rdf:type ub:Student . ?Y rdf:type ub:Course . ?X ub:takesCourse ?Y . <http://www.Department0.University0.edu/AssociateProfessor0> ub:teacherOf ?Y}}");		    
		    writer.close();
		    
		    writer = new PrintWriter("l8.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X ?Y ?Z WHERE { GRAPH lubm:v" + random.nextInt(max+1) + " {?X rdf:type ub:Student .?Y rdf:type ub:Department .?X ub:memberOf ?Y .?Y ub:subOrganizationOf <http://www.University0.edu> .?X ub:emailAddress ?Z}}");		    
		    writer.close();
		    
		    writer = new PrintWriter("l9.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X ?Y ?Z WHERE { GRAPH lubm:v" + random.nextInt(max+1) + " {?X rdf:type ub:Student . ?Y rdf:type ub:Faculty . ?Z rdf:type ub:Course . ?X ub:advisor ?Y . ?Y ub:teacherOf ?Z . ?X ub:takesCourse ?Z}}");		    
		    writer.close();
		    
		    writer = new PrintWriter("l10.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X WHERE {GRAPH lubm:v" + random.nextInt(max+1) + " {?X rdf:type ub:Student . ?X ub:takesCourse <http://www.Department0.University0.edu/GraduateCourse0>}}");		    
		    writer.close();
		    
		    writer = new PrintWriter("l11.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X WHERE { GRAPH lubm:v" + random.nextInt(max+1) + " {?X rdf:type ub:ResearchGroup . ?X ub:subOrganizationOf <http://www.University0.edu>}}");		    
		    writer.close();
		    
		    writer = new PrintWriter("l12.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X ?Y WHERE { GRAPH lubm:v" + random.nextInt(max+1) + " {?X rdf:type ub:Chair . ?Y rdf:type ub:Department . ?X ub:worksFor ?Y . ?Y ub:subOrganizationOf <http://www.University0.edu>}}");		    
		    writer.close();
		    
		    writer = new PrintWriter("l13.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X WHERE { GRAPH lubm:v" + random.nextInt(max+1) + " {?X rdf:type ub:Person . <http://www.University0.edu> ub:hasAlumnus ?X}}");		    
		    writer.close();
		    
		    writer = new PrintWriter("l14.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X WHERE { GRAPH lubm:v" + random.nextInt(max+1) + " {?X rdf:type ub:UndergraduateStudent}}");		    
		    writer.close();
		    
		    
		} catch (Exception e) {
			e.printStackTrace();
		}

		//Generate longitudinal queries
		try{
			String filter = "";
			for(int i = 0; i <= max; i++){
				filter += "?version == v"+i;
				if(i < max){
					filter += " || ";
				}
			}
			filter = "FILTER (" + filter + ")";
		    PrintWriter writer = new PrintWriter("ll1.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X WHERE{ GRAPH ?version {?X rdf:type ub:GraduateStudent . ?X ub:takesCourse <http://www.Department0.University0.edu/GraduateCourse0>} " + filter + "}");		    
		    writer.close();
		    
		    writer = new PrintWriter("ll2.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X ?Y ?Z WHERE { GRAPH ?version {?X rdf:type ub:GraduateStudent . ?Y rdf:type ub:University . ?Z rdf:type ub:Department . ?X ub:memberOf ?Z . ?Z ub:subOrganizationOf ?Y . ?X ub:undergraduateDegreeFrom ?Y} " + filter + "}");		    
		    writer.close();
		    
		    writer = new PrintWriter("ll3.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X WHERE { GRAPH ?version {?X rdf:type ub:Publication . ?X ub:publicationAuthor  <http://www.Department0.University0.edu/AssistantProfessor0>} " + filter + "}");		    
		    writer.close();
		    
		    writer = new PrintWriter("ll4.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X ?Y1 ?Y2 ?Y3 WHERE { GRAPH ?version {?X rdf:type ub:Professor . ?X ub:worksFor <http://www.Department0.University0.edu> . ?X ub:name ?Y1 . ?X ub:emailAddress ?Y2 . ?X ub:telephone ?Y3} " + filter + "}");		    
		    writer.close();
		    
		    writer = new PrintWriter("ll5.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X WHERE { GRAPH ?version {?X rdf:type ub:Person . ?X ub:memberOf <http://www.Department0.University0.edu>} " + filter + "}");		    
		    writer.close();
		    
		    writer = new PrintWriter("ll6.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X WHERE { GRAPH ?version {?X rdf:type ub:Student} " + filter + "}");		    
		    writer.close();
		    
		    writer = new PrintWriter("ll7.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X ?Y WHERE { GRAPH ?version {?X rdf:type ub:Student . ?Y rdf:type ub:Course . ?X ub:takesCourse ?Y . <http://www.Department0.University0.edu/AssociateProfessor0> ub:teacherOf ?Y} " + filter + "}");		    
		    writer.close();
		    
		    writer = new PrintWriter("ll8.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X ?Y ?Z WHERE { GRAPH ?version {?X rdf:type ub:Student .?Y rdf:type ub:Department .?X ub:memberOf ?Y .?Y ub:subOrganizationOf <http://www.University0.edu> .?X ub:emailAddress ?Z} " + filter + "}");		    
		    writer.close();
		    
		    writer = new PrintWriter("ll9.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X ?Y ?Z WHERE { GRAPH ?version {?X rdf:type ub:Student . ?Y rdf:type ub:Faculty . ?Z rdf:type ub:Course . ?X ub:advisor ?Y . ?Y ub:teacherOf ?Z . ?X ub:takesCourse ?Z} " + filter + "}");		    
		    writer.close();
		    
		    writer = new PrintWriter("ll10.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X WHERE {GRAPH ?version {?X rdf:type ub:Student . ?X ub:takesCourse <http://www.Department0.University0.edu/GraduateCourse0>} " + filter + "}");		    
		    writer.close();
		    
		    writer = new PrintWriter("ll11.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X WHERE { GRAPH ?version {?X rdf:type ub:ResearchGroup . ?X ub:subOrganizationOf <http://www.University0.edu> } " + filter + "}");		    
		    writer.close();
		    
		    writer = new PrintWriter("ll12.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X ?Y WHERE { GRAPH ?version {?X rdf:type ub:Chair . ?Y rdf:type ub:Department . ?X ub:worksFor ?Y . ?Y ub:subOrganizationOf <http://www.University0.edu>} " + filter + "}");		    
		    writer.close();
		    
		    writer = new PrintWriter("ll13.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X WHERE { GRAPH ?version {?X rdf:type ub:Person . <http://www.University0.edu> ub:hasAlumnus ?X} " + filter + "}");		    
		    writer.close();
		    
		    writer = new PrintWriter("ll14.txt", "UTF-8");
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> SELECT ?X WHERE { GRAPH ?version {?X rdf:type ub:UndergraduateStudent} " + filter + "}");		    
		    writer.close();
		    
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Generate change queries
		try{
		    PrintWriter writer = new PrintWriter("c1.txt", "UTF-8");
		    //gets all distinct change types that occured in the <changeSet> graph
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> PREFIX co: http://www.diachron-fp7.eu/changes/#> SELECT DISTINCT ?changeType FROM <changeSet> WHERE {?change rdf:type ?changeType}");		    
		    writer.close();
		    
		    writer = new PrintWriter("c2.txt", "UTF-8");
		    //gets all triple change instances
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> PREFIX co: http://www.diachron-fp7.eu/changes/#> SELECT * FROM <changeSet> WHERE {?s ?p ?o}");		    
		    writer.close();
		    
		    writer = new PrintWriter("c3.txt", "UTF-8");
		    //associates changes with data
		    writer.println("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> PREFIX co: http://www.diachron-fp7.eu/changes/#> SELECT ?s ?p2 ?p3 WHERE { GRAPH lubm:v" + random.nextInt(max+1)+" {?s ?p ?o} GRAPH <changeset> { ?c co:api_p1 ?s ; ?c rdf:type co:Add_Property_Instance . ?c co:api_p2 ?p2 ; co:api_p3 ?p3}}");		    
		    writer.close();
		    
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
