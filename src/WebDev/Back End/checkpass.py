
# check password sync


import cgi, cgitb
import csv
form = cgi.FieldStorage()


# get data
user = form.getvalue('usrname')
password = form.getvalue('passwrd')
thing = [user, password]


print("Content-Type: text/html\n\n")

# open csv
with open('./users.csv', newline='') as csvfile:
    csvHelp = csv.reader(csvfile)

    # look for the user and password combo
    for i in csvHelp:
        if thing == i: 
            print("Your Password matches")
            exit() # stop after found


print("Wrong user or password")
