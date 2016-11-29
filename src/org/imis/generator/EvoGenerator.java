/**
 * by Marios Meimaris
 * Institute for the Management of Information Systems
 * ATHENA Research and Innovation Center
 * 2015
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.imis.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.imis.model.CharacteristicSet;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.RDF;

public class EvoGenerator {

  ///////////////////////////////////////////////////////////////////////////
  //ontology class information
  //NOTE: prefix "CS" was used because the predecessor of univ-bench ontology
  //is called cs ontolgy.
  ///////////////////////////////////////////////////////////////////////////
  /** n/a */
  static final int CS_C_NULL = -1;
  /** University */
  static final int CS_C_UNIV = 0;
  /** Department */
  static final int CS_C_DEPT = CS_C_UNIV + 1;
  /** Faculty */
  static final int CS_C_FACULTY = CS_C_DEPT + 1;
  /** Professor */
  static final int CS_C_PROF = CS_C_FACULTY + 1;
  /** FullProfessor */
  static final int CS_C_FULLPROF = CS_C_PROF + 1;
  /** AssociateProfessor */
  static final int CS_C_ASSOPROF = CS_C_FULLPROF + 1;
  /** AssistantProfessor */
  static final int CS_C_ASSTPROF = CS_C_ASSOPROF + 1;
  /** Lecturer */
  static final int CS_C_LECTURER = CS_C_ASSTPROF + 1;
  /** Student */
  static final int CS_C_STUDENT = CS_C_LECTURER + 1;
  /** UndergraduateStudent */
  static final int CS_C_UNDERSTUD = CS_C_STUDENT + 1;
  /** GraduateStudent */
  static final int CS_C_GRADSTUD = CS_C_UNDERSTUD + 1;
  /** TeachingAssistant */
  static final int CS_C_TA = CS_C_GRADSTUD + 1;
  /** ResearchAssistant */
  static final int CS_C_RA = CS_C_TA + 1;
  /** Course */
  static final int CS_C_COURSE = CS_C_RA + 1;
  /** GraduateCourse */
  static final int CS_C_GRADCOURSE = CS_C_COURSE + 1;
  /** Publication */
  static final int CS_C_PUBLICATION = CS_C_GRADCOURSE + 1;
  /** Chair */
  static final int CS_C_CHAIR = CS_C_PUBLICATION + 1;
  /** Research */
  static final int CS_C_RESEARCH = CS_C_CHAIR + 1;
  /** ResearchGroup */
  static final int CS_C_RESEARCHGROUP = CS_C_RESEARCH + 1;
  
  static final int CS_C_VISITINGPROF = CS_C_RESEARCHGROUP + 1;
  
  static final int CS_C_VISITSTUD = CS_C_VISITINGPROF + 1;
  
  static final int CS_C_WEBCOURSE = CS_C_VISITSTUD + 1;  //21
  
  static final int CS_C_PROJECT = CS_C_WEBCOURSE + 1;
  
  static final int CS_C_EVENT = CS_C_PROJECT + 1;
  
  static final int CS_C_CONFPUBLICATION = CS_C_EVENT + 1;
  
  static final int CS_C_JOURNALPUBLICATION = CS_C_CONFPUBLICATION + 1;
  
  static final int CS_C_TECHNICALREPORT = CS_C_JOURNALPUBLICATION + 1;
  
  static final int CS_C_BOOK = CS_C_TECHNICALREPORT + 1;
  
  static final int CS_C_THESIS = CS_C_BOOK + 1;
  /** class information */
  static final int[][] CLASS_INFO = {
      /*{instance number if not specified, direct super class}*/
      //NOTE: the super classes specifed here do not necessarily reflect the entailment of the ontology
      {2, CS_C_NULL}, //CS_C_UNIV
      {1, CS_C_NULL}, //CS_C_DEPT
      {0, CS_C_NULL}, //CS_C_FACULTY
      {0, CS_C_FACULTY}, //CS_C_PROF
      {0, CS_C_PROF}, //CS_C_FULLPROF
      {0, CS_C_PROF}, //CS_C_ASSOPROF
      {0, CS_C_PROF}, //CS_C_ASSTPROF
      {0, CS_C_FACULTY}, //CS_C_LECTURER
      {0, CS_C_NULL}, //CS_C_STUDENT
      {0, CS_C_STUDENT}, //CS_C_UNDERSTUD
      {0, CS_C_STUDENT}, //CS_C_GRADSTUD
      {0, CS_C_NULL}, //CS_C_TA
      {0, CS_C_NULL}, //CS_C_RA
      {0, CS_C_NULL}, //CS_C_COURSE, treated as undergrad course here
      {0, CS_C_NULL}, //CS_C_GRADCOURSE
      {0, CS_C_NULL}, //CS_C_PUBLICATION
      {0, CS_C_NULL}, //CS_C_CHAIR
      {0, CS_C_NULL}, //CS_C_RESEARCH
      {0, CS_C_NULL}, //CS_C_RESEARCHGROUP
      {0, CS_C_PROF}, //CS_C_VISITPROF
      {0, CS_C_STUDENT}, //CS_C_VISITSTUD
      {0, CS_C_NULL}, //CS_C_RESEARCH
      {0, CS_C_NULL}, //CS_C_PROJECT
      {0, CS_C_NULL}, //CS_C_EVENT
      {0, CS_C_PUBLICATION}, //CS_C_CONFPUB
      {0, CS_C_PUBLICATION}, //CS_C_JOURNALPUB
      {0, CS_C_PUBLICATION}, //CS_C_TR
      {0, CS_C_PUBLICATION}, //CS_C_BOOK
      {0, CS_C_PUBLICATION}, //CS_C_THESIS
  };
  /** class name strings */
  static final String[] CLASS_TOKEN = {
      "University", //CS_C_UNIV
      "Department", //CS_C_DEPT
      "Faculty", //CS_C_FACULTY
      "Professor", //CS_C_PROF
      "FullProfessor", //CS_C_FULLPROF
      "AssociateProfessor", //CS_C_ASSOPROF
      "AssistantProfessor", //CS_C_ASSTPROF
      "Lecturer", //CS_C_LECTURER
      "Student", //CS_C_STUDENT
      "UndergraduateStudent", //CS_C_UNDERSTUD
      "GraduateStudent", //CS_C_GRADSTUD
      "TeachingAssistant", //CS_C_TA
      "ResearchAssistant", //CS_C_RA
      "Course", //CS_C_COURSE
      "GraduateCourse", //CS_C_GRADCOURSE
      "Publication", //CS_C_PUBLICATION
      "Chair", //CS_C_CHAIR
      "Research", //CS_C_RESEARCH
      "ResearchGroup", //CS_C_RESEARCHGROUP
      "VisitingProfessor",
      "VisitingStudent",
      "WebCourse",
      "ResearchProject",
      "Event",
      "ConferencePublication",
      "JournalArticle",
      "TechnicalReport",
      "Book",
      "Thesis"
  };
  /** number of classes */
  static final int CLASS_NUM = CLASS_INFO.length;
  /** index of instance-number in the elements of array CLASS_INFO */
  static final int INDEX_NUM = 0;
  /** index of super-class in the elements of array CLASS_INFO */
  static final int INDEX_SUPER = 1;
  static String[] CS_EVENT_TYPES = new String[] {"Conferene", "Workshop", "Summer School"};
  ///////////////////////////////////////////////////////////////////////////
  //ontology property information
  ///////////////////////////////////////////////////////////////////////////
  /** name */
  static final int CS_P_NAME = 0;
  /** takesCourse */
  static final int CS_P_TAKECOURSE = CS_P_NAME + 1;
  /** teacherOf */
  static final int CS_P_TEACHEROF = CS_P_TAKECOURSE + 1;
  /** undergraduateDegreeFrom */
  static final int CS_P_UNDERGRADFROM = CS_P_TEACHEROF + 1;
  /** mastersDegreeFrom */
  static final int CS_P_GRADFROM = CS_P_UNDERGRADFROM + 1;
  /** doctoralDegreeFrom */
  static final int CS_P_DOCFROM = CS_P_GRADFROM + 1;
  /** advisor */
  static final int CS_P_ADVISOR = CS_P_DOCFROM + 1;
  /** memberOf */
  static final int CS_P_MEMBEROF = CS_P_ADVISOR + 1;
  /** publicationAuthor */
  static final int CS_P_PUBLICATIONAUTHOR = CS_P_MEMBEROF + 1;
  /** headOf */
  static final int CS_P_HEADOF = CS_P_PUBLICATIONAUTHOR + 1;
  /** teachingAssistantOf */
  static final int CS_P_TAOF = CS_P_HEADOF + 1;
  /** reseachAssistantOf */
  static final int CS_P_RESEARCHINTEREST = CS_P_TAOF + 1;
  /** emailAddress */
  static final int CS_P_EMAIL = CS_P_RESEARCHINTEREST + 1;
  /** telephone */
  static final int CS_P_TELEPHONE = CS_P_EMAIL + 1;
  /** subOrganizationOf */
  static final int CS_P_SUBORGANIZATIONOF = CS_P_TELEPHONE + 1;
  /** worksFor */
  static final int CS_P_WORKSFOR = CS_P_SUBORGANIZATIONOF + 1;
  
  static final int CS_P_VISITSASPROF = CS_P_WORKSFOR + 1;
  
  static final int CS_P_VISITDURATION = CS_P_VISITSASPROF + 1;
  
  static final int CS_P_VISITSASSTUD = CS_P_VISITDURATION + 1;
  
  static final int CS_P_TOPIC = CS_P_VISITSASSTUD + 1;
  
  static final int CS_P_URL = CS_P_TOPIC + 1;
  
  static final int CS_P_HOURS = CS_P_URL + 1;
  
  static final int CS_P_DURATION = CS_P_HOURS + 1;
  
  static final int CS_P_FUNDEDBY = CS_P_DURATION + 1;
  
  static final int CS_P_SCIENTIFICADVISOR = CS_P_FUNDEDBY + 1;
  
  static final int CS_P_BUDGET = CS_P_SCIENTIFICADVISOR + 1;
  
  static final int CS_P_RESEARCHGROUP = CS_P_BUDGET + 1;
  
  static final int CS_P_EVENTTYPE = CS_P_RESEARCHGROUP + 1;
  
  static final int CS_P_EVENTORGANIZER = CS_P_EVENTTYPE + 1;
  
  static final int CS_P_DATE = CS_P_EVENTORGANIZER + 1;
  
  static final int CS_P_ISBN = CS_P_DATE + 1;
  
  static final int CS_P_VENUE = CS_P_ISBN + 1;
  
  static final int CS_P_EDITORINCHIEF = CS_P_VENUE + 1;
  
  static final int CS_P_REPORTID = CS_P_EDITORINCHIEF + 1;
  
  static final int CS_P_SUPERVISOR = CS_P_REPORTID + 1;
  /** property name strings */
  static final String[] PROP_TOKEN = {
      "name",
      "takesCourse",
      "teacherOf",
      "undergraduateDegreeFrom",
      "mastersDegreeFrom",
      "doctoralDegreeFrom",
      "advisor",
      "memberOf",
      "publicationAuthor",
      "headOf",
      "teachingAssistantOf",
      "researchInterest",
      "emailAddress",
      "telephone",
      "subOrganizationOf",
      "worksFor",
      "visitsAsProfessor",
      "durationOfVisit",
      "visitsAsStudent",
      "webCourseTopic",
      "url",
      "courseHours",
      "projectDuration",
      "fundedBy",
      "scientificAdvisor",
      "budget",
      "researchGroup",
      "eventType",
      "eventOrganizer",
      "date",
      "isbn",
      "venue",
      "editorInChief",
      "technicalReportID",
      "supervisor"
      
  };
  /** number of properties */
  static final int PROP_NUM = PROP_TOKEN.length;

  ///////////////////////////////////////////////////////////////////////////
  //restrictions for data generation
  ///////////////////////////////////////////////////////////////////////////
  /** size of the pool of the undergraduate courses for one department */
  private static final int UNDER_COURSE_NUM = 100; //must >= max faculty # * FACULTY_COURSE_MAX
  /** size of the pool of the graduate courses for one department */
  private static final int GRAD_COURSE_NUM = 100; //must >= max faculty # * FACULTY_GRADCOURSE_MAX
  
  private static final int WEB_COURSE_NUM = 100; //must >= max faculty # * FACULTY_GRADCOURSE_MAX
  /** size of the pool of universities */
  private static final int UNIV_NUM = 1000;
  /** size of the pool of reasearch areas */
  private static final int RESEARCH_NUM = 30;
  private static final int PROJECT_NUM_MIN = 10;
  private static final int PROJECT_NUM_MAX = 30;
  private static final int EVENT_NUM_MIN = 15;
  private static final int EVENT_NUM_MAX = 45;
  /** minimum number of departments in a university */
  private static final int DEPT_MIN = 15;
  /** maximum number of departments in a university */
  private static final int DEPT_MAX = 25;
  //must: DEPT_MAX - DEPT_MIN + 1 <> 2 ^ n
  /** minimum number of publications of a full professor */
  private static final int FULLPROF_PUB_MIN = 15;
  /** maximum number of publications of a full professor */
  private static final int FULLPROF_PUB_MAX = 20;
  /** minimum number of publications of an associate professor */
  private static final int ASSOPROF_PUB_MIN = 10;
  /** maximum number of publications of an associate professor */
  private static final int ASSOPROF_PUB_MAX = 18;
  /** minimum number of publications of an assistant professor */
  private static final int ASSTPROF_PUB_MIN = 5;
  /** maximum number of publications of an assistant professor */
  private static final int ASSTPROF_PUB_MAX = 10;
  /** minimum number of publications of a graduate student */
  private static final int GRADSTUD_PUB_MIN = 0;
  /** maximum number of publications of a graduate student */
  private static final int GRADSTUD_PUB_MAX = 5;
  /** minimum number of publications of a lecturer */
  private static final int LEC_PUB_MIN = 0;
  /** maximum number of publications of a lecturer */
  private static final int LEC_PUB_MAX = 5;
  /** minimum number of courses taught by a faculty */
  private static final int FACULTY_COURSE_MIN = 1;
  /** maximum number of courses taught by a faculty */
  private static final int FACULTY_COURSE_MAX = 2;
  /** minimum number of graduate courses taught by a faculty */
  private static final int FACULTY_GRADCOURSE_MIN = 1;
  /** maximum number of graduate courses taught by a faculty */
  private static final int FACULTY_GRADCOURSE_MAX = 2;
  /** minimum number of courses taken by a undergraduate student */
  private static final int UNDERSTUD_COURSE_MIN = 2;
  /** maximum number of courses taken by a undergraduate student */
  private static final int UNDERSTUD_COURSE_MAX = 4;
  private static final int VISITSTUD_COURSE_MIN = 2;
  /** maximum number of courses taken by a undergraduate student */
  private static final int VISITSTUD_COURSE_MAX = 4;
  /** minimum number of courses taken by a graduate student */
  private static final int GRADSTUD_COURSE_MIN = 1;
  /** maximum number of courses taken by a graduate student */
  private static final int GRADSTUD_COURSE_MAX = 3;
  /** minimum number of research groups in a department */
  private static final int RESEARCHGROUP_MIN = 10;
  /** maximum number of research groups in a department */
  private static final int RESEARCHGROUP_MAX = 20;
  //faculty number: 30-42
  /** minimum number of full professors in a department*/
  private static final int FULLPROF_MIN = 7;
  /** maximum number of full professors in a department*/
  private static final int FULLPROF_MAX = 10;
  private static final int VISITINGPROF_MIN = 7;
  /** maximum number of full professors in a department*/
  private static final int VISITINGPROF_MAX = 10;
  /** minimum number of associate professors in a department*/
  private static final int ASSOPROF_MIN = 10;
  /** maximum number of associate professors in a department*/
  private static final int ASSOPROF_MAX = 14;
  /** minimum number of assistant professors in a department*/
  private static final int ASSTPROF_MIN = 8;
  /** maximum number of assistant professors in a department*/
  private static final int ASSTPROF_MAX = 11;
  /** minimum number of lecturers in a department*/
  private static final int LEC_MIN = 5;
  /** maximum number of lecturers in a department*/
  private static final int LEC_MAX = 7;
  /** minimum ratio of undergraduate students to faculties in a department*/
  private static final int R_UNDERSTUD_FACULTY_MIN = 8;
  /** maximum ratio of undergraduate students to faculties in a department*/
  private static final int R_UNDERSTUD_FACULTY_MAX = 14;
  /** minimum ratio of undergraduate students to faculties in a department*/
  private static final int R_VISITSTUD_FACULTY_MIN = 2;
  /** maximum ratio of undergraduate students to faculties in a department*/
  private static final int R_VISITSTUD_FACULTY_MAX = 3;
  /** minimum ratio of graduate students to faculties in a department*/
  private static final int R_GRADSTUD_FACULTY_MIN = 3;
  /** maximum ratio of graduate students to faculties in a department*/
  private static final int R_GRADSTUD_FACULTY_MAX = 4;
  //MUST: FACULTY_COURSE_MIN >= R_GRADSTUD_FACULTY_MAX / R_GRADSTUD_TA_MIN;
  /** minimum ratio of graduate students to TA in a department */
  private static final int R_GRADSTUD_TA_MIN = 4;
  /** maximum ratio of graduate students to TA in a department */
  private static final int R_GRADSTUD_TA_MAX = 5;
  /** minimum ratio of graduate students to RA in a department */
  private static final int R_GRADSTUD_RA_MIN = 3;
  /** maximum ratio of graduate students to RA in a department */
  private static final int R_GRADSTUD_RA_MAX = 4;
  /** average ratio of undergraduate students to undergraduate student advising professors */
  private static final int R_UNDERSTUD_ADVISOR = 5;
  /** average ratio of undergraduate students to undergraduate student advising professors */
  private static final int R_VISITSTUD_ADVISOR = 2;
  /** average ratio of graduate students to graduate student advising professors */
  private static final int R_GRADSTUD_ADVISOR = 1;

  /** delimiter between different parts in an id string*/
  private static final char ID_DELIMITER = '/';
  /** delimiter between name and index in a name string of an instance */
  private static final char INDEX_DELIMITER = '_';
  /** name of the log file */
  private static final String LOG_FILE = "log.txt";

  
  /** instance count of a class */
  private class InstanceCount {
    /** instance number within one department */
    public int num = 0;
    /** total instance num including sub-classes within one department */
    public int total = 0;
    /** index of the current instance within the current department */
    public int count = 0;
    /** total number so far within the current department */
    public int logNum = 0;
    /** total number so far */
    public long logTotal = 0l;
  }

  /** instance count of a property */
  private class PropertyCount {
    /** total number so far within the current department */
    public int logNum = 0;
    /** total number so far */
    public long logTotal = 0l;
  }

  /** information a course instance */
  private class CourseInfo {
    /** index of the faculty who teaches this course */
    public int indexInFaculty = 0;
    /** index of this course */
    public int globalIndex = 0;
  }

  /** information of an RA instance */
  private class RaInfo {
    /** index of this RA in the graduate students */
    public int indexInGradStud = 0;
  }

  /** information of a TA instance */
  private class TaInfo {
    /** index of this TA in the graduate students */
    public int indexInGradStud = 0;
    /** index of the course which this TA assists */
    public int indexInCourse = 0; //local index in courses
  }

  /** informaiton of a publication instance */
  private class PublicationInfo {
    /** id */
    public String id;
    /** name */
    public String name;
    /** list of authors */
    public ArrayList authors;
  }
  

  /** univ-bench ontology url */
  String ontology;
  /** (class) instance information */
  private InstanceCount[] instances_;
  /** property instance information */
  private PropertyCount[] properties_;
  /** data file writer */
  private Writer writer_;
  private Writer writer_log;
  /** generate DAML+OIL data (instead of OWL) */
  private boolean isDaml_;
  /** random number generator */
  private Random random_;
  /** seed of the random number genertor for the current university */
  private long seed_ = 0l;
  /** user specified seed for the data generation */
  private long baseSeed_ = 0l;
  /** list of undergraduate courses generated so far (in the current department) */
  private ArrayList underCourses_;
  /** list of graduate courses generated so far (in the current department) */
  private ArrayList gradCourses_;
  private ArrayList webCourses_;
  /** list of remaining available undergraduate courses (in the current department) */
  private ArrayList remainingUnderCourses_;
  /** list of remaining available graduate courses (in the current department) */
  private ArrayList remainingGradCourses_;
  private ArrayList remainingWebCourses_;
  /** list of publication instances generated so far (in the current department) */
  private ArrayList publications_;
  private ArrayList projects_;
  /** index of the full professor who has been chosen as the department chair */
  private int chair_;
  /** starting index of the universities */
  private int startIndex_;
  /** log writer */
  private PrintStream log_ = null;
  static boolean evo = false;
  static int evoParadigm = -1;
  static double evoChange = 0d;
  static int evoVersions = 0;
  static double strict = 0; 
  static double step = 0; 
  static int evoOnlyChanges = -1;
  static ArrayList<Integer> classFilters = new ArrayList<Integer>();
  static HashSet<Integer> currentFilters = new HashSet<Integer>();
  static String tempDir = System.getProperty("user.dir") + "/temp";
  static String userDir = System.getProperty("user.dir");
  static HashMap<Integer, Double> changeWeights = new HashMap<Integer, Double>();
  static HashMap<String, InstanceCount[]> fileInstanceMap = new HashMap<String, EvoGenerator.InstanceCount[]>();
  static HashMap<String, HashMap<Integer, Double>> fileWeightsMap = new HashMap<String, HashMap<Integer,Double>>();
  static int totalDeptsV0 = 0;
  
  /**
   * main method
   */
  public static void main(String[] args) {
    //default values
	  
    int univNum = 1, startIndex = 0, seed = 0;
    boolean daml = false;   
    String ontology = null;    
    try {
      String arg;
      int i = 0;
      while (i < args.length) {    	  
        arg = args[i++];
        if (arg.equals("-univ")) {
          if (i < args.length) {
            arg = args[i++];
            univNum = Integer.parseInt(arg);
            if (univNum < 1)
              throw new NumberFormatException();
          }
          else
            throw new NumberFormatException();          
        }
        else if (arg.equals("-index")) {        	
          if (i < args.length) {
            arg = args[i++];
            startIndex = Integer.parseInt(arg);
            if (startIndex < 0)
              throw new NumberFormatException();
          }
          else
            throw new NumberFormatException();
        }
        else if (arg.equals("-seed")) {
          if (i < args.length) {
            arg = args[i++];
            seed = Integer.parseInt(arg);
            if (seed < 0)
              throw new NumberFormatException();
          }
          else
            throw new NumberFormatException();
        }
        else if (arg.equals("-daml")) {
          daml = true;
        }
        else if (arg.equals("-onto")) {
          if (i < args.length) {
            arg = args[i++];
            ontology = arg;
          }
          else
            throw new Exception();
        }
        else if (arg.equals("-evo")) {        	
        	if (i < args.length) {
        		arg = args[i++];
        		evo = true;                 
        		evoParadigm = Integer.parseInt(arg);        		       		                              
              }
              else
                throw new Exception();        	            
          }
        else if (arg.equals("-change")) {     
        	 if (i < args.length) {        			   
                 arg = args[i++];
                 evoChange = Double.parseDouble(arg);                          
               }
               else
                 throw new Exception();   
        }
        else if (arg.equals("-versions")) {
        	if (i < args.length) {
             	arg = args[i++];
             	evoVersions = Integer.parseInt(arg);
             }
             else
                 throw new Exception();
        }  
        else if (arg.equals("-strict")) {
        	if (i < args.length) {
             	arg = args[i++];
             	strict = Double.parseDouble(arg);
             	if(strict < 0 || strict > 1)
             		throw new Exception();
             	step = (double)(1-strict)/(evoVersions-1);
             	/*classFilters.add(CS_C_FULLPROF);
             	classFilters.add(CS_C_ASSOPROF);
             	classFilters.add(CS_C_ASSTPROF);
             	classFilters.add(CS_C_LECTURER);
             	classFilters.add(CS_C_UNDERSTUD);
             	classFilters.add(CS_C_GRADSTUD);
             	//classFilters.add(CS_C_COURSE);
             	classFilters.add(CS_C_GRADCOURSE);
             	classFilters.add(CS_C_RESEARCHGROUP);*/
             	System.out.println("step: " + step);
             }
             else
                 throw new Exception();
        }  
        else if (arg.equals("-onlyChanges")) {
        	if (i < args.length) {
             	arg = args[i++];
             	evoOnlyChanges = Integer.parseInt(arg);
             	if(evoOnlyChanges == 0)
             		new File(tempDir).mkdirs();
             }
             else
                 throw new Exception();
        } 
        else if (arg.equals("-dir")) {
            if (i < args.length) {
              arg = args[i++];
              userDir = arg;              
            }
            else
              throw new NumberFormatException();
          }
        else
          throw new Exception();
      }
      if ( ( (long) startIndex + univNum - 1) > Integer.MAX_VALUE) {
        System.err.println("Index overflow!");
        throw new Exception();
      }
      if (null == ontology) {
        System.err.println("ontology url is requested!");
        throw new Exception();
      }
    }
    catch (Exception e) {
      System.err.println("Usage: Generator\n" +
                         "\t[-univ <num of universities(1~" + Integer.MAX_VALUE +
                         ")>]\n" +
                         "\t[-index <start index(0~" + Integer.MAX_VALUE +
                         ")>]\n" +
                         "\t[-seed <seed(0~" + Integer.MAX_VALUE + ")>]\n" +
                         "\t[-daml]\n" +
                         "\t-onto <univ-bench ontology url>");
      System.exit(0);
    }

    
    new EvoGenerator().start(univNum, startIndex, seed, daml, ontology);
    
    File[] files = new File(System.getProperty("user.dir")).listFiles();
    Model model = ModelFactory.createDefaultModel();
    //model.set
	for(File file : files){
		if(!file.getName().contains("owl")) continue;
		try{
			
			FileInputStream in = new FileInputStream(file);
			model.read(in, "http://example.com", "RDF/XML");			
			in.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	long previousSize = model.size();
	System.out.println("v0 size: " + previousSize);
	
	String queryString = " SELECT ?s ?p WHERE {?s ?p ?o} GROUP BY ?s ?p ORDER BY ?s ";
	
	Query query = QueryFactory.create(queryString);
	
	QueryExecution qexec = QueryExecutionFactory.create(query, model);
	
	ResultSet rs = qexec.execSelect();		
	
	Resource previous = null;
	
	HashMap<Node, CharacteristicSet> characteristicSetMap = new HashMap<Node, CharacteristicSet>(); 
	
	HashSet<Resource> properties = new HashSet<Resource>();

	CharacteristicSet cs ;
	
	int nextInd = 0;
	
	int propIndex = 0;
	
	HashSet<Integer> objects = new HashSet<Integer>();
	
	while(rs.hasNext()){
		
	 	QuerySolution sol = rs.next();
	 	
	 	Resource subject = sol.getResource("s");
	 	
	 	Resource predicate = sol.getResource("p");
	 			 			 	
	 	if(!propertiesSet.containsKey(predicate.getURI())){
	 		reversePropertiesSet.put(propIndex, predicate.getURI());
    		propertiesSet.put(predicate.getURI(), propIndex++);	 
    	
    	}
	 	
	 	if(!subject.equals(previous) && previous != null){
    			 				 	
	 		cs = new CharacteristicSet(properties);
	 		
	 		//uniqueCharacteristicSets.add(cs);
	 				 				 	
	 		characteristicSetMap.put(previous.asNode(), cs);
	 		
	 		properties = new HashSet<Resource>();
	 				 		
	 		
    	}
	 	
	 	if(!properties.contains(predicate))
	 		properties.add(predicate);	 			 		 		 
	 	
	 	previous = subject;
	}
	qexec.close();
	//don't forget the last one
	cs = new CharacteristicSet(properties); 		
		//uniqueCharacteristicSets.add(cs); 		 		 	
	characteristicSetMap.put(previous.asNode(), cs);
	
		//System.out.println("Unique CS: " + uniqueCharacteristicSets.size());
	System.out.println("Unique nodes with CS: " + characteristicSetMap.size());
	HashSet<CharacteristicSet> ucs = new HashSet<CharacteristicSet>();
	for(Node n : characteristicSetMap.keySet()){
		ucs.add(characteristicSetMap.get(n));
	}
	System.out.println("Unique CS: " + ucs.size());
	
	model.close();
	WorkloadGenerator workload = new WorkloadGenerator(evoVersions-1);
	workload.generateWorkload();
	int i = 0;
	while(true){
		if(true) break;
		try{
			files = new File(System.getProperty("user.dir")+"/v"+i).listFiles();
			Model modelin = ModelFactory.createDefaultModel();
			for(File file : files){
				if(!file.getName().contains(".owl")) continue;{
					FileInputStream in = new FileInputStream(file);
					modelin.read(in, "http://example.com", "RDF/XML");			
					in.close();
				}
			}
			//System.out.println("v"+(int)(i+1)+ " size: " + (previousSize+modelin.size()));
			double shift = (double)modelin.size()/(double)previousSize;
			System.out.println(shift);
			previousSize += modelin.size();
			modelin.close();
			i++;
		}
		catch(Exception e){			
			break;
		}
	}
	
	
  
  }

  public static HashMap<String, Integer> propertiesSet = new HashMap<String, Integer>();
	public static HashMap<Integer, String> reversePropertiesSet = new HashMap<Integer, String>();
  
  /**
   * constructor
   */
  public EvoGenerator() {
    instances_ = new InstanceCount[CLASS_NUM];
    for (int i = 0; i < CLASS_NUM; i++) {
      instances_[i] = new InstanceCount();
    }
    properties_ = new PropertyCount[PROP_NUM];
    for (int i = 0; i < PROP_NUM; i++) {
      properties_[i] = new PropertyCount();
    }

    random_ = new Random();
    underCourses_ = new ArrayList();
    gradCourses_ = new ArrayList();
    webCourses_ = new ArrayList();
    remainingUnderCourses_ = new ArrayList();
    remainingGradCourses_ = new ArrayList();
    remainingWebCourses_ = new ArrayList();
    publications_ = new ArrayList();
    projects_ = new ArrayList();
  }

  /**
   * Begins the data generation.
   * @param univNum Number of universities to generate.
   * @param startIndex Starting index of the universities.
   * @param seed Seed for data generation.
   * @param daml Generates DAML+OIL data if true, OWL data otherwise.
   * @param ontology Ontology url.
   */
  public void start(int univNum, int startIndex, int seed, boolean daml,
                    String ontology) {
    this.ontology = ontology;

    isDaml_ = daml;
    if (daml)
      writer_ = new DamlWriter(this);
    else
      writer_ = new OwlWriter(this);

    writer_log = new OwlWriter(this);
    startIndex_ = startIndex;
    baseSeed_ = seed;
    instances_[CS_C_UNIV].num = univNum;
    instances_[CS_C_UNIV].count = startIndex;
    //evo = false;
    if(!evo){
    	_generate();
		System.out.println("See log.txt for more details.");
		return;
    }
    
    _generate(evoVersions);  
    
    System.out.println("See log.txt for more details.");
    
  }

  ///////////////////////////////////////////////////////////////////////////
  //writer callbacks

  /**
   * Callback by the writer when it starts an instance section.
   * @param classType Type of the instance.
   */
  void startSectionCB(int classType) {
    instances_[classType].logNum++;
    instances_[classType].logTotal++;
  }

  /**
   * Callback by the writer when it starts an instance section identified by an rdf:about attribute.
   * @param classType Type of the instance.
   */
  void startAboutSectionCB(int classType) {
    startSectionCB(classType);
  }

  /**
   * Callback by the writer when it adds a property statement.
   * @param property Type of the property.
   */
  void addPropertyCB(int property) {
    properties_[property].logNum++;
    properties_[property].logTotal++;
  }

  /**
   * Callback by the writer when it adds a property statement whose value is an individual.
   * @param classType Type of the individual.
   */
  void addValueClassCB(int classType) {
    instances_[classType].logNum++;
    instances_[classType].logTotal++;
  }

  ///////////////////////////////////////////////////////////////////////////

  /**
   * Sets instance specification.
   */
  private void _setInstanceInfo() {
    int subClass, superClass;

    for (int i = 0; i < CLASS_NUM; i++) {
      switch (i) {
        case CS_C_UNIV:
          break;
        case CS_C_DEPT:
          break;
        case CS_C_FULLPROF:
          instances_[i].num = _getRandomFromRange(FULLPROF_MIN, FULLPROF_MAX);
          break;
        case CS_C_VISITINGPROF:
            instances_[i].num = _getRandomFromRange(VISITINGPROF_MIN, VISITINGPROF_MAX);
            break;
        case CS_C_ASSOPROF:
          instances_[i].num = _getRandomFromRange(ASSOPROF_MIN, ASSOPROF_MAX);
          break;
        case CS_C_ASSTPROF:
          instances_[i].num = _getRandomFromRange(ASSTPROF_MIN, ASSTPROF_MAX);
          break;
        case CS_C_LECTURER:
          instances_[i].num = _getRandomFromRange(LEC_MIN, LEC_MAX);
          break;
        case CS_C_UNDERSTUD:
          instances_[i].num = _getRandomFromRange(R_UNDERSTUD_FACULTY_MIN *
                                         instances_[CS_C_FACULTY].total,
                                         R_UNDERSTUD_FACULTY_MAX *
                                         instances_[CS_C_FACULTY].total);
          break;
        case CS_C_VISITSTUD:
            instances_[i].num = _getRandomFromRange(R_VISITSTUD_FACULTY_MIN *
                                           instances_[CS_C_FACULTY].total,
                                           R_VISITSTUD_FACULTY_MAX *
                                           instances_[CS_C_FACULTY].total);
            break;          
        case CS_C_GRADSTUD:
          instances_[i].num = _getRandomFromRange(R_GRADSTUD_FACULTY_MIN *
                                         instances_[CS_C_FACULTY].total,
                                         R_GRADSTUD_FACULTY_MAX *
                                         instances_[CS_C_FACULTY].total);
          break;
        case CS_C_TA:
          instances_[i].num = _getRandomFromRange(instances_[CS_C_GRADSTUD].total /
                                         R_GRADSTUD_TA_MAX,
                                         instances_[CS_C_GRADSTUD].total /
                                         R_GRADSTUD_TA_MIN);
          break;
        case CS_C_RA:
          instances_[i].num = _getRandomFromRange(instances_[CS_C_GRADSTUD].total /
                                         R_GRADSTUD_RA_MAX,
                                         instances_[CS_C_GRADSTUD].total /
                                         R_GRADSTUD_RA_MIN);
          break;
        case CS_C_RESEARCHGROUP:
          instances_[i].num = _getRandomFromRange(RESEARCHGROUP_MIN, RESEARCHGROUP_MAX);
          break;
        default:
          instances_[i].num = CLASS_INFO[i][INDEX_NUM];
          break;
      }
      instances_[i].total = instances_[i].num;
      subClass = i;
      while ( (superClass = CLASS_INFO[subClass][INDEX_SUPER]) != CS_C_NULL) {
        instances_[superClass].total += instances_[i].num;
        subClass = superClass;
      }
    }
  }

  /** Begins data generation according to the specification */
  private void _generate() {
    System.out.println("Started...");
    try {
      log_ = new PrintStream(new FileOutputStream(System.getProperty("user.dir") +
                                                 "\\" + LOG_FILE));
      //writer_.start();      
	  for (int i = 0; i < instances_[CS_C_UNIV].num; i++) {
	    _generateUniv(i + startIndex_);
	  }
     
      //writer_.end();
      log_.close();
    }
    catch (IOException e) {
      System.out.println("Failed to create log file!");
    }
    System.out.println("Completed!");
  }
  
  static boolean globalVersionTrigger = false;
  /** Begins data generation according to the specification, for the defined number of versions */
  private void _generate(int versions) {
	  
	  //assignFilters(classFilters);
	  //System.out.println("current filters: " + currentFilters.toString());
	  _generate(); //V0
	  //int schemaEvol = (int) Math.floor((1/step));
	  int schemaEvol = (int)(strict*10);
	  int schemaEvol2 = schemaEvol / (evoVersions-1);
	  System.out.println("schema evol param: " + schemaEvol2);
	  int howManyDepts = (int) Math.floor(totalDeptsV0*evoChange);
	  HashMap<Integer, String> newClasses = new HashMap<Integer, String>();
	  for(int i = 0; i < schemaEvol; i++)
		  newClasses.put(i,"");

	  double evoChangeOriginal = evoChange;
	  boolean pub = false, conf = false, journ = false, tech = false, 
			  book = false, thes = false, proj = false, even = false;
	  for(int vi = 0 ; vi < evoVersions-1; vi++){
	    	
		  globalVersionTrigger = true;
		  File dir = new File(System.getProperty("user.dir")+"/v"+vi);
          dir.mkdirs();
		  //assignFilters(classFilters);
		  //System.out.println("current filters: " + currentFilters.toString());
	    	int classForChange ;
	        
	    	//the number of depts (files) to evolve, based on the defined evoChange parameter	        
	       
	        List<String> asList = new ArrayList<String>(fileWeightsMap.keySet());
	        
	        writer_log = new OwlWriter(this);	        
            writer_log.startLogFile(dir.getAbsolutePath()+"/changes.rdf");
            writer_log.start();
	        for(int d = 0 ; d < howManyDepts ; d++){
	        	
	            String randomFile = asList.get(random_.nextInt(asList.size()));
	            instances_ = fileInstanceMap.get(randomFile);
	                        
	            asList.remove(randomFile);
	        	
	            //System.out.println("Selected file: " + randomFile);
	            writer_ = new OwlWriter(this);
	            
	            writer_.startFile(dir.getAbsolutePath()+"/"+randomFile);
	            writer_.start();
	            for(Integer nextClass : fileWeightsMap.get(randomFile).keySet()){
	            	
	            	classForChange = nextClass;
	            	
	            	if(classForChange < 2) {
	            			continue;
	            	}
	            	
	            	if(classForChange == 4) continue;
	            	int totalIter = (int) Math.floor(fileWeightsMap.get(randomFile).get(classForChange))/howManyDepts;
	            	
	                for(int i = 0; i < totalIter ; i++){
	                	
	                	_generateASection(classForChange, 
	                			instances_[classForChange].count ); 	                			
	                }
	                
	                if(nextClass == 21 ){
	                	writer_log.addTypeClass(ontology+"WebCourse");
	                	writer_log.addSuperClass(ontology+"WebCourse", ontology+"Course");
	                }
	                else if(nextClass == 20 ){
	                	writer_log.addTypeClass(ontology+"VisitingStudent");
	                	writer_log.addSuperClass(ontology+"VisitingStudent", ontology+"Student");
	                }
	                else if(nextClass == 19 ){
	                	writer_log.addTypeClass(ontology+"VisitingProfessor");
	                	writer_log.addSuperClass(ontology+"VisitingProfessor", ontology+"Professor");
	                }
	                
	            }
	            
	            for(int k = 0; k < schemaEvol2+1; k++){
	              	
	            	if(newClasses.isEmpty()) break;
	            	int newClass = _getRandomFromRange(0, newClasses.keySet().size()+1);
	            	int index = 0;
	            	for(Integer s : newClasses.keySet()){
	            		if(index == newClass){
	            			newClass = s;
	            			break;
	            		}
	            		index++;
	            	}
	              	/*if(newClass == 1){
	                  	_generatePublications();
	                  	newClasses.remove(newClass);
	                 }*/
	                  if(newClass == 2){
	                  	_generateConferencePublications();
	                  	newClasses.remove(newClass);
	                  	writer_log.addTypeClass(ontology+"ConferencePublication");
	                  	writer_log.addSuperClass(ontology+"ConferencePublication", ontology+"Publication");
	                  }
	                  if(newClass == 3){
	                  	_generateJournalPublications();
	                  	newClasses.remove(newClass);
	                  	writer_log.addTypeClass(ontology+"JournalArticle");
	                  	writer_log.addSuperClass(ontology+"JournalArticle", ontology+"Publication");
	                  }
	                  if(newClass == 4 ){
	                  	_generateTechnicalReports();
	                  	newClasses.remove(newClass);
	                  	writer_log.addTypeClass(ontology+"TechnicalReport");
	                  	writer_log.addSuperClass(ontology+"TechnicalReport", ontology+"Publication");
	                  }
	                  if(newClass == 5 ){
	                  	_generateBooks();
	                  	newClasses.remove(newClass);
	                  	writer_log.addTypeClass(ontology+"Book");
	                  	writer_log.addSuperClass(ontology+"Book", ontology+"Publication");
	                  }
	                  if(newClass == 6 ){
	                  	_generateThesis();
	                  	newClasses.remove(newClass);
	                  	writer_log.addTypeClass(ontology+"Thesis");
	                  	writer_log.addSuperClass(ontology+"Thesis", ontology+"Publication");
	                  }
	                  if(newClass == 7 ){
	                  	_generateProjects();
	                  	newClasses.remove(newClass);
	                  	writer_log.addTypeClass(ontology+"Project");
	                  }
	                  if(newClass == 8){
	                  	_generateEvents();
	                  	newClasses.remove(newClass);
	                  	writer_log.addTypeClass(ontology+"Event");
	                  }
	              }
	           
	            writer_.end();
	            writer_.endFile();
	            //correction
	            //remainingUnderCourses_.
	            remainingUnderCourses_.clear();
	            
	            for (int i = 0; i < UNDER_COURSE_NUM + (int) (UNDER_COURSE_NUM*evoChange); i++) {
	                remainingUnderCourses_.add(new Integer(i));
	              }
	            remainingGradCourses_.clear();
	              for (int i = 0; i < GRAD_COURSE_NUM + (int) (GRAD_COURSE_NUM*evoChange); i++) {
	                remainingGradCourses_.add(new Integer(i));
	              }
	            remainingWebCourses_.clear();
	              for (int i = 0; i < WEB_COURSE_NUM + (int) (WEB_COURSE_NUM*evoChange); i++) {
	                remainingWebCourses_.add(new Integer(i));
	              }
	            
	            assignWeights(randomFile);	            	            
	           
	        }	
	        evoChange = evoChange + evoVersions*evoChangeOriginal*evoChangeOriginal;
	        
	        writer_log.end();
	        writer_log.endLogFile();
	        
	    	
	    }
	  
	     
  }

  private void assignFilters(ArrayList<Integer> classFilters2) {
	  
	  	 
	  int filters = (int) Math.ceil((classFilters.size()*step));
	  HashSet<Integer> previousSeeds = new HashSet<Integer>();
	  for(int i = 0; i < filters; i++){
		
		//int seed = random_.nextInt(classFilters.size());
		int seed = 8;
		previousSeeds.add(seed);
		while(previousSeeds.contains(seed)){
			//seed = random_.nextInt(classFilters.size());
	  		currentFilters.add(classFilters.get(seed));
	  		classFilters.remove(seed);
	  		previousSeeds.add(seed);
	  		break;
		}
		break;
	  }
	  //step += step;
	  System.out.println(currentFilters.toString());
	  	  
  }

/**
   * Creates a university.
   * @param index Index of the university.
   */
  private void _generateUniv(int index) {
    //this transformation guarantees no different pairs of (index, baseSeed) generate the same data
    seed_ = baseSeed_ * (Integer.MAX_VALUE + 1) + index;
    random_.setSeed(seed_);

    //determine department number
    instances_[CS_C_DEPT].num = _getRandomFromRange(DEPT_MIN, DEPT_MAX);
    instances_[CS_C_DEPT].count = 0;
    //generate departments    
    //if(!evo){
    	
	    for (int i = 0; i < instances_[CS_C_DEPT].num; i++) {
	      //System.out.println("index: " + i);
	      _generateDept(index, i);
	      String fileName = "University"+index+"_"+i+".owl";	      	     
	      assignWeights(fileName);
	      
	    }
    //}
   /* else{
    	int depts = instances_[CS_C_DEPT].num;
        int deptChange = (int) (depts * evoChange);
        System.out.println("dept Change: " + deptChange);
        //_setInstanceInfo();
        //Writer writer_ = this.writer_;//new OwlWriter(this);
        //writer_.start();
        String oldUserDir = userDir;
        for(int j = 0; j < evoVersions ; j++){
        	writer_ = new OwlWriter(this);        	
        	System.out.println("userDir: " + userDir);
        	
        	File baseDir = new File(oldUserDir+"/v"+j);
        	baseDir.mkdirs();
        	userDir = baseDir.getAbsolutePath();
        	for (int i = 0; i < depts; i++) {
        	      _generateDept(index, i);
        	}        	
        	File[] files = new File(System.getProperty("user.dir")).listFiles();
        	for(File file : files){
        		if(!file.getName().contains("owl")) continue;
        		file.renameTo(new File(baseDir.getAbsolutePath()+"/"+file.getName()));		
        	}
        	depts += deptChange;
        }
        //writer_.end();
        
    }*/
  }

  /**
   * Creates a department.
   * @param univIndex Index of the current university.
   * @param index Index of the department.
   * NOTE: Use univIndex instead of instances[CS_C_UNIV].count till generateASection(CS_C_UNIV, ) is invoked.
   */
  private void _generateDept(int univIndex, int index) {
	totalDeptsV0++;
    String fileName = System.getProperty("user.dir") + "\\" +
        _getName(CS_C_UNIV, univIndex) + INDEX_DELIMITER + index + _getFileSuffix();
    writer_.startFile(fileName);

    //reset
    _setInstanceInfo();
    underCourses_.clear();
    gradCourses_.clear();
    remainingUnderCourses_.clear();
    remainingGradCourses_.clear();
    remainingWebCourses_.clear();
    for (int i = 0; i < UNDER_COURSE_NUM; i++) {
      remainingUnderCourses_.add(new Integer(i));
    }
    for (int i = 0; i < GRAD_COURSE_NUM; i++) {
      remainingGradCourses_.add(new Integer(i));
    }
    for (int i = 0; i < WEB_COURSE_NUM; i++) {
        remainingWebCourses_.add(new Integer(i));
      }
    publications_.clear();
    projects_.clear();
    for (int i = 0; i < CLASS_NUM; i++) {
      instances_[i].logNum = 0;
    }
    for (int i = 0; i < PROP_NUM; i++) {
      properties_[i].logNum = 0;
    }

    //decide the chair
    chair_ = random_.nextInt(instances_[CS_C_FULLPROF].total);

    if (index == 0) {
      _generateASection(CS_C_UNIV, univIndex);
    }
    _generateASection(CS_C_DEPT, index);
    for (int i = CS_C_DEPT + 1; i < CLASS_NUM; i++) {
      instances_[i].count = 0;
      for (int j = 0; j < instances_[i].num; j++) {
        _generateASection(i, j);
      }
    }

    _generatePublications();
   /* _generateConferencePublications();
    _generateJournalPublications();
    _generateTechnicalReports();
    _generateBooks();
    _generateThesis();
    _generateProjects();
    _generateEvents();*/
    _generateCourses();
    _generateRaTa();

    System.out.println(fileName + " generated");
    String bar = "";
    for (int i = 0; i < fileName.length(); i++)
      bar += '-';
    log_.println(bar);
    log_.println(fileName);
    log_.println(bar);
    _generateComments();
    writer_.endFile();
  }
  
  

  ///////////////////////////////////////////////////////////////////////////
  //instance generation

  /**
   * Generates an instance of the specified class
   * @param classType Type of the instance.
   * @param index Index of the instance.
   */
  private void _generateASection(int classType, int index) {
    _updateCount(classType);
   
    /*if(classType != CS_C_UNIV && classType != CS_C_DEPT && classType != CS_C_FACULTY && classType != CS_C_PROF &&
    		 classType != CS_C_COURSE && !currentFilters.contains(classType)) 
    	return;*/
    
    switch (classType) {
      case CS_C_UNIV:
        _generateAUniv(index);
        break;
      case CS_C_DEPT:
        _generateADept(index);
        break;
      case CS_C_FACULTY:
        _generateAFaculty(index);
        break;
      case CS_C_PROF:
        _generateAProf(index);
        break;
      case CS_C_FULLPROF:
        _generateAFullProf(index);
        break;
      case CS_C_ASSOPROF:
        _generateAnAssociateProfessor(index);
        break;
      case CS_C_ASSTPROF:
        _generateAnAssistantProfessor(index);
        break;
      case CS_C_LECTURER:
        _generateALecturer(index);
        break;
      case CS_C_UNDERSTUD:
        _generateAnUndergraduateStudent(index);
        break;
      case CS_C_GRADSTUD:
        _generateAGradudateStudent(index);
        break;
      case CS_C_COURSE:
        _generateACourse(index);
        break;
      case CS_C_GRADCOURSE:
        _generateAGraduateCourse(index);
        break;
      case CS_C_WEBCOURSE:
          _generateAWebCourse(index);
          break;
      case CS_C_RESEARCHGROUP:
        _generateAResearchGroup(index);
        break;
      case CS_C_VISITINGPROF:
          _generateAVisitingProf(index);
          break;
      case CS_C_VISITSTUD:
    	  _generateAVisitingStudent(index);
          break;          
      default:
        break;
    }
  }
  
  
 
  public void assignWeights(String fileName){
	  
	       	     
	  
      InstanceCount[] thisInstCount = new InstanceCount[instances_.length];	      
      List<Integer> hello = new ArrayList<Integer>(changeWeights.keySet());
      Collections.sort(hello);
      for(int i = 0; i < instances_.length; i++){
    	  thisInstCount[i] = new InstanceCount();
    	  thisInstCount[i].count = instances_[i].count;
    	  thisInstCount[i].logNum = instances_[i].logNum;
    	  thisInstCount[i].logTotal = instances_[i].logTotal;
    	  thisInstCount[i].num = instances_[i].num;
    	  thisInstCount[i].total = instances_[i].total;
      }
      /*for(Integer nextClass : hello){
    	  thisInstCount[nextClass] = new InstanceCount();
    	  thisInstCount[nextClass].count = instances_[nextClass].count;
    	  thisInstCount[nextClass].logNum = instances_[nextClass].logNum;
    	  thisInstCount[nextClass].logTotal = instances_[nextClass].logTotal;
    	  thisInstCount[nextClass].num = instances_[nextClass].num;
    	  thisInstCount[nextClass].total = instances_[nextClass].total;

      	//System.out.println("int: "+ nextClass + ", weight: " + changeWeights.get(nextClass) + ", "
      		//	+ "count: " + instances_[nextClass].total);
      }*/
      
      fileInstanceMap.put(fileName, thisInstCount);
	  changeWeights = new HashMap<Integer, Double>();
	  changeWeights.put(CS_C_UNIV, Math.floor(instances_[CS_C_UNIV].num*evoChange));
	  changeWeights.put(CS_C_DEPT, Math.floor(instances_[CS_C_DEPT].num*evoChange)*0.2);
	  changeWeights.put(CS_C_FACULTY, Math.floor(instances_[CS_C_FACULTY].num*evoChange));
	  changeWeights.put(CS_C_PROF, Math.floor(instances_[CS_C_PROF].num*evoChange));
	  changeWeights.put(CS_C_FULLPROF, Math.floor(instances_[CS_C_FULLPROF].num*evoChange)*16);
	  changeWeights.put(CS_C_ASSOPROF, Math.floor(instances_[CS_C_ASSOPROF].num*evoChange)*22);
	  changeWeights.put(CS_C_ASSTPROF, Math.floor(instances_[CS_C_ASSTPROF].num*evoChange)*18);	  
	  changeWeights.put(CS_C_LECTURER, Math.floor(instances_[CS_C_LECTURER].num*evoChange)*11);
	  changeWeights.put(CS_C_UNDERSTUD, Math.floor(instances_[CS_C_UNDERSTUD].num*evoChange)*35);
	  changeWeights.put(CS_C_GRADSTUD, Math.floor(instances_[CS_C_GRADSTUD].num*evoChange)*24);	  
	  changeWeights.put(CS_C_COURSE, Math.floor(UNDER_COURSE_NUM*evoChange));//*20);
	  changeWeights.put(CS_C_GRADCOURSE, Math.floor(GRAD_COURSE_NUM*evoChange));//*10.1);	  
	  changeWeights.put(CS_C_RESEARCHGROUP, Math.floor(instances_[CS_C_RESEARCHGROUP].num*evoChange)*28);
	  changeWeights.put(CS_C_VISITINGPROF, Math.floor(instances_[CS_C_VISITINGPROF].num*evoChange)*18);
	  changeWeights.put(CS_C_VISITSTUD, Math.floor(instances_[CS_C_VISITSTUD].num*evoChange)*14);
	  changeWeights.put(CS_C_PROJECT, Math.floor(instances_[CS_C_PROJECT].num*evoChange)*14);
	  changeWeights.put(CS_C_WEBCOURSE, Math.floor(WEB_COURSE_NUM*evoChange));//*61);
	  changeWeights.put(CS_C_PUBLICATION, Math.floor(instances_[CS_C_PUBLICATION].num*evoChange)*61);
	  fileWeightsMap.put(fileName, changeWeights);
  }
  /**
   * Generates a university instance.
   * @param index Index of the instance.
   */
  private void _generateAUniv(int index) {
    writer_.startSection(CS_C_UNIV, _getId(CS_C_UNIV, index));
    writer_.addProperty(CS_P_NAME, _getRelativeName(CS_C_UNIV, index), false);
    writer_.endSection(CS_C_UNIV);
  }

  /**
   * Generates a department instance.
   * @param index Index of the department.
   */
  private void _generateADept(int index) {
    writer_.startSection(CS_C_DEPT, _getId(CS_C_DEPT, index));
    writer_.addProperty(CS_P_NAME, _getRelativeName(CS_C_DEPT, index), false);
    writer_.addProperty(CS_P_SUBORGANIZATIONOF, CS_C_UNIV,
                       _getId(CS_C_UNIV, instances_[CS_C_UNIV].count - 1));
    writer_.endSection(CS_C_DEPT);
  }

  /**
   * Generates a faculty instance.
   * @param index Index of the faculty.
   */
  private void _generateAFaculty(int index) {
    writer_.startSection(CS_C_FACULTY, _getId(CS_C_FACULTY, index));
    _generateAFaculty_a(CS_C_FACULTY, index, _getId(CS_C_FACULTY, index));
    writer_.endSection(CS_C_FACULTY);
  }

  /**
   * Generates properties for the specified faculty instance.
   * @param type Type of the faculty.
   * @param index Index of the instance within its type.
   */
  private void _generateAFaculty_a(int type, int index, String id) {
    int indexInFaculty;
    int courseNum;
    int courseIndex;
    boolean dup;
    CourseInfo course;

    indexInFaculty = instances_[CS_C_FACULTY].count - 1;

    writer_.addProperty(CS_P_NAME, _getRelativeName(type, index), false);
    if(globalVersionTrigger){
    	writer_log.addPropertyInstance(id, ontology+"#name", _getRelativeName(type, index), false );    	
    }
    //undergradutate courses
    courseNum = _getRandomFromRange(FACULTY_COURSE_MIN, FACULTY_COURSE_MAX);
    for (int i = 0; i < courseNum; i++) {
      courseIndex = _AssignCourse(indexInFaculty);
      writer_.addProperty(CS_P_TEACHEROF, _getId(CS_C_COURSE, courseIndex), true);
      if(globalVersionTrigger){
      	writer_log.addPropertyInstance(id, ontology+"#teacherOf", _getId(CS_C_COURSE, courseIndex), true );    	
      }
    }
    //gradutate courses
    courseNum = _getRandomFromRange(FACULTY_GRADCOURSE_MIN, FACULTY_GRADCOURSE_MAX);
    for (int i = 0; i < courseNum; i++) {
      courseIndex = _AssignGraduateCourse(indexInFaculty);
      writer_.addProperty(CS_P_TEACHEROF, _getId(CS_C_GRADCOURSE, courseIndex), true);
      if(globalVersionTrigger){
        	writer_log.addPropertyInstance(id, ontology+"#teacherOf", _getId(CS_C_GRADCOURSE, courseIndex), true );    	
      }
    }
    for (int i = 0; i < courseNum; i++) {
        courseIndex = _AssignWebCourse(indexInFaculty);
        writer_.addProperty(CS_P_TEACHEROF, _getId(CS_C_WEBCOURSE, courseIndex), true);
        if(globalVersionTrigger){
        	writer_log.addPropertyInstance(id, ontology+"#teacherOf", _getId(CS_C_WEBCOURSE, courseIndex), true );    	
        }
      }
    //person properties
    String n = _getId(CS_C_UNIV, random_.nextInt(UNIV_NUM));
    writer_.addProperty(CS_P_UNDERGRADFROM, CS_C_UNIV, n);
    if(globalVersionTrigger){
    	writer_log.addPropertyInstance(id, ontology+"#undergraduateDegreeFrom", n, true );    	
    }
    n = _getId(CS_C_UNIV, random_.nextInt(UNIV_NUM));
    writer_.addProperty(CS_P_GRADFROM, CS_C_UNIV,
                       n);
    if(globalVersionTrigger){
    	writer_log.addPropertyInstance(id, ontology+"#mastersDegreeFrom", n, true );    	
    }
    n = _getId(CS_C_UNIV, random_.nextInt(UNIV_NUM));
    writer_.addProperty(CS_P_DOCFROM, CS_C_UNIV,
                       n);
    if(globalVersionTrigger){
    	writer_log.addPropertyInstance(id, ontology+"#doctoralDegreeFrom", n, true );    	
    }
    writer_.addProperty(CS_P_WORKSFOR,
                       _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);
    if(globalVersionTrigger){
    	writer_log.addPropertyInstance(id, ontology+"#worksFor",  _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true );    	
    }
    writer_.addProperty(CS_P_EMAIL, _getEmail(type, index), false);
    if(globalVersionTrigger){
    	writer_log.addPropertyInstance(id, ontology+"#email",  _getEmail(type, index), false );    	
    }
    writer_.addProperty(CS_P_TELEPHONE, "xxx-xxx-xxxx", false);
    if(globalVersionTrigger){
    	writer_log.addPropertyInstance(id, ontology+"#telephone",  "xxx-xxx-xxxx", false );    	
    }
  }

  /**
   * Assigns an undergraduate course to the specified faculty.
   * @param indexInFaculty Index of the faculty.
   * @return Index of the selected course in the pool.
   */
  private int _AssignCourse(int indexInFaculty) {
    //NOTE: this line, although overriden by the next one, is deliberately kept
    // to guarantee identical random number generation to the previous version.
    int pos = _getRandomFromRange(0, remainingUnderCourses_.size() - 1);
    pos = 0; //fetch courses in sequence

    CourseInfo course = new CourseInfo();
    course.indexInFaculty = indexInFaculty;
    course.globalIndex = ( (Integer) remainingUnderCourses_.get(pos)).intValue();
    underCourses_.add(course);

    remainingUnderCourses_.remove(pos);

    return course.globalIndex;
  }

  /**
   * Assigns a graduate course to the specified faculty.
   * @param indexInFaculty Index of the faculty.
   * @return Index of the selected course in the pool.
   */
  private int _AssignGraduateCourse(int indexInFaculty) {
    //NOTE: this line, although overriden by the next one, is deliberately kept
    // to guarantee identical random number generation to the previous version.
    int pos = _getRandomFromRange(0, remainingGradCourses_.size() - 1);
    pos = 0; //fetch courses in sequence

    CourseInfo course = new CourseInfo();
    course.indexInFaculty = indexInFaculty;
    course.globalIndex = ( (Integer) remainingGradCourses_.get(pos)).intValue();
    gradCourses_.add(course);

    remainingGradCourses_.remove(pos);

    return course.globalIndex;
  }
  
  private int _AssignWebCourse(int indexInFaculty) {
	    //NOTE: this line, although overriden by the next one, is deliberately kept
	    // to guarantee identical random number generation to the previous version.
	    int pos = _getRandomFromRange(0, remainingWebCourses_.size() - 1);
	    pos = 0; //fetch courses in sequence

	    CourseInfo course = new CourseInfo();
	    course.indexInFaculty = indexInFaculty;
	    course.globalIndex = ( (Integer) remainingWebCourses_.get(pos)).intValue();
	    webCourses_.add(course);

	    remainingWebCourses_.remove(pos);

	    return course.globalIndex;
	  }

  /**
   * Generates a professor instance.
   * @param index Index of the professor.
   */
  private void _generateAProf(int index) {
    writer_.startSection(CS_C_PROF, _getId(CS_C_PROF, index));
    _generateAProf_a(CS_C_PROF, index, _getId(CS_C_PROF, index));
    writer_.endSection(CS_C_PROF);
  }

  /**
   * Generates properties for a professor instance.
   * @param type Type of the professor.
   * @param index Index of the intance within its type.
   */
  private void _generateAProf_a(int type, int index, String id) {
    _generateAFaculty_a(type, index, id);
    String ri = _getRelativeName(CS_C_RESEARCH,
            random_.nextInt(RESEARCH_NUM));
    writer_.addProperty(CS_P_RESEARCHINTEREST,
                      ri , false);
    if(globalVersionTrigger){
    	writer_log.addPropertyInstance(id, ontology+"#researchInterest", ri, false );    	
    }
  }

  /**
   * Generates a full professor instances.
   * @param index Index of the full professor.
   */
  private void _generateAFullProf(int index) {
    String id;

    id = _getId(CS_C_FULLPROF, index);
    writer_.startSection(CS_C_FULLPROF, id);
    if(globalVersionTrigger){
    	writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#FullProfessor", true);    	
    }
    _generateAProf_a(CS_C_FULLPROF, index, id);
    if (index == chair_) {
      writer_.addProperty(CS_P_HEADOF,
                         _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);
      if(globalVersionTrigger){
      	writer_log.addPropertyInstance(id, ontology+"#headOf", _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);    	
      }
    }   
    writer_.endSection(CS_C_FULLPROF);
    _assignFacultyPublications(id, FULLPROF_PUB_MIN, FULLPROF_PUB_MAX);
  }
  
  /**
   * Generates a full professor instances.
   * @param index Index of the full professor.
   */
  private void _generateAVisitingProf(int index) {
    String id;

    id = _getId(CS_C_VISITINGPROF, index);
    writer_.startSection(CS_C_VISITINGPROF, id);
    if(globalVersionTrigger){
    	writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#VisitingProfessor", true);    	
    }
    _generateAProf_a(CS_C_VISITINGPROF, index, id);
    writer_.addProperty(CS_P_VISITSASPROF, CS_C_UNIV,
            _getId(CS_C_UNIV, random_.nextInt(UNIV_NUM)));
    if(globalVersionTrigger){
    	writer_log.addPropertyInstance(id, ontology+"#visitsAsProfessor", _getId(CS_C_UNIV, random_.nextInt(UNIV_NUM)), true);    	
    }
    String n = _getRandomFromRange(1, 10)+" month(s)";
    writer_.addProperty(CS_P_VISITDURATION, n ,false);
    if(globalVersionTrigger){
    	writer_log.addPropertyInstance(id, ontology+"#visitDuration", n, false);    	
    }
    writer_.endSection(CS_C_VISITINGPROF);
   
  }

  /**
   * Generates an associate professor instance.
   * @param index Index of the associate professor.
   */
  private void _generateAnAssociateProfessor(int index) {
    String id = _getId(CS_C_ASSOPROF, index);
    writer_.startSection(CS_C_ASSOPROF, id);
    if(globalVersionTrigger){
    	writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#AssociateProfessor", true);    	
    }
    _generateAProf_a(CS_C_ASSOPROF, index, id);
    writer_.endSection(CS_C_ASSOPROF);
    _assignFacultyPublications(id, ASSOPROF_PUB_MIN, ASSOPROF_PUB_MAX);
    
  }

  /**
   * Generates an assistant professor instance.
   * @param index Index of the assistant professor.
   */
  private void _generateAnAssistantProfessor(int index) {
    String id = _getId(CS_C_ASSTPROF, index);
    writer_.startSection(CS_C_ASSTPROF, id);
    if(globalVersionTrigger){
    	writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#AssistantProfessor", true);    	
    }
    _generateAProf_a(CS_C_ASSTPROF, index, id);
    writer_.endSection(CS_C_ASSTPROF);
    _assignFacultyPublications(id, ASSTPROF_PUB_MIN, ASSTPROF_PUB_MAX);
  }

  /**
   * Generates a lecturer instance.
   * @param index Index of the lecturer.
   */
  private void _generateALecturer(int index) {
    String id = _getId(CS_C_LECTURER, index);
    writer_.startSection(CS_C_LECTURER, id);
    if(globalVersionTrigger){
    	writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#Lecturer", true);    	
    }
    _generateAFaculty_a(CS_C_LECTURER, index, id);
    writer_.endSection(CS_C_LECTURER);
    _assignFacultyPublications(id, LEC_PUB_MIN, LEC_PUB_MAX);
  }

  /**
   * Assigns publications to the specified faculty.
   * @param author Id of the faculty
   * @param min Minimum number of publications
   * @param max Maximum number of publications
   */
  private void _assignFacultyPublications(String author, int min, int max) {
    int num;
    PublicationInfo publication;

    num = _getRandomFromRange(min, max);
    for (int i = 0; i < num; i++) {
      publication = new PublicationInfo();
      publication.id = _getId(CS_C_PUBLICATION, i, author);
      publication.name = _getRelativeName(CS_C_PUBLICATION, i);
      publication.authors = new ArrayList();
      publication.authors.add(author);
      publications_.add(publication);
    }
  }

  /**
   * Assigns publications to the specified graduate student. The publications are
   * chosen from some faculties'.
   * @param author Id of the graduate student.
   * @param min Minimum number of publications.
   * @param max Maximum number of publications.
   */
  private void _assignGraduateStudentPublications(String author, int min, int max) {
    int num;
    PublicationInfo publication;

    num = _getRandomFromRange(min, max);
    ArrayList list = _getRandomList(num, 0, publications_.size() - 1);
    for (int i = 0; i < list.size(); i++) {
      publication = (PublicationInfo) publications_.get( ( (Integer) list.get(i)).
                                               intValue());
      publication.authors.add(author);
    }
  }

  /**
   * Generates publication instances. These publications are assigned to some faculties
   * and graduate students before.
   */
  private void _generatePublications() {
    for (int i = 0; i < publications_.size(); i++) {
      _generateAPublication( (PublicationInfo) publications_.get(i));
      if(globalVersionTrigger){
    	  PublicationInfo pub = (PublicationInfo) publications_.get(i);
    	  writer_log.addPropertyInstance(pub.id, RDF.type.getURI(), ontology+"#Publication", true);
    	  writer_log.addPropertyInstance(pub.id, ontology+"#name", pub.name, false);
    	  for(String author : (ArrayList<String>) pub.authors){
    		  writer_log.addPropertyInstance(pub.id, ontology+"#author", author, true);  
    	  }
      }
    }
  }
  
  private void _generateConferencePublications() {
	    for (int i = 0; i < publications_.size()/25; i++) {
	      _generateAConferencePublication( (PublicationInfo) publications_.get(i));
	    }
	  }
  
  private void _generateJournalPublications() {
	    for (int i = 0; i < publications_.size()/20; i++) {
	      _generateAJournalPublication( (PublicationInfo) publications_.get(i));
	    }
	  }
  
  private void _generateTechnicalReports() {
	    for (int i = 0; i < publications_.size()/10; i++) {
	      _generateATechnicalReport( (PublicationInfo) publications_.get(i));
	    }
	  }
  
  private void _generateBooks() {
	    for (int i = 0; i < publications_.size()/15; i++) {
	      _generateABook( (PublicationInfo) publications_.get(i));
	    }
	  }
  
  private void _generateThesis() {
	    for (int i = 0; i < publications_.size()/20; i++) {
	      _generateAThesis( (PublicationInfo) publications_.get(i));
	    }
	  }
  
  private void _generateProjects() {
	  int num = _getRandomFromRange(PROJECT_NUM_MIN, PROJECT_NUM_MAX);
	    for (int i = 0; i < num; i++) {
	      _generateAProject( _getRandomFromRange(150, 43958) );
	    }
	  }
  
  private void _generateEvents() {
	  int num = _getRandomFromRange(EVENT_NUM_MIN, EVENT_NUM_MAX);
	    for (int i = 0; i < num; i++) {
	      _generateAnEvent(_getRandomFromRange(150, 43958) );
	    }
	  }

  /**
   * Generates a publication instance.
   * @param publication Information of the publication.
   */
  private void _generateAPublication(PublicationInfo publication) {
    writer_.startSection(CS_C_PUBLICATION, publication.id);
    writer_.addProperty(CS_P_NAME, publication.name, false);
    for (int i = 0; i < publication.authors.size(); i++) {
      writer_.addProperty(CS_P_PUBLICATIONAUTHOR,
                         (String) publication.authors.get(i), true);
    }
    writer_.endSection(CS_C_PUBLICATION);
  }
  
  private void _generateAConferencePublication(PublicationInfo publication) {
	    instances_[CS_C_CONFPUBLICATION].count++;
	    instances_[CS_C_CONFPUBLICATION].num++;
	    writer_.startSection(CS_C_CONFPUBLICATION, publication.id);
	    writer_.addProperty(CS_P_NAME, "Conference " + publication.name, false);
	    for (int i = 0; i < publication.authors.size(); i++) {
	      writer_.addProperty(CS_P_PUBLICATIONAUTHOR,
	                         (String) publication.authors.get(i), true);
	    }
	    String n = "Venue"+_getRandomFromRange(0, 1500);
	    writer_.addProperty(CS_P_VENUE, n, false);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(publication.id, ontology+"#venue", n, false);
	    }
	    n = ""+_getRandomFromRange(1, 28)+"-"+_getRandomFromRange(1, 12)+"-"+_getRandomFromRange(2000, 2016);
	    writer_.addProperty(CS_P_DATE, n, false);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(publication.id, ontology+"#date", n, false);
	    }
	    n = "ISBN"+System.nanoTime();
	    writer_.addProperty(CS_P_ISBN, n, false);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(publication.id, ontology+"#isbn", n, false);
	    }
	    writer_.endSection(CS_C_CONFPUBLICATION);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(publication.id, RDF.type.getURI(), ontology+"#ConferencePublication", true);
	    	  writer_log.addPropertyInstance(publication.id, ontology+"#name", "Conference " + publication.name, false);
	    	  for(String author : (ArrayList<String>) publication.authors){
	    		  writer_log.addPropertyInstance(publication.id, ontology+"#author", author, true);  
	    	  }	    	  
	     }
	   
}
  
  private void _generateAJournalPublication(PublicationInfo publication) {
	  instances_[CS_C_JOURNALPUBLICATION].count++;
	  instances_[CS_C_JOURNALPUBLICATION].num++;
	    writer_.startSection(CS_C_JOURNALPUBLICATION, publication.id);
	    String n = "Journal " + _getRandomFromRange(1, 2500);
	    publication.name = n;
	    writer_.addProperty(CS_P_NAME, publication.name, false);
	    for (int i = 0; i < publication.authors.size(); i++) {
	      writer_.addProperty(CS_P_PUBLICATIONAUTHOR,
	                         (String) publication.authors.get(i), true);
	    }
	    int advisor = random_.nextInt(instances_[CS_C_FULLPROF].total);
	    writer_.addProperty(CS_P_EDITORINCHIEF,
	                         _getId(CS_C_FULLPROF, advisor), true);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(publication.id, ontology+"#editorInChief",_getId(CS_C_FULLPROF, advisor) , true);
	    }
	    n = ""+_getRandomFromRange(1, 28)+"-"+_getRandomFromRange(1, 12)+"-"+_getRandomFromRange(2000, 2016);
	    writer_.addProperty(CS_P_DATE,n, false);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(publication.id, ontology+"#date",n , false);
	    }
	    n = "ISBN"+System.nanoTime();
	    writer_.addProperty(CS_P_ISBN, n, false);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(publication.id, ontology+"#isbn",n , false);
	    }
	    writer_.endSection(CS_C_JOURNALPUBLICATION);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(publication.id, RDF.type.getURI(), ontology+"#JournalPublication", true);
	    	  writer_log.addPropertyInstance(publication.id, ontology+"#name", "Conference " + publication.name, false);
	    	  for(String author : (ArrayList<String>) publication.authors){
	    		  writer_log.addPropertyInstance(publication.id, ontology+"#author", author, true);  
	    	  }	    	  
	     }
	  }
  
  private void _generateATechnicalReport(PublicationInfo publication) {
	  instances_[CS_C_TECHNICALREPORT].count++;
	  instances_[CS_C_TECHNICALREPORT].num++;
	    writer_.startSection(CS_C_TECHNICALREPORT, publication.id);
	    
	    publication.name = "TR " + _getRandomFromRange(1, 2500);
	    writer_.addProperty(CS_P_NAME, publication.name, false);
	    
	    for (int i = 0; i < publication.authors.size(); i++) {
	      writer_.addProperty(CS_P_PUBLICATIONAUTHOR,
	                         (String) publication.authors.get(i), true);
	    }	    
	    String n = ""+_getRandomFromRange(1, 28)+"-"+_getRandomFromRange(1, 12)+"-"+_getRandomFromRange(2000, 2016);
	    writer_.addProperty(CS_P_DATE, n, false);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(publication.id, ontology+"#date",n , false);
	    }
	    n = "TR-ID-"+System.nanoTime();
	    writer_.addProperty(CS_P_REPORTID, n, false);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(publication.id, ontology+"#technicalReportID",n , false);
	    }
	    writer_.endSection(CS_C_TECHNICALREPORT);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(publication.id, RDF.type.getURI(), ontology+"#TechnicalReport", true);
	    	  writer_log.addPropertyInstance(publication.id, ontology+"#name", publication.name, false);
	    	  for(String author : (ArrayList<String>) publication.authors){
	    		  writer_log.addPropertyInstance(publication.id, ontology+"#author", author, true);  
	    	  }	    	  
	     }
	  }
  
  private void _generateABook(PublicationInfo publication) {
	  instances_[CS_C_BOOK].count++;
	  instances_[CS_C_BOOK].num++;
	    writer_.startSection(CS_C_BOOK, publication.id);
	    publication.name = "Book " + _getRandomFromRange(1, 2500);
	    writer_.addProperty(CS_P_NAME, publication.name, false);
	    for (int i = 0; i < publication.authors.size(); i++) {
	      writer_.addProperty(CS_P_PUBLICATIONAUTHOR,
	                         (String) publication.authors.get(i), true);
	    }	    
	    String n = ""+_getRandomFromRange(1, 28)+"-"+_getRandomFromRange(1, 12)+"-"+_getRandomFromRange(2000, 2016);
	    writer_.addProperty(CS_P_DATE, n, false);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(publication.id, ontology+"#date",n , false);
	    }
	    n = "ISBN"+System.nanoTime();
	    writer_.addProperty(CS_P_ISBN, n, false);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(publication.id, ontology+"#isbn",n , false);
	    }
	    writer_.endSection(CS_C_BOOK);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(publication.id, RDF.type.getURI(), ontology+"#Book", true);
	    	  writer_log.addPropertyInstance(publication.id, ontology+"#name", "Conference " + publication.name, false);
	    	  for(String author : (ArrayList<String>) publication.authors){
	    		  writer_log.addPropertyInstance(publication.id, ontology+"#author", author, true);  
	    	  }	    	  
	     }
	  }

  private void _generateAThesis(PublicationInfo publication) {
	  instances_[CS_C_THESIS].count++;
	  instances_[CS_C_THESIS].num++;
	    writer_.startSection(CS_C_THESIS, publication.id);
	    publication.name = "Thesis " + _getRandomFromRange(1, 2500);
	    writer_.addProperty(CS_P_NAME, publication.name, false);
	    for (int i = 0; i < publication.authors.size(); i++) {
	      writer_.addProperty(CS_P_PUBLICATIONAUTHOR,
	                         (String) publication.authors.get(i), true);
	    }	    
	    String n = ""+_getRandomFromRange(1, 28)+"-"+_getRandomFromRange(1, 12)+"-"+_getRandomFromRange(2000, 2016);
	    writer_.addProperty(CS_P_DATE, n, false);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(publication.id, ontology+"#date",n , false);
	    }
	    int advisor = random_.nextInt(instances_[CS_C_FULLPROF].total);
	    writer_.addProperty(CS_P_SUPERVISOR,
	                         _getId(CS_C_FULLPROF, advisor), true);	    
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(publication.id, ontology+"#supervisor",_getId(CS_C_FULLPROF, advisor) , true);
	    }
	    writer_.endSection(CS_C_THESIS);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(publication.id, RDF.type.getURI(), ontology+"#Thesis", true);
	    	  writer_log.addPropertyInstance(publication.id, ontology+"#name", publication.name, false);
	    	  for(String author : (ArrayList<String>) publication.authors){
	    		  writer_log.addPropertyInstance(publication.id, ontology+"#author", author, true);  
	    	  }	    	  
	     }
	  }
  
  private void _generateAProject(int index) {
	  	String id = _getId(CS_C_PROJECT, index);
	    writer_.startSection(CS_C_PROJECT, id);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#Project", true);	    	  	    	 
	    }
	    writer_.addProperty(CS_P_NAME, "Project", false);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(id, ontology+"#name", "Project", false);	    	  	    	 
	    }
	    String n = _getRandomFromRange(1,50)+" month(s)";
	    writer_.addProperty(CS_P_DURATION, n, false);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(id, ontology+"#duration", n, false);	    	  	    	 
	    }
	    n = _getRandomFromRange(100000,5000000)+" euro(s)";
	    writer_.addProperty(CS_P_BUDGET,  n, false);	    
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(id, ontology+"#budget", n, false);	    	  	    	 
	    }
	    int advisor = random_.nextInt(instances_[CS_C_FULLPROF].total);
	    writer_.addProperty(CS_P_SCIENTIFICADVISOR,
	                         _getId(CS_C_FULLPROF, advisor), true);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(id, ontology+"#scientificAdvisor", _getId(CS_C_FULLPROF, advisor), true);	    	  	    	 
	    }
	    int group = random_.nextInt(instances_[CS_C_RESEARCHGROUP].total);
	    writer_.addProperty(CS_P_RESEARCHGROUP,
	    		_getId(CS_C_RESEARCHGROUP, group), true);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(id, ontology+"#researchGroup", _getId(CS_C_RESEARCHGROUP, group), true);	    	  	    	 
	    }
	    int uni = random_.nextInt(instances_[CS_C_UNIV].total);
	    writer_.addProperty(CS_P_FUNDEDBY,
	    		_getId(CS_C_UNIV, uni), true);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(id, ontology+"#fundedBy", _getId(CS_C_UNIV, uni), true);	    	  	    	 
	    }
	    
	    writer_.endSection(CS_C_PROJECT);
	    
	  }
  
  private void _generateAnEvent(int index) {
	  	String id = _getId(CS_C_EVENT, index);
	    writer_.startSection(CS_C_EVENT, id);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#Event", true);	    	  	    	 
	    }
	    writer_.addProperty(CS_P_NAME, "Event", false);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(id, ontology+"#name", "Event", false);	    	  	    	 
	    }
	    String n = CS_EVENT_TYPES[_getRandomFromRange(0,2)];
	    writer_.addProperty(CS_P_EVENTTYPE, n, false);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(id, ontology+"#eventType", n, false);	    	  	    	 
	    }
	    int organizer = random_.nextInt(instances_[CS_C_FULLPROF].total);
	    writer_.addProperty(CS_P_EVENTORGANIZER,
	                         _getId(CS_C_FULLPROF, organizer), true);	 
	    if(globalVersionTrigger){
	    	  writer_log.addPropertyInstance(id, ontology+"#eventOrganizer", _getId(CS_C_FULLPROF, organizer), true);	    	  	    	 
	    }
	    writer_.endSection(CS_C_EVENT);
	  }

  /**
   * Generates properties for the specified student instance.
   * @param type Type of the student.
   * @param index Index of the instance within its type.
   */
  private void _generateAStudent_a(int type, int index, String id) {
    writer_.addProperty(CS_P_NAME, _getRelativeName(type, index), false);    
    if(globalVersionTrigger){	    	  
    	  writer_log.addPropertyInstance(id, ontology+"#name", _getRelativeName(type, index), false);	    	  	    	 
    }
    writer_.addProperty(CS_P_MEMBEROF,
                       _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);
    if(globalVersionTrigger){	    	  
  	  writer_log.addPropertyInstance(id, ontology+"#memberOf", _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);	    	  	    	 
    }
    writer_.addProperty(CS_P_EMAIL, _getEmail(type, index), false);
    if(globalVersionTrigger){	    	  
  	  writer_log.addPropertyInstance(id, ontology+"#email", _getEmail(type, index), false);	    	  	    	 
    }
    writer_.addProperty(CS_P_TELEPHONE, "xxx-xxx-xxxx", false);
    if(globalVersionTrigger){	    	  
    	  writer_log.addPropertyInstance(id, ontology+"#telephone", "xxx-xxx-xxxx", false);	    	  	    	 
      }
  }

  /**
   * Generates an undergraduate student instance.
   * @param index Index of the undergraduate student.
   */
  private void _generateAnUndergraduateStudent(int index) {
    int n;
    ArrayList list;
    String id = _getId(CS_C_UNDERSTUD, index);
    writer_.startSection(CS_C_UNDERSTUD, id);
    _generateAStudent_a(CS_C_UNDERSTUD, index, id);
    n = _getRandomFromRange(UNDERSTUD_COURSE_MIN, UNDERSTUD_COURSE_MAX);
    list = _getRandomList(n, 0, underCourses_.size() - 1);
    for (int i = 0; i < list.size(); i++) {
      CourseInfo info = (CourseInfo) underCourses_.get( ( (Integer) list.get(i)).
          intValue());
      writer_.addProperty(CS_P_TAKECOURSE, _getId(CS_C_COURSE, info.globalIndex), true);
      if(globalVersionTrigger){	    	  
    	  writer_log.addPropertyInstance(id, ontology+"#takesCourse", _getId(CS_C_COURSE, info.globalIndex), true);	    	  	    	 
      }
    }
    if (0 == random_.nextInt(R_UNDERSTUD_ADVISOR)) {
    	String ad = _selectAdvisor();
      writer_.addProperty(CS_P_ADVISOR, ad, true);
      if(globalVersionTrigger){	    	  
    	  writer_log.addPropertyInstance(id, ontology+"#advisor", ad, true);	    	  	    	 
      }
    }
    writer_.endSection(CS_C_UNDERSTUD);
  }
  
  private void _generateAVisitingStudent(int index) {
	    int n;
	    ArrayList list;	    
	    String id = _getId(CS_C_VISITSTUD, index);
	    writer_.startSection(CS_C_VISITSTUD, id);
	    _generateAStudent_a(CS_C_VISITSTUD, index, id);
	    n = _getRandomFromRange(VISITSTUD_COURSE_MIN, VISITSTUD_COURSE_MAX);
	    list = _getRandomList(n, 0, underCourses_.size() - 1);
	    for (int i = 0; i < list.size(); i++) {
	      CourseInfo info = (CourseInfo) underCourses_.get( ( (Integer) list.get(i)).
	          intValue());
	      writer_.addProperty(CS_P_TAKECOURSE, _getId(CS_C_COURSE, info.globalIndex), true);
	      if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(id, ontology+"#takesCourse", _getId(CS_C_COURSE, info.globalIndex), true);	    	  	    	 
	      }
	    }
	    writer_.addProperty(CS_P_VISITSASSTUD,
                _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(id, ontology+"#visitsAsStudent", _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);	    	  	    	 
	      }
	    String d = _getRandomFromRange(1, 10)+" month(s)";
	    writer_.addProperty(CS_P_VISITDURATION, d,false);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(id, ontology+"#visitDuration", d, false);	    	  	    	 
	      }
	    if (0 == random_.nextInt(R_VISITSTUD_ADVISOR)) {
	    	String ad = _selectAdvisor();
	      writer_.addProperty(CS_P_ADVISOR, ad, true);
	      if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(id, ontology+"#advisor", ad, true);	    	  	    	 
	      }
	    }
	    writer_.endSection(CS_C_VISITSTUD);
	  }

  /**
   * Generates a graduate student instance.
   * @param index Index of the graduate student.
   */
  private void _generateAGradudateStudent(int index) {
    int n;
    ArrayList list;
    String id;

    id = _getId(CS_C_GRADSTUD, index);
    writer_.startSection(CS_C_GRADSTUD, id);
    _generateAStudent_a(CS_C_GRADSTUD, index, id);
    n = _getRandomFromRange(GRADSTUD_COURSE_MIN, GRADSTUD_COURSE_MAX);
    list = _getRandomList(n, 0, gradCourses_.size() - 1);
    for (int i = 0; i < list.size(); i++) {
      CourseInfo info = (CourseInfo) gradCourses_.get( ( (Integer) list.get(i)).
          intValue());
      writer_.addProperty(CS_P_TAKECOURSE,
                         _getId(CS_C_GRADCOURSE, info.globalIndex), true);
      if(globalVersionTrigger){	    	  
    	  writer_log.addPropertyInstance(id, ontology+"#takesCourse", _getId(CS_C_GRADCOURSE, info.globalIndex), true);	    	  	    	 
      }
    }
    writer_.addProperty(CS_P_UNDERGRADFROM, CS_C_UNIV,
                       _getId(CS_C_UNIV, random_.nextInt(UNIV_NUM)));
    if(globalVersionTrigger){	    	  
  	  writer_log.addPropertyInstance(id, ontology+"#undergraduateDegreeFrom", _getId(CS_C_UNIV, random_.nextInt(UNIV_NUM)), true);	    	  	    	 
    }
    if (0 == random_.nextInt(R_GRADSTUD_ADVISOR)) {
    	String ad = _selectAdvisor();
    	writer_.addProperty(CS_P_ADVISOR, _selectAdvisor(), true);
	      if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(id, ontology+"#advisor", ad, true);	    	  	    	 
	      }
      
    }
    
    _assignGraduateStudentPublications(id, GRADSTUD_PUB_MIN, GRADSTUD_PUB_MAX);
    writer_.endSection(CS_C_GRADSTUD);
  }

  /**
   * Select an advisor from the professors.
   * @return Id of the selected professor.
   */
  private String _selectAdvisor() {
    int profType;
    int index;

    profType = _getRandomFromRange(CS_C_FULLPROF, CS_C_ASSTPROF);
    index = random_.nextInt(instances_[profType].total);
    return _getId(profType, index);
  }

  /**
   * Generates a TA instance according to the specified information.
   * @param ta Information of the TA.
   */
  private void _generateATa(TaInfo ta) {
	  String id = _getId(CS_C_GRADSTUD, ta.indexInGradStud);
    writer_.startAboutSection(CS_C_TA, id);
    if(globalVersionTrigger){	    	  
  	  writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#TeachingAssistant", true);	    	  	    	 
    }
    writer_.addProperty(CS_P_TAOF, _getId(CS_C_COURSE, ta.indexInCourse), true);
    if(globalVersionTrigger){	    	  
    	  writer_log.addPropertyInstance(id, ontology+"#teachingAssistantOf", _getId(CS_C_COURSE, ta.indexInCourse), true);	    	  	    	 
      }
    writer_.endSection(CS_C_TA);
  }

  /**
   * Generates an RA instance according to the specified information.
   * @param ra Information of the RA.
   */
  private void _generateAnRa(RaInfo ra) {
    writer_.startAboutSection(CS_C_RA, _getId(CS_C_GRADSTUD, ra.indexInGradStud));
    writer_.endSection(CS_C_RA);
  }

  /**
   * Generates a course instance.
   * @param index Index of the course.
   */
  private void _generateACourse(int index) {
	  String id = _getId(CS_C_COURSE, index);
    writer_.startSection(CS_C_COURSE, id);
    if(globalVersionTrigger){	    	  
    	  writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#Course", true);	    	  	    	 
      }
    writer_.addProperty(CS_P_NAME,
                       _getRelativeName(CS_C_COURSE, index), false);
    if(globalVersionTrigger){	    	  
  	  writer_log.addPropertyInstance(id, ontology+"#name", _getRelativeName(CS_C_COURSE, index), false);	    	  	    	 
    }
    writer_.endSection(CS_C_COURSE);
  }

  /**
   * Generates a graduate course instance.
   * @param index Index of the graduate course.
   */
  private void _generateAGraduateCourse(int index) {
	  String id = _getId(CS_C_GRADCOURSE, index);
    writer_.startSection(CS_C_GRADCOURSE, id);
    if(globalVersionTrigger){	    	  
  	  writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#GraduateCourse", true);	    	  	    	 
    }
    writer_.addProperty(CS_P_NAME,
                       _getRelativeName(CS_C_GRADCOURSE, index), false);
    if(globalVersionTrigger){	    	  
    	  writer_log.addPropertyInstance(id, ontology+"#name", _getRelativeName(CS_C_GRADCOURSE, index), false);	    	  	    	 
      }
    writer_.endSection(CS_C_GRADCOURSE);
  }
  
  private void _generateAWebCourse(int index) {
	  String id = _getId(CS_C_WEBCOURSE, index);
	    writer_.startSection(CS_C_WEBCOURSE, id);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#WebCourse", true);	    	  	    	 
	      }
	    writer_.addProperty(CS_P_NAME,
	                       _getRelativeName(CS_C_WEBCOURSE, index), false);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(id, ontology+"#name", _getRelativeName(CS_C_WEBCOURSE, index), false);	    	  	    	 
	      }
	    String n = "topic"+_getRandomFromRange(1, 150);
	    writer_.addProperty(CS_P_TOPIC,
                n, false);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(id, ontology+"#topic", n, false);	    	  	    	 
	      }
	    n = "http://example.com/webcourse/"+_getRandomFromRange(1, 150);
	    writer_.addProperty(CS_P_URL,
               n , false);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(id, ontology+"#url", n, false);	    	  	    	 
	      }
	    int hours = _getRandomFromRange(9, 17);
	    writer_.addProperty(CS_P_HOURS,
                hours+"-"+(int)(hours+2), false);
	    if(globalVersionTrigger){	    	  
	    	  writer_log.addPropertyInstance(id, ontology+"#hours", hours+"-"+(int)(hours+2), false);	    	  	    	 
	      }
	    writer_.endSection(CS_C_WEBCOURSE);
	  }

  /**
   * Generates course/graduate course instances. These course are assigned to some
   * faculties before.
   */
  private void _generateCourses() {
    for (int i = 0; i < underCourses_.size(); i++) {
      _generateACourse( ( (CourseInfo) underCourses_.get(i)).globalIndex);
    }
    for (int i = 0; i < gradCourses_.size(); i++) {
      _generateAGraduateCourse( ( (CourseInfo) gradCourses_.get(i)).globalIndex);
    }
    for (int i = 0; i < webCourses_.size(); i++) {
        _generateAWebCourse( ( (CourseInfo) webCourses_.get(i)).globalIndex);
      }
  }

  /**
   * Chooses RAs and TAs from graduate student and generates their instances accordingly.
   */
  private void _generateRaTa() {
	  if(instances_[CS_C_TA].total == 0) return;
    ArrayList list, courseList;
    TaInfo ta;
    RaInfo ra;
    ArrayList tas, ras;
    int i;

    tas = new ArrayList();
    ras = new ArrayList();
    list = _getRandomList(instances_[CS_C_TA].total + instances_[CS_C_RA].total,
                      0, instances_[CS_C_GRADSTUD].total - 1);
    System.out.println("underCourses " + (underCourses_.size() - 1));
    System.out.println("instances ta " + instances_[CS_C_TA].total);
    courseList = _getRandomList(instances_[CS_C_TA].total, 0,
                            underCourses_.size() - 1);

    for (i = 0; i < courseList.size(); i++) {
      ta = new TaInfo();
      ta.indexInGradStud = ( (Integer) list.get(i)).intValue();
      ta.indexInCourse = ( (CourseInfo) underCourses_.get( ( (Integer)
          courseList.get(i)).intValue())).globalIndex;
      _generateATa(ta);
    }
    while (i < list.size()) {
      ra = new RaInfo();
      ra.indexInGradStud = ( (Integer) list.get(i)).intValue();
      _generateAnRa(ra);
      i++;
    }
  }

  /**
   * Generates a research group instance.
   * @param index Index of the research group.
   */
  private void _generateAResearchGroup(int index) {
    String id;
    id = _getId(CS_C_RESEARCHGROUP, index);
    if(globalVersionTrigger){	    	  
  	  writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#ResearchGroup", true);	    	  	    	 
    }
    writer_.startSection(CS_C_RESEARCHGROUP, id);
    writer_.addProperty(CS_P_SUBORGANIZATIONOF,
                       _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);
    if(globalVersionTrigger){	    	  
  	  writer_log.addPropertyInstance(id, ontology+"#subOrganizationOf", _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);	    	  	    	 
    }
    writer_.endSection(CS_C_RESEARCHGROUP);
  }

  ///////////////////////////////////////////////////////////////////////////

  /**
   * @return Suffix of the data file.
   */
  private String _getFileSuffix() {
    return isDaml_ ? ".daml" : ".owl";
  }

  /**
   * Gets the id of the specified instance.
   * @param classType Type of the instance.
   * @param index Index of the instance within its type.
   * @return Id of the instance.
   */
  private String _getId(int classType, int index) {
    String id;

    switch (classType) {
      case CS_C_UNIV:
        id = "http://www." + _getRelativeName(classType, index) + ".edu";
        break;
      case CS_C_DEPT:
        id = "http://www." + _getRelativeName(classType, index) + "." +
            _getRelativeName(CS_C_UNIV, instances_[CS_C_UNIV].count - 1) +
            ".edu";
        break;
      default:
        id = _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1) + ID_DELIMITER +
            _getRelativeName(classType, index);
        break;
    }

    return id;
  }

  /**
   * Gets the id of the specified instance.
   * @param classType Type of the instance.
   * @param index Index of the instance within its type.
   * @param param Auxiliary parameter.
   * @return Id of the instance.
   */
  private String _getId(int classType, int index, String param) {
    String id;

    switch (classType) {
      case CS_C_PUBLICATION:
        //NOTE: param is author id
        id = param + ID_DELIMITER + CLASS_TOKEN[classType] + index;
        break;
      default:
        id = _getId(classType, index);
        break;
    }

    return id;
  }

  /**
   * Gets the globally unique name of the specified instance.
   * @param classType Type of the instance.
   * @param index Index of the instance within its type.
   * @return Name of the instance.
   */
  private String _getName(int classType, int index) {
    String name;

    switch (classType) {
      case CS_C_UNIV:
        name = _getRelativeName(classType, index);
        break;
      case CS_C_DEPT:
        name = _getRelativeName(classType, index) + INDEX_DELIMITER +
            (instances_[CS_C_UNIV].count - 1);
        break;
      //NOTE: Assume departments with the same index share the same pool of courses and researches
      case CS_C_COURSE:
      case CS_C_GRADCOURSE:
      case CS_C_RESEARCH:
        name = _getRelativeName(classType, index) + INDEX_DELIMITER +
            (instances_[CS_C_DEPT].count - 1);
        break;
      default:
        name = _getRelativeName(classType, index) + INDEX_DELIMITER +
            (instances_[CS_C_DEPT].count - 1) + INDEX_DELIMITER +
            (instances_[CS_C_UNIV].count - 1);
        break;
    }

    return name;
  }

  /**
   * Gets the name of the specified instance that is unique within a department.
   * @param classType Type of the instance.
   * @param index Index of the instance within its type.
   * @return Name of the instance.
   */
  private String _getRelativeName(int classType, int index) {
    String name;

    switch (classType) {
      case CS_C_UNIV:
        //should be unique too!
        name = CLASS_TOKEN[classType] + index;
        break;
      case CS_C_DEPT:
        name = CLASS_TOKEN[classType] + index;
        break;
      default:
        name = CLASS_TOKEN[classType] + index;
        break;
    }

    return name;
  }

  /**
   * Gets the email address of the specified instance.
   * @param classType Type of the instance.
   * @param index Index of the instance within its type.
   * @return The email address of the instance.
   */
  private String _getEmail(int classType, int index) {
    String email = "";

    switch (classType) {
      case CS_C_UNIV:
        email += _getRelativeName(classType, index) + "@" +
            _getRelativeName(classType, index) + ".edu";
        break;
      case CS_C_DEPT:
        email += _getRelativeName(classType, index) + "@" +
            _getRelativeName(classType, index) + "." +
            _getRelativeName(CS_C_UNIV, instances_[CS_C_UNIV].count - 1) + ".edu";
        break;
      default:
        email += _getRelativeName(classType, index) + "@" +
            _getRelativeName(CS_C_DEPT, instances_[CS_C_DEPT].count - 1) +
            "." + _getRelativeName(CS_C_UNIV, instances_[CS_C_UNIV].count - 1) +
            ".edu";
        break;
    }

    return email;
  }

  /**
   * Increases by 1 the instance count of the specified class. This also includes
   * the increase of the instacne count of all its super class.
   * @param classType Type of the instance.
   */
  private void _updateCount(int classType) {
    int subClass, superClass;

    instances_[classType].count++;
    subClass = classType;
    while ( (superClass = CLASS_INFO[subClass][INDEX_SUPER]) != CS_C_NULL) {
      instances_[superClass].count++;
      subClass = superClass;
    }
  }
  
  private void _updateCount(int classType, InstanceCount inst) {
	    int subClass, superClass;

	    inst.count++;
	    subClass = classType;
	    while ( (superClass = CLASS_INFO[subClass][INDEX_SUPER]) != CS_C_NULL) {
	      inst.count++;
	      subClass = superClass;
	    }
	  }

  /**
   * Creates a list of the specified number of integers without duplication which
   * are randomly selected from the specified range.
   * @param num Number of the integers.
   * @param min Minimum value of selectable integer.
   * @param max Maximum value of selectable integer.
   * @return So generated list of integers.
   */
  private ArrayList _getRandomList(int num, int min, int max) {
    ArrayList list = new ArrayList();
    ArrayList tmp = new ArrayList();
    for (int i = min; i <= max; i++) {
      tmp.add(new Integer(i));
    }
    
    for (int i = 0; i < num; i++) {
    	if(tmp.size() > 1){
	      int pos = _getRandomFromRange(0, tmp.size() - 1);
	      list.add( (Integer) tmp.get(pos));
	      tmp.remove(pos);
    	}
    }

    return list;
  }

  /**
   * Randomly selects a integer from the specified range.
   * @param min Minimum value of the selectable integer.
   * @param max Maximum value of the selectable integer.
   * @return The selected integer.
   */
  private int _getRandomFromRange(int min, int max) {
	  
    return min + random_.nextInt(max - min + 1);
  }

  /**
   * Outputs log information to both the log file and the screen after a department
   * is generated.
   */
  private void _generateComments() {
    int classInstNum = 0; //total class instance num in this department
    long totalClassInstNum = 0l; //total class instance num so far
    int propInstNum = 0; //total property instance num in this department
    long totalPropInstNum = 0l; //total property instance num so far
    String comment;

    comment = "External Seed=" + baseSeed_ + " Interal Seed=" + seed_;
    log_.println(comment);
    log_.println();

    comment = "CLASS INSTANCE# TOTAL-SO-FAR";
    log_.println(comment);
    comment = "----------------------------";
    log_.println(comment);
    for (int i = 0; i < CLASS_NUM; i++) {
      comment = CLASS_TOKEN[i] + " " + instances_[i].logNum + " " +
          instances_[i].logTotal;
      log_.println(comment);
      classInstNum += instances_[i].logNum;
      totalClassInstNum += instances_[i].logTotal;
    }
    log_.println();
    comment = "TOTAL: " + classInstNum;
    log_.println(comment);
    comment = "TOTAL SO FAR: " + totalClassInstNum;
    log_.println(comment);

    comment = "PROPERTY---INSTANCE NUM";
    log_.println();
    comment = "PROPERTY INSTANCE# TOTAL-SO-FAR";
    log_.println(comment);
    comment = "-------------------------------";
    log_.println(comment);
    for (int i = 0; i < PROP_NUM; i++) {
      comment = PROP_TOKEN[i] + " " + properties_[i].logNum;
      comment = comment + " " + properties_[i].logTotal;
      log_.println(comment);
      propInstNum += properties_[i].logNum;
      totalPropInstNum += properties_[i].logTotal;
    }
    log_.println();
    comment = "TOTAL: " + propInstNum;
    log_.println(comment);
    comment = "TOTAL SO FAR: " + totalPropInstNum;
    log_.println(comment);

    System.out.println("CLASS INSTANCE #: " + classInstNum + ", TOTAL SO FAR: " +
                       totalClassInstNum);
    System.out.println("PROPERTY INSTANCE #: " + propInstNum +
                       ", TOTAL SO FAR: " + totalPropInstNum);
    System.out.println();

    log_.println();
  }

}