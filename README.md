# cm3110-coursework-JakeMilne
cm3110-coursework-JakeMilne created by GitHub Classroom

gson
relevance of mobile specific features
colour/design
</br></br>
App Description
<br>
Your readme.md file should include an app overview (assessing LO1 and LO3) (up to 300
words):
<br>
• Developer name (for this, use your name and ID number) <br>
• App title. <br>
• One line, promo text summary of your app. <br>
• List of app functionalities your users will experience in your app. These should relate <br>
to the app requirements outlined above. <br>

<b>Jake Milne 2102515</b>         <h1>App title</h1><br>
Live football, without the bookies <b> change that </b> <br>

App Title allows you to keep track of your favourite teams and leagues in realtime, without the temptations and distractions of the gambling sponsers which cast a shadow over the industry. We allow you to track goals, bookings and more! <br>
In the app you get a choice of which league to follow, the options and the scottish premiership, and the danish superliga, as well as both leagues playoffs. You can change league whenever you feel like it thanks to our profile system. <br>
App title contains 3 main pages, which can be accessed by the navigation bar at the bottom of your screen. These pages are the home page/ upcoming games page, live games page and the profile page.<br>
the profile page allows you to update your league preferences, as well as your username. <br>
the main page shows all upcoming matches for your chosen league, as well as specific details about each game once it is selected, including: <b> DETAILS HERE </b>, when you open the main page, the app will first load the last retrieved set of upcoming games, before getting an updated set from the API, and replacing the old set. This means that even without a connection you will be able to view upcoming games, even though it may not be the most up to date experience. <br>
The live games page shows all ongoing games in the chosen league. Selecting a game will show more details about the game, such as the score, and a timeline of major events in the game. <br>


<br>
The readme.md file should also include details of the app design (assessing LO1 and LO3)
(up to 500 words) including: <br>


<h2>App design</h2>

• A wireframe or screenshot of each page, describing the widgets used to display and
receive information to/from the user and why, and the layout managers used to
arrange them. <br>
• Details of how the functionalities are implemented in each page, for example: <br>
o How users navigate through the app. This can be a screenshot of the
navigation graph visualisation, with appropriate description. <br>
o Which lifecycle events each page will respond to and how. <br>
<h3>Life cycle events</h3>
<h4>Main Activity</h4>
Main activity handles the navigation bar, handling the fragment switching in the onCreate method
<h4>Home Page</h4>
onCreateView handles the creation of the recyclerView via an istance of the HomeViewModel class, with the nested onChanged function updating the recycler view, onStart uses the instance of HomeViewClass to load data from the database and the api. onItemClick is used to handle redirects to the game details fragment. <br>

<h5>Game Details</h5>
The onCreateView method takes the ID of the game selected in the homepage and calls the api to find and store more details about the game, these details are then displayed to the user in textViews. <br>

<h4>Live Scores</h4>
The Live Scores fragment has a similar design to the homepage, with onCreateView, onStart, onItemClick and onChanged providing very similar functionality. The main difference being live scores uses a different API endpoint, which provides data for live games only. The onItemClick function also redirects to a different fragment, this time going to the live game details fragment.
<br>
<h5>Live Game Details</h5>

Again, The Live Game Details fragment is similar to its Game Details counterpart. With the only difference being the data being showed. <br>

<h4>profile</h4>

o Which web service your app uses and how, including details of the data to be
sent/received to/from it. <br>
o The structure of data stored in the Room SQLite database. <br>
The Room SQLite database contains 2 tables, titled "GameDatabase" and "UserDatabase". <br>
The game database is used to store upcoming games retrieved from the api so that the app can partially function without internet access, which allow the user to see the most recently retrieved set of upcoming games.
The user database acts as a configuration file, storing the name and league preference of the user so that when the app is loaded, details the user previously entered can be used <br>


o Any mobile specific functionalities that have been implemented. <br>




<br>
Reflective Statement
The readme.md file should also include a reflective statement (assessing LO2) (up to 500
words).<br>
Having completed your implementation, you should reflect on the experience. This should
consider details of the tools used and your experience of them, examples of how code
samples from third parties (e.g. generative AI, StackOverflow) were used and the challenges
encountered with their use, what was successful about the project, what could have gone
better, and any lessons you learnt that will be useful for future development projects
