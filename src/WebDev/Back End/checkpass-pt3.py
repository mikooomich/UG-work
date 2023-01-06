# check password async

# import substitution
exec(open("./substitution.py").read())
# exec(open("./substitution.cgi").read())


import cgi, cgitb
import csv
form = cgi.FieldStorage()


# get data
user = form.getvalue('usrname')
password = form.getvalue('passwrd')
thing = str(user) +","+ str(password)


print("Content-Type: text/html\n\n")
# open csv
with open('./usersEncrypted.csv', newline='') as csvfile:
    csvHelp = csv.reader(csvfile)

    # look for the user and password combo
    for i in csvHelp:
        # decrypt the csv data
        decrypted = decrypt(str(i[0]+ "," + str(i[1])))
        print(i)
        if thing == decrypted: 
            print("Your Password matches")
            exit() # stop after found


print("Wrong user or password")

