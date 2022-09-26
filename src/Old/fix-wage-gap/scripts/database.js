//Kelvin's work
var listOfPeople = [];

//Allows script to access the html elements
const dynamicPage = document.getElementById("dynamicPage");
const submitButtonActivate = document.getElementById("submitButton");

/**
 * Takes a one-dimensional array and dynamically shows them on the database tab.
 * Basically refreshes the page with information from a list
 * 
 * @param {array} newList - An array of objects that will be shown on the database screen
 */
function refreshTable(newList){
  //Clears the previous data from the div
  console.info("clearing page of all previous cards");
  dynamicPage.innerHTML = ""

  //Takes in each object and creates a card for each object
  for (var i = 0; i < newList.length; i++){
    //adds the border of the card
    console.info("Created border of the dynamic card");
    borderDiv =  document.createElement("div");
    borderDiv.classList= "border";

    discriminatationTags = document.createElement("ul");
    discriminatationTags.classList = "discriminationTags";
    
    //adds first and last names to the card
    console.info("Created first and last name in the dynamic card");
    fullName= document.createElement("li");
    fullName.innerHTML =  newList[i].firstName + " " + newList[i].lastName
    discriminatationTags.append(fullName);
    
    //adds the age of the user to the card
    console.info("Added age to the dynamic card");
    age= document.createElement("span");
    age.innerHTML =  newList[i].age + " years old"
    discriminatationTags.append(age);

    //adds the ethnicity of the user if specified
    if(newList[i].ethnicity != "Not Selected"){
      console.info("Added ethnicity to the dynamic card");
      ethnicity= document.createElement("span");
      ethnicity.innerHTML =  newList[i].ethnicity
      discriminatationTags.append(ethnicity);
    }

    //adds the religion of the user if specified
    if(newList[i].religion != "Not Selected" & newList[i].religion != "Other"){
      console.info("Added religion to the dynamic card");
      religion= document.createElement("span");
      religion.innerHTML =  newList[i].religion
      discriminatationTags.append(religion);
    }

    //adds the gender of the user if specified
    if(newList[i].gender != "Not Selected"){
      console.info("Added gender to the dynamic card");
      gender= document.createElement("span");
      gender.innerHTML =  newList[i].gender
      discriminatationTags.append(gender);
    };

    //adds the sexual orientation of the user if specified
    if(newList[i].sOrientation != "Not Selected"){
      console.info("Added sexual orientation to the dynamic card");
      sOrientation = document.createElement("span");
      sOrientation.innerHTML =  newList[i].sOrientation
      discriminatationTags.append(sOrientation);
    };
   
    borderDiv.append(discriminatationTags);

    //adds the job title of the user
    console.info("Added name of job to the dynamic card");
    jobDiv = document.createElement("p");
    jobDiv.classList = "job";
    jobDiv.innerHTML = newList[i].job + ", at " + newList[i].company + " for " + newList[i].yearsOfWork + " years."
    borderDiv.append(jobDiv);

    //adds the job income of the user
    console.info("Added the job income to the dynamic card");
    incomeDiv = document.createElement("p");
    incomeDiv.classList = "income";
    incomeDiv.innerHTML = "Salary: $" + newList[i].income + "/per year"
    borderDiv.append(incomeDiv);
    
    //adds the job description of the user
    console.info("Added the job requirements to the dynamic card");
    infoDiv = document.createElement("p");
    infoDiv.classList = "info";
    infoDiv.innerHTML = newList[i].experience 
    borderDiv.append(infoDiv);

    dynamicPage.append(borderDiv);
 
  }

  //Gives out an error message if there is nothing in the list/ nothing fit the user's search criteria
  if (newList.length == 0){
    console.info("Couldn't find the specified job from the search criteria");
    errorMessage = document.createElement("p");
    errorMessage.innerHTML = "Sorry, no one matches your criteria. :'("
    dynamicPage.append(errorMessage);

  }

};

/**
 * Takes in data from .json and displays it on the database screen
 */
fetch('sample_people.json')
    .then(res => res.json())
    .then((out) => {
        console.log('Output: ', out);
        Array.prototype.push.apply(listOfPeople, out)
        applyingFilters()
}).catch(err => console.error(err));

