# cm3110-coursework-JakeMilne
cm3110-coursework-JakeMilne created by GitHub Classroom


<br>
</br></br>
App Description
<br>
Your readme.md file should include an app overview (assessing LO1 and LO3) (up to 300
words):


<b>Jake Milne 2102515</b>         <h1>YouScore</h1><br>
Your scores, your football, without the bookies <br>

YouScore allows you to keep track of your favourite teams and leagues in realtime, without the temptations and distractions of the gambling sponsers which cast a shadow over the industry. We allow you to track goals, bookings and more! <br>
In the app you get a choice of which league to follow, the options and the scottish premiership, and the danish superliga, as well as both leagues playoffs. You can change league whenever you feel like it thanks to our profile system. <br>
YouScore contains 3 main pages, which can be accessed by the navigation bar at the bottom of your screen. These pages are the home page/ upcoming games page, live games page and the profile page.<br>
the profile page allows you to update your league preferences, as well as your username. <br>
the main page shows all upcoming matches for your chosen league, as well as specific details about each game once it is selected, including: the teams playing, the venue, kickoff time and the league positions of each team, when you open the main page, the app will first load the last retrieved set of upcoming games, before getting an updated set from the API, and replacing the old set. This means that even without a connection you will be able to view upcoming games, even though it may not be the most up to date experience. <br>
The live games page shows all ongoing games in the chosen league. Selecting a game will show more details about the game, such as the score, and a timeline of major events in the game. <br> <br> <br>





<h2>App design</h2>
<h3>Main page </h3>
<p>The main page shows upcoming games for your selected league in a recyclerView, and gives you the option to view each game in more detail, by clicking on the respective button </p>

![image](https://github.com/RobertGordonUniversity/cm3110-coursework-JakeMilne/assets/91962700/6e585a1f-d106-46d2-9ac8-55ddbbf26541)
<h3>Upcoming game details </h3>
<p>The game details page shows you more specific information about the game you selected in the main page using textViews. You can also add the game to your calender via the button</p>

![image](https://github.com/RobertGordonUniversity/cm3110-coursework-JakeMilne/assets/91962700/9be0bb25-e36b-4dc9-a9cc-4b7b7c2b9c32)

<h3>Live games </h3>
<p>The live games page shows all live games for your selected league, and gives you the option to view each game in more detail  </p>

![image](https://github.com/RobertGordonUniversity/cm3110-coursework-JakeMilne/assets/91962700/0f853680-b1f3-4775-97d0-b795aa9bdd0f)

<h3>Live game details </h3>
<p>The live details page is accessable from either the live games page, or by clicking on a notification, and is where you can see the live score, as well as other events for your selected game via textViews and a scrollView. You can also enable notifications by following the game using the button. </p>

![image](https://github.com/RobertGordonUniversity/cm3110-coursework-JakeMilne/assets/91962700/ebd1203a-06c5-463e-bb60-7b9968f6433d)

<h3>User Profile </h3>
<p>The user profile page takes user input for the user table using an EditText and a Spinner</p>

![image](https://github.com/RobertGordonUniversity/cm3110-coursework-JakeMilne/assets/91962700/2a1f920c-6795-4104-806b-dcbfc1d23f65)

<h3>Notifications </h3>
<p>here is an example of a notification, clicking on it will open the relevant live details page</p>

![image](https://github.com/RobertGordonUniversity/cm3110-coursework-JakeMilne/assets/91962700/513d1629-4cb3-4cb5-a246-1368e5d3585a)



<h2>Navigation graph</h2>

<p>Below is a screenshot of the navigation graph. When a user opens the app they are sent to the homepage fragment, and from there they can navigate using either buttons or the navigation bar</p>

![image](https://github.com/RobertGordonUniversity/cm3110-coursework-JakeMilne/assets/91962700/7fe18649-0633-4ace-ab7a-7ece318c2b7a)




<h3>Life cycle events</h3>
<h4>Main Activity</h4>
Main activity handles the navigation bar and fragment switching in the onCreate method
<h4>Home Page/Upcoming Games and Live Scores</h4>
The Home Page and Live Scores page have similar life cycle events, onCreateView handles the creation of the recyclerView via an istance of the HomeViewModel class, with the nested onChanged function updating the recycler view, onStart uses the instance of HomeViewClass to load data from the database and the api. onItemClick is used to handle redirects to the game details or live game details fragment. <br>

<h5>Game Details And Live Game Details</h5>
The onCreateView method handles , these details are then displayed to the user in textViews. <br>



<h4>profile</h4>
The onCreateView function is used to initialise and handle the user inputs via an EditText and a spinner, onItemSelected is used to retrieve user input from the spinner and something about editText, which is then saved to the userdb when the save button is pressed <br>



<h2>Web Service</h2>
YouScore makes use of the Sportmonks api, which allows a user to access the scottish and danish first divisions, as well as each leagues playoff rounds.<br>
The three endpoints that this app uses are /football/fixtures/, /football/livescores/inplay, and /football/venues/. A fourth endpoint(/core/types), which produces a list of events types was used to help understand, and format the json for live games in a readable manner, however is no longer used. The showTypes method which called this endpoint is in HomeViewModel<br>
The /football/fixtures/ endpoint was used in the mainSearch function in the homeviewmodel as well as gamedetails fragment to show details about upcoming matches. <br>
The /football/livescores/inplay endpoint was used in the liveSearch function in the liveviewmodel as well as livegamedetails fragment to show details about live matches. <br>
The /football/venues/ was used to retreive venue names, given an id. This was necessary since the livescores endpoint did not include venue name. <br>
examples of the json produced are available in the jsonExamples.txt file <br>



<h2>Room SQLite Database</h2>
The Room SQLite database contains 3 tables,"FollowedGameDatabase" "GameDatabase" and "UserDatabase". <br>
GameDatabase is used to store upcoming games retrieved from the api so that the app can partially function without internet access, showing the user the last set of games when they load the app.
UserDatabase acts as a configuration file, storing the name and league preference of the user so that when the app is loaded, details the user previously entered can be used.
FollowedGameDatabase stores the id and amount of events for each game the user follows<br>


<h2>Mobile Specific Functionalities</h2>
YouScore gives the user the option to add upcoming games to their calender, as well as the option to enable notifications for games in progress, so they never miss an event.




<h2>Reflective Statement</h2>
Overall I think the biggest challenge faced was the testing of anything that made use of the /football/livescores/inplay API endpoint, due to a lack of free APIs that met my requirements I had to go with one that only supplied 2 leagues, because of this there weren't as many testing opportunities as I would have liked, In future I'm going to consider things like this more when I'm deciding on a project. Another thing worth mentioning about the API is the amount of parsing I had to do, the livescores and fixtures endpoints held completely different data, fixtures had the venue name, while livescores didn't. As far as I'm aware livescores doesn't have an option of searching for a specific game id, this meant looping over each live game when I needed to access a specific game, because of this and the previous point I had to write quite a lot of code to handle the responses whereas with another API this may or may not have been an issue. Most of the third party code was taken from generative AI, and I aimed to use it for methods that could be written without much context, however this sometimes required multiple iterations or giving the AI code to reference. I also used it to find solutions for a small number of errors, which worked quite well. Some of the other external sources included the android documentation and youtube. Using non-AI sources such as the documentation felt more benifencial as the code given wasn't tailored to my app already, so I had to interpret the code more than if I used AI.
