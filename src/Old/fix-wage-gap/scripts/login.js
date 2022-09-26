/** Michael */
const inputtedUsername = document.getElementById("usernameBox")
const inputtedPassword = document.getElementById("passwordBox")
const inputtedPasswordConfirm = document.getElementById("confirmPasswordBox")
const inputtedEmail = document.getElementById("emailBox")
const inputtedDOB = document.getElementById("birthdayBox")
var db = openDatabase('mydb', '1.0', 'credentials', 2 * 1024 * 1024);

let regesteredCredentials = [] // array to to store the data copied from database

function linearSearch(array, thingToSearch) {
  /**
   * Searches for a username inside a given array with the linear search method
   * 
   * @param {array} An array
   * @param {string} The username to search for
   * 
   * @return {number} The index of the item in the array
   * @return {number} -1 if the item is not found
   * 
   */

  for (i in array) {
    // console.log(i)
    if (array[i].id == thingToSearch) {
      return parseInt(i)
    } 
  }
  return -1
}



function getCredentials() {
   /**
   * Handles login requests. Retrieves username and password from the page and searches a database for the specified combos. Tells the user if they successfully logged in or not.
   * 
   * 
   */

  console.log('Login has been summoned')
  db.transaction(function (tx) { 
    tx.executeSql('CREATE TABLE IF NOT EXISTS credentials (id unique, password, email, dob)');
    tx.executeSql('SELECT * FROM credentials', [], function (tx, results) {
      
      var len = results.rows.length, i;
      // window.alert(len)
      // check if database has at least one entry or else it will break.  
      if (len <= 0) { // on first run insert a blank entry into the database so the program. This should only be ran once. 
        destroy(false)
        getCredentials() // run the function again but this time with a working database
      }

      else { 
        for (i = 0; i < len; i++){
          console.log(results.rows);
          regesteredCredentials = results.rows // copy data to array 
          console.log(regesteredCredentials)

          // look up the username in the database. 
          var userCredentials = linearSearch(regesteredCredentials, inputtedUsername.value)
          // console.log(userCredentials)

          if (userCredentials !== -1) { // check if username is regestered

            if (regesteredCredentials[userCredentials].password == inputtedPassword.value) { // check if password corelates to username
              document.getElementById("outputBox").style.color = "#00ff00";
              document.getElementById("outputBox").innerText = 'Logged in successfully!'
              console.log('Login successfully')
              //Kelvin's part I added this to help customise the user's login stuff like said in the WBS. There wasn't much i could do, since the account feature doesn't really do anything at this moment
              alert("welcome " + inputtedUsername.value + " to fix the wage gap!!!")
              setTimeout(() => { // give people time to see before it redirects               
                window.location.href = "/database.html";
              }, 1000);
              break
            }

            else { // password does not corelate with username
              document.getElementById("outputBox").style.color = "#ff0000";
              document.getElementById("outputBox").innerText = 'Incorrect user or password'
              console.log('Password does not corelate with username')
            }
          }

          else { // username does not exist
            document.getElementById("outputBox").style.color = "#ff0000";
            document.getElementById("outputBox").innerText = 'Incorrect user or password'
            console.log('Username doesnt exist')
          }
        }
      }

    }, null);
  });
}



function makeCredentials() {
  /**
   * Takes in information from a form and "creates the account" then enters it into a database so the user can login to the website later on
   * 
   */
  console.log('Create account has been summoned')
  db.transaction(function (tx) {
    console.log("c")
    tx.executeSql('CREATE TABLE IF NOT EXISTS credentials (id unique, password, email, dob)');
    tx.executeSql('SELECT * FROM credentials', [], function (tx, results) {

      var len = results.rows.length, i; // how many entries of data we have
      // window.alert(len)

      // check if database has at least one entry. if it 
      if (len <= 0) { // No entries. Prepare the database for action. This should only be ran once. 
        destroy(false)
        // run the create account again once database has reset
        makeCredentials() 
      }

      else { // there is at least one entry

        for (i = 0; i < len; i++) { 

          // console.log(results.rows);
          regesteredCredentials = results.rows // copy data to array 
          // console.log(regesteredCredentials)

          // look up the username in the database. 
          var userCredentials = linearSearch(regesteredCredentials, inputtedUsername.value)

          // is the username not blank AND not taken? 
          if (userCredentials == -1 && inputtedUsername.value !== '') { 
            if (inputtedPassword.value == inputtedPasswordConfirm.value) { // do passwords match?
              // write to account databases, then redirect user to login page
              db.transaction(function (tx) {
                tx.executeSql('CREATE TABLE IF NOT EXISTS credentials (id unique, password, email, dob)');
                tx.executeSql('INSERT INTO credentials (id, password, email, dob) VALUES (?, ?, ?, ?)', [inputtedUsername.value, inputtedPassword.value, inputtedEmail.value, inputtedDOB.value]);
              });
              // console.log('Redirecting to login page...')
              document.getElementById("outputBox").style.color = "#00ff00";
              document.getElementById("outputBox").innerText = 'Account created! Redirecting to login page...'
              console.log('Successful account creation')
              setTimeout(() => {                
                window.location.href = "account.html"; 
              }, 3000);
            }

            else { // password and conformation password do not match, complain about it
              document.getElementById("outputBox").style.color = "#ff0000";
              document.getElementById("outputBox").innerText = 'Passwords do not match'
              console.log('Passwords do not match')
            }
          }

          else { // user did not input a username or inputted a taken username, complain about it
            document.getElementById("outputBox").style.color = "#ff0000";
            document.getElementById("outputBox").innerText = 'Invalid account information'
            console.log("Cannot create account with invalid account information")
          }
        }
      }

    }, null);
  });
}

function create() {
   /**
   * Redirects to create account page
   * 
   * @param none
   * 
   * @return none
   * 
   */
  window.location.href = "createAccount.html"
}

function destroy(windowAlert = true) {
 /**
   * Brutally destroys the database
   * 
   * @param {boolean} enable/disable a popup message
   * 
   * @return none
   * 
   */
  db.transaction(function (tx) {
    tx.executeSql('DROP TABLE credentials');
    tx.executeSql('CREATE TABLE IF NOT EXISTS credentials (id unique, password, email, dob)');
    tx.executeSql('INSERT INTO credentials (id, password, email, dob) VALUES ("0", "0", "0", "0")'); // input an entry after reset or else something will break
    if (windowAlert == true) {
      window.alert("Account storage destroyed. You were told to not click this. Now look what you've done.") // send a popup as this is the best way to get someone's attention as they were instructed in the tutorial video specifically to not click this
    }
  });
}