/**
 * Creates a sample array of people, so that you can see how the cards in the database will be laid out
 * 

for (var i = 0; i < 5; i++){
  let firstName = ['Kelvin', 'Micheal', 'David', 'Bob', 'Joe', 'Peter', 'Harry', 'Jeff', 'Alex', 'Homer'];
  let lastName = ['Smith', 'Geller', 'Jackson', 'Bobby', 'Greene', 'Stark', 'Parker', 'Lang', 'Rogers', 'Natasha'];
  let company = ["Google", "Microsoft", "Amazon", "McDonalds", "Harveys", "Walmart", "Staples", "Best Buy", "Target", "Starbucks"];
  let experience = ["Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras pretium placerat mi, nec rutrum leo luctus ut. Ut molestie quam in metus lobortis lobortis. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Donec nec ex in tortor varius pretium nec vitae risus. In semper, eros ac pharetra vestibulum, felis nunc vulputate odio, nec tincidunt diam metus nec felis. Sed vel est tincidunt, eleifend ante ut, tincidunt diam. In lobortis augue porttitor, varius neque sed, auctor lacus."];
  let job = ['Regional Manager', 'Website Developer', 'Warehouse manager', 'Janitor', 'Receptionist', 'Manager', 'Employee', 'Product tester', 'Accountant', 'Marketing lead'];
  let income = randomNumberGenerator(10000,500000)
  let yearsOfWork = randomNumberGenerator(1,30)
  let age = randomNumberGenerator(30,97)
  let ethnicity = ["Not Selected", "Caucasian", "West Asian", "Latin American", "Southeast Asian", "Arab", "Filipino", "African American", "Korean", "Japanese", "Chinese", "Visible minority, other","Multiple visible minorities"];
  let religion = ["Not Selected", "Christian", "Muslim", "Hindu", "Sikh", "Buddhist", "Jewish", "No religious affliation", "Other"];
  let gender = ["male", "female", "transgender", "gender neutral", "non-binary", "agender", "pangender", "genderqueer", "two-spirit", "third gender", "Not Selected"];
  let sOrientation = ["Not Selected", "heterosexual", "homosexual", "bisexual", "asexual"];
  let newPerson = new People(job[randomNumberGenerator(0,10)], income, experience, company[randomNumberGenerator(0,10)], firstName[randomNumberGenerator(0,10)], lastName[randomNumberGenerator(0,10)], yearsOfWork, age, ethnicity[randomNumberGenerator(0,13)], religion[randomNumberGenerator(0,9)], gender[randomNumberGenerator(0,11)], sOrientation[randomNumberGenerator(0,5)]);
  listOfPeople.push(newPerson);
};
 */

/**
 * Takes in a max and min income and filters out the specified range in the list
 * 
 * @param {array} array - The array that the function will sort through
 * @param {number} max - The lowest income the person can have
 * @param {number} min - The highest income the person can have
 * @returns {array} - Returns the filtered array with the specified range of incomes
 */
function filterByIncome(array, max, min){
  console.info("Filtering income")
  return array.filter(function(person) {
    if (person.income <= max & person.income >= min){
      return true
    }
    return  false
  })
}

/**
 * Takes in age of a person and filters out ages that aren't close to the specified age
 * 
 * @param {array} array - The array that the function will sort through
 * @param {number} age - The specified age that want to be filtered
 * @returns {array} - Returns the filtered array with people with ages that are at most two years away from the specified age
 */
function filterByAge(array, age){
  console.info("Filtering age")
  return array.filter(function(person) {
    if (age <= person.age + 2 & age >= person.age - 2){
      return true
    }
    return  false
  })
}

/**
 * Takes in years of experience of a person and filters out people without similar years of experience
 * 
 * @param {array} array - The array that the function will sort through
 * @param {number} experience - The specified number of years the person must have to be in the new array
 * @returns {array} - Returns the filtered array with people that have similar years of experience at the company
 */
function filterByExperience(array, experience){
  console.info("Filtering years of experience")
  return array.filter(function(person) {
    if (experience <= person.yearsOfWork + 2 & experience >= person.yearsOfWork - 2){
      return true
    }
    return  false
  })
}

