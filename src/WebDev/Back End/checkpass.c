/**
verify password sync
*/


#include <stdlib.h>
#include <stdio.h>
#include <string.h>
int main() {

    int length = atoi(getenv("CONTENT_LENGTH"));

    // user and pass from cgi string
    char usrName[300];
    char passwrd[300];
    int pointer = 0; // for usrName and passwrd arrays
    int point = 0; // for whole cgi string

    char character;
    int readPassword = 0; // 0 reads as username,1 reads as password

   


    printf("Content-Type: text/html\n\n");
    printf("<html><body>");
 

    // read username and password from cgi
    while ((character = getchar()) != EOF && point < length) {
        point++; // total chars


        if (character == '=' && point < length) { // start reading after = sign
            // the csv does not require %encoding... this code does not account for that :D


            // append to array until end of string marked by &
            while ((character = getchar()) != '&' && point < length) {
                
                if (readPassword == 0) { // read username
                    usrName[pointer] = character;
                }
                else { // read password
                    passwrd[pointer] = character;
                }
                point++; // total chars
                pointer++; // index of array to append to 
            }

                // terminate string
                if (readPassword == 0) {
                    usrName[pointer] = '\0';
                }
                else {
                    passwrd[pointer] ='\0';
                    break;
                }
 
            pointer = 0;
            readPassword++;
        }

    }




 
 



    // manually read from csv
    // read username and password, compare, repeat

    FILE *file = fopen("./users.csv", "r");
    int index = 0;
    char usrToCompare[300];
    char passwordToCompare[300];


    while (character != EOF) {// read until csv empty
        passwordToCompare[300];
        usrToCompare[300];

        
        // read username
        while ((character = fgetc(file)) != EOF) {
            if ((character == ',') || (character =='\n')) {
                // terminate string, reset index
                usrToCompare[index] = '\0';
                index = 0;
                break;
            }

            usrToCompare[index] = character;
            index++;
        }

        // read password
        while ((character = fgetc(file)) != EOF) {
            if ((character == ',') || (character =='\n')) {
                passwordToCompare[index] = '\0';
                index = 0;
                break;
            }
            passwordToCompare[index] = character;
            index++;
        }




    
        // compare user and password
        if (strcmp(usrName, usrToCompare) == 0 && strcmp(passwrd, passwordToCompare) == 0) {
            printf("<p>Your password matches</p>");
            printf("</body></html>");
            return;
        }
    }


    // no matches
    printf("<p>Wrong username or password</p>");
    printf("</body></html>");

}

