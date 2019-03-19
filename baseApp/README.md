baseApp
==============

Based on Vaadin 8 - Multi module example.

Template for a full-blown Vaadin application that only requires a Servlet 3.0 container to run (no other JEE dependencies).


Project Structure
=================

The project consists of the following three modules:

- parent project: common metadata and configuration
- baseApp-addon: addon module, custom server and client side code 
- baseApp-baseUI: base application module that all main application ui modules should be based uppon (eg. baseApp-ui and DpApp-ui).
- baseApp-ui: a main application module
- DpApp-ui: another main application module
- service-client: a simple Rest client to be used by Application UI modules to retrieve data from WS. It implements an abstract WS interface with only 2 methods: 'execute' and 'executeUpdate'. Their difference is that the latter will use a 'transaction'. 
- baseApp-backend: base backend module, contains the base classes for handling WS calls. It implements the same WS interface as the service-client above. Each App ui should have its own 'backend' project.
- utils***: a set of helper modules used by all other modules

Workflow
========

- Check out this project
- Create your own Vaadin 8 Multi module project and update dependencies to include: baseApp-baseUI, utils***, service-client, baseApp-backend
- In UI module:
  - the UI class should extend BaseUI
  - delete demo services from UI class
  - delete init(VaadinRequest vaadinRequest) method (from UI class ) or override it.
  - replace with 'VaadinServlet' with 'BaseAppServlet'
  - create an 'AppLayout' class that implements 'AppLayout' interface and use it in UI class as generic along with a UserBean class. An existing Layout class from baseApp-baseUI can be used as base. 
  - add and implement abstract methods from BaseUI
  - add property bundle files in resources folder that match UI's class name and add related static code from BaseUI to load them.
  - Copy (if needed) styles from baseTheme.scss from one of the demo UI projects.
  - override 'getViewDefinitions' in UI class and add menu structure. super() provides an example. In time the override will not be needed as data will be retrieved from a WS.

 - need to revise
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
