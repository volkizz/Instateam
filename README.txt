If you get errors follow this steps

Before starting the h2 db and bootrun:
- At resources/application.properties change the instateam.db.url property to locate your folder with db files in the project.
- Refrash gradle
- In terminal  run "java -cp h2-1.4.190.jar org.h2.tools.Server"
- Bootrun app.
