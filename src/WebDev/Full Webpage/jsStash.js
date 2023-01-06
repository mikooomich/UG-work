// welcome to my javascript monstrosity

const path = "../cgi-bin/login.cgi";
// const path = "../../cgi-bin/login.cgi"; // sand castle


/**
 * load up a new webpage according to page id given
 * 
 * @param to integer of page to switch to 
 */
function switchPage(to) {
    
    // try auto login. page 4 is the past projects page
    if (to == 4) {
        let status = logon("login"); 
        if (status != "err") {
            // ticket login sucess, our work here is done
            return;
        }
    }


    let outDisplay = document.getElementById("waa"); // content body place thing

    let request = new XMLHttpRequest();
    request.addEventListener("readystatechange", eve => {
        if (request.readyState == 4) {
            // set whatever is recieved from back end
            outDisplay.innerHTML = request.responseText;

            // hide the login button if logged in 
            if (sessionStorage.getItem("username") != undefined && sessionStorage.getItem("ticket") != undefined) {
                changeButtonLayout("hide");
            }
        }
    });
        

    request.open("POST", "./home.php");
    // send encoded string
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    request.send("pageTOGO=" + to);
}



 /**
 * verify that a field has content entered
 * auto parse what the error output id should be, parse error message, etc
 * I am using my A4 stuff wrote at 1am, hello
 * 
 * @param thing the field to check
 * 
 */
function verifyField(thing) {
    let content = document.getElementById(thing).value;

    if (content.length <= 0) { // if empty, complain about it
        let out = document.getElementById(thing + "Err");
        out.innerHTML = `${thing} is required`;
        return;
    }

    let out = document.getElementById(thing+"Err");
    out.innerHTML = "";
}


/**
 * for login page elements
 * show/hide login and signup pages, Login and signup page are mutrally exclusive
 */
let hiddenState = false; // hidden status of signup boxes
function changeLayout() {
    var signUp = document.getElementById("signUp");
    var login = document.getElementById("login");

    if (hiddenState == false) {
        signUp.setAttribute("style", `display: none`);
        login.removeAttribute("style", `display: block`);
        hiddenState = true;
        return
    }
    signUp.removeAttribute("style", `display: block`);
    login.setAttribute("style", `display: none`);
    hiddenState = false; 
}


/**
 * hide/show login button, log out button, accordingly
 * 
 * @param showOrHide 
 * @returns 
 */
function changeButtonLayout(showOrHide) {
    try {
        let button = document.getElementById("loginButton");
        let logOUTButton = document.getElementById("logOUTButton");
        let text = document.getElementById("loggedStatus");
        
        if (showOrHide == 'hide') {
            // console.log("hiding")
            text.innerHTML = `Hello, ${sessionStorage.getItem("username")}!`;
            logOUTButton.setAttribute("style", `float: right`);

            button.setAttribute("style", `display: none`);
            return;
        }
        button.removeAttribute("style", `display: block`);
        logOUTButton.setAttribute("style", `float: none`);
        }
    catch {
        console.log("idk why my life is on fire");
    }
}
            
    /**
     * Log out by deleting session storage and redirect to homepage
     */
    function logout() {
        sessionStorage.removeItem("username");
        sessionStorage.removeItem("password");
        sessionStorage.removeItem("ticket");
        switchPage(0); // redirect to home
    }




/**
 * For manual toggle the categories and recent post menu
 * The button acts like a togle, overriding hover action
 */
function navBarShow() {
    var thing = document.getElementById("navContainer");

    if (hiddenState == false) {
        thing.setAttribute("style", `display: inline-block`);
        hiddenState = true;
        return
    }
    thing.removeAttribute("style", `display: none`);
    hiddenState = false; 
}











/**
 * This absolute monstrosity handles account login, creating, and deletion
 * This was not my best idea
 * 
 * @param signupOrLogin what to do. Assume login/signup if not stated to delete
 */
function logon(signupOrLogin) {

    if (signupOrLogin == 'delete') {
        let us = sessionStorage.getItem("username");
        let ps = sessionStorage.getItem("password");
        // console.log(us);
        // console.log(ps);
        
        // prompt for deletion
        let enterUser = prompt("Enter Username");
        let enterPass= prompt("Enter Password");
        // console.log(enterUser);
        // console.log(enterPass);

        // compare entered info with credentials, need to be logged in to work
        if (us === enterUser && ps === enterPass && enterUser != undefined && enterPass != undefined) {
        
            let request = new XMLHttpRequest();
            request.addEventListener("readystatechange", eve => {
                if (request.readyState == 4) {
                    // set whatever is recieved from back end
                    alert(request.responseText);
                
                    if (request.responseText.includes("Deleted account sucessfully")) {
                        logout(); // auto log out
                    }
                }
            });
                
            request.open("POST", path);

            // send encoded string
            request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            request.send("username=" + us + "&" + "password=" + ps + "&" +"create=del");
                
        }
        else {
            alert("You must be signed in *AND* enter correct credentials to delete");
        }
        return;
    }

    
    /**
     * login and sign up
     */
    let user;
    let password;
    let outDisplay;

    let firstname;
    let lastname;
    let email;


    // get info from fields and/or session storage
    try {
        user = document.getElementById(signupOrLogin+"username").value;
        password = document.getElementById(signupOrLogin+"password").value;
        outDisplay = document.getElementById("out");

        firstname = document.getElementById("First Name").value;
        lastname = document.getElementById("Last Name").value;
        email = document.getElementById("Email").value;
    } catch {}

    let us = sessionStorage.getItem("username");
    let ps = sessionStorage.getItem("password");
    let ticket = sessionStorage.getItem("ticket");
    // console.log(us)
    // console.log(ps)
    // console.log(ticket)


    
    let request = new XMLHttpRequest();
    request.addEventListener("readystatechange", eve => {
        if (request.readyState == 4) {
            console.log(request.responseText.split("awooga"))
        
            // switch output to page insted of error field when we get a html page (success login)
            if (request.responseText.includes("header")) {
                outDisplay = document.getElementById("waa");
            
                // store login creds into session storage          
                if (us == undefined && ps == undefined) { // first login, store creds in session storage
                    sessionStorage.setItem("username", user);
                    sessionStorage.setItem("password", password);
                    sessionStorage.setItem("ticket", request.responseText.split("awooga")[1]);
                }
            }
            

            // so that refreshing page doesnt show you are logged out until you click into a menu
            // do not direct to past projects page upon refresh page
            if (signupOrLogin != "loginRefresh") {
                // set whatever is recieved from back end
                // request.responseText is array: [useless stuff, ticket, stuff from backend]
                outDisplay.innerHTML = request.responseText.split("awooga")[2];
            }
            // also set login button hide status after we recieve page
            if (request.responseText.includes("header")) {
                changeButtonLayout('hide');
            }
            
        }
    });
        
    request.open("POST", path);
    // send encoded string
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    
    // send whether ticket, login details or create account details
    if (user == undefined && password == undefined && ticket == undefined) {
        // user not logged in/no details. This is for switchpage function to know if need to pull up login page
        return "err";
    }
    else if (ticket != undefined) {
        request.send("ticket=" + ticket);
    }
    else if (signupOrLogin != "login") {
        request.send("username=" + user + "&" + "password=" + password + "&" + "firstname=" + firstname + "&" + "lastname=" + lastname +"&" + "email=" + email + "&" +"create=yes");
    }
    else { // login, abouve is signup
        request.send("username=" + user + "&" + "password=" + password);
    }
} 