/**
 * Takes in a person's ethnicity and looks for people with the same ethnicity in the array
 * 
 * @param {array} array - The array that the function will sort through
 * @param {string} chosenEthnicity - The chosen ethnicity that the function will look for in the array
 * @returns {array} - Returns the filtered array with people that have the same ethnicity
 */
function filterEthnicity(array, chosenEthnicity){
  console.info("Filtering ethnicities")
  return array.filter(function(person) {
    if (person.ethnicity == chosenEthnicity){
      return true
    }
    return  false
  })
}

/**
 * Takes in a person's religion and looks for people with the same religion in the array
 * 
 * @param {array} array - The array that the function will sort through
 * @param {string} chosenReligion - The chosen religion that the function will look for in the array
 * @returns {array} - Returns the filtered array with people that have the same religion
 */
function filterReligion(array, chosenReligion){
  console.info("Filtering religion")
  return array.filter(function(person) {
    if (person.religion == chosenReligion){
      return true
    }
    return  false
  })
}

/**
 * Takes in a person's gender and looks for people with the same gender in the array
 * 
 * @param {array} array - The array that the function will sort through
 * @param {string} chosenGender - The chosen gender that the function will look for in the array
 * @returns {array} - Returns the filtered array with people that have the same gender
 */
function filterGender(array, chosenGender){
  console.info("Filtering gender")
  return array.filter(function(person) {
    if (person.gender == chosenGender){
      return true
    }
    return  false
  })
}

/**
 * Takes in a person's sexual orientation and looks for people with the same orientation in the array
 * 
 * @param {array} array - The array that the function will sort through
 * @param {string} chosenOrientation - The chosen sexual orientation that the function will look for in the array
 * @returns {array} - Returns the filtered array with people that have the same orientation
 */
function filterOrientation(array, chosenOrientation){
  console.info("Filtering sexual orientation")
  return array.filter(function(person) {
    if (person.sOrientation == chosenOrientation){
      return true
    }
    return  false
  })
}

/**
 * Takes in a query from a search bar and searches through an array for the jobs and companies that match their query
 * 
 * @param {array} array - The array that the function will sort through
 * @param {string} query - The query that the user has written
 * @returns {array} - Returns the filtered array with objects that contain letters in their query
 */
function filterItems(array, query) {
  console.info("Filtering queries from a search bar")
  return array.filter(function(person) {
    if (person.job.toLowerCase().indexOf(query.toLowerCase()) !== -1)
      return true
    if (person.company.toLowerCase().indexOf(query.toLowerCase()) !== -1)
      return true
    if (person.firstName.toLowerCase().indexOf(query.toLowerCase()) !== -1)
      return true
    if (person.lastName.toLowerCase().indexOf(query.toLowerCase()) !== -1)
      return true
    return false
  })
}

/**
 * Retrieves data from the user and filters an array using the user inputs. Then it takes this filtered array and displays it on the screen
 */
