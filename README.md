[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/YIEa2Fm0)

# Week-Three-Lab

# Lab 3: Tourist Observer
In this lab, you will implement the observer pattern and ensure that it when it enters a museum, the artwork is displayed. 


## Introduction
A common travel game is to find the letters in a word on the license plates of vehicles that you see on the road. For example,
the license plates ATT-351 and CPO-OO3 would be sufficient to match “CAT”.

For this lab, you will be given code that draws cars, buses, and a person moving around a map. As the person comes into contact with a car,
that car’s plate will be used to match letters  in various keywords. The first keyword you will match is “MSOE”. Your implementation
will use the Observer Pattern.

### The ```CityMap```
The key class in this is ```CityMap```. It displays a portion of Milwaukee. This map is just for reference - your game pieces will not
actually  follow the roads and will even float across the harbor. This map also includes a ```Museum``` represented by a rectangular area you
can walk through. The moving objects are subclasses of the abstract class MobileEntity. We use the term “tagging” to mean colliding with
another object. The base class ```Taggable``` are those objects that can collide with other objects.

### The Given Code
The code is given to draw the actors on the map. It is essential to implement a simple game based on a tourist visiting the city and
completing challenges. For this part, the following challenge must be done:
- Enter the Art Museum

## The Challenge
In this challenge, the game displays a picture of The Wood Gatherer whenever the person enters the rectangle representing the location
of the Milwaukee Art Museum (MAM). This picture is shown in the status area on the right hand side of the screen.

When the program starts up, it will display just the message Challenge: Find art in the status area. Once the user enters the museum,
it will display the woodcutter image. The woodcutter image is available in the repository folder src/mketour/img. You can model the
code for loading it after the code that loads the images for the cars, buses, and person.



## Design Requirements
- [x] Begin by sketching a minimal solution diagram (or another diagram if required by your instructor) that illustrates the
  Observer pattern for the challenge you are implementing. As you sketch this diagram, think about how the responsibilities of
  the classes will change.

- [x] Plan to make only minimal edits to the existing classes, inserting all new responsibilities into new classes, and only
  adding minimal code to tie the existing classes to the new ones. Note that it IS OK to extend existing responsibilities of the
  existing classes. For example:
    - [x] It is fine to extend existing classes or add associations to existing classes.
    - [x] It is likely that you will need to add calls to ```notifyObservers``` in existing methods.
    - [x] You may find it necessary to add getters to existing classes so other code can access private data. But do avoid the following:
        - [x] Adding any GUI responsibilities to code in the actors folder
        - [x] Writing event handlers that use instanceof; you will likely need to use instanceof when you set up the initial observers,
          but the observer handlers should not need to do this.

- [x] Identify where the events that are generated in the code and make those parts of your code the concrete subjects. The remainder
  of the pattern classes can be new classes.

- [x] Find out what data to pass in the operation to update the observers. Such as passing the objects being tagged, the subject that is
  producing the event, or data such as license plates.

- [x] Make sure how you are using the pattern isolates your observers and your subjects. There should be very little communication between
  your observers and your subjects.

## Constraint Requirements
Your design must satisfy the following constraints:
- [x] The challenges should be observers, and you will need to create a subject (not TagEvent) as a separate class.
- [x] The subject and observer cannot be the same class.
- [x] Do ensure your design uses reasonable names. For example, a bus cannot be an event, and a painting cannot be an observer.
  Observers should be observing events, not things.
- [x] Once you are satisfied with your design, begin the implementation. Again, be sure to only make minimal edits to the existing
  classes to tie them into the new classes you add to support the strategies. 


## Implementation Needs
A major part of this assignment is implementing the Observer Pattern. You are to write your own classes implementing this pattern:
- [x] Do not use ```Java.util.Observable``` or ```Java.util.Observer```. You may use your own names for the Observer Pattern; it may be
  helpful
  to use names that are specific to the problem such as “CarObserver” or “TagObserver.”
- [x] Avoid unnecessary casting. For example, you should not need to cast from an Object to Taggable.
- [x] Do not need to modify existing classes other than to introduce code tying to new pattern classes. Add only one line of code in
  ```CityMap.addEntities()``` to support the new challenge. In particular, new GUI code should not be added to the CityMap.
- [x] Check that ```CityMap.DEBUG_LEVEL``` is 0 so any debug code is disabled. 

# Week-Four-Lab

# Lab 4: Tourist Observer
In this lab, you will implement the MSOE Challenge and the BUS Challenge

## The MSOE Challenge
This challenge involves finding the word "MSOE" by tagging cars with those letters anywhere in their license plates. Here are the
following requirements to be satisfied:
- [x] The challenge should be available from the start of the game and is signaled by the message
  "Challenge: Find all the letters in "MSOE" at the top of the status area
- [x] The cars can be tagged in any order, and each letter is shown in the challenge area when matched
- [x] When the user has found all four letters, display the message "MSOE CHALLENGE COMPLETED", replacing the message showing
  which of the "MSOE" letters have been found

## The Easter Egg Challenge
Here are the following requirements to be satisfied:
- [x] The user only discovers this challenge once the person tags a Bus.
- [x] The message for this challenge "Challenge: Find all letters in "BUS"
- [x] When all three letters have been found, replace the challenge with "BUS CHALLENGE COMPLETED"

Note that there is an overlap between the target words, so in some cases finding a letter can make progress in both license plate
challenges.

- [x] If a player finds the S for the initial challenge before activating the bus challenge, then an S needs to be found again to
  satisfy the bus challenge. 
