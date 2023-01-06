# simple implementation of substitution cypher



def encrypt(string):
    finalString = []
    # iterate over every letter in string
    for i in range(len(string)):
        found = 0 # if character is in alphabet or not
        
        # iterate alphabet, add to encoded string list
        for char in range(len(result)):
            if result[char][0] == string[i]:
                finalString.append(result[char][1])
                found = 1
                break
      
        # no matches, dont encode
        if found == 0:
            finalString.append(string[i])

    return ''.join(finalString)




def decrypt(string):
    finalString = []
    
    for i in range(len(string)):
        found = 0
        # iterate alphabet
        for char in range(len(result)):
            if result[char][1] == string[i]:
                finalString.append(result[char][0])
                found = 1
                break

        if found == 0:
            finalString.append(string[i])
 
    return ''.join(finalString)



    





key = ""
alphabet = "abcdefghijklmnopqrstuvwxyz"

# [['a', 'e'], ['b', 'z'], ['c', 's'].... etc
result = []



# read arguments, use quotes plz
# sub.py -<e/d> "<what you want to process>" <key>
import sys
args = sys.argv

try:
    key = str(args[3])
    operation = args[1]
    text = args[2]
except:
    key = "ezsflrxjovgbikyqahwctmpdnu" # default key when no args provided


    



    




# build "result" array used to put together letter with equivilant letter
for i in range(len(key)):
    result.append([alphabet[i], key[i]])


# for if command line arguments are provided
idkHowToHaveAnEmptyCatch = 0
try:
    if operation == "-e":
        print(encrypt(text))
    elif operation == "-d":
        print(decrypt(text))
    else:
        print("Invalid argument")
except:
    idkHowToHaveAnEmptyCatch = 0
