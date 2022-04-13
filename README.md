# Falling Words Game

In this falling word game the game start with 0 score and 3 lives. The user have to choose 15 falling words correct or wrong. 
During this 15 times word faliing the user can use their 3 lives. Each live will cut when the word fall or he/she give a worng answer.

In the code base the falling word game using MVI architecture with clean architecture approach. The app is divided into 4 modules e.g., app,
core, domain-fallingwords, feature-fallingwords. The app module contains the all the app feature and application class. The core module is the common module 
for every other module of the project. All common classes are placed here which can be used by every other feature. 

The domain-fallingwords contains the domain and data layer of the clean architecture. Data layer is resposible for data which can come from different sources
in this location file. This layer fetch the data and convert it to domain compatable data model and pass it to domain layer. Domain layer is the business logic
layer for the feature. It can handle the business logic for loading data and game engine. GameUseCase is reposible for calculating the next question,
the score and the game end. 

The feature module contains the domain module as dependency and the presentation layer of the clean code architecture. User creates the events when 
interacting to the views. The events are handled by the view model and change the model which is observed by the views. This architecture supports
unidirectional data flow from view - intent - model - view. 

## Time Distribution
I have spend around 7 hour to do it. 
- Planning, project structure creation and dependency resolve 1:30h
- Data layer takes 1h
- Domain layer takes 1h
- view, view model and animation 1:30h
- unit testing 1:30h
- Documenting 30 mins

## Improvement
- Work on the animation
- More levels of the game
- UI testing
