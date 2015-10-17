# While Idea Plugin
Custom plugin defining While language. PSI-components are generated with [Grammar-Kit](https://github.com/JetBrains/Grammar-Kit))

# Standalone usage
* Clone the project
* Init and update sub-module (git submodule update --init)
* Run gradle wrapper (gradlew or gradlew.bat depending or your OS)
* Run, for example, *test* task (type *"gradlew test"* in terminal)
* For generating IDEA-based project files use *setup* task (type *"gradlew setup"* in terminal).
* To run this project from IDEA you need [IntelliJ IDEA SDK](https://www.jetbrains.com/idea/help/sdks-intellij-idea.html). You can download IDEA sources [here](http://www.jetbrains.org/display/IJOS/Download).
* Notice that sub-module "printergenerator" generates source code for While Idea Plugin. In that case, it should be run first (gradle does this automatically if you run *test/build/assemble etc*. but not *setup* task). 
 

