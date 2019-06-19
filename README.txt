1. The 'groovierspring' project directory is setup like this:

   etc: Contains script to create database
   lib: Contains all project dependencies
   src: Source files (Java, Groovy, XML, etc.)
   test: Test files
   scripts: Contains Groovy scripts

2. Setting up the project:

   There is an IntelliJ project already set up for you.
   You should install the JetGroovy plugin for Groovy/Grails support.

   In Eclipse you can create a new project from existing sources.
   Note that some classes are written in Groovy so you'll need the Eclipse Groovy plugin.

3. Running the examples:

   There are two Groovy scripts you can use in src directory:
     a. GenerateInvoices.groovy:
        Generates a PDF invoice in the target directory. Uses the Spring application context
        configured in src/applicationContext.xml. Loops continuously generating a new PDF
        every five seconds.

        Edit the applicationContext.xml file to use the different configuration options, and
        after editing re-run the script. If using the database example you will first need to
        create a MySql database using the SQL in etc/create-db.sql.

    b. BeanBuilderGenerateInvoices.groovy:
       Generates a PDF invoice in the target directory. Uses the Grails BeanBuilder to create
       the Spring application context. Loops continuously generating a new PDF every five seconds.

        Edit the script directly to change which BeanBuilder code block is used to switch
        configurations. After editing, re-run the script. If using the database example you will
        first need to create a MySql database using the SQL in etc/create-db.sql.
