#Convert an integer to binary

.data
prompt: .asciiz "Hewwo! Plz enter: "
nl: .asciiz "\n" # new line character
errMsg: .asciiz "Error, no negative numbers\n"


# Sometimes when you complile and run, the code breaks, then you reset and run it again, everything works. I have no clue
.text


main:
# prompt a request to console, read from console
li $v0, 4
la $a0, prompt
syscall

li $v0, 5
syscall


# handle invalid inputs and stop condition
beqz $v0, die
bltz $v0, err

#load value read into reg t1
la $t1, ($v0) 

# print sign bit zero
li $a0, 0
li $v0, 1
syscall


li $s3, 30 # the number of bits of number to process, the line below already loads the 31st bit 
li $t7, 1 # num to subtr
li $t6, 0 # count of times multiplied




	
# the following calculates the power to subtract, by multiplying over and over again
#ex 2^31, 2^30.... etc
# should have just calculated the highest power once and divided by 2, instead of calculating the power each time, appologies for the inefficiency
exponent:
	
	blt $s3, 0, exit # exit program when we have processed all powers of 2, counting down 
	bge $t6, $s3, loop # break loop when we have the right power of 2
	
	# muliply and incrment times multiplied
	mul $t7, $t7, 2
	add $t6, $t6, 1

b exponent





loop:
	# move onto next power of 2, counting down
	# reset values used to calculate powers
	sub $s3, $s3, 1 
	li $t6, 0 

	# skip subtraction if the power of 2 is greater than the input number
	bge $t1, $t7, subtr
	blt $t1, $t7, next 

	beqz $t1, exit # no more powers of 2 to process



next:
	#print 0
	li $a0, 0
	li $v0, 1
	syscall
	li $t7, 1 # reset num to subtract
	
	b exponent


subtr:
	# print 1, subtract power from input value
	sub $t1, $t1, $t7
	li $a0, 1
	li $v0, 1
	syscall
	li $t7, 1 # reset num to subtract
	
	b exponent



exit:
	# go back to re-prompt an input after print new line
	la $a0, nl
	li $v0, 4
	syscall
	b main


err:
	# go back to re-prompt an input after print error message
	la $a0, errMsg
	li $v0, 4
	syscall
	b main



die:
	li $v0, 10
	syscall