function applyingFilters() {

  var maxIncome = $("#maxIncome").val();
  var minIncome = $("#minIncome").val();

  // gives the max/min income some default numbers incase the user doesn't want to filter using this filter. Makes sure everything is shown
  if (maxIncome == ''){
    console.info("using default value for max income (999999999999)")
    maxIncome = 999999999999;
  }

  if (minIncome == ''){
    console.info("using default value for min income (0)")
    minIncome = 0;
  }
  
  var modifiedList = filterByIncome(listOfPeople, maxIncome, minIncome);

  //filters ethnicity in the database
  var chosenEthnicity = $("#ethnicity").val();
  if (chosenEthnicity != 'Not Selected'){
    var modifiedList = filterEthnicity(modifiedList, chosenEthnicity)
  }

  //filters religion in the database
  var chosenReligion = $('#religion').val()
  if (chosenReligion != 'Not Selected'){
    var modifiedList = filterReligion(modifiedList, chosenReligion)
  }

  //filters gender in the database
  var chosenGender = $('#gender').val()
  if (chosenGender != 'Not Selected'){
    var modifiedList = filterGender(modifiedList, chosenGender)
  }

  //filters sexual orientation in the database
  var chosenOrientation = $('#sOrientation').val()
  if (chosenOrientation != 'Not Selected'){
    var modifiedList = filterOrientation(modifiedList, chosenOrientation)
  }
  
  //filters age in the database
  var chosenAge = $('#age').val()
  if (chosenAge != ''){
    var modifiedList = filterByAge(modifiedList, chosenAge)
  }
  
  //filters years of experience in the database
  var chosenExperience = $('#yearsOfExperience').val()
  if (chosenExperience != ''){
    var modifiedList = filterByExperience(modifiedList, chosenExperience)
  }

  //filters out specified search terms in the database
  var searchTerm = $('#searchBox').val()
  if (searchTerm != ''){
    var modifiedList = filterItems(modifiedList, searchTerm)
  }

  var sortingCriteria = $('#sorting').val()

  //allows user to sort database from a-z
  if (sortingCriteria == "Name (A-Z)"){
    console.info("Sorted by name (a-z)")
    modifiedList.sort((a, b) => (a.firstName + a.lastName).localeCompare(b.firstName + b.lastName))
  }

  //allows user to sort database from z-a
  if (sortingCriteria == "Name (Z-A)"){
    console.info("Sorted by name (z-a)")
    modifiedList.sort((a, b) => (b.firstName + b.lastName).localeCompare(a.firstName + a.lastName))
  }

  //allows user to sort database from ascending age
  if (sortingCriteria == "Age (Ascending)"){
    console.info("Sorted by age (ascending)")
    modifiedList.sort((a, b) => a.age - b.age)
  }

  //allows user to sort database from descending age
  if (sortingCriteria == "Age (Descending)"){
    console.info("Sorted by age (descending)")
    modifiedList.sort((a, b) => b.age - a.age)
  }

  //allows user to sort database from ascending income
  if (sortingCriteria == "Income (Ascending)"){
    console.info("Sorted by income (ascending)")
    modifiedList.sort((a, b) => a.income - b.income)
  }

  //allows user to sort database from descending income
  if (sortingCriteria == "Income (Descending)"){
    console.info("Sorted by income (descending)")
    modifiedList.sort((a, b) => b.income - a.income)
  }

  //allows user to sort database from ascending years of experience
  if (sortingCriteria == "Years of Experience (Ascending)"){
    console.info("Sorted by years of experience (ascending)")
    modifiedList.sort((a, b) => a.yearsOfWork - b.yearsOfWork)
  }

  //allows user to sort database from descending years of experience
  if (sortingCriteria == "Years of Experience (Descending)"){
    console.info("Sorted by years of experience (descending)")
    modifiedList.sort((a, b) => b.yearsOfWork - a.yearsOfWork)
  }

//Sends the filtered array to the refreshtable function so that everything gets displayed
  refreshTable(modifiedList)
}

/**
 * Applies the user's search terms every time they type, so they get live updates and don't need to press a button
 */
$( "input" )
  .keyup(applyingFilters)

/**
 * Applies the users filter everytime they change a filter term. 
 */
$( "select" )
  .change(applyingFilters)

/**
 * Takes in info from the contribute section of the page. Work in progress - depends if we have time to work on the live database section of the website (which is part of the wish list)
 */
submitButtonActivate.addEventListener("click", () => {
  let job = $('#addedJob').val()
  let income = $('#addedIncome').val()
  let experience = $('#addedExperience').val()
  let company = $('#addedCompany').val()
  let firstName = $('#addedFirstName').val()
  let lastName = $('#addedLastName').val()
  let yearsOfWork = $('#addedYearsOfExperience').val()
  let age = $('#addedAge').val()
  let ethnicity = $('#addedEthnicity').val()
  let religion = $('#addedReligion').val()
  let gender = $('#addedGender').val()
  let sOrientation = $('#addedSOrientation').val()

  addedInfo = new People (job, income, experience, company, firstName, lastName, yearsOfWork, age, ethnicity, religion, gender, sOrientation)

  listOfPeople.push(addedInfo)

  window.location.href = "/database.html";
});

