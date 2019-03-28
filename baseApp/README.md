baseApp
==============

Based on Vaadin 8 - Multi module example.

Template for a full-blown Vaadin application that only requires a Servlet 3.0 container to run (no other JEE dependencies).


Project Structure
=================

The project consists of the following modules:

- parent project: common metadata and configuration
- baseApp-addon: addon module, custom server and client side code 
- baseApp-baseUI: base application module that all main application ui modules should be based uppon (eg. baseApp-ui and DpApp-ui). It supports the following:
  - multilingual content
  - Logo and Application title in header
  - dynamic menus
  - login/logout
  - multiple user roles and access rights
  - data retrieval from multiple WS
  - static multilingual content using html templates located in theme/layouts folder
- service-client: a simple Rest client to be used by Application UI modules to retrieve data from WS. It implements an abstract WS interface with only 2 methods: 'execute' and 'executeUpdate'. Their difference is that the latter will use a 'transaction'. 
- baseApp-backend: base backend module, contains the base classes for handling WS calls. It implements the same WS interface as the service-client above. Each App ui should have its own 'backend' project. Also it contains default Entity objects. In order to be used they must be included in 'persistence.xml' of the related 'xxcx-backend' module. The entities are:
  - Users (User, UserAccount, UserRole, AccountRole)
  - Application Parameters (ApplicationParameter)
  - Locking itmes (AppLock)
  - User Session monitoring (AppSession)
- utils***: a set of helper modules used by all other modules

- baseApp-ui: a main application module

- DpApp-ui: another main application module
- DpApp-common: a simple helper module (each UI module should have one) that contains classes common both to backend and UI (ie OnlineResult, JobFailureCode, UserBean, etc.)
- DpApp-backend: main backend module of a UI module. Each UI should have one.
- DpApp-service: service module that produces the 'war' with the Application rest services. Each App should have at least one (assuming UI requires any).

Workflow
========

- Check out this project
- Create your own Vaadin 8 Multi module project and update dependencies to include: baseApp-baseUI, utils***, service-client, baseApp-backend. Also add 'Spring' dependencies.
- In UI module:
  - the UI class should extend BaseUI and use classes defined in xxxx-common module as Generics.
  - delete demo services from UI class
  - delete init(VaadinRequest vaadinRequest) method (from UI class) or override it.
  - replace with 'VaadinServlet' with 'BaseAppServlet'
  - create an 'AppLayout' class that implements 'AppLayout' interface and use it in UI class as generic along with a UserBean class. An existing Layout class from baseApp-baseUI can be used as base. 
  - add and implement abstract methods from BaseUI
  - create a class that extends BaseView that will be the base for all application Views. This will help locate abstract methods that need to be implemented and know what will need to be overridden to make everything work. Remember to set Generics defined in BaseView. 
  - add property bundle files in resources folder that match UI's class name and add related static code from BaseUI to load them.
  - Copy (if needed) styles from baseTheme.scss from one of the demo UI projects.
  - override 'retrieveViewClasses' and 'retrieveMenu' in UI class and add menu structure. See example apps to get an idea. In time the override will not be needed as data will be retrieved from a WS.
  - in 'resources' folder create a 'config' folder where configuration files will be placed that define the WS end points that UI will use. The end points should match the ones defined in service module described later on. The name of the files depend on environment variables and start with 'services-config-':
    - deploy.environment: eg 'LOCAL', 'DEV', 'TEST', 'PROD', etc
    - db.type: eg 'MSSQL2012', 'DB2', etc

- create a new Maven module (jar) - 'xxxx-common' to hold data types common to backend and UI. (pom modifications are required afterwards) in that module:
  - create the following packages and classes/enum:
    - xxxx.ws.beans: in there create:
      - a copy of enum JobFailureCode (unless existing one in 'utils' module covers your needs)
      - a copy of enum RoleEnum (unless existing one in 'utils' module covers your needs)
      - a class 'OnlineResult' that extends OnlineBaseResult and uses JobFailureCode created above (JFC generic).
      - a class 'OnlineParams' that extends OnlineBaseParams.
      - an interface 'xxxxWsMethods' that extends interface 'WsMethods'. These methods are the ones to be called using reflection (see WsBaseDataHandler in baseApp-backend module)
      - a class xxxxUserBean that extends UserBean in 'utils' module and uses the RoleEnum created earlier (RE generic).
      Make sure you define generics.

- In Backend module:
  - create the following packages and classes:
    - xxxx.domain: create class DataRepository that extends DataBaseRepository
    - xxxx.ws: create class WsDataHandler that extends WsBaseDataHandler. Also make sure to define generics, 'autowire' its constructor (see DpApp-backend module for example).

- In Service module:
  - create package 'xxxx.service' and in it 'DataControllerService' class. Add Spring annotations '@RestController' and '@RequestMapping("/dcs")'. This is where the end points are defined. It must have at least 'execute' and 'executeUpdate' (the ones declared in DataHandlerClient interface - the class though, is not required to implement that interface). These methods use 'OnlineResult' and 'OnlineParams' defined in 'xxxx-common' module. See DpApp-service as an example of what the class should contain.
  - in resources folder the following must exist (see related structure and files in DpApp-service module as an example):
    - META-INF folder: where persistence.xml should be located
    - application-context.xml: spring config file
    - yyyyyyyy.properties: property file with Database connection info. The name of the property file is defined in 'application-context.xml' and includes environment variables.
    - WEB-INF folder with the following:
      - web.xml
      - xxxxApi-servlet.xml
       
  

need to revise the next

To compile the entire project, run "mvn install" in the parent project.

Other basic workflow steps:

- getting started
- compiling the whole project
  - run "mvn install" in parent project
- developing the application
  - edit code in the ui module
  - run "mvn jetty:run" in ui module
  - open http://localhost:8080/
- adding add-ons to the project
  - edit POM in ui module, add add-ons as dependencies
- client side changes
  - edit code in addon module
  - run "mvn install" in addon module
  - if a new add-on has an embedded theme, run "mvn vaadin:update-theme" in the ui module
- debugging client side code
  - run "mvn vaadin:run-codeserver" in ui module
  - activate Super Dev Mode in the debug window of the application
- enabling production mode
  - set vaadin.productionMode=true system property in the production server

Developing a theme using the runtime compiler
-------------------------

When developing the theme, Vaadin can be configured to compile the SASS based
theme at runtime in the server. This way you can just modify the scss files in
your IDE and reload the browser to see changes.

To use on the runtime compilation, open pom.xml of your UI project and comment 
out the compile-theme goal from vaadin-maven-plugin configuration. To remove 
an existing pre-compiled theme, remove the styles.css file in the theme directory.

When using the runtime compiler, running the application in the "run" mode 
(rather than in "debug" mode) can speed up consecutive theme compilations
significantly.

The production module always automatically precompiles the theme for the production WAR.

Using Vaadin pre-releases
-------------------------

If Vaadin pre-releases are not enabled by default, use the Maven parameter
"-P vaadin-prerelease" or change the activation default value of the profile in pom.xml .
