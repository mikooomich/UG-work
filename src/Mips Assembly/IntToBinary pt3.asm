#Convert an integer to binary

# t1 is value read from console
# s4 is address of out array pointer
# t0 is for use to get num to print

# for exponet calculating
# $s3 the power of 2 required to hit
# t7 the number of the pwr of 2 to subtrat
# t6 is multiply count of t7


.data

prompt: .asciiz "\nHewwo! Plz enter a positive integer: "


nl: .asciiz "\n"
errMsg: .asciiz "Error, no negative numbers\n"

outArray: .space 33

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



li $s3, 30 # the number of bits of number to process, the line below already loads the 31st bit 
li $t7, 1 # num to subtr
li $t6, 0 # count of times multiplied
la $s4, outArray # address of variable

# print sign bit zero
li $t0, 0
add $t0, $t0, 48
sb $t0, ($s4)
add $s4, $s4, 1
	


	
# the following calculates the power to subtract, by multiplying over and over again
#ex 2^31, 2^30.... etc
# should have just calculated the highest power once and divided by 2, instead of calculating the power each time, appologies for the inefficiency
exponent:
	
	blt $s3, 0, exit # exit program when we have processed all powers of 2
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

	beqz $t1, exit 



next:
	# save to the array
	li $t0, 0
	add $t0, $t0, 48 # 48 is ascii offset for zero
	sb $t0, ($s4)
	add $s4, $s4, 1
	
	
	li $t7, 1 # reset num to subtract
	
	b exponent


subtr:
	# subtract power from input value
	sub $t1, $t1, $t7
	
	
	# save to the array
	li $t0, 1
	add $t0, $t0, 48
	sb $t0, ($s4)
	add $s4, $s4, 1
	

	li $t7, 1 # reset num to subtract
	
	b exponent



exit:
	# prep for printing, treminate with null, load string address
	li $t0, 0
	sb $t0, ($s4)
	la $s4, outArray


	# print out the string (array)
	li $v0, 4
	la $a0, outArray
	syscall

	# go back to re-prompt an input after print new line
	la $a0, nl
	li $v0, 4
	syscall
	
	
	b main



err:
	la $a0, errMsg
	li $v0, 4
	syscall
	# go back to re-prompt an input after print error message
	b main



die:
	li $v0, 10
	syscall