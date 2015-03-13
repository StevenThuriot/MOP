# Project for Methodologies for design of software

As an assignment for a class called `Methodologies for design of software`, we had to create a task manager written in Java. The main goal was to think about design patterns and refactoring.

As far as refactoring goes, we found that splitting up some long methods and creating data containers for methods with huge parameter lists really improved the quality of our code.
We decided to use the `Model View Controller` pattern as a structure for our project. We followed it to the letter and completely isolated our model from the view. Everything has to go through the controllers. Objects that could be passed to the view all implemented a certain interface. The controllers only passed these objects to the view as an implementation of this interface. As a result, the view has no idea whatsoever about any of the objects in the model or how they work.

We used quite a number of design patterns in this project, but other than MVC, the most important ones are the `State Pattern` and `Prototype`.

A task can have several states, each with their own set of rules. This, of course, is a perfect job for the state pattern. This pattern does a wonderful job to keep the system in a stable state as it only allows calling certain methods when in a certain state, each with their unique implementation.

The system had to be completely dynamic as well. Rather than hard coding the objects, they have to be built up from an XML file. The prototype pattern is ideal for this. The system parses the XML file and creates a list of prototypes which it then stores in a repository. When we then want to use a certain type of object, the system makes a call to the repository which clones the prototype implementation, resulting in a completely dynamic object for the system to use.
