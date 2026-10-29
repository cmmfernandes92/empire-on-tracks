# Supplementary Specification (FURPS+)

## Functionality

_Specifies functionalities that:  
&nbsp; &nbsp; (i) are common across several US/UC;  
&nbsp; &nbsp; (ii) are not related to US/UC, namely: Audit, Reporting and Security._


  *  **Security**
     * "All those who wish to use the application must be authenticated with a password of seven alphanumeric characters, including three capital letters and two digits."
       
     * "During the simulation execution current budget and date should be
       permanently visible"


## Usability

_Evaluates the user interface. It has several subcategories,
among them: error prevention; interface aesthetics and design; help and
documentation; consistency and standards._

* **Localizability**
   * "The application documentation must be in English."


## Reliability

_Refers to the integrity, compliance and interoperability of the software. The requirements to be considered are: frequency and severity of failure, possibility of recovery, possibility of prediction, accuracy, average time between failures._

* TBD
  
  

## Performance

_Evaluates the performance requirements of the software, namely: response time, start-up time, recovery time, memory consumption, CPU usage, load capacity and application availability._

  * TBD

## Supportability

_The supportability requirements gathers several characteristics, such as:
testability, adaptability, maintainability, compatibility,
configurability, installability, scalability and more._

* **Testability**
   * "The development team must implement unit tests for all methods, except for the methods that implement Input/Output operations. Unit tests should be implemented using the JUnit 5 framework."
   * "The JaCoCo plugin should be used to generate the coverage report."

* **Maintainability**
  * "To increase the solution maintainability and respect good software development practices, the implementation must follow a Test-Driven Development (TDD) approach."
  * "The class structure must be designed to allow easy maintenance and the addition of new features following the best Object-Oriented (OO) practices."


## +

### Design Constraints

_Specifies or constraints the system design process. Examples may include: programming languages, software process, mandatory standards/patterns, use of development tools, class library, etc._

* **Programming languages**
   * "The application must be developed in Java language."
   * "The software solution to be developed must be composed of a set of Java applications that must meet the requirements."


* **Software process**
  * "Therefore, the agile SCRUM approach must be used to manage teamwork in each four-week SPRINT."


* **Mandatory standards/patterns**
    * "During system development, the team must: (i) adopt best practices for identifying requirements and for OO software analysis and design;"
    * "All the images/figures produced during the software development process should be recorded in SVG format."




  


### Implementation Constraints

_Specifies or constraints the code or construction of a system such as: mandatory standards/patterns, implementation languages,
database integrity, resource limits, operating system._

* **Mandatory standards/patterns**
  * "(ii) adopt recognized coding standards (e.g., CamelCase);"
  * "(iii) use Javadoc to generate useful documentation for Java code."
  * "The application’s graphical interface is to be developed in JavaFX 11 covering the functionalities related with the Player."

* **Database integrity** 
   * "The application ought to employ object serialization to guarantee the persistence of the data in two successive runs."
  


### Interface Constraints

_Specifies or constraints the features inherent to the interaction of the
system being developed with other external systems._

* "The application’s graphical interface is to be developed in JavaFX 11
  covering the functionalities related with the Player."


### Physical Constraints

_Specifies a limitation or physical requirement regarding the hardware used to house the system, as for example: material, shape, size or weight._

* TBD