# handles login logic
# past projects page is printed by this python thingy to make sure page cant be accessed without right credentials


path = "../htdocs/A5t/"
# path = "../A5/RbcN2/" # sand castle

dbpath = "./data.db"
# dbpath = "../../data/data.db" # sand castle

import cgi, cgitb
import sqlite3
import random
form = cgi.FieldStorage()



#generate a random ticket and and set client's ticket to the ticket
def ticketGenerate():
    randomTicket = str(int(random.random() *1000000000000000))
    connection = sqlite3.connect(dbpath)
    connection.execute("UPDATE data set ticket ='" + randomTicket +"' WHERE user = '"+user+"'")
    connection.commit()
    connection.close()
    return str(randomTicket)






# get data
user = form.getvalue('username')
password = form.getvalue('password')
ticket = form.getvalue('ticket')
create = form.getvalue('create') # flag to create or delete a user

firstname = form.getvalue('firstname')
lastname = form.getvalue('lastname')
email = form.getvalue('email')


print("Content-Type: text/html\n\n")

# signup
if create == "yes":
    if user != None and password != None and firstname != None and lastname != None and email != None:

        # create table if not existing
        try:
            connection = sqlite3.connect(dbpath)
            connection.execute('''CREATE TABLE data (
            user text NOT NULL UNIQUE,
            password text NOT NULL,
            firstname text NOT NULL,
            lastname text NOT NULL,
            email text NOT NULL,
            ticket text NOT NULL);''') 
            connection.commit() 
            connection.close()
        except Exception as e:
            print(e)
            connection.commit()
            connection.close()


        # create a new user in database
        connection = sqlite3.connect(dbpath)
        cursor = connection.cursor()

        print("aaawooga"+ "aye ya" +"awooga") # I use this for formatting in the fron end JS
        try:
            cursor.execute("INSERT INTO data VALUES (?,?,?,?,?,?)", (user, password, firstname, lastname, email, "0"))
            print("Create success, please return to the login screen and login")

        except Exception as e:
            print(e)
            print("Creation error. Username is taken")
            
        connection.commit()
        connection.close()
    else:
        print("aaawooga"+ "bb" +"awooga")
        print("Please fill in all the fields before submission")


# flag to delete a user
elif create == "del":
    connection = sqlite3.connect(dbpath)
    connection.execute("DELETE FROM data WHERE user = '"+user+"'")
    connection.commit()
    connection.close()
    print("Deleted account sucessfully")
    
    


# login functions, try login with ticket, 
# if no ticket, login with username and password
else:
    connection = sqlite3.connect(dbpath)
    cursor = connection.cursor()

    # try ticket
    if ticket != None:

        print("awooga"+ "aye ya" +"awooga") # I use this for formatting in the front end JS
        cursor.execute("SELECT * FROM data WHERE ticket =:selection", {"selection": ticket})

        try:
            getname = cursor.fetchone()[1] # fails if user is not found
        
            # print out past projects page (because user is found)
            file = open(path+"head.html", "r")
            print(file.read())
            file.close()
            print('''<body bgcolor="#f1f2f6" id="waa" style="display: block;  padding: 0px; margin: 0; min-width: 400px;">''')
            
            file = open(path+"pastProject.html", "r")
            print(file.read())
            file.close()

            print("</body>")
            file = open(path+"foot.html", "r")
            print(file.read())
            file.close()

            # print(getname)

        except Exception as e:
            # prompt to login again, should really never show up
            print("Login Expired, please login again")


            

    #login with username and password
    else: 
        cursor.execute("SELECT * FROM data WHERE user =:selection", {"selection": user})
      
        try:
            getname = cursor.fetchone()[1]
        
            if getname == password:
                # generate a new ticket once correct details
                # then print the new page
                theTicket = ticketGenerate()
                print("aaaawooga"+ theTicket +"awooga") #give client the ticket
              
                file = open(path+"head.html", "r")
                print(file.read())
                file.close()
                print('''<body bgcolor="#f1f2f6" id="waa" style="display: block;  padding: 0px; margin: 0; min-width: 400px;">''')
               
                file = open(path+"pastProject.html", "r")
                print(file.read())
                file.close()

                print("</body>")
                file = open(path+"foot.html", "r")
                print(file.read())
                file.close()


            else:
                print("aaawooga"+ "bb" +"awooga")
                print("Wrong user or password")
                
        except Exception as e:
            print("aaawooga"+ "bb" +"awooga")
            # print(e)
            print("Wrong user or password, or account does not exist")
            